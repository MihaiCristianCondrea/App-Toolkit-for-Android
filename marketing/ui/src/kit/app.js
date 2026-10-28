import { definePreviewApp } from "material-next-web-components/marketing.js";
import { asset } from "./assets.js";

/**
 * App Toolkit's one shell declaration (sample/app AppGraph.kt with the library's toolkitFooter()). Material Next
 * draws the shell from it: app bars, the navigation each window calls for, insets, drawers and sheets. Settings,
 * Help & feedback, Updates and Share open library pages the marketing UI does not draw, so they only close the
 * navigation. Tab ids are the marketing screens they open; `numbers` keeps its route id under the Measure label.
 */
export const APP = definePreviewApp({
  name: "App Toolkit",
  logo: asset("/logos/app_logo.svg"),
  tabs: [
    { id: "quick-tools", label: "Quick Tools", icon: { name: "grid_view", rounded: true } },
    { id: "numbers", label: "Measure", icon: "straighten" },
    { id: "apps", label: "Apps", icon: "apps" },
  ],
  drawer: {
    footer: [
      { id: "settings", label: "Settings", icon: "settings" },
      { id: "help", label: "Help & feedback", icon: "help_outline" },
      { id: "updates", label: "Updates", icon: "event_note" },
      { id: "share", label: "Share", icon: "share" },
    ],
  },
  // The app bar's overflow on tab roots (overflow { supportUs() }).
  overflow: [{ id: "support", label: "Support the app", icon: "volunteer_activism" }],
});
