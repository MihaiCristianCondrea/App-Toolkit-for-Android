/**
 * Decoration placed by geometry: the hero's sound arcs and bubbles, and MaterialShapes for every
 * screenshot. Everything here is deterministic, so a layout renders the same each time.
 */
import { GOLDEN, PHI, SAFE_X, SPACE, TEXT_CLEARANCE, W, H, distanceTo, grow, inside } from "./geometry.ts";
import type { Box, Measured, Pt } from "./geometry.ts";
import type { Ribbon } from "./ribbons.ts";
import { SHAPE_BEHIND, SHAPE_TONES } from "../theme.ts";

const rad = (d: number) => (d * Math.PI) / 180;

/**
 * Sound leaving both groups of speaker holes: for each, arcs centred on the projected holes, opening along
 * the bottom face's projected normal, spanning ±`SPREAD` degrees, with radii from the spacing scale.
 */
const SPREAD = 44;
const RINGS = [SPACE.m, SPACE.l, SPACE.xl, SPACE.xxl, SPACE.xl + SPACE.xxl];
export type Arc = { pts: Pt[]; ring: number };
const onArc = (m: Measured, k: number, radius: number, degrees: number): Pt => {
  const { at: s, direction: d } = m.speakers[k], n: Pt = [-d[1], d[0]], a = rad(degrees);
  return [s[0] + radius * (d[0] * Math.cos(a) + n[0] * Math.sin(a)), s[1] + radius * (d[1] * Math.cos(a) + n[1] * Math.sin(a))];
};
const ARC_REACH = 4; // half the drawn stroke with its glow
const arc = (m: Measured, k: number, radius: number) => Array.from({ length: 41 }, (_, i) => onArc(m, k, radius, -SPREAD + (2 * SPREAD * i) / 40));
export const soundArcs = (m: Measured, caption: Box): Arc[] =>
  m.speakers.flatMap((_, k) => {
    // The outermost ring is the largest radius, up to the scale's, whose arc keeps TEXT_CLEARANCE from the
    // caption; a ring that would end within SPACE.s of the one inside it is left out.
    const limit = caption.top - TEXT_CLEARANCE - ARC_REACH, fits = (r: number) => Math.max(...arc(m, k, r).map((p) => p[1])) <= limit;
    const radii: number[] = [];
    for (const r of RINGS) {
      let fit = r;
      if (!fits(r)) { let [lo, hi] = [radii[radii.length - 1] ?? 0, r]; for (let i = 0; i < 30; i++) { const mid = (lo + hi) / 2; if (fits(mid)) lo = mid; else hi = mid; } fit = lo; }
      if (fit - (radii[radii.length - 1] ?? 0) < SPACE.s) break;
      radii.push(fit);
    }
    return radii.map((radius, ring) => ({ ring, pts: arc(m, k, radius) }));
  });

/**
 * Bubbles, [x, y, r], carried by the sound: each sits just outside a wavefront, as if the wave were pushing
 * it, and grows the further out it rides (radii 8, 13, 21 from the scale). Angles are mirrored between the
 * two speakers and offset a little so the pattern is not symmetric. A bubble is dropped if it would touch
 * the phone, come within TEXT_CLEARANCE of the caption or meet another bubble.
 */
const RIDERS: [number, number, number][] = [[1, 22, 8], [2, -30, 13], [2, 34, 8], [3, -38, 13], [3, 12, 21], [4, -20, 21], [4, 30, 13]];
export type Bubble = [number, number, number];
export const bubbles = (m: Measured, caption: Box): Bubble[] => {
  const out: Bubble[] = [];
  m.speakers.forEach((_, k) => RIDERS.forEach(([ring, degrees, r]) => {
    const radius = RINGS[ring] + r * 0.7;
    const [x, y] = onArc(m, k, radius, (k ? -degrees : degrees) + (k ? 6 : 0));
    if (y + r > caption.top - TEXT_CLEARANCE || x - r < SAFE_X || x + r > W - SAFE_X) return;
    if (inside(m.visible, [x, y]) || distanceTo(m.visible, [x, y]) < r + SPACE.xs) return;
    if (out.some(([bx, by, br]) => Math.hypot(bx - x, by - y) < br + r + SPACE.xs)) return;
    out.push([x, y, r]);
  }));
  return out;
};

/** A MaterialShape, as the kit names it, with its box; `behind` marks one drawn behind the phones on purpose. */
export type Piece = { name: string; x: number; y: number; size: number; color: string; rotate: number; behind?: boolean };

/** MaterialShapes in the campaign palette. */
const SHAPE_NAMES = ["cookie_9sided", "flower", "clover_4leaf", "soft_burst", "gem", "pill", "sunny", "cookie_6sided"];
const SHAPE_COLORS = SHAPE_TONES;

/**
 * How a screenshot's shapes are placed. `big` puts one shape behind the phones, centred at `at` but moved
 * in so it never crosses a divider, as large as it can be (up to `max`) while it stays clear of the
 * ribbons; it is dropped below `min`. `small` scatters that many smaller shapes; `seed` varies the scatter
 * and `offset` the first shape's name.
 */
export type ShapePlan = { seed: number; offset?: number; small: number; big?: { at: (phones: Measured[]) => Pt; max: number; min: number; name?: string; color?: string; rotate?: number } };

/**
 * Every shape keeps SPACE.s clear of the background's glass ribbons and TEXT_CLEARANCE clear of the
 * caption's text block, and stays inside its own screenshot left to right, except at the outer ends of the
 * strip (`edges`); any may run off the top or bottom, but at least half of each small shape shows. Small shapes also keep SPACE.s from every phone's
 * visible silhouette and SPACE.m from the effects and each other.
 */
/** Anything shapes keep clear of: its visible outline, as a measured phone or a device box gives it. */
export type Obstacle = { visible: Pt[] };
export const placeShapes = (plan: ShapePlan, phones: Obstacle[], caption: Box | null, effects: Bubble[], ribbons: Ribbon[], edges: { left: boolean; right: boolean }, area = { w: W, h: H }): Piece[] => {
  const [W_, H_] = [area.w, area.h];
  const clearOfRibbons = (cx: number, cy: number, r: number) => ribbons.every((b) => b.pts.every((p) => Math.hypot(p[0] - cx, p[1] - cy) > r + b.half + SPACE.s));
  const text = caption && grow(caption, TEXT_CLEARANCE);
  const clearOfText = (cx: number, cy: number, r: number) => !text || Math.hypot(Math.max(text.left - cx, 0, cx - text.right), Math.max(text.top - cy, 0, cy - text.bottom)) > r;
  const inPanel = (cx: number, r: number) => (edges.left || cx - r >= 0) && (edges.right || cx + r <= W_);
  const pieces: Piece[] = [];
  if (plan.big) {
    const [ax, ay] = plan.big.at(phones as Measured[]);
    for (let size = plan.big.max; size >= plan.big.min; size -= SPACE.xs) {
      const r = size / 2, cx = Math.min(Math.max(ax, edges.left ? -Infinity : r), edges.right ? Infinity : W_ - r);
      if (clearOfRibbons(cx, ay, r) && clearOfText(cx, ay, r)) {
        pieces.push({ name: plan.big.name ?? "cookie_12sided", x: cx - r, y: ay - r, size, color: plan.big.color ?? SHAPE_BEHIND, rotate: plan.big.rotate ?? 12, behind: true });
        break;
      }
    }
  }
  let seed = plan.seed;
  const rand = () => ((seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0) / 4294967296);
  const clearOfPhones = (cx: number, cy: number, r: number) => phones.every((m: Obstacle) => !inside(m.visible, [cx, cy]) && distanceTo(m.visible, [cx, cy]) > r + SPACE.s);
  const small: Piece[] = [];
  for (let k = 0; small.length < plan.small && k < 8000; k++) {
    const size = SPACE.xl + rand() * (SPACE.xxl + SPACE.l), r = size / 2;
    const cx = -r * 0.4 + rand() * (W_ + r * 0.8), cy = -r * 0.4 + rand() * (H_ + r * 0.8);
    // At least half of a shape shows; a sliver at an edge reads as a mistake.
    if (cx < r * 0.2 || cx > W_ - r * 0.2 || cy < r * 0.2 || cy > H_ - r * 0.2) continue;
    if (!inPanel(cx, r) || !clearOfPhones(cx, cy, r) || !clearOfRibbons(cx, cy, r) || !clearOfText(cx, cy, r)) continue;
    if (pieces.some((p) => Math.hypot(p.x + p.size / 2 - cx, p.y + p.size / 2 - cy) < r + p.size / 2 + SPACE.m)) continue;
    if (effects.some(([x, y, er]) => Math.hypot(x - cx, y - cy) < r + er + SPACE.m)) continue;
    if (small.some((p) => Math.hypot(p.x + p.size / 2 - cx, p.y + p.size / 2 - cy) < (p.size + size) / 2 + SPACE.m)) continue;
    small.push({ name: SHAPE_NAMES[(small.length + (plan.offset ?? 0)) % SHAPE_NAMES.length], x: cx - r, y: cy - r, size, color: SHAPE_COLORS[small.length % SHAPE_COLORS.length], rotate: rand() * 360 });
  }
  return [...pieces, ...small];
};

/** The hero's big shape sat SPACE.xxl in from its right edge, on the upper golden line divided by φ. */
export const HERO_BIG_AT = (): Pt => [W - SPACE.xxl, GOLDEN.y[0] / PHI];
