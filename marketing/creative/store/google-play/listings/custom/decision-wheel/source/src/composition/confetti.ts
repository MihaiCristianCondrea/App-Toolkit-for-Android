/**
 * Confetti scattered over a canvas, deterministic for a seed. Mitchell's best-candidate sampling spreads the
 * pieces evenly without a pattern (blue noise): each new piece is the candidate farthest from the pieces
 * already placed, so there are no clumps, rows or rings. Candidates are kept more often near the subject
 * (`focus`) and less often far from it, so the scatter gathers loosely around it and thins towards the
 * edges. Pieces mix strips, squares and dots, sizes and angles, in the campaign's colours.
 */
import type { Box } from "./geometry.ts";
import { CONFETTI } from "../theme.ts";

export type Confetto = { x: number; y: number; w: number; h: number; rotate: number; color: string; kind: "strip" | "square" | "dot" };

export const scatterConfetti = (o: {
  width: number; height: number; seed: number; count: number; focus: Box;
  /** True where a piece of radius r may sit. */
  free: (x: number, y: number, r: number) => boolean;
  /** Margin kept from the canvas edges. */
  margin?: number;
}): Confetto[] => {
  let s = o.seed >>> 0 || 1;
  const rand = () => ((s = (Math.imul(s, 1664525) + 1013904223) >>> 0) / 4294967296);
  const margin = o.margin ?? 21, f = o.focus;
  const away = (x: number, y: number) => Math.hypot(Math.max(f.left - x, 0, x - f.right), Math.max(f.top - y, 0, y - f.bottom));
  const reach = Math.max(o.width, o.height) * 0.45;
  const out: Confetto[] = [];
  for (let tries = 0; out.length < o.count && tries < o.count * 40; tries++) {
    // The piece is chosen first, so its own radius decides where it fits.
    const kind = (["strip", "strip", "strip", "square", "dot", "dot"] as const)[Math.floor(rand() * 6)];
    const size = 0.75 + rand() * 0.6;
    const [w, h] = kind === "strip" ? [12 * size, 30 * size] : kind === "square" ? [17 * size, 17 * size] : [14 * size, 14 * size];
    const r = Math.hypot(w, h) / 2;
    let best: { x: number; y: number; d: number } | undefined;
    for (let c = 0; c < 14; c++) {
      const x = margin + r + rand() * (o.width - 2 * (margin + r)), y = margin + r + rand() * (o.height - 2 * (margin + r));
      // Denser near the subject: keep with probability 1 up close, falling to 0.3 far away.
      if (rand() > 1 - 0.7 * Math.min(away(x, y) / reach, 1)) continue;
      if (!o.free(x, y, r)) continue;
      const d = Math.min(Infinity, ...out.map((p) => Math.hypot(p.x - x, p.y - y)));
      if (!best || d > best.d) best = { x, y, d };
    }
    if (!best || best.d < 34) continue;
    out.push({ x: best.x, y: best.y, w, h, rotate: rand() * 360, color: CONFETTI[Math.floor(rand() * CONFETTI.length)], kind });
  }
  return out;
};
