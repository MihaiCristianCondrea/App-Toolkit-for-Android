# App Toolkit: Trailer

**One app. Many everyday moments.** A 30-second 16:9 trailer: the app's name, then one small story through the five custom store listings' worlds, in which one useful form turns into the next until the app's tools gather into its icon. The tools carry no titles; they show what they are.

| | |
| --- | --- |
| Status | Animatic for owner review; master after approval |
| Sound | Our own score, `soundtrack.mp3`, synthesised by `source/scripts/compose.mjs` from the same timeline: 120 BPM in F major, 30 seconds, about -14 LUFS, peak -1 dBFS |
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
| 0 to 3.2 | App Toolkit | One shape per tool arrives on each note in its world's colour, the icon pops, the shapes breathe round it, and "App Toolkit, Everyday tools in one app" lands. Then the others swap out while the blue shape travels on. | Centred lockup |
| 3 to 6.85 | Water Eject | The blue shape reaches the phone's place and morphs into its backdrop as the blue floods in; the phone rises into it, speaker down as the sheet asks. Each speaker sends out its own wave on every note, with a bubble on every other note and droplets on two; the 165 Hz tone pulses beside it. | Phone right of centre, the tone on the left |
| 6.15 to 10.95 | Ruler & Protractor | While the last wave flattens into the ruler's line, the blue backdrop morphs and moves into the yellow one (the yellow fills it from the centre) and the phone turns in 3D: past edge-on it comes round as the Surface Ruler. The Protractor phone joins it and the ruler pulls back to a strip. An arm rises to 45 degrees with the Protractor and holds; a marker measures 76.0 mm while the Surface Ruler reads the same. Both phones end on their results. | The two phones lead, the ruler supports |
| 10.95 to 13.5 | Unit Converter | The reading's shape has expanded into the converter's ground; under 76.0 mm the first card grows, 7.6 cm beside it, then Mass, Temperature, Volume and Data on syncopated beats; the shapes morph like the loading indicator. | Wide, the pair leads |
| 13.15 to 17 | Tip & Bill Splitter | The receipt prints the bill and the 10% tip, the share lands at €28.60, and the total divides into two people's cards. Then a rest. | Receipt-led, the phone rises in beside it; an almost still push during the rest |
| 17 to 21.9 | Decision Wheel | The six choices land, a quiet beat, a wind-up backwards, the spin, Music, one burst of confetti. | Full-frame wheel |
| 21.3 to 27.1 | The toolkit | Sixteen pieces of confetti fly to a grid and become the app's tools as MaterialShape badges with their names; the badges rise into a band and the five tools' real screens track in from the right on the beat. | Wide, a lateral track |
| 27 to 30 | App Toolkit | Everything converges into the icon on the final downbeat; the name and the official Get it on Google Play badge settle and hold. | Centred, static hold |

## Transitions, and why each is different

| Change | How | Why |
| --- | --- | --- |
| App Toolkit to Water Eject | The other shapes swap out while the blue one travels to the phone's place, grows and morphs into the water backdrop as the blue floods in; the phone rises into it | The name first, kept short; the first tool comes out of the shapes that stand for the tools |
| Water Eject to Ruler | The last wave loses its curve into a straight line that grows graduations and opens the yellow world; meanwhile the blue backdrop morphs and moves into the yellow one and the phone turns in 3D, coming round as the Surface Ruler; the line thickens into the ruler | A morph with a physical reason (a wave becomes a measuring line), and one phone carried from tool to tool |
| Ruler to Unit Converter | The 76.0 mm reading lifts onto a soft-burst shape, which keeps growing until it is the converter's off-white ground; under the same text the converter's first card grows, 7.6 cm beside it | A container transform: the value carries the viewer, and the conversion is the app's real result |
| Unit Converter to Tip & Bill Splitter | The two cards slide together, round into one surface and stretch and whiten into the receipt, while the coral world opens out of it in a soft burst | A physical Material transformation that keeps shape, place and motion; the receipt is artwork, not the app's UI |
| Tip & Bill Splitter to Decision Wheel | A rest after the shares (the drums stop), then a hard cut on a strong accent | A change of rhythm: stillness before a choice |
| Decision Wheel to the toolkit | The result's confetti does not settle: sixteen pieces fly on and morph into the app's tools | The payoff, and the most expressive change: a choice reveals the whole collection |
| The toolkit to the icon | The cards and badges converge into the icon as it pops on the resolution | Everything gathers into one app |

One is a plain cut; no two adjacent changes share a motion, and there is no shared camera.

## Sound

The arrangement follows the story's energy: an F pad under the intro, a rising mallet note per shape, a bell for the icon, and a drop as the water world opens; soft half-time pulses and an arpeggio for the water; a four-on-the-floor with ticks on every sixteenth for the measuring, dropping out before the match cut; a syncopated bass for the converter; prints for the receipt, a bell for the share and a chime per person, then only the pad for the rest; a sparse wheel with a mallet per choice, a wind-up swell, a ratchet that follows the wheel's real rotation and a build through the spin to the film's only crash on the result; a breath, then the fullest groove for the montage, where each card lands with its tool's sound; and a held F major on the icon. Accents at the cuts are a low tom and a bell, not crashes. Chords, one per bar from 3 s: F C Dm Bb F C Dm Bb C F Dm C F.

## Motion and truth

1. One useful form turns into the next (a shape, a wave, a line, a reading, a card, a receipt, a choice, the tools), but not every change is a morph: one is a hard cut and one a rest. No per-tool titles: the screens and objects say what each tool is.
2. The UI is real and truthful: each phone and card mounts the app's screen from the UI kit in a real state. Every number on screen is the app's own: the 165 Hz Water Eject sheet; the Protractor at 45 degrees and the Surface Ruler's 76.0 mm, both live (the drawn ruler is in millimetres and measures the same); the five conversions (76.0 mm = 7.6 cm, 500 g = 0.5 kg, 20 °C = 68 °F, 2 L = 2000 mL, 1 GB = 1000 MB); the €52.00 bill with a 10% tip, €28.60 each for two; the wheel's six choices settled on Music. The water, the ruler artwork, the receipt, the badges and the confetti are decoration, not app features. The tools named in the montage are tools the app ships.
3. Material, not illustration: MaterialShapes on the UI kit's own curves, drawn as SVG paths so they move and turn at sub-pixel precision (CSS masks snapped to whole pixels and looked shaky); nothing drifts for the sake of it; each world's tonal palette, thick rounded strokes, two elevation levels. The end card uses Google's unaltered badge from `marketing/assets/badges/google-play`, moved and scaled whole as its guidelines allow, with the trademark line.
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
