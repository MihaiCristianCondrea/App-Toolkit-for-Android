# Default listing before the redesign (retired)

App Toolkit's Google Play default listing as it was uploaded before the October 2026 redesign. Its visuals were made
outside this repository and have no source, so they are kept here as they were uploaded. Do not edit or upload them.

| | |
| --- | --- |
| Status | Retired, October 29, 2026 ([`listing.json`](listing.json)) |
| Locales | en-US |
| Source | None: device screenshots and a designer's export, moved in from `sample/app/src/main/play/` |
| Replaced by | [`../default/`](../default/source/README.md), rendered with Remotion from the UI kit's redesigned screens |
| Still used elsewhere | Nothing |

## Text

| File | Length | Content |
| --- | --- | --- |
| [`title.txt`](listing/en-US/title.txt) | 23 | "App Toolkit for Android" |
| [`short-description.txt`](listing/en-US/short-description.txt) | 60 | "Your ultimate all-in-one productivity and utility companion." |
| [`full-description.txt`](listing/en-US/full-description.txt) | 3,185 | An intro, then "Features" in three emoji-headed groups (quick-access utilities: coin and dice, tally counter, compass and level, reaction test, SOS flash, Flash Dimmer, Morse flashlight, Quick Settings tiles; device insights: Material colors; wellness: breathing), "Benefits", "How it works", "Get started today" and a feedback request |

## Graphics

| Folder | Files | Size | Shows |
| --- | --- | --- | --- |
| `icon/` | `icon.png` | 512 x 512 | The earlier launcher icon: a briefcase with the Android robot, a gear and braces |
| `feature-graphic/` | `feature-graphic.png` | 1024 x 500 | The earlier logo and "AppToolkit for Android" |
| `phone-screenshots/` | 4 | 2160 x 3840 | Welcome, Apps, the onboarding theme page, privacy |
| `tablet-screenshots/` | 4 | 3840 x 2160 | The same four screens on a tablet |

The screenshots and the feature graphic were saved without their alpha channel when they moved in (every pixel was
opaque, so they look the same).

[`unused/feature-graphic-1025x500.png`](unused/feature-graphic-1025x500.png) sat beside the uploaded feature graphic: a
phone and "Discover Android Tools, apps and a reusable dev components". At 1025 px wide Play rejects it, so it was
never uploaded.

## Why it was retired

- The screenshots show the onboarding, the Apps catalog and the privacy page, not the tools; none of them shows the
  redesigned Quick Tools, Measure or Play & Decide.
- The icon and the feature graphic carry the earlier briefcase mark, replaced by the toolbox.
- The text no longer meets the listing rules: praise ("ultimate"), emojis as headings, a call to action
  ("Download App Toolkit ... today"), and claims the app can't back ("maximum efficiency with minimal battery and memory
  footprint").
