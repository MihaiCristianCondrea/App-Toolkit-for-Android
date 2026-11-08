# Unit Converter listing source

Four 2160 x 3840 phone screenshots, four 3840 x 2160 foldable screenshots, four 3840 x 2160 desktop screenshots, a 1024 x 500 feature graphic and the unchanged 512 x 512 official icon. Upload files are in ../listing/en-US; current review sheets and reports are in ../review.

## Render and verify

The existing Marketing UI dependencies and public assets must be installed and synced. Material-Next Web Components 0.2.0 supplies the real 3D Pixel. Use npm.cmd on Windows when PowerShell blocks npm.ps1.

```sh
npm ci
npm run lint
npm run probe -- length temperature mass volume data reverse toolkit length@fold temperature@fold mass@fold data@fold length@desktop temperature@desktop mass@desktop data@desktop
npm run solve
node scripts/preview-campaign.mjs
npm run verify -- en-US
npm run render -- en-US --verify-repeat
npm run metadata
npm run audit
npm run archive
node scripts/placement.ts
```

For a phone-only refresh, use npm run render -- en-US --phone-only --verify-repeat, followed by npm run metadata -- --phone-only. Staging copies the existing wide graphics, feature graphic and icon unchanged, replaces only the staged phone set, and publishes the folder only after every new image succeeds. Metadata readback checks all 14 assets. Run node scripts/validate.mjs from marketing/creative/store/google-play.

## Campaign and geometry

The four compositions serve different roles: a close converter hero, a two-device mass/volume comparison, grouped temperature/data UI details, and the broader Measure workspace. The earlier six-poster pattern is removed.

- geometry.ts uses the actual model projection to measure silhouettes, centroids and critical UI.
- shots.ts searches the two-phone scene jointly. Of 972 candidates, 846 fail containment, minimum scale, card width, caption clearance or silhouette spacing. The chosen pair has a 30.998 px gap, zero overlap and 239.998 px stagger. Its projected area-weighted horizontal centre is the canvas centre. Result areas use the lower golden guides as preferences, never at the expense of readability.
- compose.ts validates phone and native-detail geometry, including label regions and all measured fields. The hero's crop is intentional at the top canvas boundary; the other complete devices stay inside their image.
- NativePanel.tsx crops the actual mounted Marketing UI after it settles. It measures the source From/To region, checks it against the probe, and translates the real screen into the crop. It never reconstructs controls or results.
- LitScreen.tsx verifies each converter's actual values and unit selectors. The toolkit capture requires the real ruler, protractor, converter and bill-splitter cards. Finite shadow-root transitions are finished before capture.
- scripts/bundle.mjs inlines the installed symbol fonts to avoid interrupted WOFF2 HTTP requests. Every capture requires Google Sans Flex and both Material Symbol fonts to be loaded.
- The verifier renders clean, caption-free and debug images and checks geometry, caption contrast, interference and pixel-weighted balance. placement.json records every projected control, native UI crop, silhouette and pair measurement.

Length shows 1 m = 100 cm, mass 2.26796185 kg = 5 lb, volume 1 L = 1000 mL, temperature 20 Celsius = 68 Fahrenheit, and data 1 GB = 1000 MB. The mass pair is exact under UnitConversionRepository's decimal factors. The Data note remains in the native detail.

The feature graphic, icon and large-screen upload assets are preserved. Earlier hero-study images remain as design history; contact-clean.png shows the current campaign.

## Checks and publication gate

The audit requires four exact repeated phone captures, 12 screenshot alt-text rows, 14 correctly sized graphics, a pixel-identical official icon, loaded fonts, no browser resource errors and successful metadata readback with unchanged decoded pixels. Review reports separate hard failures, preferences and intentional crops.

Production availability of the redesigned 26.11.02 app and candidate keyword availability still require Play Console confirmation. No connected-device comparison or publication is claimed. Android code and other custom listings are unchanged by this redesign.
