# Fidelity record

| Requirement | Implementation | Verification |
| --- | --- | --- |
| Original artwork | Lid and body come from the original SVG paths; Pixel skin and Play badge retain their source artwork | Asset preparation and source inspection |
| Attached chest lid | Shared body drop; folding face compensated at the 44.24% seam; mirrored inside below body in stacking order | Sheet at 1.7, 1.8, 1.85, 2.0, 2.4 and 3.0 s, enlarged lid still and constant-seam assertion |
| Four tools leave the mouth | Scale 0.3 start behind the body, alternating lanes, ordered stacking and shared-transform shadows | Launch/onset sheets and every-frame measured rectangle intersection guard |
| Real UI | All screens and cards mount LiveScreen or LiveCrop, with ads and More apps disabled | Rendered sheets and source inspection; no UI-kit screens changed |
| Measured assembly | Crops restore their real Quick Tools coordinates as the centre-built screen forms around them | Assembly samples at 9.6, 11.2 and 12.4 s |
| Material tab motion | Measured nav item selects shared-axis direction; old and new screens move 30 px with a 0.3 s fade-through | Samples at 17.0, 17.3, 17.6, 18.1, 21.9, 22.2, 22.6 and 23.0 s |
| Card container transforms | Source and return rectangles come from the actual phone DOM; original cards are hidden while lifted; scroll state carries through tab changes | Feature and return sheets |
| Layout and copy | Separate text/subject columns and 96 px safe margin; words reveal on detected notes | Default 19-time sheet; enlarged title and feature samples |
| Beat coverage | 94 referenced responses, 35 separated large gestures, phrase-boundary camera moves | npm run beats, called by npm run check; rejection fixtures for gaps and collisions |
| Closing hold and hit | Foreground holds 27.90 to 29.40; mark anticipates, then the kit gold burst covers the frame; final mark reaches exact rest | Ending sheet through the last frame, plus minimum hold assertion |
| Full soundtrack | Zero start, unity gain, unchanged source/prepared bytes, no trimming or looping | SHA-256 preparation/check and encoded duration/audio checks |
| Device aperture | Original 1280 x 2856 aperture at 60,61 | Asset check and rendered mask inspection |
| Deterministic capture | Only frame state drives transforms, values, scroll and UI mounts | Out-of-order still rendering and complete preview render |

## Review

Each of the seven local tasks has its own commit. The contact-sheet command is `npm run sheet`, with optional seconds and `BROWSER_EXECUTABLE`. It writes numbered samples to `output/images/review-sheet.png`. The beat audit output is included in the beat-audit commit message.

The campaign is still a creative preview. Full-speed visual playback and listening must be reported separately from still inspection and numerical audio validation. Static Compose-to-web parity was not rerun; the film uses the existing UI kit and retains its documented deviations. No Android code, soundtrack, device art, badge or logo source was changed. No master render or push is part of this work.
