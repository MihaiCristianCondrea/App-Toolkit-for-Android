# App Toolkit: Trailer

**One app. Many everyday moments.** A 28-second 16:9 trailer that tells one small story through the five custom store listings' worlds: a problem, the tool that fixes it, and one useful form turning into the next, until the app's tools gather into its icon.

| | |
| --- | --- |
| Status | Animatic for owner review; master after approval |
| Sound | Our own score, `soundtrack.mp3`, synthesised by `source/scripts/compose.mjs` from the same timeline: 120 BPM in F major, 28 seconds, about -14 LUFS, peak -1 dBFS |
| Design size | 1920 x 1080, 90 fps |
| Preview (animatic) | 960 x 540, `output/video/app-toolkit-trailer-preview.mp4` |
| Master | 2560 x 1440, `output/video/app-toolkit-trailer-master.mp4` |
| Output | `output/`, generated and ignored by Git |

It replaces the earlier trailers in `app-trailer/` and `trailer-v2/`, which stay as records.

## Why it was rebuilt

The previous cut was eight equal four-second scenes, each title-left and phone-right, joined by the same camera move (push into the phone, cut on the screen, pull out). Smoothing that move made the repetition smoother, not more interesting. This cut keeps the artwork and changes the edit: scene lengths follow what happens in them, every transition has its own reason, and the music follows the story's energy instead of repeating one block per scene.

## Storyboard

| Time | Scene | What happens | Shot |
| --- | --- | --- | --- |
| 0 to 1 | Cold open | A drop falls on a speaker grille; the sound goes dull. "Water in the speaker?" | A graphic close-up, a slow push |
| 1 to 4.85 | Water Eject | The phone stands centred with its speaker down, as the sheet asks; the tone pushes waves, glass bubbles and droplets out of it. The title sits on one side, the 165 Hz tone as a waveform on the other. | Static, symmetrical, phone-led |
| 4.5 to 8.5 | Ruler & Protractor | The ruler crosses the frame. An arm rises from its zero to 45 degrees while the Protractor phone shows the same, then lies back down; a marker measures 76.0 mm with the Surface Ruler. The reading lifts to the left. | Horizontal, the ruler leads, the phone slides in as support |
| 8.5 to 11.5 | Unit Converter | 76.0 mm beside 7.6 cm, then Mass, Temperature, Volume and Data on syncopated beats; the shapes morph like the loading indicator. | Wide, the pair leads |
| 11.15 to 15 | Tip & Bill Splitter | The receipt prints the bill and the 10% tip, the share lands at €28.60, and the total divides into two people's cards. Then a rest. | Receipt-led, the phone rises in beside it; an almost still push during the rest |
| 15 to 19.9 | Decision Wheel | The six choices land, a quiet beat, a wind-up backwards, the spin, Music, one burst of confetti. | Full-frame wheel |
| 19.3 to 25.1 | The toolkit | Sixteen pieces of confetti fly to a grid and become the app's tools as MaterialShape badges with their names; the badges rise into a band and the five tools' real screens track in from the right on the beat; "Everyday tools, one app". | Wide, a lateral track |
| 25 to 28 | App Toolkit | Everything converges into the icon on the final downbeat; the name and the official Get it on Google Play badge settle and hold. | Centred, static hold |

## Transitions, and why each is different

| Change | How | Why |
| --- | --- | --- |
| Cold open to Water Eject | A hard cut on the first downbeat | The problem, then the answer at once: no logo before the viewer sees what the app does |
| Water Eject to Ruler | The last wave keeps travelling, loses its curve into a straight line, grows graduations; the yellow world opens outward from the line, which thickens into the ruler | A morph with a physical reason: a wave becomes a measuring line |
| Ruler to Unit Converter | A match cut: the 76.0 mm reading lifts onto a soft-burst shape the size and place of the converter's first card, which holds 76.0 mm beside 7.6 cm | Brief and quiet; the value carries the viewer, and the conversion is the app's real result |
| Unit Converter to Tip & Bill Splitter | The two cards slide together, round into one surface and stretch and whiten into the receipt, while the coral world opens out of it in a soft burst | A physical Material transformation that keeps shape, place and motion; the receipt is artwork, not the app's UI |
| Tip & Bill Splitter to Decision Wheel | A rest after the shares (the drums stop), then a hard cut on a strong accent | A change of rhythm: stillness before a choice |
| Decision Wheel to the toolkit | The result's confetti does not settle: sixteen pieces fly on and morph into the app's tools | The payoff, and the most expressive change: a choice reveals the whole collection |
| The toolkit to the icon | The cards and badges converge into the icon as it pops on the resolution | Everything gathers into one app |

Two are plain cuts, one a match cut; no two adjacent changes share a motion, and there is no shared camera.

## Sound

The arrangement follows the story's energy: a muffled tone and a drop in the cold open; soft half-time pulses and an arpeggio for the water; a four-on-the-floor with ticks on every sixteenth for the measuring, dropping out before the match cut; a syncopated bass for the converter; prints for the receipt, a bell for the share and a chime per person, then only the pad for the rest; a sparse wheel with a mallet per choice, a wind-up swell, a ratchet that follows the wheel's real rotation and a build through the spin to the film's only crash on the result; a breath, then the fullest groove for the montage, where each card lands with its tool's sound; and a held F major on the icon. Accents at the cuts are a low tom and a bell, not crashes. Chords, one per bar from 1 s: F C Dm Bb F C Dm Bb C F Dm C F.

## Motion and truth

1. One useful form turns into the next (a drop, a wave, a line, a reading, a card, a receipt, a choice, the tools), but not every change is a morph: two are cuts.
2. The UI is real and truthful: each phone and card mounts the app's screen from the UI kit in a real state. Every number on screen is the app's own: the 165 Hz Water Eject sheet; the Protractor at 45 degrees; the Surface Ruler's 76.0 mm (the drawn ruler is in millimetres and measures the same); the five conversions (76.0 mm = 7.6 cm, 500 g = 0.5 kg, 20 °C = 68 °F, 2 L = 2000 mL, 1 GB = 1000 MB); the €52.00 bill with a 10% tip, €28.60 each for two; the wheel's six choices settled on Music. The water, the ruler artwork, the receipt, the badges and the confetti are decoration, not app features. The tools named in the montage are tools the app ships.
3. Material, not illustration: MaterialShapes on the UI kit's own curves, each world's tonal palette, thick rounded strokes, two elevation levels. The end card uses Google's unaltered badge from `marketing/assets/badges/google-play`, moved and scaled whole as its guidelines allow, with the trademark line.
4. Every frame is a function of its frame number; picture and sound read the same `src/timeline.json`.

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
npm run preview            # the animatic: 960 x 540 with sound
npm run master             # 2560 x 1440, after approval
```

`npm run dev` opens Remotion Studio. All timings are seconds in `src/timeline.json`; frames are seconds times 90.

## Source

| File | Contents |
| --- | --- |
| `src/timeline.json` | The story: scenes, every beat, the transitions sheet's times, and the wheel's turn |
| `src/theme.ts` | Each world's palette, from its listing's theme |
| `src/Trailer.tsx` | The scenes, the transitions between them and the edit |
| `src/motifs/morph.ts` | MaterialShape morphing on the UI kit's own curves, for the unit shapes |
| `src/motifs/` | Phone, headline, elevation, decoration, chips, sound waves, glass bubbles, droplets and ripples, ruler, unit pair, receipt, wheel, confetti and the shape wipe; `geometry.ts` projects the 3D Pixel (from the Water Eject listing) and `confetti.ts` is the Decision Wheel listing's scatter |
| `src/LitScreen.tsx`, `src/Shape.tsx` | The UI kit's screens and MaterialShapes, as the listings use them |
| `scripts/compose.mjs` | The soundtrack, synthesised from the timeline |
| `scripts/render.mjs` | Frames, the transitions sheet, preview and master |
