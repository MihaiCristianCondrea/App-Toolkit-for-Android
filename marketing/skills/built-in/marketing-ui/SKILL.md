---
name: marketing-ui
description: >
  Build and maintain a marketing UI kit in any app repository: the app's real
  screens rebuilt as Lit web components with Material Web, in marketing/ui, so
  previews, PNG captures and Remotion videos show the app as it is. Covers
  bootstrapping the hub, translating Compose to Material Web, the embed API,
  captures, and the parity check against the app.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-02'
  keywords:
  - marketing-ui
  - material-web
  - lit
  - compose-translation
  - ui-kit
  - playwright
  - remotion-embed
---

# Marketing UI

A marketing hub is a small web project in `marketing/ui` that renders the app's real screens from a state object. Videos, carousels, store graphics and press shots all mount the same screens, so they never show a fake or outdated app. Text in the kit follows [`writing-style`](../writing-style/SKILL.md).

Built on [Material Web](https://github.com/material-components/material-web) (`@material/web`), Google's Material 3 web components, with [Lit](https://lit.dev). Read the Material Web docs for the components and tokens: https://material-web.dev.

## 1. Bootstrap a hub in a new repository

The hub itself (gallery, prototype page, preview settings, device frames, captures, the embed lifecycle) comes from
[Material Next](https://github.com/MihaiCristianCondrea/material-next-web-components) (`material-next-web-components`
on npm). The repository writes only what is the app's: its theme, its screens and their state controls. Do not build a
gallery, a prototype page or device frames by hand.

What is personal to the author stays in the app, never in Material Next: a hub that shows `<mnw-product-launcher>`
passes its own `catalog` (its products, admin consoles and links, built with `createLauncherCatalog()`, spreading
`DEFAULT_LAUNCHER_ITEMS` to keep the public consoles), and a page that wants the Google mark on the phone's back passes
it as the frame's `logo`. The library's defaults name no one's products and carry no logo.

```bash
mkdir -p marketing/ui && cd marketing/ui
npm init -y
npm i lit @material/web material-next-web-components material-symbols
npm i -D vite playwright-core
```

```text
marketing/ui/
├── README.md                # how to open, capture and add a screen
├── open-marketing-ui.cmd    # Windows launcher
├── open-marketing-ui.sh     # macOS and Linux launcher
├── package.json             # scripts: assets, dev, build, qa
├── index.html               # the hub: src/gallery-page.js
├── screen.html              # one screen, state from the URL: src/screen-page.js (captures, direct links)
├── src/
│   ├── gallery-page.js      # mountMarketingHub()
│   ├── screen-page.js       # mountScreenPrototype()
│   ├── config/product.js    # id (namespaces saved preview settings), name, icon
│   ├── controls.js          # the app's palettes for the preview settings
│   ├── theme/               # tokens generated from the app's theme (colors, type, shapes), fonts
│   ├── kit/                 # the app's chrome: status bar, app bars, navigation, sheets; layout.js: adaptive rules
│   ├── screens/             # one module per app screen: { id, title, description, defaults, controls, render(state) }
│   └── embed.js             # createMarketingEmbed(): setup, mount, mountPart for Remotion and captures
└── scripts/
    ├── sync-assets.mjs      # fonts, logos, illustrations into public/; Material Next's frames into public/material-next/
    ├── capture.mjs          # Playwright: screen.html to PNG at any scale and window
    ├── check-ui.mjs         # npm run qa: every screen at phone, fold and desktop, light and dark, no errors
    └── qa-parity.mjs        # npm run parity: side-by-side sheets, app renders vs kit
```

`src/gallery-page.js` is the whole hub: the gallery and each screen's prototype in one page, a card opening its screen at
`?id=<screen>` without a page load.

```js
import {mountMarketingHub} from "material-next-web-components/marketing.js";
import {loadFonts} from "./theme/index.js";
import {SCREENS} from "./screens/index.js";
import {PALETTES} from "./controls.js";
import {PRODUCT_CONFIG} from "./config/product.js";

await loadFonts();
mountMarketingHub({
  product: PRODUCT_CONFIG,
  screens: SCREENS,
  palettes: PALETTES,
  navigationMode: "none", // no drawer or menu button while the hub has one destination
  adsToggle: true,        // a Show ads switch on the prototype, off by default
});
```

`src/screen-page.js` mounts `mountScreenPrototype()` with the same options, so `screen.html?id=<screen>&panel=none` stays
a bare screen for captures. `src/embed.js` wraps `createMarketingEmbed({screens, parts, loadTheme})` and exports its
`setup`, `mount` and `mountPart`.

`package.json` scripts:

```json
{
  "scripts": {
    "assets": "node scripts/sync-assets.mjs",
    "dev": "npm run assets && vite",
    "build": "npm run assets && vite build",
    "capture": "node scripts/capture.mjs",
    "qa": "node scripts/check-ui.mjs",
    "parity": "node scripts/qa-parity.mjs"
  }
}
```

The launchers refresh dependencies on every start, so a pull that changes them is picked up, and stop on errors:

```bat
@echo off
rem open-marketing-ui.cmd
cd /d "%~dp0"
where npm >nul 2>nul || (echo Node.js was not found. Install it from https://nodejs.org. & pause & exit /b 1)
call npm install --no-fund || (pause & exit /b 1)
call npm run dev -- --open || (pause & exit /b 1)
```

```sh
#!/usr/bin/env sh
# open-marketing-ui.sh
set -eu
cd "$(dirname "$0")"
command -v npm >/dev/null 2>&1 || { echo "Node.js and npm must be on PATH." >&2; exit 1; }
npm install --no-fund
exec npm run dev -- --open
```

App Toolkit's `marketing/ui` is the reference hub for `check-ui.mjs`, Smart Cleaner's for `capture.mjs` and Android
Insider's for `qa-parity.mjs`; copy them and adjust the screen list.

## 2. Translate Compose to Material Web

Translate from the app's source, not from screenshots. For each screen, open its Composable and map it piece by piece.

| Compose | Material Web or kit |
| --- | --- |
| `MaterialTheme.colorScheme` roles | `--md-sys-color-*` custom properties generated from the app's color scheme |
| `MaterialTheme.typography` styles | `--md-sys-typescale-*` with the app's fonts and sizes |
| `MaterialTheme.shapes` | `--md-sys-shape-corner-*` |
| `Button`, `FilledTonalButton`, `OutlinedButton`, `TextButton` | `md-filled-button`, `md-filled-tonal-button`, `md-outlined-button`, `md-text-button` |
| `IconButton`, `FilledIconButton` | `md-icon-button`, `md-filled-icon-button` |
| `FloatingActionButton` | `md-fab` |
| `Card`, `ElevatedCard`, `OutlinedCard` | a `div` with the card's surface role, shape and `md-elevation` |
| `FilterChip`, `AssistChip`, `SuggestionChip` | `md-filter-chip`, `md-assist-chip`, `md-suggestion-chip` in `md-chip-set` |
| `OutlinedTextField`, `TextField` | `md-outlined-text-field`, `md-filled-text-field` |
| `NavigationBar`, `NavigationRail` | kit shell components (Material Web has no navigation bar yet) |
| `TopAppBar` variants | kit app bar: 64 dp, `title-large`, 48 dp action slots |
| `ListItem` | `md-list` and `md-list-item` |
| `Switch`, `Checkbox`, `RadioButton`, `Slider` | `md-switch`, `md-checkbox`, `md-radio`, `md-slider` |
| `CircularProgressIndicator`, `LinearProgressIndicator` | `md-circular-progress`, `md-linear-progress` |
| `Icon(Icons.*)` | Material Symbols, self-hosted, the same style and fill as the app |
| `LazyColumn` and `Arrangement`, `Modifier.padding` | flex or grid with the same order, alignment and dp values |

Rules:

- **1 dp = 1 CSS px.** Lay a phone screen out at the device's width in dp (for example 412 x 915 for a typical Pixel); the device frame decides the final size.
- **Copy the arrangement, not just the parts.** `Arrangement.End`, weights and paddings decide where a button sits; check each row against the Composable.
- **Real content and real states.** Load the app's real strings and sample data. Show only states the app can show (an empty card that the app would hide must be hidden here too).
- **Edge to edge.** Status bar 24 dp and gesture bar over the content, as Android draws them.
- **Every screen renders from state.** No timers or random values inside a screen; videos set the state per frame.
- **Large screens come from the Android code, not from guesses.** Tablets, foldables and desktop windows take the app's own adaptive rules, so port them, constant for constant, into one module (`kit/layout.js`) with a comment naming each source file: the window size breakpoints and navigation mode (bottom bar, rail, expanded rail, permanent drawer; App Toolkit apps: `ShellLayoutPolicy` in `ShellLayout.kt`, widths in `NavigationSurfaces.kt`), list and detail panes (`ListDetailScene.kt`: list fraction, separator), and the feed grid (the app's `listingLayout()`-style function: minimum cell width, column cap, when a banner stacks). Screens read the layout from a `window` in their state (`{ width, height, system }`, with presets such as `phone`, `fold` and `desktop`); `mount()`, the host and the capture script pass it. Also port how the wide shell is painted, not only where things go (for App Toolkit: the navigation and app bar share surface-container, and a tab's content is a surface card with a 24 dp top-start corner). When the app's library is a dependency, read its source at the exact version the app uses (clone the library at that tag, or disassemble the cached AAR with `javap -c` for the constants) before writing CSS.
- **Device frames per window.** For phone windows, Material Next's 3D Pixel 9 Pro XL (`<mnw-device-frame>`, 465.23 x 988.76 around the 430 x 957 window; `finish`, `yaw` and `pitch` pick its colour and angle); for a tablet window the Pixel 9 Pro Fold open (Material Next's `device-frames/pixel_9_pro_fold`, copied with `npx mnw-marketing-assets`; its `layout` stays in `assets/device-frames/pixel_9_pro_fold/default`: a 2204 x 2274 frame around a 2076 x 2152 display at 62, 62, which is 852 x 883 dp at 2.4375). Size the frame so its display is exactly the window in CSS px, measure the display's corner radius from `mask.webp`, and keep the status icons clear of the camera cutout. For a desktop window, Google's device art for the Pixelbook Go (from the Android Developers Device Art Generator, `device-art-resources/pixelbook_go/land_back.png`, `land_shadow.png`, `land_fore.png`): find the screen as the transparent rectangle in `land_back.png` (1826 x 1079 px at 181, 159), and size the desktop window to it (1218 x 720 dp at 1.5). A desktop window has no system bars.
- **Check every window.** After a change, capture each screen at every window (`npm run capture -- <id> --window=fold`) and look: wide layouts break differently (a flex row without `min-width: 0` lets a pane overflow; content capped at 800 dp needs centring; long lists need an end).
- **The served layout, not a guess at it.** Snapshot what the API serves for each screen, not only the items: for a composed feed, every group (featured, curated, topics, latest) with each article's style in its group and its ad positions. The kit draws that composition the way the app does, styles on every group the contract allows. When the kit, the app and an admin preview disagree, the contract decides; fix all three, then refresh the snapshot.
- **One look for every mock.** Where a backend console previews the app (a feed preview, an ad stand-in), it draws the kit's components with the same structure and copy, so the console, the kit and the app agree. Change the kit first, then the console.
- **Ads off by default.** Screens take `showAds` (off); the gallery has a Show ads switch to check ad layouts. Marketing media (store screenshots, posts, videos) never shows ad UI.
- **Expose parts.** When a film needs one piece of a screen, factor it into a function the screen also calls, add it to `PARTS` in `embed.js`, and check that the screen still renders identically.

## 3. Use it

- **Gallery:** `marketing/ui/open-marketing-ui.cmd` (Windows) or `./marketing/ui/open-marketing-ui.sh`.
- **Checks:** `npm run qa` after any change to a screen or the kit.
- **Captures:** `npm run capture -- <screen-id> --scale=3` writes PNGs for store graphics and posts; `--window=fold|desktop` renders the large-screen layout and `--frame=device` the window's device. Run [`metadata`](../metadata/SKILL.md) on them before they leave the repository.
- **Videos:** Remotion mounts screens and parts through `embed.js` (a `LitScreen` component that calls `mount()` with a state derived from the frame). See [`motion-video`](../motion-video/SKILL.md).

```tsx
<LitScreen id="article" state={{ scroll: interpolate(frame, [0, 60], [0, 480]) }} />
```

When a campaign needs a screen the kit doesn't have, add it to the kit first, so the next campaign gets it too.

## 4. Parity check (before any campaign uses a screen)

The kit is only useful if it matches the app.

1. Render the app's screens: Compose previews, Robolectric or Paparazzi screenshot tests, or device screenshots.
2. `npm run parity` writes one sheet per screen, app on the left, kit on the right, at the same width.
3. Fix every difference in the kit: arrangement, type role, colour role, icon, spacing, content. Note what can't match (for example browser text rendering a few percent wider) in `marketing/ui/README.md`.

A screen that fails parity is not used in a campaign until it is fixed.

## 3D device

The phone is our own Pixel 9 Pro XL, built in three.js from Google's published measurements and
product photos, shipped by Material Next (0.2.0 and later). No third-party model or file is needed.

1. **A phone with a live screen: `<mnw-device-frame>`.** It draws the phone and lays the real screen
   over its display, head on or turned (`yaw`, `pitch`), in any finish (`obsidian`, `porcelain`,
   `hazel`, `rose-quartz`). Head on, the screen is not transformed, so screenshots stay sharp. Every
   frame on a page shares one WebGL context and one picture per pose; without WebGL it draws the
   phone's outline in CSS, so a page is always complete. Await `frame.updateComplete` before a capture.
2. **The back mark is the host's.** The model carries no logo. Pass `logo` (the URL of a
   white-on-transparent mask) only on your own pages; the mark is drawn polished, in the back's colour.
3. **A phone that turns on scroll: still the frame.** Set the frame's `yaw` and `pitch` from scroll
   progress. While the pose keeps changing, the frame draws straight into a canvas once per animation
   frame at one device pixel per CSS pixel, with no picture encoded, and swaps in the sharp picture
   280 ms after it settles; off screen it draws nothing. Move one phone at a time; still phones cost
   nothing after their first picture.
4. **A scene of your own: the model itself.** For a film or a scene with more than a phone, import
   `createPixelModel()`, `createStudio()` and `disposePixelModel()` from
   `material-next-web-components/marketing/ui/pixel-model.js` and render it in your own three.js
   scene, with the light `createStudio()` gives as the environment. The model is in millimetres,
   display towards +Z. Lay the live screen over it with a `CSS3DRenderer` layer drawn with the same
   camera: place a frameless `ScreenHost` (430 x 957) on the display plane, 0.1 mm above the glass,
   inset 2.9 mm from the sides and 2.6 mm from the top and bottom, its corners 8.2 mm round
   (`PIXEL_9_PRO_XL` has every number), with `backface-visibility: hidden`.
5. **Performance in your own scene.** Import three.js on demand; render only when the pose or the
   size changes, never in an idle loop; cap the pixel ratio at 2 and lower it to 1 while the phone
   moves; stop while the canvas is off screen; keep one WebGL context per page. With reduced motion
   or without WebGL show `<mnw-device-frame>` head on.
6. **Own the lifecycle.** `disposePixelModel(phone)` frees every geometry, material and texture the
   model made (the logo texture stays yours); dispose the renderer and the environment with it.
7. **Motion.** Turn the phone in as it arrives (from edge-on through three quarters to the front,
   with a tilt that settles), driven by scroll progress so it reverses cleanly.

### Scroll-built pages

A presentation page built from the kit follows the Pixel film rules translated to the web:

- Scroll is the timeline. Give each scene a tall section with a sticky stage and publish its progress
  (0 to 1) as a CSS variable; every transform is a function of it, so scrolling back plays it in reverse.
- Drive the real screens from that progress (a scan's `progress`, a swipe card's `drag`), never a
  recording of them.
- Do not use the kit's `embed.js` `setup()` on a live page: it marks the document `data-motion="frame"`
  and freezes every CSS animation, which is right for video and wrong for the web. Host screens with
  Material Next's `ScreenHost`.
- The kit's `scrollArea` cancels the wheel over a screen. Hand the wheel back to the page when the
  screen did not move, or a phone traps page scroll.
- With reduced motion, show every scene at its final state in normal flow.

