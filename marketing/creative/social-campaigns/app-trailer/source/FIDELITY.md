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

## Review of the second draft

Reviewed the changes in `c54621ac` and `fd2d44cb`, then rendered the corrected source at `d7e58fe9` at 960 x 540, 90 fps. The full soundtrack remains unchanged.

The review found and fixed:

- Reused Lit nodes retained hidden-widget styles, and older asynchronous mounts could overwrite newer frame state. Each frame state now owns a fresh mount host.
- The Counter fragment included the shell navigation because it was below the fold. Crops now scroll their actual content viewport before measuring the widget.
- Measure heroes could cross the heading, and the moving phone crossed the entering title. The phone now clears first; the smaller heroes have faster entrances, clear holds and fading departures.
- The chance cards arrived too late to hold their results. Their entrances now begin earlier on detected onsets, preserving the existing measured-width spacing.
- The closing stack showed Measure in front of the outgoing Play screen. Play now stays foremost at the handoff, final values carry through, and the fan starts with zero velocity.

Verification: TypeScript and structural checks pass. The complete 2690-frame preview rendered successfully. All three six-frames-per-second pacing strips (180 frames) were inspected, with additional stills around the bento handoff, both tab changes and the closing handoff. The chip row and final labels remain separated. Counter is fully visible without the shell navigation in its crop.

An encoded regression check sampled 76 frames across the compass animation: every sample retained the coloured widgets (minimum 14,469 coloured pixels in the fixed phone region). Source/prepared soundtrack hashes match. The full decoded source fits within the encoded audio after its 42.625ms codec delay; waveform correlation is 0.9942. Metadata tagging verifies that the media stream packets remain unchanged.

## Third cut

The original mark now follows the opening symbol motif, anticipates and overshoots, splits, and leaves its body at the bottom as the launcher. Its original lid exits above frame. All six real widgets plus Material Colors stay in the gather. The tall bento unfolds into landscape rows for readability, then restores the measured phone coordinates. This is a campaign arrangement of real crops, not a change to the UI kit.

`timeline.json` records all 94 detected onsets, their phrase, visual action, editorial accent strength and background target. The structural check compares the ordered onset times with the read-only analysis. Assigned shapes ratchet, punch and squash on these notes with no idle rotation; the backdrop follows 22% of camera travel. Variable-font words reveal through individual masks on named detected notes in `events.type`; a check rejects word cues outside the analysis. The full Quick Tools caption travels out as Measure enters, Counter crosses into Play, and the Play device carries into the closing fan. Outgoing Play cards lift away during the fan. Fast windows use three CameraMotionBlur samples; other frames bypass that wrapper because its zero-shutter path fails at frame zero in 4.0.529.

The closing foreground settles by 28.26 seconds and holds for 1.62 seconds. Only the background answers the final notes. The full soundtrack and original artwork remain unchanged. No Android code, UI-kit screens or assets were edited.

Verification for this cut:

- `npm ci` and asset preparation in both the UI kit and trailer source.
- TypeScript, eight contiguous scenes, unchanged soundtrack hashes, original device aperture, ordered onset coverage and minimum closing hold checks.
- Quarter-scale stills at 0.3, 0.9, 1.8, 2.4, 3.6, 5.2, 8.0, 9.5, 12.0, 15.8, 18.9, 23.5, 25.9 and 28.8 seconds, plus the frame before, at and after every scene boundary, combined into `output/images/third-cut/contact-sheet.png`.
- A complete 2690-frame preview at 960 x 540 and 90 fps. Encoded review strips sample the full film at six frames per second. Full video/audio decoding and metadata stream-preservation checks are separate from creative approval.

## Remaining approval and limits

Full-speed playback and listening approval remain open. The browser automation host was unavailable; the native Chrome window remained minimized and the available computer-use API could not obtain its state. No auditory review is claimed. Encoded frame strips, stills and numerical audio checks provide the current review evidence. The encoded audio covers the full decoded source after a 341-sample delay at 8 kHz, with waveform correlation 0.9942. Static Compose-to-web parity was not rerun, and the existing kit deviations remain. The owner renders the 2K master after approving this preview. No master render or push was performed.
