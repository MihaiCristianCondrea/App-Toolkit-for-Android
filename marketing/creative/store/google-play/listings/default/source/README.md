# Default listing: source

App Toolkit's default listing visuals refreshed October 30, 2026: 8 phone, 4 tablet and 4 desktop screenshots, the feature
graphic and the icon, all rendered with Remotion from the UI kit's real screens (`marketing/ui`). They replace the
sourceless visuals now kept in [`../../default-legacy/`](../../default-legacy/README.md).

**Upload with the redesigned app, not before.** The screenshots and the text show the redesign as the UI kit draws
it: Measure, Play & Decide, the Decision Wheel and Water Eject are not in the Compose app on Google Play yet. Play
requires the listing to show the app people install, so these go up with the release that ships the redesign.

| File | What it shows |
| --- | --- |
| phone `1-quick-tools.png` to `8-sos.png` | Quick Tools, Measure, Play & Decide, the Decision Wheel picking Sushi, the full-screen Protractor at 60 degrees, the Tip & Bill Splitter, Water Eject, Morse & SOS |
| tablet `1-quick-tools.png` to `4-unit-converter.png` | Quick Tools, Measure, Play & Decide and the Unit Converter in the app's foldable layout (navigation rail) on the Pixel 9 Pro Fold, open, 3840 x 2160 |
| desktop `1-quick-tools.png` to `4-unit-converter.png` | The same four in a 1218 x 720 dp desktop window with the permanent drawer, on the Pixelbook Go, 3840 x 2160 |
| `feature-graphic.png` | "App Toolkit" and "Everyday tools to measure, signal and decide", with Quick Tools and Play & Decide at the sides |
| `icon.png` | The toolbox (`marketing/assets/logos/icon.svg`, from `.idea/icon.svg`) on a transparent ground, 32-bit PNG |

## How it is made

A Remotion project of stills (`src/Root.tsx`), one per graphic, each a pure function of its props (`locale`, `index`):

- `strings/<locale>.json`: every caption and alt text, and the feature graphic's text. A new language is a new
  strings file listed in `src/strings.ts`.
- `src/screens.ts`: which kit screen and state each screenshot mounts.
- `src/LitScreen.tsx`: mounts a kit screen through `marketing/ui/src/embed.js`, in its device frame and window
  (`phone`, `fold`, `desktop`), and holds the still until Material Web has settled. Ads are off.
- `src/Phone.tsx`: the 8640 x 1920 phone panorama (one ground, shapes centred on every seam, a seeded scatter clear of
  the captions, the phones and each other) and `PhoneShot`, which frames one 1080 x 1920 screenshot of it. A caption
  over 20% of its image fails the render.
- `src/Wide.tsx`: the tablet and desktop strips, laid out at 1920 x 1080 and rendered at 2x.
- `src/FeatureGraphic.tsx`, `src/Icon.tsx`; `src/theme.ts` holds the toolbox's ambers; `src/Shape.tsx` draws the UI
  kit's own MaterialShapes curves.
- `scripts/render.mjs`: bundles once, renders every still per language into `../listing/<locale>/graphics/`,
  flattens the screenshots and the feature graphic to 24-bit PNG, writes `alt-text.txt`, and writes review strips to
  `output/` (not committed).
- `metadata.json`: the metadata written into the uploaded files.

```bash
cd ../../../../../../ui && npm install && npm run assets    # the kit and its assets first
cd - && npm install
npm run dev                                                  # Remotion Studio, to preview every still
npm run render                                               # every language; or: npm run render -- en-US
python3 ../../../../../../skills/built-in/metadata/scripts/inject_metadata.py --manifest metadata.json \
  --readme ../../../../../../README.md ../listing/en-US/graphics/*/*.png
cd ../../.. && node scripts/validate.mjs
```

`REMOTION_BROWSER` (or `CHROME_PATH`) points the render at a local Chrome Headless Shell; without it Remotion
downloads its own.

## Decisions

- One look for the three sets: a warm cream ground with the toolbox's ambers for the shapes and the caption pill, and
  the app's screens in their own colours (the kit's `captured` palette, what the app opens with), so the UI stands
  out from the ground.
- The first three screenshots are the three tabs, so they say what the app is; then the tools added in the redesign.
- Water Eject's caption says what it does ("A tone for a wet speaker"), as the app does: it can help, it doesn't
  repair water damage.
- The Decision Wheel shows typed choices and rests on its pick under the pointer, the state a spin leaves it in.
- Tablet and desktop screenshots carry no captions: Play wants large-screen screenshots to show only the app.
- The icon has no background of its own: Play keeps the alpha. On a white page the cream lid has little edge; that
  comes from the artwork and the launcher's white background, not from this render.


The October 30 audit removes the outdated wallpaper-colour claim from the description. The
Protractor artwork is refreshed from the current kit without the removed Angle label or instructions.
The More apps footer and its details are ad-free and optional; the three primary tabs stay intact.
