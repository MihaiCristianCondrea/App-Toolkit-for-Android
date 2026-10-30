# App Toolkit: Your toolkit

A 16:9 product trailer based on the owner's toolbox sketches. The existing mark opens, live UI widgets jump out with rounded corners, and the same pieces settle into the app's bento layout. A Pixel frame and the rest of the UI form around them.

| | |
| --- | --- |
| Status | Third cut, music-led choreography preview |
| Sound | The complete supplied `soundtrack.mp3`, 29.88 seconds, unchanged speed and volume |
| Design size | 1920 x 1080 |
| Preview | 960 x 540 at 90 fps |
| Master | 2560 x 1440 at 90 fps, rendered after creative review |
| Output | `output/video/` and `output/images/`, generated and ignored by Git |

## Build

Requires Node 22+, Python with Pillow and FFmpeg/ffprobe on PATH. From a clean clone:

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
| 0.00 to 1.76 | Six Material Symbols answer five motif notes, Water and Breathe sharing the last note. |
| 1.76 to 3.32 | The original mark pops and opens as a chest. The lid folds over its seam, mirrors behind the body and shares the body's drop. |
| 3.32 to 6.76 | Compass, Bubble Level, Flash Dimmer and SOS leave the mouth at scale 0.3 and fan through four separate lanes. |
| 6.76 to 10.08 | The four crops gather into a large right-column 2x2 bento beside the two-line "One place." A single kit shape grows behind them. |
| 10.08 to 17.14 | The real screen grows from its centre and the Pixel frame forms around the cards. The measured landing restores the real layout; Quick Tools copy pushes the device right. |
| 17.14 to 21.98 | A shared-axis X tab change opens Measure. Protractor, Unit Converter, Bill Splitter and Counter grow from measured phone rectangles and return to them. |
| 21.98 to 26.26 | Play opens with the same tab motion. The wheel grows out, chooses Sushi, and the chance cards resolve to 73, 5 and Heads before returning. |
| 26.26 to 29.88 | Three labelled destinations fan into the right column. The mark, headline and original Play badge hold from 27.90 to 29.40. The mark squashes, a gold burst fills the frame on 29.54, and the centred mark lands on 29.80. |

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
2. The original SVG paths remain the only logo artwork. The lid's face folds through scaleY 1, 0 and -1, is shaded as its inside and stays on the body's drop. The handle is clipped from the mirrored inside so it cannot hang between lid and body.
3. Only four cards leave the box. Their paths alternate sides, begin small behind the body and carry their own soft shadows. A geometry guard checks every frame from launch through assembly using the measured card rectangles.
4. Copy uses the left or right column, with the subject in the other. Resting content stays within the 96 px safe margin. A single shape stages each subject; there is no ambient rotation.
5. The screen builds outward from its centre. Tab changes use a 30 px shared-axis X move with a 0.3-second fade-through. That fade always travels with the screen.
6. Featured cards replace hidden counterparts in the phone, grow from live measured bounds and return with the reverse container transform. The phone brings below-fold tools into its viewport before measuring. The outgoing tab retains its scroll position.
7. Headline words reveal through masks on named notes, modulating Google Sans Flex width and weight. Chips and captions hold clear of the device and cards. Camera pushes are separate from card and chapter starts.
8. The closing foreground holds for 1.5 seconds before the mark anticipates at 29.40. The explosion uses the UI kit's `--tk-gold-container` role at 29.54, with motion blur only through 29.79 for that move. An opaque gold cover prevents text showing through from 29.70; the final mark is at exact rest on the last frame.
9. All animation comes from frame state and transforms. Audio plays in full at unity gain without a trim, loop or fade. Ads and More apps stay disabled. Master size remains 2560 x 1440 at 90 fps, rendered by the owner after preview approval.

## Guidance and references

The campaign follows `marketing/STARTUP.md`, the marketing UI, motion-video, Material 3, writing-style, metadata, taste-skill and UI Unslop guidance. The official Remotion create, markup, composition and rendering guidance informed the source structure. Image-generation and unrelated visual directions do not replace the owner's artwork or existing design system.

The Android Insider `develop` trailer's font and UI mounting gate and Smart Cleaner `develop`'s `LiveScreen` bridge informed the integration. Their completed campaign sources are reference material and were not changed.

## Review

See `source/FIDELITY.md`. Preparation and structural checks can pass independently of visual review. A successful render does not establish parity, readable choreography or correct timing.

- [x] TypeScript, scene continuity, unchanged audio hash, device aperture and onset coverage checks.
- [x] 20% contact sheets for the lid, each launch, column layouts, chapter transitions, beat accents and the final burst.
- [ ] Full-speed preview and sound approval by the owner before the 2K master.

The gather shows four real widgets in a 2x2 arrangement and restores their exact measured positions during the phone handoff. Other tools stay inside the phone. The UI kit and Android screens were not changed. See `source/FIDELITY.md` for the review scope.
