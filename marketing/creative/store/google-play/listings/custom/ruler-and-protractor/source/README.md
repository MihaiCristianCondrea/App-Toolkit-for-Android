# Ruler and Protractor creative source

Five phone, four tablet and four desktop screenshots, a feature graphic and the official icon are rendered from the Marketing UI. Each phone screenshot contains exactly one Pixel.

## Art direction

The default App Toolkit listing supplies the cream ground, warm brown typography and amber caption pills. Large pale-yellow MaterialShapes sit behind the phones; smaller amber shapes and fine ruler or angle ornaments provide balance. The application retains its captured palette.

Surface Ruler leads, followed by Protractor, Advanced Ruler, Unit Converter and the Measure workspace. Phone poses vary in scale, position, yaw and roll while retaining the full device and critical UI. The feature graphic uses the same warm shapes and measurement ornaments without a device.

## Render

From this directory:

```text
npm ci
npm run lint
npm run probe -- ruler protractor advanced converter measure ruler@fold protractor@fold advanced@fold measure@fold ruler@desktop protractor@desktop advanced@desktop measure@desktop
npm run solve
npm run verify -- en-US
npm run render -- en-US
npm run metadata
```

The composition solver, renderer and verifier share projected geometry and safe-area rules. Verification writes clean and debug contact sheets plus report.json to output/verify/. Rendering stages all graphics before replacing the upload folder.

## Verification

The warm campaign's 13 screenshot compositions passed all hard checks. Phone caption contrast is at least 13.17:1, pill text 5.62:1, with zero measured interference in the caption zones. The closing image has a soft pixel-balance preference at 0.558 W. Its asymmetric composition was inspected visually.

The renderer continues to report an intermittent local WOFF2 request error. Visible type and icons were inspected, but the request error itself remains unresolved. Production availability must be confirmed before upload.
