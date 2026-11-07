import React from "react";
import { AbsoluteFill } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";

/**
 * Measures the hero screen's Water Eject UI, head on and unscaled, in the device frame's own CSS px
 * (origin at the frame's top left corner). scripts/probe-hero.mjs writes the result to src/hero-ui.json,
 * which the hero solver projects through the camera.
 */
export const HeroProbe: React.FC = () => {
  useFonts();
  const [id, state] = SCREENS["water-eject"];
  const measure = (host: HTMLElement) => {
    const frame = host.querySelector("mnw-device-frame")!.getBoundingClientRect();
    const rect = (el: Element | null | undefined) => {
      if (!el) throw new Error("Hero probe: a measured element is missing");
      const r = el.getBoundingClientRect();
      return [r.left - frame.left, r.top - frame.top, r.right - frame.left, r.bottom - frame.top].map((n) => Math.round(n * 100) / 100);
    };
    const screen = host.querySelector("mnw-android-screen") as HTMLElement;
    const eject = host.querySelector(".water-eject")!;
    const out = {
      frame: [frame.width, frame.height],
      screen: rect(screen),
      sheet: rect(screen.shadowRoot?.querySelector(".sheet")),
      content: rect(host.querySelector(".tk-sheet-content")),
      visual: rect(eject.querySelector(".water-visual")),
      readout: rect(eject.querySelector(".water-readout")),
      instructions: rect(eject.querySelector(":scope > p:not(.water-note)")),
      button: rect(eject.querySelector("md-filled-button")),
      note: rect(eject.querySelector(".water-note")),
      contentChildren: [...host.querySelector(".tk-sheet-content")!.querySelectorAll("*")].slice(0, 12).map((el) => [el.tagName, el.className, (el.textContent ?? "").trim().slice(0, 24), rect(el)]),
    };
    console.log("HERO_UI " + JSON.stringify(out));
  };
  return (
    <AbsoluteFill style={{ background: "#fff" }}>
      <LitScreen id={id} state={state} style={{ width: 465.23 }} measure={measure} />
    </AbsoluteFill>
  );
};
