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

## Remaining approval and limits

Full-speed creative playback and listening approval remain with the owner before the 2K master. The review used encoded pacing strips, boundary stills and numerical audio checks; it does not claim an auditory review. Static Compose-to-web parity was not rerun; the film mounts the current UI kit directly. The output remains a creative preview under the motion-video skill's master-render gate.
