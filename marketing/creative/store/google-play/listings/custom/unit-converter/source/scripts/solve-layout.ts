// Solves every screenshot's composition (node scripts/solve-layout.ts [index ...]): for each composition in
// src/composition/shots.ts, tries its candidates, drops any that break a hard rule, keeps the lowest
// preference score, and writes the poses to src/composition/layout.json. With indices, only those are
// re-solved and the rest kept.
import { readFileSync, writeFileSync, existsSync } from "node:fs";
import { SHOTS, measureAll } from "../src/composition/shots.ts";
import { phoneRules } from "../src/composition/validate.ts";
import type { Pose } from "../src/composition/geometry.ts";

const ui = JSON.parse(readFileSync(new URL("../src/composition/ui.json", import.meta.url), "utf8"));
const file = new URL("../src/composition/layout.json", import.meta.url);
const layout = existsSync(file) ? JSON.parse(readFileSync(file, "utf8")) : { shots: [] };
const only = process.argv.slice(2).map(Number);
const r = (n: number, d = 3) => Math.round(n * 10 ** d) / 10 ** d;

SHOTS.forEach((spec, index) => {
  if (only.length && !only.includes(index)) return;
  const started = Date.now();
  let best: { poses: Pose[]; score: number } | undefined, tried = 0, rejected = 0;
  for (const poses of spec.candidates(ui)) {
    tried++;
    const ms = measureAll(poses, spec.screens, ui);
    if (phoneRules(spec, ms).length) { rejected++; continue; }
    const score = Object.values(spec.score(ms)).reduce((a, b) => a + b, 0);
    if (!best || score < best.score) best = { poses, score };
  }
  if (!best) throw new Error(`${spec.id}: no candidate meets the hard rules (${tried} tried)`);
  const poses = best.poses.map((p) => Object.fromEntries(Object.entries(p).map(([k, v]) => [k, r(v)])) as Pose);
  const terms = spec.score(measureAll(poses, spec.screens, ui));
  layout.shots[index] = { id: spec.id, screens: spec.screens, poses, score: r(best.score, 4), terms: Object.fromEntries(Object.entries(terms).map(([k, v]) => [k, r(v, 4)])), searched: tried, rejected };
  console.log(`${index + 1} ${spec.id}: ${tried} tried, ${rejected} rejected, score ${r(best.score, 4)} in ${((Date.now() - started) / 1000).toFixed(1)} s`, JSON.stringify(poses));
});
writeFileSync(file, JSON.stringify(layout, null, 1) + "\n");
