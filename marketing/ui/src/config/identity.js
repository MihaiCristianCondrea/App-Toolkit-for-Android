import {defineProductIdentity, supportUs, toolkitFooter} from "material-next-web-components/marketing.js";

/**
 * App Toolkit's identity, declared once, as sample/app declares it in AppGraph.kt (`toolkitGraph(appTitle,
 * appIcon) { tab(...); drawer { toolkitFooter() }; overflow { supportUs() } }`) and its theme (Google Sans Flex,
 * the palettes in theme/palettes.css). Material Next draws the app bars, rails, drawers and previews from
 * `IDENTITY.app` and the hub from `IDENTITY.product`; the assets stay in public/.
 *
 * Tab ids are the marketing screens they open; `numbers` keeps its route id under the Measure label. Settings,
 * Help & feedback, Updates, Share and Support the app open library pages the marketing UI does not draw, so they
 * only close the navigation.
 */
export const IDENTITY = defineProductIdentity({
  id: "app-toolkit",
  name: "App Toolkit",
  icons: {
    // The launcher icon (hub, favicons) and the mark beside the name in the drawer (R.drawable.app_logo).
    app: "/logos/icon.svg",
    logo: "/logos/app_logo.svg",
  },
  fonts: {
    // AppTypography: the Material 3 scale set in Google Sans Flex, all weights and widths.
    brand: { family: "Google Sans Flex", src: "fonts/GoogleSansFlex.ttf", weight: "1 1000", stretch: "25% 151%" },
    symbols: ["Material Symbols Outlined", "Material Symbols Rounded"],
  },
  colors: {
    // theme/palettes.css (scripts/extract-palettes.mjs), in picker order. "captured" keeps each screen's own.
    palettes: ["captured", "dynamic", "android-vivid", "android", "blue", "green", "purple", "rose", "monochrome", "orange", "red", "yellow", "skin", "halloween", "christmas"],
    defaultPalette: "captured",
  },
  destinations: {
    tabs: [
      { id: "quick-tools", label: "Quick Tools", icon: { name: "grid_view", rounded: true } },
      { id: "numbers", label: "Measure", icon: "straighten" },
      { id: "apps", label: "Apps", icon: "apps" },
    ],
    drawer: { footer: toolkitFooter() },
    overflow: [supportUs()],
  },
  links: {
    repository: "https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android",
    privacy: "https://mihaicristiancondrea.github.io/profile/#privacy-policy",
    codeOfConduct: "https://mihaicristiancondrea.github.io/profile/#code-of-conduct",
  },
});

/** The shell declaration every screen renders in: `previewScreen(APP, s, {...})`. */
export const APP = IDENTITY.app;
