# App Toolkit UI Kit (`@app-toolkit/ui-kit`)

Interactive browser showcase for **App Toolkit for Android**, built with **Material Next**, **Vite**, **Lit**, and official **Material Web (`@material/web`)** components.

Material Next owns the gallery, preview controls, screen cards, prototype inspector, comparison views,
window rules, device frames and embed lifecycle. The desktop drawer starts closed (`navigationMode:
"modal"`). Open its menu to reach Screens. Motion and Specifications remain standalone reference URLs. App Toolkit keeps its three
screen renderers, state controls, Android app chrome, palettes and product content.

The library is pinned to source commit `a4c1bbd` pending its 0.0.4 npm release.
Installation needs Git and Node.js 24; npm builds the package from source.
Gallery cards start without device frames. Opening screen detail shows a frame automatically,
while an explicit `frame=none` URL stays frameless.

Shared frames and badges are copied from the installed package into ignored `public/material-next/`
at build time. App-specific fonts, logos and illustrations still come from this repository.

## Entry Points

* `index.html` (`src/gallery-page.js`) - Showcase hub with Quick Tools, Numbers and Apps, without category filters.
* `screen.html` (`src/screen-page.js`) - Live interactive device frame (`Phone`, `Fold`, `Desktop`) with palette, contrast, light/dark mode, and per-screen state controls.
* `motion.html` (`src/motion-page.js`) - Material 3 Expressive spatial and effects springs playground and the 35 `MaterialShapes` polygon masks.
* `specs.html` (`src/specs-page.js`) - Extracted App Toolkit color palettes, Google Sans Flex typography scale, and Compose-to-Web component mapping.

## Embedding and captures

`src/embed.js` keeps `setup()`, `mount()`, `mountPart()`, `SCREENS`, `byId`, `WINDOWS`,
`shellLayout` and `PARTS` for motion consumers. Its lifecycle is supplied by
`createMarketingEmbed()`. Pass state explicitly for deterministic renders.

`screen.html?id=numbers&window=fold&theme=dark&frame=device&panel=none` renders only the screen
and frame. `mode=compare&compare=theme|nav|palette|frame` opens a comparison. Palette and theme,
window size, frame, system navigation and screen-specific controls are available in the inspector.

The browser check exercises all three screens in light and dark at phone, fold and desktop sizes,
closed desktop navigation, interactive sheets, comparison and the public embed entry point. It
checks the web migration; Android screenshot parity remains a separate campaign check.

## Translated Screens (`src/screens/`)

* `quick-tools.js` - `ToolkitTilesScreen.kt` (collapsible Quick Settings tile categories and pocket tools).
* `numbers.js` - `NumbersScreen.kt` (warm-gold hero banner, grouped bento corners with 28dp outer and 8dp inner seams, clickable Counter feature card, and interactive `ToolkitToolBottomSheet` modals for Counter, Tip & Bill Splitter, Unit Converter, Random Number Picker, and Dice Roll).
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
