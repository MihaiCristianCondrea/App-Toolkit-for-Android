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
