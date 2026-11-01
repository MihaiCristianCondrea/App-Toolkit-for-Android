#!/usr/bin/env node
// Checks the live Play listing against the limits Google Play enforces, before anything is uploaded.
// Usage: node scripts/validate.mjs            (from marketing/creative/store/google-play)
import { readFileSync, readdirSync, existsSync, statSync } from "node:fs";
import { join, dirname } from "node:path";
import { fileURLToPath } from "node:url";

const root = join(dirname(fileURLToPath(import.meta.url)), "..");
const errors = [];
const warn = [];

const LIMITS = { "title.txt": 30, "short-description.txt": 80, "full-description.txt": 4000 };
// Graphics: exact sizes, or the screenshot rules (each side 320 to 3840 px, long side at most twice the short one).
const GRAPHICS = {
  icon: { exact: [512, 512], max: 1, min: 1 },
  "feature-graphic": { exact: [1024, 500], max: 1, min: 1 },
  "phone-screenshots": { screenshot: true, max: 8, min: 2 },
  "tablet-screenshots": { screenshot: true, max: 8, min: 0 },
  "desktop-screenshots": { screenshot: true, max: 8, min: 0 },
};

// PNG colour type 4 or 6 carries an alpha channel; Play wants screenshots and the feature graphic without one.
const pngAlpha = (file) => { const b = readFileSync(file); return b.toString("ascii", 1, 4) === "PNG" && [4, 6].includes(b[25]); };

const pngSize = (file) => {
  const b = readFileSync(file);
  if (b.toString("ascii", 1, 4) === "PNG") return [b.readUInt32BE(16), b.readUInt32BE(20)];
  if (b[0] === 0xff && b[1] === 0xd8) {
    for (let i = 2; i < b.length; ) {
      const marker = b[i + 1];
      const len = b.readUInt16BE(i + 2);
      if (marker >= 0xc0 && marker <= 0xcf && ![0xc4, 0xc8, 0xcc].includes(marker)) return [b.readUInt16BE(i + 7), b.readUInt16BE(i + 5)];
      i += 2 + len;
    }
  }
  return null;
};

for (const f of ["default-language.txt", "contact-website.txt"]) if (!existsSync(join(root, f))) errors.push(`missing ${f}`);
// Every listing (listings/default and listings/custom/<id>) has source/ (what renders it) and listing/<locale>/ (what is
// uploaded). Each listing/<locale>/ is checked; custom listings may also carry keywords.txt. Retired listings
// (listings/<name>/ beside default/) are records of what was uploaded and are not checked.
const listingsRoot = join(root, "listings");
const isDir = (p) => existsSync(p) && statSync(p).isDirectory();
const targets = [];
if (isDir(join(listingsRoot, "default", "listing"))) targets.push(["default", join(listingsRoot, "default")]);
const customRoot = join(listingsRoot, "custom");
if (isDir(customRoot)) for (const id of readdirSync(customRoot).filter((d) => isDir(join(customRoot, d)))) targets.push([`custom/${id}`, join(customRoot, id)]);

for (const [name, dir] of targets) {
  const listing = join(dir, "listing");
  if (!isDir(listing)) continue;
  for (const locale of readdirSync(listing).filter((d) => isDir(join(listing, d)))) {
    const tag = `${name}/${locale}`;
    for (const [file, max] of Object.entries(LIMITS)) {
      const p = join(listing, locale, file);
      if (!existsSync(p)) { (name === "default" ? errors : warn).push(`${tag}: missing ${file}`); continue; }
      const text = readFileSync(p, "utf8").replace(/\r\n/g, "\n").trim();
      const n = [...text].length;
      if (n === 0) errors.push(`${tag}/${file}: empty`);
      if (n > max) errors.push(`${tag}/${file}: ${n} characters, Play allows ${max}`);
      if (/[\u2013\u2014]/u.test(text)) warn.push(`${tag}/${file}: contains an en or em dash (writing-style)`);
    }
    const graphics = join(listing, locale, "graphics");
    for (const [kind, rule] of Object.entries(GRAPHICS)) {
      const gdir = join(graphics, kind);
      const files = existsSync(gdir) ? readdirSync(gdir).filter((f) => /\.(png|jpe?g)$/i.test(f)) : [];
      if (files.length < rule.min && name === "default") errors.push(`${tag}/${kind}: ${files.length} image(s), at least ${rule.min} needed`);
      if (files.length > rule.max) errors.push(`${tag}/${kind}: ${files.length} images, Play allows ${rule.max}`);
      for (const f of files) {
        const size = pngSize(join(gdir, f));
        if (!size) { errors.push(`${tag}/${kind}/${f}: not a PNG or JPEG`); continue; }
        const [w, h] = size;
        if (rule.exact && (w !== rule.exact[0] || h !== rule.exact[1])) errors.push(`${tag}/${kind}/${f}: ${w}x${h}, must be ${rule.exact.join("x")}`);
        if (kind === "icon" && statSync(join(gdir, f)).size > 1024 * 1024) errors.push(`${tag}/icon/${f}: larger than 1024 KB`);
        if (kind !== "icon" && pngAlpha(join(gdir, f))) (kind === "phone-screenshots" || kind === "feature-graphic" ? errors : warn).push(`${tag}/${kind}/${f}: has an alpha channel, Play wants 24-bit PNG or JPEG`);
        if (kind === "phone-screenshots" && Math.min(w, h) < 1080) warn.push(`${tag}/${kind}/${f}: under 1080 px, not eligible for Play's promotion surfaces`);
        if (rule.screenshot) {
          if (Math.min(w, h) < 320 || Math.max(w, h) > 3840) errors.push(`${tag}/${kind}/${f}: ${w}x${h}, sides must be 320 to 3840 px`);
          if (Math.max(w, h) > 2 * Math.min(w, h)) errors.push(`${tag}/${kind}/${f}: ${w}x${h}, long side more than twice the short side`);
        }
      }
    }
    const alt = join(listing, locale, "alt-text.txt");
    if (existsSync(alt)) for (const line of readFileSync(alt, "utf8").split(/\r?\n/).filter(Boolean)) {
      const [file, text = ""] = line.split("\t");
      if (text.length > 140) errors.push(`${tag}/alt-text.txt: ${file} alt text is ${text.length} characters, Play allows 140`);
      if (/^(photo|image|picture) of/i.test(text)) warn.push(`${tag}/alt-text.txt: ${file} starts with "${text.split(" ").slice(0, 2).join(" ")}"`);
    }
  }
  const kw = join(dir, "keywords.txt");
  if (existsSync(kw)) {
    const words = readFileSync(kw, "utf8").split(/\r?\n/).map((l) => l.trim()).filter(Boolean);
    if (words.length > 500) errors.push(`${name}/keywords.txt: ${words.length} keywords, Play allows 500`);
    for (const k of words) if (k.length > 80) errors.push(`${name}/keywords.txt: "${k.slice(0, 30)}..." longer than 80 characters`);
    if (new Set(words.map((k) => k.toLowerCase())).size !== words.length) warn.push(`${name}/keywords.txt: duplicate keywords`);
  }
}

for (const w of warn) console.warn(`warning  ${w}`);
for (const e of errors) console.error(`error    ${e}`);
console.log(errors.length ? `${errors.length} problem(s)` : "Listing is valid.");
process.exit(errors.length ? 1 : 0);
