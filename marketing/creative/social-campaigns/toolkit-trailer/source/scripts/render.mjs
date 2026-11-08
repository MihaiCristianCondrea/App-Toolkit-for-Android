// Renders the trailer (node scripts/render.mjs frames [seconds...] | preview | master) into ../output/.
//   frames:  stills at the given seconds (default: one per scene and beat) and a labelled contact sheet;
//   preview: 960 x 540 H.264 with the composed soundtrack;  master: 2560 x 1440 H.264 with the soundtrack.
import { bundle } from "@remotion/bundler";
import { renderMedia, renderStill, selectComposition } from "@remotion/renderer";
import { mkdir } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { readPng, sheet, shrink, writePng } from "./png.mjs";

const root = fileURLToPath(new URL("../", import.meta.url)), out = path.resolve(root, "../output");
const [mode = "preview", ...rest] = process.argv.slice(2);
await mkdir(path.join(out, "images"), { recursive: true });
await mkdir(path.join(out, "video"), { recursive: true });
const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
const composition = await selectComposition({ serveUrl, id: "ToolkitTrailer", inputProps: {} });
const fps = composition.fps;

if (mode === "frames") {
  const times = rest.length ? rest.map(Number) : [0.8, 2.6, 3.4, 4.7, 5.9, 7.3, 8.6, 9.6, 11.0, 12.9, 14.3, 15.4, 16.7, 18.2, 19.4, 20.9, 22.1, 22.8, 23.6, 24.6, 26.2, 27.4, 28.3, 28.9, 29.6, 31.2];
  const files = [];
  for (const s of times) {
    const file = path.join(out, "images", `frame-${s.toFixed(2)}.png`);
    await renderStill({ composition, serveUrl, output: file, frame: Math.min(composition.durationInFrames - 1, Math.round(s * fps)), scale: 0.5, imageFormat: "png", timeoutInMilliseconds: 300000 });
    files.push(file);
    console.log(file);
  }
  const imgs = await Promise.all(files.map(async (f) => shrink(await readPng(f), 2)));
  const rows = [];
  for (let i = 0; i < imgs.length; i += 4) rows.push(sheet(imgs.slice(i, i + 4), 12));
  const width = Math.max(...rows.map((r) => r.width)), height = rows.reduce((a, r) => a + r.height, 0), data = Buffer.alloc(width * height * 4, 255);
  let y0 = 0;
  for (const r of rows) { for (let y = 0; y < r.height; y++) r.data.copy(data, ((y0 + y) * width) * 4, y * r.width * 4, (y + 1) * r.width * 4); y0 += r.height; }
  await writePng(path.join(out, "images", "contact-sheet.png"), { width, height, data });
  console.log(path.join(out, "images", "contact-sheet.png"));
} else {
  const master = mode === "master";
  const file = path.join(out, "video", `app-toolkit-trailer-${master ? "master" : "preview"}.mp4`);
  await renderMedia({
    composition, serveUrl, codec: "h264", outputLocation: file, scale: master ? 4 / 3 : 0.5, crf: master ? 16 : 22,
    audioCodec: "aac", audioBitrate: "320k", concurrency: master ? 2 : 3, timeoutInMilliseconds: 300000,
    onProgress: ({ progress }) => { if (Math.round(progress * 100) % 10 === 0) process.stdout.write(`\r${Math.round(progress * 100)}%`); },
  });
  console.log(`\n${file}`);
}
