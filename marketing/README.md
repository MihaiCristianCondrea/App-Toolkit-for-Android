# App Toolkit marketing

Local previews, store listing files, campaigns and shared assets for the **App Toolkit for Android** app on Google
Play. Nothing here is packaged in the app. The library is used only by the owner's own apps and is not marketed. The
Compose sample app is the reference for product screens; marketing composition may vary around those screens.

Start any marketing task from [`STARTUP.md`](STARTUP.md): it routes each task to the skills in [`skills/`](skills/llms.txt),
in order.

## Layout

| Folder | Contents |
| --- | --- |
| [STARTUP.md](STARTUP.md) | Task router: which skills, in which order |
| [briefs](briefs) | App Toolkit brand and product positioning |
| [ui](ui/README.md) | The UI kit: Material Next hub with Lit and Material Web screens, previews and capture tools |
| [creative/store/google-play](creative/store/google-play/README.md) | Google Play store listing: the default listing (custom listings per tool later), with its source and the uploaded files per language |
| [creative/social-campaigns](creative/social-campaigns) | One folder per campaign in progress (`creative/social-campaigns/<campaign-slug>/`), every media type; published ones in `completed/` |
| [creative/templates](creative/templates) | Starting points for new work: [`ui-motion-starter`](creative/templates/ui-motion-starter/README.md), Remotion over the UI kit's live screens |
| [research](research/README.md) | Google Analytics exports: context, not claims |
| [assets](assets) | Logos, illustrations, `MaterialShapes` SVGs, device frames and the official Play badge; `ideas/` holds redesign input until the redesign has used it |
| [skills](skills/llms.txt) | `built-in/` (ours, from [android-skills](https://github.com/MihaiCristianCondrea/android-skills)), `design-taste/`, `google/` and `social-media/` (installed, never edited) |

### Campaigns

None yet. The first one follows the app redesign, see
[`docs/plans/redesign-and-marketing.md`](../docs/plans/redesign-and-marketing.md).

A campaign folder holds `README.md`, `<campaign-slug>-metadata.json`, `captions/`, `source/` (the code that makes the
media) and `output/` (finished files by type: `video/`, `images/`, `audio/`). `output/` is never committed.

## Preview and render

```bash
cd marketing/ui
npm install
npm run dev          # or open-marketing-ui.cmd / open-marketing-ui.sh
npm run build
npm run qa           # browser checks of the hub and every screen

cd ../creative/store/google-play
node scripts/validate.mjs

cd ../../templates/ui-motion-starter
npm install
npm run dev          # Remotion Studio, or open-motion-studio.cmd
npm run render
```

## Render quality

New renders are 2K, and video runs at 90 fps. Lay a frame out at its design size (1920x1080, 1080x1920, 1080x1440 or
1080x1080) and render it at 4/3 scale with `Config.setScale()` in `remotion.config.ts`. Write timing in seconds and turn
it into frames with the `fps` from `useVideoConfig()`, so the frame rate is one number in `Root.tsx`, as
`ui-motion-starter` does. The 2K 90 fps render is the master; a platform that needs another frame rate gets a new
render at that rate, not a converted file.

## Product fidelity

- Use Material Web components where they match the app. Themes use App Toolkit's generated color roles, Google Sans
  Flex, Material Symbols and `MaterialShapes`.
- Reuse `ui/src/embed.js`, the screen modules and kit components. Avoid parallel mock interfaces.
- Show actual behavior and strings. Show only tools the app has; no features from the owner's other apps, no ad
  placements in store or campaign output.
- Use existing logos, illustrations and code-rendered screens. Preserve the official Play badge and its usage rules.
- Keep copy short and concrete. Do not invent statistics, endorsements or features, and do not quote `research/`
  numbers.

## Completed campaigns and review

Published campaigns live in `creative/social-campaigns/completed/` as a record. Do not edit or re-render them without
an explicit request. A new idea gets a new campaign. Work awaiting review stays in
`creative/social-campaigns/<campaign-slug>/` with editable sources and captions; reviewers get the rendered files sent
to them, since `output/` is not committed. Once published, move the campaign into `completed/` and fix its relative
paths for the extra folder level.

Check every rendered frame and the contact sheet for clipped text, missing fonts or images, misleading states and UI
drift. A successful build alone does not establish visual parity. Record checks and known limits in the campaign's
README.

## Project metadata

Read by the `metadata` skill for every file it tags, and by `social-captions`. Change it here, not in individual
campaigns.

<!-- project-metadata -->
```json
{
  "project": {
    "name": "App Toolkit for Android",
    "shortName": "App Toolkit",
    "packageId": "com.d4rk.android.apps.apptoolkit",
    "storeUrl": "https://play.google.com/store/apps/details?id=com.d4rk.android.apps.apptoolkit",
    "url": "https://play.google.com/store/apps/details?id=com.d4rk.android.apps.apptoolkit",
    "repository": "https://github.com/MihaiCristianCondrea/App-Toolkit-for-Android",
    "category": "Tools",
    "language": "en",
    "monetization": "Free with ads",
    "formFactors": ["Phone", "Tablet", "Foldable", "Desktop"],
    "keywords": ["App Toolkit", "Android", "Quick Settings tiles", "compass", "bubble level", "unit converter", "tip calculator", "counter"]
  },
  "credits": {
    "creator": "Mihai-Cristian Condrea",
    "copyrightHolder": "Mihai-Cristian Condrea",
    "country": "Romania",
    "license": "All rights reserved",
    "contact": "https://mihaicristiancondrea.github.io/profile/#contact",
    "profiles": {
      "GitHub": "https://github.com/MihaiCristianCondrea",
      "X": "https://x.com/MihaiCristianC",
      "Facebook": "https://www.facebook.com/dev.mihaicristiancondrea",
      "TikTok": "https://www.tiktok.com/@dev.mihaicristiancondrea",
      "Instagram": "https://www.instagram.com/dev.mihaicristiancondrea"
    }
  }
}
```
