const water = { activeToolId: "water_eject" };
export const SCREENS: Record<string, [string, Record<string, unknown>]> = {
 "water-eject": ["quick-tools", water],
 "water-frequency": ["quick-tools", water],
 "water-guidance": ["quick-tools", water],
 "water-cycle": ["quick-tools", water],
 "water-running": ["quick-tools", { ...water, waterRunning: true, waterSeconds: 20 }],
 "water-stop": ["quick-tools", { ...water, waterRunning: true, waterSeconds: 8 }],
 "quick-tools": ["quick-tools", { activeToolId: "none" }],
 "unit-converter": ["numbers", { activeSheet: "unit_converter" }],
 level: ["quick-tools", { activeToolId: "bubble_level" }],
 measure: ["numbers", { activeSheet: "none" }],
};

/**
 * Screens shown scrolled, in CSS px, at every window. The Water Eject sheet opens from the Water Eject card
 * in Quick Tools, so the water screenshots scroll the list until that card sits where Compass and Bubble
 * Level would (they have their own screenshot): the card that opened the sheet shows above it.
 */
export const SCROLL: Record<string, number> = { "water-eject": 672, "water-running": 672, "water-stop": 672 };
