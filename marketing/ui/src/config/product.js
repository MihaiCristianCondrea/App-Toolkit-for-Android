import { IDENTITY } from "./identity.js";

/**
 * What only the marketing pages show about App Toolkit: the motion stories, the specs and the release. The
 * name, icon, links and palettes come from the identity (config/identity.js), so the hub, the previews and the
 * app bars always agree.
 */
export const PRODUCT_CONFIG = {
  ...IDENTITY.product,
  toolkitVersion: "3.0.0",

  theme: {
    defaultTheme: "system",
  },

  motion: {
    title: "Interactive Storytelling & Motion Sequences",
    subtitle:
      "Screen sequences, transitions, and marketing motion scenes for Play Store reels and social demos.",
    stories: [
      {
        id: "tools-to-numbers",
        title: "From Hardware Tiles to Everyday Numbers",
        subtitle: "Quick Tools -> Numbers Bento",
        description:
          "User browses categorized Quick Settings tiles, then switches to Numbers to split a restaurant bill or increment a live tally counter.",
        screens: ["quick-tools", "numbers"],
        accent: "#FFCA28",
      },
      {
        id: "numbers-to-apps",
        title: "Companion App Ecosystem",
        subtitle: "Numbers -> Apps Catalog",
        description:
          "From daily numerical utilities to specialized Android apps by Mihai-Cristian Condrea.",
        screens: ["numbers", "apps"],
        accent: "#4285F4",
      },
    ],
  },

  specs: [
    { label: "Target Device", value: "Pixel 9 Pro XL (430 x 957 dp)" },
    { label: "Design System", value: "Material Design 3 (Expressive)" },
    { label: "Typography", value: "Google Sans Flex (Variable)" },
    { label: "System Navigation", value: "Gesture Bar (default) & 3-Button" },
    { label: "Android Target SDK", value: "Android 16 (API 36)" },
  ],
};
