# App Toolkit: Your toolkit

A 16:9 product trailer based on the owner's toolbox sketches. The mark opens, four live widgets leave its mouth and the camera tracks them into close-ups. A connected bento becomes the real screen, then the film travels through large UI fragments before three full-height destinations fan into the closing.

| | |
| --- | --- |
| Status | Shot-list preview, awaiting playback approval |
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
npm run beats
npm run preview
```

`npm run dev` opens the editable Remotion Studio project. `npm run frames` makes eight review frames and a contact sheet. `npm run render` makes the approved 2K master. All timings are seconds, and `Root.tsx` owns the frame rate.

`npm run sheet` renders the standard 19 samples at 20% scale into `output/images/review-sheet.png`. To review a move, pass seconds: `npm run sheet -- 1.7 1.85 2.0 2.4 3.0`. Set `BROWSER_EXECUTABLE` to a local Chrome executable when needed. Requires Python with Pillow. The whole-cut review uses 24 evenly spaced midpoint samples, `(i + 0.5) * 29.88 / 24`, passed as seconds. The encoded preview is also sampled at 6 fps with FFmpeg for pacing and scene-boundary review.

The **App trailer preview** Actions workflow also builds the preview and publishes the review files as an artifact. Its manual **master** input renders the approved 2K version. A master is not rendered automatically on a push.

## Scene order

| Time | Picture |
| --- | --- |
| 0.00 to 1.76 | Three cropped corner accents spring in on the motif. The centre stays clear until the original mark arrives on 1.76. |
| 1.76 to 3.32 | The large mark pops. On 2.32 its intact lid swings around the right lip, handle attached, while the camera pulls wide for the swing, then pans to the lower launcher. |
| 3.32 to 6.76 | Compass and Flashlight receive large face close-ups; Level and SOS get wider context. All four start at the mouth and keep separate lanes. The camera then pulls wide around the chest. |
| 6.76 to 10.08 | The four cards join a connected bento beside 280 px whole-word type. Connected corners ease to 8 px. A close push and shape wipe carry the same objects into their native screen positions. |
| 10.08 to 17.14 | The camera travels along a rail of live Compass, Level, Flashlight and SOS crops. The four cards return to a large device beside 260 px Quick tools type, then the camera pushes into the measured Measure tab. |
| 17.14 to 21.98 | The shared-axis screen bridge gives way to a large Protractor, an extreme Converter crop, Bill Splitter and a landscape Counter. Originating shape wipes connect their different scales and angles. |
| 21.98 to 26.26 | The real wheel circle grows beside giant chapter words, fills almost the whole height for its spin, then expands back into its card on Sushi. Number, die and coin land in a row on consecutive onsets. |
| 26.26 to 29.88 | Three devices at 86% scale enter as a fan with depth. Labelled destinations, identity and the original badge hold 27.90 to 29.40. The kit-gold explosion covers the frame and the final mark lands on 29.80. |

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

1. `music-analysis.json` stays read-only. All 94 onsets reference visible gestures, native value changes or short accents in `timeline.json`. The beat audit rejects gaps, unresolved or unused references, absent targets, large gesture starts closer than 0.15 seconds, and camera changes outside phrase boundaries.
2. `events.shots` is the shot list. Each phrase names its framing, composition, transition and settle. Consecutive shots have different compositions. The camera alternates measured face close-ups, card context, the wide bento and device views. The opening's empty centre before 1.76 and the final identity hold are deliberate exceptions to continuous product occupancy and movement.
3. The original logo paths remain the only mark artwork. Three cropped background accents frame it, with no symbol badges. The intact lid rotates around the right lip and shares the body's drop. The body renders in front of launching cards; the lid stays behind them.
4. Four cards leave the mouth at scale 0.3 with shared-transform contact shadows. Every flight frame is checked for intersections using native measured boxes. Standalone surfaces and their crop/shadow bounds have 28 px corners. Only connected seams ease to 8 px in the 2x2 bento; the phone handoff restores native radii.
5. Shape wipes grow logarithmically from the outgoing subject until every corner is covered, then travel offscreen over the next shot's ground. Shapes follow the camera at 40% depth. Rotation and scale accents finish on individual hits, with no idle loop.
6. The Quick Tools rail uses the native widget faces as camera targets. Compass swings, Level settles, Flashlight brightness fills, and SOS uses its native animation on the frame clock. Cards return to the exact phone projection before their native counterparts reappear. Whole-word tool names replace the clipped ticker.
7. The Measure tab is measured from the live navigation. The camera enters it; screens use a 30 px shared-axis X move and 0.3-second fade-through. That fade travels with the screen. The Protractor lifts from its measured screen rectangle. Later cards travel through originating shape wipes, with varied scale, angle and framing.
8. The wheel uses its measured native circle as the visible container during the spin. Its clip expands into the original full card during the pull-back. The three chance cards retain their real aspect ratios and share a landing baseline.
9. Chapter words are 220 to 300 px, spring through padded whole-word masks and modulate Google Sans Flex width and weight. The UI pushes the chapter type away. Fast tracking, whip moves, the spinning wheel and the final hit use bounded CameraMotionBlur windows. The longer hold belongs to the closing identity.
10. The closing devices are 86% scale with restrained depth rotations. The foreground reaches exact rest before the 1.5-second hold. At 29.40 the mark anticipates; the original kit-gold burst explodes at 29.54, covers all text by 29.70 and settles the final mark on the last frame.
11. All motion derives from frame state. Transforms move objects; no animated layout positions, timers or random calls are introduced. The bridge pauses native animations, including open component roots, and seeks functional previews to frame time. Audio remains complete at unity gain. Ads and More apps stay off. The master remains 2560 x 1440 at 90 fps and is rendered only after owner approval.

## Guidance and references

The opening follows the owner's `human idea/` images: background shapes, the original icon, then the chest opening. The campaign follows `marketing/STARTUP.md`, the marketing UI, motion-video, Material 3, writing-style, metadata, taste-skill and UI Unslop guidance. The official Remotion create, markup, multi-scene and motion-blur guidance informed the source structure and camera work. The installed Remotion packages remain pinned to 4.0.529. Image-generation and unrelated visual directions do not replace the owner's artwork or existing design system.

The completed Android Insider `android-insider-stay-curious` source supplies the focus-camera, logarithmic shape coverage and padded kinetic-word patterns adapted here. Smart Cleaner's finished `keep-what-matters-v5.zip` supplies the UI-fragment, connected-corner and screen-bridge reference. Their sources were read only. The existing App Toolkit bridge retains its font/image gate, capture guards and frame-derived native animation.

## Review

See `source/FIDELITY.md`. Preparation and structural checks can pass independently of visual review. A successful render does not establish parity, readable choreography or correct timing.

- [x] TypeScript, scene continuity, unchanged audio hash, device aperture and all-onset coverage checks.
- [x] Iterated close-up, corner, copy-clearance, container and ending sheets; 24 evenly spaced samples for the full cut.
- [x] Complete current 960 x 540 preview, five 6 fps pacing strips, 72 encoded samples and all scene boundaries.
- [ ] Owner playback with sound before the 2K master.

The film uses the existing UI kit without changes to its screens. See `source/FIDELITY.md` for the exact validation scope and remaining review limits.
