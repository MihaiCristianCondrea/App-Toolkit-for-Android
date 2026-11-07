// Measures the hero screen's Water Eject UI head on (src/HeroProbe.tsx) and writes src/hero-ui.json for the solver.
import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import { writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
const root = fileURLToPath(new URL("../", import.meta.url));
const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
const composition = await selectComposition({ serveUrl, id: "HeroProbe", inputProps: {} });
let ui;
await renderStill({
  composition, serveUrl, inputProps: {}, output: path.join(root, "output", "hero-probe.png"), timeoutInMilliseconds: 300000,
  onBrowserLog: ({ text }) => { if (text.startsWith("HERO_UI ")) ui = JSON.parse(text.slice(8)); },
});
if (!ui) throw new Error("The probe logged no measurements");
await writeFile(path.join(root, "src", "hero-ui.json"), JSON.stringify(ui, null, 1) + "\n");
console.log(JSON.stringify(ui, null, 1));
