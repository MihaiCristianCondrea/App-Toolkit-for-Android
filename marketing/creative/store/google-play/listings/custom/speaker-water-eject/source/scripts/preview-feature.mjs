// Renders each feature graphic concept (src/FeatureGraphic.tsx) at full size, and at half size on Play-like
// light and dark backgrounds, into output/feature/ for review (node scripts/preview-feature.mjs [locale]).
import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import { mkdir } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { readPng, shrink, writePng } from "./png.mjs";
const root = fileURLToPath(new URL("../", import.meta.url));
const locale = process.argv[2] ?? "en-US", out = path.join(root, "output", "feature");
await mkdir(out, { recursive: true });
const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
const concepts = ["ripple", "ribbon"], halves = [];
for (const concept of concepts) {
  const inputProps = { locale, concept }, file = path.join(out, `${concept}.png`);
  await renderStill({ composition: await selectComposition({ serveUrl, id: "FeatureGraphic", inputProps }), serveUrl, inputProps, output: file, imageFormat: "png", timeoutInMilliseconds: 300000 });
  halves.push(shrink(await readPng(file), 2));
}
// One sheet: each concept at half size on Play's light (#FFFFFF) and dark (#131314) surfaces.
const pad = 32, [hw, hh] = [halves[0].width, halves[0].height], width = 2 * hw + 3 * pad, height = concepts.length * (hh + pad) + pad;
const data = Buffer.alloc(width * height * 4);
for (let y = 0; y < height; y++) for (let x = 0; x < width; x++) { const dark = x >= width / 2, i = (y * width + x) * 4; data[i] = data[i + 1] = data[i + 2] = dark ? 0x13 : 0xff; data[i + 3] = 255; }
halves.forEach((img, row) => [0, 1].forEach((col) => { const x0 = pad + col * (hw + pad), y0 = pad + row * (hh + pad); for (let y = 0; y < hh; y++) img.data.copy(data, ((y0 + y) * width + x0) * 4, y * hw * 4, (y + 1) * hw * 4); }));
await writePng(path.join(out, "concepts-half-size.png"), { width, height, data });
console.log(concepts.map((c) => path.join(out, `${c}.png`)).join("\n"), "\n" + path.join(out, "concepts-half-size.png"));
