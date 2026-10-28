/**
 * Product Showcase Configuration for App Toolkit for Android.
 */
export const PRODUCT_CONFIG = {
  id: "app-toolkit",
  name: "App Toolkit",
  headline: "Product UI showcase",
  description:
    "Browse and preview App Toolkit screens (Quick Tools, Measure, and Apps) with official Material Web 3 components.",
  buildNumber: "300000",
  toolkitVersion: "3.0.0",
  logo: "/logos/app_logo.svg",
  icon: "/logos/icon.svg",
  repositoryUrl: "https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android",
  privacyUrl: "https://mihaicristiancondrea.github.io/profile/#privacy-policy",
  codeOfConductUrl: "https://mihaicristiancondrea.github.io/profile/#code-of-conduct",

  badges: [
    { label: "Material 3 Expressive", icon: "palette", desc: "Expressive M3 tokens, shapes & dynamic schemes" },
    { label: "Compose Parity", icon: "code", desc: "1:1 parity with Kotlin Jetpack Compose" },
    { label: "Quick Settings Tiles", icon: "widgets", desc: "Hardware sensors, utilities & tile shortcuts" },
    { label: "Numbers Bento", icon: "numbers", desc: "Warm-gold bento grid for counting, splitting & converting" },
  ],

  categories: [
    { id: "all", label: "All Screens", icon: "apps" },
    { id: "numbers", label: "Numbers", icon: "numbers" },
    { id: "tools", label: "Quick Tools", icon: "widgets" },
    { id: "catalog", label: "Apps Catalog", icon: "storefront" },
  ],

  tiers: [
    { id: 0, label: "All Tiers", desc: "Full catalog" },
    { id: 1, label: "Tier 1: Lead", desc: "Primary flagship destinations" },
    { id: 2, label: "Tier 2: Supporting", desc: "Component & design system surfaces" },
  ],

  featuredScreenIds: ["quick-tools", "numbers", "apps"],

  theme: {
    defaultPalette: "captured",
    defaultTheme: "system",
    defaultNavType: "gesture",
    defaultMode: "showcase",
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
