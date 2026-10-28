---
name: social-captions
description: >
  Write the post text for a finished campaign, one paste-ready file per
  platform (youtube.txt, instagram.txt, x.txt, linkedin.txt, tiktok.txt,
  facebook.txt, threads.txt). Asks the developer which platforms the campaign
  goes to, reads the project block in marketing/README.md and the campaign
  manifest, and writes marketing/creative/social-campaigns/<campaign-slug>/captions/.
  Use when a video, carousel or image campaign is ready to publish.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-16'
  keywords:
  - captions
  - social-media
  - youtube
  - instagram
  - linkedin
  - hashtags
  - seo
---

# Social captions

One campaign, one set of facts, one file per platform. Every word follows [`writing-style`](../writing-style/SKILL.md).

## 1. Ask first

Before writing, ask the developer two things, with the usual answers as defaults:

1. **Captions for this campaign?** Usually yes. If no, stop here.
2. **Which platforms?** Offer the list below; preselect the ones earlier campaigns in `creative/social-campaigns/` used.

| Platform | File | Template and example |
| --- | --- | --- |
| YouTube | `youtube.txt`, `youtube-tags.txt` | [`references/youtube.md`](references/youtube.md) |
| Instagram | `instagram.txt` | [`references/instagram.md`](references/instagram.md) |
| X | `x.txt` | [`references/x.md`](references/x.md) |
| LinkedIn | `linkedin.txt` | [`references/linkedin.md`](references/linkedin.md) |
| TikTok | `tiktok.txt` | [`references/tiktok.md`](references/tiktok.md) |
| Facebook | `facebook.txt` | [`references/facebook.md`](references/facebook.md) |
| Threads | `threads.txt` | [`references/threads.md`](references/threads.md) |

Write only the files for the platforms chosen. For a platform not in the list, follow the closest one and the platform's own limits.

## 2. Gather the facts

1. The project block in `marketing/README.md` ("Project metadata"): app name, store URL, category, keywords, credits and profiles.
2. The campaign manifest `<campaign-slug>-metadata.json`: title, description, keywords, hashtags, per-file alt text.
3. The campaign README and the media itself: what the viewer actually sees.

Both files come from the [`metadata`](../metadata/SKILL.md) skill. If the manifest doesn't exist yet, run that skill first, so the captions and the files say the same thing.

Pick keywords and hashtags for the audience the product is for, following [`references/keywords.md`](references/keywords.md). A note-taking app for students gets tags students search for, and for TikTok the installed skills in `skills/social-media/tiktok/` (hooks, captions, audience insights) go deeper; developer tags (`#AndroidDev`, `#JetpackCompose`) only belong when the audience is developers.

## 3. Write the files

```text
marketing/creative/social-campaigns/<campaign-slug>/captions/
├── youtube.txt
├── instagram.txt
└── ...
```

**Each file is exactly the text to paste into the platform's post box, and nothing else.**

- No headings, no labels ("Caption:", "Post:"), no notes, no Markdown: no `**bold**`, lists with `-`, links in `[]()` or code fences.
- Line breaks only where the post should have them.
- Plain text files (`.txt`, UTF-8): open, select all, copy, paste.

What is not post text lives elsewhere:

- YouTube also gets `youtube-tags.txt` (see [`references/youtube.md`](references/youtube.md), "Tags").
- The YouTube title, the alt text for each image and the thumbnail text are in the campaign manifest (`copy.title`, `files.<path>.altText`), written by the metadata skill. The person posting copies them from there.
- Trademark notices required by a badge in the media ("Google Play and the Google Play logo are trademarks of Google LLC.") go at the end of the post text itself.

## 4. Facts that never change

The store link uses the package from the project block:

```text
https://play.google.com/store/apps/details?id=<packageId>
```

The creator block, where a platform's template uses it, comes from the project block's profiles, in order and unmodified.

## 5. Check before handing over

- The app name, package and store URL match the project block exactly.
- Each file is within its platform's limits (see its reference) and contains only paste-ready text.
- The hashtags fit the product's audience, not a default list.
- No em dashes, no emojis, none of the banned words in [`writing-style`](../writing-style/SKILL.md).
