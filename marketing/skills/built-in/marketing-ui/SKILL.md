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
│   ├── kit/                 # shell: status bar, app bars, navigation, device frame
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
- **Expose parts.** When a film needs one piece of a screen, factor it into a function the screen also calls, register it in `parts/`, and check that the screen still renders identically.

## 3. Use it

- **Gallery:** `marketing/ui/open-marketing-ui.cmd` (Windows) or `./marketing/ui/open-marketing-ui.sh`.
- **Captures:** `npm run capture -- <screen-id> --scale=3` writes PNGs for store graphics and posts. Run [`metadata`](../metadata/SKILL.md) on them before they leave the repository.
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
