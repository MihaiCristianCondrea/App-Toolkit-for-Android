// States of the actual Marketing UI. The Water Eject states need parity with Android before rendering.
const WHEEL = ["Pizza", "Sushi", "Tacos", "Curry"];
export const SCREENS: Record<string, [string, Record<string, unknown>]> = {
  "water-eject": ["quick-tools", { activeToolId: "water_eject" }],
  "water-running": ["quick-tools", { activeToolId: "water_eject", waterRunning: true, waterSeconds: 20 }],
  "quick-tools": ["quick-tools", { activeToolId: "none" }],
  sos: ["quick-tools", { activeToolId: "morse_sos" }],
  measure: ["numbers", { activeSheet: "none" }],
  "unit-converter": ["numbers", { activeSheet: "unit_converter" }],
  play: ["play", { activeToolId: "none", wheelText: WHEEL.join("\n") }],
  "decision-wheel": ["play", { activeToolId: "decision_wheel", wheelText: WHEEL.join("\n"), wheelWinner: "Sushi", wheelRotation: 225 }],
};
