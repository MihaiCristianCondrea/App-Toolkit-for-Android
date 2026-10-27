// Copies brand assets into public/ so screens use the real files:
//  - Google Sans Flex font                     -> public/fonts/GoogleSansFlex.ttf
//  - marketing/assets/illustrations            -> public/illustrations/
//  - marketing/assets/logos/*.svg              -> public/logos/
//  - marketing/assets/shapes/*.svg             -> public/shapes/
import { cpSync, copyFileSync, existsSync, mkdirSync, readdirSync } from "node:fs";
import { join } from "node:path";
import { fileURLToPath } from "node:url";

const here = (rel) => fileURLToPath(new URL(rel, import.meta.url));
const repo = here("../../../");
const pub = here("../public/");
const marketingIllustrations = join(repo, "marketing", "assets", "illustrations");
const shapesDir = join(repo, "marketing", "assets", "shapes");

for (const d of ["fonts", "illustrations", "logos", "shapes", "images", "assets/shapes"]) {
  mkdirSync(join(pub, d), { recursive: true });
}
mkdirSync(marketingIllustrations, { recursive: true });

const localFont = join(pub, "fonts", "GoogleSansFlex.ttf");
const toolkitFont = join(
  repo,
  "library",
  "core",
  "designsystem",
  "src",
  "main",
  "res",
  "font",
  "google_sans_flex_variable.ttf",
);

if (!existsSync(localFont) && existsSync(toolkitFont)) {
  copyFileSync(toolkitFont, localFont);
}

if (existsSync(marketingIllustrations)) {
  for (const file of readdirSync(marketingIllustrations)) {
    copyFileSync(join(marketingIllustrations, file), join(pub, "illustrations", file));
  }
}

const logos = join(repo, "marketing", "assets", "logos");
if (existsSync(logos)) {
  for (const f of readdirSync(logos)) {
    if (f.endsWith(".svg")) copyFileSync(join(logos, f), join(pub, "logos", f));
  }
}

if (existsSync(shapesDir)) {
  for (const f of readdirSync(shapesDir)) {
    if (f.endsWith(".svg")) {
      copyFileSync(join(shapesDir, f), join(pub, "shapes", f));
      copyFileSync(join(shapesDir, f), join(pub, "assets", "shapes", f));
    }
  }
}

// Shared device frames, badges and launcher artwork have one source in Material Next.
const packageAssets = fileURLToPath(new URL("../node_modules/material-next-web-components/assets/marketing/", import.meta.url));
cpSync(packageAssets, join(pub, "material-next"), {recursive: true, filter: path => !path.endsWith("README.md")});
console.log("App Toolkit and Material Next assets synced");
