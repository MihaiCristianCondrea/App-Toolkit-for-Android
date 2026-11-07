# Speaker Water Eject listing source

The English package has five 2160 x 3840 phone screenshots, four 3840 x 2160 tablet screenshots, four 3840 x 2160 desktop screenshots, a 1024 x 500 feature graphic and a 512 x 512 official icon. Rendered files are in ../listing/en-US; the latest contact sheets and verification report are in ../review.

## Render and verify

From this folder:

```text
npm ci
npm run lint
npm run probe -- water-eject water-running water-stop unit-converter quick-tools level   # when a screen's UI changes
npm run solve                                                                            # or: npm run solve -- 3
npm run verify -- en-US
npm run render -- en-US
npm run metadata
```

Run the Play validator from marketing/creative/store/google-play with `node scripts/validate.mjs`. Rendering writes into a staging folder and replaces the published graphics only once every image succeeded. Metadata tagging runs after rendering, requires all 15 outputs and verifies decoded pixels are unchanged.

## Composition system

The phone screenshots are solved, not placed. Everything lives in src/composition and is plain TypeScript, so Node runs the solver and verifier on the same code the render uses.

- `geometry.ts` projects the Material-Next 3D Pixel exactly as its device frame and WebGL renderer do (yaw, then pitch about the phone's centre, a camera `perspective` px in front of it; roll and scale in the picture plane), and measures a pose: projected silhouette, visible box and area, area centroid, bottom-edge thickness and angle, speaker points, keystone, and each screen's UI projected onto the image. It also holds the golden guides, the Fibonacci spacing scale (13, 21, 34, 55, 89, 144 px at 1080 x 1920) and the caption blocks.
- `ui.json` is each screen's UI measured head on by `npm run probe` (src/UiProbe.tsx), from the selectors in `uiSelectors.ts`: critical UI must stay legible; secondary UI may be cropped on purpose.
- `shots.ts` states each screenshot's art direction as its own targets, camera search and preferences. `npm run solve` searches them, rejects candidates that break a hard rule and keeps the lowest preference score in `layout.json`. The two-phone screenshot is searched as a pair.
- `effects.ts` places the hero's sound arcs and bubbles and every screenshot's MaterialShapes; `ribbons.ts` is the background's ribbon geometry, shared with src/Glass.tsx.
- `validate.ts` checks a composition; `compose.ts` assembles one for both the render (src/Shot.tsx) and the verifier. A hard failure stops the render.
- `npm run verify` renders each screenshot clean, without captions and with the debug overlay, measures caption contrast and background interference on the composited pixels and the contrast-weighted balance, and writes contact sheets and report.json to output/verify.

Hard rules (a render fails): a phone's visible silhouette stays 34 px inside its screenshot's sides and never crosses a divider; phones, shapes and effects keep 34 px from a caption's text block; critical UI stays inside 0.05 W side margins and 89 px from the top and bottom edges, clear of captions, and is never covered by a phone in front; a shape stays inside its screenshot (except at the strip's outer ends), clear of the glass ribbons, shows at least half of itself, and touches a phone only when placed behind it on purpose; caption ink keeps at least 4.5:1 contrast against the real background under 99% of its glyphs; the background departs from the plain field under no more than 1% of a caption's text zone; and each screenshot's own rules (the hero runs off the top with its speaker edge facing the camera and centred; the close crop runs off the bottom; the Stop shot shows the whole phone).

Preferences (scored and reported when outside tolerance): each screenshot's targets on golden lines and sizes, camera angles near its art direction, the pair's balance, overlap and stagger, and a visual centroid within 0.05 W of the centre.

Intentional exceptions are reported, not flagged: phones cropped at the top or bottom edge, secondary UI cropped, one phone overlapping another clear of its critical UI, and a large shape behind a phone.

The ribbons fade behind every caption's text block through a stacked soft mask (no blur filter, which seams on a strip this wide), keeping 10% of their opacity, so the panorama stays continuous and the letters sit on a quiet field without an opaque box.

## Design and fidelity

Water Eject follows WaterEjectTool.kt: a 200dp visual, 165 Hz when idle, remaining seconds while running, native guidance and note, full-width Start 30-second cycle or Stop cycle. Capture state uses waterCapture to hold the countdown without audio. Material-Next 0.2.0 must actually be installed, not just declared in package.json. The capture refuses to substitute a CSS phone outline when the 3D picture is unavailable.

## Publication gate

The creative package targets application version 26.11.02. The public Play page retrieved November 7 showed older content and did not establish that version's availability. Confirm its production rollout for the intended audience in Play Console before upload. No device was connected for a side-by-side native capture; fidelity was checked against Compose and Android strings.

## Validation results

See ../review/verification.json. On the final renders: no hard failures; caption contrast at least 12.2:1 under 99% of glyphs (pill text 9.4:1); background interference 0.00% in every text zone; contrast-weighted balance 0.493 to 0.514 W. Two preferences sit outside tolerance on purpose: the close crop's countdown sits about 160 px below the lower golden line, because the larger phone and the caption clearance win; the level's target sits about 100 px below the upper golden line for the same reason. TypeScript, metadata tagging and readback passed.

## Local artwork ownership

`Glass.tsx` draws the glass ground for phone, tablet, desktop and feature layouts from `composition/ribbons.ts`. The default listing is independent. No external model or texture was added.
