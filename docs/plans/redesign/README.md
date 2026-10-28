# App Toolkit redesign: web side first

Status: started October 28, 2026. Phase 2 of [`redesign-and-marketing.md`](../redesign-and-marketing.md), done
in `marketing/ui` before any Compose change.

## Decisions

Made by the owner on October 28, 2026:

1. **Three tabs stay:** Quick Tools, Numbers, Apps. No new tab, no device information screen.
2. **Every tool the app has gets a place on a tab.** Coin Flip and Reaction Test join Numbers under
   **Chance & reflex**; Screen Light joins Quick Tools under **Light & signals**. Material Colors stays in the app
   only.
3. **Settings, About, Help, onboarding and the other library screens are not in the marketing UI.**
4. **The existing tool sheets keep their design.** Flash Dimmer, Compass, Bubble Level, Ruler, Protractor, Morse &
   SOS, Box Breathing and the Numbers sheets are not changed; new tools get sheets in the same style.

## Where each tool lives

| Tab | Section | Tools |
| --- | --- | --- |
| Quick Tools | Measure | Compass and Bubble Level (live glance), Surface Ruler, Protractor |
| Quick Tools | Light & signals | Flash Dimmer, Morse & SOS, Screen Light |
| Quick Tools | Calm | Box Breathing |
| Numbers | Calculate & convert | Tip & Bill Splitter, Unit Converter |
| Numbers | Count | Counter |
| Numbers | Chance & reflex | Random Number, Dice Roll, Coin Flip, Reaction Test |
| Apps | | The developer's apps from the metadata API |

The app today has 12 tools on Quick Tools in four categories (Sensors & Measurement, Utilities & Fun, System &
Controls, Wellbeing), with Morse and SOS as two tools. The table is the target; the Compose build follows it after
the web screens are approved.

## Steps

| Step | What | Status |
| --- | --- | --- |
| 1 | Every tool on its tab in the kit: Screen Light on Quick Tools; Count and Chance & reflex on Numbers; sheets for Screen Light, Coin Flip and Reaction Test, translated from `ScreenLightTool.kt`, `CoinFlipTool.kt` and `ReactionTestTool.kt` | Done |
| 2 | Quick Tools layout pass: the Stitch concept's live sensor glance and tile status (on, level, ready) on every card; the Quick Settings how-to card | Next |
| 3 | Numbers layout pass: hero, section rhythm, the counter card | |
| 4 | Apps: real icons in captures, the Installed and Not installed filters the app has, the random app button | |
| 5 | Phone, fold and desktop for each tab; light and dark; `ui-unslop` review | |
| 6 | Owner approval, then Compose (`ToolkitTilesScreen.kt`, `NumbersScreen.kt`, the tile catalogue in `DefaultToolkitTilesRepository.kt`) | |

Each step ends with `npm run build` and `npm run qa` passing in `marketing/ui`. The Stitch exports in
`marketing/assets/ideas/` are read for steps 2 to 4 and deleted when step 4's concept is approved.

## Known gaps in step 1

- The coin faces use Material Symbols (`person`, `token`); the app draws its own `ic_coin_heads` and
  `ic_coin_tails`.
- The app opens Screen Light as a full-screen light with the controls at the bottom. The kit shows the same controls
  in a sheet, with a preview of the light's colour.
