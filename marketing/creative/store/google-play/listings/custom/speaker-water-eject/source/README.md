# Speaker Water Eject listing source

The English package has eight 2160 x 3840 phone screenshots, four 3840 x 2160 tablet screenshots, four 3840 x 2160 desktop screenshots, a 1024 x 500 feature graphic and a 512 x 512 official icon. Rendered files are in ../listing/en-US. Review strips are in ignored output/.

## Render and verify

From this folder:

```text
npm ci
npm run lint
npm run render -- en-US
npm run metadata
```

Run the Play validator from marketing/creative/store/google-play with `node scripts/validate.mjs`. Run the focused browser check from marketing/ui with `node scripts/check-water-eject.mjs`; set CHROMIUM_PATH to an installed Chromium executable. The broad check is `npm run qa` there.

Rendering overwrites only this listing's graphics folders. Metadata tagging runs after rendering and verifies decoded pixels are unchanged. Its file manifest requires all 18 outputs. The Play validator alone does not require graphics for custom listings.

## Design and fidelity

The first five phone screenshots explain one feature, rather than switching to unrelated tools after the opening image. Captions retain the two-line pill treatment. A shallow-water aqua ground, cyan and mint contours, elliptical ripples, original droplets and sparse smooth MaterialShapes form the strip. A small warm reflection accent avoids an icy mood. The hero is straight on, two shots crop closer to the native controls, and the other phones use restrained 3D yaw, pitch and roll. Phone layouts render at twice their 1080 x 1920 design resolution for clearer perspective text. The app keeps its captured palette. The existing amber toolbox icon retains the brand.

Water Eject follows WaterEjectTool.kt: a 200dp visual, 165 Hz when idle, remaining seconds while running, native guidance and note, full-width Start 30-second cycle or Stop cycle. Capture state uses waterCapture to hold the countdown without audio. Material-Next 0.2.0 must actually be installed, not just declared in package.json. Run npm ci in marketing/ui and sync its assets if the installed version is stale. The capture refuses to substitute a CSS phone outline when the 3D picture is unavailable. The frame's updateComplete is awaited and the preview's entrance animation is disabled for still captures, so its note is not cropped mid-transition.

The shared Marketing UI owns UI parity and state rendering. Listing copy, composition, artwork and metadata remain local. With only two visual sources, keep the small renderer explicit; a future shared runner can take the source/output paths and strings as inputs when another listing demonstrates the same needs. Do not couple custom artwork to the default listing or introduce a configuration framework now.

## Publication gate

The creative package targets application version 26.11.02. The public Play page retrieved November 7 showed older content and did not establish that version's availability. Confirm its production rollout for the intended audience in Play Console before upload. No device was connected for a side-by-side native capture; fidelity was checked against Compose and Android strings.

## Validation results (previous iteration)

- All 18 final graphics rendered and were visually inspected.
- TypeScript and the Google Play listing validator passed.
- All 147 existing Marketing UI browser checks passed.
- The focused Water Eject check passed for idle/running copy, silent capture and countdown stability.
- Metadata tagging and readback passed for all 18 files; decoded pixels were preserved.
- git diff --check passed.

The previous renderer logged nonfatal local font-request warnings. The final images were inspected for visible text and icon rendering. No connected Android device comparison was performed.

## Local artwork ownership

`WaterArt.tsx` supplies the same water contours and accent geometry to phone, tablet, desktop and feature layouts. The default listing is independent. Pose choices live next to the phone sequence; the UI adapter accepts only yaw and pitch, with the Pixel's supported porcelain finish. No external model or texture was added.
