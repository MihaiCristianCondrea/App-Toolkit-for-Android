# App Toolkit: Trailer

A 32-second 16:9 trailer that gathers the five custom store listings into one film. Every tool opens its own world, with its listing's palette and signature object, and hands off to the next through a MaterialShape wipe; the tools then gather as App Toolkit.

| | |
| --- | --- |
| Status | Preview for owner review; master after approval |
| Sound | Our own score, `soundtrack.mp3`, synthesised by `source/scripts/compose.mjs` from the timeline: 120 BPM in F major, 32 seconds, -13.5 LUFS, peak -1 dBFS |
| Design size | 1920 x 1080, 90 fps |
| Preview | 960 x 540, `output/video/app-toolkit-trailer-preview.mp4` |
| Master | 2560 x 1440, `output/video/app-toolkit-trailer-master.mp4` |
| Output | `output/`, generated and ignored by Git |

It replaces the earlier trailers in `app-trailer/` and `trailer-v2/`, which stay as records.

## Concept and motion rules

**Every tool brings its own world.** The worlds and their objects come from the listings: Water Eject's sound waves and bubbles, the Ruler and Protractor's yellow ruler, the Unit Converter's unit shapes and category chips, the Tip and Bill Splitter's receipt and the Decision Wheel's wheel and confetti.

1. Picture and music share one clock: 120 BPM, every scene two bars. The beats are in `src/timeline.json`, and `scripts/compose.mjs` reads the same file, so every motif has its own sound on its own frame: a water drop per wave, a tick per protractor sweep, a print per receipt line, a ratchet click each time a wheel segment passes the pointer, and a mallet sparkle for the confetti.
2. One camera, no slides. The water world grows out of its shape in the intro; after that every scene opens pushed into its phone (straight on, centred, 1.5 times its size) and pulls back while the phone twists into its pose on a spring, and ends pushing into a phone while the ground blends to the next world's colour. Both scenes hold the same straight phone at the cut, so the cut is only its screen changing. The montage pulls back from the wheel's pick to the row of five phones, and the row gathers into the end card's icon.
3. The UI is real and truthful: each phone mounts the app's screen from the UI kit in the state its listing shows, in Material-Next's 3D Pixel. The motifs leave the phone: the waves leave the actual speaker holes of the posed phone, the receipt slides out from behind it.
4. Type rises out of a mask, title then line, on consecutive beats.
5. The phones' turns are real: the 3D Pixel is redrawn for every pose. Entrances use Material's emphasised decelerate curve, the push into a phone its emphasised accelerate curve (so the cut lands at full speed), pops use springs, and everything else drifts slowly.
6. One grid for every tool scene, from Remotion's video-layout rules: one thing to notice per zone and key content inside a safe area 144 px from the sides and 108 px from the top and bottom. The title block (132 px, two lines, with a 59 px line under it) sits top left, the tool's motif in the zone under it, starting on the title's left edge, and the phones in the right column. Confetti keeps clear of the title. The intro and end card are centred, with a 150 px title.
7. Material, not illustration: shapes are MaterialShapes, colours are each world's tonal palette, strokes are thick and rounded, lifted surfaces share two elevation levels (`ELEVATION`), every world is decorated as its listing is, with four or five MaterialShapes in its tones placed by hand around the phone and at the frame's edges (never in a row, never touching the type, motif or phone), that pop in and drift, and the end card's rings are state-layer ripples in each world's container colour. The water keeps the Water Eject listing's own drawing: thin arcs with a soft glow and glass bubbles.

## Beat sheet

| Time | Picture | Sound |
| --- | --- | --- |
| 0 to 4 | One shape per tool arrives on each beat in its listing's colour; the App Toolkit icon pops on 2.5, the shapes swing round it and "App Toolkit, Everyday tools in one app" lands. | Pad and a rising mallet note per shape; a bell for the icon. |
| 4 to 8 | Water Eject's blue world. The phone turns in and stands with its speaker pointing down, as the sheet asks; on every note three glowing arcs leave the speaker, glass bubbles rise off them and pop, and droplets are thrown out and fall. The left zone shows the tone, 165 Hz, as a waveform that swells on every note. | The groove starts; a water drop per wave. |
| 8 to 12 | Ruler and Protractor's yellow world, with two phones turning in from either side. The drawn ruler slides in and measures to 76.0 mm, the same reading the Surface Ruler phone shows; then the Protractor phone sweeps live to 45 degrees on four beats. | Ticks as the scale draws and measures, a click and a mallet note per sweep. |
| 12 to 16 | Unit Converter's mint world. Five categories, one per beat: the filter chip selects it, and the two unit shapes, turning like Material 3 Expressive's loading indicator, spin on and morph into the next pair's shapes as the values swap (1 m = 100 cm, 500 g = 0.5 kg, 20 °C = 68 °F, 2 L = 2000 mL, 1 GB = 1000 MB); the phone shows the same conversion. | A mallet pair per category. |
| 16 to 20 | Tip and Bill Splitter's peach world. A receipt slides out from behind the phone and prints the bill, the 10% tip and €28.60 per person; a card per person follows, each paying €28.60, and the phone shows the same split. | A print per line, a bell for the share. |
| 20 to 24 | Decision Wheel's lilac world. The six choices (Walk, Read, Cook, Draw, Music, Rest) land on the wheel one by one; it spins and settles on Music on 22.5, the winning segment lifts out under the white pointer, a Music chip pops, confetti bursts and the phone shows the pick. | A mallet per choice; the kick drops out, a ratchet follows the wheel, the build lands on the confetti. |
| 24 to 28 | The camera pulls back from the wheel's pick and reveals it as the last of a row of five phones, one per world, each bobbing on its note; "Everyday tools, one app" rises and more of the app's tools stream in as chips; then the row gathers into the centre. | The groove returns; a mallet per phone, a pop per chip. |
| 28 to 32 | The row lands in the App Toolkit icon: a ripple in each world's colour and confetti leave it, the name rises and Google's official Get it on Google Play badge comes in whole, with the trademark line under it. | The groove stops on F major in bells and fades out. |

Every number and tool name on screen is in the app: the 165 Hz Water Eject sheet, the Surface Ruler, the 45-degree Protractor, the five conversions above, the €52.00 bill with a 10% tip split two ways, and the wheel's six choices settled on Music. The end card uses Google's unaltered badge from `marketing/assets/badges/google-play`, moved and scaled whole as its guidelines allow. The chips name tools the app ships (Compass, Bubble Level, Flash Dimmer, Morse and SOS, Counter, Coin Flip, Dice Roll, Random number, Reaction Test, Breathe).

## Build

Requires Node 22+ and the UI kit's assets (`npm ci && npm run assets` in `marketing/ui`).

```sh
cd marketing/creative/social-campaigns/toolkit-trailer/source
npm ci
npm run lint
npm run music              # the soundtrack, into ../soundtrack.mp3
npm run frames             # stills at each scene and beat, and output/images/contact-sheet.png
npm run frames -- 4.6 19.98 # stills at chosen seconds
npm run preview            # 960 x 540 with sound
npm run master             # 2560 x 1440, after approval
```

`npm run dev` opens Remotion Studio. All timings are seconds in `src/timeline.json`; frames are seconds times 90.

## Source

| File | Contents |
| --- | --- |
| `src/timeline.json` | Scenes, hand-off shapes and points, and every beat |
| `src/theme.ts` | Each world's palette, from its listing's theme |
| `src/Trailer.tsx` | The eight scenes and the film |
| `src/motifs/morph.ts` | MaterialShape morphing on the UI kit's own curves, for the unit shapes |
| `src/motifs/` | Phone, headline, elevation, decoration, chips, sound waves, glass bubbles, droplets and ripples, ruler, unit pair, receipt, wheel, confetti and the shape wipe; `geometry.ts` projects the 3D Pixel (from the Water Eject listing) and `confetti.ts` is the Decision Wheel listing's scatter |
| `src/LitScreen.tsx`, `src/Shape.tsx` | The UI kit's screens and MaterialShapes, as the listings use them |
| `scripts/compose.mjs` | The soundtrack, synthesised from the timeline |
| `scripts/render.mjs` | Frames, preview and master |
