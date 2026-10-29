# App Toolkit UI Kit (`@app-toolkit/ui-kit`)

Interactive browser showcase for **App Toolkit for Android**, built with **Material Next**, **Vite**, **Lit**, and official **Material Web (`@material/web`)** components.

Material Next owns the gallery, preview controls, screen cards, prototype inspector, comparison views,
window rules, device frames and embed lifecycle. The gallery has no drawer or footer (`navigationMode:
"none"`). Motion and Specifications remain standalone reference URLs.

Material Next also draws the app's shell: its Android preview (`definePreviewApp()`, `previewScreen()`,
`<mnw-android-screen>`) renders the status bar, app bars and search, the navigation each window calls for
(navigation bar and modal drawer, rail, expandable rail, permanent drawer), the system bars and insets, and the
sheets, full-screen dialogs, drawers and overflow menu over the content. Each screen gives its tab, search,
content, FAB and overlay, and nothing else. Which navigation buttons appear comes from Material Next's rules, never
from a screen. App Toolkit keeps its screen content, tools, state controls, palettes, assets and product copy.

## Product identity

`src/config/identity.js` is the one place App Toolkit names itself, as `AppGraph.kt` does with
`toolkitGraph(appTitle, appIcon) { ... }`. Material Next's `defineProductIdentity()` takes:

| Field | App Toolkit | Read by |
| --- | --- | --- |
| `id`, `name` | `app-toolkit`, App Toolkit | App bars, expanded rail, drawer header, hub, saved preview settings |
| `icons` | `app: /logos/icon.svg` (launcher), `logo: /logos/app_logo.svg` (`R.drawable.app_logo`) | Hub; drawer header |
| `fonts` | Google Sans Flex from `fonts/GoogleSansFlex.ttf`; Material Symbols Outlined and Rounded | `--md-ref-typeface-brand` / `-plain`, `loadFonts()` |
| `colors` | The `theme/palettes.css` palettes in picker order, starting at `captured` | Palette pickers, prototype and gallery defaults |
| `destinations` | Quick Tools, Measure (`numbers`), Apps; `toolkitFooter()`; `overflow: [supportUs()]` | The Android preview |
| `links` | Repository, privacy policy, code of conduct | Hub drawer |

Everything else derives from it: `APP` (the shell declaration screens pass to `previewScreen()`),
`PRODUCT_CONFIG` (which adds only what the hub alone shows: motion stories, specs, the release), `PALETTES`,
`loadFonts()`, and `asset()` / `setAssetBase()`, which move the identity's logo and fonts with every other asset.
The assets themselves stay in `public/`.

### Migration notes

From `src/kit/app.js` and the old `PRODUCT_CONFIG` (before Material Next `0.0.9`):

- `import {APP} from "../kit/app.js"` becomes `import {APP} from "../config/identity.js"`; `kit/app.js` is gone.
- `PRODUCT_CONFIG` no longer carries `id`, `name`, `icon`, `logo`, `privacyUrl`, `codeOfConductUrl` or
  `repositoryUrl` of its own: it spreads `IDENTITY.product`. The unused `badges`, `categories`, `tiers`,
  `featuredScreenIds`, `buildNumber`, `headline` and `description` fields were removed, as were the theme defaults
  nothing read (`defaultPalette` is `IDENTITY.defaultPalette`).
- `PALETTES` (`src/controls.js`) is `IDENTITY.palettes`; add a palette to the identity, next to its CSS.
- `--md-ref-typeface-brand` and `--md-ref-typeface-plain` are no longer in `theme/base.css`: the identity sets them
  when `theme/index.js` loads.
- The drawer's mark resolves when the drawer draws, so `setAssetBase()` after import (Remotion) now moves it too.
- The drawer footer and overflow are Material Next's `toolkitFooter()` and `supportUs()`, the library's own entries.

Android Insider ports the same way: its identity declares its name, icons, fonts, palettes and tabs, and
`drawer: {footer: toolkitFooter()}, overflow: [supportUs()]` as its `AppNavigationGraph.kt` does; the App Toolkit
shell, rails, drawers and sheets come with Material Next.

## Dependencies

It uses Material Next `0.1.0` from npm and needs Node.js 24. The gallery opens
each screen in the same page; `screen.html` remains for captures and direct links.
Gallery cards start without device frames. Opening screen detail shows a frame automatically,
while an explicit `frame=none` URL stays frameless.

Shared frames and badges are copied from the installed package into ignored `public/material-next/`
at build time. App-specific fonts, logos and illustrations still come from this repository.

## Entry Points

* `index.html` (`src/gallery-page.js`) - Showcase hub with Quick Tools, Measure, Play & Decide and Apps, without category filters.
* `screen.html` (`src/screen-page.js`) - Live interactive device frame (`Phone`, `Fold`, `Desktop`) with palette, contrast, light/dark mode, and per-screen state controls.
* `motion.html` (`src/motion-page.js`) - Material 3 Expressive spatial and effects springs playground and the 35 `MaterialShapes` polygon masks.
* `specs.html` (`src/specs-page.js`) - Extracted App Toolkit color palettes, Google Sans Flex typography scale, and Compose-to-Web component mapping.

## Embedding and captures

`src/embed.js` keeps `setup()`, `mount()`, `mountPart()`, `IDENTITY`, `SCREENS`, `byId`, `WINDOWS`,
`shellLayout` and `PARTS` (`shapeBadge`, `toolMetaChips`, `toolOverlay`, `icon`) for motion consumers. Its
lifecycle is supplied by `createMarketingEmbed()`. Pass state explicitly for deterministic renders: videos have no
`set()`, so the shell renders static and nothing a screen draws may depend on it.

The drawer, rail and overflow menu are screen state like any other (`drawerOpen`, `railExpanded`, `railOpen`,
`overflowOpen`), so `screen.html?id=quick-tools&state={"drawerOpen":true}` opens the drawer for a capture.

`screen.html?id=numbers&window=fold&theme=dark&frame=device&panel=none` renders only the screen
and frame. `mode=compare&compare=theme|nav|palette|frame` opens a comparison. Palette and theme,
window size, frame, system navigation and screen-specific controls are available in the inspector.

The browser check exercises all four screens in light and dark at phone, fold and desktop sizes,
closed desktop navigation, interactive sheets, comparison and the public embed entry point, and mounts every
screen, preset, sheet and navigation surface the way videos do (without `set()`) to catch anything that needs it. It
checks the web migration; Android screenshot parity remains a separate campaign check.

## Translated Screens (`src/screens/`)

* `quick-tools.js` - `ToolkitTilesScreen.kt` (collapsible Quick Settings tile categories and pocket tools, including Water Eject under Hardware & sound).
* `numbers.js` - Measure: icon-led sections, ruler and protractor previews, live conversion and per-person bill readouts, Counter controls and Reaction Test. Grouped cards keep 28dp outer and 8dp inner corners; each opens its existing Compose-style tool surface.
* `apps.js` - `AppsListScreen.kt` (developer app catalog with category filter chips and favorites).

## Opening the hub

On Windows, double-click `open-marketing-ui.cmd`. On macOS or Linux, run
`sh open-marketing-ui.sh`. Both launchers work from any current directory, refresh dependencies
on each launch (including after a pull), sync assets and open the gallery in the browser.
Node.js and npm must be on PATH. The Windows window stays open on failure so the error remains visible.

## Commands

```bash
npm install
npm run dev      # sync assets and start Vite dev server
npm run build    # sync assets and build production bundle to dist/
npm run qa       # browser checks (CHROMIUM_PATH can select a local Chromium)
npm run palettes # regenerate src/theme/palettes.css from Kotlin Color.kt files
```

Chance sheets preserve signed 64-bit ranges and presets from `RandomNumberTool.kt`, the six-sided die from `DiceRollTool.kt`, and the original coin vector paths from `CoinFlipTool.kt`. Random Number has an approved web redesign: a result-first tonal panel, experimental Material Web segmented presets and paired filled range fields. Browser motion approximates Compose animation; device screenshot parity has not been verified.

In Chance, tapping the die or coin plays its animation in the widget; tapping the card background opens its sheet. Both sheets reserve the same motion area and keep their height while rolling or flipping.

Counter supports inline add, subtract and reset. Tapping the Reaction Test card opens its sheet; its Start button opens the sheet and begins a timed round. Closing the sheet cancels a running round.

## Measure tool parity

Measure tools are translated from `sample/feature/tiles/.../ui/views/tools`: Surface Ruler opens full-screen, with edge graduations, draggable bounds, mm/in units and the 30 mm calibration reference. Protractor has its graduated dial and drag/slider input. Counter uses a primary-container oval and single-step controls. Bill Splitter uses 10/15/20% segments and exact cent rounding. Unit Converter includes all app units, both editable directions, and tonal unit menus. Reaction Test has delayed signals, false starts and five-round history.

The web ruler uses a simulated 160 dpi baseline adjusted by calibration, not a measured physical display DPI; calibrate against a real ruler before measuring. Calibration stays in prototype state. Browser rendering, haptics, digit transitions and device screenshot parity have not been verified against an Android device.

Bill Splitter supports USD, EUR, GBP, RON (lei), JPY and INR. Currency selects the denomination, without exchange conversion, and updates the sheet and overview together. JPY uses whole-yen inputs and rounding; other currencies use two decimal places. Include tip toggles the animated percentage controls and preserves the chosen percentage when disabled.

Measure uses the shared shell search bar to filter tool names and descriptions. Protractor now opens full-screen like Surface Ruler, with the straight edge on the left in portrait and the bottom in landscape, a draggable angle arm and a keyboard-accessible slider. This is an approved web redesign rather than the current Compose sheet arrangement.

Unit Converter pairs two editable value cards with full unit names, category icons and a swap action that preserves the quantity. Protractor adds angle classification, a highlighted sector and 0/45/90/180-degree presets.

Protractor docks its controls beside the dial in landscape and below it in portrait. Converter unit selectors use Material Web select popovers; the swap action uses a momentary button with keyboard focus indication.

Navigation now has Quick Tools, Measure and Play & Decide. The existing Chance tools live under Play & Decide; Discover our apps opens the catalog from the drawer. Water Eject is an explicit-start 165 Hz sine tone with a 30-second limit; Stop, sheet dismissal, leaving the screen or hiding the page stop audio and release its context. The feature is a marketing web prototype, with no water-damage-repair guarantee. Finger chooser and team maker are not implemented.

Decision Wheel accepts 2–12 directly editable choices, one per line. Spinning locks edits until the chosen segment settles; reduced motion shows the result without a prolonged spin. Water Eject uses expanding rings with a reduced-motion fallback and fills its section row.

Quick Tools bounds its projected content to an explicit scroll viewport so tool hit regions cannot extend over the shell navigation. Touch regression checks switch tabs at five scroll offsets.

Decision Wheel no longer has preset chips. SOS uses separate SOS and Morse-message modes with a signal preview, code panel and a prominent stop control. This marketing preview does not control the camera torch.
