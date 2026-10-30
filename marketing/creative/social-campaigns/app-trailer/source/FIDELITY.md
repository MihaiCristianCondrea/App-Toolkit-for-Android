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

The corrected preview at `b98bba9b` passed TypeScript, the full 2690-frame render, source soundtrack integrity, encoded audio/duration checks and metadata verification. All eight rendered stills were inspected. The phone status bar clears the camera and search field, the bento shot contains four complete cards, and the level starts tilted consistently before settling in the phone.

Review output: [preview MP4, eight frames and contact sheet](https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android/actions/runs/41085515647/artifacts/12991771756).

Full-speed creative approval, listening to the encoded music, and exact handoff boundary review remain before the master render. Static Compose-to-web parity was not rerun in this campaign; screens and fragments use the current UI kit directly. These limits are not implied to pass by the source checks.

The output is a creative preview until the owner approves the cut. The motion-video skill's master-render gate remains in place.
