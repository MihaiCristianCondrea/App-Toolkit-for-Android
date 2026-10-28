# Default listing source

There is no source yet. The visuals in [`listing/en-US/graphics/`](../listing/en-US/graphics) were made outside this
repository and moved here, unchanged, from `sample/app/src/main/play/` in October 2026:

| Folder | Files | Shows |
| --- | --- | --- |
| `icon/` | `icon.png`, 512 x 512 | The launcher icon |
| `feature-graphic/` | `feature-graphic.png`, 1024 x 500 | The logo and "AppToolkit for Android" |
| `phone-screenshots/` | 4, 2160 x 3840 | Welcome, Apps, onboarding theme, privacy |
| `tablet-screenshots/` | 4, 3840 x 2160 | The same four screens on a tablet |

The screenshots and the feature graphic were saved without their alpha channel, which Play does not accept for them.
Every pixel was opaque, so the images look the same.

[`unused/feature-graphic-1025x500.png`](unused/feature-graphic-1025x500.png) is a second feature graphic (a phone and
"Discover Android Tools, apps and a reusable dev components") that sat beside the first. It is 1025 px wide, a size Play
rejects, so it was not the uploaded one. It is kept only until the retired listing is made.

## Next

After the redesign (Phase 3 of [`docs/plans/redesign-and-marketing.md`](../../../../../../../docs/plans/redesign-and-marketing.md)),
this folder gets a real source, started from the `play-store-media` skill's template: screenshots and the feature
graphic rendered from the `marketing/ui` screens, with `strings/en-US.json` for the captions. The current visuals and
`unused/` then move to `listings/default-legacy/`.
