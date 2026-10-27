import "./palettes.css";
import "./base.css";
import "../kit/kit.css";
import "../kit/md.js";
import { asset } from "../kit/assets.js";

/** Registers Google Sans Flex (all weights and widths) from the current asset base. */
export const loadFonts = async () => {
  if ([...document.fonts].some((f) => f.family === "Google Sans Flex")) return;
  const face = new FontFace("Google Sans Flex", `url(${asset("fonts/GoogleSansFlex.ttf")}) format("truetype")`, {
    weight: "1 1000",
    stretch: "25% 151%",
    display: "block",
  });
  document.fonts.add(await face.load());
  await document.fonts.load('24px "Material Symbols Outlined"', "home");
};
