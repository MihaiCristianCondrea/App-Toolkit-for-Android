# App Toolkit: Trailer

**One app. Many everyday moments.** A 32-second 16:9 trailer: the app's name, then one small story through the five custom store listings' worlds, in which one useful form turns into the next until the app's tools gather into its icon. The tools carry no titles; they show what they are.

| | |
| --- | --- |
| Status | Approved and rendered at 2K (2560 x 1440, 90 fps) |
| Sound | Our own score, `soundtrack.mp3`, synthesised by `source/scripts/compose.mjs` from the same timeline: 120 BPM in F major, 32 seconds, about -14 LUFS, peak -1 dBFS |
| Design size | 1920 x 1080, 90 fps |
| Preview (animatic) | 960 x 540, `output/video/app-toolkit-trailer-preview.mp4` |
| Review | 1920 x 1080, `output/video/app-toolkit-trailer-review.mp4`, to watch at normal size before approving |
| Master | 2560 x 1440, `output/video/app-toolkit-trailer-master.mp4` |
| Thumbnail | 2560 x 1440, `output/images/app-toolkit-trailer-thumbnail.png` |
| Metadata | [`toolkit-trailer-metadata.json`](toolkit-trailer-metadata.json) and `output/schema.jsonld` |
| Captions | `captions/youtube.txt`, `youtube-tags.txt`, `instagram.txt`, `x.txt`, `facebook.txt`, `tiktok.txt`, `linkedin.txt`, `threads.txt`: paste-ready; the YouTube title and alt text are in the metadata manifest |
| Output | `output/`, generated and ignored by Git |

It replaces the earlier trailers in `app-trailer/` and `trailer-v2/`, which stay as records.

## Why it was rebuilt

The previous cut was eight equal four-second scenes, each title-left and phone-right, joined by the same camera move (push into the phone, cut on the screen, pull out). Smoothing that move made the repetition smoother, not more interesting. This cut keeps the artwork and changes the edit: scene lengths follow what happens in them, every transition has its own reason, and the music follows the story's energy instead of repeating one block per scene.

## Storyboard

| Time | Scene | What happens | Shot |
| --- | --- | --- | --- |
| 0 to 3.2 | App Toolkit | One shape per tool arrives on each note in its world's colour, the icon pops, the shapes breathe round it, and "App Toolkit, Everyday tools in one app" lands. Then the others swap out while the blue shape travels on. | Centred lockup |
| 3 to 6.85 | Water Eject | The blue shape reaches the phone's place and morphs into its backdrop as the blue floods in; the phone rises and rotates smoothly in 3D into the store listing's dramatic hero orientation (pitch 28, yaw -13). Below each group of speaker holes, concentric wave arcs and glass bubbles fan out from small to big on the beat. | Phone centred and dominant |
| 6.15 to 10.95 | Ruler & Protractor | While the last wave flattens into the ruler's line, the blue backdrop morphs and moves into the yellow one (the yellow fills it from the centre) and the phone turns in 3D: past edge-on it comes round as the Surface Ruler. The Protractor phone joins it and the ruler pulls back to a strip with a semicircular protractor dial. An arm rises to 45 degrees with the Protractor, which leads while it sweeps; a marker measures 76.0 mm while the Surface Ruler, leading again, reads the same. Both phones end on their results. | Two large phones, one leading at a time; the ruler supports |
| 10.95 to 13.5 | Unit Converter | The reading's shape has expanded into the converter's ground; under 76.0 mm the first card grows, 7.6 cm beside it. One more conversion follows, 20 °C to 68 °F, and each result holds for a full second; the shapes morph like the loading indicator, and the phone shows the same category. | Wide, the pair leads |
| 13.15 to 17 | Tip & Bill Splitter | The receipt prints the bill and the 10% tip, the share lands at €28.60, and the total divides into two people's cards. Then the phone zooms in and glides to the centre, blending into the Decision Wheel phone. | Receipt-led, the phone rises in beside it, then zooms to the centre |
| 17 to 23.5 | Decision Wheel | The zoomed phone arrives with the real Decision Wheel sheet and its six choices, turned a little. The spin starts inside the app; one camera move pushes towards the wheel while the phone turns face on, and passes through: the phone's edges, then its bezel, leave the frame by framing alone (19.72 s), and for a moment the app's card is all there is. The lilac grows from behind the wheel, which settles to fill the frame and slows to Music in the app's 3 seconds (21 s). The pick is marked, "Music" holds for a second, and only then the confetti (22 s). | The phone, then the camera inside the app, then the wheel as the scene |
| 22.75 to 29.1 | The toolkit | Twelve pieces of the result's confetti fly to a grid and become the app's tools as MaterialShape badges with their Material Symbols icons and names, and the named grid holds for about a second and a half; then the badges rise into a band and the five tools' real screens track in from the right on the beat. | Wide, a lateral track |
| 29 to 32 | App Toolkit | Everything converges into the icon on the final downbeat; the name and the official Get it on Google Play badge settle and hold. | Centred, static hold |

## Transitions, and why each is different

| Change | How | Why |
| --- | --- | --- |
| App Toolkit to Water Eject | The other shapes swap out while the blue one travels to the phone's place, grows and morphs into the water backdrop as the blue floods in; the phone rises into it | The name first, kept short; the first tool comes out of the shapes that stand for the tools |
| Water Eject to Ruler | The last wave loses its curve into a straight line that grows graduations and opens the yellow world; meanwhile the blue backdrop morphs and moves into the yellow one and the phone turns in 3D, coming round as the Surface Ruler; the line thickens into the ruler | A morph with a physical reason (a wave becomes a measuring line), and one phone carried from tool to tool |
| Ruler to Unit Converter | The 76.0 mm reading lifts onto a soft-burst shape, which keeps growing until it is the converter's off-white ground; under the same text the converter's first card grows, 7.6 cm beside it | A container transform: the value carries the viewer, and the conversion is the app's real result |
| Unit Converter to Tip & Bill Splitter | The two cards slide together, round into one surface and stretch and whiten into the receipt, while the coral world opens out of it in a soft burst | A physical Material transformation that keeps shape, place and motion; the receipt is artwork, not the app's UI |
| Tip & Bill Splitter to Decision Wheel | The phone zooms in and glides from the right to the centre as the receipt fades aside, blending into the Decision Wheel phone | Keeps the phone in motion across the scene boundary and sets up the camera push into the wheel |
| Inside the Decision Wheel | One camera move from the arrival: it creeps in, accelerates towards the wheel while the phone turns face on, and passes through the phone, whose edges and bezel leave the frame while the camera is still moving; the phone itself never moves. Face on, the wheel is handed to a replica drawn from the app's own wheel (same plate, colours, labels, pointer and angle, where the app paints it), so it can stay sharp and in front of the lilac, which grows from behind it once the phone has gone. The wheel overshoots the frame a little and settles to fill it | The one dramatic camera move in the film, and one wheel throughout: the viewer enters the app rather than watching something taken out of it |
| Decision Wheel to the toolkit | The result holds, the confetti bursts, and it does not settle: twelve pieces fly on and morph into the app's tools | The payoff, and the most expressive change: a choice reveals the whole collection |
| The toolkit to the icon | The cards and badges converge into the icon as it pops on the resolution | Everything gathers into one app |

No two adjacent changes share a motion, and there is no shared camera.

## Sound

The arrangement follows the story's energy: an F pad under the intro, a rising mallet note per shape, a bell for the icon, and a drop as the water world opens; soft half-time pulses and an arpeggio for the water; a four-on-the-floor with ticks on every sixteenth for the measuring, dropping out before the match cut; a syncopated bass for the converter; prints for the receipt, a bell for the share and a chime per person, then only the pad for the rest; a sparse wheel with a mallet per choice, a wind-up swell, a ratchet that follows the wheel's real rotation (it loads `src/wheelMotion.ts`, the code the picture draws the wheel with, so each click lands on the frame a segment passes the pointer), an air glide that rises with the camera's push and peaks as it passes through the phone, a soft tom as the lilac grows, a build through the spin, a bell and a low kick as Music lands on the downbeat, a held breath over the result, and the film's only crash with the confetti; hats that lead into the montage's downbeat, then the fullest groove for the montage, where each card lands with its tool's sound; and a held F major on the icon. Accents at the cuts are a low tom and a bell, not crashes. Chords, one per bar from 3 s: F C Dm Bb F C Dm Bb C F Dm C F.

## Motion and truth

1. One useful form turns into the next (a shape, a wave, a line, a reading, a card, a receipt, a choice, the tools), but not every change is a morph: one is a hard cut and one a rest. No per-tool titles: the screens and objects say what each tool is.
2. The UI is real and truthful: each phone and card mounts the app's screen from the UI kit in a real state. Every number on screen is the app's own: the 165 Hz Water Eject sheet; the Protractor at 45 degrees and the Surface Ruler's 76.0 mm, both live (the drawn ruler is in millimetres and measures the same); the two conversions (76.0 mm = 7.6 cm, 20 °C = 68 °F); the €52.00 bill with a 10% tip, €28.60 each for two; the wheel's six choices settled on Music, spun as the app spins it (5.25 turns in 3 seconds). The water, the ruler artwork, the receipt, the badges and the confetti are decoration, not app features. The tools named in the montage are tools the app ships.
3. Material, not illustration: MaterialShapes on the UI kit's own curves, drawn as SVG paths so they move and turn at sub-pixel precision (CSS masks snapped to whole pixels and looked shaky); nothing drifts for the sake of it; each world's tonal palette, thick rounded strokes, two elevation levels. The end card uses Google's unaltered badge from `marketing/assets/badges/google-play`, moved and scaled whole as its guidelines allow, with the trademark line.
4. The phones are sized per scene to read the tool's result at normal playback size: 0.86 for Water Eject, 0.8 for the two measuring phones (the leading one at full size, the other at 0.86 of it), the Unit Converter and the Bill Splitter, and 0.9 for the Decision Wheel at the cut, before the camera closes in. Each tool shows one interaction.
5. Every frame is a function of its frame number; picture and sound read the same `src/timeline.json`. No angle or position is rounded: a phone is placed by its transform alone (Blink rounds a box's left and top to whole pixels, which would make a slow phone step a pixel at a time), and the water phone turns into the ruler as one angle through edge-on (`src/turn.ts`), at the same angular velocity either side of the swap.
6. The Decision Wheel's motion is checked, not eyeballed: `scripts/verify-wheel.mjs` samples `src/wheelMotion.ts` on every frame (continuity of value and velocity of the wheel's angle, size and centre and the phone's pose; the spin's duration and stop; the phone staying put; its edges and bezel leaving the frame before the lilac; the lilac covering the app's wheel before the replica parts from it), then renders the handoff and two frames after it at full size, as the film draws them and with the replica hidden, and measures both: a circle fitted to the disc's rim, the angle of the six segment boundaries, the pointer's top edge and apex, the rim ray by ray, and every pixel outside the wheel. It writes `output/wheel-checks.json` and the per-frame values to `output/wheel-motion.csv`.
7. The rendered film is reviewed at normal size by measurement as well as by eye: `scripts/playback.mjs` measures the change between every pair of frames and fails on any single-frame jump that is not a planned cut, and cross-correlates the film's sound with the soundtrack. Its first run found the water phone popping in a third of the way up at 3 s, the coral wipe opening 1600 px in one frame at 13.17 s, a two-level flash as the end card's ripples faded through a few percent opacity, and every sound 42.7 ms late (Remotion's AAC kept the encoder's priming without an edit list). The phones now rise from wholly below the frame, the wipe starts from rest, the ripples clear quickly once faint, and the renders take their sound from ffmpeg, which records the priming. The replica is placed where the app paints its wheel, (233, 643) in the phone's pixels: the sheet lays it out at (232.61, 642.78), and Blink paints a box at its layout position rounded to whole pixels of the phone's own space, before the phone is scaled.

## Build

Requires Node 22+ and the UI kit's assets (`npm ci && npm run assets` in `marketing/ui`).

```sh
cd marketing/creative/social-campaigns/toolkit-trailer/source
npm ci
npm run lint
npm run music              # the soundtrack, into ../soundtrack.mp3
npm run frames             # a still per scene and output/images/contact-sheet.png
npm run transitions        # before, during and after every transition: output/images/transitions.png
npm run frames -- 4.6 19.98 # stills at chosen seconds
npm run verify:wheel       # the wheel's motion and handoff checks (add -- --motion to skip the renders)
npm run preview            # the animatic: 960 x 540 with sound
npm run review             # 1920 x 1080 with sound, to watch at normal size
npm run playback           # the review's jumps and sound sync, measured (output/playback.json)
npm run master             # 2560 x 1440, after approval
npm run thumbnail          # 2560 x 1440 closing brand poster (output/images/app-toolkit-trailer-thumbnail.png)
npm run metadata           # tags soundtrack.mp3 and output/**/*, writes output/schema.jsonld, and verifies
```

`npm run dev` opens Remotion Studio. All timings are seconds in `src/timeline.json`; frames are seconds times 90.

## Source

| File | Contents |
| --- | --- |
| `src/timeline.json` | The story: scenes, every beat, the transitions sheet's times, and the wheel's turn |
| `src/theme.ts` | Each world's palette, from its listing's theme |
| `src/Trailer.tsx` | The scenes, the transitions between them and the edit |
| `src/wheelMotion.ts` | The Decision Wheel's camera, the phone's turn, the spin and the handoff, shared by the picture, the soundtrack and the checks |
| `src/turn.ts` | A phone turning through edge-on into another screen, as one continuous angle |
| `src/motifs/morph.ts` | MaterialShape morphing on the UI kit's own curves, for the unit shapes |
| `src/motifs/` | Phone, headline, elevation, decoration, chips, sound waves, glass bubbles, droplets and ripples, ruler, unit pair, receipt, wheel, confetti and the shape wipe; `geometry.ts` projects the 3D Pixel (from the Water Eject listing) and `confetti.ts` is the Decision Wheel listing's scatter |
| `src/LitScreen.tsx`, `src/Shape.tsx` | The UI kit's screens and MaterialShapes, as the listings use them |
| `scripts/compose.mjs` | The soundtrack, synthesised from the timeline |
| `scripts/render.mjs` | Frames, the transitions sheet, preview, review and master |
| `scripts/verify-wheel.mjs` | The wheel's motion and geometry checks |
| `scripts/playback.mjs` | The rendered film's frame-to-frame jumps and sound sync |
| `scripts/metadata.mjs` | Injects and verifies SEO, rights and accessibility metadata across all rendered outputs |
| `scripts/load-ts.mjs` | Loads the TypeScript motion code in Node, so the soundtrack and the checks run the picture's own code |
