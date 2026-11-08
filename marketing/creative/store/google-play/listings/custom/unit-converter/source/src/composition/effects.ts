import {GOLDEN,PHI,SPACE,TEXT_CLEARANCE,W,H,distanceTo,gapToBox,grow,inside} from "./geometry.ts";
import type {Box,Measured,Pt} from "./geometry.ts";
import type {Ribbon} from "./ribbons.ts";
import {SHAPE_BEHIND,SHAPE_TONES} from "../theme.ts";
export type Arc={pts:Pt[];ring:number};
export type Bubble=[number,number,number];
/** A MaterialShape, as the kit names it, with its box; `behind` marks one drawn behind the phones on purpose. */
export type Piece = { name: string; x: number; y: number; size: number; color: string; rotate: number; behind?: boolean };

/** MaterialShapes in the campaign palette. */
const SHAPE_NAMES = ["cookie_9sided", "flower", "clover_4leaf", "soft_burst", "gem", "pill", "sunny", "cookie_6sided"];
const SHAPE_COLORS = SHAPE_TONES;

/**
 * How a screenshot's shapes are placed. `big` puts one shape behind the phones, centred at `at` but moved
 * in so it never crosses a divider, as large as it can be (up to `max`) while it stays clear of the
 * ribbons; it is dropped below `min`. `small` scatters that many smaller shapes; `seed` varies the scatter
 * and `offset` the first shape's name. `size` and `colors` optionally narrow the scatter's range;
 * `contained` keeps small pieces whole. Fixed pieces passed to placeShapes are included in the
 * result and reserve their clearance before scattering.
 */
export type ShapePlan = { seed: number; offset?: number; small: number; size?: [number, number]; colors?: string[]; contained?: boolean; big?: { at: (phones: Measured[]) => Pt; max: number; min: number; name?: string; color?: string; rotate?: number } };

/**
 * Every shape keeps SPACE.s clear of the background's glass ribbons and TEXT_CLEARANCE clear of the
 * caption's text block, and stays inside its own screenshot left to right, except at the outer ends of the
 * strip (`edges`); any may run off the top or bottom, but at least half of each small shape shows. Small shapes also keep SPACE.s from every phone's
 * visible silhouette and SPACE.m from the effects and each other.
 */
/** Anything shapes keep clear of: its visible outline, as a measured phone or a device box gives it. */
export type Obstacle = { visible: Pt[] };
export const placeShapes = (plan: ShapePlan, phones: Obstacle[], caption: Box | null, effects: Bubble[], ribbons: Ribbon[], edges: { left: boolean; right: boolean }, area = { w: W, h: H }, fixed: Piece[] = []): Piece[] => {
  const [W_, H_] = [area.w, area.h];
  const clearOfRibbons = (cx: number, cy: number, r: number) => ribbons.every((b) => b.pts.every((p) => Math.hypot(p[0] - cx, p[1] - cy) > r + b.half + SPACE.s));
  const text = caption && grow(caption, TEXT_CLEARANCE);
  const clearOfText = (cx: number, cy: number, r: number) => !text || Math.hypot(Math.max(text.left - cx, 0, cx - text.right), Math.max(text.top - cy, 0, cy - text.bottom)) > r;
  const inPanel = (cx: number, r: number) => (edges.left || cx - r >= 0) && (edges.right || cx + r <= W_);
  const pieces: Piece[] = [...fixed];
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
  const clearOfPhones = (cx: number, cy: number, r: number) => phones.every((m: Obstacle) => plan.contained ? gapToBox(m.visible, {left:cx-r,top:cy-r,right:cx+r,bottom:cy+r}) > SPACE.s : !inside(m.visible, [cx, cy]) && distanceTo(m.visible, [cx, cy]) > r + SPACE.s);
  const small: Piece[] = [];
  for (let k = 0; small.length < plan.small && k < 8000; k++) {
    const [min,max] = plan.size ?? [SPACE.xl, SPACE.xl + SPACE.xxl + SPACE.l];
    const size = min + rand() * (max - min), r = size / 2;
    const cx = -r * 0.4 + rand() * (W_ + r * 0.8), cy = -r * 0.4 + rand() * (H_ + r * 0.8);
    // At least half of a shape shows; a sliver at an edge reads as a mistake.
    const edge = plan.contained ? r : r * 0.2;
    if (cx < edge || cx > W_ - edge || cy < edge || cy > H_ - edge) continue;
    if (!inPanel(cx, r) || !clearOfPhones(cx, cy, r) || !clearOfRibbons(cx, cy, r) || !clearOfText(cx, cy, r)) continue;
    if (pieces.some((p) => Math.hypot(p.x + p.size / 2 - cx, p.y + p.size / 2 - cy) < r + p.size / 2 + SPACE.m)) continue;
    if (effects.some(([x, y, er]) => Math.hypot(x - cx, y - cy) < r + er + SPACE.m)) continue;
    if (small.some((p) => Math.hypot(p.x + p.size / 2 - cx, p.y + p.size / 2 - cy) < (p.size + size) / 2 + SPACE.m)) continue;
    small.push({ name: SHAPE_NAMES[(small.length + (plan.offset ?? 0)) % SHAPE_NAMES.length], x: cx - r, y: cy - r, size, color: (plan.colors ?? SHAPE_COLORS)[small.length % (plan.colors ?? SHAPE_COLORS).length], rotate: rand() * 360 });
  }
  return [...pieces, ...small];
};

/** The hero's big shape sat SPACE.xxl in from its right edge, on the upper golden line divided by φ. */
export const HERO_BIG_AT = (): Pt => [W - SPACE.xxl, GOLDEN.y[0] / PHI];
