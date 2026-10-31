# Fidelity record

| Requirement | Implementation | Verification |
| --- | --- | --- |
| Original assets | Original logo paths, shape masks, Pixel frame, Play badge and soundtrack | Source inspection, preparation and unchanged soundtrack SHA-256 |
| Sketchbook opening | Three cropped corner accents, clear centre until 1.76, large mark, intact right-hinge lid and attached handle | Opening samples compared with idea1, idea2 and idea3; idea4 was not present locally |
| Four mouth launches | Scale 0.3 start at the body's top edge, separate flight lanes, ordered stacking and contact shadows | Every-frame measured box intersection guard plus launch sheets |
| Bento corners | Native surfaces, crop masks and shadows share 28 px standalone corners, 8 px connected seams, then measured screen radii | Every-frame flight guard, corner endpoint assertions and runtime DOM-radius checks |
| Real UI | Every fragment and screen comes from LiveCrop or LiveScreen; no recreated controls or altered UI-kit screens | Source inspection and rendered sheets |
| Shot list | 28 contiguous shots alternate face close-ups, card context, bento and device framing | Timeline shot assertions and 24 evenly spaced midpoint samples |
| Subject transitions | Measured focus camera, logarithmic shape-to-ground wipes, screen push-through and travelling card containers | Transition sheets and encoded frames on both sides of every scene boundary |
| Quick Tools | Large native Compass, Level, Flashlight and SOS crops change heading, tilt, brightness and pulse | Native state and animation capture; close-up samples |
| Type | Whole-word masks with padded descenders and variable width/weight; no clipped name belt | Full-cut and targeted chapter samples; Protractor and Counter copy clearances corrected |
| Measure | Measured tab push-through, 30 px shared-axis screen bridge, Protractor container lift and different framing for Converter, Splitter and Counter | Targeted tab and feature sheets |
| Wheel | Its measured native circle fills nearly the whole height, spins to Sushi and expands into the original card | Wheel samples at 22.3, 22.5, 23.2, 24.4 and 25.9 s |
| Chance row | Three real cards retain native aspect ratios and land at one baseline on separate onsets | Result and landing samples |
| Closing | Three 86%-scale devices with depth, labelled destinations, original identity and badge | Closing sheet and minimum 1.5-second hold assertion |
| Gold ending | Original kit-gold burst, opaque cover from 29.70, final mark at exact rest | Ending samples through the last frame |
| Beat coverage | 94 referenced onsets, 33 separated large gestures, one named shot per phrase | npm run check and npm run beats, including rejection fixtures for gaps and collisions |
| Audio and frame rate | Complete unchanged source, unity gain, 29.88 s, 90 fps; preview 960 x 540 | Source hash, encoded 90 fps/duration/audio checks, decoded correlation 0.994206 and gain 0.998993 |
| Determinism | Frame-derived transforms and UI values, finite accents, paused and sought native animations | Existing mount/capture guards retained; complete current preview decodes successfully |

## Review

The opening was reviewed before the rest of the shot-list cut. Subsequent sheets caught and corrected crossing gather paths, a mismatched phone handoff, Protractor and Counter copy contact, and the undersized spinning wheel. The whole-film review uses 24 equally spaced midpoint samples. The empty centre before the mark arrives at 1.76 is required by the owner's sketch; the closing foreground holds 27.90 to 29.40. These are deliberate exceptions to continuous product occupancy and camera movement. Chapter type follows the owner's requested fast phrase gestures; the final identity keeps the longer reading hold.

The current 960 x 540 preview is rendered with the full soundtrack. Review covered 72 encoded samples, including 24 evenly spaced midpoint frames, all scene boundaries, standard feature samples and the ending. Five FFmpeg tile strips sample the film at 6 fps. The decoded video, opaque gold cover and audio integrity checks pass. Additional launch samples corrected the camera crossing empty space; swing samples corrected the handle leaving the top edge. The camera now returns to the mouth before each launch, and pulls wide before the lid opens. Audible playback cannot be independently judged through the available capture tools. Owner playback with sound remains necessary before approving a master. Static Compose-to-web parity was not rerun; the film uses the existing kit and retains its documented deviations. No Android code, UI-kit screen, original asset or soundtrack was changed. No master is rendered in this task.
