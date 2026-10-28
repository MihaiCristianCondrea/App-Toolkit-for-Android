import {createMarketingEmbed, WINDOWS, shellLayout} from "material-next-web-components/marketing.js";
import {setAssetBase} from "./kit/assets.js";
import {icon} from "./kit/shell.js";
import {loadFonts} from "./theme/index.js";
import {SCREENS, byId} from "./screens/index.js";
import {shapeBadge, toolMetaChips, toolOverlay} from "./kit/components.js";

export {SCREENS, byId, WINDOWS, shellLayout};
export const PARTS = {shapeBadge, toolMetaChips, toolOverlay, icon};
const embed = createMarketingEmbed({
  screens: SCREENS,
  parts: PARTS,
  loadTheme: async ({assetBase = "/"}) => { setAssetBase(assetBase); await loadFonts(); },
});
export const {setup, mount, mountPart} = embed;
