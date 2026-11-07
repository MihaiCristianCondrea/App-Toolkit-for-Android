// Verifies the phone screenshots (node scripts/verify.mjs [locale]). For each screenshot it renders, at 1×,
// the clean image, the same without captions, and the debug overlay into output/verify/; checks the
// geometry with the same validator the render uses; and measures on the composited pixels:
//   - caption contrast: WCAG contrast of the caption ink against the actual background under each glyph
//     (from the render without captions), and of the pill's text on the pill;
//   - interference: the share of the text zone (caption block + 34 px) where the background departs from
//     the plain field, i.e. ribbons, shapes or anything else behind the letters;
//   - visual balance: the contrast-weighted centroid of the finished image.
// The tablet and desktop sets (no captions) are rendered clean and with their overlay, checked by the same
// geometry and measured for balance. It writes report.json and contact sheets (clean and debug, for phones,
// tablets and desktops), prints a summary and exits 1 on any hard failure.
import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import { mkdir, readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { readPng, sheet, shrink, writePng } from "./png.mjs";
import { composeShot } from "../src/composition/compose.ts";
import { CAPTION, H, TEXT_CLEARANCE, W, grow } from "../src/composition/geometry.ts";
import { SHOTS } from "../src/composition/shots.ts";
import { FIELD, INK, ON_PILL, PILL } from "../src/theme.ts";
import { CANVAS, WIDE_SETS, composeWide } from "../src/composition/wide.ts";

const root = fileURLToPath(new URL("../", import.meta.url));
const locale = process.argv[2] ?? "en-US";
const out = path.join(root, "output", "verify");
await mkdir(out, { recursive: true });
const layout = JSON.parse(await readFile(path.join(root, "src/composition/layout.json"), "utf8"));
const ui = JSON.parse(await readFile(path.join(root, "src/composition/ui.json"), "utf8"));

// Thresholds. Captions are 76 px type: WCAG's large-text minimum is 3:1 (AA) and 4.5:1 (AAA); we require
// 4.5:1 under 99% of glyph pixels. Interference counts background pixels more than 20 (RGB distance) off
// the plain field: over 1% of the text zone fails, over 0.2% is reported.
const LIMIT = { contrast: 4.5, interferenceHard: 0.01, interferencePref: 0.002, balance: 0.05 };

const hex = (h) => [1, 3, 5].map((i) => parseInt(h.slice(i, i + 2), 16));
const lum = ([r, g, b]) => { const f = (c) => { c /= 255; return c <= 0.04045 ? c / 12.92 : ((c + 0.055) / 1.055) ** 2.4; }; return 0.2126 * f(r) + 0.7152 * f(g) + 0.0722 * f(b); };
const ratio = (a, b) => { const [x, y] = [lum(a), lum(b)].sort((p, q) => q - p); return (x + 0.05) / (y + 0.05); };
const dist = (a, b) => Math.hypot(a[0] - b[0], a[1] - b[1], a[2] - b[2]);
const at = (img, x, y) => { const i = (y * img.width + x) * 4; return [img.data[i], img.data[i + 1], img.data[i + 2]]; };
const [ink, pill, onPill, top, bottom] = [hex(INK), hex(PILL), hex(ON_PILL), hex(FIELD[0]), hex(FIELD[1])];
const field = (y) => top.map((c, k) => c + (bottom[k] - c) * (y / H));

const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
const render = async (index, props, name) => {
  const inputProps = { locale, index, ...props };
  const composition = await selectComposition({ serveUrl, id: "PhoneShot", inputProps });
  const file = path.join(out, `${name}-${index + 1}.png`);
  await renderStill({ composition, serveUrl, inputProps, output: file, imageFormat: "png", scale: 1, timeoutInMilliseconds: 300000 });
  return file;
};

const results = [];
for (let index = 0; index < SHOTS.length; index++) {
  const spec = SHOTS[index], g = composeShot(index, SHOTS.length, layout, ui);
  const [clean, bare] = [await readPng(await render(index, {}, "clean")), await readPng(await render(index, { captions: false }, "bare"))];
  await render(index, { debug: true }, "debug");
  const k = clean.width / W, block = CAPTION[spec.caption].block, zone = grow(block, TEXT_CLEARANCE);
  // Glyph pixels: where the caption changed the image, and the result is the ink colour.
  const contrasts = [];
  let pillPixels = 0;
  for (let y = Math.floor(block.top * k); y < block.bottom * k; y++) for (let x = Math.floor(block.left * k); x < block.right * k; x++) {
    const [c, b] = [at(clean, x, y), at(bare, x, y)];
    if (dist(c, b) < 48) continue;
    if (dist(c, ink) < 40) contrasts.push(ratio(ink, b));
    else if (dist(c, pill) < 24) pillPixels++;
  }
  contrasts.sort((a, b) => a - b);
  const p1 = contrasts[Math.floor(contrasts.length * 0.01)] ?? Infinity;
  // Interference in the text zone, on the image without captions.
  let busy = 0, total = 0;
  for (let y = Math.max(0, Math.floor(zone.top * k)); y < Math.min(H, zone.bottom) * k; y++) for (let x = Math.max(0, Math.floor(zone.left * k)); x < Math.min(W, zone.right) * k; x++) {
    total++;
    if (dist(at(bare, x, y), field(y / k)) > 20) busy++;
  }
  // Contrast-weighted centroid of the finished image.
  let sw = 0, sx = 0, sy = 0;
  for (let y = 0; y < clean.height; y += 2) for (let x = 0; x < clean.width; x += 2) {
    const d = dist(at(clean, x, y), field(y / k));
    if (d < 18) continue;
    sw += d; sx += d * x; sy += d * y;
  }
  const pixelBalance = [sx / sw / clean.width, sy / sw / clean.height];
  const hard = [...g.report.hard], preferences = [...g.report.preferences];
  if (p1 < LIMIT.contrast) hard.push(`caption contrast ${p1.toFixed(2)}:1 under 1% of glyphs, below ${LIMIT.contrast}:1`);
  if (busy / total > LIMIT.interferenceHard) hard.push(`background interferes with ${(100 * busy / total).toFixed(2)}% of the text zone`);
  else if (busy / total > LIMIT.interferencePref) preferences.push(`background touches ${(100 * busy / total).toFixed(2)}% of the text zone`);
  if (Math.abs(pixelBalance[0] - 0.5) > LIMIT.balance) preferences.push(`pixel balance at ${pixelBalance[0].toFixed(3)} W`);
  results.push({
    index: index + 1, id: spec.id, hard, preferences, intentional: g.report.intentional,
    caption: { glyphPixels: contrasts.length, minContrast: +(contrasts[0] ?? 0).toFixed(2), contrastP1: +p1.toFixed(2), pillText: +ratio(onPill, pill).toFixed(2), pillPixels, interference: +(busy / total).toFixed(5) },
    balance: { geometric: g.report.balance.map((v, i) => +(v / (i ? H : W)).toFixed(3)), pixels: pixelBalance.map((v) => +v.toFixed(3)) },
    phones: g.phones.map((m, i) => ({ screen: spec.screens[i], pose: m.pose, centroid: [+(m.centroid[0] / W).toFixed(3), +(m.centroid[1] / H).toFixed(3)], area: +(m.area / (W * H)).toFixed(3), box: Object.fromEntries(Object.entries(m.box).map(([key, v]) => [key, +(v / (key === "left" || key === "right" ? W : H)).toFixed(3)])) })),
    terms: g.report.terms,
  });
}

const sheets = async (name, count = SHOTS.length, k = 3) => writePng(path.join(out, `contact-${name}.png`), sheet(await Promise.all(Array.from({ length: count }, async (_, i) => shrink(await readPng(path.join(out, `${name}-${i + 1}.png`)), k))), 24));
await sheets("clean");
await sheets("debug");

// The tablet and desktop sets: no captions; geometry, and pixel balance.
const strings = JSON.parse(await readFile(path.join(root, "strings", `${locale}.json`), "utf8"));
for (const kind of Object.keys(WIDE_SETS)) {
  const screens = strings[WIDE_SETS[kind].key].map((s) => s.id), id = kind === "tablet" ? "TabletShot" : "DesktopShot";
  for (let index = 0; index < screens.length; index++) {
    const g = composeWide(kind, index, screens, ui);
    for (const debug of [false, true]) {
      const inputProps = { locale, kind, index, debug };
      const composition = await selectComposition({ serveUrl, id, inputProps });
      await renderStill({ composition, serveUrl, inputProps, output: path.join(out, `${kind}-${debug ? "debug" : "clean"}-${index + 1}.png`), imageFormat: "png", scale: 1, timeoutInMilliseconds: 300000 });
    }
    const clean = await readPng(path.join(out, `${kind}-clean-${index + 1}.png`)), k = clean.width / CANVAS.w;
    const fieldWide = (y) => top.map((c, j) => c + (bottom[j] - c) * (y / CANVAS.h));
    let sw = 0, sx = 0;
    for (let y = 0; y < clean.height; y += 2) for (let x = 0; x < clean.width; x += 2) { const d = dist(at(clean, x, y), fieldWide(y / k)); if (d < 18) continue; sw += d; sx += d * x; }
    const preferences = [...g.report.preferences], pixels = sx / sw / clean.width;
    if (Math.abs(pixels - 0.5) > LIMIT.balance) preferences.push(`pixel balance at ${pixels.toFixed(3)} W`);
    results.push({ index: `${kind} ${index + 1}`, id: g.screen, hard: g.report.hard, preferences, intentional: g.report.intentional, caption: null, balance: { geometric: [+(g.report.balance[0] / CANVAS.w).toFixed(3)], pixels: [+pixels.toFixed(3)] }, device: g.box, shapes: g.shapes.length });
  }
  await sheets(`${kind}-clean`, screens.length, 4);
  await sheets(`${kind}-debug`, screens.length, 4);
}
await writeFile(path.join(out, "report.json"), JSON.stringify(results, null, 1) + "\n");
for (const r of results) {
  console.log(r.caption
    ? `${r.index} ${r.id}: contrast ≥ ${r.caption.contrastP1}:1 (min ${r.caption.minContrast}), pill ${r.caption.pillText}:1, interference ${(100 * r.caption.interference).toFixed(2)}%, balance ${r.balance.pixels[0]} W (pixels) / ${r.balance.geometric[0]} W (geometry)`
    : `${r.index} ${r.id}: ${r.shapes} shapes, balance ${r.balance.pixels[0]} W (pixels) / ${r.balance.geometric[0]} W (geometry)`);
  for (const t of r.hard) console.log(`  HARD ${t}`);
  for (const t of r.preferences) console.log(`  PREF ${t}`);
  for (const t of r.intentional) console.log(`  OK   ${t}`);
}
if (results.some((r) => r.hard.length)) process.exit(1);
