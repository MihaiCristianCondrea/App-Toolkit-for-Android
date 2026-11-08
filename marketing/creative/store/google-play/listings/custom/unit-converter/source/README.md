# Unit Converter listing source

Reproducible English creative for the unit-converter custom listing. Output is six 2160 x 3840 phone PNGs, four 3840 x 2160 foldable PNGs, four 3840 x 2160 desktop PNGs, a 1024 x 500 feature graphic and the existing 512 x 512 icon. Upload files are in ../listing/en-US; review sheets and reports are in ../review.

## Render and verify

Run these commands from this directory with Node.js, npm and Python with Pillow installed. On Windows use npm.cmd if PowerShell blocks npm.ps1.

```sh
npm ci
npm run lint
npm run probe -- length temperature mass volume data reverse length@fold temperature@fold mass@fold data@fold length@desktop temperature@desktop mass@desktop data@desktop
npm run solve
npm run concepts
npm run verify -- en-US
npm run render -- en-US --verify-repeat
npm run metadata
npm run audit
npm run archive
node scripts/placement.ts
```

Run node scripts/validate.mjs from marketing/creative/store/google-play. The UI kit dependencies and its public assets must already be installed and synced. The checked installed Material-Next Web Components version is 0.2.0.

Rendering stages every output before replacing this listing's graphics. The icon is copied, never rendered or redrawn. Metadata tagging runs after rendering, checks readback and verifies unchanged decoded pixels. The audit requires all 16 outputs, 14 alt-text rows and six exact repeat captures.

## Composition and fidelity

The production pipeline is adapted from Decision Wheel and Water Eject. It remains local so existing approved listings and their assets do not change. No dependency or shared rendering framework was added.

- src/screens.ts declares six real converter states and their expected values, checked from UnitConversionRepository's decimal factors.
- LitScreen.tsx mounts the authoritative Marketing UI. It scrolls the real category strip to reveal its selection and the underlying Measure content towards Convert & split. After scrolling it finishes finite shadow-root animations at their final state, including the app bar's 0.2-second tint transition. UI structure, colours and behaviour are unchanged.
- scripts/bundle.mjs inlines the installed local symbol fonts. This removes the interrupted WOFF2 HTTP requests seen in the initial render, while the capture still requires every font to be loaded.
- composition/geometry.ts projects the actual Pixel camera pose and measures silhouette, centroid and critical UI. uiSelectors.ts and the probe record title, category, both fields, swap, hint and the Data note.
- composition/shots.ts provides gentle camera directions and separate caption/focus targets. The solver enforces device containment and critical-UI visibility before scoring framing and golden guides.
- composition/compose.ts places the paired MaterialShapes and arrow paths outside the projected phone. Glass.tsx draws the continuous connecting curves, with caption exclusion masks.
- scripts/verify.mjs checks geometry and composited pixels and writes clean/debug contact sheets for all three device sets.
- FeatureGraphic.tsx contains three compositions: Material transformation, Connected measurements and Editorial utility. The middle concept is selected after full-size and thumbnail review.
- scripts/render.mjs records actual displayed conversions, loaded fonts and browser resource failures in capture-checks.json. The capture fails if values or selected units differ from the expected example.

The real UI supplies all values. The mass example is exactly 2.26796185 kg = 5 lb. Data uses powers of 1000, not binary units. The last shot edits the destination value after swapping the units, showing the same supported category in reverse.

## Validation

The review reports separate hard constraints, visual preferences and intentional behind-phone shapes. Whole devices and critical UI stay inside their safe areas. Captions use the project's two-line Material pill treatment and stay below 20% of the image. Shapes and arrows keep clear of caption text; effects never cover app UI.

Final checks passed for all 16 assets and 14 screenshot layouts. Caption contrast is at least 10.59:1 under 99% of glyphs, pill text is 6.62:1, and caption-zone interference is 0.00%. All six phone source PNGs repeat byte-identically. All 34 captured screen instances report the required fonts loaded, with no browser resource errors. The official icon is pixel-identical and metadata readback preserves every decoded pixel.

One soft target remains intentionally off the golden guide in the temperature composition. Keeping the whole device and readable fields takes priority. No hard rule is relaxed.

The final render checks exact repeated source PNGs for all six phone shots. The package audit checks RGB screenshots and feature graphic, the RGBA official icon, dimensions, copy limits, keyword uniqueness, alt text, fonts and resource failures. Embedded metadata is read back and must preserve every decoded pixel.

## Publication gate

Status stays planned. Confirm production availability of the redesigned app (26.11.02) and review the candidate keywords in Play Console before upload. Public listing content did not establish the rollout. No connected-device visual comparison was performed.

## Artwork ownership

Backgrounds use actual MaterialShape masks from the existing kit and original deterministic SVG curves and arrows. No third-party textures, models or competitor artwork are included. Existing app screens and the official icon retain their identity.
