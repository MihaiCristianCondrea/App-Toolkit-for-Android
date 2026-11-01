# Codex task: App Toolkit trailer, third cut

Owner feedback on cut `cc0e90be` (develop). The cut is technically sound but reads like a slide deck, does not move with the song, and the opening is weak. Rework the choreography; keep the pipeline, assets and real UI.

## Read first (in this order)

1. `AGENTS.md` (repo root) and `marketing/STARTUP.md`.
2. `marketing/skills/built-in/motion-video/SKILL.md`, then its `references/technical-rules.md`, `references/pixel-style.md`, `references/review.md`, and `references/index.md` (with `references/stills/` and `references/films/` where present) for the Pixel feature-drop grammar this film should follow.
3. `marketing/skills/design-taste/taste-skill/SKILL.md` (motion must be motivated, one accent per moment, no perpetual loops on still content) and `marketing/skills/design-taste/ui-unslop/SKILL.md`.
4. This folder's `README.md` (scene table, motion rules, asset ownership) and `source/FIDELITY.md`.

## Where to edit

All work is in `marketing/creative/social-campaigns/app-trailer/source/src/`:

| File | Owns |
| --- | --- |
| `timeline.json` | Scene ids, start/end, events. `scripts/check.mjs` requires 8 contiguous scenes covering 29.88 s. Update scene titles/actions to match the new cut. |
| `music-analysis.json` | Detected onsets (read only). Drive every gesture from these. |
| `visuals.tsx` | `ease`, `easeIn`, `pop` (spring), `beat`, `camera()` + `CAMERA` keyframes, `Shape`, `Toolbox`, `Phone`, `Headline`, `Chip`. |
| `OpeningJourney.tsx` | 0 to 17.14 s: icons, logo, burst, gather, pull back to the phone, Quick Tools. |
| `FeatureJourney.tsx` | 17.14 to 26.26 s: Measure and Play & Decide. |
| `ClosingScene.tsx` | 26.26 to 29.88 s: three destinations, mark, headline, Play badge. |
| `Trailer.tsx` | Sequences and the camera wrapper. |
| `Kit.tsx` | `LiveScreen`, `LiveCrop`, `GeometryProbe`, widget selectors. Change only if a new crop needs a selector. |

Do not edit `marketing/ui` screens, the logo, shapes, device frame, badge or `soundtrack.mp3`. If the toolbox needs new layers, split them in `scripts/prepare.mjs` from the existing `marketing/assets/logos/icon.svg` paths (it already writes `toolbox-base.svg` and `toolbox-lid.svg`); never draw a new logo.

## Feedback to implement

1. **Open on the "tiu-niu".** The track opens with a two-note motif (onsets 0.02, 0.32, 0.62, 1.04, 1.46). Bring the tool icons in first, one per note, as the kit's Material Symbols in their kit shapes (compass, level, flashlight, SOS, water drop, breathe). The app logo arrives after them, on 1.76.
2. **The logo opening is lame. Replace it.** The mark pops: a fast zoom out then zoom in (anticipation scale-down, overshoot up, settle), and it opens by splitting. The lid leaves the frame; keep only the body half with the wrench and screwdriver. That half travels to the bottom of the frame and stays there as the launcher the widgets burst out of. The app logo then sits at the bottom of the composition, not in the middle.
3. **Remove the "Many tools." headline.** The burst must say it without text.
4. **Background shapes are too slow and too plain.** Give them real choreography: faster rotation on hits, spring scale punches on bass onsets, parallax against the camera (background moves less than foreground), squash on landing, and handoffs where a shape becomes the next element's backdrop (it grows from the control that caused the change). No constant idle spin that ignores the music.
5. **The "One place" group is still empty.** While the widgets gather (6.76 to 10.08 s, before the phone mock), fill the frame: gather all six widgets plus the Material Colors card into the full bento as it appears on the real screen, let the camera push close enough that the bento plus headline fill the frame, and bring shapes in behind it. No large empty quadrants.
6. **It moves like PowerPoint.** This is the biggest flaw. Remove every in-place fade-in/fade-out swap. Use instead: match cuts (a card becomes the next scene's header), continuous objects carried across chapter boundaries, overlapping action (the next move starts while the previous settles out), whip pans or push-throughs on downbeats with motion blur (`@remotion/motion-blur` `CameraMotionBlur` for the fast moves only; add it to `source/package.json` at the exact `@remotion/cli` version, `4.0.529`, and commit the lockfile), and kinetic type (masked word reveals, `wdth`/`wght` modulation per word on the beat). Headlines push existing elements aside instead of appearing over empty space.
7. **Match the song's energy.** Every onset in `music-analysis.json` must trigger something visible (a hit, a cut, a swap, a punch, a value change), with bigger gestures on the stronger onsets. Group consecutive onsets into phrases and change the camera on phrase boundaries. Build a beat sheet in `timeline.json` (`events`) and reference it rather than hard-coding seconds in components.

## Keep

- Real UI only: every screen and card comes from `LiveScreen`/`LiveCrop` (the marketing UI kit). No recreated UI, no ads, More apps footer off.
- The full soundtrack, unchanged, 29.88 s. 1920x1080 layout, 90 fps, master at 2560x1440.
- Determinism: only frame-derived values (`useCurrentFrame`, `spring`, `interpolate`, seeded random). No CSS transitions, timers or `Math.random`.
- Move things by `transform`, never by animated `left`/`top`.
- The closing: Quick Tools, Measure and Play & Decide destinations, "App Toolkit. Your toolkit." and the original Google Play badge, held at least 1.5 s.

## Review loop (cheap, do it every iteration)

```sh
cd marketing/ui && npm ci && npm run assets
cd ../creative/social-campaigns/app-trailer/source && npm ci && npm run assets
npm run lint && npm run check
# Stills at quarter scale, then one contact sheet:
node node_modules/@remotion/cli/remotion-cli.js still src/index.ts AppTrailer out.png --frame <seconds*90> --scale .25 \
  --browser-executable /opt/pw-browsers/chromium-1243/chrome-linux64/chrome   # or your local Chrome
```

Check stills at least at 0.3, 0.9, 1.8, 2.4, 3.6, 5.2, 8.0, 9.5, 12.0, 15.8, 18.9, 23.5, 25.9 and 28.8 s, and frames on either side of every scene boundary. Then render `npm run preview` (960x540) once and watch it with sound. Do not render the 2K master; the owner renders it after approving the preview.

## Done when

- The opening reads: icons on the motif, then the logo pops, splits, and its tool half sits at the bottom launching the widgets. No "Many tools." text.
- No frame in 0 to 26 s has a large empty area; the gather frame is full.
- No element fades in or out in place; every change is carried by motion.
- Each onset has a visible response; phrase changes move the camera.
- `npm run lint` and `npm run check` pass; `README.md` scene table and motion rules, and `timeline.json`, describe the new cut.
- Commit to `develop` with a message describing the choreography change.
