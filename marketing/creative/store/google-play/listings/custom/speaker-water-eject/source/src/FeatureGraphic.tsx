import React, { useLayoutEffect, useRef } from "react";
import { AbsoluteFill, cancelRender, continueRender, delayRender } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { Glass, SoundBubbles } from "./Glass";
import { STRINGS } from "./strings";
import { FIELD_DEEP, FONT, INK, RIBBON_STOPS, SHEET_BLUE, SHEET_BLUE_BRIGHT } from "./theme";

const W = 1024, H = 500;
/** Play may crop the edges: essential content stays inside the middle, 10% in from each side. */
const SAFE = { left: 0.1 * W, right: 0.9 * W, top: 0.1 * H, bottom: 0.9 * H };
const SPACE = { xs: 13, s: 21, m: 34, l: 55, xl: 89 };

/** The two directions explored; `ribbon` was chosen: the name centred, a glass ribbon flowing through (see the listing README). */
export type Concept = "ripple" | "ribbon";

/** Arcs around (cx, cy), facing `degrees`, at the given radii, spanning ±`spread` degrees. */
const arcs = (cx: number, cy: number, degrees: number, radii: number[], spread: number) =>
  radii.map((r, ring) => ({ ring, pts: Array.from({ length: 41 }, (_, i) => { const a = ((degrees - spread + (2 * spread * i) / 40) * Math.PI) / 180; return [cx + r * Math.cos(a), cy + r * Math.sin(a)]; }) }));

/**
 * The real Water Eject visual, cut from the live screen: the sheet's water-drop circle, as a round
 * fragment of the actual UI (the screen mounted without a frame, moved so the visual sits in the window).
 */
const UiFragment: React.FC<{ cx: number; cy: number; d: number }> = ({ cx, cy, d }) => {
  const [id, state] = SCREENS["water-eject"];
  // The visual is 200 px square at (115, 473) on the 430 px wide screen.
  const k = d / 200;
  return (
    <div data-art style={{ position: "absolute", left: cx - d / 2, top: cy - d / 2, width: d, height: d, borderRadius: "50%", overflow: "hidden", boxShadow: `0 18px 40px ${SHEET_BLUE}2e`, zIndex: 2 }}>
      <div style={{ position: "absolute", left: -115 * k, top: -473 * k, width: 430, height: 957, transform: `scale(${k})`, transformOrigin: "0 0" }}>
        <LitScreen id={id} state={state} frame={false} style={{ width: 430, height: 957 }} />
      </div>
    </div>
  );
};

/** A broad liquid-glass ribbon in the sheet's blues, flowing across the canvas through (x, y) points. */
const Ribbon: React.FC<{ d: string }> = ({ d }) => (
  <svg aria-hidden width={W} height={H} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
    <defs>
      <linearGradient id="fg-ribbon" x1="0" y1="0" x2={W} y2="0" gradientUnits="userSpaceOnUse">
        <stop offset="0" stopColor={RIBBON_STOPS[0]} />
        <stop offset=".45" stopColor={RIBBON_STOPS[1]} />
        <stop offset=".8" stopColor={RIBBON_STOPS[2]} />
        <stop offset="1" stopColor={RIBBON_STOPS[3]} />
      </linearGradient>
    </defs>
    <path d={d} fill="none" stroke="url(#fg-ribbon)" strokeWidth="150" strokeLinecap="round" opacity=".85" />
    <path d={d} fill="none" stroke={SHEET_BLUE_BRIGHT} strokeWidth="14" transform="translate(0 52)" opacity=".22" />
    <path d={d} fill="none" stroke="#FFFFFF" strokeWidth="5" transform="translate(0 -68)" opacity=".95" />
    <path d={d} fill="none" stroke="#FFFFFF" strokeWidth="2.5" transform="translate(0 70)" opacity=".8" />
  </svg>
);

/**
 * The feature graphic: the name and one line of what it is, the App Toolkit name small above them, on the
 * campaign's glass ground with the sheet's blues. No device frame, no icon, no figures or claims. The render
 * fails if any text leaves the central safe area or touches the artwork.
 */
export const FeatureGraphic: React.FC<{ locale: string; concept?: Concept }> = ({ locale, concept = "ribbon" }) => {
  useFonts();
  const { brand, title, tagline } = STRINGS[locale].featureGraphic;
  const ref = useRef<HTMLDivElement>(null);
  useLayoutEffect(() => {
    const handle = delayRender("Checking the feature graphic's text");
    document.fonts.ready.then(() => {
      const root = ref.current!.getBoundingClientRect();
      const box = (el: Element) => { const r = el.getBoundingClientRect(); return { left: r.left - root.left, top: r.top - root.top, right: r.right - root.left, bottom: r.bottom - root.top }; };
      const texts = [...ref.current!.querySelectorAll("[data-text]")].map(box), art = [...ref.current!.querySelectorAll("[data-art]")].map(box);
      const out = texts.find((t) => t.left < SAFE.left || t.right > SAFE.right || t.top < SAFE.top || t.bottom > SAFE.bottom);
      const hit = texts.find((t) => art.some((a) => t.left < a.right + SPACE.s && t.right > a.left - SPACE.s && t.top < a.bottom + SPACE.s && t.bottom > a.top - SPACE.s));
      if (out) cancelRender(new Error("Feature graphic text leaves the central safe area"));
      else if (hit) cancelRender(new Error("Feature graphic text comes within 21 px of the artwork"));
      else continueRender(handle);
    });
  }, [concept]);

  const type = (align: "left" | "center") => (
    <>
      <p data-text style={{ margin: 0, fontSize: 22, lineHeight: 1.2, fontWeight: 600, letterSpacing: 0.4, color: SHEET_BLUE, textAlign: align, width: "max-content", ...(align === "center" ? { marginInline: "auto" } : {}) }}>{brand}</p>
      <h1 data-text style={{ margin: `${SPACE.xs}px 0 0`, fontSize: 84, lineHeight: 1.02, letterSpacing: -2.4, fontWeight: 600, color: INK, width: "max-content", ...(align === "center" ? { marginInline: "auto" } : {}) }}>{title}</h1>
      <p data-text style={{ margin: `${SPACE.s}px 0 0`, fontSize: 30, lineHeight: 1.25, fontWeight: 450, letterSpacing: -0.3, color: INK, width: "max-content", ...(align === "center" ? { marginInline: "auto" } : {}) }}>{tagline}</p>
    </>
  );

  if (concept === "ribbon") {
    const ribbon = `M -120 420 C 160 470 300 210 520 250 C 740 290 820 90 1150 70`;
    const textBox = { left: 230, top: 150, right: 794, bottom: 352 };
    return (
      <AbsoluteFill ref={ref} style={{ fontFamily: FONT, overflow: "hidden" }}>
        {/* One step deeper than the screenshots' ground, so the graphic holds its edge on a white page. */}
        <Glass id="feature" width={W} height={H} panels={1} ribbons={false} field={FIELD_DEEP} />
        <div style={{ position: "absolute", inset: 0, WebkitMaskImage: `radial-gradient(ellipse 330px 150px at 512px 250px, rgba(0,0,0,.12) 60%, #000 100%)`, maskImage: `radial-gradient(ellipse 330px 150px at 512px 250px, rgba(0,0,0,.12) 60%, #000 100%)` }}>
          <Ribbon d={ribbon} />
        </div>
        {/* Sound rising off the ribbon's crest, inside the safe area. */}
        <div data-art style={{ position: "absolute", left: 772, top: 52, width: 150, height: 120 }}>
          <SoundBubbles arcs={arcs(70, 70, -30, [34, 55, 89], 40).map((a) => ({ ...a, pts: a.pts.map(([x, y]) => [x, y]) }))} bubbles={[[120, 30, 8], [30, 100, 5]]} width={150} height={120} />
        </div>
        <div style={{ position: "absolute", left: textBox.left, top: textBox.top, width: textBox.right - textBox.left, zIndex: 3 }}>{type("center")}</div>
      </AbsoluteFill>
    );
  }

  // Ripple: the real drop visual left of centre, sound spreading from it outward, the type to its right.
  const c = { x: 318, y: 250 }, d = 196;
  return (
    <AbsoluteFill ref={ref} style={{ fontFamily: FONT, overflow: "hidden" }}>
      {/* One step deeper than the screenshots' ground, so the graphic holds its edge on a white page. */}
      <Glass id="feature" width={W} height={H} panels={1} text={[{ left: 476, top: 150, right: 900, bottom: 350 }]} field={FIELD_DEEP} />
      <div style={{ position: "absolute", inset: 0, zIndex: 1 }}>
        <SoundBubbles arcs={arcs(c.x, c.y, 180, [d / 2 + SPACE.s, d / 2 + SPACE.l, d / 2 + SPACE.xl], 46)} bubbles={[[c.x - 205, c.y - 92, 10], [c.x - 172, c.y + 118, 6], [c.x - 232, c.y + 34, 5], [c.x + 40, c.y - 150, 7]]} width={W} height={H} />
      </div>
      <UiFragment cx={c.x} cy={c.y} d={d} />
      <div style={{ position: "absolute", left: 476, top: 150, zIndex: 3 }}>{type("left")}</div>
    </AbsoluteFill>
  );
};
