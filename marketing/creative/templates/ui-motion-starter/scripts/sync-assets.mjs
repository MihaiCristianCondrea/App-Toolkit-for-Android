// Copies the UI kit's public assets (font, illustrations, logos) into this project's public/.
// Run `npm install && npm run assets` in marketing/ui first (open-motion-studio.cmd does both).
import { cpSync, existsSync } from "node:fs";
import { fileURLToPath } from "node:url";

const src = fileURLToPath(new URL("../../../../ui/public/", import.meta.url));
const dest = fileURLToPath(new URL("../public/", import.meta.url));
if (!existsSync(src)) {
  console.error("marketing/ui/public not found. Run `npm install && npm run assets` in marketing/ui.");
  process.exit(1);
}
cpSync(src, dest, { recursive: true });
console.log("synced marketing/ui/public -> public/");
