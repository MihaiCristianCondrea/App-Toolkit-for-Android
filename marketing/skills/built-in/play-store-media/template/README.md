# Default listing: source

The default listing's visuals from October 2026: 8 phone screenshots, the feature graphic and the app icon, all rendered
from the app's real screens in the UI kit. Live since 2026-10-16 (upload pending).

| File | What it shows |
| --- | --- |
| `1-news.png` to `8-conversations.png` | News, Guides, the reader, Studio Bot answering, a guide attached to the chat, search, favorites, conversation history |
| `feature-graphic.png` | The name and "News, guides and Studio Bot for Android developers", with the Guides tab and a Studio Bot answer at the sides |
| `icon.png` | The launcher icon (white background, Android robot over a book), full bleed: Play applies the mask |

## How it is made

- `strings/<locale>.json`: every caption and alt text. A new language is a new strings file, nothing else.
- `src/store.js`: which kit screen and state each screenshot shows, the panorama's shapes, the feature graphic and the icon.
- `scripts/render.mjs`: renders every language into `../listing/<locale>/` (graphics and `alt-text.txt`), checks that each caption covers at most 20% of its image, flattens the screenshots and feature graphic to 24-bit PNG, and writes a review panorama to `output/` (not committed).
- `metadata.json`: the metadata written into the uploaded files; after rendering, run
  `python3 ../../../../../skills/built-in/metadata/scripts/inject_metadata.py --manifest metadata.json ../listing/en-US/graphics/*/*.png`.

The eight screenshots are cut from one 8640 x 1920 panorama: one ground, and shapes centred on every seam plus a seeded
scatter, so the set reads as one strip on Google Play.

```bash
node scripts/render.mjs          # every language; or: node scripts/render.mjs en-US
```

## Decisions

- News stories link their publishers' artwork, which store graphics may not show (third-party trademarks). The renderer
  replaces every external image with the app's own guide artwork. The News screenshot's headlines are the real stories.
- Tablet and desktop screenshots are still the v1 set (not rendered by this source).
