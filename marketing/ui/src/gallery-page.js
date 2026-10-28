import {mountMarketingHub} from "material-next-web-components/marketing.js";
import "./theme/index.js";
import {loadFonts} from "./theme/index.js";
import {SCREENS} from "./screens/index.js";
import {PALETTES} from "./controls.js";
import {PRODUCT_CONFIG} from "./config/product.js";
import {IDENTITY} from "./config/identity.js";

await loadFonts();
// The gallery and each screen's prototype in one page: no drawer, no footer.
// screen.html stays for captures and direct links.
mountMarketingHub({
  product: PRODUCT_CONFIG,
  screens: SCREENS,
  palettes: PALETTES,
  navigationMode: "none",
  defaults: {palette: IDENTITY.defaultPalette},
  prototypeDefaults: {palette: IDENTITY.defaultPalette},
  adsToggle: true,
});
