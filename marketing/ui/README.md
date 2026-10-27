# App Toolkit UI Kit (`@app-toolkit/ui-kit`)

Interactive browser showcase for **App Toolkit for Android**, built with **Vite**, **Lit**, and official **Material Web 3 (`@material/web`)** components.

## Entry Points

* `index.html` (`src/gallery-page.js`) - Showcase Hub with screen cards, category filters, and links to system reference pages.
* `screen.html` (`src/screen-page.js`) - Live interactive device frame (`Phone`, `Fold`, `Desktop`) with palette, contrast, light/dark mode, and per-screen state controls.
* `motion.html` (`src/motion-page.js`) - Material 3 Expressive spatial and effects springs playground and the 35 `MaterialShapes` polygon masks.
* `specs.html` (`src/specs-page.js`) - Extracted App Toolkit color palettes, Google Sans Flex typography scale, and Compose-to-Web component mapping.

## Translated Screens (`src/screens/`)

* `numbers.js` - `NumbersScreen.kt` (warm-gold hero banner, grouped bento corners with 28dp outer and 8dp inner seams, clickable Counter feature card, and interactive `ToolkitToolBottomSheet` modals for Counter, Tip & Bill Splitter, Unit Converter, Random Number Picker, and Dice Roll).
* `quick-tools.js` - `ToolkitTilesScreen.kt` (collapsible Quick Settings tile categories and pocket tools).
* `apps.js` - `AppsListScreen.kt` (developer app catalog with category filter chips and favorites).
* `components.js` - `ComponentsShowcaseScreen.kt` (official Material Web buttons, chips, switches, sliders, and shape badges).

## Commands

```bash
npm install
npm run dev      # sync assets and start Vite dev server
npm run build    # sync assets and build production bundle to dist/
npm run palettes # regenerate src/theme/palettes.css from Kotlin Color.kt files
```
