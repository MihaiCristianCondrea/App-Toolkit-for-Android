/**
 * The five compositions. Each states its art direction as targets: which point of which phone lands where,
 * what the camera may do, which hard rules are its own, and what it prefers. scripts/solve-layout.ts
 * searches each composition's candidates, drops those that break a hard rule (validate.ts plus `hard`),
 * and keeps the one with the lowest preference score. Golden lines and the Fibonacci spacing scale are
 * targets where a composition asks for them, not rules every screenshot has to follow.
 */
import { CAPTION, GOLDEN, H, SPACE, W, centre, fitPose, measure, overlapArea } from "./geometry.ts";
import type { CaptionAt, Measured, Pose, ScreenUi } from "./geometry.ts";
import type { ShapePlan } from "./effects.ts";
import { HERO_BIG_AT } from "./effects.ts";

export type Terms = Record<string, number>;
export type ShotSpec = {
  /** The screenshot's id in strings/<locale>.json. */
  id: string;
  intent: string;
  caption: CaptionAt;
  /** The phones' screens, back to front. */
  screens: string[];
  effects?: "sound";
  shapes: ShapePlan;
  /** Every layout worth trying: one pose per phone, back to front. */
  candidates: (ui: Record<string, ScreenUi>) => Iterable<Pose[]>;
  /** Hard rules of this composition only; the shared ones are in validate.ts. */
  hard?: (ms: Measured[]) => string[];
  /** Preferences, each normalised so 1 is the edge of its tolerance; the solver minimises their sum. */
  score: (ms: Measured[]) => Terms;
};

const range = (from: number, to: number, step: number) => Array.from({ length: Math.round((to - from) / step) + 1 }, (_, i) => Math.round((from + i * step) * 1000) / 1000);
const sq = (x: number) => x * x;
/** The line a phone's top may not rise above under a top caption: SPACE.xl below its text. */
const UNDER_TOP_CAPTION = CAPTION.top.block.bottom + SPACE.xl;
const uiCentre = (m: Measured, name: string) => centre(m.critical[name]);
const width = (m: Measured) => m.box.right - m.box.left;

/** One phone: every camera shape on the grid, at every width, fitted so `x` and `y` meet their targets. */
function* single(ui: ScreenUi, grid: { pitch: number[]; yaw: number[]; roll: number[]; perspective: number[]; width: number[] }, fit: (w: number) => Parameters<typeof fitPose>[2]) {
  for (const pitch of grid.pitch) for (const yaw of grid.yaw) for (const roll of grid.roll) for (const perspective of grid.perspective) for (const w of grid.width)
    yield [fitPose({ pitch, yaw, roll, perspective }, ui, fit(w))];
}

export const SHOTS: ShotSpec[] = [
  {
    id: "water-eject",
    intent: "Hero: a large phone centred and running off the top, leaning back so its speaker edge faces the camera; sound and bubbles leave both speakers; the caption stands at the foot.",
    caption: "bottom",
    screens: ["water-eject"],
    effects: "sound",
    shapes: { seed: 11, small: 5, big: { at: HERO_BIG_AT, max: W, min: SPACE.xxl * 2 } },
    *candidates(ui) {
      // The search and targets that solved the hero: silhouette centroid at 0.50 W, speaker edge at 0.70 H,
      // the visible silhouette 0.78 W wide.
      for (let pitch = 6; pitch <= 40; pitch += 1) for (let yaw = -24; yaw <= 0; yaw += 1) for (let roll = -10; roll <= 0; roll += 0.5)
        for (const perspective of [1200, 1500, 1800, 2100, 2400, 2700, 3000])
          yield [fitPose({ pitch, yaw, roll, perspective }, ui["water-eject"], { width: 0.78 * W, x: (m) => m.centroid[0], xTarget: W / 2, y: (m) => m.focal[1], yTarget: 0.7 * H })];
    },
    hard: ([m]) => [
      ...(m.full.top > -SPACE.xl ? ["the hero phone must run beyond the top edge"] : []),
      ...(m.box.bottom > CAPTION.bottom.block.top - SPACE.xl ? ["the hero phone must stay SPACE.xl clear of the caption"] : []),
      ...(m.thickness <= 0 ? ["the speaker edge must face the camera"] : []),
      ...(Math.abs(m.focal[0] - W / 2) > 0.04 * W ? ["the speaker edge must be centred within 0.04 W"] : []),
      ...Object.entries(m.critical).filter(([, q]) => q.some(([, y]) => y < SPACE.xl)).map(([k]) => `${k} must stay SPACE.xl below the top edge`),
    ],
    score: ([m]) => ({
      thickness: 4 * sq((m.thickness - 0.035 * W) / (0.035 * W)),
      angle: sq((m.angle + 4) / 4),
      roll: 0.5 * sq(m.pose.roll / 10),
      side: 0.3 * sq((m.side - 0.012 * W) / (0.012 * W)),
      focal: 0.5 * sq((m.focal[0] - W / 2) / (0.04 * W)),
      perspective: 0.2 * sq((m.pose.perspective - 1800) / 1200),
      keystone: 2 * sq((m.keystone - 0.9) / 0.05),
    }),
  },
  {
    id: "water-running",
    intent: "A close crop on the running cycle: a large phone leaning right, running off the bottom, the countdown on the lower golden line.",
    caption: "top",
    screens: ["water-running"],
    shapes: { seed: 23, offset: 2, small: 3 },
    candidates: (ui) => single(ui["water-running"], { pitch: range(-8, 6, 2), yaw: range(2, 14, 2), roll: range(0, 6, 1), perspective: [3000], width: [0.8 * W, 0.83 * W, 0.86 * W, 0.89 * W] },
      (w) => ({ width: w, x: (m) => m.centroid[0], xTarget: W / 2, y: (m) => uiCentre(m, "readout")[1], yTarget: GOLDEN.yLow, minTop: UNDER_TOP_CAPTION })),
    hard: ([m]) => (m.box.bottom < H ? ["the close crop must run off the bottom edge"] : []),
    score: ([m]) => ({
      size: sq((0.89 * W - width(m)) / (0.03 * W)),
      countdown: sq((uiCentre(m, "readout")[1] - GOLDEN.yLow) / SPACE.xl),
      yaw: sq((m.pose.yaw - 8) / 6),
      roll: sq((m.pose.roll - 3) / 3),
      pitch: sq(m.pose.pitch / 8),
    }),
  },
  {
    id: "water-stop",
    intent: "The whole phone turned to show its side, the Stop button on the lower golden line, room around it.",
    caption: "top",
    screens: ["water-stop"],
    shapes: { seed: 5, offset: 4, small: 3, big: { at: ([m]) => m.centroid, max: 0.9 * W, min: 0.5 * W } },
    candidates: (ui) => single(ui["water-stop"], { pitch: range(-6, 6, 2), yaw: range(-30, -14, 2), roll: range(0, 6, 1), perspective: [3000], width: range(0.5 * W, 0.62 * W, 0.03 * W) },
      (w) => ({ width: w, x: (m) => m.centroid[0], xTarget: W / 2, y: (m) => uiCentre(m, "button")[1], yTarget: GOLDEN.yLow, minTop: UNDER_TOP_CAPTION })),
    hard: ([m]) => (m.full.bottom > H - SPACE.l ? ["the whole phone must show, SPACE.l above the bottom edge"] : []),
    score: ([m]) => ({
      size: sq((width(m) - 0.6 * W) / (0.04 * W)),
      stop: sq((uiCentre(m, "button")[1] - GOLDEN.yLow) / SPACE.xl),
      yaw: sq((m.pose.yaw + 22) / 6),
      roll: sq((m.pose.roll - 3) / 3),
      pitch: sq(m.pose.pitch / 8),
    }),
  },
  {
    id: "quick-tools",
    intent: "Two tools as large as their UI allows, tilted against each other (a V or a Λ, whichever keeps their UI clear): Unit Converter behind on the left, Quick Tools in front on the right, overlapping a little.",
    caption: "top",
    screens: ["unit-converter", "quick-tools"],
    shapes: { seed: 41, offset: 1, small: 3 },
    *candidates(ui) {
      // Each phone's camera shape is chosen on its own preferences first (three best each), then the
      // pair's positions are searched together.
      const shapes = (screen: string, rolls: number[], lean: number) => {
        const out: { shape: { pitch: number; yaw: number; roll: number; perspective: number }; w: number; s: number }[] = [];
        for (const pitch of [-4, 0, 4]) for (const yaw of [-8, -4, 0, 4, 8]) for (const roll of rolls) for (const w of [0.44 * W, 0.46 * W, 0.48 * W, 0.5 * W])
          out.push({ shape: { pitch, yaw, roll, perspective: 3000 }, w, s: sq(pitch / 6) + sq(yaw / 8) + sq((roll - lean) / 3) + sq((w - 0.5 * W) / (0.03 * W)) });
        return out.sort((a, b) => a.s - b.s).slice(0, 4);
      };
      // Both arrangements: tops apart (a V, the back phone rolled left) or tops together (a Λ).
      const arrangements = [-1, 1].map((sign) => [shapes("unit-converter", range(6, 12, 1).map((r) => sign * r), sign * 8), shapes("quick-tools", range(6, 12, 1).map((r) => -sign * r), -sign * 8)]);
      const fit = (screen: string, c: (typeof arrangements)[number][number][number], x: number, top: number) =>
        fitPose(c.shape, ui[screen], { width: c.w, x: (m) => m.centroid[0], xTarget: x, y: (m) => m.full.top, yTarget: top });
      for (const [back, front] of arrangements) for (const b of back) for (const f of front) for (const xb of range(0.2 * W, 0.4 * W, 0.02 * W)) for (const xf of range(0.6 * W, 0.8 * W, 0.02 * W))
        for (const top of [0, SPACE.m, SPACE.xl].map((d) => UNDER_TOP_CAPTION + d)) for (const drop of [-SPACE.l, 0, SPACE.l, SPACE.xl])
          yield [fit("unit-converter", b, xb, top), fit("quick-tools", f, xf, top + drop)];
    },
    score: ([b, f]) => {
      const total = b.area + f.area, cx = (b.area * b.centroid[0] + f.area * f.centroid[0]) / total;
      const overlap = (b.area + f.area - unionArea(b, f)) / b.area;
      return {
        balance: sq((cx - W / 2) / (0.02 * W)),
        overlap: sq((overlap - 0.06) / 0.04),
        stagger: sq(((f.full.top - b.full.top) / H - 0.03) / 0.03),
        lean: sq((Math.abs(b.pose.roll) - 8) / 3) + sq((Math.abs(f.pose.roll) - 8) / 3) + (Math.sign(b.pose.roll) === Math.sign(f.pose.roll) ? 100 : 0),
        turn: sq(b.pose.yaw / 8) + sq(f.pose.yaw / 8) + sq(b.pose.pitch / 6) + sq(f.pose.pitch / 6),
        size: sq((width(b) - 0.5 * W) / (0.04 * W)) + sq((width(f) - 0.5 * W) / (0.04 * W)),
      };
    },
  },
  {
    id: "level",
    intent: "Bubble Level alone, large and tilted, the level's target on the upper golden line where the caption allows.",
    caption: "top",
    screens: ["level"],
    shapes: { seed: 67, offset: 6, small: 3 },
    candidates: (ui) => single(ui.level, { pitch: range(-6, 6, 2), yaw: range(-10, 10, 2), roll: range(-8, -2, 1), perspective: [3000], width: range(0.72 * W, 0.84 * W, 0.03 * W) },
      (w) => ({ width: w, x: (m) => m.centroid[0], xTarget: W / 2, y: (m) => uiCentre(m, "level")[1], yTarget: GOLDEN.y[1], minTop: UNDER_TOP_CAPTION })),
    score: ([m]) => ({
      size: sq((width(m) - 0.8 * W) / (0.04 * W)),
      level: sq((uiCentre(m, "level")[1] - GOLDEN.y[1]) / SPACE.xl),
      roll: sq((m.pose.roll + 5) / 3),
      yaw: sq(m.pose.yaw / 8),
      pitch: sq(m.pose.pitch / 8),
    }),
  },
];

/** The area two phones cover together, from their visible silhouettes (the front one convex). */
export const unionArea = (a: Measured, b: Measured) => a.area + b.area - overlapArea(a.visible, b.visible);

export const measureAll = (poses: Pose[], screens: string[], ui: Record<string, ScreenUi>) => poses.map((p, i) => measure(p, ui[screens[i]]));
