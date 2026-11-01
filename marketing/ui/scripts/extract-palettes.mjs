// Generates src/theme/palettes.css from App Toolkit's Compose colour files, so the web
// tokens are the exact values the app ships.
//
//   npm run palettes -- ../App-Toolkit
import { readFileSync, readdirSync, statSync, writeFileSync } from "node:fs";
import { join, basename, dirname } from "node:path";
import { argbFromHex, hexFromArgb, Hct, MaterialDynamicColors, SchemeTonalSpot } from "@material/material-color-utilities";
import { vibrant, adjustedForContrast, hexToRgb } from "../src/theme/contrast.js";

/** Wallpaper-style palette (Android 12+ TonalSpot) seeded from Android Insider emerald green. */
const DYNAMIC_SEED = process.env.SEED ?? "#00A86B";

const root = process.argv[2] ?? join(import.meta.dirname, "../../../../App Toolkit");
const colorsDir = join(
  root,
  "library/core/designsystem/src/main/kotlin/com/mihaicristiancondrea/android/libs/apptoolkit/core/designsystem/ui/style/colors",
);

const walk = (dir) =>
  readdirSync(dir).flatMap((f) => {
    const p = join(dir, f);
    return statSync(p).isDirectory() ? walk(p) : f === "Colors.kt" ? [p] : [];
  });

const kebab = (s) => s.replace(/[A-Z]/g, (c) => "-" + c.toLowerCase());

const parseColorMap = (lines) => {
  const map = {};
  for (const l of lines) {
    const m = l.match(/--md-sys-color-([^:]+):\s*(#[0-9A-Fa-f]{6});/);
    if (m) map[m[1]] = m[2];
  }
  return map;
};

const generateDerivedTokens = (colorMap) => {
  const primary = colorMap["primary"] ?? "#00A86B";
  const secondary = colorMap["secondary"] ?? primary;
  const secondaryContainer = colorMap["secondary-container"] ?? secondary;
  const surfaceVariant = colorMap["surface-variant"] ?? "#E4ECE2";
  const onSurface = colorMap["on-surface"] ?? "#191C19";
  const onSurfaceVariant = colorMap["on-surface-variant"] ?? "#414942";

  const v1 = vibrant(secondary, -12, 1.4, 1.1);
  const v2 = vibrant(secondaryContainer, 24, 1.4, 1.1);
  const v3 = vibrant(primary, -16, 1.2, 1.1);
  const [r1, g1, b1] = hexToRgb(v1);
  const [r2, g2, b2] = hexToRgb(v2);
  const [r3, g3, b3] = hexToRgb(v3);

  const ctx = { surfaceVariantHex: surfaceVariant, primaryHex: primary };

  return [
    `  --sc-v1: rgba(${r1}, ${g1}, ${b1}, 0.36);`,
    `  --sc-v2: rgba(${r2}, ${g2}, ${b2}, 0.36);`,
    `  --sc-v3: rgba(${r3}, ${g3}, ${b3}, 0.36);`,
    `  --sc-v1-subtle: rgba(${r1}, ${g1}, ${b1}, 0.05);`,
    `  --sc-qs-icon-primary: ${adjustedForContrast(primary, ctx)};`,
    `  --sc-qs-text-primary: ${adjustedForContrast(onSurface, ctx)};`,
    `  --sc-qs-text-secondary: ${adjustedForContrast(onSurfaceVariant, ctx)};`,
  ];
};

const blocks = [];
const names = [];
const paletteMap = {};

for (const file of walk(colorsDir).sort()) {
  const name = basename(dirname(file));
  const src = readFileSync(file, "utf8");
  const light = [];
  const dark = [];
  for (const m of src.matchAll(/val (\w+?)(Light|Dark)\s*=\s*Color\(0x[Ff]{2}([0-9A-Fa-f]{6})\)/g)) {
    const line = `  --md-sys-color-${kebab(m[1])}: #${m[3].toUpperCase()};`;
    (m[2] === "Light" ? light : dark).push(line);
  }
  if (!light.length) continue;
  names.push(name);

  const lightMap = parseColorMap(light);
  const darkMap = parseColorMap(dark);
  paletteMap[name] = { lightMap, darkMap, light, dark };

  const lightDerived = generateDerivedTokens(lightMap);
  const darkDerived = generateDerivedTokens(darkMap);

  const sel = `[data-palette="${name}"]`;
  blocks.push(`${sel} {\n${light.concat(lightDerived).join("\n")}\n}`);
  const darkSel = `[data-palette="${name}"][data-theme="dark"]`;
  blocks.push(`${darkSel} {\n  color-scheme: dark;\n${dark.concat(darkDerived).join("\n")}\n}`);
}

const roles = Object.entries(MaterialDynamicColors).filter(([, v]) => v && typeof v.getArgb === "function");
const dynLines = (isDark) => {
  const scheme = new SchemeTonalSpot(Hct.fromInt(argbFromHex(DYNAMIC_SEED)), isDark, 0);
  return roles
    .filter(([k]) => !/^(controlActivated|controlNormal|controlHighlight|textPrimaryInverse|textSecondaryAndTertiaryInverse|textPrimaryInverseDisableOnly|textSecondaryAndTertiaryInverseDisabled|textHintInverse|highestSurface|surfaceTint|background|onBackground|shadow)$/.test(k))
    .map(([k, v]) => `  --md-sys-color-${kebab(k)}: ${hexFromArgb(v.getArgb(scheme)).toUpperCase()};`);
};

names.push("android-vivid");
const androidLightLines = [...paletteMap["android"].light];
const androidDarkLines = [...paletteMap["android"].dark];
const vividOverrides = {
  primary: "#3DDC84",
  "on-primary": "#00210E",
  "primary-container": "#66FEA2",
  "on-primary-container": "#00210E",
};

const applyOverrides = (lines, overrides) => {
  const map = parseColorMap(lines);
  Object.assign(map, overrides);
  const result = [];
  for (const [k, v] of Object.entries(map)) {
    result.push(`  --md-sys-color-${k}: ${v};`);
  }
  return { lines: result, map };
};

const vividLight = applyOverrides(androidLightLines, vividOverrides);
const vividDark = applyOverrides(androidDarkLines, {
  primary: "#66FEA2",
  "primary-container": "#00522B",
});

blocks.push(`[data-palette="android-vivid"] {\n${vividLight.lines.concat(generateDerivedTokens(vividLight.map)).join("\n")}\n}`);
blocks.push(`[data-palette="android-vivid"][data-theme="dark"] {\n  color-scheme: dark;\n${vividDark.lines.concat(generateDerivedTokens(vividDark.map)).join("\n")}\n}`);

names.push("dynamic");
const dynLightLines = dynLines(false);
const dynDarkLines = dynLines(true);
const dynLightMap = parseColorMap(dynLightLines);
const dynDarkMap = parseColorMap(dynDarkLines);

blocks.push(`:where(:root),\n[data-palette="dynamic"] {\n${dynLightLines.concat(generateDerivedTokens(dynLightMap)).join("\n")}\n}`);
blocks.push(`:where([data-theme="dark"]),\n[data-palette="dynamic"][data-theme="dark"] {\n  color-scheme: dark;\n${dynDarkLines.concat(generateDerivedTokens(dynDarkMap)).join("\n")}\n}`);

writeFileSync(
  new URL("../src/theme/palettes.css", import.meta.url),
  `/* Generated by scripts/extract-palettes.mjs from App Toolkit. Do not edit by hand.\n` +
    ` * Palettes: ${names.join(", ")}. Default: dynamic (TonalSpot from Android Insider green; android/blue/... are the exact App Toolkit palettes).\n` +
    ` * Use <html data-palette="blue" data-theme="dark"> to switch. */\n\n` +
    blocks.join("\n\n") +
    "\n",
);
console.log("palettes:", names.join(", "));
