# App Toolkit: Your toolkit

Superseded by [toolkit-trailer](../toolkit-trailer/README.md), which gathers the five custom store listings' worlds into one film on the same soundtrack. This campaign stays as a record.

A 16:9 product trailer based on the owner's toolbox sketches. The mark opens, four live widgets leave its mouth and the camera tracks them into close-ups. A connected bento becomes the real screen, then the film travels through large UI fragments before three full-height destinations fan into the closing.

| | |
| --- | --- |
| Status | Polished preview, awaiting owner playback approval |
| Sound | The complete supplied `soundtrack.mp3`, 29.88 seconds, unchanged speed and volume |
| Design size | 1920 x 1080 |
| Preview | 960 x 540 at 90 fps |
| Master | 2560 x 1440 at 90 fps, rendered after creative review |
| Output | `output/video/` and `output/images/`, generated and ignored by Git |

## Version 2

`source/src/v2/TrailerV2.tsx` is a complete rework in one file, registered as the `AppTrailerV2` composition beside the original `AppTrailer`. Every shot starts on a detected onset:

| Time | Picture |
| --- | --- |
| 0.00 to 1.76 | The sketch's corner shapes arrive one per motif note. |
| 1.76 to 3.32 | The mark pops, its lid swings about the right hinge like a chest lid (handle attached) and the box settles low as the launcher. |
| 3.32 to 6.76 | Compass, Bubble level, Flash Dimmer and SOS rise out of the box, one per note, and lock into a bento whose inner corners tighten to 8 px. |
| 6.76 to 10.08 | A teal ground grows from the bento; it moves right and grows beside "One place.". |
| 10.08 to 17.14 | The bento becomes the phone. Each Quick Tools widget lifts out of the screen on its note while its name lands. |
| 17.14 to 21.98 | A gold ground and the Measure screen grow from the Measure tab; Protractor, Unit Converter, Bill Splitter and Counter push through the left column with live values. |
| 21.98 to 26.26 | A pink ground and Play & Decide grow from their tab while the phone springs to the left half; on the right the wheel spins to Sushi, then random number, dice and coin land. |
| 26.26 to 29.88 | The three destinations fan in beside the mark, title and Google Play badge; a gold burst from the mark takes the frame. |

Render it with `COMPOSITION=AppTrailerV2 npm run preview` (or `npm run render` for the master once approved). Stills: `npx remotion still src/index.ts AppTrailerV2 out.png --frame <seconds*90>`.

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
| 3.32 to 6.76 | Four whole native cards receive tracked close-ups with a separate lower-left label. They start at the mouth and keep separate lanes. Between launches the camera opens its framing, then shows a tight arc above the chest. |
| 6.76 to 10.08 | The cards separate into rows, then join a connected bento beside 240 px whole-word type. Seams ease to 8 px. A mint stage grows behind them, and the device builds from its centre around their native screen positions without clipping the cards. |
| 10.08 to 17.14 | The camera travels along whole live Compass, Level, Flashlight and SOS cards. Each native face grows its own backdrop. The camera widens between neighbours and the cards return to a device pushed right by 240 px Quick tools type, before the measured tab push. |
| 17.14 to 21.98 | The shared-axis screen bridge gives way to whole Protractor, Converter and Bill Splitter cards, then a landscape Counter. Each owning word moves with its card; a wipe attached to that moving card supplies its native tool-colour ground. |
| 21.98 to 26.26 | The real wheel circle grows beside chapter words, fills almost the whole height for its spin, then expands back into its card on Sushi. After it settles, number, die and coin land with equal gaps and their bottoms at y 970. |
| 26.26 to 29.88 | A large Play screen carries the first frame into three devices at 86% scale in a fan with depth. The left identity column, labelled destinations and original badge hold 27.90 to 29.40. The unchanged gold explosion covers all text by 29.70; the final mark lands on 29.80. |

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
5. GroundWipe grows a kit mask logarithmically from the current subject over 0.36 seconds, behind the UI, then retains that colour as the new ground. Separate subject stages use 40% parallax. Rotation and scale accents finish on hits, with no idle loop.
6. Launch and Quick Tools close-ups fit the projected whole card, including camera roll, inside a 48 px margin and above the fixed label line at 96,980. Neighbouring Quick Tools shots widen through a framing that contains both cards. Passed cards retire by the next settle. Compass swings, Level settles, Flashlight fills and SOS pulses from each shot's onset. Cards return to their exact native phone projections.
7. The Measure tab is measured from the live navigation. The camera enters it; screens use a 30 px shared-axis X move and 0.3-second fade-through. That fade travels with the screen. The Protractor lifts from its measured screen rectangle. Later cards and their owning words share one push, with varied scale and framing. The UI renders above campaign words, and an outgoing word retires with its card. Incoming Measure titles remain hidden until their whole card is inside the frame; the previous card carries that interval.
8. The wheel uses its measured native circle as the visible container during the spin. Its clip expands into the original full card during the pull-back. The three chance cards start at least 0.1 seconds after the wheel settles. Their real aspect ratios share the screen-space baseline y 970 and equal 70 px gaps, independent of the camera.
9. Campaign type has one scale: chapter 240/750, tool label 56/650, closing title 128/750, subtitle 72/650 and phone label 36/650. Padded whole-word masks modulate width and weight. The UI pushes chapter type aside. CameraMotionBlur runs only for the fastest 0.12 seconds of a move, with six samples at 180 degrees and a maximum 18% trail over a sharp current frame.
10. The closing devices are 86% scale with restrained depth rotations, level 3 shadows while travelling and level 2 at rest. The first rendered Play aperture covers at least 30% of the frame. Labels have 50 px gaps; identity occupies x 96 to 760 with 24 px logical gaps. The foreground reaches exact rest before the 1.5-second hold. At 29.40 the mark anticipates; the original kit-gold burst explodes at 29.54, covers all text by 29.70 and settles the final mark on the last frame.
11. All motion derives from frame state. Transforms move objects; no animated layout positions, timers or random calls are introduced. The bridge pauses native animations, including open component roots, and seeks functional previews to frame time. Audio remains complete at unity gain. Ads and More apps stay off. The master remains 2560 x 1440 at 90 fps and is rendered only after owner approval.

## Guidance and references

The opening follows the owner's `human idea/` images: background shapes, the original icon, then the chest opening. The campaign follows `marketing/STARTUP.md`, the marketing UI, motion-video, Material 3, writing-style, metadata, taste-skill and UI Unslop guidance. The official Remotion create, markup, multi-scene and motion-blur guidance informed the source structure and camera work. The installed Remotion packages remain pinned to 4.0.529. Image-generation and unrelated visual directions do not replace the owner's artwork or existing design system.

The completed Android Insider `android-insider-stay-curious` source supplies the focus-camera, logarithmic shape coverage and padded kinetic-word patterns adapted here. Smart Cleaner's finished `keep-what-matters-v5.zip` supplies the UI-fragment, connected-corner and screen-bridge reference. Their sources were read only. The existing App Toolkit bridge retains its font/image gate, capture guards and frame-derived native animation.

## Review

See `source/FIDELITY.md`. Preparation and structural checks can pass independently of visual review. A successful render does not establish parity, readable choreography or correct timing.

- [x] TypeScript, scene continuity, unchanged audio hash, device aperture and all-onset coverage checks.
- [x] Iterated close-up, corner, copy-clearance, container and ending sheets; 24 evenly spaced samples for the full cut.
- [x] Complete revised 960 x 540 preview, five 6 fps pacing strips, 139 encoded samples, dense blur samples and both sides of all scene boundaries.
- [ ] Owner playback with sound before the 2K master.

The film uses the existing UI kit without changes to its screens. See `source/FIDELITY.md` for the exact validation scope and remaining review limits.

## Polish systems

Cards, phones and the toolbox use one two-layer Material elevation helper. Resting pieces use level 2; lifted and travelling pieces use level 3. A parent draws the shadow outside each crop, using that frame's actor transform.

Ground colours come from cumulative `GroundWipe` shapes behind the product. Each grows from its subject over 0.36 seconds and remains as the new ground. The chapter root no longer switches its background per shot.

Campaign type uses one scale: chapters 240/750, tool labels 56/650, closing title 128, closing subtitle 72 and phone labels 36. Close-up labels share the bottom-left anchor at 96,980. Product UI retains its native type.

Launch and Quick Tools framing fits the projected whole card within a 48 px frame margin and reserves a separate label line. Passed cards unmount by the next shot's settle, with no opacity fade. Fast camera trails use six samples at 180 degrees, with at most 18% trail opacity over a sharp current frame, for 0.12 seconds per whip.

The bento/device handoff clips the growing device shell, rather than the card artwork.

The item-by-item rendered verification times and moving-preview scope are recorded in `source/FIDELITY.md`.
