/**
 * The tablet and desktop screenshots, on the same system as the phones: each device at its true size,
 * centred on a 1920 x 1080 canvas (Play's large-screen rule: the app's own large-screen layout, no
 * captions), its critical UI measured by the probe at that window, MaterialShapes placed around it clear of
 * the glass ribbons, and a validation report. Shared by src/Wide.tsx and scripts/verify.mjs.
 */
import { SPACE, areaCentroid, bbox, inside, rectPoly } from "./geometry.ts";
import type { Box, Pt, ScreenUi } from "./geometry.ts";
import { placeShapes } from "./effects.ts";
import type { Piece, ShapePlan } from "./effects.ts";
import { ribbonLines } from "./ribbons.ts";
import type { Report } from "./validate.ts";

export const CANVAS = { w: 1920, h: 1080 };
export type Kind = "tablet" | "desktop";
export const WIDE_SETS: Record<Kind, { key: "tabletScreenshots" | "desktopScreenshots"; window: "fold" | "desktop"; device: [number, number]; shapes: ShapePlan }> = {
  tablet: { key: "tabletScreenshots", window: "fold", device: [904.53, 933.27], shapes: { seed: 13, offset: 3, small: 4 } },
  desktop: { key: "desktopScreenshots", window: "desktop", device: [1459.47, 973.2], shapes: { seed: 29, offset: 5, small: 4 } },
};

/** The golden guides of the wide canvas. */
const PHI = (1 + Math.sqrt(5)) / 2;
export const WIDE_GOLDEN = { x: [CANVAS.w / PHI ** 2, CANVAS.w / PHI], y: [CANVAS.h / PHI ** 2, CANVAS.h / PHI] };

export const composeWide = (kind: Kind, index: number, screens: string[], ui: Record<string, ScreenUi>) => {
  const set = WIDE_SETS[kind], [dw, dh] = set.device, panels = screens.length, screen = screens[index];
  const box: Box = { left: (CANVAS.w - dw) / 2, top: (CANVAS.h - dh) / 2, right: (CANVAS.w + dw) / 2, bottom: (CANVAS.h + dh) / 2 };
  const device = rectPoly(box);
  const measured = ui[`${screen}@${set.window}`];
  if (!measured) throw new Error(`No UI measured for ${screen} at the ${set.window} window: run npm run probe -- ${screen}@${set.window}`);
  const place = (boxes: Record<string, number[]>) => Object.fromEntries(Object.entries(boxes).map(([k, [l, t, r, b]]) => [k, rectPoly({ left: box.left + l, top: box.top + t, right: box.left + r, bottom: box.top + b })])) as Record<string, Pt[]>;
  const [critical, secondary] = [place(measured.critical), place(measured.secondary)];
  const ribbons = ribbonLines(panels * CANVAS.w, CANVAS.h, panels).map((r) => ({ ...r, pts: r.pts.map(([x, y]) => [x - index * CANVAS.w, y] as [number, number]) }));
  const edges = { left: index === 0, right: index === panels - 1 };
  const shapes = placeShapes(set.shapes, [{ visible: device }], null, [], ribbons, edges, CANVAS);
  const report = validateWide(device, critical, shapes, ribbons, edges);
  return { kind, screen, box, device, critical, secondary, shapes, report };
};

/**
 * Hard: the device keeps SPACE.m from the canvas edges; critical UI lies inside the device and the canvas;
 * every shape stays inside its screenshot (except at the strip's outer ends), clear of the ribbons and the
 * device, and shows at least half of itself. Preference: the visual centroid within 0.05 W of the centre.
 */
const validateWide = (device: Pt[], critical: Record<string, Pt[]>, shapes: Piece[], ribbons: { pts: [number, number][]; half: number }[], edges: { left: boolean; right: boolean }): Report => {
  const hard: string[] = [], preferences: string[] = [], intentional: string[] = [];
  const d = bbox(device);
  if (d.left < SPACE.m || d.top < 0 || d.right > CANVAS.w - SPACE.m || d.bottom > CANVAS.h) hard.push("the device must keep SPACE.m from the canvas sides and stay inside it");
  for (const [k, q] of Object.entries(critical)) {
    const b = bbox(q);
    if (b.left < d.left || b.right > d.right || b.top < d.top || b.bottom > d.bottom || b.top < 0 || b.bottom > CANVAS.h) hard.push(`${k} must show inside the device`);
  }
  for (const p of shapes) {
    const [cx, cy, r] = [p.x + p.size / 2, p.y + p.size / 2, p.size / 2];
    if ((!edges.left && cx - r < -0.5) || (!edges.right && cx + r > CANVAS.w + 0.5)) hard.push(`shape ${p.name} crosses into the next screenshot`);
    if (cx < 0 || cx > CANVAS.w || cy < 0 || cy > CANVAS.h) hard.push(`shape ${p.name} shows less than half of itself`);
    if (ribbons.some((b) => b.pts.some(([x, y]) => Math.hypot(x - cx, y - cy) < r + b.half))) hard.push(`shape ${p.name} overlaps a glass ribbon`);
    if (inside(device, [cx, cy]) || (cx + r > d.left && cx - r < d.right && cy + r > d.top && cy - r < d.bottom && Math.hypot(Math.max(d.left - cx, 0, cx - d.right), Math.max(d.top - cy, 0, cy - d.bottom)) < r)) hard.push(`shape ${p.name} touches the device`);
  }
  const parts: [number, Pt][] = [[areaCentroid(device).area, areaCentroid(device).centroid], ...shapes.map((p) => [0.25 * Math.PI * (p.size / 2) ** 2, [p.x + p.size / 2, p.y + p.size / 2]] as [number, Pt])];
  const total = parts.reduce((a, [w]) => a + w, 0);
  const balance: Pt = [parts.reduce((a, [w, c]) => a + w * c[0], 0) / total, parts.reduce((a, [w, c]) => a + w * c[1], 0) / total];
  if (Math.abs(balance[0] - CANVAS.w / 2) > 0.05 * CANVAS.w) preferences.push(`visual centroid at ${(balance[0] / CANVAS.w).toFixed(3)} W, outside 0.50 ± 0.05`);
  return { hard, preferences, intentional, balance, terms: {} };
};
