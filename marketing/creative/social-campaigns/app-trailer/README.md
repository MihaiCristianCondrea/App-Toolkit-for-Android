# App Toolkit: Your toolkit

A 16:9 product trailer based on the owner's toolbox sketches. The existing mark opens, real widgets leave its mouth and the camera follows their actions. The same pieces assemble into a Pixel screen, lift back out for live demonstrations and return before the closing fan.

| | |
| --- | --- |
| Status | Sketchbook opening and connected bento preview |
| Sound | The complete supplied `soundtrack.mp3`, 29.88 seconds, unchanged speed and volume |
| Design size | 1920 x 1080 |
| Preview | 960 x 540 at 90 fps |
| Master | 2560 x 1440 at 90 fps, rendered after creative review |
| Output | `output/video/` and `output/images/`, generated and ignored by Git |

## Build

Requires Node 22+, Python with Pillow and FFmpeg/ffprobe on PATH. Use a full FFmpeg build: the metadata integrity check needs `streamhash`, which Remotion's bundled build omits. From a clean clone:

```sh
cd marketing/ui
npm ci
npm run assets
cd ../creative/social-campaigns/app-trailer/source
npm ci
npm run assets
npm run lint
npm run check
npm run preview
```

`npm run dev` opens the editable Remotion Studio project. `npm run frames` makes eight review frames and a contact sheet. `npm run render` makes the approved 2K master. All timings are seconds, and `Root.tsx` owns the frame rate.

`npm run sheet` renders the standard 19 samples at 20% scale into `output/images/review-sheet.png`. To review a move, pass seconds: `npm run sheet -- 1.7 1.85 2.0 2.4 3.0`. Set `BROWSER_EXECUTABLE` to a local Chrome executable when needed. Requires Python with Pillow.

The **App trailer preview** Actions workflow also builds the preview and publishes the review files as an artifact. Its manual **master** input renders the approved 2K version. A master is not rendered automatically on a push.

## Scene order

| Time | Picture |
| --- | --- |
| 0.00 to 1.76 | Three large background shapes frame the opening. The original app icon pops into the centre on 1.04, then anticipates opening. |
| 1.76 to 3.32 | The lid swings 135 degrees around the right lip, with its handle visible. The body moves down into its launcher pose, matching the owner's sketchbook direction. |
| 3.32 to 6.76 | Four real cards leave through separate lanes. The camera whips from Compass and Flashlight to Level and SOS while the flashlight fill changes smoothly and SOS runs its native preview pulse. |
| 6.76 to 10.08 | Cards join into a tight 2x2 bento beside "One place." Connected corners ease down to 8 px while the four outside corners remain 28 px. The camera then pushes closer. |
| 10.08 to 17.14 | The bento becomes the centre-built screen. Quick Tools pushes the device right, then Compass and Level lift into live close-ups and return. A tool-name belt advances below. |
| 17.14 to 21.98 | Measure opens in the same device as it crosses left. Four tools grow into varied hero poses. The camera takes a solo Counter close-up and follows it back into the phone. |
| 21.98 to 26.26 | Play opens with shared-axis motion. The wheel grows out and spins; its result sends the device offstage while the wheel crosses left. Chance cards arrive on the right, then return as the phone re-enters. |
| 26.26 to 29.88 | Three labelled destinations fan from the outgoing device pose. The mark, headline and original Play badge hold from 27.90 to 29.40. The gold explosion fills the frame and the final mark lands at 29.80. |

Major gestures follow detected bass onsets in `source/src/music-analysis.json`. Its BPM is an estimate, not a claim about the song. The timeline follows the supplied track rather than forcing the music onto an invented grid.

## Asset and UI ownership

- `marketing/assets/logos/icon.svg`: the original paths are split into lid and body during preparation. No replacement logo is drawn.
- `marketing/ui/src/embed.js`: every screen and isolated widget mounts the real UI kit. A fragment is cropped from that screen, not recreated in React.
- `marketing/ui/src/kit/assets.js` and `marketing/assets/shapes/`: existing Material shape masks and exported cookie faces.
- The identity's Google Sans Flex and Material Symbols files, served locally.
- `marketing/assets/device-frames/pixel_9_pro/layout`: frame, aperture, corner radius and camera mask. Geometry is read from the layout file.
- `marketing/assets/badges/google-play/en_badge_web_generic.png`: original badge with its aspect ratio and artwork preserved.
- `soundtrack.mp3`: owner-supplied source, copied byte for byte. The composition covers the entire file, with less than one video frame of rounding at its end.

Ads and the More apps footer are disabled in the film. Product backgrounds, accent roles and layout styles come from the UI kit. The campaign ground comes from the existing brand brief.

## Motion rules

1. `music-analysis.json` stays read-only. Each of its 94 onsets has a concrete cue reference or visible accent in `timeline.json`. `npm run beats` rejects missing responses, unresolved cues, absent accent targets, large gestures starting within 0.15 seconds, and camera changes away from phrase boundaries.
2. The original SVG paths remain the only logo artwork. Background shapes frame the original mark, with no opening symbol badges. The lid rotates around its right lip and shares the body's drop. Its full artwork and handle remain visible; it renders behind the launched widgets while the body remains in front.
3. Only four cards leave the box. Their paths alternate sides, begin small behind the body and carry their own soft shadows. Standalone crops and their actual UI surfaces have four 28 px corners. The connected 2x2 bento has 8 px inner corners and 28 px outer corners, eased as the gaps close. The phone handoff restores its measured native radii. Geometry guards check every flight frame and the corner endpoints; capture checks the applied DOM radii.
4. Copy and subjects have clear resting regions within the 96 px safe margin. Camera moves deliberately leave the column layout for tool close-ups. The gathered grid uses measured heights to fit the closer view. Shapes follow the camera at 40% depth and punch or turn on specific hits, with no ambient loop.
5. The screen builds outward from its centre. Tab changes use a 30 px shared-axis X move with a 0.3-second fade-through. That fade always travels with the screen.
6. Featured cards replace hidden counterparts in the phone, grow from measured bounds and return with the reverse container transform. One projection includes the device rotation, scale and beat accent for both Quick Tools and feature cards. Below-fold tools scroll into view before measurement; outgoing tabs retain their scroll position.
7. Headline words spring through masks on named notes, modulating Google Sans Flex width and weight, then reach exact rest. Captions and the moving tool-name belt stay clear of hero cards. Camera moves follow phrase boundaries; the Counter return and camera pull-back share one gesture. Fast pans, card travel and the ending use short CameraMotionBlur windows.
8. The closing foreground holds for 1.5 seconds before the mark anticipates at 29.40. The explosion uses the UI kit's `--tk-gold-container` role at 29.54, with motion blur only through 29.79 for that move. An opaque gold cover prevents text showing through from 29.70; the final mark is at exact rest on the last frame.
9. All animation comes from frame state and transforms. The capture pauses native animations, including those inside open component roots, and seeks functional previews to the supplied frame time. Flashlight brightness uses a frame-derived fill transform. Finite entrance and digit animations finish without a wall clock. Audio plays in full at unity gain without a trim, loop or fade. Ads and More apps stay disabled. Master size remains 2560 x 1440 at 90 fps, rendered by the owner after preview approval.

## Guidance and references

The opening follows the owner's `human idea/` images: background shapes, the original icon, then the chest opening. The campaign follows `marketing/STARTUP.md`, the marketing UI, motion-video, Material 3, writing-style, metadata, taste-skill and UI Unslop guidance. The official Remotion create, markup, multi-scene and motion-blur guidance informed the source structure and camera work. The installed Remotion packages remain pinned to 4.0.529. Image-generation and unrelated visual directions do not replace the owner's artwork or existing design system.

The Android Insider `develop` trailer's font and UI mounting gate and Smart Cleaner `develop`'s `LiveScreen` bridge informed the integration. Their completed campaign sources are reference material and were not changed.

## Review

See `source/FIDELITY.md`. Preparation and structural checks can pass independently of visual review. A successful render does not establish parity, readable choreography or correct timing.

- [x] TypeScript, scene continuity, unchanged audio hash, device aperture and onset coverage checks.
- [x] 20% contact sheets for the sketchbook opening, lid swing, standalone and connected corners, live previews and scene handoffs. Repeated out-of-order SOS captures match pixel for pixel.
- [x] Complete 960 x 540 preview render with the supplied soundtrack; encoded motion strips and boundary frames inspected.
- [ ] Full-speed preview and sound approval by the owner before the 2K master.

The gather shows four real widgets in a connected 2x2 arrangement and restores their exact measured positions during the phone handoff. Other tools stay inside the phone. The UI kit and Android screens were not changed. See `source/FIDELITY.md` for the review scope.
