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
  const speaker = project(pose, [SPEAKER.left, hh, 0]);
  // The direction water leaves the grille: the bottom face's outward normal, as the camera sees it.
  const out = project(pose, [SPEAKER.left, hh + 40, 0]);
  const len = Math.hypot(out[0] - speaker[0], out[1] - speaker[1]);
  const direction: Pt = [(out[0] - speaker[0]) / len, (out[1] - speaker[1]) / len];
  // Signed the same way: how far the back of the right side shows beyond its front, to the right.
  const [sf, sb] = [project(pose, [FRAME.width / 2, 0, d]), project(pose, [FRAME.width / 2, 0, -d])];
  const side = ((sb[0] - sf[0]) * along[0] + (sb[1] - sf[1]) * along[1]) / al;
  const quads = Object.fromEntries(Object.entries(ui).map(([k, [l, t, r, b]]) => [k, [[l, t], [r, t], [r, b], [l, b]].map((p) => projectScreen(pose, p as Pt))])) as Record<keyof Ui, Pt[]>;
  // How much narrower the phone is where it leaves the top edge than just above its bottom edge.
  const keystone = widthAt(silhouette, 0) / widthAt(silhouette, focal[1] - SPACE.xl);
  return { keystone, silhouette, visible, area, centroid, box: bbox(visible), full: bbox(silhouette), edgeFront, edgeBack, focal, thickness, angle, speaker, direction, side, quads };
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

/** A cubic Bézier sampled at `n` + 1 points. */
export const bezier = ([p0, p1, p2, p3]: Pt[], n: number): Pt[] => Array.from({ length: n + 1 }, (_, i) => {
  const t = i / n, u = 1 - t;
  return [0, 1].map((k) => u * u * u * p0[k] + 3 * u * u * t * p1[k] + 3 * u * t * t * p2[k] + t * t * t * p3[k]) as Pt;
});

/**
 * The water's spine: two cubic Béziers joined smoothly. It leaves the speaker along the bottom face's
 * outward normal, bends through its lowest point on the right golden vertical, and rises into the open
 * space under the phone's right side, ending below it.
 */
export const waterSpine = (m: ReturnType<typeof measure>): Pt[] => {
  const s = m.speaker, d = m.direction;
  // The lowest point sits on the right golden vertical, SPACE.l above the water band's floor, so the
  // ribbon's width stays clear of the caption; the tip stops SPACE.xl below the phone's silhouette,
  // short of the right safe margin.
  const low: Pt = [GOLDEN.x[1], WATER_BAND.bottom - SPACE.l];
  const tipX = W - SAFE_X - SPACE.xl;
  const tip: Pt = [tipX, Math.max(...m.silhouette.filter((p) => Math.abs(p[0] - tipX) < SPACE.xl).map((p) => p[1]), m.focal[1]) + SPACE.xl];
  const first = bezier([s, [s[0] + d[0] * SPACE.xl, s[1] + d[1] * SPACE.xl], [low[0] - SPACE.xxl, low[1]], low], 60);
  const second = bezier([low, [low[0] + SPACE.xxl, low[1]], [tip[0] + SPACE.xl, tip[1] + SPACE.xl], tip], 60);
  return [...first, ...second.slice(1)];
};

/** Droplets leaving the tip along its last direction, and two small ones at the grille. Radii 13, 8, 5. */
export const waterDrops = (spine: Pt[]): [number, number, number][] => {
  const [a, b] = [spine[spine.length - 6], spine[spine.length - 1]];
  const l = Math.hypot(b[0] - a[0], b[1] - a[1]), d = [(b[0] - a[0]) / l, (b[1] - a[1]) / l], n = [-d[1], d[0]];
  const s = spine[0];
  return [
    [b[0] + d[0] * SPACE.m + n[0] * SPACE.xs, b[1] + d[1] * SPACE.m + n[1] * SPACE.xs, 8],
    [b[0] + d[0] * SPACE.l - n[0] * SPACE.s, b[1] + d[1] * SPACE.l - n[1] * SPACE.s, 5],
    [b[0] + d[0] * SPACE.s - n[0] * SPACE.m, b[1] + d[1] * SPACE.s - n[1] * SPACE.m, 13],
    [s[0] - SPACE.m, s[1] + SPACE.s, 5],
    [s[0] - SPACE.l, s[1] + SPACE.xs, 3],
  ];
};

/** Half-width of the water along its spine: from a 6 px jet to a broad body, tapering to the tip. */
export const waterHalfWidth = (t: number) => 3 + (SPACE.xl * 0.75) * Math.pow(Math.sin(Math.PI * Math.min(t * 1.1, 1)), 1.1);
