# Fidelity record

| Requirement | Implementation | Verification |
| --- | --- | --- |
| Original artwork | Lid and body come from the original SVG paths; Pixel skin and Play badge retain their source artwork | Asset preparation and source inspection |
| Sketchbook opening and chest | Three original background shapes frame the original mark; its intact lid swings around the right lip, with the handle visible and the body dropping into place | Opening sheet at 0.3, 0.9, 1.3, 1.7, 1.95 and 2.4 s, compared with the owner's references |
| Standalone and connected corners | Actual card surfaces and crop/shadow bounds share the same animated radii: 28 px alone, 8 px on connected seams, then original measured radii inside the phone | Runtime DOM-radius checks, corner endpoint assertions, enlarged 9.5 s bento still and every-frame flight guard |
| Four tools leave the mouth | Scale 0.3 start behind the body, alternating lanes, ordered stacking and shared-transform shadows | Launch/onset sheets and every-frame measured rectangle intersection guard |
| Real UI | All screens and cards mount LiveScreen or LiveCrop, with ads and More apps disabled | Rendered sheets and source inspection; no UI-kit screens changed |
| Measured assembly | Crops restore their real Quick Tools coordinates as the centre-built screen forms around them | Assembly samples at 9.6, 11.2 and 12.4 s |
| Material tab motion | Measured nav item selects shared-axis direction; old and new screens move 30 px with a 0.3 s fade-through | Samples at 17.0, 17.3, 17.6, 18.1, 21.9, 22.2, 22.6 and 23.0 s |
| Card container transforms | Source and return rectangles come from the actual phone DOM; original cards are hidden while lifted; scroll state carries through tab changes | Feature and return sheets |
| Layout and copy | Clear copy regions, measured-height bento close-up, masked spring type and a moving tool-name belt; camera close-ups leave the column layout on purpose | Numbered sheets for the full cut and transitions |
| Live Quick Tools actions | Compass and Level lift from their real phone rectangles, change values and return; the flashlight fill changes smoothly and the native SOS preview is sought to frame time during the launch phrase | Quick Tools samples at 13.8 and 15.8 s, launch tracking samples at 4.8 and 5.3 s |
| Changing composition | Alternating launch whip pans, full-frame bento, solo Counter shot and the wheel taking over from the device | Encoded pacing strips and camera-boundary frames |
| Background depth | Shapes use 40% camera depth with finite hit rotations and scale punches | Camera and feature samples |
| Beat coverage | 94 referenced responses, 51 separated large gestures, phrase-boundary camera moves | npm run beats, called by npm run check; rejection fixtures for gaps and collisions |
| Closing hold and hit | Foreground holds 27.90 to 29.40; mark anticipates, then the kit gold burst covers the frame; final mark reaches exact rest | Ending sheet through the last frame, plus minimum hold assertion |
| Full soundtrack | Zero start, unity gain, unchanged source/prepared bytes, no trimming or looping | SHA-256 preparation/check, encoded duration/audio checks and decoded audio correlation 0.9942 with gain 0.999 |
| Device aperture | Original 1280 x 2856 aperture at 60,61 | Asset check and rendered mask inspection |
| Deterministic capture | Only frame state drives transforms, values, scroll, UI mounts and paused native animations | SOS samples at 5.4, 6.0 and 5.4 s; repeated 5.4 s captures are pixel-identical; complete preview render |

## Review

The earlier seven local tasks have separate commits; the subsequent choreography pass keeps their asset pipeline and review tooling. The contact-sheet command is `npm run sheet`, with optional seconds and `BROWSER_EXECUTABLE`. It writes numbered samples to `output/images/review-sheet.png`. The current audit reports 94 onsets, 51 large gestures and no gaps or start collisions within 0.15 seconds.

The sketchbook cut has a complete preview render with the original soundtrack. The encoded preview decodes completely. Its 41 review frames, six-sample-per-second temporal strips and scene handoffs were inspected; audible playback could not be verified in this environment. Full-speed playback with sound still needs owner review before the master. Static Compose-to-web parity was not rerun; the film uses the existing UI kit and retains its documented deviations. No Android code, soundtrack, device art, badge or logo source was changed. No master render or push is part of this work.
