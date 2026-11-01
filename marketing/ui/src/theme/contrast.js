/**
 * Color and contrast utilities mirroring Compose:
 * - Color.luminance()
 * - Color.vibrant() in BackgroundModifiers.kt
 * - Color.adjustedForContrast() in ColorContrastUtils.kt
 */

export const hexToRgb = (hex) => {
  const clean = hex.replace("#", "").trim();
  const full = clean.length === 3 ? clean.split("").map((c) => c + c).join("") : clean;
  const num = parseInt(full, 16);
  return [(num >> 16) & 255, (num >> 8) & 255, num & 255];
};

export const rgbToHex = (r, g, b) =>
  `#${[r, g, b].map((x) => Math.round(Math.min(255, Math.max(0, x))).toString(16).padStart(2, "0")).join("").toUpperCase()}`;

export const rgbToHsv = (r, g, b) => {
  const rn = r / 255;
  const gn = g / 255;
  const bn = b / 255;
  const max = Math.max(rn, gn, bn);
  const min = Math.min(rn, gn, bn);
  const delta = max - min;

  let h = 0;
  if (delta !== 0) {
    if (max === rn) {
      h = ((gn - bn) / delta) % 6;
    } else if (max === gn) {
      h = (bn - rn) / delta + 2;
    } else {
      h = (rn - gn) / delta + 4;
    }
    h *= 60;
    if (h < 0) h += 360;
  }

  const s = max === 0 ? 0 : delta / max;
  const v = max;
  return [h, s, v];
};

export const hsvToRgb = (h, s, v) => {
  const c = v * s;
  const hp = ((h % 360) + 360) % 360 / 60;
  const x = c * (1 - Math.abs((hp % 2) - 1));
  const m = v - c;

  let r1 = 0;
  let g1 = 0;
  let b1 = 0;

  if (hp >= 0 && hp < 1) {
    r1 = c; g1 = x; b1 = 0;
  } else if (hp >= 1 && hp < 2) {
    r1 = x; g1 = c; b1 = 0;
  } else if (hp >= 2 && hp < 3) {
    r1 = 0; g1 = c; b1 = x;
  } else if (hp >= 3 && hp < 4) {
    r1 = 0; g1 = x; b1 = c;
  } else if (hp >= 4 && hp < 5) {
    r1 = x; g1 = 0; b1 = c;
  } else if (hp >= 5 && hp < 6) {
    r1 = c; g1 = 0; b1 = x;
  }

  return [(r1 + m) * 255, (g1 + m) * 255, (b1 + m) * 255];
};

/** Relative luminance in sRGB matching androidx.compose.ui.graphics.luminance() */
export const luminance = (hexOrRgb) => {
  const [r, g, b] = typeof hexOrRgb === "string" ? hexToRgb(hexOrRgb) : hexOrRgb;
  const toLinear = (c) => {
    const v = c / 255;
    return v <= 0.04045 ? v / 12.92 : Math.pow((v + 0.055) / 1.055, 2.4);
  };
  return 0.2126 * toLinear(r) + 0.7152 * toLinear(g) + 0.0722 * toLinear(b);
};

/**
 * Port of Color.vibrant() from BackgroundModifiers.kt:
 * Adjusts hue, saturation, and value (brightness) in HSV color space.
 */
export const vibrant = (hex, hueShift = 0, satBoost = 1.3, valBoost = 1.1) => {
  const [r, g, b] = hexToRgb(hex);
  const [h, s, v] = rgbToHsv(r, g, b);
  let newH = (h + hueShift) % 360;
  if (newH < 0) newH += 360;
  const newS = Math.min(1, Math.max(0, s * satBoost));
  const newV = Math.min(1, Math.max(0, v * valBoost));
  const [nr, ng, nb] = hsvToRgb(newH, newS, newV);
  return rgbToHex(nr, ng, nb);
};

/**
 * Port of Color.adjustedForContrast() from ColorContrastUtils.kt:
 * Modifies HSV brightness and saturation based on effective background luminance
 * calculated from surfaceVariant (0.64) and primary (0.36).
 */
export const adjustedForContrast = (colorHex, { surfaceVariantHex, primaryHex }) => {
  const baseLuminance = luminance(surfaceVariantHex);
  const gradientLuminance = luminance(primaryHex);
  const effectiveBgLuminance = baseLuminance * 0.64 + gradientLuminance * 0.36;

  const [r, g, b] = hexToRgb(colorHex);
  let [h, s, v] = rgbToHsv(r, g, b);

  if (effectiveBgLuminance > 0.45) {
    v = Math.min(v, 0.35);
    s = Math.max(s, 0.6);
  } else {
    v = Math.max(v, 0.9);
    s = Math.min(s, 0.3);
  }

  const [nr, ng, nb] = hsvToRgb(h, s, v);
  return rgbToHex(nr, ng, nb);
};
