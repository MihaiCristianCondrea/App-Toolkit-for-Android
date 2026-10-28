# App Toolkit UI Kit (`@app-toolkit/ui-kit`)

Interactive browser showcase for **App Toolkit for Android**, built with **Material Next**, **Vite**, **Lit**, and official **Material Web (`@material/web`)** components.

Material Next owns the gallery, preview controls, screen cards, prototype inspector, comparison views,
window rules, device frames and embed lifecycle. The gallery has no drawer or footer (`navigationMode:
"none"`). Motion and Specifications remain standalone reference URLs.

Material Next also draws the app's shell: its Android preview (`definePreviewApp()`, `previewScreen()`,
`<mnw-android-screen>`) renders the status bar, app bars and search, the navigation each window calls for
(navigation bar and modal drawer, rail, expandable rail, permanent drawer), the system bars and insets, and the
sheets, full-screen dialogs, drawers and overflow menu over the content. App Toolkit declares itself once in
`src/kit/app.js` (tabs, drawer footer, overflow); each screen gives its tab, search, content, FAB and overlay, and
nothing else. Which navigation buttons appear comes from Material Next's rules, never from a screen. App Toolkit
keeps its screen content, tools, state controls, palettes and product copy.

It uses Material Next from its Git source (`github:…#9627d8e`) until the next npm release carries the Android
preview, so installing needs Git as well as Node.js 24; the package builds itself on install. The gallery opens
each screen in the same page; `screen.html` remains for captures and direct links.
Gallery cards start without device frames. Opening screen detail shows a frame automatically,
while an explicit `frame=none` URL stays frameless.

Shared frames and badges are copied from the installed package into ignored `public/material-next/`
at build time. App-specific fonts, logos and illustrations still come from this repository.

## Entry Points

* `index.html` (`src/gallery-page.js`) - Showcase hub with Quick Tools, Measure and Apps, without category filters.
* `screen.html` (`src/screen-page.js`) - Live interactive device frame (`Phone`, `Fold`, `Desktop`) with palette, contrast, light/dark mode, and per-screen state controls.
* `motion.html` (`src/motion-page.js`) - Material 3 Expressive spatial and effects springs playground and the 35 `MaterialShapes` polygon masks.
* `specs.html` (`src/specs-page.js`) - Extracted App Toolkit color palettes, Google Sans Flex typography scale, and Compose-to-Web component mapping.

## Embedding and captures

`src/embed.js` keeps `setup()`, `mount()`, `mountPart()`, `SCREENS`, `byId`, `WINDOWS`,
`shellLayout` and `PARTS` (`shapeBadge`, `toolMetaChips`, `toolOverlay`, `icon`) for motion consumers. Its
lifecycle is supplied by `createMarketingEmbed()`. Pass state explicitly for deterministic renders: videos have no
`set()`, so the shell renders static and nothing a screen draws may depend on it.

The drawer, rail and overflow menu are screen state like any other (`drawerOpen`, `railExpanded`, `railOpen`,
`overflowOpen`), so `screen.html?id=quick-tools&state={"drawerOpen":true}` opens the drawer for a capture.

`screen.html?id=numbers&window=fold&theme=dark&frame=device&panel=none` renders only the screen
and frame. `mode=compare&compare=theme|nav|palette|frame` opens a comparison. Palette and theme,
window size, frame, system navigation and screen-specific controls are available in the inspector.

The browser check exercises all three screens in light and dark at phone, fold and desktop sizes,
closed desktop navigation, interactive sheets, comparison and the public embed entry point, and mounts every
screen, preset, sheet and navigation surface the way videos do (without `set()`) to catch anything that needs it. It
checks the web migration; Android screenshot parity remains a separate campaign check.

## Translated Screens (`src/screens/`)

* `quick-tools.js` - `ToolkitTilesScreen.kt` (collapsible Quick Settings tile categories and pocket tools, including Chance: Random Number, Dice Roll and Coin Flip).
* `numbers.js` - `NumbersScreen.kt` (warm-gold hero banner, grouped bento corners with 28dp outer and 8dp inner seams, clickable Counter feature card, and interactive `ToolkitToolBottomSheet` modals for Counter, Tip & Bill Splitter, Unit Converter).
* `apps.js` - `AppsListScreen.kt` (developer app catalog with category filter chips and favorites).

## Opening the hub

On Windows, double-click `open-marketing-ui.cmd`. On macOS or Linux, run
`sh open-marketing-ui.sh`. Both launchers work from any current directory, refresh dependencies
on each launch (including after a pull), sync assets and open the gallery in the browser.
Node.js, npm and Git must be on PATH. The Windows window stays open on failure so the error remains visible.

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

## Measure tool parity

Measure tools are translated from `sample/feature/tiles/.../ui/views/tools`: Surface Ruler opens full-screen, with edge graduations, draggable bounds, mm/in units and the 30 mm calibration reference. Protractor has its graduated dial and drag/slider input. Counter uses a primary-container oval and single-step controls. Bill Splitter uses 10/15/20% segments and exact cent rounding. Unit Converter includes all app units, both editable directions, and tonal unit menus. Reaction Test has delayed signals, false starts and five-round history.

The web ruler uses a simulated 160 dpi baseline adjusted by calibration, not a measured physical display DPI; calibrate against a real ruler before measuring. Calibration stays in prototype state. Browser rendering, haptics, digit transitions and device screenshot parity have not been verified against an Android device.
