# Decision Wheel custom listing

Status: planned, not uploaded or published. English creative contains six phone, four foldable and four desktop screenshots, a feature graphic and the unchanged official icon.

## Audience and positioning

For people who want to enter a few choices and make a random pick. The opening three images show Decision Wheel ready, a selected result and a different set of typed choices. Random Number, Coin Flip and Dice Roll provide secondary value.

The title is "Spin the Wheel: App Toolkit". The description explains the real 2–12 choice limit, entry flow and selected result. It makes no promises about saved wheel collections, weighted odds, verified fairness or prize drawings.

## Research

Reviewed November 8, 2026:

- [Spin The Wheel, net.spinthewheel.app](https://play.google.com/store/apps/details?id=net.spinthewheel.app) promotes a much larger entry allowance and prebuilt wheels.
- [Spin The Wheel, com.spinthewheeldecider](https://play.google.com/store/apps/details?id=com.spinthewheeldecider) emphasizes customization and wheel sharing.
- [Spin the Wheel, DevGeniuses](https://play.google.com/store/apps/details?id=com.spin.the.wheel.spinner.random.picker) positions itself around a customizable random picker.

App Toolkit's credible position is a small, simple choice wheel alongside three other chance tools. Competitor descriptions establish category vocabulary, not search volume or a ranking forecast.

The 157 keyword candidates cover wheel searches, natural requests to choose, small-group and meal/activity examples, and the supporting tools. They contain no competitor names. User-entered examples require entering the choices manually; there are no advertised built-in food or classroom presets. Google Play Console must confirm available keyword targets and grouping. Historical exports do not establish per-keyword demand.

## Creative direction

A little moment of chance: pale lilac, plum captions, warm paper-like coral and butter accents. MaterialShapes have distinct roles: a large flower balances the hero, a coral sunny burst celebrates the result, and a soft pill supports the coin. Other scenes use smaller accents and more open space. Rounded confetti follows a mathematically placed elliptical trajectory outside the projected phone and becomes denser around the selected-result scene. It is campaign illustration, not an advertised app animation.

The lilac palette was selected over a warmer cream study because it gives the chance tools a separate identity while retaining App Toolkit's typography, MaterialShapes and caption pills. Alternating upper and lower captions, different device sizes and independent pitch/yaw targets break up the sequence. The result turns 14° in yaw and 6° in pitch; the hero stays nearly front-facing. The projected phone silhouette is centered in the space left by its caption. Golden horizontal guides are secondary targets for the main control, with framing and readability taking priority. Every phone image contains exactly one Pixel.

1. Entered choices, ready to spin.
2. Six activities with Music selected, a settled pointer and more celebratory decoration.
3. Ava, Leo, Mia and Noah as a different manually entered set.
4. Random Number showing 37 within 1–100.
5. A settled Tails coin.
6. A settled die showing five.

The feature graphic uses an oversized segmented wheel, pointer and MaterialShape silhouette. Three compositions are retained as previews. The right-hand wheel with left-hand title was chosen for its readable silhouette at small sizes. The illustration is decorative; app screenshots always use the genuine wheel.

## Source and review

[Render instructions](source/README.md). [Clean phone contact sheet](review/contact-clean.png), [debug sheet](review/contact-debug.png), [verification](review/verification.json), and [feature concepts](review/feature-concepts.png).

The source adapts the existing listing pipeline locally. Existing listings and Android code are unchanged. No rendering framework or dependency was added.

## Validation

- TypeScript, layout solver and all 14 screenshot geometry/pixel checks passed.
- Phone captions: at least 12.61:1 contrast, 7.48:1 pill text, 0.00% background interference.
- All six full-resolution phone captures were byte-identical on repeat rendering.
- All 16 graphics passed metadata injection/readback with decoded pixels preserved.
- Package audit passed: dimensions, PNG color modes, alt text, text limits and 157 unique keyword candidates.
- The official icon is pixel-identical to the completed Ruler & Protractor listing.
- The shared Play listing validator passed.
- Material-Next Web Components 0.2.0 was confirmed installed.

See review/package-audit.json for exact text counts and file hashes, and review/placement.json for measured golden-guide placement. Device placement uses the projected silhouette rather than an individual control's horizontal position. Pitch and roll are restrained, and spacing above and below each phone is scored within its caption-free band.

## Publication limits

Confirm that the shown app version and UI are available to the intended production audience in Play Console before uploading. Production rollout has not been independently established by this work. Confirm search-target availability there as well.

Source parity was checked against Compose and the Marketing UI; no connected Android device comparison was performed. Bundling emits auxiliary WOFF2 content-length warnings. The local Google Sans Flex TTF is explicitly required to load before a still can complete. Final images are inspected for text and icon rendering.
