# Speaker Water Eject custom listing

Status: planned, not published. The English creative package contains 5 phone, 4 tablet and 4 desktop screenshots, a feature graphic and the official icon. Upload remains conditional on confirming production availability of version 26.11.02 in Play Console. The public Play page retrieved on November 7 still showed October 3 information and did not verify the rollout.

## Audience and positioning

People looking for a wet speaker solution can find a 165 Hz tone with a timed cycle and clear start and stop controls, alongside other everyday tools. The copy says the tone may help dislodge droplets and does not promise repair, water removal or waterproofing. The title retains App Toolkit's identity.

## Research

Reviewed November 7, 2026. [Water Eject: Speaker Cleaner](https://play.google.com/store/apps/details?id=com.water_eject.speaker_cleaner) describes a 165 Hz tone and a simple flow. [Speaker Cleaner: Water Eject](https://play.google.com/store/apps/details?id=com.easystudio.speakercleaner) describes auto and manual frequency modes and speaker and microphone tests. App Toolkit's focused fixed-frequency control is an alternative for a person who wants fewer choices; it does not claim those additional tests or configurable modes.

The keywords describe intent, not measured demand. The prior Play reports did not disclose individual search terms, and competitor install counts do not establish current keyword volume. Check the actual terms and targeting options in Play Console before selecting them. See [Google Play custom store listings](https://support.google.com/googleplay/android-developer/answer/9867158).

## Creative direction

Five screenshots read as one panorama in the language of Material 3's own hero art: a light aqua field crossed by ribbons of liquid glass in aqua and white, with sharp white rims. The ribbons are computed across the whole strip, so every seam meets. No phone crosses a divider: each screenshot stands on its own, and the render fails if a phone's visible part enters the next image. Headlines are dark on the light field, with App Toolkit's aqua caption pill. Phones are Material-Next's porcelain 3D Pixel 9 Pro XL. Every image shows a different screen or state, with its own composition: the ready 165 Hz screen appears once, in the hero.

1. Hero: a solved product shot, not a placed one. scripts/solve-hero.ts projects the 3D Pixel through the device frame's own camera and searches pitch, yaw, roll and perspective, solving scale and position so the visible silhouette's centroid sits at 0.50 W, the speaker edge at 0.70 H and the top runs beyond the image; it scores the bottom edge's visible thickness and angle and the narrowing towards the top against the reference, and rejects any pose that pushes the measured Water Eject UI inside the 0.05 W margins or the phone into the caption. The solution (src/hero-pose.json) is pitch 28°, yaw -13°, no roll, a 3000 px camera. Fine sound arcs leave both groups of the model's speaker holes along the bottom face's projected normal, with radii on a Fibonacci scale (34 to 233 px), and bubbles ride just outside the wavefronts as if pushed by them. MaterialShapes in the campaign's aqua tones sit only where the background's glass ribbons leave room, the largest behind the phone's right edge. The caption sits at the foot of the image, 89 px from the edge. Measured balance: contrast-weighted centroid 0.494 W, weight 0.511 left and 0.489 right. scripts/debug-hero.mjs overlays the golden guides, silhouette, centroid, speaker points, bands, arcs and bubbles. The sound and bubbles illustrate the tone; they are not a promise of what the app does to a phone.
2. Up to 30 seconds: a close crop on the running cycle and its 20 second countdown.
3. You choose when to stop: the whole phone turned to show its side, at 8 seconds with the Stop button in view.
4. The toolbox: Unit Converter and Quick Tools side by side, tilted apart.
5. Bubble Level.

The guidance and its limits are on every Water Eject screen and in the description. Tablet, desktop and feature artwork share the glass ribbons. No image promises a repair or shows a successful water-removal result.

## Verification and limits

The renderer, TypeScript check, targeted Water Eject browser check, listing validator and metadata injection/readback were run. All 15 image files are present. See source/README.md for commands and full check outcomes. Web screenshots were compared against Compose source and resources; a connected-device visual comparison and production rollout confirmation remain outstanding. Search targeting still needs to be selected from the terms Play Console makes available.
