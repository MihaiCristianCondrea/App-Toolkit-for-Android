# Shared marketing assets

Use `marketing/assets/` as the source for shared artwork in a project. Copy the files you need when setting up another repository. Keep the app's own theme, brand assets and approved layouts as the source for its product screens.

## Fonts

The library has 72 TrueType files. It contains no WOFF2 files.

| Family | Static files | Variable files | Uses |
| --- | --- | --- | --- |
| [Google Sans Flex](fonts/google-sans-flex/) | 45 upright instances. The 9pt, 24pt, 36pt, 72pt and 120pt optical sizes each have nine weights from Thin to Black. Names follow `<optical-size>-<weight>.ttf`. | [`variable/variable.ttf`](fonts/google-sans-flex/variable/variable.ttf) with `ROND`, `opsz`, `slnt`, `wdth` and `wght`. | Use the variable file for a flexible type scale or animated weight and width. Use static instances when the renderer or Android integration needs fixed weights and optical sizes. |
| [Google Sans Code](fonts/google-sans-code/) | 24 instances. Monospaced and proportional cuts each have six weights from Light to Extra Bold in upright and italic. Names follow `[proportional-]<weight>[-italic].ttf`, with `italic.ttf` for Regular Italic. | [Upright](fonts/google-sans-code/variable/variable.ttf) and [italic](fonts/google-sans-code/variable/variable-italic.ttf), each with `MONO` and `wght`. | Use the mono setting for aligned code or tabular snippets. Use the proportional cut when the design calls for the same family outside a code block. |

Google publishes both families under the SIL Open Font License 1.1. Upstream credits the Google Sans Flex Authors (2015) and the Google Sans Code Project Authors (2025). Include the corresponding notice and license text when redistributing copies of the font files. [Google Sans Flex license](https://github.com/google/fonts/blob/main/ofl/googlesansflex/OFL.txt) · [Google Sans Code license](https://github.com/google/fonts/blob/main/ofl/googlesanscode/OFL.txt)

The repository does not record exact upstream release tags for these binaries. Consult the [Google Sans Flex releases](https://github.com/googlefonts/googlesans-flex/releases) and [Google Sans Code releases](https://github.com/googlefonts/googlesans-code/releases) if you need a different distribution format.

- **Marketing UI.** Sync selected fonts into `marketing/ui/public/fonts/` with family names in their serving paths. Declare static files by weight and style in CSS. Declare a weight range for a variable file and use the family in the app's typography tokens.
- **Remotion.** Sync selected fonts into the project's `public/fonts/`, resolve URLs with `staticFile()`, and load them before measuring text or rendering. Keep the family directory in the path because both families provide a `variable.ttf`.
- **Android.** Copy selected faces into the owning module's `res/font/` using Android resource names such as `google_sans_flex_24pt_regular.ttf`. Match the app's supported font APIs and typography settings.

## Shapes

Use SVG paths as images or CSS masks. For Remotion, serve the same SVG through `public/` and `staticFile()`. For path animation, import an existing path and preserve its `viewBox`. Keep its aspect ratio when cropping or scaling. Place decoration where it leaves text and controls readable.

The names below describe the local files. The repository does not record an upstream source or license for this set. Verify provenance and reuse rights before external publication. The Material UI skill documents Google's Material shapes separately. [Material shape reference](../.agents/skills/official/google/material-ui/references/shape.md)

| File | Shape | Category |
| --- | --- | --- |
| [`4-sided-cookie.svg`](shapes/4-sided-cookie.svg) | 4-sided cookie | Scalloped |
| [`6-sided-cookie.svg`](shapes/6-sided-cookie.svg) | 6-sided cookie | Scalloped |
| [`7-sided-cookie.svg`](shapes/7-sided-cookie.svg) | 7-sided cookie | Scalloped |
| [`8-sided-cookie.svg`](shapes/8-sided-cookie.svg) | 8-sided cookie | Scalloped |
| [`9-sided-cookie.svg`](shapes/9-sided-cookie.svg) | 9-sided cookie | Scalloped |
| [`12-sided-cookie.svg`](shapes/12-sided-cookie.svg) | 12-sided cookie | Scalloped |
| [`16-sided-cookie.svg`](shapes/16-sided-cookie.svg) | 16-sided cookie | Scalloped |
| [`16-sided-cookie-outlined.svg`](shapes/16-sided-cookie-outlined.svg) | 16-sided cookie outlined | Scalloped |
| [`4-leaf-clover.svg`](shapes/4-leaf-clover.svg) | 4-leaf clover | Botanical and soft |
| [`8-leaf-clover.svg`](shapes/8-leaf-clover.svg) | 8-leaf clover | Botanical and soft |
| [`flower.svg`](shapes/flower.svg) | Flower | Botanical and soft |
| [`puffy.svg`](shapes/puffy.svg) | Puffy | Botanical and soft |
| [`bun.svg`](shapes/bun.svg) | Bun | Botanical and soft |
| [`ghost-ish.svg`](shapes/ghost-ish.svg) | Ghost ish | Botanical and soft |
| [`heart.svg`](shapes/heart.svg) | Heart | Botanical and soft |
| [`boom.svg`](shapes/boom.svg) | Boom | Radial |
| [`burst.svg`](shapes/burst.svg) | Burst | Radial |
| [`soft-burst.svg`](shapes/soft-burst.svg) | Soft burst | Radial |
| [`sunny.svg`](shapes/sunny.svg) | Sunny | Radial |
| [`very-sunny.svg`](shapes/very-sunny.svg) | Very sunny | Radial |
| [`dots.svg`](shapes/dots.svg) | Dots | Pattern |
| [`dots-grid.svg`](shapes/dots-grid.svg) | Dots grid | Pattern |
| [`dots-small.svg`](shapes/dots-small.svg) | Dots small | Pattern |
| [`arrow.svg`](shapes/arrow.svg) | Arrow | Directional |
| [`rounded-arrow.svg`](shapes/rounded-arrow.svg) | Rounded arrow | Directional |
| [`fan.svg`](shapes/fan.svg) | Fan | Directional |
| [`slanted.svg`](shapes/slanted.svg) | Slanted | Directional |
| [`x.svg`](shapes/x.svg) | X | Directional |
| [`arch.svg`](shapes/arch.svg) | Arch | Geometric |
| [`circle.svg`](shapes/circle.svg) | Circle | Geometric |
| [`circle-triangle.svg`](shapes/circle-triangle.svg) | Circle triangle | Geometric |
| [`diamond.svg`](shapes/diamond.svg) | Diamond | Geometric |
| [`gem.svg`](shapes/gem.svg) | Gem | Geometric |
| [`hexagon.svg`](shapes/hexagon.svg) | Hexagon | Geometric |
| [`oval.svg`](shapes/oval.svg) | Oval | Geometric |
| [`pentagon.svg`](shapes/pentagon.svg) | Pentagon | Geometric |
| [`pill.svg`](shapes/pill.svg) | Pill | Geometric |
| [`semicircle.svg`](shapes/semicircle.svg) | Semicircle | Geometric |
| [`square.svg`](shapes/square.svg) | Square | Geometric |
| [`triangle.svg`](shapes/triangle.svg) | Triangle | Geometric |
| [`very-rounded-triangle.svg`](shapes/very-rounded-triangle.svg) | Very rounded triangle | Geometric |

| Category | Uses |
| --- | --- |
| Scalloped | Feature chips, photo masks and staggered motion accents. |
| Botanical and soft | Social illustrations, end-card accents and quiet background silhouettes. |
| Radial | Emphasis behind a headline or a timed reveal in a video. |
| Pattern | Background texture, gutters and small accents behind clear areas of the frame. |
| Directional | Movement cues and decorative framing near the edge of a composition. |
| Geometric | Masks, crops and background blocks for product graphics and UI prototypes. |

## Device frames

Use the device's display aperture and corner mask to fit the actual app window. The frame guide identifies which skins are local and which Material Next provides. [Device frames](device-frames/README.md)

## Google Play badge

The library holds the [English Get it on Google Play SVG](badges/google-play/en-get-it-on-google-play.svg). Use it as a download call to action in external graphics, video end cards and landing pages for an app distributed on Google Play. Check the current artwork and use the official localized badge for other languages. Preserve its proportions, colors and artwork. Keep clear space of at least one-quarter of the badge height, use a contrasting background, and use a digital height of at least 28 px. Follow Google's brand review and attribution guidance. [Google Play badge guidelines](https://partnermarketinghub.withgoogle.com/brands/google-play/google-play/lockups-icons-badges/) · [Android brand guidelines](https://developer.android.com/distribute/marketing-tools/brand-guidelines)

Keep store badges out of Google Play listing screenshots and feature graphics. [Play Console preview asset guidance](https://support.google.com/googleplay/android-developer/answer/9866151)
