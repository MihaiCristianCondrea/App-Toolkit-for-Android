/**
 * The UI of each screen the layout has to respect, as selectors the probe measures (src/UiProbe.tsx).
 * `critical` must stay legible: inside the safe margins, inside the image, clear of captions and not
 * covered by another phone. `secondary` is measured and reported, and may be cropped on purpose.
 *
 * Selector forms: a CSS selector; `css@n` for its n-th match (from 0); `text:tag|start` for an element of
 * that tag whose text starts with `start`; `a + b` for the box around both.
 */
export type UiSpec = { critical: Record<string, string>; secondary?: Record<string, string> };

const waterTitle = ".tk-sheet-title-row";
export const UI_SELECTORS: Record<string, UiSpec> = {
  "water-eject": {
    critical: { title: waterTitle, visual: ".water-visual", readout: ".water-readout", instructions: ".water-eject > p:not(.water-note)", button: ".water-eject md-filled-button", note: ".water-note" },
  },
  "water-running": {
    critical: { title: waterTitle, visual: ".water-visual", readout: ".water-readout" },
    secondary: { button: ".water-eject md-filled-button", note: ".water-note" },
  },
  "water-stop": {
    critical: { title: waterTitle, readout: ".water-readout", button: ".water-eject md-filled-button" },
    secondary: { visual: ".water-visual", note: ".water-note" },
  },
  "unit-converter": {
    critical: { title: waterTitle, from: "text:span|Metre + md-outlined-text-field@0", to: "text:span|Centimetre + md-outlined-text-field@1" },
  },
  "quick-tools": {
    critical: {
      compass: "text:span|Compass + text:span|Northwest",
      level: "text:span|Bubble Level + text:span|0.4",
      flashlight: "text:span|Flashlight + text:span|Swipe for brightness",
      sos: "text:span|SOS + text:span|Morse with the torch",
    },
  },
  level: {
    critical: { title: waterTitle, level: ".tk-level", readings: ".tk-pill-pair" },
    secondary: { tile: ".tk-qs-integration-group" },
  },
};
