# App Toolkit: Your toolkit

A 16:9 product trailer based on the owner's toolbox sketches. The existing mark opens, live UI widgets jump out with rounded corners, and the same pieces settle into the app's bento layout. A Pixel frame and the rest of the UI form around them.

| | |
| --- | --- |
| Status | Draft cut for visual review |
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

The **App trailer preview** Actions workflow also builds the preview and publishes the review files as an artifact. Its manual **master** input renders the approved 2K version. A master is not rendered automatically on a push.

## Scene order

| Time | Picture |
| --- | --- |
| 0.00 to 1.76 | The existing toolbox mark arrives among Material shapes. |
| 1.76 to 3.32 | The original lid and handle open. |
| 3.32 to 6.76 | Six real Quick Tools widgets jump out. "Many tools." |
| 6.76 to 10.08 | The box leaves. Widgets gather into the real screen geometry. "One place." |
| 10.08 to 17.14 | Pull back to the Pixel; the shell appears around the same cards. Compass turns, level settles. |
| 17.14 to 21.98 | Measure opens from its actual tab. Ruler and protractor are visible; the counter advances. |
| 21.98 to 26.26 | Play & Decide opens. The decision wheel chooses Sushi; random number, coin and dice remain real UI. |
| 26.26 to 29.88 | Three destinations fan out. "App Toolkit. Your toolkit." Original Google Play badge. |

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

1. Objects cause transitions: lid to widgets, widgets to bento, bento to device, tabs to destination.
2. One continuous coordinate system handles the toolbox-to-phone sequence. Landing targets come from live DOM measurements.
3. Floating widgets have full 28dp corners; their actual bento corners return as they land.
4. Gestures settle before the next move. Springs land on exact poses; there is no camera noise.
5. The actual UI state is a function of the current frame. Wall-clock UI animations are disabled during capture.
6. Type is sparse and holds in clear space. The soundtrack plays at unity gain with no trim, loop or added fade.

## Guidance and references

The campaign follows `marketing/STARTUP.md`, the marketing UI, motion-video, Material 3, writing-style, metadata, taste-skill and UI Unslop guidance. The official Remotion create, markup, composition and rendering guidance informed the source structure. Image-generation and unrelated visual directions do not replace the owner's artwork or existing design system.

The Android Insider `develop` trailer's font and UI mounting gate and Smart Cleaner `develop`'s `LiveScreen` bridge informed the integration. Their completed campaign sources are reference material and were not changed.

## Review

See `source/FIDELITY.md`. Preparation and structural checks can pass independently of visual review. A successful render does not establish parity, readable choreography or correct timing.

- [ ] Review all eight stills and the full preview against the owner's sketch.
- [ ] Check the widget-to-screen handoff and phone aperture at transition boundaries.
- [ ] Check text, clip edges, typeface, symbols and image loading in the encoded preview.
- [ ] Listen to the full preview and verify the music's natural ending.
- [ ] Approve the cut before the 2K master and platform captions.
