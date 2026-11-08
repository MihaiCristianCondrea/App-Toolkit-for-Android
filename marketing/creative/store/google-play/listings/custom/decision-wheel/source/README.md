# Decision Wheel creative source

Independent Remotion source for the decision-wheel custom listing. The Marketing UI supplies real app screens; Material-Next supplies the 3D Pixel, Fold and Pixelbook Go frames. The app UI retains its captured colors.

## Commands

```sh
npm ci
npm run assets
npm run probe -- wheel result choices number coin dice wheel@fold result@fold number@fold dice@fold wheel@desktop result@desktop number@desktop dice@desktop
npm run solve
node scripts/placement.ts
npm run lint
npm run concepts
npm run verify -- en-US
npm run render -- en-US --verify-repeat
npm run metadata
node scripts/audit-package.mjs
node scripts/archive-review.mjs
```

Run the shared listing validator from marketing/creative/store/google-play with `node scripts/validate.mjs`.

## Composition and capture

The existing geometry solver projects the Material-Next device silhouette and measured critical UI. Each shot specifies a gentle pose, size and caption location. The projected silhouette is centered horizontally and fitted to the midpoint of the remaining vertical band; a measured UI focus near a golden horizontal guide is a secondary preference. Hard rules protect critical controls, caption clearances and the entire phone silhouette. Lower captions reserve 144 logical px beyond the device body for its shadow; the pixel verifier also checks the actual composited shadow against the text zone. The solver does not choose the art direction. Coin and die selectors are scoped to the open sheet; the probe rejects ambiguous matches rather than silently measuring a widget behind it.

The verifier renders clean, caption-free and debug views. It checks caption contrast and interference on actual pixels, projected UI bounds and visual balance. Reports and contact sheets are archived in ../review. placement.json records the primary silhouette framing target and actual position, framing error, camera angles, and the secondary control's offset from its golden guide. Pixel balance is checked separately.

Choice and result states are explicit in src/screens.ts. The hero has four food choices. The result has six activity choices, 1530° settled rotation and winner Music, matching the pointer calculation. The entry example uses four names. Coin and die captures use settled state, not a moving animation. The desktop wheel sheet is scrolled 24 CSS px, as a user can scroll it, so the complete Spin control remains visible. No app behavior is changed.

Artwork uses a large flower behind the hero, a smaller coral sunny burst behind the result, a pill behind the coin, and calmer unframed supporting scenes. Deterministic rounded confetti follows an ellipse around each projected phone, with silhouette, shape and caption exclusions. There is no simulated UI or randomly changing artwork. The optional warm hero study only changes the campaign field. The feature graphic concepts are selected through the existing composition props.

Rendering stages all 16 assets before replacing ../listing/en-US/graphics. Phone PNGs are 2160 × 3840, wide PNGs 3840 × 2160, feature graphic 1024 × 500 and official icon 512 × 512. Screenshots and feature are RGB; icon is RGBA.

Metadata uses the shared injector and readback. Package audit checks text limits, keyword uniqueness, asset dimensions and PNG modes, official icon pixels, alt text, and preserved pixels after metadata tagging. The repeatability option renders every phone twice and compares their hashes before metadata.

## Dependencies and boundaries

Remotion dependencies match the completed listings. Material-Next comes from marketing/ui; verify its installed version with npm ls there. No new creative dependency is needed.

The campaign has local source so changes cannot silently alter the completed listings. The projection, probe and validation techniques are reused without a shared-source refactor.

## Limits

The local font TTF must load before rendering; auxiliary bundled WOFF2 requests can still warn about content-length mismatch. No connected-device capture or production rollout verification is claimed. See the parent README for publication gates.
