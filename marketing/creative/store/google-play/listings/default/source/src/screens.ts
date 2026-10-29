// What each screenshot mounts from the UI kit: a real screen in a real state. Copy is in strings/<locale>.json.
const WHEEL = ["Pizza", "Sushi", "Tacos", "Curry"];
export const SCREENS: Record<string, [string, Record<string, unknown>]> = {
  "quick-tools": ["quick-tools", { activeToolId: "none" }],
  measure: ["numbers", { activeSheet: "none" }],
  play: ["play", { activeToolId: "none", wheelText: WHEEL.join("\n") }],
  // The wheel at rest on its pick: Sushi (choice 2 of 4) under the pointer, as a spin leaves it.
  "decision-wheel": ["play", { activeToolId: "decision_wheel", wheelText: WHEEL.join("\n"), wheelWinner: "Sushi", wheelRotation: 360 - 1.5 * (360 / WHEEL.length) }],
  protractor: ["numbers", { activeSheet: "protractor", protractorAngle: 60 }],
  "bill-splitter": ["numbers", { activeSheet: "bill_splitter" }],
  "water-eject": ["quick-tools", { activeToolId: "water_eject" }],
  sos: ["quick-tools", { activeToolId: "morse_sos" }],
  "unit-converter": ["numbers", { activeSheet: "unit_converter" }],
};
