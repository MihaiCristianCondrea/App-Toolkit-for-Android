// Tags every rendered file in ../output/ and ../soundtrack.mp3 using inject_metadata.py, writes ../output/schema.jsonld,
// and verifies with --check that every title and XMP/ID3/container tag reads back with zero pixel or stream changes.
import { execFileSync } from "node:child_process";
import { existsSync } from "node:fs";
import { readdir } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = fileURLToPath(new URL("../", import.meta.url));
const campaign = path.resolve(root, "..");
const out = path.join(campaign, "output");
const manifest = path.join(campaign, "toolkit-trailer-metadata.json");
const injector = path.resolve(campaign, "../../../skills/built-in/metadata/scripts/inject_metadata.py");
const python = process.platform === "win32" ? "python" : "python3";

const collect = async (dir, ext) => {
  if (!existsSync(dir)) return [];
  const names = (await readdir(dir)).filter((n) => n.endsWith(ext)).sort();
  return names.map((n) => path.join(dir, n));
};

const audio = [path.join(campaign, "soundtrack.mp3"), path.join(root, "public/soundtrack.mp3")].filter(existsSync);
const videos = await collect(path.join(out, "video"), ".mp4");
const images = await collect(path.join(out, "images"), ".png");
const files = [...audio, ...videos, ...images];

execFileSync(python, [injector, "--manifest", manifest, "--jsonld", path.join(out, "schema.jsonld"), ...files], { stdio: "inherit" });
execFileSync(python, [injector, "--manifest", manifest, "--check", ...files], { stdio: "inherit" });
