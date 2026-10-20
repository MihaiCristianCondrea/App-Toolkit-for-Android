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
  last-updated: '2026-10-16'
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

```bash
mkdir -p marketing/ui && cd marketing/ui
npm init -y
npm i lit @material/web
npm i -D vite playwright
```

Then create:

```text
marketing/ui/
├── README.md                # how to open, capture and add a screen
├── open-marketing-ui.cmd    # Windows launcher
├── open-marketing-ui.sh     # macOS and Linux launcher
├── package.json             # scripts: dev, build, capture, qa
├── index.html               # the gallery: every screen with a state panel
├── screen.html              # one screen, full size, state from the URL
├── src/
│   ├── theme/               # tokens generated from the app's theme (colors, type, shapes)
│   ├── kit/                 # shell: status bar, app bars, navigation (bar, rail, drawer), panes, device frames; layout.js: windows and adaptive rules
│   ├── screens/             # one Lit element per app screen
│   ├── parts/               # pieces of screens a film can mount alone (a card, a row, a button)
│   └── embed.js             # mount(el, id, state, options) and mountPart(el, id, state)
└── scripts/
    ├── capture.mjs          # Playwright: screens to PNG at any scale
    └── qa-parity.mjs        # side-by-side sheets: app renders vs kit
```

`package.json` scripts:

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "capture": "node scripts/capture.mjs",
    "qa": "node scripts/qa-parity.mjs"
  }
}
```

Launchers:

```bat
@echo off
rem open-marketing-ui.cmd
cd /d "%~dp0"
if not exist node_modules call npm install
call npm run dev -- --open
```

```sh
#!/usr/bin/env sh
# open-marketing-ui.sh
cd "$(dirname "$0")" || exit 1
[ -d node_modules ] || npm install
npm run dev -- --open
```

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
- **Device frames per window.** The phone frame for phone windows; for a tablet window the Pixel 9 Pro Fold open (`assets/device-frames/pixel_9_pro_fold/default`: a 2204 x 2274 frame around a 2076 x 2152 display at 62, 62, which is 852 x 883 dp at 2.4375). Size the frame so its display is exactly the window in CSS px, measure the display's corner radius from `mask.webp`, and keep the status icons clear of the camera cutout. For a desktop window, Google's device art for the Pixelbook Go (from the Android Developers Device Art Generator, `device-art-resources/pixelbook_go/land_back.png`, `land_shadow.png`, `land_fore.png`): find the screen as the transparent rectangle in `land_back.png` (1826 x 1079 px at 181, 159), and size the desktop window to it (1218 x 720 dp at 1.5). A desktop window has no system bars.
- **Check every window.** After a change, capture each screen at every window (`npm run capture -- <id> --window=fold`) and look: wide layouts break differently (a flex row without `min-width: 0` lets a pane overflow; content capped at 800 dp needs centring; long lists need an end).
- **The served layout, not a guess at it.** Snapshot what the API serves for each screen, not only the items: for a composed feed, every group (featured, curated, topics, latest) with each article's style in its group and its ad positions. The kit draws that composition the way the app does, styles on every group the contract allows. When the kit, the app and an admin preview disagree, the contract decides; fix all three, then refresh the snapshot.
- **One look for every mock.** Where a backend console previews the app (a feed preview, an ad stand-in), it draws the kit's components with the same structure and copy, so the console, the kit and the app agree. Change the kit first, then the console.
- **Ads off by default.** Screens take `showAds` (off); the gallery has a Show ads switch to check ad layouts. Marketing media (store screenshots, posts, videos) never shows ad UI.
- **Expose parts.** When a film needs one piece of a screen, factor it into a function the screen also calls, register it in `parts/`, and check that the screen still renders identically.

## 3. Use it

- **Gallery:** `marketing/ui/open-marketing-ui.cmd` (Windows) or `./marketing/ui/open-marketing-ui.sh`.
- **Captures:** `npm run capture -- <screen-id> --scale=3` writes PNGs for store graphics and posts; `--window=fold|desktop` renders the large-screen layout and `--frame=device` the window's device. Run [`metadata`](../metadata/SKILL.md) on them before they leave the repository.
- **Videos:** Remotion mounts screens and parts through `embed.js` (a `LitScreen` component that calls `mount()` with a state derived from the frame). See [`motion-video`](../motion-video/SKILL.md).

```tsx
<LitScreen id="article" state={{ scroll: interpolate(frame, [0, 60], [0, 480]) }} />
```

When a campaign needs a screen the kit doesn't have, add it to the kit first, so the next campaign gets it too.

## 4. Parity check (before any campaign uses a screen)

The kit is only useful if it matches the app.

1. Render the app's screens: Compose previews, Robolectric or Paparazzi screenshot tests, or device screenshots.
2. `npm run qa` writes one sheet per screen, app on the left, kit on the right, at the same width.
3. Fix every difference in the kit: arrangement, type role, colour role, icon, spacing, content. Note what can't match (for example browser text rendering a few percent wider) in `marketing/ui/README.md`.

A screen that fails parity is not used in a campaign until it is fixed.
