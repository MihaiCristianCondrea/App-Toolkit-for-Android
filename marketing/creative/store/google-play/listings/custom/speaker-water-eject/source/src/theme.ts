// The Water Eject campaign takes its whole palette from the Water Eject sheet (ToolkitTileVisuals.kt's blue
// tool, as the kit draws it): its content blue (the Start button and icon), its brighter container blue and
// the water visual's container fill, with the ground, the glass ribbons, the ink and the decoration all in
// that blue family. The app's UI keeps its own colours.
export const SHEET_BLUE = "#0B4FA8";
export const SHEET_BLUE_BRIGHT = "#2D7DF6";
export const SHEET_BLUE_CONTAINER = "#B8CDE3";
/** Headlines: a deep navy from the same family. */
export const INK = "#0B1D36";
export const PILL = SHEET_BLUE_CONTAINER;
export const ON_PILL = SHEET_BLUE;
/** The ground, top to bottom; the feature graphic uses the deeper pair so it holds its edge on a white page. */
export const FIELD: [string, string] = ["#EEF3FB", "#D2E1F4"];
export const FIELD_DEEP: [string, string] = ["#DCE7F7", "#BED3EF"];
/** The glass ribbons: a band shading through these, a refracting edge and white rims. */
export const RIBBON_STOPS = ["#9CBDEB", "#F6F9FF", "#C9DAF2", "#7EA6E3"];
export const RIBBON_EDGE = SHEET_BLUE_BRIGHT;
/** MaterialShapes, and the large one drawn behind a phone. */
export const SHAPE_TONES = ["#C3D5F0", "#ABC4EA", "#93B4E4"];
export const SHAPE_BEHIND = "#D6E3F6";
export const BUBBLE_TINT = "#9DBDF0";
export const FONT = "'Google Sans Flex', sans-serif";
export const PALETTE = "captured";
