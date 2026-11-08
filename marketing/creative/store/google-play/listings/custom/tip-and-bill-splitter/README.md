# Tip & Bill Splitter custom listing

Status: live. Approved by Google Play review and published on November 8, 2026. English creative package: 5 phone, 4 foldable and 4 desktop screenshots, a feature graphic and the official icon.

## Audience and positioning

People calculating a restaurant gratuity and splitting the total equally between friends. The promise is to enter the bill, select a supported tip and read each person's share together with the tip and total. This is a local calculation, not a payment service, receipt scanner, itemized splitter or currency exchange tool.

The Android source offers 10%, 15% and 20% tips, plus a tip-off checkbox. It accepts a positive whole-number party size. BillCurrency contains USD, EUR, GBP, RON, JPY and INR. BillSplitterRepository rounds the tip half up and each share up to the denomination's decimal places. Yen uses whole numbers. Screenshots preserve the rounding note.

## Listing text

| File | Content | Characters |
| --- | --- | --- |
| title.txt | Tip Calculator: App Toolkit | 27 / 30 |
| short-description.txt | Calculate a tip and split the bill equally with a clear per-person amount. | 74 / 80 |
| full-description.txt | Tip controls, equal splitting, currencies, usage, rounding and related tools | 1,544 / 4,000 |

There are 189 candidate search terms. See [keyword-review.md](keyword-review.md) for priorities, exploratory terms and exclusions. There is no per-keyword search-volume evidence and no promise of ranking or placement.

## Research and positioning

Public Google Play descriptions were reviewed November 8, 2026:

| Reference | Common expectations and broader features | Decision for App Toolkit |
| --- | --- | --- |
| [Tip Calculator, UKM APPS](https://play.google.com/store/apps/details?id=com.sanca.tip) | Bill, percentage and party size lead to tip, total and share. Also advertises more presets, rounding controls and tipping guidance. | Show the three core inputs and the result immediately; use only the supported presets. |
| [Tip Calculator & Bill Splitter, MCMLV1](https://play.google.com/store/apps/details?id=org.mcmlv1.tipcalculator) | Equal split, separate tax, custom percentages, copy and round-up controls. | Keep the calculator's scope explicit; no tax, copy, custom-rate or manual rounding claims. |
| [TipSplit](https://play.google.com/store/apps/details?id=za.co.protus.tipsplit) | Equal and proportional splits, named people, fixed tips and sharing. | Say equal shares and avoid implying itemized or shared calculations. |

Speed, accuracy, privacy and no-ad claims recur in this category. These are advertisers' claims, not verified comparisons. App Toolkit makes no superiority, no-tracking, no-ad or guaranteed-accuracy claim. The competitive angle is readable everyday calculation inside a broader toolkit. Screenshot URLs could not be retrieved reliably, so this research does not claim a visual competitor audit.

The copy and graphics follow Google's [Metadata](https://support.google.com/googleplay/android-developer/answer/9898842) and [Deceptive Behavior](https://support.google.com/googleplay/android-developer/answer/17006354) guidance: real functionality, accurate examples and no unsupported payment or exchange features.

## Creative direction

Warm cream, coral, apricot and muted butter, with dark warm-brown type. Google Sans Flex, the caption pill and real MaterialShapes retain the App Toolkit identity. A thin perforated line connects the panorama like a bill divider. Rounded layered shapes and five smaller accents per image frame the device without hiding the actual UI. The app's magenta bill sheet is unchanged.

1. Tip and split in one place: a full Pixel with an 84.50 USD bill, 15% tip, three people and a 32.40 USD share.
2. Choose your tip: a closer Pixel with a 52 euro bill and the 10% preset. The top device edge is intentionally beyond the image boundary; every bill control and result remains visible.
3. Dinner for four: a full Pixel with a 126 GBP bill, 10% tip and a 34.65 GBP share.
4. Your currency: a whole Pixel with JPY selected, a 4800 yen bill and a 2640 yen share for two people.
5. More tools: the real Measure workspace, including the bill-splitter entry and supporting tools.

One 3D Pixel per phone screenshot. Whole devices, camera poses and critical UI are measured through Material-Next's actual projection. Screenshots 1, 3 and 5 are whole product shots; image 2 is deliberately closer; image 4 places the caption below. The alternate hero was reviewed and kept as a study.

The selected feature graphic pairs a large headline with a dinner-bill illustration on a coral MaterialShape. Its euro example matches the calculator: 52.00 plus 5.20 tip totals 57.20, or 28.60 for two people. It is supporting artwork, not an app receipt-scanning interface. Two other concepts remain in the comparison sheet.

Foldable and desktop assets use the actual adaptive layouts, Pixel 9 Pro Fold and Pixelbook Go, at true size with no promotional captions. The official icon is copied unchanged.

## Validation and publication limits

See [source/README.md](source/README.md) for commands; the verifier writes results and contact sheets to source/output/ (generated, not committed). The adapter validates bill amounts, currency codes, party size and displayed shares against fixed examples. Geometry, caption contrast, shape clearances, font loading, repeated captures, formats, alt text, metadata readback and icon identity are separate checks.

A preview parity fix changes only the Marketing UI currency selector to the Android code-button and dropdown. The Marketing UI browser suite passed 147 checks. Android application code and other listing assets are untouched.

The public Play page retrieved on November 8 still displayed October 3 information and did not establish availability of the redesigned 26.11.02 app. Production rollout and candidate keyword availability require manual Play Console confirmation. No connected-device comparison, upload or publication is claimed.
