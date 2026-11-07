// Renders the hero at 1× with its geometry overlaid and no water (output/hero-debug.png), and without the
// overlay (output/hero.png), for checking the solved pose against the actual 3D render.
import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import path from "node:path";
import { fileURLToPath } from "node:url";
const root = fileURLToPath(new URL("../", import.meta.url));
const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
for (const debug of process.argv.includes("--water") ? [false] : process.argv.includes("--both") ? [true, false] : [true]) {
  const inputProps = { locale: "en-US", index: 0, debug };
  const composition = await selectComposition({ serveUrl, id: "PhoneShot", inputProps });
  const output = path.join(root, "output", debug ? "hero-debug.png" : "hero.png");
  await renderStill({ composition, serveUrl, inputProps, output, imageFormat: "png", timeoutInMilliseconds: 300000 });
  console.log(output);
}
