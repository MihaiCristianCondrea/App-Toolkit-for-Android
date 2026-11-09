import { spring } from "remotion";
import timeline from "../timeline.json";

export const FPS = timeline.fps;
export const W = timeline.design.width, H = timeline.design.height;
export const B = timeline.beats;
export const clamp = (x: number, a = 0, b = 1) => Math.min(b, Math.max(a, x));
export const mix = (a: number, b: number, p: number) => a + (b - a) * p;

/** A cubic Bézier easing, as CSS defines it. */
const bezier = (x1: number, y1: number, x2: number, y2: number) => (x: number) => {
  if (x <= 0) return 0; if (x >= 1) return 1;
  let t = x;
  for (let i = 0; i < 8; i++) {
    const cx = 3 * x1 * t * (1 - t) ** 2 + 3 * x2 * t * t * (1 - t) + t ** 3 - x;
    const d = 3 * x1 * (1 - t) ** 2 + 6 * (x2 - x1) * t * (1 - t) + 3 * (1 - x2) * t * t;
    if (Math.abs(d) < 1e-6) break; t -= cx / d;
  }
  return 3 * y1 * t * (1 - t) ** 2 + 3 * y2 * t * t * (1 - t) + t ** 3;
};
/** Material 3's emphasised decelerate (entering) and accelerate (leaving) curves. */
export const enter = bezier(0.05, 0.7, 0.1, 1);
export const leave = bezier(0.3, 0, 0.8, 0.15);
export const standard = bezier(0.2, 0, 0, 1);
/**
 * The camera's push into a phone and its pull out of the next: the push ends at the speed the pull starts
 * (about 3.4 of zoom per second with the trailer's 0.7 s push and 1 s pull), so the cut reads as one move.
 */
export const pushIn = bezier(0.4, 0, 0.75, 0.4);
export const pullOut = bezier(0.2, 0.68, 0.3, 1);

/** 0 to 1 from `start` over `dur` seconds, eased. */
export const ramp = (t: number, start: number, dur: number, ease = enter) => ease(clamp((t - start) / dur));
/** A Material spring that starts at `start` seconds: 0 before, settling at 1 (with overshoot). */
export const pop = (t: number, start: number, damping = 11, stiffness = 170) =>
  t < start ? 0 : spring({ frame: (t - start) * FPS, fps: FPS, config: { damping, stiffness, mass: 0.8 } });
/** Deterministic noise in -1..1 for slow drift. */
export const drift = (t: number, seed: number, speed = 0.35) => Math.sin(t * speed * 2 * Math.PI + seed * 12.9898) * 0.6 + Math.sin(t * speed * 3.7 + seed * 78.233) * 0.4;
