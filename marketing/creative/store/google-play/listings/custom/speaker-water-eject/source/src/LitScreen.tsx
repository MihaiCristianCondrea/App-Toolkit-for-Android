import React, { useLayoutEffect, useRef } from "react";
import { cancelRender, continueRender, delayRender, staticFile } from "remotion";
import { mount, setup } from "../../../../../../../../ui/src/embed.js";
import { PALETTE } from "./theme";

// One-time setup: the kit's fonts, Material Web and the asset base Remotion serves public/ from.
const assetBase = staticFile("fonts/GoogleSansFlex.ttf").replace("fonts/GoogleSansFlex.ttf", "");
export const ready = setup({ assetBase, marketingAssetBase: `${assetBase}material-next/`, palette: PALETTE, theme: "light" })
  .then(() => document.fonts.ready);

/** Holds the still until the kit's fonts are loaded, for compositions that draw text of their own. */
export const useFonts = () => {
  useLayoutEffect(() => {
    const handle = delayRender("Loading fonts");
    ready.then(() => new Promise<void>(resolve => requestAnimationFrame(() => requestAnimationFrame(() => resolve()))))
      .then(() => continueRender(handle), cancelRender);
  }, []);
};

/**
 * A marketing/ui screen in a given state, in its device frame or bare, at a kit window ("phone", "fold" or "desktop").
 * The still waits until every Material Web element has finished updating.
 */
export const LitScreen: React.FC<{
  id: string;
  state?: Record<string, unknown>;
  frame?: boolean;
  window?: string;
  pose?: { yaw: number; pitch: number };
  detail?: "sheet" | "guidance";
  safeBounds?: {left:number;right:number;top:number;bottom:number};
  style?: React.CSSProperties;
}> = ({ id, state = {}, frame = true, window = "phone", pose = { yaw: 0, pitch: 0 }, detail, safeBounds, style }) => {
  const ref = useRef<HTMLDivElement>(null);
  const key = JSON.stringify(state);
  useLayoutEffect(() => {
    const handle = delayRender(`Rendering ${id}`);
    ready
      // No-op navigation: a still never follows a tap. Screenshots never show ads.
      .then(() => mount(ref.current!, id, { go: () => {}, set: () => {}, ...JSON.parse(key), waterCapture: true }, { frame, window, showAds: false }))
      .then(async () => { ref.current!.querySelectorAll("mnw-android-screen").forEach(screen => screen.removeAttribute("interactive")); await document.fonts.ready; await Promise.all([...ref.current!.querySelectorAll("mnw-device-frame")].map(async (frame: any) => { frame.yaw = pose.yaw; frame.pitch = pose.pitch; frame.finish = "porcelain"; await frame.updateComplete; if (frame.device === "pixel_9_pro_xl" && !frame.shadowRoot?.querySelector("img.phone")) throw new Error("The 3D Pixel picture is unavailable; refusing a fallback capture."); })); })
      .then(() => Promise.all([...ref.current!.querySelectorAll("img")].map((img) => img.decode())))
      .then(async () => {
        const host=ref.current!;
        if(detail){
          const screen=host.querySelector("mnw-android-screen") as HTMLElement;
          const first=host.querySelector(detail==="sheet"?".tk-sheet-content":".water-eject p") as HTMLElement;
          const last=host.querySelector(".water-note") as HTMLElement;
          if(!screen||!first||!last) throw new Error("Native Water Eject detail is unavailable.");
          const sr=screen.getBoundingClientRect(),factor=sr.width/screen.offsetWidth;
          const surface=detail==="sheet"?screen.shadowRoot?.querySelector(".sheet"):null;
          if(detail==="sheet"&&!surface)throw new Error("Native sheet surface is unavailable.");
          const start=surface?surface.getBoundingClientRect().top:first.getBoundingClientRect().top-24*factor;
          const top=(start-sr.top)/factor;
          const height=(last.getBoundingClientRect().bottom-start)/factor+24;
          host.style.height=height+"px";
          host.style.overflow="hidden";
          host.style.borderRadius="28px";
          screen.style.transform=`translateY(${-top}px)`;
          await new Promise<void>(resolve=>requestAnimationFrame(()=>requestAnimationFrame(()=>resolve())));
        }
        if(safeBounds){
          const root=host.closest("[data-shot]")!.getBoundingClientRect();
          const model=host.querySelector("mnw-device-frame");
          const visible=detail?host:model?.shadowRoot?.querySelector("img.phone");
          if(!visible)throw new Error("No visible subject to bounds-check.");
          const box=visible.getBoundingClientRect(),s=safeBounds;
          const bounds={left:box.left-root.left,right:box.right-root.left,top:box.top-root.top,bottom:box.bottom-root.top};
          if(bounds.left<s.left||bounds.right>s.right||bounds.top<s.top||bounds.bottom>s.bottom)
            throw new Error(`Subject outside composition safe bounds: ${JSON.stringify(bounds)}`);
        }
      })
      .then(() => continueRender(handle), cancelRender);
  }, [id, key, frame, window, pose.yaw, pose.pitch, detail]);
  return <div ref={ref} style={style} />;
};
