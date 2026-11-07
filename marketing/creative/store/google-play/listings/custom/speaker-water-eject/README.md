# Speaker Water Eject custom listing

Status: planned, not published. The English creative package contains 5 phone, 4 tablet and 4 desktop screenshots, a feature graphic and the official icon. Upload remains conditional on confirming production availability of version 26.11.02 in Play Console. The public Play page retrieved on November 7 still showed October 3 information and did not verify the rollout.

## Audience and positioning

People looking for a wet speaker solution can find a 165 Hz tone with a timed cycle and clear start and stop controls, alongside other everyday tools. The copy says the tone may help dislodge droplets and does not promise repair, water removal or waterproofing. The title retains App Toolkit's identity.

## Research

Reviewed November 7, 2026. [Water Eject: Speaker Cleaner](https://play.google.com/store/apps/details?id=com.water_eject.speaker_cleaner) describes a 165 Hz tone and a simple flow. [Speaker Cleaner: Water Eject](https://play.google.com/store/apps/details?id=com.easystudio.speakercleaner) describes auto and manual frequency modes and speaker and microphone tests. App Toolkit's focused fixed-frequency control is an alternative for a person who wants fewer choices; it does not claim those additional tests or configurable modes.

The keywords describe intent, not measured demand. The prior Play reports did not disclose individual search terms, and competitor install counts do not establish current keyword volume. Check the actual terms and targeting options in Play Console before selecting them. See [Google Play custom store listings](https://support.google.com/googleplay/android-developer/answer/9867158).

## Creative direction

Five screenshots read as one panorama in the language of Material 3's own hero art: a light aqua field crossed by ribbons of liquid glass in aqua and white, computed across the whole strip so every seam meets, and fading behind each caption so the letters sit on a quiet field. Accents take the Water Eject sheet's own blues (ToolkitTileVisuals.kt's blue tool): the caption pill, the MaterialShapes, the sound arcs and the bubbles. Headlines are dark on the light field. Phones are Material-Next's porcelain 3D Pixel 9 Pro XL. On every water screen, phone and large screen alike, Quick Tools is scrolled to the Water Eject card that opens the sheet, so the card shows above it instead of Compass and Bubble Level, which have their own screenshot. Every image shows a different screen or state, with its own composition, and stands on its own: no phone crosses a divider.

Each composition is solved from its own targets by source/scripts/solve-layout.ts and checked by source/scripts/verify.mjs; see source/README.md for the system, its hard rules and its preferences, and review/ for the contact sheets and the verification report.

1. Hero: a large phone centred and running off the top, leaning back (pitch 28°, yaw -13°, a 3000 px camera) so the speaker holes and USB-C port on its bottom edge face the viewer. Fine sound arcs leave both groups of speaker holes, with bubbles riding their wavefronts; the caption stands at the foot of the image. The sound and bubbles illustrate the tone; they are not a promise of what the app does to a phone.
2. Up to 30 seconds: a close crop on the running cycle, the phone leaning right and running off the bottom, the countdown large in the lower half; the Stop button is cropped on purpose.
3. You choose when to stop: the whole phone turned to show its side (yaw -22°), the Stop button near the lower golden line.
4. The toolbox: Unit Converter and Quick Tools leaning together, solved as a pair at the largest size their UI allows; Quick Tools overlaps the top of Unit Converter by about 6%, clear of its title and fields.
5. Bubble Level, large and tilted, with its target and readings in view.

Tablet and desktop screenshots show the app's own large-screen layouts on the Pixel 9 Pro Fold and the Pixelbook Go, each centred at true size with no captions, as Play asks, and MaterialShapes placed by the same system clear of the device and the ribbons.

The feature graphic names the feature and says what it is, "Water Eject" over "A short tone for wet speakers", with App Toolkit small above, centred on a deeper aqua field with a glass ribbon in the sheet's blues flowing through it and a little sound rising off its crest. It has no device frame, no icon, no 165 Hz or 30 seconds and no claims. A second concept (the real drop visual with sound spreading from it) stays in source/src/FeatureGraphic.tsx as `ripple`; `node scripts/preview-feature.mjs` renders both, at half size on light and dark backgrounds.

The guidance and its limits are on every Water Eject screen and in the description. Tablet, desktop and feature artwork share the glass ribbons. No image promises a repair or shows a successful water-removal result.

## Verification and limits

The layout solver, the verifier (geometry, caption contrast, background interference and balance on the composited pixels), the renderer, TypeScript and metadata injection and readback were run; all 15 image files are present. See source/README.md for commands and results, and review/ for the contact sheets. Web screenshots were compared against Compose source and resources; a connected-device visual comparison and production rollout confirmation remain outstanding. Search targeting still needs to be selected from the terms Play Console makes available.
