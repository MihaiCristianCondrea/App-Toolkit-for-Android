/**
 * Window sizes and the layout rules the app applies to them, ported from the Android sources, which stay the
 * reference: change those first, then these.
 *
 *  - App Toolkit (3.0.0-pre23) navigation/layout/ShellLayout.kt: ShellLayoutPolicy and ShellLayoutInfo
 *  - App Toolkit shell/chrome/NavigationSurfaces.kt: the rail and drawer widths
 *  - feature/articles/.../listing/ui/views/list/render/ListingLayout.kt: listingLayout()
 */

/** ShellLayoutPolicy's defaults, Material's window size class breakpoints. The app keeps them. */
export const SHELL_POLICY = { railFrom: 600, expandedRailFrom: 840, permanentDrawerFrom: 1200, listDetailFrom: 600, largeTopBarFrom: 480 };

/** CollapsedRailWidth, ExpandedRailWidth and PermanentDrawerWidth. */
export const NAV_WIDTHS = { rail: 96, expandedRail: 280, permanentDrawer: 300 };

/**
 * The windows the kit draws, in dp (CSS px). `system` decides the system bars: a phone or tablet has the status bar
 * and gesture navigation, a desktop window (ChromeOS) has neither. `device` names the frame in kit/shell.js device().
 */
export const WINDOWS = {
  /** Pixel 9 Pro XL: 1344 x 2992 px at 3.125. */
  phone: { width: 430, height: 957, system: "phone", device: "pixel_9_pro_xl" },
  /** Pixel 9 Pro Fold, open: 2076 x 2152 px at 2.4375. */
  fold: { width: 852, height: 883, system: "phone", device: "pixel_9_pro_fold" },
  /**
   * A Chromebook window filling the Pixelbook Go's screen (assets/device-frames/pixelbook_go: the 1826 x 1079 px
   * screen of Google's device art at 1.5).
   */
  desktop: { width: 1218, height: 720, system: "desktop", device: "pixelbook_go" },
};

/** `window` as given (an object or a WINDOWS key), or the phone. */
export const windowOf = (win) => (typeof win === "string" ? WINDOWS[win] : win) ?? WINDOWS.phone;

/** ShellLayoutPolicy.resolve() for Auto, and the parts of ShellLayoutInfo the screens read. */
export const shellLayout = (win) => {
  const w = windowOf(win);
  const mode = w.width >= SHELL_POLICY.permanentDrawerFrom ? "permanentDrawer"
    : w.width >= SHELL_POLICY.expandedRailFrom ? "expandedRail"
      : w.width >= SHELL_POLICY.railFrom ? "rail" : "bottomBar";
  // The expanded rail layout starts with its rail collapsed (ShellFrame.rememberShellFrameState).
  const navWidth = mode === "permanentDrawer" ? NAV_WIDTHS.permanentDrawer : mode === "bottomBar" ? 0 : NAV_WIDTHS.rail;
  return {
    ...w,
    mode,
    /** A rail or the permanent drawer beside the content (keepsNavigationBeside). */
    framed: mode !== "bottomBar",
    navWidth,
    listDetail: w.width >= SHELL_POLICY.listDetailFrom,
    allowsLargeTopBar: w.height >= SHELL_POLICY.largeTopBarFrom,
    statusBar: w.system === "phone" ? 48 : 0,
    systemNav: w.system === "phone" ? 24 : 0,
  };
};

/** ListingLayout.kt. `width` and `height` are the pane minus its 16 dp padding. */
export const listingLayout = ({ width, height, shortWindow = false, spacing = 16 }) => {
  if (width < 600 && !shortWindow) return { columns: 1, wide: false, stackedBanner: true, maxLeadImageHeight: Infinity, width, cellWidth: width };
  const columns = Math.min(3, Math.max(1, Math.floor((width + spacing) / (300 + spacing))));
  const cellWidth = (width - spacing * (columns - 1)) / columns;
  return { columns, wide: true, stackedBanner: cellWidth * (9 / 16) + 240 <= height, maxLeadImageHeight: height * 0.8, width, cellWidth };
};

/** ListDetailScene.kt: the list rests at 38% of the panes' width, after a 24 dp separator. */
export const LIST_DETAIL = { listFraction: 0.38, separator: 24 };

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
