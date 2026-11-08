/**
 * Assembles one screenshot from its solved layout: the phones measured in their poses, the effects and
 * shapes placed around them, and the validation report. Shared by the render (src/Shot.tsx) and the
 * verifier (scripts/verify.mjs), so both judge the same composition.
 */
import { CAPTION, H, W } from "./geometry.ts";
import type { Pose, ScreenUi } from "./geometry.ts";
import { bubbles, placeConfetti, placeShapes, soundArcs } from "./effects.ts";
import type { Arc, Bubble } from "./effects.ts";
import { ribbonLines } from "./ribbons.ts";
import { SHOTS, measureAll } from "./shots.ts";
import { validate } from "./validate.ts";

export type Layout = { shots: { id: string; poses: Pose[] }[] };

export const composeShot = (index: number, panels: number, layout: Layout, ui: Record<string, ScreenUi>) => {
  const spec = SHOTS[index];
  const poses = layout.shots[index].poses;
  const phones = measureAll(poses, spec.screens, ui);
  const block = CAPTION[spec.caption].block;
  const arcs: Arc[] = spec.effects === "sound" ? soundArcs(phones[0], block) : [];
  const spheres: Bubble[] = spec.effects === "sound" ? bubbles(phones[0], block) : [];
  // This screenshot's stretch of the strip's ribbons, in its own coordinates.
  const ribbons = ribbonLines(panels * W, H, panels).map((r) => ({ ...r, pts: r.pts.map(([x, y]) => [x - index * W, y] as [number, number]) }));
  const edges = { left: index === 0, right: index === panels - 1 };
  const effects: Bubble[] = [...arcs.flatMap((a) => a.pts).map(([x, y]) => [x, y, 4] as Bubble), ...spheres];
  const shapes = placeShapes(spec.shapes, phones, block, effects, ribbons, edges);
  const confetti = placeConfetti(phones, block, shapes, index);
  effects.push(...confetti.map(p => [p.x,p.y,Math.hypot(p.w,p.h)/2] as Bubble));
  const report = validate(spec, phones, shapes, effects, ribbons, edges);
  return { spec, poses, phones, arcs, spheres, shapes, confetti, report };
};
