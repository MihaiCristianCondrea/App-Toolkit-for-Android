# App Toolkit: Trailer

A 29.88-second 16:9 trailer that gathers the five custom store listings into one film. Every tool opens its own world, with its listing's palette and signature object, and hands off to the next through a MaterialShape wipe; the tools then gather as App Toolkit.

| | |
| --- | --- |
| Status | Preview for owner review; master after approval |
| Sound | The supplied `soundtrack.mp3` (from trailer-v2), 29.88 seconds, unchanged |
| Design size | 1920 x 1080, 90 fps |
| Preview | 960 x 540, `output/video/app-toolkit-trailer-preview.mp4` |
| Master | 2560 x 1440, `output/video/app-toolkit-trailer-master.mp4` |
| Output | `output/`, generated and ignored by Git |

It replaces the earlier trailers in `app-trailer/` and `trailer-v2/`, which stay as records.

## Concept and motion rules

**Every tool brings its own world.** The worlds and their objects come from the listings: Water Eject's sound waves and bubbles, the Ruler and Protractor's yellow ruler and protractor, the Unit Converter's m and cm shapes, the Tip and Bill Splitter's receipt and the Decision Wheel's wheel and confetti.

1. Every change lands on an onset detected in the soundtrack (`src/music-analysis.json`); the beats are in `src/timeline.json`.
2. Each world opens out of a MaterialShape in its colour, growing from a point until it fills the frame; the previous world stays underneath until covered.
3. The UI is real and truthful: each phone mounts the app's screen from the UI kit in the state its listing shows, in Material-Next's 3D Pixel. The motifs leave the phone: the waves leave the actual speaker holes of the posed phone, the receipt slides out from behind it.
4. Type rises out of a mask, title then line, on consecutive beats.
5. Entrances use Material's emphasised decelerate curve; pops use springs; everything else drifts slowly.

## Beat sheet

| Time | Picture |
| --- | --- |
| 0.00 to 3.32 | One shape per tool arrives on each motif note in its listing's colour; the App Toolkit icon pops on 1.76, the shapes swing round it and "App Toolkit, Everyday tools in one app" lands. |
| 3.32 to 6.76 | Water Eject's blue world. The phone leans back so its speaker edge faces the camera; on every note a sound wave leaves both groups of speaker holes and pushes bubbles out. |
| 6.76 to 10.08 | Ruler and Protractor's yellow world. The ruler slides in and draws its ticks; the protractor arm sweeps to 45 degrees on four beats, matching the phone's Protractor at 45 degrees. |
| 10.08 to 13.72 | Unit Converter's mint world. 1 m counts up to 100 cm, the shapes swap places on 12.22 and become 20 °C and 68 °F on 12.92; the phone shows 1 metre as 100 centimetres. |
| 13.72 to 17.14 | Tip and Bill Splitter's peach world. A receipt slides out from behind the phone and prints the bill, the 10% tip and €28.60 per person; the phone shows the same split. |
| 17.14 to 21.98 | Decision Wheel's lilac world. The wheel spins and settles on 19.98; confetti bursts and the phone changes to the pick, Music. |
| 21.98 to 26.26 | The five worlds return as tiles, one per note; "Everyday tools, one app"; more of the app's tools join as chips. |
| 26.26 to 29.88 | The icon returns with confetti and a sound wave; "App Toolkit, Available on Google Play" lands and holds. |

Every number and tool name on screen is in the app: the 165 Hz Water Eject sheet, the 45-degree Protractor, the 1 m conversion, the €52.00 bill with a 10% tip split two ways, and the wheel's six choices settled on Music. The chips name tools the app ships (Compass, Bubble Level, Flash Dimmer, Morse and SOS, Counter, Coin Flip, Dice Roll, Random number, Reaction Test, Breathe).

## Build

Requires Node 22+ and the UI kit's assets (`npm ci && npm run assets` in `marketing/ui`).

```sh
cd marketing/creative/social-campaigns/toolkit-trailer/source
npm ci
npm run lint
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
| `src/motifs/` | Phone, headline, sound waves and bubbles, ruler, protractor, unit pair, receipt, wheel, confetti and the shape wipe; `geometry.ts` projects the 3D Pixel (from the Water Eject listing) and `confetti.ts` is the Decision Wheel listing's scatter |
| `src/LitScreen.tsx`, `src/Shape.tsx` | The UI kit's screens and MaterialShapes, as the listings use them |
| `scripts/render.mjs` | Frames, preview and master |
