# App Toolkit 3.0 interface and release checklist

Updated October 30, 2026. The web-first redesign is implemented in Compose and in
`marketing/ui`. This file describes the current interface, not the retired Apps tab.

## Navigation and tools

| Tab | Section | Tools |
| --- | --- | --- |
| Quick Tools | Sensors | Compass, Bubble Level |
| Quick Tools | Light & signals | Flash Dimmer, Morse & SOS |
| Quick Tools | Hardware & sound | Water Eject |
| Quick Tools | Wellbeing | Breathe |
| Quick Tools | Colors | Material Colors |
| Quick Tools footer | More apps | Optional developer-app carousel, with app details |
| Measure (route `numbers`) | Length & angle | Surface Ruler, Protractor |
| Measure | Convert & split | Unit Converter, Tip & Bill Splitter |
| Measure | Count & time | Counter, Reaction Test |
| Play & Decide | Chance | Decision Wheel, Random Number, Coin Flip, Dice Roll |

Apps is no longer a destination or a drawer entry. Stored Apps startup selections open
Quick Tools. The app graph supplies More apps through the footer callback; the apps and
tiles features do not depend on each other. Their loading, state and errors remain separate.

## Current visual and interaction rules

- Fixed tool accents in light and dark mode. The app theme owns the screen background and
  neutral surfaces. The widgets do not change their colours with the wallpaper.
- Native tool faces and badges follow the kit's shape curves. App icons retain their artwork.
- Section titles and individual cards use the library's entrance motion, including reduced motion.
- More apps uses filled 28dp cards and the shared GeneralButton in Tonal style. It has no
  colour overrides, so its actions inherit the Material colour scheme.
- A missing, empty or unavailable catalogue contributes no footer layout node. A cached
  catalogue remains useful offline. This optional section never blocks the tools.
- More apps rechecks local installation and version information on ON_RESUME without
  downloading the catalogue. An open details sheet follows install, update and uninstall changes.
- Ruler and Protractor are full-screen tools. Other tools use the shared expanded sheet host.
- The shell consumes covered insets; ToolWorkspace reserves its padding and adds the remaining
  system-navigation inset outside the scrolling viewport.

## Monetization and analytics

| Surface | Native placement |
| --- | --- |
| Quick Tools | After Light & signals, before Hardware & sound; after Sensors on torchless devices |
| Measure | Between Convert & split and Count & time |
| Play & Decide | Below Chance |
| More apps and its details | None |

Search suppresses overview ads. Loading or unavailable ads leave no empty slot. Store graphics
and marketing captures have ads off. Existing consent and ad-impression reporting remain library-owned.

More apps reports view_item_list after at least half its cards row is visible for one
foreground second, while its details sheet is closed. Repeated callbacks and catalogue refreshes
do not duplicate a continuous exposure. Leaving view or the foreground ends the exposure;
a later qualifying exposure can report again. App-card actions remain separate events.

## Verification and release gates

Android CI now runs on pushes and pull requests for master and develop, and supports manual
dispatch. It installs the platform declared in release.properties, assembles the sample debug app,
runs Android JVM tests, checks module boundaries and runs debug lint. Reports are retained as artifacts.
A workflow configuration is not evidence of a passing run; inspect its result before release.

| Check | Required evidence |
| --- | --- |
| Install/update/uninstall with the app retained | Local package checks update cards and open details, with no catalogue download |
| Visibility analytics | No load-time impression; one per qualifying foreground exposure; no refresh duplicate |
| Optional footer | No heading, placeholder or extra section gap without apps; tools stay usable |
| Offline | Cached catalogue shown; no-cache failure contributes no widget |
| Small phones and large fonts | Actions remain reachable, names truncate safely, sheets scroll |
| Fold and desktop | Cards reflow, rail/drawer rules and insets remain intact |
| Light/dark and reduced motion | App background preserved, actions use theme roles, motion follows system preferences |
| No-torch device | Torch controls absent; one Quick Tools ad remains after Sensors |
| Rotation and foreground return | Tool state retained; install metadata refreshed; no replay of audio or flashing |
| Native runtime | Build the changed APK and exercise it on an emulator or device; JVM and web checks are not substitutes |

## Store listing

The default listing lives in `marketing/creative/store/google-play/listings/default`. Its
first three phone graphics show Quick Tools, Measure and Play & Decide. Screenshots are generated
from the current UI kit with ads off; the listing's existing visual design is retained.
The text describes fixed tool accents and distinguishes full-screen measurement tools from sheets.

The October 29 Protractor graphic contained the removed Angle label and alignment instruction.
The graphics were refreshed from the current kit and their RGB format, metadata and dimensions
were validated. Upload the listing together with the redesigned app. Updating repository files does not publish
them to Google Play. The retired default-legacy listing remains unchanged.
