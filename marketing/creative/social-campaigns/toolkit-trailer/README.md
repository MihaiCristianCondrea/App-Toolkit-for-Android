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

**Every tool brings its own world.** The worlds and their objects come from the listings: Water Eject's sound waves and bubbles, the Ruler and Protractor's yellow ruler and protractor, the Unit Converter's m and cm shapes, the Tip and Bill Splitter's receipt and the Decision Wheel's wheel and confetti.

1. Picture and music share one clock: 120 BPM, every scene two bars. The beats are in `src/timeline.json`, and `scripts/compose.mjs` reads the same file, so every motif has its own sound on its own frame: a water drop per wave, a tick per protractor sweep, a print per receipt line, a ratchet click each time a wheel segment passes the pointer, and a mallet sparkle for the confetti.
2. Each world opens out of a MaterialShape in its colour, growing from a point until it fills the frame; the previous world stays underneath until covered.
3. The UI is real and truthful: each phone mounts the app's screen from the UI kit in the state its listing shows, in Material-Next's 3D Pixel. The motifs leave the phone: the waves leave the actual speaker holes of the posed phone, the receipt slides out from behind it.
4. Type rises out of a mask, title then line, on consecutive beats.
5. Entrances use Material's emphasised decelerate curve; pops use springs; everything else drifts slowly.
6. Material, not illustration: shapes are MaterialShapes, colours are each world's tonal palette, strokes are thick and rounded, lifted surfaces share two elevation levels (`ELEVATION`), and the sound is drawn as Material draws it: waves are tonal bands that thin as they travel, bubbles are flat tonal circles with one highlight, and the end card's rings are state-layer ripples in each world's container colour.

## Beat sheet

| Time | Picture | Sound |
| --- | --- | --- |
| 0 to 4 | One shape per tool arrives on each beat in its listing's colour; the App Toolkit icon pops on 2.5, the shapes swing round it and "App Toolkit, Everyday tools in one app" lands. | Pad and a rising mallet note per shape; a bell for the icon. |
| 4 to 8 | Water Eject's blue world. The phone leans back so its speaker edge faces the camera; on every note a tonal sound wave leaves the speaker edge and pushes bubbles out, which rise and pop. | The groove starts; a water drop per wave. |
| 8 to 12 | Ruler and Protractor's yellow world. The ruler slides in and draws its ticks; the protractor arm sweeps to 45 degrees on four beats, matching the phone's Protractor at 45 degrees. | Ticks as the scale draws, a click and a mallet note per sweep. |
| 12 to 16 | Unit Converter's mint world. 1 m counts up to 100 cm, the shapes swap places on 14.0 and become 20 °C and 68 °F on 15.0; the phone shows 1 metre as 100 centimetres. | Ticks for the count, a whoosh for the swap. |
| 16 to 20 | Tip and Bill Splitter's peach world. A receipt slides out from behind the phone and prints the bill, the 10% tip and €28.60 per person; the phone shows the same split. | A print per line, a bell for the share. |
| 20 to 24 | Decision Wheel's lilac world. The wheel spins and settles on 22.5; confetti bursts and flutters, and the phone changes to the pick, Music. | The kick drops out; a ratchet follows the wheel, the build lands on the confetti. |
| 24 to 28 | The five worlds return as tiles, one per beat; "Everyday tools, one app"; more of the app's tools join as chips. | The groove returns; a mallet per tile, a pop per chip. |
| 28 to 32 | The icon returns; a ripple in each world's colour and confetti leave it; "App Toolkit, Available on Google Play" lands and holds. | The groove stops on F major in bells and fades out. |

Every number and tool name on screen is in the app: the 165 Hz Water Eject sheet, the 45-degree Protractor, the 1 m conversion, the €52.00 bill with a 10% tip split two ways, and the wheel's six choices settled on Music. The chips name tools the app ships (Compass, Bubble Level, Flash Dimmer, Morse and SOS, Counter, Coin Flip, Dice Roll, Random number, Reaction Test, Breathe).

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
| `src/motifs/` | Phone, headline, elevation, sound waves, bubbles and ripples, ruler, protractor, unit pair, receipt, wheel, confetti and the shape wipe; `geometry.ts` projects the 3D Pixel (from the Water Eject listing) and `confetti.ts` is the Decision Wheel listing's scatter |
| `src/LitScreen.tsx`, `src/Shape.tsx` | The UI kit's screens and MaterialShapes, as the listings use them |
| `scripts/compose.mjs` | The soundtrack, synthesised from the timeline |
| `scripts/render.mjs` | Frames, preview and master |
