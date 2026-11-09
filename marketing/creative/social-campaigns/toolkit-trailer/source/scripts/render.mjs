// Renders the trailer (node scripts/render.mjs frames [seconds...] | transitions | preview | review | master) into ../output/.
//   frames:  stills at the given seconds (default: one per scene and beat) and a contact sheet;
//   transitions: before, during and after every scene change (timeline.json transitions), one row each, as
//            output/images/transitions.png;
//   preview: 960 x 540 H.264 with the composed soundtrack;  review: 1920 x 1080, to watch at normal size before
//   approving;  master: 2560 x 1440 H.264 with the soundtrack, only once the review is approved.
import { bundle } from "@remotion/bundler";
import { renderMedia, renderStill, selectComposition } from "@remotion/renderer";
import { mkdir, readFile } from "node:fs/promises";
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

const still = async (s, name) => {
  const file = path.join(out, "images", `${name}-${s.toFixed(2)}.png`);
  await renderStill({ composition, serveUrl, output: file, frame: Math.min(composition.durationInFrames - 1, Math.round(s * fps)), scale: 0.5, imageFormat: "png", timeoutInMilliseconds: 300000 });
  return file;
};
const stack = (rows) => {
  const width = Math.max(...rows.map((r) => r.width)), height = rows.reduce((a, r) => a + r.height, 0), data = Buffer.alloc(width * height * 4, 255);
  let y0 = 0;
  for (const r of rows) { for (let y = 0; y < r.height; y++) r.data.copy(data, ((y0 + y) * width) * 4, y * r.width * 4, (y + 1) * r.width * 4); y0 += r.height; }
  return { width, height, data };
};

if (mode === "transitions") {
  const { transitions } = JSON.parse(await readFile(path.join(root, "src/timeline.json"), "utf8"));
  const rows = [];
  for (const [k, tr] of transitions.entries()) {
    const imgs = [];
    for (const s of tr.times) imgs.push(shrink(await readPng(await still(s, `transition-${k + 1}`)), 2));
    rows.push(sheet(imgs, 12));
    console.log(`${k + 1}. ${tr.name}: ${tr.times.join(" / ")} s`);
  }
  await writePng(path.join(out, "images", "transitions.png"), stack(rows));
  console.log(path.join(out, "images", "transitions.png"));
} else if (mode === "frames") {
  const times = rest.length ? rest.map(Number) : [0.7, 2.6, 4.6, 6.45, 7.4, 8.47, 9.9, 11.5, 12.6, 14.6, 16.0, 17.6, 19.0, 19.8, 20.6, 21.6, 22.3, 24.4, 26.4, 27.8];
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
  const master = mode === "master", review = mode === "review";
  const file = path.join(out, "video", `app-toolkit-trailer-${master ? "master" : review ? "review" : "preview"}.mp4`);
  await renderMedia({
    composition, serveUrl, codec: "h264", outputLocation: file, scale: master ? 4 / 3 : review ? 1 : 0.5, crf: master ? 16 : review ? 18 : 22,
    audioCodec: "aac", audioBitrate: "320k", concurrency: master ? 2 : 3, timeoutInMilliseconds: 300000,
    onProgress: ({ progress }) => { if (Math.round(progress * 100) % 10 === 0) process.stdout.write(`\r${Math.round(progress * 100)}%`); },
  });
  console.log(`\n${file}`);
}
