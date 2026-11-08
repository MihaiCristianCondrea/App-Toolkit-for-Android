// Copies the UI kit's public assets (fonts, logos, shapes, Material Next's device frames) into public/, where Remotion's
// staticFile() serves them. Run `npm install && npm run assets` in marketing/ui first.
import { cpSync, existsSync } from "node:fs";
import { fileURLToPath } from "node:url";

const src = fileURLToPath(new URL("../../../../../ui/public/", import.meta.url));
const dest = fileURLToPath(new URL("../public/", import.meta.url));
if (!existsSync(`${src}material-next/device-frames`)) {
  console.error("marketing/ui/public is not ready. Run `npm install && npm run assets` in marketing/ui.");
  process.exit(1);
}
cpSync(src, dest, { recursive: true });
console.log("synced marketing/ui/public -> public/");
// The supplied soundtrack, served to the film as public/soundtrack.mp3.
cpSync(fileURLToPath(new URL("../../soundtrack.mp3", import.meta.url)), fileURLToPath(new URL("../public/soundtrack.mp3", import.meta.url)));
