/**
 * The Decision Wheel scene's motion, in one place for the picture (Trailer.tsx), the soundtrack's ratchet
 * (scripts/compose.mjs) and the checks (scripts/verify-wheel.mjs). It imports only the timeline, the phone's
 * geometry and the easing curves, so Node runs it through esbuild.
 *
 * One camera move, no exit. The phone stays where the cut finds it, turned a little. The camera creeps in from
 * the cut and then accelerates towards the wheel, keeping the wheel's centre in focus while the phone turns face
 * on. It passes through: the phone's edges, then its bezel, leave the frame by framing alone, never by the phone
 * moving, and only then does the lilac world grow from behind the wheel. The wheel overshoots the frame a little
 * and settles to its hero size while it slows to Music. The spin is the app's: 5.25 turns in 3 seconds.
 *
 * Every curve here is continuous in value and velocity, and nothing is rounded: the checks sample it at 90 fps.
 */
import timeline from "./timeline.json";
import { projectScreen } from "./motifs/geometry";
import { enter } from "./lib/anim";

const B = timeline.beats;
const clamp = (x: number, a = 0, b = 1) => Math.min(b, Math.max(a, x));
const mix = (a: number, b: number, p: number) => a + (b - a) * p;
/** Smoothstep and smootherstep: zero velocity at both ends (smootherstep also zero acceleration). */
export const smooth01 = (x: number) => x * x * (3 - 2 * x);
export const smoother01 = (x: number) => x * x * x * (x * (6 * x - 15) + 10);

export const CHOICES = ["Walk", "Read", "Cook", "Draw", "Music", "Rest"];
export const WINNER = 4;
/**
 * The sheet's wheel in the phone frame's pixels, as it is painted. The rendered Play screen lays its disc out at
 * (108.61, 518.78), 248 px square; Blink paints a box at its layout position rounded to whole pixels of the
 * phone's own space, before the phone is scaled, so the disc, its plate and its pointer are drawn about
 * (233, 643). Scaled 3.4 times, the difference is 1.3 px; scripts/verify-wheel.mjs measures it.
 */
export const SHEET_WHEEL = { cx: Math.round(108.61) + 124, cy: Math.round(518.78) + 124, box: 248 };
/** How the cut finds the phone: turned a little away, so the move can bring it face on. */
export const PHONE_TURN = { yaw: -12, pitch: 6 };
/** Where the wheel's centre is on screen: at the cut, when the camera has passed through, and at rest. */
export const FOCUS = { start: [990, 672], peak: [960, 540], hero: [960, 544] } as const;
/** The wheel's size at rest, once it is the scene: it fills the frame's height, pointer and all. */
export const HERO_BOX = 980;
/** The phone frame's width and its screen's inset, in its own pixels: the bezel leaves the frame at 960 / (232.61 - 17.6) = 4.47. */
export const PHONE_EDGE = { half: 232.61, screen: 17.6 };

type Key = [t: number, value: number, slope: number];
/** A cubic Hermite through keys (time, value, slope per second): continuous in value and in velocity. */
export const hermite = (keys: Key[], t: number) => {
  if (t <= keys[0][0]) return keys[0][1];
  if (t >= keys[keys.length - 1][0]) return keys[keys.length - 1][1];
  let i = 0;
  while (t > keys[i + 1][0]) i++;
  const [t0, p0, v0] = keys[i], [t1, p1, v1] = keys[i + 1], h = t1 - t0, s = (t - t0) / h;
  return (2 * s ** 3 - 3 * s ** 2 + 1) * p0 + (s ** 3 - 2 * s ** 2 + s) * h * v0 + (-2 * s ** 3 + 3 * s ** 2) * p1 + (s ** 3 - s ** 2) * h * v1;
};

/**
 * The phone's scale on screen, in log space (so equal steps feel like equal pushes). From the cut it creeps in,
 * accelerates, and is still moving when the phone's bezel crosses the frame's edges (4.47: the wheel sits on the
 * frame's centre line, 215 px from the screen's edge); it stops just beyond, then the wheel settles to HERO_BOX.
 * Each segment meets the Fritsch-Carlson condition, so the push never reverses before the settle.
 */
const [c0, cEdge, cPeak] = B.wheelCamera, [set0, set1] = B.wheelSettle;
export const SCALE_KEYS: Key[] = [
  [timeline.scenes.find((s) => s.id === "wheel")!.start, Math.log(0.9), 0.06],
  [c0, Math.log(0.97), 0.12],
  [cEdge, Math.log(4.47), 0.6],
  [cPeak, Math.log(4.47) + 0.6 * (B.wheelCamera[2] - B.wheelCamera[1]) / 2, 0],
  [set1, Math.log(HERO_BOX / SHEET_WHEEL.box), 0],
];
if (Math.abs(set0 - cPeak) > 1e-9) throw new Error("wheelSettle must start when the camera stops (wheelCamera[2]).");

/** The scale the wheel is drawn at, in the phone frame's pixels per screen pixel. */
export const wheelScale = (t: number) => Math.exp(hermite(SCALE_KEYS, t));

/** The wheel's centre on screen: it travels to the frame's centre in step with the push, then settles. */
export const wheelFocus = (t: number): [number, number] => {
  const ls0 = SCALE_KEYS[0][1], lsPeak = SCALE_KEYS[3][1];
  if (t <= cPeak) {
    const lam = clamp((hermite(SCALE_KEYS, t) - ls0) / (lsPeak - ls0));
    return [mix(FOCUS.start[0], FOCUS.peak[0], lam), mix(FOCUS.start[1], FOCUS.peak[1], lam)];
  }
  const e = smooth01(clamp((t - set0) / (set1 - set0)));
  return [mix(FOCUS.peak[0], FOCUS.hero[0], e), mix(FOCUS.peak[1], FOCUS.hero[1], e)];
};

/** The phone's turn: held from the cut, then face on by wheelFace, with no jolt at either end. */
export const wheelTurn = (t: number) => {
  const e = smoother01(clamp((t - c0) / (B.wheelFace - c0)));
  return { yaw: PHONE_TURN.yaw * (1 - e), pitch: PHONE_TURN.pitch * (1 - e) };
};

/** The spin: a small wind-up, then it speeds up from rest inside the app and slows to a stop on Music. */
export const spinCurve = (u: number) => { const a = 0.15, g = (u < a ? (u * u) / (2 * a) : u - a / 2) / (1 - a / 2); return 1 - (1 - g) ** 3; };
export const wheelRotation = (t: number) => {
  const [w0, w1] = B.wheelWindup, [s0, s1] = B.wheelSpin, back = timeline.wheel.windup;
  if (t < w0) return 0;
  if (t < s0) return -back * smooth01(clamp((t - w0) / (w1 - w0)));
  return -back + (timeline.wheel.turn + back) * spinCurve(clamp((t - s0) / (s1 - s0)));
};

export type Pose = { x: number; y: number; scale: number; yaw: number; pitch: number; roll: number };
/** Where the wheel's centre lands, relative to the phone's centre, for a phone at scale 1 in this turn. */
const wheelOffset = (yaw: number, pitch: number) => projectScreen({ pitch, yaw, roll: 0, perspective: 3000, scale: 1, x: 0, y: 0 }, [SHEET_WHEEL.cx, SHEET_WHEEL.cy]);

/**
 * Everything the scene draws, at time t. The phone is placed so its wheel's centre is exactly at the focus; once
 * the camera stops it is held there (the lilac covers it before the wheel settles). From wheelFace the phone is
 * face on and `replica` is the same wheel drawn over it at the same centre, size and angle, so it can stay sharp
 * and in front of the lilac; the app's own wheel stops turning under it (`inApp`), hidden.
 */
export const wheelState = (t: number) => {
  const held = Math.min(t, cPeak), turn = wheelTurn(held), s = wheelScale(held), [fx, fy] = wheelFocus(held), [ox, oy] = wheelOffset(turn.yaw, turn.pitch);
  const phone: Pose = { x: fx - s * ox, y: fy - s * oy, scale: s, yaw: turn.yaw, pitch: turn.pitch, roll: 0 };
  const [cx, cy] = wheelFocus(t), rotation = wheelRotation(t);
  const [r0, r1] = B.wheelReveal;
  return {
    phone,
    /** The camera's zoom since the cut, and where the wheel was then: the decoration rides the same camera. */
    camera: { zoom: s / Math.exp(SCALE_KEYS[0][1]), from: FOCUS.start, to: [fx, fy] as [number, number] },
    handed: t >= B.wheelFace,
    inApp: wheelRotation(Math.min(t, B.wheelFace)),
    replica: { cx, cy, box: SHEET_WHEEL.box * wheelScale(t), rotation },
    reveal: clamp((t - r0) / (r1 - r0)),
    /** The lilac's two shapes (cookie_12sided, so their inner radius is 0.4 of the size): a ring of the container colour, then the ground. */
    revealSizes: [0, 0.1].map((delay) => mix(SHEET_WHEEL.box * wheelScale(t), 3000, enter(clamp((t - r0 - delay) / (r1 - r0 - delay))))) as [number, number],
    /** The phone's own shadow, which would otherwise grow with the push into a dark band at the frame's edges. */
    shadow: 1 - smooth01(clamp((s - 2) / 2)),
  };
};
