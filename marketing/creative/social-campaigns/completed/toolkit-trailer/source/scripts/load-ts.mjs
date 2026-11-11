// Loads one of the trailer's TypeScript modules in Node, bundled by esbuild with the JSON and modules it imports,
// so the soundtrack and the checks run the picture's own motion code rather than a copy of it.
import { build } from "esbuild";
import { fileURLToPath } from "node:url";

export const loadTs = async (relative) => {
  const entry = fileURLToPath(new URL(`../${relative}`, import.meta.url));
  const out = await build({ entryPoints: [entry], bundle: true, format: "esm", platform: "node", write: false, logLevel: "silent" });
  return import(`data:text/javascript;base64,${Buffer.from(out.outputFiles[0].text).toString("base64")}`);
};
