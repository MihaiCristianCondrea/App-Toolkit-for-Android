import "./palettes.css";
import "./base.css";
import "../kit/kit.css";
import "../kit/md.js";
import { applyProductTypefaces, loadProductFonts } from "material-next-web-components/marketing.js";
import { IDENTITY } from "../config/identity.js";

// The typeface tokens before anything renders; the faces themselves load from the current asset base.
applyProductTypefaces(IDENTITY);

/** Registers the identity's fonts (Google Sans Flex) from the current asset base and waits for the icon fonts. */
export const loadFonts = () => loadProductFonts(IDENTITY);
