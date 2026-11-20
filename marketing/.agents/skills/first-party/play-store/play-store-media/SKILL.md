---
name: play-store-media
description: >
  Render Google Play phone, tablet and foldable screenshots, feature graphics
  and app icons from real app screens and artwork. Validate aspect ratios, device
  rendering, exports, captions and alt text per locale
  against Play's rules. Use for a new default listing design or a custom store listing
  for one audience. Works with play-store-listing, which keeps the listings, their files and retired uploads.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-21'
  keywords:
  - google-play
  - screenshots
  - foldables
  - pixel-3d
  - tablet
  - feature-graphic
  - app-icon
  - aso
  - custom-store-listings
---

# Play Store media

Store visuals are a design, built like a campaign: real app screens from the UI kit, one look, one source that renders every size and language. Where the files go, how a listing changes and where replaced uploads go is in [`play-store-listing`](../play-store-listing/SKILL.md). Every word follows [`writing-style`](../../../../../../.agents/skills/first-party/writing-style/SKILL.md).

## 1. Ask first

1. **Which listing?** The default listing, or a custom store listing. A custom listing is for one audience (for example "developers learning Jetpack Compose"), with its own screenshots order, captions, text and search keywords. Ask who the audience is.
2. **Languages:** captions and alt text are written in English. Ask whether to translate them; the design renders any language from its strings file.
3. **Look.** Start with the approved app brand. Use [Material UI](../../../official/google/material-ui/SKILL.md) for Material guidance and [Taste](../../../third-party/leonxlnx/taste-skill/SKILL.md) when exploring composition. Reuse suitable [shared marketing assets](../../../../../assets/README.md). Review representative shots before applying a new look to the set.

## 2. Store checks

Use [Play Console's preview asset guidance](https://support.google.com/googleplay/android-developer/answer/9866151) for current requirements and promotion recommendations, including dedicated rules for foldables, Wear OS, TV, Automotive and XR. The bundled listing validator checks screenshot ratios and has separate foldable dimension, size, count and aspect-ratio checks. Recheck Google's live limits before uploading.

| Asset | Rule |
| --- | --- |
| Phone screenshots | Play requires at least 2 screenshots across device types and allows up to 8 per device type. JPEG or 24-bit PNG, no alpha. Sides 320 to 3,840 px, long side at most twice the short side. For Play promotion formats that use app screenshots, supply at least four at 1080 x 1920 (9:16) or 1920 x 1080 (16:9) or larger. |
| First three screenshots | Mostly the app's UI. |
| Captions on screenshots | Only if needed, at most 20% of the image, large and readable, on a calm background. |
| Status bar | Clean: no notifications or carrier; full Wi-Fi, signal and battery. |
| Alt text | One per screenshot, at most 140 characters, never starting with "Photo of" or "Image of". |
| Tablet and Chromebook | For large-screen promotion, supply at least 4 per target, 16:9 or 9:16, with each side between 1,080 and 3,840 px for the bundled validator. Keep text within the app UI. They must show the app's own large-screen layout, not a phone screen stretched or centred. |
| Foldable screenshots (apps) | Optional dedicated Play set, but when provided upload **3 to 8** PNG/JPEG images, each **960 to 3,840 px per side**, **8 MB maximum**. Include at least **one 1:1 unfolded square inner-display** screenshot, **one 4:3 landscape unfolded wide inner-display** screenshot, and **one 10:16 portrait folded outer-display** screenshot. Other allowed ratios for additional foldable images are 3:4, 16:10, 16:9 and 9:16. Use the dedicated Foldable Screenshots category, not tablet or phone screenshot folders. |
| Feature graphic | 1024 x 500, JPEG or 24-bit PNG, no alpha. Key content centred (edges get cropped), strong colour (not plain white, black or dark grey); no device frames, no store badges, no icon duplicate. |
| App icon | 512 x 512, 32-bit PNG with alpha, at most 1,024 KB, no text or badges. Play keeps the alpha and applies its mask. |
| Everywhere | No rankings or praise ("Best", "#1", "Top", "New"), no prices or promotions, no download counts, no testimonials, no calls to action ("Download now"), nothing time-limited, no unrelated third-party trademarks or implied endorsements. A correctly depicted Pixel device may show its authentic back mark if the artwork and usage comply with applicable branding rules. Do not place the Google Play badge or other store badges inside listing screenshots or feature graphics. |

Translated text without translated graphics shows the default language's graphics, so translate captions and graphics together.

## 3. Design rules

- **Real screens only.** Mount supported screens and states from [Marketing UI](../../marketing/marketing-ui/SKILL.md). Keep the real app's content, interaction states, window layout and display geometry intact. Refresh the content snapshot when the app uses one, and render each content type with its actual artwork.
- **Phone screenshots: use Material Next's 3D Pixel.** Default to the Pixel 9 Pro XL model in Material Next `<mnw-device-frame device="pixel_9_pro_xl">`, with the real Marketing UI phone screen in the display. Supply the Google mark on the phone's back through the frame's `logo` property using a properly sourced, host-owned white-on-transparent logo mask. The 3D model does **not** include this logo by default. Preserve a crisp, readable front screen; use an angled or back view only when it adds value and keeps the product clearly represented. Wait for `frame.updateComplete` and verify WebGL actually rendered before capturing. Do not substitute a flat generic phone frame or invent the app UI.
- **Tablet screenshots: use tablet art.** Use `marketing/assets/device-frames/pixel_tablet/` (`back.webp`, `mask.webp`, `layout`) as the tablet device asset, composited around the real tablet-window Marketing UI screen. `<mnw-device-frame>` does not currently provide a Pixel Tablet preset, so do not invent `device="pixel_tablet"`. Match the display opening using the asset's layout data. Verify the app's actual large-screen layout and legibility.
- **Foldable screenshots: capture distinct postures.** Produce the exact **1:1 unfolded square inner**, **4:3 landscape unfolded wide inner**, and **10:16 portrait folded outer** app layouts; do not crop, stretch or rotate a phone screenshot to imitate a foldable. Use the corresponding emulator profiles or verified Marketing UI window sizes and compare with the actual Compose UI. Existing Pixel 9 Pro Fold open/closed art may frame matching captures, but device decoration is optional and never changes the required screenshot aspect ratio. Ensure the rendered *entire image* matches the intended Play aspect ratio, not just the screen inset.
- **Choose a composition for the listing.** Use a continuous panorama, standalone frames, a related series of screenshots, or paired devices when a comparison needs both screens. Keep each image legible on its own and use a coherent type and color system across the set.
- **Panorama option.** Reserve caption and device bounds. Place seam art deliberately and check both the joined strip and each crop. Use a fixed seed for procedural decoration. Keep decorative assets clear of text and key UI in every layout.
- **No ads.** Screenshots never show ad UI: mount screens with ads off (the kit's default).
- **Large screens show the large-screen app.** Tablet and desktop screenshots mount the kit's screens at a wide window (`window: "fold"` or `"desktop"`, see [`marketing-ui`](../../marketing/marketing-ui/SKILL.md)), which apply the Android client's own adaptive rules: the rail or permanent drawer, the grid columns, list and detail side by side. If the kit can't draw a screen at that width yet, port the rule from the Android code first; never fake it in the store source.
- **Tablet and desktop sets.** Use the Pixel Tablet asset for tablets. For desktop, choose device art that fits the actual window or use direct captures. Apply the required output dimensions for each target and keep app controls legible. Leave captions off these layouts. Foldables use their own 1:1, 4:3 and 10:16 export profiles.
- **Captions.** Keep them optional, brief and readable at listing size. If a listing uses the two-line pill style, use the [caption reference](references/captions.md) for its layout and checks.
- **Order.** Lead with the product's core: the first three tell what the app is. Then the features, then the habit (saving, history).
- **Feature graphic.** The name and one line of what the app is, centred; real UI fragments at the sides; no device frame.
- **App icon.** Render it from the launcher icon itself, never redrawn. Keep it a PNG without a background of its own when the foreground artwork stands on its own (Play supports transparency); add the launcher's background only when the artwork needs it to read. Capture it with a transparent page background (`omitBackground`), or the page colour fills the alpha.

## 4. Build it

Each listing has one source that renders every language: `marketing/creative/store/google-play/listings/<listing>/source/` (layout in [`play-store-listing`](../play-store-listing/SKILL.md)). Start it from [`template`](template/), a complete working source (Android Insider's default listing, October 2026):

```text
source/
├── README.md               what the visuals show, decisions, how to render
├── metadata.json           metadata manifest for the uploaded files (paths point into ../listing/)
├── strings/<locale>.json   captions and alt text per screenshot, including foldable posture; feature graphic text
├── index.html, package.json
├── src/store.js            screens and states, selectable compositions and device targets; reads ?locale= and ?shot=
├── src/store.css
├── scripts/render.mjs      renders every strings/<locale>.json into ../listing/<locale>/graphics/ and alt-text.txt
└── output/                 review strips and downloaded images; not committed
```

The template demonstrates an older panorama and does not yet generate dedicated foldable shots. Update screen IDs, palette, strings, layout and render checks for the chosen composition. For a foldable listing, add **three distinct responsive captures** to `listing/<locale>/graphics/foldable-screenshots/` (1:1, 4:3, 10:16), extend `alt-text.txt` for those files and run the supplied validator. Do not claim the starter's render script produces foldables automatically. Keep the store validator in place. The template reaches the UI kit at `marketing/ui` from `listings/<listing>/source/`. For `listings/custom/<id>/source/`, add one `../` to the UI kit imports in `src/store.js` and `scripts/render.mjs`, and to `marketing` in `render.mjs`.

A custom listing that reuses the default listing's look keeps its own `source/` with its own strings and screen order; copy the default source and change those. One that shows the default listing's graphics as they are is text only: its `source/README.md` says so, nothing is copied after a render, and the default's graphics and `alt-text.txt` are uploaded with it.

## 5. Publish

1. If the images being replaced have no source (device screenshots), keep their listing as a retired listing, `listings/<name>-legacy/` with a full README, or move them into a custom listing that still uses them ([`play-store-listing`](../play-store-listing/SKILL.md)). Run the content snapshot refresh.
2. `node scripts/render.mjs` in `source/` (all languages, or one: `node scripts/render.mjs en-US`). It writes straight into `listing/<locale>/`. Review each image at listing size and full size. Review the joined strips when the composition uses them.
3. Run the store's validator. For every included foldable set, verify the presence of 1:1 unfolded, 4:3 unfolded and 10:16 folded images, with distinct real UI layouts; for phones, verify the Material Next 3D Pixel and supplied back logo; for tablets, verify the Pixel Tablet asset.
4. Write metadata into every published file with [`metadata`](../../metadata/files-metadata/SKILL.md): record each screenshot's alt text in its file entry and verify the output metadata.
5. Upload in Play Console (or with Gradle Play Publisher), and paste the alt text per screenshot.
