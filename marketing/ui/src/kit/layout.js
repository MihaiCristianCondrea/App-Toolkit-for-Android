// Shared App Toolkit window classes and shell geometry come from Material Next.
import {shellLayout, windowOf} from "material-next-web-components/marketing.js";
export {SHELL_POLICY, NAV_WIDTHS, WINDOWS, windowOf, shellLayout, LIST_DETAIL} from "material-next-web-components/marketing.js";

/** ListingLayout.kt. `width` and `height` are the pane minus its 16 dp padding. */
export const listingLayout = ({ width, height, shortWindow = false, spacing = 16 }) => {
  if (width < 600 && !shortWindow) return { columns: 1, wide: false, stackedBanner: true, maxLeadImageHeight: Infinity, width, cellWidth: width };
  const columns = Math.min(3, Math.max(1, Math.floor((width + spacing) / (300 + spacing))));
  const cellWidth = (width - spacing * (columns - 1)) / columns;
  return { columns, wide: true, stackedBanner: cellWidth * (9 / 16) + 240 <= height, maxLeadImageHeight: height * 0.8, width, cellWidth };
};


/**
 * The feed's layout in a screen of `win`: the window minus the navigation beside it, the app bar and the system bars,
 * minus the grid's 16 dp padding (HomeFeedColumn.kt). `paneWidth` overrides the width for a list pane.
 */
export const feedLayout = (win, { paneWidth, chips = true } = {}) => {
  const l = shellLayout(win);
  const width = (paneWidth ?? l.width - l.navWidth) - 32;
  const bottomBar = l.framed ? 0 : 80;
  const height = l.height - l.statusBar - 64 - (chips ? 80 : 0) - bottomBar - l.systemNav - 32;
  // WindowSizeClass HEIGHT_DP_MEDIUM_LOWER_BOUND is 480 dp.
  return listingLayout({ width, height, shortWindow: l.height < 480 });
};
