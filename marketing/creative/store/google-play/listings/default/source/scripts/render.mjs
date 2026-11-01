// Renders this listing's Google Play graphics with Remotion, from one source into every language: each
// strings/<locale>.json becomes ../listing/<locale>/graphics/ (phone, tablet and desktop screenshots, feature graphic,
// icon) and ../listing/<locale>/alt-text.txt. Review strips of the three sets go to output/ (not committed).
// Usage: npm run render [-- locale ...]. REMOTION_BROWSER (or CHROME_PATH) picks a local Chrome Headless Shell.
import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import { execFileSync } from "node:child_process";
import { mkdir, readFile, readdir, rm, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = fileURLToPath(new URL("../", import.meta.url));
const listing = path.resolve(root, "..", "listing");
const output = path.join(root, "output");
const wanted = process.argv.slice(2);
const locales = (await readdir(path.join(root, "strings"))).filter((f) => f.endsWith(".json")).map((f) => f.slice(0, -5))
  .filter((l) => !wanted.length || wanted.includes(l));
const browserExecutable = process.env.REMOTION_BROWSER ?? process.env.CHROME_PATH ?? null;

const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public") });
const still = async (id, inputProps, file, scale = 1) => {
  const composition = await selectComposition({ serveUrl, id, inputProps, browserExecutable });
  await mkdir(path.dirname(file), { recursive: true });
  await renderStill({ composition, serveUrl, inputProps, output: file, scale, browserExecutable, imageFormat: "png", timeoutInMilliseconds: 120000 });
};

await mkdir(output, { recursive: true });
for (const locale of locales) {
  const strings = JSON.parse(await readFile(path.join(root, "strings", `${locale}.json`), "utf8"));
  const out = path.join(listing, locale, "graphics");
  for (const dir of ["phone-screenshots", "tablet-screenshots", "desktop-screenshots", "feature-graphic", "icon"]) await rm(path.join(out, dir), { recursive: true, force: true });
  const flatten = [];

  // Phone: 1080 x 1920 each, cut from the panorama. PhoneShot fails the render if a caption covers over 20%.
  for (const [i, s] of strings.screenshots.entries()) {
    const file = path.join(out, "phone-screenshots", `${i + 1}-${s.id}.png`);
    await still("PhoneShot", { locale, index: i }, file);
    flatten.push(file);
  }
  await still("PhonePanorama", { locale }, path.join(output, `panorama-${locale}.png`), 0.5);

  // Tablet and desktop: 1920 x 1080 laid out, rendered at 2x (3840 x 2160).
  for (const [kind, list, id] of [["tablet", strings.tabletScreenshots, "Tablet"], ["desktop", strings.desktopScreenshots, "Desktop"]]) {
    for (const [i, s] of list.entries()) {
      const file = path.join(out, `${kind}-screenshots`, `${i + 1}-${s.id}.png`);
      await still(`${id}Shot`, { locale, kind, index: i }, file, 2);
      flatten.push(file);
    }
    await still(`${id}Strip`, { locale, kind }, path.join(output, `${kind}-strip-${locale}.png`), 0.5);
  }

  const feature = path.join(out, "feature-graphic", "feature-graphic.png");
  await still("FeatureGraphic", { locale }, feature);
  flatten.push(feature);
  const icon = path.join(out, "icon", "icon.png");
  await still("Icon", {}, icon);

  // Play wants screenshots and the feature graphic as 24-bit PNG without alpha; the icon as 32-bit PNG.
  execFileSync(process.platform === "win32" ? "python" : "python3", ["-c",
    'import sys\nfrom PIL import Image\nfor p in sys.argv[2:]: Image.open(p).convert("RGB").save(p, optimize=True)\nImage.open(sys.argv[1]).convert("RGBA").save(sys.argv[1], optimize=True)',
    icon, ...flatten]);
  // One line per screenshot: its path under graphics/, a tab, its alt text (Play allows 140 characters).
  const alt = [["phone", strings.screenshots], ["tablet", strings.tabletScreenshots], ["desktop", strings.desktopScreenshots]]
    .flatMap(([kind, list]) => list.map((s, i) => `${kind}-screenshots/${i + 1}-${s.id}.png\t${s.alt}`));
  await writeFile(path.join(listing, locale, "alt-text.txt"), alt.join("\n") + "\n");
  console.log(`${locale}: ${strings.screenshots.length} phone, ${strings.tabletScreenshots.length} tablet and ${strings.desktopScreenshots.length} desktop screenshots, feature graphic, icon.`);
}
