# App Toolkit redesign: web side first

Status: started October 28, 2026. Phase 2 of [`redesign-and-marketing.md`](../redesign-and-marketing.md), done
in `marketing/ui` before any Compose change.

## Decisions

Made by the owner on October 28, 2026:

1. **Three tabs stay:** Quick Tools, Measure, Apps. No new tab, no device information screen. The second tab was
   called Numbers; with the ruler and protractor on it, every tool there gives a measurement or a number, so it is
   **Measure**. Its route id stays `numbers`.
2. **Quick Tools has four sections:** Sensors, Light & signals, Chance, Wellbeing. Ruler and Protractor move to Measure.
   Random Number, Dice Roll and Coin Flip belong to Chance in Quick Tools. Reaction Test stays in Measure. Material Colors stays in the app only. **Screen Light is not
   planned** and is not in the marketing UI.
3. **Settings, About, Help, onboarding and the other library screens are not in the marketing UI.**
4. **The compass is redesigned** in the style of [MBCompass](https://github.com/CompassMB/MBCompass): a rose of
   ticks, degree numbers and cardinal names that turns with the phone, a fixed pointer, and a large degree and
   direction readout. Drawn for App Toolkit (MBCompass is GPL-3.0 and its rose artwork CC BY-SA 4.0; nothing is
   copied), in the web kit and in Compose alike.
5. **Sheets follow the app.** Flash Dimmer (`FlashDimmerTool.kt`), Bubble Level (`LevelTool.kt`) and Breathe
   (`BreathingTool.kt`) are the app's, one to one, with one change: two values side by side (pitch and roll; phase
   and seconds) are a connected pair under the face, round outside and 8dp inside, like grouped buttons. The level
   face is the 16-sided cookie, crossed edge to edge like its widget. Every sheet opens like the app's `ModalBottomSheet`: no drag handle, straight to expanded,
   sliding up over a 32% scrim. Surface Ruler is the Compose exception: a full-screen dialog with edge graduations and calibration. Protractor, Morse & SOS and the other Measure sheets keep their design.
6. **Quick Tools is live.** Sensors and Light & signals are rows of two equal live widgets, [Compass][Bubble Level]
   and [Flashlight][SOS], each tool working inside its own shape: the compass in the 8-sided cookie, the level in
   the 16-sided cookie (its cross reaching the shape's edges), the torch in a burst, SOS in a 7-sided cookie. Light &
   signals sits on its tools' own colours, read from the start edge, so it does not look like Sensors. A tap on any
   widget opens its sheet. A vertical swipe over the flashlight's shape sets the brightness and fills the shape like
   a progress bar, only on torches that can dim; the number of levels is the device's (a Pixel 8 Pro has 45).
   Breathe (the app's name, rhythm 4-2-4-1) is one wide card without a play button. Quick Tools cards do not scale
   when pressed. The "Add a tool to Quick Settings" card is gone, and so are the filter chips: three sections scroll
   faster than they filter. Each section title has an icon. The Quick Tools icon in the bottom bar is the rounded
   one, selected and not.
8. **One native ad on Quick Tools,** between Sensors and Light & signals, under a "Sponsored content" title: drawn
   like the widgets (the advertiser's icon in a shape, headline, line, rating, Install). The section title marks it, so it has
   no Ad badge of its own. It shows
   only with the prototype's Show ads switch; marketing media never shows it.
7. **Kotlin waits.** The app's code is not changed during the web redesign. `CompassTool.kt` was changed once
   (`2da90de1`) and stays as it is; nothing else in Compose changes until step 7.

## Where each tool lives

| Tab | Section | Tools |
| --- | --- | --- |
| Quick Tools | Sensors | Compass (live rose), Bubble Level (live dial) |
| Quick Tools | Light & signals | Flash Dimmer, Morse & SOS |
| Quick Tools | Wellbeing | Breathe |
| Measure | Length & angle | Surface Ruler, Protractor |
| Measure | Convert & split | Unit Converter, Tip & Bill Splitter |
| Measure | Count & time | Counter, Reaction Test |
| Quick Tools | Chance | Random Number, Dice Roll, Coin Flip |
| Apps | | The developer's apps from the metadata API |

The app today has 12 tools on Quick Tools in four categories (Sensors & Measurement, Utilities & Fun, System &
Controls, Wellbeing), with Morse and SOS as two tools, and Screen Light. The table is the target; the Compose build
follows it after the web screens are approved.

## Shell

The app's shell is Material Next's Android preview (October 28, 2026): App Toolkit declares its identity once in
`marketing/ui/src/config/identity.js` (name, icons, fonts, palettes, destinations, links, with the library's
`toolkitFooter()` and `supportUs()`), and Material Next decides and draws the app bars, the navigation for each window,
the insets, the drawers and the sheets, following the library's `ShellChrome.kt` and `ShellFrame.kt`. New marketing
UI work and redesign steps build on it; Smart Cleaner and Android Insider move to it next, before smaller shared
patterns (result pills, shape badges, grouped cards) are extracted.

## Steps

| Step | What | Status |
| --- | --- | --- |
| 1 | Every tool on its tab in the kit; sheets for Coin Flip and Reaction Test, translated from `CoinFlipTool.kt` and `ReactionTestTool.kt` | Done |
| 2 | Quick Tools: Sensors as one grouped stack (compass rose card, level card), 28dp outer and 8dp inner corners in every group; the compass sheet redesigned; Measure tab with its four sections | Done (web) |
| 3 | Live widgets on Quick Tools; Flash Dimmer sheet and sheet motion from the app | Done (web) |
| 4 | Expressive pass on both tabs: emphasized type, shape and colour per section, motion | |
| 5 | Apps: real icons in captures, the Installed and Not installed filters the app has, the random app button | |
| 6 | Phone, fold and desktop for each tab; light and dark; `ui-unslop` review | |
| 7 | Owner approval, then Compose (`ToolkitTilesScreen.kt`, `NumbersScreen.kt`, the tile catalogue in `DefaultToolkitTilesRepository.kt`) | |

Each step ends with `npm run build` and `npm run qa` passing in `marketing/ui`. The Stitch exports in
`marketing/assets/ideas/` are read for steps 2 to 4 and deleted when step 4's concept is approved.

## In the app, not in the marketing UI

| Area | Missing in the kit |
| --- | --- |
| Quick Tools | Collapsible categories (remembered), the native ad between categories, tile status from the system (added, can add, unsupported) and the Android 13 "Add tile" request, hiding torch tools on devices without a torch |
| Tools | Separate SOS and Morse tools (the kit shows them as one), Material Colors (app only by decision), Screen Light (not planned) |
| Apps | Installed and Not installed state, the app details sheet (screenshots, version, Open app, Google Play, Share, Copy package, and Battery, Notifications, Permissions and Storage shortcuts), Share on each card, the loading and error states with Try again |
| Shell | Settings, Help & feedback, About, onboarding and the Components showcase: library screens, kept out of the marketing UI by decision |

## Known gaps

- The coin faces use Material Symbols (`person`, `token`); the app draws its own `ic_coin_heads` and
  `ic_coin_tails`.
- The app's compass reads magnetic north only; the kit shows no true north or field strength either.

### Measure tool translation

The web tools now follow the existing Compose presentations: full-screen Surface Ruler with calibration, graduated Protractor, single-step Counter, Bill Splitter, bidirectional Unit Converter and timed Reaction Test. The Measure overview design remains a separate step. Random Number presets are ordered 1–2, 1–6, 1–10, 1–100. Android code is unchanged; device parity still needs review.

## Native implementation, October 29, 2026

The owner approved moving the marketing design into Compose. Native Quick Tools, Measure and Play & Decide now follow the approved groups and interactive widgets. Tool input and overview/sheet state share dedicated ViewModels, with saved input restoration and foreground cleanup. Full-screen Protractor adapts to orientation; the new wheel and speaker cycle use native rendering and platform audio ownership.

The historical web-first steps above describe the sequence that produced the design. Native compile, automated regression coverage and visual/device parity are tracked separately; the marketing browser checks do not verify Android behavior. Final device review remains required before uploading the redesigned store listing.

## Owner decisions and widget parity, October 29, 2026

- The apps catalog and the Components showcase are debug pages: the drawer offers them in debug builds, or once the
  showcase is unlocked from About. The marketing UI no longer draws the apps catalog.
  - Superseded October 30, 2026: the apps catalog page, its drawer link and its list ads are removed. Quick Tools ends
    with a "More apps" carousel (the library's `AppShowcase`), and the Components page is a debug-build page only.
- Screen Light is removed from the app (the system's brightness and the Flash Dimmer cover it), and with it the
  More tools drawer page.
- Material Colors is the last Quick Tools section, in the app and in the marketing UI: a strip of the scheme's
  accents and containers that opens the palette sheet.
- The Compose widgets were rebuilt from the kit's computed styles: uppercase labels, faces in each tool's shape on a
  tint of its accent, bold values and accent notes; the Measure cards' visuals and readouts; the Chance group on the
  secondary container. The die faces the viewer at rest (the earlier fixed camera tilt skewed it) and the coin uses
  the kit's sizes and rim.

