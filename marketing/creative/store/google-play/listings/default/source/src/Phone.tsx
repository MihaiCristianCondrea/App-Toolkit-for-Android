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
// The kit's Pixel 9 Pro XL frame, 469 x 992 CSS px, scaled and centred, running off the bottom edge.
const FRAME = { w: 469, h: 992, scale: 1.48, top: 404 };
const PHONE = { x0: WIDTH / 2 - (FRAME.w * FRAME.scale) / 2, x1: WIDTH / 2 + (FRAME.w * FRAME.scale) / 2, y0: FRAME.top };

type Piece = { name: string; x: number; y: number; size: number; color: string; rotate: number };

const layout = (n: number) => {
  // On each seam: a large shape low and a small one high, alternating sides, so neighbours always share a shape.
  const seams: Piece[] = Array.from({ length: n - 1 }, (_, i) => {
    const x = (i + 1) * WIDTH;
    const big = 980 + (i % 2) * 60;
    const small = 300 - (i % 3) * 20;
    return [
      { name: ["cookie_12sided", "soft_burst", "clover_8leaf", "cookie_9sided", "flower", "sunny", "very_sunny"][i % 7], x: x - big / 2, y: (i % 2 ? 1520 : 1280) - big / 2, size: big, color: SHAPE_COLORS[i % 3], rotate: 8 + i * 7 },
      { name: ["clover_4leaf", "flower", "soft_boom", "burst", "cookie_12sided", "soft_burst", "clover_8leaf"][i % 7], x: x - small / 2 - 130, y: (i % 2 ? 270 : 330) - small / 2, size: small, color: SHAPE_COLORS[(i + 3) % SHAPE_COLORS.length], rotate: -10 + i * 5 },
    ];
  }).flat();
  const waves = [[880, 40, 340, 8], [2980, 1700, 380, -12], [5100, 60, 320, 4], [7300, 1760, 360, 10]].filter(([x]) => x < n * WIDTH);
  const dots = [[960, 1800], [3150, 380], [4300, 1790], [6420, 400], [7580, 1800]].filter(([x]) => x < n * WIDTH);
  // Every placed shape is an obstacle for the next one: a circle around its centre, with a margin.
  const taken = [
    ...seams.map((s) => ({ x: s.x + s.size / 2, y: s.y + s.size / 2, r: s.size / 2 })),
    ...waves.map(([x, y, w]) => ({ x: x + w / 2, y: y + 30, r: w / 2 })),
    ...dots.map(([x, y]) => ({ x: x + 48, y: y + 48, r: 60 })),
  ];
  const rand = seeded(7);
  const scatter: Piece[] = [];
  for (let k = 0; scatter.length < n * 7 && k < 6000; k++) {
    const size = 90 + rand() * 220;
    const x = rand() * n * WIDTH - size / 2;
    const y = rand() * HEIGHT - size / 2;
    const lx = x - Math.floor((x + size / 2) / WIDTH) * WIDTH;
    // Whole inside one image, clear of the caption band and of the phone.
    if (lx < 24 || lx + size > WIDTH - 24 || y < 24 || y + size > HEIGHT - 24) continue;
    if (y < 340 && lx + size > 40 && lx < 900) continue;
    if (lx + size > PHONE.x0 - 32 && lx < PHONE.x1 + 32 && y + size > PHONE.y0 - 32) continue;
    const c = { x: x + size / 2, y: y + size / 2, r: size / 2 };
    if (taken.some((o) => Math.hypot(o.x - c.x, o.y - c.y) < o.r + c.r + 40)) continue;
    taken.push(c);
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
          <Caption lines={s.lines} pill={s.pill} check={only === i} />
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

/** Two short lines in the top band, one on a pill. Play allows captions to cover at most 20% of the image. */
const Caption: React.FC<{ lines: string[]; pill: number; check: boolean }> = ({ lines, pill, check }) => {
  const ref = useRef<HTMLHeadingElement>(null);
  useLayoutEffect(() => {
    if (!check) return;
    const handle = delayRender("Measuring the caption");
    document.fonts.ready.then(() => {
      const r = ref.current!.getBoundingClientRect();
      const share = (r.width * r.height) / (WIDTH * HEIGHT);
      if (share > 0.2) cancelRender(new Error(`Caption covers ${(share * 100).toFixed(1)}% of the image; Play allows 20%`));
      else continueRender(handle);
    });
  }, [check]);
  return (
    <h1 ref={ref} style={{ position: "absolute", left: 80, top: 92, margin: 0, zIndex: 2, color: INK, fontSize: 92, lineHeight: 1, letterSpacing: -3, fontWeight: 700 }}>
      {lines.map((line, i) => (
        <span key={i} style={{ display: "block", marginTop: i === pill ? 10 : 0 }}>
          {i === pill ? <b style={{ display: "inline-block", fontWeight: 700, padding: "0 .22em .08em", borderRadius: 999, background: PILL, color: ON_PILL }}>{line}</b> : line}
        </span>
      ))}
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
