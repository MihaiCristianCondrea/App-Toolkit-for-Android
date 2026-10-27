import {mountMarketingGallery} from "material-next-web-components/marketing.js";
import "./theme/index.js";
import {loadFonts} from "./theme/index.js";
import {SCREENS} from "./screens/index.js";
import {PALETTES} from "./controls.js";
import {PRODUCT_CONFIG} from "./config/product.js";
import {hubNavigation} from "./hub-navigation.js";

await loadFonts();
mountMarketingGallery({
  product: PRODUCT_CONFIG,
  screens: SCREENS,
  palettes: PALETTES,
  navigationMode: "modal",
  defaults: {palette: "captured"},
  navigation: () => hubNavigation("screens"),
});
