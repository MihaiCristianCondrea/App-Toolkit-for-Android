/**
 * The checks every screenshot shares. `hard` fails the render; `preferences` are reported when a
 * composition drifts outside a tolerance; `intentional` lists overlaps and crops the art direction asks
 * for, so they are told apart from accidents.
 */
import { CAPTION, H, SAFE_X, SPACE, TEXT_CLEARANCE, W, clipConvex, gapToBox, grow, overlapArea, rectPoly } from "./geometry.ts";
import type { Box, Measured, Pt } from "./geometry.ts";
import type { Bubble, Piece } from "./effects.ts";
import type { Ribbon } from "./ribbons.ts";
import type { ShotSpec } from "./shots.ts";

export type Report = { hard: string[]; preferences: string[]; intentional: string[]; balance: Pt; terms: Record<string, number> };

const inImage = (q: Pt[], box: Box) => q.every(([x, y]) => x >= box.left && x <= box.right && y >= box.top && y <= box.bottom);
const circleGap = (cx: number, cy: number, r: number, b: Box) => Math.hypot(Math.max(b.left - cx, 0, cx - b.right), Math.max(b.top - cy, 0, cy - b.bottom)) - r;
/** A phone keeps this much air from the screenshot's sides, so it never reads as cut by the divider. */
const SIDE = SPACE.m;
const pct = (n: number) => `${(n * 100).toFixed(1)}%`;

/** The geometric rules for the phones alone; the solver applies these to every candidate. */
export const phoneRules = (spec: ShotSpec, ms: Measured[]): string[] => {
  const v: string[] = [];
  const block = CAPTION[spec.caption].block;
  const safe: Box = { left: SAFE_X, top: SPACE.xl, right: W - SAFE_X, bottom: H - SPACE.xl };
  ms.forEach((m, i) => {
    const name = spec.screens[i];
    // The silhouette between the top and bottom edges, not clipped at the sides, so a crossing shows.
    const xs = clipConvex(m.silhouette, rectPoly({ left: -1e5, top: 0, right: 1e5, bottom: H })).map((p) => p[0]);
    if (Math.min(...xs) < -0.5 || Math.max(...xs) > W + 0.5) v.push(`${name}: the phone crosses into the next screenshot`);
    else if (Math.min(...xs) < SIDE || Math.max(...xs) > W - SIDE) v.push(`${name}: the phone comes within ${SIDE} px of a side edge`);
    if (gapToBox(m.visible, block) < TEXT_CLEARANCE) v.push(`${name}: the phone comes within ${TEXT_CLEARANCE} px of the caption`);
    for (const [k, q] of Object.entries(m.critical)) {
      if (!inImage(q, safe)) v.push(`${name}: ${k} leaves the safe area (0.05 W sides, ${SPACE.xl} px top and bottom)`);
      if (gapToBox(q, block) < TEXT_CLEARANCE) v.push(`${name}: ${k} comes within ${TEXT_CLEARANCE} px of the caption`);
      ms.slice(i + 1).forEach((front, j) => {
        if (overlapArea(q, front.visible) > 0) v.push(`${name}: ${k} is covered by the ${spec.screens[i + 1 + j]} phone`);
      });
    }
  });
  return [...v, ...(spec.hard?.(ms) ?? [])];
};

/** Everything: phones, shapes, effects, balance and the composition's own preferences. */
export const validate = (spec: ShotSpec, ms: Measured[], shapes: Piece[], effects: Bubble[], ribbons: Ribbon[], edges: { left: boolean; right: boolean }): Report => {
  const hard = phoneRules(spec, ms), preferences: string[] = [], intentional: string[] = [];
  const block = CAPTION[spec.caption].block;
  for (const p of shapes) {
    const [cx, cy, r] = [p.x + p.size / 2, p.y + p.size / 2, p.size / 2];
    if (circleGap(cx, cy, r, block) < TEXT_CLEARANCE) hard.push(`shape ${p.name} comes within ${TEXT_CLEARANCE} px of the caption`);
    if ((!edges.left && cx - r < -0.5) || (!edges.right && cx + r > W + 0.5)) hard.push(`shape ${p.name} crosses into the next screenshot`);
    if (!p.behind && (cx < 0 || cx > W || cy < 0 || cy > H)) hard.push(`shape ${p.name} shows less than half of itself`);
    if (ribbons.some((b) => b.pts.some(([x, y]) => Math.hypot(x - cx, y - cy) < r + b.half))) hard.push(`shape ${p.name} overlaps a glass ribbon`);
    const under = ms.filter((m) => gapToBox(m.visible, { left: cx - r, top: cy - r, right: cx + r, bottom: cy + r }) === 0);
    if (under.length && !p.behind) hard.push(`shape ${p.name} touches a phone`);
    if (under.length && p.behind) intentional.push(`shape ${p.name} sits behind the phone`);
  }
  for (const [x, y, r] of effects) if (circleGap(x, y, r, block) < TEXT_CLEARANCE) hard.push(`an effect comes within ${TEXT_CLEARANCE} px of the caption`);

  ms.forEach((m, i) => {
    const name = spec.screens[i];
    if (m.full.top < 0) intentional.push(`${name}: cropped at the top edge`);
    if (m.full.bottom > H) intentional.push(`${name}: cropped at the bottom edge`);
    for (const [k, q] of Object.entries(m.secondary))
      if (!inImage(q, { left: 0, top: 0, right: W, bottom: H })) intentional.push(`${name}: ${k} (secondary) cropped`);
    ms.slice(i + 1).forEach((front, j) => {
      const shared = overlapArea(m.visible, front.visible);
      if (shared > 0) intentional.push(`${spec.screens[i + 1 + j]} overlaps ${name} by ${pct(shared / m.area)} of its visible area, clear of its critical UI`);
    });
  });

  // Visual balance: area-weighted centroid, with a weight per kind for how much it draws the eye (phones
  // 1, caption text 0.5, light shapes 0.25, bubbles 0.3); the pixel measure in scripts/verify.mjs follows.
  const parts: [number, Pt][] = [
    ...ms.map((m) => [m.area, m.centroid] as [number, Pt]),
    [0.5 * (block.right - block.left) * (block.bottom - block.top) * 0.6, [(block.left + block.right) / 2, (block.top + block.bottom) / 2]],
    ...shapes.map((p) => [0.25 * Math.PI * (p.size / 2) ** 2 * (p.behind ? 0.4 : 1), [p.x + p.size / 2, p.y + p.size / 2]] as [number, Pt]),
    ...effects.map(([x, y, r]) => [0.3 * Math.PI * r * r, [x, y]] as [number, Pt]),
  ];
  const total = parts.reduce((a, [w]) => a + w, 0);
  const balance: Pt = [parts.reduce((a, [w, c]) => a + w * c[0], 0) / total, parts.reduce((a, [w, c]) => a + w * c[1], 0) / total];
  if (Math.abs(balance[0] - W / 2) > 0.05 * W) preferences.push(`visual centroid at ${(balance[0] / W).toFixed(3)} W, outside 0.50 ± 0.05`);
  const terms = spec.score(ms);
  for (const [k, t] of Object.entries(terms)) if (t > 1) preferences.push(`${k} outside its tolerance (${t.toFixed(2)})`);
  return { hard, preferences, intentional, balance, terms };
};

/** The text block grown by the clearance: the region the background fades behind and foreground avoids. */
export const textZone = (spec: ShotSpec) => grow(CAPTION[spec.caption].block, TEXT_CLEARANCE);
