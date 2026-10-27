import {mountScreenPrototype} from "material-next-web-components/marketing.js";
import {loadFonts} from "./theme/index.js";
import {SCREENS} from "./screens/index.js";
import {PALETTES} from "./controls.js";
import {PRODUCT_CONFIG} from "./config/product.js";

await loadFonts();
mountScreenPrototype({
  product: PRODUCT_CONFIG,
  screens: SCREENS,
  palettes: PALETTES,
  defaults: {palette: "captured"},
  adsToggle: true,
});
