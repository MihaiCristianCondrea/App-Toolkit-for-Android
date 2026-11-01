# Fidelity record

## Source and ownership

Every screen and card mounts the existing marketing UI kit through `LiveScreen` or `LiveCrop`. No Android code, UI-kit screen, original logo, shape, device frame, badge or soundtrack is changed in this polish pass. Ads and More apps remain disabled. The original SVG paths supply the chest body, intact lid and attached handle.

The design is 1920 x 1080 at 90 fps. The preview is 960 x 540; the approved master remains 2560 x 1440. The complete supplied soundtrack stays at unity gain, without a cut, loop or fade. Its SHA-256 is `74ef18806060b8330bff0e3efd1866aadaf3339ed068711bc465addc7de863be`.

## Shared systems

- `elevation(level, pose)` draws two Material shadow layers outside the clipped card, phone or chest. Level 2 is the resting state; level 3 is the lifted state. The filter follows the same frame-derived transform as the surface.
- `GroundWipe` grows an existing kit shape from its subject over 0.36 seconds, behind the UI, and retains the new ground after coverage. Measure origins follow the projected centre of the moving native card. The root ground does not switch by shot.
- Campaign type uses `TYPE`: chapter 240/750, tool label 56/650, closing title 128/750, subtitle 72/650 and phone label 36/650. Native product type is unchanged. Tool labels share the separate bottom-left anchor at 96,980.
- Launch and Quick Tools close-ups fit the projected whole card, including roll, within a 48 px frame margin and above the label. Adjacent Quick Tools shots widen through a two-card framing. The bento handoff keeps the cards whole while the device builds from its centre. Passed cards and their words retire by the following settle.
- Fast moves use six CameraMotionBlur samples at 180 degrees for at most 0.12 seconds. A sharp current-frame layer caps the trail at 18%. The final boom retains its original timing and kit gold.
- All motion and native widget values derive from frame time. The existing bridge pauses and seeks native animations, including open component roots. No timers, random calls or CSS transitions are added.

## Rendered item review

These times were rendered with `npm run sheet -- <times>` and visually inspected. Source inspection alone was not used to mark a visual item complete. Part 1 was committed separately as `4107dcbb0`.

| Item | Times in seconds | Observed result |
| --- | --- | --- |
| S1 elevation | 1.9, 2.8, 3.6, 4.3, 5.0, 10.9, 14.6, 28.8 | Attached shadows remain outside card and device clips. |
| S2 ground wipes | 10.9, 13.0, 13.1, 13.3, 14.5, 17.2, 19.4, 20.8, 22.0 | Originating shapes change the ground behind visible UI. |
| S3 blur cap | 5.78, 5.79, 5.80, 5.81; 19.34, 19.351, 19.362, 19.373, 19.384, 19.395, 19.406, 19.417, 19.428 | Sharp SOS and Measure subjects remain visible through the trails. |
| S4 type | 3.6, 4.3, 5.0, 10.9, 12.2, 13.4, 14.6, 28.8 | Common label anchor and type scale; closing copy fits its column. |
| S5 whole-card crop | 3.6, 4.3, 5.0, 11.0, 12.2, 13.4, 14.6 | Settled close-ups keep native titles and values whole. |
| S6 outgoing fragments | 4.3, 5.0, 12.2, 13.3, 14.6 | Passed card edges and the old label fragments are absent after settle. |
| O1 motif | 0.3, 0.9, 1.5, 1.9 | Separate spring entries and restrained camera push preserve the intended clear centre before the mark. |
| O2 lid and chest | 1.9, 2.2, 2.5, 2.8, 3.1 | Lid retains its colours, handle and right-lip swing; body has elevation. |
| O3 launch arc | 6.6, 6.9, 7.3, 8.0 | Four separate lanes gather into rows before joining; the wide arc stays above the visible chest. |
| O4 SOS launch | 5.8, 5.86, 6.0 | SOS stays distinct from the body and readable during the pull-back. |
| Q1 live actions and crop | 10.1, 10.6, 11.0, 11.45, 12.2, 12.95, 13.3, 13.4, 14.0, 14.5, 14.6 | Compass swings, bubble settles, brightness fills and SOS pulses; labels stay separate. |
| Q2 chapter push | 15.45, 15.5, 15.66, 15.8, 16.1, 16.4 | One-line chapter type clears the returning phone on its right. |
| M1 linked card and word | 17.2, 17.8, 18.62, 18.7, 19.36, 19.4, 19.5, 20.9, 21.3 | Each word moves with its owning card; outgoing words retire with the card instead of trailing at the edge. Incoming titles enter only once the whole card is inside the frame. |
| M2 tool-colour wipes | 17.2, 17.8, 18.7, 19.4, 19.5, 20.8, 20.9, 22.0 | Cream, gold, rose and lavender originate behind their native Measure cards. |
| P1 chance row | 24.6, 25.0, 25.4, 25.8, 26.1 | Results start after the settled wheel and land with equal gaps and a shared y 970 bottom. |
| C1 closing grid | 27.0, 27.5, 28.8 | Left identity column, separate destination labels and a large fan hold without copy overlap. |
| C2 closing handoff | 26.26, 26.4, 26.6 | Play fills the first frame and carries the handoff into the fan. A rendered DOM measurement at frame 2363 records 30.23% visible screen coverage. |
| C3 opaque ending | 29.7, 29.85 | The gold burst covers all copy before the final mark. |
| Camera edge follow-up | 3.6, 3.95, 4.3, 4.5, 5.0, 5.08, 5.8, 6.0, 11.45, 11.56, 12.95, 13.03, 14.0, 14.08, 15.33, 15.8, 16.1 | Active cards stay visible during camera handoffs; paired Quick Tools framing avoids edge fragments and the returning phone builds around its cards. |
| Bento handoff | 8.98, 9.2, 9.5, 9.8, 10.1 | Cards stay whole while the native device grows around them. |

## Whole-film verification

The first full preview review exposed returning old launch cards and clipped bento headers that the isolated stills had missed. The launch retirement rule, paired Quick Tools framing and whole-bento device build correct those cases. Dense Measure samples also caught an incoming title crossing the frame edge. Whole-card entry visibility now protects that title while the previous card remains visible, and the ground wipe follows its moving measured centre. The final preview and pacing review include those corrections.

The final 960 x 540, 90 fps preview was rendered with `npm run preview`. Visual inspection covered 139 encoded samples, both sides of every scene boundary, 24 evenly spaced midpoints, five FFmpeg strips at 6 fps and every frame around 5.74 to 5.86 and 19.30 to 19.48. The strips keep timestamps above the image and include the final mark at 29.833. The actual rounded closing sequence boundary was checked at 26.244 and 26.256, in addition to the requested closing samples. The inspected frames retain readable subjects and clear campaign copy, with no incoming Measure title crossing the frame edge.

Full video decoding and opaque gold coverage pass. Decoded soundtrack correlation is 0.994206, gain is 0.998993 and alignment is 341 samples at 8 kHz, within the codec-delay tolerance. Lint, check and beats pass, including all 94 onsets and 33 separated large gestures. Flight intersection, corner endpoint and rendered closing-coverage guards remain active.

The empty centre before 1.76 is required by the owner's sketch. The closing foreground holds 27.90 to 29.40. Audible playback cannot be independently judged through the available capture tools; owner playback with sound remains necessary before approving a master. Static Compose-to-web parity was not rerun. This task renders no master.

## Guidance

The owner's available idea1, idea2 and idea3 images guide the opening; idea4 was absent locally. The finished Android Insider film and its source were reviewed read-only for elevation, large type, padded word masks, focus-camera moves and logarithmic wipe coverage. Smart Cleaner's finished UI-fragment and connected-corner patterns were also read-only references.

Loaded guidance: motion-video and Pixel references; official Remotion create, markup, multi-scene and motion-blur; marketing UI and Material 3; taste-skill and UI Unslop; writing-style, metadata, Ponytail and build validation. Existing packages remain pinned to Remotion 4.0.529.
