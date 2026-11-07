export const SCREENS: Record<string, [string, Record<string, unknown>]> = {
  ruler: ["numbers", {activeSheet: "ruler", rulerAdvanced: false}],
  protractor: ["numbers", {activeSheet: "protractor", protractorAngle: 45}],
  advanced: ["numbers", {activeSheet: "ruler", rulerAdvanced: true}],
  converter: ["numbers", {activeSheet: "unit_converter"}],
  measure: ["numbers", {activeSheet: "none"}],
};
export const SCROLL: Record<string, number> = {};
