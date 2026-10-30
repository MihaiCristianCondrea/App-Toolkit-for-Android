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
| 0.00 to 1.76 | Oversized Material Symbols land in the kit shapes and make room for each other on the opening motif. Water and Breathe share the fifth note. |
| 1.76 to 3.32 | The original mark anticipates, overshoots and splits. The lid exits above frame; the body with the tools lands at the bottom. |
| 3.32 to 6.76 | Six real widgets burst from the body on notes. Material Colors follows. No burst headline. |
| 6.76 to 10.08 | All seven live cards gather into unfolded bento rows filling the frame under "One place." |
| 10.08 to 17.14 | The phone rises into the folding cards. They land at the screen's measured coordinates; "Quick tools." pushes the device right. Sensor values and chips follow notes. |
| 17.14 to 21.98 | Measure reveals from its tab. Protractor, Unit Converter, Bill Splitter and Counter relay across the frame. Values change with the music. |
| 21.98 to 26.26 | Counter exits across the boundary as Play opens. The wheel chooses Sushi, then number, die and coin resolve. |
| 26.26 to 29.88 | Play continues into the destination fan while the outgoing cards lift away. Mark, headline and badge settle by 28.26 and hold for 1.62 seconds. |

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

1. The detected notes live in read-only `music-analysis.json`. `timeline.json` maps each to a visible action and background target. Strength is an editorial accent weight, not a measured loudness value.
2. The motif introduces six Material Symbols in the existing kit masks before the mark. The final note introduces Water and Breathe together because there are five opening onsets.
3. The mark uses only the prepared original SVG layers. Its lid leaves above frame and the body stays at the bottom as the launcher until the gather.
4. Seven real crops retain their screen order and row seams while unfolding across the landscape canvas. Their final landing coordinates come from live DOM measurements. The phone's viewport clips the below-fold cards as the group folds back into the actual scroll layout.
5. Background shapes ratchet their rotation on assigned notes, punch scale and squash briefly on landing. They move at 22% of the foreground camera travel, with exact rest between hits. No idle spin.
6. Camera phrases, gesture triggers, hero relays and chip notes come from timeline events. `CameraMotionBlur` samples only the marked fast windows; settled frames bypass the blur wrapper.
7. Type reveals word by word through masks, changing Google Sans Flex width and weight. It pushes the phone or card group aside. Card exits travel beyond the frame instead of fading in place; Counter and the Play device carry across chapter boundaries.
8. The UI remains frame-derived, with ads and More apps disabled. Animated placement uses transforms. The ending's foreground holds still from 28.26 through 29.88 while the last notes animate only the shapes behind it.
9. The soundtrack plays in full at unity gain, without a trim, loop or fade. The preview remains 960 x 540 at 90 fps; the owner renders the 2K master after approval.

## Guidance and references

The campaign follows `marketing/STARTUP.md`, the marketing UI, motion-video, Material 3, writing-style, metadata, taste-skill and UI Unslop guidance. The official Remotion create, markup, composition and rendering guidance informed the source structure. Image-generation and unrelated visual directions do not replace the owner's artwork or existing design system.

The Android Insider `develop` trailer's font and UI mounting gate and Smart Cleaner `develop`'s `LiveScreen` bridge informed the integration. Their completed campaign sources are reference material and were not changed.

## Review

See `source/FIDELITY.md`. Preparation and structural checks can pass independently of visual review. A successful render does not establish parity, readable choreography or correct timing.

- [x] TypeScript, scene continuity, unchanged audio hash, device aperture and onset coverage checks.
- [x] Quarter-scale stills at the requested sample times and immediately around all seven scene boundaries.
- [ ] Full-speed preview and sound approval by the owner before the 2K master.

The gather deliberately unfolds the tall phone bento into landscape rows so all six widgets and Material Colors are readable together. It restores the exact measured positions during the phone handoff. The UI kit and Android screens were not changed. See `source/FIDELITY.md` for the review scope.
