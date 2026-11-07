// Measures each screen's critical UI head on (src/UiProbe.tsx) and writes src/composition/ui.json.
// `--discover` also prints every sizeable element, for choosing selectors in src/composition/uiSelectors.ts.
import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import { readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
const root = fileURLToPath(new URL("../", import.meta.url));
const discover = process.argv.includes("--discover");
const screens = process.argv.slice(2).filter((a) => !a.startsWith("--"));
const serveUrl = await bundle({ entryPoint: path.join(root, "src/index.ts"), publicDir: path.join(root, "public"), enableCaching: false });
const file = path.join(root, "src", "composition", "ui.json");
const all = JSON.parse(await readFile(file, "utf8").catch(() => "{}"));
for (const screen of screens) {
  const inputProps = { screen, discover };
  const composition = await selectComposition({ serveUrl, id: "UiProbe", inputProps });
  let found;
  await renderStill({
    composition, serveUrl, inputProps, output: path.join(root, "output", `probe-${screen}.png`), timeoutInMilliseconds: 300000,
    onBrowserLog: ({ text }) => { if (text.startsWith(`UI ${screen} `)) found = JSON.parse(text.slice(4 + screen.length)); },
  });
  if (!found) throw new Error(`The probe logged nothing for ${screen}`);
  if (discover) console.log(screen, JSON.stringify(found.elements));
  all[screen] = { screen: found.screen, critical: found.critical, secondary: found.secondary };
}
await writeFile(file, JSON.stringify(all, null, 1) + "\n");
console.log(JSON.stringify(all));
