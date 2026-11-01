---
name: play-store-media
description: >
  Make an app's Google Play visuals from its real screens: 8 phone screenshots
  cut from one continuous panorama, the feature graphic and the app icon, with
  captions and alt text per language, rendered by code and checked against
  Play's rules. Use for a new default listing design or a custom store listing
  for one audience. Works with store-listing, which keeps the listings, their files and retired uploads.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-16'
  keywords:
  - google-play
  - screenshots
  - feature-graphic
  - app-icon
  - aso
  - custom-store-listings
---

# Play Store media

Store visuals are a design, built like a campaign: real app screens from the UI kit, one look, one source that renders every size and language. Where the files go, how a listing changes and where replaced uploads go is in [`store-listing`](../store-listing/SKILL.md). Every word follows [`writing-style`](../writing-style/SKILL.md).

## 1. Ask first

1. **Which listing?** The default listing, or a custom store listing. A custom listing is for one audience (for example "developers learning Jetpack Compose"), with its own screenshots order, captions, text and search keywords. Ask who the audience is.
2. **Languages:** captions and alt text are written in English. Ask whether to translate them; the design renders any language from its strings file.
3. **Look:** pick one style from `design-taste/` (or the project's brand) and the Pixel shape language from [`motion-video/references/pixel-style.md`](../motion-video/references/pixel-style.md). Offer three style frames of screenshot 1 when the owner hasn't chosen.

## 2. Google Play's rules (checked by the validator)

| Asset | Rule |
| --- | --- |
| Phone screenshots | 2 to 8 per device type; 8 for the listing. JPEG or 24-bit PNG, no alpha. Sides 320 to 3,840 px, long side at most twice the short side. For Play's promotion surfaces: 1080 x 1920 (9:16) or larger. |
| First three screenshots | Mostly the app's UI. |
| Captions on screenshots | Only if needed, at most 20% of the image, large and readable, on a calm background. |
| Status bar | Clean: no notifications or carrier; full Wi-Fi, signal and battery. |
| Alt text | One per screenshot, at most 140 characters, never starting with "Photo of" or "Image of". |
| Tablet and Chromebook | At least 4 each, 1,080 to 7,680 px, 16:9 or 9:16, no text that is not part of the app (no captions). They must show the app's own large-screen layout, not a phone screen stretched or centred. |
| Feature graphic | 1024 x 500, JPEG or 24-bit PNG, no alpha. Key content centred (edges get cropped), strong colour (not plain white, black or dark grey); no device frames, no store badges, no icon duplicate. |
| App icon | 512 x 512, 32-bit PNG with alpha, at most 1,024 KB, no text or badges. Play keeps the alpha and applies its mask. |
| Everywhere | No rankings or praise ("Best", "#1", "Top", "New"), no prices or promotions, no download counts, no testimonials, no calls to action ("Download now"), nothing time-limited, no third-party trademarks or other stores' badges (the Google Play badge used in social posts is not allowed here). |

Translated text without translated graphics shows the default language's graphics, so translate captions and graphics together.

## 3. Design rules

- **Real screens only.** Every screenshot mounts a real screen and state from the UI kit ([`marketing-ui`](../marketing-ui/SKILL.md)), in the device frame. Content comes from the app's live API: refresh the UI kit's content snapshot before rendering, and serve remote images through the renderer (downloaded once, outside the browser, if a sandbox blocks them). One content type never stands in for another: a news story shows its own artwork, never a guide's cover.
- **One continuous strip.** The phone screenshots are cut from one panorama (N x 1080 by 1920 px): one ground colour, shapes centred on every seam so each pair of neighbours shares a shape, and a seeded scatter of shapes filling every gap, the top band included. Placed side by side the images read as one picture; no area feels empty. Shapes keep clear of the captions and may run behind the phones and off the edges.
- **Varied shapes, never overlapping.** Mix large and small Material shapes, solid fills in the palette's tones, a few outlined lines (a wave) and dot grids, rotated. No shape overlaps another: every placed shape is an obstacle for the next, with a margin. Leave out the pixel shapes (`pixel_triangle`, `pixel_circle`): they clash with the rounded set. Use the same seed for every render so the strip never changes between renders.
- **Shapes that never bother.** A scattered shape sits whole inside one image and keeps clear of the device by a margin; only the seam shapes cross a seam or an edge. A small shape half hidden behind a device, or cut by the image's edge, looks like a mistake. Leave out the palette's near-black tones (on-primary-container and the like): small dark shapes read as holes in the ground.
- **No ads.** Screenshots never show ad UI: mount screens with ads off (the kit's default).
- **Large screens show the large-screen app.** Tablet and desktop screenshots mount the kit's screens at a wide window (`window: "fold"` or `"desktop"`, see [`marketing-ui`](../marketing-ui/SKILL.md)), which apply the Android client's own adaptive rules: the rail or permanent drawer, the grid columns, list and detail side by side. If the kit can't draw a screen at that width yet, port the rule from the Android code first; never fake it in the store source.
- **Tablet set.** The app on the Pixel 9 Pro Fold, open (Material Next's `device-frames/pixel_9_pro_fold`, copied with `npx mnw-marketing-assets`; see `assets/device-frames/README.md`), centred and unscaled on a 1920 x 1080 canvas captured at 2x (3840 x 2160). The images are cut from one strip like the phone set: the same ground, shapes on the seams, a seeded scatter that never overlaps a shape or a device. No captions.
- **Desktop set.** The app's desktop window on the Pixelbook Go (Google's device art from the Device Art Generator, `assets/device-frames/pixelbook_go`: shadow, laptop, screen; the glare layer left out), centred and unscaled on a 1920 x 1080 canvas at 2x (3840 x 2160), cut from one strip like the tablet set. No captions.
- **Captions.** Two short lines in the top band, one of them on a pill; the screen's benefit in the user's words. Under 20% of the image (the renderer measures it).
- **Order.** Lead with the product's core: the first three tell what the app is. Then the features, then the habit (saving, history).
- **Feature graphic.** The name and one line of what the app is, centred; real UI fragments at the sides; no device frame.
- **App icon.** Render it from the launcher icon itself, never redrawn. Keep it a PNG without a background of its own when the foreground artwork stands on its own (Play supports transparency); add the launcher's background only when the artwork needs it to read. Capture it with a transparent page background (`omitBackground`), or the page colour fills the alpha.

## 4. Build it

Each listing has one source that renders every language: `marketing/creative/store/google-play/listings/<listing>/source/` (layout in [`store-listing`](../store-listing/SKILL.md)). Start it from [`template/`](template/), a complete working source (Android Insider's default listing, October 2026):

```text
source/
├── README.md               what the visuals show, decisions, how to render
├── metadata.json           metadata manifest for the uploaded files (paths point into ../listing/)
├── strings/<locale>.json   captions (lines + pill) and alt text per phone screenshot; tablet and desktop ids and alt text; feature graphic text
├── index.html, package.json
├── src/store.js            screens and states, phone panorama, tablet strip, desktop windows, feature graphic, icon; reads ?locale= and ?shot=
├── src/store.css
├── scripts/render.mjs      renders every strings/<locale>.json into ../listing/<locale>/graphics/ and alt-text.txt
└── output/                 review strips and downloaded images; not committed
```

Change `SCREENS` (screen ids and states), the strings, the palette tokens and the icon paths for the project. The template reaches the UI kit at `marketing/ui` from `listings/<listing>/source/`; for a custom listing (`listings/custom/<id>/source/`) add one `../` to the UI kit imports in `src/store.js` and `scripts/render.mjs`, and to `marketing` in `render.mjs`.

A custom listing that reuses the default listing's look keeps its own `source/` with its own strings and screen order; copy the default source and change those. One that shows the default listing's graphics as they are is text only: its `source/README.md` says so, nothing is copied after a render, and the default's graphics and `alt-text.txt` are uploaded with it.

## 5. Publish

1. If the images being replaced have no source (device screenshots), keep their listing as a retired listing, `listings/<name>-legacy/` with a full README, or move them into a custom listing that still uses them ([`store-listing`](../store-listing/SKILL.md)). Run the content snapshot refresh.
2. `node scripts/render.mjs` in `source/` (all languages, or one: `node scripts/render.mjs en-US`). It writes straight into `listing/<locale>/`. Look at `output/panorama-<locale>.png`, the tablet and desktop strips, and every image.
3. Run the store's validator.
4. Write metadata into every published file with [`metadata`](../metadata/SKILL.md): a manifest per design, the alt text as each file's `altText`, Android Studio as the software, no tool names.
5. Upload in Play Console (or with Gradle Play Publisher), and paste the alt text per screenshot.
