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
        id: "tools-to-measure",
        title: "From Live Sensors to Everyday Measuring",
        subtitle: "Quick Tools -> Measure",
        description:
          "The compass and level read live in Quick Tools, then Measure splits a restaurant bill and counts up on the tally counter.",
        screens: ["quick-tools", "numbers"],
        accent: "#FFCA28",
      },
      {
        id: "measure-to-play",
        title: "Measure, then Decide",
        subtitle: "Measure -> Play & Decide",
        description:
          "From the protractor and unit converter to the Decision Wheel, a coin flip and a dice roll.",
        screens: ["numbers", "play"],
        accent: "#F6A800",
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
