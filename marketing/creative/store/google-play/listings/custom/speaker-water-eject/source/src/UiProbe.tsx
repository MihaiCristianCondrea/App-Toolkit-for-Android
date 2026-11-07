import React from "react";
import { AbsoluteFill } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { UI_SELECTORS } from "./composition/uiSelectors";

/**
 * Measures one screen's critical UI head on and unscaled, in the device frame's CSS px (origin at the
 * frame's top left corner), and logs it as `UI <screen> <json>`. With `discover`, it also logs every
 * sizeable element so selectors can be chosen. scripts/probe-ui.mjs collects the results into
 * src/composition/ui.json, which the layout solver projects through the camera.
 */
export const UiProbe: React.FC<{ screen: string; discover?: boolean }> = ({ screen, discover }) => {
  useFonts();
  const [id, state] = SCREENS[screen];
  const measure = (host: HTMLElement) => {
    const frame = host.querySelector("mnw-device-frame")!.getBoundingClientRect();
    const rect = (el: Element) => {
      const r = el.getBoundingClientRect();
      return [r.left - frame.left, r.top - frame.top, r.right - frame.left, r.bottom - frame.top].map((n) => Math.round(n * 100) / 100);
    };
    const screenEl = host.querySelector("mnw-android-screen") as HTMLElement;
    const find = (selector: string): Element | null => {
      if (selector.startsWith("text:")) {
        const [tag, text] = selector.slice(5).split("|");
        return [...host.querySelectorAll(tag)].find((el) => (el.textContent ?? "").trim().startsWith(text)) ?? null;
      }
      const [css, n] = selector.split("@");
      return host.querySelectorAll(css)[Number(n ?? 0)] ?? null;
    };
    const box = (name: string, selector: string) => {
      const boxes = selector.split(" + ").map((part) => {
        const el = find(part);
        if (!el) throw new Error(`UI probe: "${name}" (${part}) is missing on ${screen}`);
        return rect(el);
      });
      return [Math.min(...boxes.map((b) => b[0])), Math.min(...boxes.map((b) => b[1])), Math.max(...boxes.map((b) => b[2])), Math.max(...boxes.map((b) => b[3]))];
    };
    const spec = UI_SELECTORS[screen];
    const pick = (group: Record<string, string> = {}) => Object.fromEntries(Object.entries(group).map(([name, selector]) => [name, box(name, selector)]));
    const out: Record<string, unknown> = { screen: rect(screenEl), critical: pick(spec?.critical), secondary: pick(spec?.secondary) };
    if (discover) {
      out.elements = [...host.querySelectorAll("*")].map((el) => [el.tagName.toLowerCase(), typeof el.className === "string" ? el.className : "", (el.children.length ? "" : (el.textContent ?? "").trim().slice(0, 28)), rect(el)] as const)
        .filter(([, , , r]) => (r[2] - r[0]) * (r[3] - r[1]) > 600 && r[1] >= 0 && r[1] < 990)
        .slice(0, 160);
    }
    console.log(`UI ${screen} ${JSON.stringify(out)}`);
  };
  return (
    <AbsoluteFill style={{ background: "#fff" }}>
      <LitScreen id={id} state={state} style={{ width: 465.23 }} measure={measure} />
    </AbsoluteFill>
  );
};
