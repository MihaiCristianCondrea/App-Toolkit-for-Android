// Checks the Decision Wheel's camera move and handoff, and the water phone's turn (node scripts/verify-wheel.mjs).
//
// Motion, without rendering: it samples the picture's own motion code (src/wheelMotion.ts, src/turn.ts) on every
// frame at 90 fps and checks that
//   - each curve is continuous in value and in velocity: the left and right derivatives agree at every frame and
//     at every key, so nothing steps and nothing jolts (output/wheel-motion.csv has the per-frame values);
//   - the spin takes the app's 3 seconds and lands in the middle of Music, and the result holds before the confetti;
//   - the wheel turns one way only, the push reverses only at its peak, and the phone's turn is monotonic;
//   - the phone stays where the cut put it (only the camera moves), and its edges and then its bezel leave the
//     frame by framing, with the camera still moving, before the lilac starts;
//   - from the handoff on, the replica sits exactly on the app's wheel, and the lilac covers the app's wheel
//     before the two part.
//
// Geometry, from renders: the handoff frame and two after it, at full size, twice each: as the film draws them,
// and with the replica hidden and the app's own wheel still turning. On each it fits the disc's rim (a circle
// through about 340 edge points), reads the angle of the six segment boundaries and finds the pointer's top edge
// and apex from its two sides, then compares the two renders with each other and with the motion's prediction.
// It also confirms that no pixel outside the wheel differs (so nothing is patched or left behind), and that once
// the lilac starts nothing of the phone is left in the frame.
//
// Writes output/wheel-checks.json; exits 1 if a check fails. --motion skips the renders; --reuse measures the
// renders already in output/checks instead of making them again.
import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import { access, mkdir, readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { loadTs } from "./load-ts.mjs";
import { readPng } from "./png.mjs";

const root = fileURLToPath(new URL("../", import.meta.url)), out = path.resolve(root, "../output");
const tl = JSON.parse(await readFile(path.join(root, "src/timeline.json"), "utf8"));
const B = tl.beats, FPS = tl.fps, wheelStart = tl.scenes.find((s) => s.id === "wheel").start, montageStart = tl.scenes.find((s) => s.id === "montage").start;
const M = await loadTs("src/wheelMotion.ts"), G = await loadTs("src/motifs/geometry.ts"), T = await loadTs("src/turn.ts");
const motionOnly = process.argv.includes("--motion"), reuse = process.argv.includes("--reuse");
await mkdir(path.join(out, "checks"), { recursive: true });

const report = { motion: {}, geometry: [], checks: [] };
let failed = 0;
const check = (name, ok, summary) => { report.checks.push({ name, ok, summary }); if (!ok) failed++; console.log(`${ok ? "pass" : "FAIL"}  ${name}${summary ? `: ${summary}` : ""}`); };
const f2 = (v, d = 2) => Number(v).toFixed(d);

// ------------------------------------------------------------------------------------------------ motion
const st = (t) => M.wheelState(t);
const Q = {
  "wheel angle (deg)": (t) => M.wheelRotation(t),
  "wheel size (log px)": (t) => Math.log(st(t).replica.box),
  "wheel centre x (px)": (t) => st(t).replica.cx,
  "wheel centre y (px)": (t) => st(t).replica.cy,
  "phone yaw (deg)": (t) => st(t).phone.yaw,
  "phone pitch (deg)": (t) => st(t).phone.pitch,
  "phone scale (log)": (t) => Math.log(st(t).phone.scale),
  "phone x (px)": (t) => st(t).phone.x,
  "phone y (px)": (t) => st(t).phone.y,
};
const [w0, s0] = B.wheelWindup, s1 = B.wheelSpin[1], [c0, cEdge, cPeak] = B.wheelCamera, [r0, r1] = B.wheelReveal, set1 = B.wheelSettle[1];
const end = montageStart + 0.3, frames = [];
for (let f = Math.ceil(wheelStart * FPS); f <= Math.round(end * FPS); f++) frames.push(f / FPS);
const keys = [w0, s0, c0, cEdge, cPeak, B.wheelFace, r0, r1, set1, s1, B.wheelPick];
const H = 1e-6;
/** The largest jump between the left and right derivative over these times: 0 for a curve with no corner. */
const corner = (q, times) => {
  let worst = 0, at = 0, vmax = 0;
  for (const t of times) {
    const vl = (q(t) - q(t - H)) / H, vr = (q(t + H) - q(t)) / H;
    vmax = Math.max(vmax, Math.abs(vl), Math.abs(vr));
    if (Math.abs(vr - vl) > worst) { worst = Math.abs(vr - vl); at = t; }
  }
  return { worst, at, vmax };
};
for (const [name, q] of Object.entries(Q)) {
  const steps = frames.slice(1).map((t, i) => q(t) - q(frames[i])), changes = steps.slice(1).map((d, i) => d - steps[i]);
  // The scene opens on a hard cut, already moving, so its first frame has no left side to agree with.
  const c = corner(q, [...frames.filter((t) => t > wheelStart + 1e-9), ...keys]);
  const ok = c.worst <= 1e-3 * c.vmax + 1e-6;
  report.motion[name] = { maxPerFrame: Math.max(...steps.map(Math.abs)), maxChangePerFrame: Math.max(...changes.map(Math.abs)), velocityJump: c.worst, at: c.at, maxVelocityPerSecond: c.vmax };
  check(`${name} is continuous in value and velocity`, ok, `largest velocity jump ${c.worst.toExponential(1)}/s at ${f2(c.at, 3)} s (peak ${f2(c.vmax, 3)}/s); at most ${f2(report.motion[name].maxPerFrame, 3)} per frame, changing by at most ${f2(report.motion[name].maxChangePerFrame, 4)} frame to frame`);
}
const monotone = (q, a, b, dir) => { let bad = 0, prev = q(a); for (let f = Math.ceil(a * FPS) + 1; f <= Math.floor(b * FPS); f++) { const v = q(f / FPS); if (dir * (v - prev) < -1e-9) bad++; prev = v; } return bad; };
check("the wind-up only turns back", monotone(Q["wheel angle (deg)"], w0, s0, -1) === 0);
check("the spin only turns forward", monotone(Q["wheel angle (deg)"], s0, end, 1) === 0);
check("the push only closes in until its peak", monotone(Q["wheel size (log px)"], wheelStart, cPeak, 1) === 0);
check("the settle only eases back", monotone(Q["wheel size (log px)"], cPeak, set1, -1) === 0);
check("the phone turns face on without reversing", monotone(Q["phone yaw (deg)"], wheelStart, end, 1) === 0 && monotone(Q["phone pitch (deg)"], wheelStart, end, -1) === 0);

// The spin: the app's duration and turn, ending in the middle of Music; then the hold.
const final = M.wheelRotation(s1), local = (((-90 - final + 90) % 360) + 360) % 360, segment = local / 60;
check("the spin takes the app's 3 seconds", Math.abs(s1 - s0 - 3) < 0.05, `${f2(s1 - s0)} s, ${f2(final / 360)} turns`);
check("it stops in the middle of Music", Math.floor(segment) === M.WINNER && Math.abs((segment % 1) - 0.5) < 1e-6, `pointer at ${f2(segment % 1)} of segment ${Math.floor(segment)} (${M.CHOICES[Math.floor(segment)]})`);
check("the result holds before the confetti", B.wheelBurst - B.wheelPick >= 0.9, `${f2(B.wheelBurst - B.wheelPick)} s held, ${f2(montageStart - B.wheelPick)} s before the toolkit`);

// The phone is anchored: undo the camera and its centre barely moves (it turns about its wheel, a few px).
const world = frames.filter((t) => t <= cPeak).map((t) => { const s = st(t), { zoom, from, to } = s.camera; return [from[0] + (s.phone.x - to[0]) / zoom, from[1] + (s.phone.y - to[1]) / zoom]; });
const range = (i) => Math.max(...world.map((p) => p[i])) - Math.min(...world.map((p) => p[i]));
check("the phone stays where the cut put it; only the camera moves", range(0) < 12 && range(1) < 12, `its centre, in the cut's frame, moves ${f2(range(0), 1)} px across and ${f2(range(1), 1)} px down while it turns about its wheel`);
// The phone leaves by framing: the edges of its screen (its bezel's inner side) cross the frame's at the camera's edge key, still moving.
const PE = M.PHONE_EDGE, screenEdges = (t) => { const p = st(t).phone; return [p.x - (PE.half - PE.screen) * p.scale, p.x + (PE.half - PE.screen) * p.scale]; };
const outer = (t) => { const p = st(t).phone, m = G.measure({ ...p, perspective: 3000 }, { critical: {}, secondary: {} }); return m.full; };
const rate = (Q["wheel size (log px)"](cEdge + 0.005) - Q["wheel size (log px)"](cEdge - 0.005)) / 0.01;
const [eL, eR] = screenEdges(cEdge), [lL, lR] = screenEdges(r0), body = outer(r0);
check("the phone's bezel crosses the frame's edges with the camera still moving", eL <= 1.5 && eR >= 1918.5 && rate > 0.4, `screen edges at ${f2(eL, 1)} and ${f2(eR, 1)} px at ${cEdge} s, the zoom still growing ${f2(rate * 100, 0)} % a second`);
check("nothing of the phone is in the frame when the lilac starts", lL < -20 && lR > 1940 && body.top < -1000 && body.bottom > 2080, `screen edges ${f2(lL, 0)} and ${f2(lR, 0)} px at ${r0} s; the phone's body spans ${f2(body.left, 0)} to ${f2(body.right, 0)} by ${f2(body.top, 0)} to ${f2(body.bottom, 0)}`);

// The replica on the app's wheel: the same centre and size from the handoff, then the lilac covers the app's wheel.
let worstFit = 0, uncovered = 0, parted = null;
for (const t of frames.filter((t) => t >= B.wheelFace && t <= r1)) {
  const s = st(t), pose = { ...s.phone, perspective: 3000 }, c = G.projectScreen(pose, [M.SHEET_WHEEL.cx, M.SHEET_WHEEL.cy]), e = G.projectScreen(pose, [M.SHEET_WHEEL.cx + M.SHEET_WHEEL.box / 2, M.SHEET_WHEEL.cy]);
  const r = Math.hypot(e[0] - c[0], e[1] - c[1]), dc = Math.hypot(c[0] - s.replica.cx, c[1] - s.replica.cy), dr = Math.abs(r - s.replica.box / 2);
  if (t <= cPeak) worstFit = Math.max(worstFit, dc, dr);
  else if (dc > 0.25 || dr > 0.25) { parted ??= t; if (0.4 * s.revealSizes[0] < r + dc + 1) uncovered++; }
}
check("from the handoff the replica sits exactly on the app's wheel", worstFit < 1e-6, `largest difference in centre or radius ${worstFit.toExponential(1)} px from ${B.wheelFace} to ${cPeak} s`);
check("the lilac covers the app's wheel before the replica settles away from it", uncovered === 0, `they part from ${parted === null ? "never" : f2(parted, 3) + " s"}; frames left uncovered: ${uncovered}`);

// The water phone's turn into the Surface Ruler: one angle through edge-on (yaws of WATER_POSE and RULER_A in Trailer.tsx).
{
  const [a, b] = B.phoneTurn, theta = (t) => { const r = T.throughEdge(Math.min(1, Math.max(0, (t - a) / (b - a))), -10, 12); return r.yaw + (r.second ? 180 : 0); };
  const ts = []; for (let f = Math.ceil(a * FPS); f <= Math.floor(b * FPS); f++) ts.push(f / FPS);
  let swap = a; for (let t = a; t < b; t += 1e-4) if (T.throughEdge((t - a) / (b - a), -10, 12).second) { swap = t; break; }
  const c = corner(theta, [...ts, swap]), vl = (theta(swap) - theta(swap - H)) / H, vr = (theta(swap + H) - theta(swap)) / H;
  report.motion["water-to-ruler turn (deg)"] = { velocityJump: c.worst, at: c.at, swap, velocityAtSwap: [vl, vr] };
  check("the water phone turns into the ruler at one angular velocity through edge-on", c.worst <= 1e-3 * c.vmax, `${f2(vl, 0)} deg/s before the swap at ${f2(swap, 3)} s, ${f2(vr, 0)} deg/s after`);
}

// Per-frame values, for anyone who wants to plot them.
const csv = ["frame,seconds,angle_deg,deg_per_frame,wheel_px,zoom_pct_per_frame,centre_x,centre_y,phone_yaw,phone_pitch,phone_scale"];
frames.forEach((t, i) => { const s = st(t), prev = i ? st(frames[i - 1]) : s; csv.push([Math.round(t * FPS), f2(t, 4), f2(s.replica.rotation, 3), f2(s.replica.rotation - prev.replica.rotation, 3), f2(s.replica.box, 2), f2((s.replica.box / prev.replica.box - 1) * 100, 3), f2(s.replica.cx, 2), f2(s.replica.cy, 2), f2(s.phone.yaw, 3), f2(s.phone.pitch, 3), f2(s.phone.scale, 4)].join(",")); });
await writeFile(path.join(out, "wheel-motion.csv"), csv.join("\n") + "\n");

// ------------------------------------------------------------------------------------------------ geometry
if (!motionOnly) {
  let serveUrl = null;
  const render = async (t, mode, scale = 1) => {
    const file = path.join(out, "checks", `wheel-${mode}-${f2(t, 3)}.png`);
    if (reuse && (await access(file).then(() => true, () => false))) return readPng(file);
    serveUrl ??= await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
    const inputProps = mode === "phone" ? { check: "phone" } : {}, composition = await selectComposition({ serveUrl, id: "ToolkitTrailer", inputProps });
    await renderStill({ composition, serveUrl, output: file, frame: Math.round(t * FPS), scale, imageFormat: "png", inputProps, timeoutInMilliseconds: 300000 });
    return readPng(file);
  };
  /** A channel at a point of the picture, bilinear; pixel i covers [i, i + 1), so its centre is at i + 0.5. */
  const sample = (img, X, Y, ch) => {
    const x = X - 0.5, y = Y - 0.5, x0 = Math.floor(x), y0 = Math.floor(y), fx = x - x0, fy = y - y0;
    const g = (xx, yy) => img.data[(Math.min(img.height - 1, Math.max(0, yy)) * img.width + Math.min(img.width - 1, Math.max(0, xx))) * 4 + ch];
    return (g(x0, y0) * (1 - fx) + g(x0 + 1, y0) * fx) * (1 - fy) + (g(x0, y0 + 1) * (1 - fx) + g(x0 + 1, y0 + 1) * fx) * fy;
  };
  const luma = (img, x, y) => 0.2126 * sample(img, x, y, 0) + 0.7152 * sample(img, x, y, 1) + 0.0722 * sample(img, x, y, 2);
  /** The rim, ray by ray: where the disc's surface (blue 244) gives way to the stage's card (blue 201), to a quarter pixel and interpolated. The pointer's rays are skipped. */
  const rim = (img, cx, cy, R) => {
    const pts = [];
    for (let k = 0; k < 360; k++) {
      if (k >= 258 && k <= 282) continue;
      const a = (k * Math.PI) / 180, [ux, uy] = [Math.cos(a), Math.sin(a)];
      let prev = sample(img, cx + R * 0.95 * ux, cy + R * 0.95 * uy, 2);
      for (let r = R * 0.95 + 0.25; r < R * 1.06; r += 0.25) {
        const v = sample(img, cx + r * ux, cy + r * uy, 2);
        if (v < 222.5) { const rr = r - 0.25 + (0.25 * (prev - 222.5)) / (prev - v); pts.push({ k, x: cx + rr * ux, y: cy + rr * uy }); break; }
        prev = v;
      }
    }
    return pts;
  };
  /** A least-squares circle through the points (Kasa), about a nearby origin. */
  const fitCircle = (pts, ox, oy) => {
    let [a11, a12, a13, a22, a23, a33, b1, b2, b3] = [0, 0, 0, 0, 0, 0, 0, 0, 0];
    for (const p of pts) { const x = p.x - ox, y = p.y - oy, z = x * x + y * y; a11 += x * x; a12 += x * y; a13 += x; a22 += y * y; a23 += y; a33 += 1; b1 -= x * z; b2 -= y * z; b3 -= z; }
    const det = (m) => m[0] * (m[4] * m[8] - m[5] * m[7]) - m[1] * (m[3] * m[8] - m[5] * m[6]) + m[2] * (m[3] * m[7] - m[4] * m[6]);
    const A = [a11, a12, a13, a12, a22, a23, a13, a23, a33], d = det(A);
    const D = det([b1, a12, a13, b2, a22, a23, b3, a23, a33]) / d, E = det([a11, b1, a13, a12, b2, a23, a13, b3, a33]) / d, F = det([a11, a12, b1, a12, a22, b2, a13, a23, b3]) / d;
    const cx = -D / 2, cy = -E / 2, r = Math.sqrt(cx * cx + cy * cy - F), rms = Math.sqrt(pts.reduce((s, p) => s + (Math.hypot(p.x - ox - cx, p.y - oy - cy) - r) ** 2, 0) / pts.length);
    return { cx: cx + ox, cy: cy + oy, r, rms };
  };
  /** The six segment boundaries' angle, modulo a segment (60 degrees): each is the middle of the surface-coloured stroke between two fills, read on a ring clear of the labels. */
  const FILLS = [[107, 94, 0], [255, 240, 179], [213, 213, 185], [246, 251, 244]];
  const boundaries = (img, cx, cy, rho) => {
    const n = 7200, cls = [];
    for (let i = 0; i < n; i++) {
      const a = (i / n) * 2 * Math.PI, c = [0, 1, 2].map((ch) => sample(img, cx + rho * Math.cos(a), cy + rho * Math.sin(a), ch));
      cls.push(FILLS.map((f) => (f[0] - c[0]) ** 2 + (f[1] - c[1]) ** 2 + (f[2] - c[2]) ** 2).reduce((best, d, j, all) => (d < all[best] ? j : best), 0));
    }
    // Solid runs of one fill (at least 0.4 degrees); the boundary is the middle of whatever lies between two
    // different ones (the stroke and the edge pixels blended from both sides).
    const i0 = cls.findIndex((c, i) => c !== cls[(i + n - 1) % n]), runs = [];
    for (let i = 0; i < n; i++) { const c = cls[(i0 + i) % n], last = runs[runs.length - 1]; if (last && last.c === c) last.end = i + 1; else runs.push({ c, start: i, end: i + 1 }); }
    const solid = runs.filter((r) => r.c !== 3 && r.end - r.start >= 8), angles = [];
    solid.forEach((r, k) => { const next = solid[(k + 1) % solid.length], start = k + 1 < solid.length ? next.start : next.start + n; if (next.c !== r.c) angles.push(((((r.end - 1 + start) / 2 + i0) / n) * 360) % 360); });
    const mean = angles.reduce((acc, a) => [acc[0] + Math.cos((a * Math.PI) / 30), acc[1] + Math.sin((a * Math.PI) / 30)], [0, 0]);
    return { count: angles.length, phase: (((Math.atan2(mean[1], mean[0]) * 30) / Math.PI) % 60 + 60) % 60, spread: Math.max(...angles.map((a) => Math.abs((((a - (Math.atan2(mean[1], mean[0]) * 30) / Math.PI) % 60) + 90) % 60 - 30))) };
  };
  /** The pointer: its two sides, row by row where it stands on the card and the disc's margin, fitted as lines; the apex is where they meet. Its top edge is read down its middle. */
  const pointer = (img, cx, top, k) => {
    const left = [], right = [];
    for (let y = Math.ceil(top) + 1; y <= top + 12 * k; y++) {
      const xs = []; for (let x = cx - 14 * k; x <= cx + 14 * k; x += 0.25) xs.push([x, luma(img, x, y)]);
      const i = xs.findIndex(([, v]) => v < 160), j = xs.length - 1 - [...xs].reverse().findIndex(([, v]) => v < 160);
      if (i <= 0 || j >= xs.length - 1 || j <= i) continue;
      const cross = (a, b) => a[0] + ((b[0] - a[0]) * (a[1] - 160)) / (a[1] - b[1]);
      left.push([y, cross(xs[i - 1], xs[i])]); right.push([y, cross(xs[j + 1], xs[j])]);
    }
    const line = (pts) => { const n = pts.length, sy = pts.reduce((s, p) => s + p[0], 0), sx = pts.reduce((s, p) => s + p[1], 0), syy = pts.reduce((s, p) => s + p[0] * p[0], 0), sxy = pts.reduce((s, p) => s + p[0] * p[1], 0), b = (n * sxy - sy * sx) / (n * syy - sy * sy); return { a: (sx - b * sy) / n, b }; };
    const L = line(left), R = line(right), apexY = (R.a - L.a) / (L.b - R.b);
    let edge = null, prev = luma(img, cx, top - 6);
    for (let y = top - 6 + 0.25; y < top + 6; y += 0.25) { const v = luma(img, cx, y); if (v < 160) { edge = y - 0.25 + (0.25 * (prev - 160)) / (prev - v); break; } prev = v; }
    return { top: edge, apex: [L.a + L.b * apexY, apexY], rows: left.length };
  };
  const measureWheel = (img, s) => {
    const { cx, cy, box, rotation } = s.replica, R = box / 2, k = box / M.SHEET_WHEEL.box, u = (232 * k) / 300;
    const pts = rim(img, cx, cy, R), circle = fitCircle(pts, cx, cy), b = boundaries(img, circle.cx, circle.cy, 120 * u), p = pointer(img, circle.cx, circle.cy - circle.r - 2 * k, k);
    return { circle, rimPoints: pts.length, rays: pts, phase: b.phase, boundaryCount: b.count, boundarySpread: b.spread, pointer: p, expected: { cx, cy, r: R, phase: (((rotation - 90) % 60) + 60) % 60, top: cy - R - 2 * k, apex: [cx, cy - R + 18 * k] } };
  };
  const dPhase = (a, b) => Math.abs(((a - b + 90) % 60 + 60) % 60 - 30);
  const times = [B.wheelFace, B.wheelFace + 1 / FPS, B.wheelFace + 12 / FPS];
  for (const t of times) {
    const s = st(t), [film, phone] = [await render(t, "film"), await render(t, "phone")];
    const a = measureWheel(phone, s), b = measureWheel(film, s), e = a.expected;
    // The rim, ray by ray, in both renders: the visible outline must not move at the handoff.
    const byRay = new Map(a.rays.map((p) => [p.k, Math.hypot(p.x - a.circle.cx, p.y - a.circle.cy)]));
    const rimShift = Math.max(...b.rays.filter((p) => byRay.has(p.k)).map((p) => Math.abs(Math.hypot(p.x - a.circle.cx, p.y - a.circle.cy) - byRay.get(p.k))));
    // Everything outside the wheel and its pointer is the same picture in both.
    let outside = 0, outsideMax = 0;
    const k = s.replica.box / M.SHEET_WHEEL.box;
    for (let y = 0; y < film.height; y++) for (let x = 0; x < film.width; x++) {
      if (Math.hypot(x - e.cx, y - e.cy) < e.r + 3 || (Math.abs(x - e.cx) < 12 * k && y > e.top - 4 && y < e.top + 24 * k)) continue;
      const i = (y * film.width + x) * 4, d = Math.max(...[0, 1, 2].map((c) => Math.abs(film.data[i + c] - phone.data[i + c])));
      if (d > 2) outside++; outsideMax = Math.max(outsideMax, d);
    }
    const row = {
      t, frame: Math.round(t * FPS), angle: s.replica.rotation, box: s.replica.box,
      app: { centre: [a.circle.cx, a.circle.cy], radius: a.circle.r, rimRms: a.circle.rms, rimPoints: a.rimPoints, phase: a.phase, boundaries: a.boundaryCount, pointerTop: a.pointer.top, apex: a.pointer.apex },
      replica: { centre: [b.circle.cx, b.circle.cy], radius: b.circle.r, rimRms: b.circle.rms, rimPoints: b.rimPoints, phase: b.phase, boundaries: b.boundaryCount, pointerTop: b.pointer.top, apex: b.pointer.apex },
      predicted: e, rimShift, outsidePixelsDiffering: outside, outsideMaxDifference: outsideMax,
    };
    report.geometry.push(row);
    const dc = Math.hypot(a.circle.cx - b.circle.cx, a.circle.cy - b.circle.cy), dr = Math.abs(a.circle.r - b.circle.r), dp = dPhase(a.phase, b.phase);
    const dTop = Math.abs(a.pointer.top - b.pointer.top), dApex = Math.hypot(a.pointer.apex[0] - b.pointer.apex[0], a.pointer.apex[1] - b.pointer.apex[1]);
    const label = `${f2(t, 3)} s (${f2(s.replica.box, 0)} px wheel)`;
    check(`${label}: the replica's centre and radius are the app's`, dc < 0.5 && dr < 0.5, `centre ${f2(dc, 2)} px apart, radius ${f2(a.circle.r, 2)} against ${f2(b.circle.r, 2)} px (rim fits within ${f2(Math.max(a.circle.rms, b.circle.rms), 2)} px rms)`);
    check(`${label}: the segment boundaries are at the same angle`, dp < 0.2 && a.boundaryCount === 6 && b.boundaryCount === 6, `${f2(a.phase, 3)} against ${f2(b.phase, 3)} degrees modulo 60 (${f2(dp, 3)} apart; the motion predicts ${f2(e.phase, 3)})`);
    check(`${label}: the pointer is in the same place`, dTop < 0.75 && dApex < 1, `top edge ${f2(dTop, 2)} px apart, apex ${f2(dApex, 2)} px apart`);
    check(`${label}: the visible rim does not move`, rimShift < 0.75, `largest change along any ray ${f2(rimShift, 2)} px`);
    check(`${label}: nothing outside the wheel changes`, outside === 0, `${outside} pixels differ by more than 2 levels (largest ${outsideMax})`);
    check(`${label}: both match the motion's prediction`, Math.hypot(b.circle.cx - e.cx, b.circle.cy - e.cy) < 1 && Math.abs(b.circle.r - e.r) < 1 && dPhase(b.phase, e.phase) < 0.3, `replica centre ${f2(Math.hypot(b.circle.cx - e.cx, b.circle.cy - e.cy), 2)} px, radius ${f2(b.circle.r - e.r, 2)} px, angle ${f2(dPhase(b.phase, e.phase), 3)} degrees from predicted; the app's wheel ${f2(Math.hypot(a.circle.cx - e.cx, a.circle.cy - e.cy), 2)} px, ${f2(a.circle.r - e.r, 2)} px, ${f2(dPhase(a.phase, e.phase), 3)} degrees`);
  }
  // No phone left in the frame once the lilac starts; the same detector finds the bezel earlier (so it can see one).
  const darkOutsideWheel = (img, s, scale) => {
    let n = 0; const { cx, cy, box } = s.replica, k = box / M.SHEET_WHEEL.box;
    for (let y = 0; y < img.height; y++) for (let x = 0; x < img.width; x++) {
      const X = x / scale, Y = y / scale;
      if (Math.hypot(X - cx, Y - cy) < box / 2 + 4 || (Math.abs(X - cx) < 12 * k && Y < cy - box / 2 + 22 * k)) continue;
      const i = (y * img.width + x) * 4; if (0.2126 * img.data[i] + 0.7152 * img.data[i + 1] + 0.0722 * img.data[i + 2] < 60) n++;
    }
    return n;
  };
  const before = B.wheelFace + 0.1, [early, late] = [await render(before, "film", 0.5), await render(r0, "film", 0.5)];
  const nEarly = darkOutsideWheel(early, st(before), 0.5), nLate = darkOutsideWheel(late, st(r0), 0.5);
  report.exit = { before, darkPixelsBefore: nEarly, revealStart: r0, darkPixelsAtReveal: nLate };
  check("the phone's dark bezel is in the frame before it passes and gone when the lilac starts", nEarly > 100 && nLate === 0, `${nEarly} dark pixels outside the wheel at ${f2(before, 2)} s, ${nLate} at ${r0} s (half-size renders)`);
}

await writeFile(path.join(out, "wheel-checks.json"), JSON.stringify(report, null, 2) + "\n");
console.log(`\n${report.checks.length - failed} of ${report.checks.length} checks pass; ${path.join(out, "wheel-checks.json")}`);
process.exit(failed ? 1 : 0);
