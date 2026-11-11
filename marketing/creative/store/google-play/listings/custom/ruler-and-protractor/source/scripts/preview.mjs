// Renders the phone panorama at half size into output/, for reviewing the set as one strip.
import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import path from "node:path";
import { fileURLToPath } from "node:url";
const root = fileURLToPath(new URL("../", import.meta.url));
const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
const inputProps = { locale: process.argv[2] ?? "en-US" };
const composition = await selectComposition({ serveUrl, id: "PhonePanorama", inputProps });
await renderStill({ composition, serveUrl, inputProps, output: path.join(root, "output", "preview.png"), scale: 0.5, imageFormat: "png", timeoutInMilliseconds: 300000 });
console.log("output/preview.png");
