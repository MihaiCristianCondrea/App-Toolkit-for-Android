// Renders this listing's Google Play graphics with Remotion, from one source into every language: each
// strings/<locale>.json becomes ../listing/<locale>/graphics/ (phone, tablet and desktop screenshots, feature graphic,
// icon) and ../listing/<locale>/alt-text.txt. Review strips of the three sets go to output/ (not committed).
// Usage: npm run render [-- locale ...]. REMOTION_BROWSER (or CHROME_PATH) picks a local Chrome Headless Shell.
import { bundle } from "./bundle.mjs";
import { renderStill, selectComposition } from "@remotion/renderer";
import { execFileSync } from "node:child_process";
import { cp, copyFile, mkdir, readFile, readdir, rename, rm, writeFile } from "node:fs/promises";
import path from "node:path";
import { createHash } from "node:crypto";
import { fileURLToPath } from "node:url";


const root = fileURLToPath(new URL("../", import.meta.url));
const listing = path.resolve(root, "..", "listing");
const output = path.join(root, "output");
const phoneOnly=process.argv.includes("--phone-only");
const selectArg=process.argv.find(a=>a.startsWith("--shots="));
const selected=selectArg?selectArg.slice(8).split(",").map(Number):[];
if(selected.length&&!phoneOnly)throw Error("--shots requires --phone-only");
const verifyRepeat = process.argv.includes("--verify-repeat");
const wanted = process.argv.slice(2).filter(arg => !arg.startsWith("--"));
const locales = (await readdir(path.join(root, "strings"))).filter((f) => f.endsWith(".json")).map((f) => f.slice(0, -5))
  .filter((l) => !wanted.length || wanted.includes(l));
const browserExecutable = process.env.REMOTION_BROWSER ?? process.env.CHROME_PATH ?? null;

const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
const previousCapture=selected.length?JSON.parse(await readFile(path.join(output,"capture-checks.json"),"utf8")):{captures:[],browserErrors:[]};
const captures = [...previousCapture.captures], browserErrors = [...previousCapture.browserErrors];
const still = async (id, inputProps, file, scale = 1) => {
  const composition = await selectComposition({ serveUrl, id, inputProps, browserExecutable });
  await mkdir(path.dirname(file), { recursive: true });
  await renderStill({ composition, serveUrl, inputProps, output: file, scale, browserExecutable, imageFormat: "png", timeoutInMilliseconds: 120000,
    onBrowserLog: ({text}) => { if(text.startsWith("BILL ")||text.startsWith("TOOLKIT ")) captures.push({asset:path.basename(file),...JSON.parse(text.slice(text.indexOf(" ")+1))}); if(/Failed to load resource|ERR_CONTENT_LENGTH_MISMATCH/.test(text)) browserErrors.push(text); }
  });
};

await mkdir(output, { recursive: true });
for (const locale of locales) {
  const strings = JSON.parse(await readFile(path.join(root, "strings", `${locale}.json`), "utf8"));
  // Render into a staging folder; the published graphics are replaced only once every image succeeded.
  const graphics = path.join(listing, locale, "graphics");
  const out = path.join(output, `graphics-${locale}`);
  await rm(out, { recursive: true, force: true });
  if(phoneOnly){
    await cp(graphics,out,{recursive:true});
    if(!selected.length)await rm(path.join(out,"phone-screenshots"),{recursive:true,force:true});
  }
  const flatten = [];
  const repeatability = selected.length?JSON.parse(await readFile(path.join(output,"repeatability.json"),"utf8")).filter((entry)=>!selected.some(i=>strings.screenshots[i]?.id===entry.id)):[];
  if(selected.some(i=>!Number.isInteger(i)||i<0||i>=strings.screenshots.length))throw Error("Invalid zero-based phone index");

  // Phone: 1080 x 1920 layouts rendered at 2x (2160 x 3840) for sharper perspective text. PhoneShot fails the render if a caption covers over 20%.
  for (const [i, s] of strings.screenshots.entries()) {
    if(selected.length&&!selected.includes(i))continue;
    const file = path.join(out, "phone-screenshots", `${i + 1}-${s.id}.png`);
    await still("PhoneShot", { locale, index: i }, file, 2);
    if (verifyRepeat) {
      const repeat = path.join(output, `repeat-${i+1}.png`);
      await still("PhoneShot", {locale,index:i}, repeat, 2);
      const hash = bytes => createHash("sha256").update(bytes).digest("hex");
      const firstHash=hash(await readFile(file)),secondHash=hash(await readFile(repeat));
      if(firstHash!==secondHash) throw new Error(`Non-deterministic phone capture: ${s.id}`);
      repeatability.push({id:s.id,sha256:firstHash,identical:true});
    }
    flatten.push(file);
  }
  repeatability.sort((a,b)=>strings.screenshots.findIndex(s=>s.id===a.id)-strings.screenshots.findIndex(s=>s.id===b.id));
  if(verifyRepeat) await writeFile(path.join(output,"repeatability.json"),JSON.stringify(repeatability,null,2)+"\n");
  await still("PhonePanorama", { locale }, path.join(output, `panorama-${locale}.png`), 0.5);

  if(!phoneOnly){
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
  await mkdir(path.dirname(icon), {recursive:true});
  await copyFile(path.resolve(root, "../../ruler-and-protractor/listing/en-US/graphics/icon/icon.png"), icon);

  }
  // Play wants screenshots and the feature graphic as 24-bit PNG without alpha; the icon as 32-bit PNG.
  execFileSync(process.platform === "win32" ? "python" : "python3", ["-c",
    'import sys\nfrom PIL import Image\nfor p in sys.argv[1:]: Image.open(p).convert("RGB").save(p, optimize=True)',
    ...flatten.map((file) => process.platform === "win32" ? path.toNamespacedPath(file) : file)]);
  // One line per screenshot: its path under graphics/, a tab, its alt text (Play allows 140 characters).
  const alt = [["phone", strings.screenshots], ["tablet", strings.tabletScreenshots], ["desktop", strings.desktopScreenshots]]
    .flatMap(([kind, list]) => list.map((s, i) => `${kind}-screenshots/${i + 1}-${s.id}.png\t${s.alt}`));
  if(browserErrors.length) throw Error("Browser resource failures: "+browserErrors.join("; "));
  await writeFile(path.join(output,"capture-checks.json"), JSON.stringify({captures,browserErrors},null,2)+"\n");
  await rm(graphics, { recursive: true, force: true });
  await rename(out, graphics);
  await writeFile(path.join(listing, locale, "alt-text.txt"), alt.join("\n") + "\n");
  console.log(`${locale}: ${strings.screenshots.length} phone, ${strings.tabletScreenshots.length} tablet and ${strings.desktopScreenshots.length} desktop screenshots, feature graphic, icon.`);
}
