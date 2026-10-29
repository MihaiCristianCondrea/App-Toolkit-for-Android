import React from "react";
import { AbsoluteFill } from "remotion";
import { LitScreen } from "./LitScreen";
import { SCREENS } from "./screens";
import { SCATTER, Shape, seeded } from "./Shape";
import { STRINGS } from "./strings";
import { GROUND, SHAPE_COLORS } from "./theme";

// Tablet and desktop screenshots: the app's own wide layout (the kit's "fold" and "desktop" windows, with the rail or
// the permanent drawer) in its device, centred and unscaled on 1920 x 1080 canvases rendered at 2x (3840 x 2160, 16:9).
// Each set is cut from one strip like the phone set. No captions: Play wants large-screen screenshots to show the app.
export const CANVAS = { w: 1920, h: 1080 };
export const SETS = {
  tablet: { key: "tabletScreenshots", window: "fold", device: [904.53, 933.27], seed: 11 },
  desktop: { key: "desktopScreenshots", window: "desktop", device: [1459.47, 973.2], seed: 23 },
} as const;
export type Kind = keyof typeof SETS;

const layout = (kind: Kind, n: number) => {
  const [dw, dh] = SETS[kind].device;
  const dx = (CANVAS.w - dw) / 2;
  const dy = (CANVAS.h - dh) / 2;
  const gap = CANVAS.w - dw;
  // Seam shapes fill the gap between two devices, a large one and a small one, never reaching a device.
  const seams = Array.from({ length: n - 1 }, (_, i) => {
    const big = Math.min(640, gap - 80);
    const small = Math.min(220, gap / 2 - 40);
    const x = (i + 1) * CANVAS.w;
    return [
      { name: ["cookie_12sided", "clover_8leaf", "soft_burst"][i % 3], x: x - big / 2, y: (i % 2 ? 300 : 760) - big / 2, size: big, color: SHAPE_COLORS[i % 2 ? 0 : 2], rotate: 10 * (i + 1) },
      { name: ["flower", "very_sunny", "burst"][i % 3], x: x + (i % 2 ? 40 : -60) - small / 2, y: (i % 2 ? 880 : 170) - small / 2, size: small, color: SHAPE_COLORS[1], rotate: -12 },
    ];
  }).flat();
  const devices = Array.from({ length: n }, (_, i) => ({ x0: i * CANVAS.w + dx - 32, x1: i * CANVAS.w + dx + dw + 32, y0: dy - 32, y1: dy + dh + 32 }));
  const taken = seams.map((s) => ({ x: s.x + s.size / 2, y: s.y + s.size / 2, r: s.size / 2 }));
  const rand = seeded(SETS[kind].seed);
  const scatter = [];
  for (let k = 0; scatter.length < n * 6 && k < 6000; k++) {
    const size = 80 + rand() * 160;
    const x = rand() * n * CANVAS.w - size / 2;
    const y = rand() * CANVAS.h - size / 2;
    const lx = x - Math.floor((x + size / 2) / CANVAS.w) * CANVAS.w;
    if (lx < 24 || lx + size > CANVAS.w - 24 || y < 24 || y + size > CANVAS.h - 24) continue;
    const c = { x: x + size / 2, y: y + size / 2, r: size / 2 };
    if (devices.some((d) => c.x + c.r > d.x0 && c.x - c.r < d.x1 && c.y + c.r > d.y0 && c.y - c.r < d.y1)) continue;
    if (taken.some((o) => Math.hypot(o.x - c.x, o.y - c.y) < o.r + c.r + 40)) continue;
    taken.push(c);
    scatter.push({ name: SCATTER[scatter.length % SCATTER.length], x, y, size, color: SHAPE_COLORS[(scatter.length * 3) % SHAPE_COLORS.length], rotate: rand() * 360 });
  }
  return { seams, scatter, dx, dy };
};

export const WideStrip: React.FC<{ locale: string; kind: Kind; only?: number }> = ({ locale, kind, only }) => {
  const list = STRINGS[locale][SETS[kind].key];
  const { seams, scatter, dx, dy } = layout(kind, list.length);
  return (
    <AbsoluteFill style={{ background: GROUND, width: list.length * CANVAS.w, overflow: "hidden" }}>
      {scatter.map((p, i) => <Shape key={`s${i}`} {...p} />)}
      {seams.map((p, i) => <Shape key={`m${i}`} {...p} />)}
      {list.map((t, i) => (only === undefined || only === i) && (
        <div key={t.id} style={{ position: "absolute", left: i * CANVAS.w + dx, top: dy, filter: kind === "tablet" ? "drop-shadow(0 30px 50px rgba(58, 38, 0, .22))" : undefined }}>
          <LitScreen id={SCREENS[t.id][0]} state={SCREENS[t.id][1]} window={SETS[kind].window} />
        </div>
      ))}
    </AbsoluteFill>
  );
};

export const WideShot: React.FC<{ locale: string; kind: Kind; index: number }> = ({ locale, kind, index }) => (
  <AbsoluteFill style={{ overflow: "hidden" }}>
    <div style={{ position: "absolute", left: -index * CANVAS.w, top: 0, height: CANVAS.h }}>
      <WideStrip locale={locale} kind={kind} only={index} />
    </div>
  </AbsoluteFill>
);
