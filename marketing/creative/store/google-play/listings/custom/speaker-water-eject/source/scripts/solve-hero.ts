// Solves the hero phone's camera pose (node scripts/solve-hero.ts): a search over pitch, yaw, roll and
// perspective; for each, scale and position are solved so the projected silhouette meets the targets, hard
// constraints reject a pose, and the remaining ones are scored. Writes src/hero-pose.json.
import { readFileSync, writeFileSync } from "node:fs";
import { CAPTION_TOP, GOLDEN, H, W, measure, violations } from "../src/heroGeometry.ts";
import type { Pose, Ui } from "../src/heroGeometry.ts";

const probe = JSON.parse(readFileSync(new URL("../src/hero-ui.json", import.meta.url), "utf8"));
const titleRow = probe.contentChildren.find((c: [string, string]) => c[1] === "tk-sheet-title-row")[3];
const ui: Ui = { title: titleRow, visual: probe.visual, readout: probe.readout, instructions: probe.instructions, button: probe.button, note: probe.note };

const TARGET = {
  centreX: 0.5 * W, // the visible silhouette's area centroid
  focal: [0.5 * W, 0.7 * H], // the middle of the bottom (speaker) edge
  width: 0.78 * W, // the visible silhouette's width, as in the reference
  thickness: 0.035 * W, // how much of the bottom edge shows
  angle: -4, // the bottom edge rises gently to the right, as in the reference
  side: 0.012 * W, // a sliver of the right side shows
  keystone: 0.9, // top-edge width over bottom width, as measured on the reference
};
const fit = (pose: Pose) => {
  let p = { ...pose, scale: 1, x: W / 2, y: H / 2 };
  for (let i = 0; i < 6; i++) {
    let m = measure(p, ui);
    p = { ...p, scale: p.scale * (TARGET.width / (m.box.right - m.box.left)) };
    m = measure(p, ui);
    p = { ...p, x: p.x + TARGET.centreX - m.centroid[0], y: p.y + TARGET.focal[1] - m.focal[1] };
  }
  return p;
};

const score = (p: Pose, m: ReturnType<typeof measure>) =>
  4 * ((m.thickness - TARGET.thickness) / TARGET.thickness) ** 2 +
  ((m.angle - TARGET.angle) / 4) ** 2 +
  0.5 * (p.roll / 10) ** 2 +
  0.3 * ((m.side - TARGET.side) / TARGET.side) ** 2 +
  0.5 * ((m.focal[0] - TARGET.focal[0]) / (0.04 * W)) ** 2 +
  0.2 * ((p.perspective - 1800) / 1200) ** 2 +
  2 * ((m.keystone - TARGET.keystone) / 0.05) ** 2;

let best: { pose: Pose; s: number } | undefined;
let tried = 0, rejected = 0;
for (let pitch = 6; pitch <= 40; pitch += 1)
  for (let yaw = -24; yaw <= 0; yaw += 1)
    for (let roll = -10; roll <= 0; roll += 0.5)
      for (const perspective of [1200, 1500, 1800, 2100, 2400, 2700, 3000]) {
        tried++;
        const pose = fit({ pitch, yaw, roll, perspective, scale: 1, x: 0, y: 0 });
        const m = measure(pose, ui);
        if (violations(m).length) { rejected++; continue; }
        const s = score(pose, m);
        if (!best || s < best.s) best = { pose, s };
      }
if (!best) throw new Error("No pose meets the constraints");

const r = (n: number, d = 4) => Math.round(n * 10 ** d) / 10 ** d;
const pose = Object.fromEntries(Object.entries(best.pose).map(([k, v]) => [k, r(v, 3)])) as Pose;
const m = measure(pose, ui);
const nx = (x: number) => r(x / W), ny = (y: number) => r(y / H);
const report = {
  searched: tried, rejected, score: r(best.s),
  silhouetteCentroid: [nx(m.centroid[0]), ny(m.centroid[1])],
  visibleBox: { left: nx(m.box.left), top: ny(m.box.top), right: nx(m.box.right), bottom: ny(m.box.bottom) },
  fullTop: ny(m.full.top),
  speakerEdgeCentre: [nx(m.focal[0]), ny(m.focal[1])],
  speakerHoles: [nx(m.speaker[0]), ny(m.speaker[1])],
  keystone: r(m.keystone, 3), bottomEdgeAngleDeg: r(m.angle, 2), bottomEdgeThickness: nx(m.thickness), rightSideWidth: nx(m.side),
  uiBounds: Object.fromEntries(Object.entries(m.quads).map(([k, q]) => [k, [nx(Math.min(...q.map((p) => p[0]))), ny(Math.min(...q.map((p) => p[1]))), nx(Math.max(...q.map((p) => p[0]))), ny(Math.max(...q.map((p) => p[1])))]])),
  captionTop: ny(CAPTION_TOP), golden: { x: GOLDEN.x.map(nx), y: GOLDEN.y.map(ny), yLow: ny(GOLDEN.yLow) },
};
writeFileSync(new URL("../src/hero-pose.json", import.meta.url), JSON.stringify({ pose, ui, report }, null, 1) + "\n");
console.log(JSON.stringify({ pose, report }, null, 1));
