# Fidelity record

| Requirement | Implementation | Verification |
| --- | --- | --- |
| Use the actual toolbox | Original SVG paths split into lid/body layers | Preparation rejects a changed SVG structure |
| Use existing widgets | Crop `marketing/ui` screens; keep Material Web controls and themes | Rendered stills and comparison with the current UI kit |
| Full soundtrack | Audio starts at zero, unity gain, no trimming props, no loop | Source and prepared file SHA-256 equality; encoded duration check |
| Measured bento landing | Widget bounds come from the same Quick Tools DOM | Review the handoff frames at 10.08, 11.44 and 12.46 seconds |
| Real phone frame | Pixel 9 Pro skin and parsed layout, correct mask in the display aperture | 1280 x 2856 aperture at 60,61; `system:phone` supplies the 48dp status bar and 24dp system navigation inset; visual camera/corner check |
| Correct tab origins | Read the actual navbar button bounds | Review destination reveal at 17.14 and 21.98 seconds |
| Real wheel result | Three choices, 1260-degree rotation, Sushi selected | The wheel's target equation gives the second segment at 180 degrees |
| No ads | Embed mounts with `showAds:false` | Source check and encoded frame inspection |
| Deterministic capture | Frame state, settled components, no CSS animation or interactive digit clock | Render repeatability and out-of-order still review |

## Status

The first preview passed TypeScript, eight-scene rendering, audio/duration checks and metadata verification. Visual review found the missing phone system bars, a below-fold fragment peeking into the bento shot and a level-state mismatch at the handoff. The revised source corrects all three; its new render is checked before approval.\n\nSource preparation, soundtrack integrity and timeline checks are automated. TypeScript compilation and actual render results are recorded by the preview workflow. Static Compose-to-web parity, encoded visual review and music playback review must be recorded after inspecting the resulting files; they are not implied by these source checks.

The output is a creative preview until the owner approves the cut. The motion-video skill's master-render gate remains in place.
