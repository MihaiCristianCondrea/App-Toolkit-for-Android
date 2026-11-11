// App Toolkit's trailer: every tool keeps its store listing's own world. The palettes below come from the
// five custom listings' themes; the opening and the end card stand on a warm neutral that holds them all.
export const FONT = "'Google Sans Flex', sans-serif";
export const PALETTE = "captured";
export type World = { name: string; field: string; ink: string; accent: string; bright: string; container: string; shape: string; tones: string[] };
export const WORLDS = {
  water: { name: "Water Eject", field: "#EEF3FB", ink: "#0B1D36", accent: "#0B4FA8", bright: "#2D7DF6", container: "#B8CDE3", shape: "#D6E3F6", tones: ["#C3D5F0", "#ABC4EA", "#93B4E4"] },
  ruler: { name: "Ruler & Protractor", field: "#FFF4D9", ink: "#3A2600", accent: "#8F5700", bright: "#F6A800", container: "#FDE7A2", shape: "#FFD03B", tones: ["#F6A800", "#FFD03B", "#EE9B00"] },
  unit: { name: "Unit Converter", field: "#F7F8F1", ink: "#153F3C", accent: "#23655F", bright: "#55B4A6", container: "#B6E5DF", shape: "#B6E5DF", tones: ["#B6E5DF", "#EED995", "#D9ECE5"] },
  bill: { name: "Tip & Bill Splitter", field: "#FFF6EE", ink: "#502A25", accent: "#A43D31", bright: "#D4745D", container: "#F7D8C9", shape: "#F3A18A", tones: ["#F3A18A", "#F3D49B", "#F7D8C9"] },
  wheel: { name: "Decision Wheel", field: "#F7F1FA", ink: "#35233E", accent: "#684379", bright: "#BFA0D0", container: "#E7D9EF", shape: "#E3C4DF", tones: ["#BFA0D0", "#E3C4DF", "#DDA795"] },
} satisfies Record<string, World>;
export const NEUTRAL = { field: "#FBF8F2", ink: "#1F1B16", accent: "#5B4E3C" };
/** Confetti: one colour from each world, so the finale gathers them all. */
export const CONFETTI = [WORLDS.water.bright, WORLDS.ruler.bright, WORLDS.unit.bright, WORLDS.bill.shape, WORLDS.wheel.bright, "#D9C67D"];
