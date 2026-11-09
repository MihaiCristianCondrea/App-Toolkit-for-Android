/**
 * The screenshots' geometry, shared by the solver (scripts/solve-layout.ts), the validator, the render and
 * its debug overlay.
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

export type Box = { left: number; top: number; right: number; bottom: number };

/**
 * Captions are two lines of 76 px type at a 1.36 line height. A top caption stands 72 px below the top
 * edge; a bottom caption stands SPACE.xl above the bottom edge. `band` is where the caption's measured
 * box must fit; `block` is the box its two lines fill across the band, used for clearances.
 */
export type CaptionAt = "top" | "bottom";
const LINES = 2 * 76 * 1.36;
export const CAPTION = {
  top: { band: { left: 120, right: 960, top: 72, bottom: 312 }, block: { left: 120, right: 960, top: 72, bottom: 72 + LINES } },
  bottom: { band: { left: 120, right: 960, top: 0.82 * H, bottom: H - SPACE.xl }, block: { left: 120, right: 960, top: H - SPACE.xl - LINES, bottom: H - SPACE.xl } },
} satisfies Record<CaptionAt, { band: Box; block: Box }>;
/** Nothing in the foreground comes within this of a caption's text block; the background fades there. */
export const TEXT_CLEARANCE = SPACE.m;
export const grow = (b: Box, d: number): Box => ({ left: b.left - d, top: b.top - d, right: b.right + d, bottom: b.bottom + d });

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

/** Sutherland-Hodgman against the image rectangle. */
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

export const bbox = (pts: Pt[]): Box => {
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

/** Even-odd point-in-polygon test. */
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

/** A screen's measured UI (src/composition/ui.json), in the frame's CSS px: [left, top, right, bottom]. */
export type ScreenUi = { critical: Record<string, number[]>; secondary: Record<string, number[]> };

/** Signed area; positive for a clockwise outline in image coordinates (y down). */
const signedArea = (poly: Pt[]) => poly.reduce((a, p, i) => { const q = poly[(i + 1) % poly.length]; return a + p[0] * q[1] - q[0] * p[1]; }, 0) / 2;

/** The part of `subject` inside the convex polygon `clip` (Sutherland-Hodgman). */
export const clipConvex = (subject: Pt[], clip: Pt[]): Pt[] => {
  const sign = Math.sign(signedArea(clip)) || 1;
  let out = subject;
  clip.forEach((a, i) => {
    const b = clip[(i + 1) % clip.length];
    const inner = (p: Pt) => sign * ((b[0] - a[0]) * (p[1] - a[1]) - (b[1] - a[1]) * (p[0] - a[0])) >= 0;
    const cut = (p: Pt, q: Pt): Pt => {
      const [dx, dy, ex, ey] = [q[0] - p[0], q[1] - p[1], b[0] - a[0], b[1] - a[1]];
      const t = (ex * (a[1] - p[1]) - ey * (a[0] - p[0])) / (ex * dy - ey * dx);
      return [p[0] + t * dx, p[1] + t * dy];
    };
    const src = out; out = [];
    src.forEach((cur, k) => {
      const prev = src[(k + src.length - 1) % src.length];
      if (inner(cur)) { if (!inner(prev)) out.push(cut(prev, cur)); out.push(cur); }
      else if (inner(prev)) out.push(cut(prev, cur));
    });
  });
  return out;
};
/** The area two polygons share, the second convex. */
export const overlapArea = (a: Pt[], convex: Pt[]) => { const c = clipConvex(a, convex); return c.length > 2 ? areaCentroid(c).area : 0; };

export const rectPoly = (b: Box): Pt[] => [[b.left, b.top], [b.right, b.top], [b.right, b.bottom], [b.left, b.bottom]];
/** The gap between a polygon and a box; 0 when they touch or overlap. */
export const gapToBox = (poly: Pt[], b: Box) => {
  if (poly.length < 3) return Infinity;
  if (overlapArea(rectPoly(b), poly) > 0 || poly.some(([x, y]) => x >= b.left && x <= b.right && y >= b.top && y <= b.bottom)) return 0;
  const toBox = ([x, y]: Pt) => Math.hypot(Math.max(b.left - x, 0, x - b.right), Math.max(b.top - y, 0, y - b.bottom));
  return Math.min(...poly.map(toBox), ...rectPoly(b).map((p) => distanceTo(poly, p)));
};

/** Everything the solver scores, the validator checks and the overlay draws, for one phone in one pose. */
export const measure = (pose: Pose, ui: ScreenUi) => {
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
  const quads = (boxes: Record<string, number[]>) => Object.fromEntries(Object.entries(boxes).map(([k, [l, t, r, b]]) => [k, [[l, t], [r, t], [r, b], [l, b]].map((p) => projectScreen(pose, p as Pt))])) as Record<string, Pt[]>;
  const [critical, secondary] = [quads(ui.critical), quads(ui.secondary)];
  // How much narrower the phone is where it leaves the top edge than just above its bottom edge.
  const keystone = widthAt(silhouette, 0) / widthAt(silhouette, focal[1] - SPACE.xl);
  return { keystone, silhouette, visible, area, centroid, box: bbox(visible), full: bbox(silhouette), edgeFront, edgeBack, focal, thickness, angle, speaker, direction, speakers, side, critical, secondary, pose };
};
export type Measured = ReturnType<typeof measure>;

/** The centre of a projected UI box. */
export const centre = (q: Pt[]): Pt => [q.reduce((a, p) => a + p[0], 0) / q.length, q.reduce((a, p) => a + p[1], 0) / q.length];

/**
 * Fits a pose's scale and position: scales until the visible silhouette is `width` wide, then moves it so
 * `x(m)` and `y(m)` meet their targets, repeating while clipping at the image edges settles. `minTop` then
 * lowers the phone, if needed, so its top stays at or below that line (a phone under a top caption).
 */
export type Fit = { width: number; x: (m: Measured) => number; xTarget: number; y: (m: Measured) => number; yTarget: number; minTop?: number };
export const fitPose = (shape: { pitch: number; yaw: number; roll: number; perspective: number }, ui: ScreenUi, f: Fit): Pose => {
  let p: Pose = { ...shape, scale: 1, x: W / 2, y: H / 2 };
  for (let i = 0; i < 6; i++) {
    let m = measure(p, ui);
    p = { ...p, scale: p.scale * (f.width / (m.box.right - m.box.left)) };
    m = measure(p, ui);
    p = { ...p, x: p.x + f.xTarget - f.x(m), y: p.y + f.yTarget - f.y(m) };
  }
  if (f.minTop !== undefined) {
    const top = measure(p, ui).full.top;
    if (top < f.minTop) p = { ...p, y: p.y + f.minTop - top };
  }
  return p;
};
