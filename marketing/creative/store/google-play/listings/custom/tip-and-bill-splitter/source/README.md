# Tip & Bill Splitter listing source

Five 2160 x 3840 phone images, four 3840 x 2160 foldable images, four 3840 x 2160 desktop images, one 1024 x 500 feature graphic and the unchanged 512 x 512 official icon. Upload files live in ../listing/en-US. Review sheets and reports live in ../review.

## Render and verify

Material-Next Web Components 0.2.0 is installed in marketing/ui. The listing adapts the completed Unit Converter rendering tools and device geometry, while keeping its own examples, palette, artwork, source and uploads.

Use npm.cmd on Windows if PowerShell blocks npm.ps1.

```sh
npm ci
npm run lint
npm run probe -- dinner tip friends currency toolkit dinner@fold tip@fold friends@fold currency@fold dinner@desktop tip@desktop friends@desktop currency@desktop
npm run solve
node scripts/placement.ts
node scripts/preview-campaign.mjs
node scripts/creative-preview.mjs
npm run verify -- en-US
npm run render -- en-US --verify-repeat
npm run metadata
npm run audit
npm run archive
```

Run node scripts/validate.mjs from marketing/creative/store/google-play. Marketing UI changes also require npm run qa from marketing/ui.

The renderer stages every output before replacing graphics. It copies the canonical icon from the approved Ruler & Protractor listing and checks pixel identity. Metadata is tagged after rendering, read back from all 15 graphics, and checked for unchanged decoded pixels.

For a targeted phone refresh, first keep a complete verified package, then run node scripts/verify.mjs en-US 3 4 and npm run render -- en-US --phone-only --shots=3,4 --verify-repeat. Indices are zero based. Untouched renders and checks are preserved; the contact sheet and panorama are rebuilt.

## Composition and capture

- screens.ts defines four bill examples and the Measure workspace. Every bill uses an actual supported 10/15/20 percent preset and a positive whole-number party size.
- LitScreen.tsx mounts the genuine Marketing UI. It checks the actual bill and people inputs, currency button, per-person result, required fonts and 3D Pixel picture. Exact minor-unit expectations also validate the tip and total. Finite transitions are finished before capture.
- UiProbe.tsx records the title, amount/currency row, percentage options, people controls, result and rounding note at each window size.
- geometry.ts projects the installed Pixel model's silhouette and UI through the real camera transform.
- shots.ts searches scale, position and gentle camera poses for each image. The creative objectives vary; containment and readable controls are hard rules.
- compose.ts places real MaterialShapes with fixed obstacles and a seeded scatter, enforcing caption, phone and shape clearances.
- Glass.tsx draws a continuous perforated divider behind the campaign. It is masked out of the caption zones.
- verify.mjs produces clean, caption-free and debug renders and checks caption contrast, background interference, layout and pixel-weighted balance. Wide layouts have their own measured controls and device bounds.

The tip image has a deliberate top-boundary crop; its controls and complete summary remain visible. Every other phone is whole. The feature graphic is an illustrated example, not a receipt-scanning claim or a fake app screen.

The two hero studies and three feature concepts are reproducible through creative-preview.mjs. The top-caption hero and connected feature composition are selected. Review sheets retain the alternatives for comparison.

## Limits

The currency button was aligned with BillSplitterTool.kt before capture. Browser and Android text rasterization can differ; no connected-device side-by-side comparison was available. Examples shown use USD, EUR, GBP and JPY. The description also lists the implemented RON and INR denominations without claiming exchange.

The public Play page does not establish the 26.11.02 rollout. Confirm production and Play Console targeting before upload. Nothing here publishes a listing.

npm ci retains the established Remotion 4.0.529 toolchain and reported eight high-severity dependency advisories. No dependency upgrade or production-app audit is part of this creative task.
