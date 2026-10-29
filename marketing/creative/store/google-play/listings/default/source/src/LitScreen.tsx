import React, { useLayoutEffect, useRef } from "react";
import { cancelRender, continueRender, delayRender, staticFile } from "remotion";
import { mount, setup } from "../../../../../../../ui/src/embed.js";
import { PALETTE } from "./theme";

// One-time setup: the kit's fonts, Material Web and the asset base Remotion serves public/ from.
const assetBase = staticFile("fonts/GoogleSansFlex.ttf").replace("fonts/GoogleSansFlex.ttf", "");
export const ready = setup({ assetBase, marketingAssetBase: `${assetBase}material-next/`, palette: PALETTE, theme: "light" })
  .then(() => document.fonts.ready);

/** Holds the still until the kit's fonts are loaded, for compositions that draw text of their own. */
export const useFonts = () => {
  useLayoutEffect(() => {
    const handle = delayRender("Loading fonts");
    ready.then(() => continueRender(handle), cancelRender);
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
  style?: React.CSSProperties;
}> = ({ id, state = {}, frame = true, window = "phone", style }) => {
  const ref = useRef<HTMLDivElement>(null);
  const key = JSON.stringify(state);
  useLayoutEffect(() => {
    const handle = delayRender(`Rendering ${id}`);
    ready
      // No-op navigation: a still never follows a tap. Screenshots never show ads.
      .then(() => mount(ref.current!, id, { go: () => {}, set: () => {}, ...JSON.parse(key) }, { frame, window, showAds: false }))
      .then(() => Promise.all([...ref.current!.querySelectorAll("img")].map((img) => img.decode())))
      .then(() => continueRender(handle), cancelRender);
  }, [id, key, frame, window]);
  return <div ref={ref} style={style} />;
};
