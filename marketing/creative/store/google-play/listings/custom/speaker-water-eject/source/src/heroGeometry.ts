/**
 * The hero's geometry, shared by the solver (scripts/solve-hero.ts), the render and its debug overlay.
 *
 * The 3D Pixel is projected exactly as Material-Next's device frame draws it: the phone turns about
 * its centre (yaw about the vertical axis, then pitch about the horizontal one, in CSS's axes with y
 * down and z towards the viewer), sits the display's depth behind the screen plane, and is seen by a
 * camera `perspective` px in front of the frame's centre, the same camera its WebGL renderer uses. Roll
 * and scale are then applied in the picture plane about the frame's centre, which lands at (x, y).
 * Plain TypeScript with no imports, so Node runs the solver from the same file.
 */

export const W = 1080;
export const H = 1920;
export const PHI = (1 + Math.sqrt(5)) / 2;

/** Golden guides: the 1/φ² and 1/φ lines each way, and the golden subdivision of the lower band. */
export const GOLDEN = {
  x: [W / PHI ** 2, W / PHI],
  y: [H / PHI ** 2, H / PHI],
  yLow: H / PHI + (H - H / PHI) / PHI ** 2,
};

/** The spacing scale, in CSS px of the 1080 × 1920 layout (the PNG is rendered at 2×). */
export const SPACE = { xs: 13, s: 21, m: 34, l: 55, xl: 89, xxl: 144 };

/** Minimum side margin for visible critical content: 0.05 W, which is the scale's 55. */
export const SAFE_X = SPACE.l;

/** The caption sits SPACE.xl above the bottom edge; its band is allowed from 0.82 H to that line. */
export const CAPTION_BAND = { left: 120, right: 960, top: 0.82 * H, bottom: H - SPACE.xl };
/** Where the water may play, between the phone and the caption. */
export const WATER_BAND = { top: 0.65 * H, bottom: 0.8 * H };

// The Pixel 9 Pro XL as the device frame draws it: 430 px of display is 70.8 mm.
const MM = 430 / 70.8;
export const FRAME = { width: 76.6 * MM, height: 162.8 * MM, depth: 8.5 * MM, corner: 12.2 * MM };
/** The display's face, in front of the phone's centre. */
const FACE = (8.5 / 2 + 0.1) * MM;
/** The model's speaker holes: four each side of the USB-C port, 10.8 to 18 mm from the centre. */
export const SPEAKER = { left: -14.4 * MM, right: 14.4 * MM };

export type Pose = { pitch: number; yaw: number; roll: number; perspective: number; scale: number; x: number; y: number };
export type Pt = [number, number];
type P3 = [number, number, number];

const rad = (d: number) => (d * Math.PI) / 180;

/** A point of the phone, in px from its centre (x right, y down, z towards the viewer), to the image. */
export const project = (pose: Pose, [px, py, pz]: P3): Pt => {
  const [cy, sy, cp, sp] = [Math.cos(rad(pose.yaw)), Math.sin(rad(pose.yaw)), Math.cos(rad(pose.pitch)), Math.sin(rad(pose.pitch))];
  const x1 = px * cy + pz * sy;
  const z1 = -px * sy + pz * cy;
  const y2 = py * cp - z1 * sp;
  const z2 = py * sp + z1 * cp - FACE;
  const k = pose.perspective / (pose.perspective - z2);
  const [X, Y] = [x1 * k * pose.scale, y2 * k * pose.scale];
  const [cr, sr] = [Math.cos(rad(pose.roll)), Math.sin(rad(pose.roll))];
  return [pose.x + X * cr - Y * sr, pose.y + X * sr + Y * cr];
};

/** A point on the screen, in the frame's CSS px from its top left corner (as hero-ui.json measures it). */
export const projectScreen = (pose: Pose, [sx, sy]: Pt): Pt => project(pose, [sx - FRAME.width / 2, sy - FRAME.height / 2, FACE]);

/** The phone's rounded outline at depth z, clockwise. */
const outline = (z: number, n = 14): P3[] => {
  const [hw, hh, r] = [FRAME.width / 2, FRAME.height / 2, FRAME.corner];
  const corners: [number, number, number][] = [[hw - r, -hh + r, -90], [hw - r, hh - r, 0], [-hw + r, hh - r, 90], [-hw + r, -hh + r, 180]];
  return corners.flatMap(([cx, cy, a0]) => Array.from({ length: n + 1 }, (_, i) => {
    const a = rad(a0 + (90 * i) / n);
    return [cx + r * Math.cos(a), cy + r * Math.sin(a), z] as P3;
  }));
};

const cross = (o: Pt, a: Pt, b: Pt) => (a[0] - o[0]) * (b[1] - o[1]) - (a[1] - o[1]) * (b[0] - o[0]);
/** Andrew's monotone chain. */
export const hull = (pts: Pt[]): Pt[] => {
  const p = [...pts].sort((a, b) => a[0] - b[0] || a[1] - b[1]);
  const lower: Pt[] = [], upper: Pt[] = [];
  for (const q of p) { while (lower.length >= 2 && cross(lower[lower.length - 2], lower[lower.length - 1], q) <= 0) lower.pop(); lower.push(q); }
  for (const q of [...p].reverse()) { while (upper.length >= 2 && cross(upper[upper.length - 2], upper[upper.length - 1], q) <= 0) upper.pop(); upper.push(q); }
  return [...lower.slice(0, -1), ...upper.slice(0, -1)];
};

/** Sutherland–Hodgman against the image rectangle. */
export const clipToImage = (poly: Pt[]): Pt[] => {
  const edges: [(p: Pt) => boolean, (a: Pt, b: Pt) => Pt][] = [
    [(p) => p[0] >= 0, (a, b) => [0, a[1] + ((b[1] - a[1]) * (0 - a[0])) / (b[0] - a[0])]],
    [(p) => p[0] <= W, (a, b) => [W, a[1] + ((b[1] - a[1]) * (W - a[0])) / (b[0] - a[0])]],
    [(p) => p[1] >= 0, (a, b) => [a[0] + ((b[0] - a[0]) * (0 - a[1])) / (b[1] - a[1]), 0]],
    [(p) => p[1] <= H, (a, b) => [a[0] + ((b[0] - a[0]) * (H - a[1])) / (b[1] - a[1]), H]],
  ];
  let out = poly;
  for (const [inside, cut] of edges) {
    const src = out; out = [];
    src.forEach((cur, i) => {
      const prev = src[(i + src.length - 1) % src.length];
      if (inside(cur)) { if (!inside(prev)) out.push(cut(prev, cur)); out.push(cur); }
      else if (inside(prev)) out.push(cut(prev, cur));
    });
  }
  return out;
};

/** Area and area-weighted centroid of a simple polygon. */
export const areaCentroid = (poly: Pt[]) => {
  let a = 0, cx = 0, cy = 0;
  poly.forEach((p, i) => {
    const q = poly[(i + 1) % poly.length], c = p[0] * q[1] - q[0] * p[1];
    a += c; cx += (p[0] + q[0]) * c; cy += (p[1] + q[1]) * c;
  });
  a /= 2;
  return { area: Math.abs(a), centroid: (a ? [cx / (6 * a), cy / (6 * a)] : [0, 0]) as Pt };
};

export const bbox = (pts: Pt[]) => {
  const xs = pts.map((p) => p[0]), ys = pts.map((p) => p[1]);
  return { left: Math.min(...xs), top: Math.min(...ys), right: Math.max(...xs), bottom: Math.max(...ys) };
};

/** The width of a closed polygon along the horizontal line at `y`. */
export const widthAt = (poly: Pt[], y: number) => {
  const xs: number[] = [];
  poly.forEach((p, i) => {
    const q = poly[(i + 1) % poly.length];
    if ((p[1] - y) * (q[1] - y) <= 0 && p[1] !== q[1]) xs.push(p[0] + ((q[0] - p[0]) * (y - p[1])) / (q[1] - p[1]));
  });
  return xs.length ? Math.max(...xs) - Math.min(...xs) : 0;
};

/** Even–odd point-in-polygon test. */
export const inside = (poly: Pt[], [x, y]: Pt) => {
  let c = false;
  poly.forEach((p, i) => { const q = poly[(i + poly.length - 1) % poly.length]; if ((p[1] > y) !== (q[1] > y) && x < ((q[0] - p[0]) * (y - p[1])) / (q[1] - p[1]) + p[0]) c = !c; });
  return c;
};

/** The lowest point of a closed polygon on the vertical line at `x`, or -Infinity if the line misses it. */
export const floorAt = (poly: Pt[], x: number) => {
  let y = -Infinity;
  poly.forEach((p, i) => {
    const q = poly[(i + 1) % poly.length];
    if ((p[0] - x) * (q[0] - x) <= 0 && p[0] !== q[0]) y = Math.max(y, p[1] + ((q[1] - p[1]) * (x - p[0])) / (q[0] - p[0]));
  });
  return y;
};

/** The distance from a point to a closed polygon's outline. */
export const distanceTo = (poly: Pt[], [x, y]: Pt) => Math.min(...poly.map((p, i) => {
  const q = poly[(i + 1) % poly.length], [dx, dy] = [q[0] - p[0], q[1] - p[1]];
  const t = Math.max(0, Math.min(1, ((x - p[0]) * dx + (y - p[1]) * dy) / (dx * dx + dy * dy || 1)));
  return Math.hypot(x - p[0] - t * dx, y - p[1] - t * dy);
}));

export type Ui = Record<"title" | "visual" | "readout" | "instructions" | "button" | "note", number[]>;

/** Everything the solver scores and the overlay draws, for one pose. */
export const measure = (pose: Pose, ui: Ui) => {
  const silhouette = hull([...outline(FRAME.depth / 2), ...outline(-FRAME.depth / 2)].map((p) => project(pose, p)));
  const visible = clipToImage(silhouette);
  const { area, centroid } = areaCentroid(visible);
  const [hh, a, d] = [FRAME.height / 2, FRAME.width / 2 - FRAME.corner, FRAME.depth / 2];
  const edgeFront: [Pt, Pt] = [project(pose, [-a, hh, d]), project(pose, [a, hh, d])];
  const edgeBack: [Pt, Pt] = [project(pose, [-a, hh, -d]), project(pose, [a, hh, -d])];
  const focal = project(pose, [0, hh, 0]);
  // Signed: how far the back of the bottom edge shows below its front, across the edge. Positive only
  // when the bottom face turns towards the camera; a face turned away measures negative.
  const [ef, eb] = [project(pose, [0, hh, d]), project(pose, [0, hh, -d])];
  const along = [edgeFront[1][0] - edgeFront[0][0], edgeFront[1][1] - edgeFront[0][1]], al = Math.hypot(along[0], along[1]);
  const thickness = ((eb[0] - ef[0]) * -along[1] + (eb[1] - ef[1]) * along[0]) / al;
  const angle = (Math.atan2(edgeFront[1][1] - edgeFront[0][1], edgeFront[1][0] - edgeFront[0][0]) * 180) / Math.PI;
  // Each group of speaker holes, and the direction sound leaves it: the bottom face's outward normal,
  // as the camera sees it.
  const speakers = [SPEAKER.left, SPEAKER.right].map((sx) => {
    const at = project(pose, [sx, hh, 0]), out = project(pose, [sx, hh + 40, 0]);
    const len = Math.hypot(out[0] - at[0], out[1] - at[1]);
    return { at, direction: [(out[0] - at[0]) / len, (out[1] - at[1]) / len] as Pt };
  });
  const [speaker, direction] = [speakers[0].at, speakers[0].direction];
  // Signed the same way: how far the back of the right side shows beyond its front, to the right.
  const [sf, sb] = [project(pose, [FRAME.width / 2, 0, d]), project(pose, [FRAME.width / 2, 0, -d])];
  const side = ((sb[0] - sf[0]) * along[0] + (sb[1] - sf[1]) * along[1]) / al;
  const quads = Object.fromEntries(Object.entries(ui).map(([k, [l, t, r, b]]) => [k, [[l, t], [r, t], [r, b], [l, b]].map((p) => projectScreen(pose, p as Pt))])) as Record<keyof Ui, Pt[]>;
  // How much narrower the phone is where it leaves the top edge than just above its bottom edge.
  const keystone = widthAt(silhouette, 0) / widthAt(silhouette, focal[1] - SPACE.xl);
  return { keystone, silhouette, visible, area, centroid, box: bbox(visible), full: bbox(silhouette), edgeFront, edgeBack, focal, thickness, angle, speaker, direction, speakers, side, quads };
};

/** The caption's top: its two lines (76 px at a 1.36 line height) stand on CAPTION_BAND.bottom. */
export const CAPTION_TOP = CAPTION_BAND.bottom - 2 * 76 * 1.36;

/** The hard constraints; a pose that breaks any of them is rejected by the solver and fails the render. */
export const violations = (m: ReturnType<typeof measure>) => {
  const v: string[] = [];
  for (const [k, q] of Object.entries(m.quads)) {
    if (q.some(([x]) => x < SAFE_X || x > W - SAFE_X)) v.push(`${k} must stay inside the side margins`);
    if (q.some(([, y]) => y < SPACE.xl)) v.push(`${k} must stay below the top edge`);
  }
  if (m.full.top > -SPACE.xl) v.push("the phone must run beyond the top edge");
  if (m.box.bottom > CAPTION_TOP - SPACE.xl) v.push("the phone must stay clear of the caption");
  if (m.thickness <= 0) v.push("the bottom edge must face the camera");
  if (Math.abs(m.focal[0] - W / 2) > 0.04 * W) v.push("the speaker edge must be centred");
  return v;
};

/**
 * Sound leaving both groups of speaker holes: for each, arcs centred on the projected holes, opening along
 * the bottom face's projected normal, spanning ±`SPREAD` degrees, with radii from the spacing scale;
 * the largest stops SPACE.m short of the caption.
 */
const SPREAD = 44;
const RINGS = [SPACE.m, SPACE.l, SPACE.xl, SPACE.xxl, SPACE.xl + SPACE.xxl];
export type Arc = { pts: Pt[]; ring: number };
const onArc = (m: ReturnType<typeof measure>, k: number, radius: number, degrees: number): Pt => {
  const { at: s, direction: d } = m.speakers[k], n: Pt = [-d[1], d[0]], a = rad(degrees);
  return [s[0] + radius * (d[0] * Math.cos(a) + n[0] * Math.sin(a)), s[1] + radius * (d[1] * Math.cos(a) + n[1] * Math.sin(a))];
};
export const soundArcs = (m: ReturnType<typeof measure>): Arc[] =>
  m.speakers.flatMap((_, k) => RINGS.filter((radius) => onArc(m, k, radius, 0)[1] <= CAPTION_TOP - SPACE.m).map((radius, ring) => ({ ring, pts: Array.from({ length: 41 }, (_, i) => onArc(m, k, radius, -SPREAD + (2 * SPREAD * i) / 40)) })));

/**
 * Bubbles, [x, y, r], carried by the sound: each sits just outside a wavefront, as if the wave were pushing
 * it, and grows the further out it rides (radii 8, 13, 21 from the scale). Angles are mirrored between the
 * two speakers and offset a little so the pattern is not symmetric. A bubble is dropped if it would touch
 * the phone, come within SPACE.m of the caption or meet another bubble.
 */
const RIDERS: [number, number, number][] = [[1, 22, 8], [2, -30, 13], [2, 34, 8], [3, -38, 13], [3, 12, 21], [4, -20, 21], [4, 30, 13]];
export const bubbles = (m: ReturnType<typeof measure>): [number, number, number][] => {
  const out: [number, number, number][] = [];
  m.speakers.forEach((_, k) => RIDERS.forEach(([ring, degrees, r]) => {
    const radius = RINGS[ring] + r * 0.7;
    const [x, y] = onArc(m, k, radius, (k ? -degrees : degrees) + (k ? 6 : 0));
    if (y + r > CAPTION_TOP - SPACE.m || x - r < SAFE_X || x + r > W - SAFE_X) return;
    if (inside(m.visible, [x, y]) || distanceTo(m.visible, [x, y]) < r + SPACE.xs) return;
    if (out.some(([bx, by, br]) => Math.hypot(bx - x, by - y) < br + r + SPACE.xs)) return;
    out.push([x, y, r]);
  }));
  return out;
};

export type Piece = { name: string; x: number; y: number; size: number; color: string; rotate: number };

/** The MaterialShapes the other listings scatter, in the campaign's aqua tones only. */
const SHAPE_NAMES = ["cookie_9sided", "flower", "clover_4leaf", "soft_burst", "gem", "pill", "sunny", "cookie_6sided"];
const SHAPE_COLORS = ["#A6EDEA", "#7DDDE1", "#C2F0F0"];

/**
 * Shapes for the hero: one large shape behind the phone, showing beside its right edge above the
 * the sound, and smaller ones from a seeded generator in the open space. Every shape keeps SPACE.s
 * clear of the background's glass ribbons; a small one also keeps SPACE.s from the phone's visible
 * silhouette, SPACE.m from the caption's text, the sound arcs, the bubbles and the other shapes, and may
 * run off the image's edge.
 */
export type Ribbon = { pts: Pt[]; half: number };
export const heroShapes = (m: ReturnType<typeof measure>, arcs: Arc[], spheres: [number, number, number][], ribbons: Ribbon[]): Piece[] => {
  const clearOfRibbons = (cx: number, cy: number, r: number) => ribbons.every((b) => b.pts.every((p) => Math.hypot(p[0] - cx, p[1] - cy) > r + b.half + SPACE.s));
  // The large shape: centred SPACE.xxl in from the right edge, on the upper golden line divided by φ,
  // as large as it can be (up to the image's width) while it stays clear of the ribbons.
  const [bx, by] = [W - SPACE.xxl, GOLDEN.y[0] / PHI];
  let big = W;
  while (big > SPACE.xxl && !clearOfRibbons(bx, by, big / 2)) big -= SPACE.xs;
  const pieces: Piece[] = [{ name: "cookie_12sided", x: bx - big / 2, y: by - big / 2, size: big, color: "#BDEFEF", rotate: 12 }];
  let seed = 11;
  const rand = () => ((seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0) / 4294967296);
  const effect = [...arcs.flatMap((a) => a.pts).map((p) => [p[0], p[1], 6]), ...spheres] as number[][];
  const caption = { left: W / 2 - 330, right: W / 2 + 330, top: CAPTION_TOP, bottom: CAPTION_BAND.bottom };
  const clearOfPhone = (cx: number, cy: number, r: number) => !inside(m.visible, [cx, cy]) && distanceTo(m.visible, [cx, cy]) > r + SPACE.s;
  const small: Piece[] = [];
  for (let k = 0; small.length < 5 && k < 6000; k++) {
    const size = SPACE.xl + rand() * (SPACE.xxl + SPACE.l), r = size / 2;
    const cx = -r * 0.4 + rand() * (W + r * 0.8), cy = -r * 0.4 + rand() * (H + r * 0.8);
    if (!clearOfPhone(cx, cy, r) || !clearOfRibbons(cx, cy, r)) continue;
    if (Math.hypot(cx - bx, cy - by) < r + big / 2 + SPACE.m) continue;
    if (cx + r > caption.left - SPACE.m && cx - r < caption.right + SPACE.m && cy + r > caption.top - SPACE.m && cy - r < caption.bottom + SPACE.m) continue;
    if (effect.some(([x, y, er]) => Math.hypot(x - cx, y - cy) < r + er + SPACE.m)) continue;
    if (small.some((p) => Math.hypot(p.x + p.size / 2 - cx, p.y + p.size / 2 - cy) < (p.size + size) / 2 + SPACE.m)) continue;
    small.push({ name: SHAPE_NAMES[small.length % SHAPE_NAMES.length], x: cx - r, y: cy - r, size, color: SHAPE_COLORS[small.length % SHAPE_COLORS.length], rotate: rand() * 360 });
  }
  return [...pieces, ...small];
};
