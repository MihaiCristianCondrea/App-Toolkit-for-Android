import React, { useLayoutEffect, useRef } from "react";
import { AbsoluteFill, cancelRender, continueRender, delayRender } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { Dots, SCATTER, Shape, Wave, seeded } from "./Shape";
import { STRINGS } from "./strings";
import { FONT, GROUND, INK, ON_PILL, PILL, SHAPE_COLORS } from "./theme";

// The eight phone screenshots are cut from one panorama, 8 x 1080 by 1920: one ground, shapes centred on every seam so
// each pair of neighbours shares one, and a seeded scatter filling the gaps. Side by side on Google Play they read as
// one picture.
export const WIDTH = 1080;
export const HEIGHT = 1920;
// Material-Next's 3D Pixel 9 Pro XL frame, 465.23 x 988.76 CSS px, scaled and centred, running off the bottom edge.
const FRAME = { w: 465.23, h: 988.76, scale: 1.48, top: 404 };
const PHONE = { x0: WIDTH / 2 - (FRAME.w * FRAME.scale) / 2, x1: WIDTH / 2 + (FRAME.w * FRAME.scale) / 2, y0: FRAME.top };

// The caption's box in every image: centred in the top band. Nothing else is drawn in it, and the caption must fit
// inside it (the render fails otherwise), so a shape can never touch the text.
const CAPTION = { left: 120, right: WIDTH - 120, top: 72, bottom: 312 };
// How far the pill's icon sits below the baseline, so its centre meets the centre of the word's lowercase letters.
const ICON_DROP = "-.1em";
// Space every shape keeps from another shape and from the caption box.
const MARGIN = 40;

type Piece = { name: string; x: number; y: number; size: number; color: string; rotate: number };
type Circle = { x: number; y: number; r: number; what: string };

const circleHitsRect = (c: Circle, left: number, top: number, right: number, bottom: number, margin: number) => {
  const dx = c.x - Math.max(left, Math.min(c.x, right));
  const dy = c.y - Math.max(top, Math.min(c.y, bottom));
  return Math.hypot(dx, dy) < c.r + margin;
};
const hitsCaption = (c: Circle, n: number) =>
  Array.from({ length: n }, (_, i) => i * WIDTH).some((x0) => circleHitsRect(c, x0 + CAPTION.left, CAPTION.top, x0 + CAPTION.right, CAPTION.bottom, MARGIN));

const layout = (n: number) => {
  // On each seam, so neighbours always share them: a large shape low, running behind both phones, and a small one in
  // the gap between the two captions. A wave or a dot grid sits in the gutter between the phones, under the captions.
  const seams: Piece[] = Array.from({ length: n - 1 }, (_, i) => {
    const x = (i + 1) * WIDTH;
    const big = 980 + (i % 2) * 60;
    const small = 120 + (i % 3) * 15;
    return [
      { name: ["cookie_12sided", "soft_burst", "clover_8leaf", "cookie_9sided", "flower", "sunny", "very_sunny"][i % 7], x: x - big / 2, y: (i % 2 ? 1520 : 1280) - big / 2, size: big, color: SHAPE_COLORS[i % 3], rotate: 8 + i * 7 },
      { name: ["clover_4leaf", "flower", "soft_boom", "burst", "cookie_12sided", "soft_burst", "clover_8leaf"][i % 7], x: x - small / 2, y: 192 - small / 2, size: small, color: SHAPE_COLORS[(i + 3) % SHAPE_COLORS.length], rotate: -10 + i * 5 },
    ];
  }).flat();
  const WAVE = 240;
  const waves = Array.from({ length: n - 1 }, (_, i) => i).filter((i) => i % 2 === 0)
    .map((i) => [(i + 1) * WIDTH - WAVE / 2, 500, WAVE, i % 4 ? -10 : 8]);
  const dots = Array.from({ length: n - 1 }, (_, i) => i).filter((i) => i % 2 === 1)
    .map((i) => [(i + 1) * WIDTH - 48, 470]);
  // Every placed piece is a circle around its centre. None may come within MARGIN of another, or of a caption box.
  const placed: Circle[] = [
    ...seams.map((s, k) => ({ x: s.x + s.size / 2, y: s.y + s.size / 2, r: s.size / 2, what: `seam shape ${k}` })),
    ...waves.map(([x, y, w], k) => ({ x: x + w / 2, y: y + (w * 60) / 400 / 2, r: w / 2, what: `wave ${k}` })),
    ...dots.map(([x, y], k) => ({ x: x + 48, y: y + 48, r: 68, what: `dots ${k}` })),
  ];
  placed.forEach((a, i) => {
    if (hitsCaption(a, n)) throw new Error(`${a.what} overlaps a caption`);
    placed.slice(i + 1).forEach((b) => {
      if (Math.hypot(a.x - b.x, a.y - b.y) < a.r + b.r + MARGIN) throw new Error(`${a.what} overlaps ${b.what}`);
    });
  });
  const rand = seeded(7);
  const scatter: Piece[] = [];
  for (let k = 0; scatter.length < n * 7 && k < 6000; k++) {
    const size = 90 + rand() * 220;
    const x = rand() * n * WIDTH - size / 2;
    const y = rand() * HEIGHT - size / 2;
    const lx = x - Math.floor((x + size / 2) / WIDTH) * WIDTH;
    // Whole inside one image, clear of the phone.
    if (lx < 24 || lx + size > WIDTH - 24 || y < 24 || y + size > HEIGHT - 24) continue;
    if (lx + size > PHONE.x0 - 32 && lx < PHONE.x1 + 32 && y + size > PHONE.y0 - 32) continue;
    const c = { x: x + size / 2, y: y + size / 2, r: size / 2, what: "scatter" };
    if (hitsCaption(c, n)) continue;
    if (placed.some((o) => Math.hypot(o.x - c.x, o.y - c.y) < o.r + c.r + MARGIN)) continue;
    placed.push(c);
    scatter.push({ name: SCATTER[scatter.length % SCATTER.length], x, y, size, color: SHAPE_COLORS[(scatter.length * 5) % SHAPE_COLORS.length], rotate: rand() * 360 });
  }
  return { seams, scatter, waves, dots };
};

/** The whole panorama; `only` draws one screenshot's screen (the others' shapes still frame it). */
export const PhonePanorama: React.FC<{ locale: string; only?: number }> = ({ locale, only }) => {
  useFonts();
  const strings = STRINGS[locale];
  const n = strings.screenshots.length;
  const { seams, scatter, waves, dots } = layout(n);
  return (
    <AbsoluteFill style={{ background: GROUND, width: n * WIDTH, fontFamily: FONT, overflow: "hidden" }}>
      {scatter.map((p, i) => <Shape key={`s${i}`} {...p} />)}
      {waves.map(([x, y, w, r], i) => <Wave key={`w${i}`} x={x} y={y} width={w} rotate={r} color="#EE9B00" />)}
      {dots.map(([x, y], i) => <Dots key={`d${i}`} x={x} y={y} color="#C67D05" />)}
      {seams.map((p, i) => <Shape key={`m${i}`} {...p} />)}
      {strings.screenshots.map((s, i) => (
        <div key={s.id} style={{ position: "absolute", left: i * WIDTH, top: 0, width: WIDTH, height: HEIGHT }}>
          <Caption lines={s.lines} highlight={s.highlight} icon={s.icon} check={only === i} />
          {(only === undefined || only === i) && (
            <div style={{ position: "absolute", left: WIDTH / 2, top: FRAME.top, width: FRAME.w, height: FRAME.h, transform: `translateX(-50%) scale(${FRAME.scale})`, transformOrigin: "50% 0", filter: "drop-shadow(0 40px 60px rgba(58, 38, 0, .25))" }}>
              <LitScreen id={SCREENS[s.id][0]} state={SCREENS[s.id][1]} style={{ width: FRAME.w, height: FRAME.h }} />
            </div>
          )}
        </div>
      ))}
    </AbsoluteFill>
  );
};

/**
 * Two short lines, centred in the caption box. One word or short phrase of them sits on a pill with the Material Symbol
 * of the screen it describes, the way Google's own apps caption their store screenshots. No commas, full stops or
 * dashes: the line break carries the pause. The render fails if the highlight is not in the lines exactly once, if a line has
 * punctuation or a dash, if the caption leaves its box, or if it covers over 20% of the image, Play's limit.
 */
const Caption: React.FC<{ lines: string[]; highlight: string; icon: string; check: boolean }> = ({ lines, highlight, icon, check }) => {
  const ref = useRef<HTMLHeadingElement>(null);
  const found = lines.flatMap((line, i) => (line.split(highlight).length - 1 === 1 ? [i] : []));
  const total = lines.reduce((sum, line) => sum + line.split(highlight).length - 1, 0);
  useLayoutEffect(() => {
    if (!check) return;
    const handle = delayRender("Measuring the caption");
    if (total !== 1) {
      cancelRender(new Error(`The highlight "${highlight}" must appear exactly once in the caption "${lines.join(" ")}"`));
      return;
    }
    if (/[,.;:\u2010-\u2015-]/.test(lines.join(""))) {
      cancelRender(new Error(`The caption "${lines.join(" ")}" has punctuation or a dash; the line break carries the pause`));
      return;
    }
    document.fonts.ready.then(() => {
      const r = ref.current!.getBoundingClientRect();
      const share = (r.width * r.height) / (WIDTH * HEIGHT);
      const box = ref.current!.parentElement!.getBoundingClientRect();
      const inside = r.left - box.left >= CAPTION.left && r.right - box.left <= CAPTION.right && r.top - box.top >= CAPTION.top && r.bottom - box.top <= CAPTION.bottom;
      if (share > 0.2) cancelRender(new Error(`Caption covers ${(share * 100).toFixed(1)}% of the image; Play allows 20%`));
      else if (!inside) cancelRender(new Error(`The caption "${lines.join(" ")}" leaves its box; shorten it`));
      else continueRender(handle);
    });
  }, [check, total, highlight, lines]);
  // An inline block shares the line's baseline, so the highlighted word sits level with the words around it; the
  // padding, not an offset, sizes the pill around it.
  const pill = (
    <b style={{ display: "inline-block", margin: "0 .08em", padding: ".02em .3em .1em .2em", lineHeight: 1.04, borderRadius: 999, background: PILL, color: ON_PILL, fontWeight: 600, whiteSpace: "nowrap" }}>
      <span aria-hidden style={{ display: "inline-block", marginRight: ".16em", fontFamily: "'Material Symbols Rounded'", fontWeight: 400, fontSize: ".72em", lineHeight: 1, verticalAlign: ICON_DROP, fontVariationSettings: "'FILL' 1, 'wght' 500, 'opsz' 48" }}>{icon}</span>
      {highlight}
    </b>
  );
  return (
    <h1 ref={ref} style={{ position: "absolute", left: "50%", top: CAPTION.top, transform: "translateX(-50%)", width: "max-content", maxWidth: CAPTION.right - CAPTION.left, margin: 0, zIndex: 2, color: INK, fontSize: 76, lineHeight: 1.36, letterSpacing: -1.5, fontWeight: 560, textAlign: "center" }}>
      {lines.map((line, i) => {
        if (i !== found[0]) return <span key={i} style={{ display: "block" }}>{line}</span>;
        const [before, after] = line.split(highlight);
        return <span key={i} style={{ display: "block" }}>{before}{pill}{after}</span>;
      })}
    </h1>
  );
};

/** One phone screenshot: the panorama, moved so screenshot `index` fills the frame. */
export const PhoneShot: React.FC<{ locale: string; index: number }> = ({ locale, index }) => (
  <AbsoluteFill style={{ overflow: "hidden" }}>
    <div style={{ position: "absolute", left: -index * WIDTH, top: 0, height: HEIGHT }}>
      <PhonePanorama locale={locale} only={index} />
    </div>
  </AbsoluteFill>
);
