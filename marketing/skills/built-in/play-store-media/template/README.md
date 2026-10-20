# Default listing: source

The default listing's visuals from October 2026: 8 phone screenshots, 4 tablet and 4 desktop screenshots, the feature
graphic and the app icon, all rendered from the app's real screens in the UI kit. Live since 2026-10-16 (upload pending).

| File | What it shows |
| --- | --- |
| `1-news.png` to `8-conversations.png` | News, Guides, the reader, Studio Bot answering, a guide attached to the chat, search, favorites, conversation history |
| tablet `1-news.png` to `4-studio-bot.png` | News, Guides, a guide beside the Guides list, Studio Bot's history beside a chat: the app's wide layout on the Pixel 9 Pro Fold, open, 3840 x 2160 |
| desktop `1-news.png` to `4-studio-bot.png` | The same four in a 1218 x 720 dp desktop window with the permanent drawer, on the Pixelbook Go, 3840 x 2160 |
| `feature-graphic.png` | The name and "News, guides and Studio Bot for Android developers", with the Guides tab and a Studio Bot answer at the sides |
| `icon.png` | The launcher foreground (the Android robot over a book) on a transparent ground, 32-bit PNG |

## How it is made

- `strings/<locale>.json`: every caption and alt text (tablet and desktop screenshots carry no captions, only alt text). A new language is a new strings file, nothing else.
- `src/store.js`: which kit screen and state each screenshot shows, the phone panorama, the tablet and desktop strips, the feature graphic and the icon. Tablet and desktop screens mount at the kit's `fold` and `desktop` windows, which apply the Android app's adaptive layout (kit/layout.js).
- `scripts/render.mjs`: renders every language into `../listing/<locale>/` (graphics and `alt-text.txt`), checks that each caption covers at most 20% of its image, flattens the screenshots and feature graphic to 24-bit PNG, and writes review strips of the three sets to `output/` (not committed). News images are downloaded into `output/images/` once.
- `metadata.json`: the metadata written into the uploaded files; after rendering, run
  `python3 ../../../../../skills/built-in/metadata/scripts/inject_metadata.py --manifest metadata.json ../listing/en-US/graphics/*/*.png`.

The eight screenshots are cut from one 8640 x 1920 panorama: one ground, and shapes centred on every seam plus a seeded
scatter, so the set reads as one strip on Google Play.

```bash
node scripts/render.mjs          # every language; or: node scripts/render.mjs en-US
```

## Decisions

- News is the live API's (`marketing/ui/scripts/fetch-content.mjs`, run before rendering): real stories with their
  publishers' artwork, as the app shows them. Guides never stand in for news.
- Shapes: no near-black tone; a scattered shape sits whole inside one image and keeps clear of the device; only the
  seam shapes cross a seam. A shape half hidden behind a device or cut by an edge looks like a mistake.
- The icon has no background of its own: Play keeps the alpha, and the robot over the book stands on its own.
- Tablet and desktop screenshots have no captions: Play wants large-screen screenshots to show only the app. The tablet
  and desktop sets are strips too, like the phone set, on the Pixel 9 Pro Fold and the Pixelbook Go (Google's device art).
- The custom listings are text only and use these graphics and `alt-text.txt` as they are: nothing to copy after a render.
