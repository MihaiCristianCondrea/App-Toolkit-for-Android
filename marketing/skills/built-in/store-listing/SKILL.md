---
name: store-listing
description: >
  Keep an app's Google Play store listings in the repository under
  marketing/store/google-play/listings/: the default listing and custom store
  listings, each with source/ (what renders it) and listing/<locale>/ (what is
  uploaded), plus status, keywords, history and a validator for Play's rules.
  Use when changing listing text, publishing new visuals, adding a language or
  a custom listing, or moving an existing listing (app/src/main/play) in.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-16'
  keywords:
  - google-play
  - store-listing
  - custom-store-listings
  - aso
  - keywords
---

# Store listing

Every store listing lives in one place, and every listing has the same two parts: the source that makes it and the files that are uploaded. Text follows [`writing-style`](../writing-style/SKILL.md); visuals are made with [`play-store-media`](../play-store-media/SKILL.md).

## Layout

```text
marketing/store/google-play/
├── README.md                       a table of the listings and their status
├── contact-email.txt, contact-website.txt, default-language.txt
├── scripts/validate.mjs            Play's rules for every listing and language (copy from this skill)
└── listings/
    ├── default/                    the main store listing
    │   ├── source/                 code, strings/<locale>.json, metadata.json, README
    │   ├── listing/
    │   │   └── <locale>/           title.txt, short-description.txt, full-description.txt, alt-text.txt
    │   │       └── graphics/       icon/, feature-graphic/, phone-screenshots/, tablet-screenshots/, desktop-screenshots/
    │   └── history/<yyyy-mm-dd>-<name>/   uploads that cannot be rebuilt from source/, kept as they were
    └── custom/
        ├── README.md
        ├── <id>/                   a custom store listing: the same source/ and listing/, plus listing.json and keywords.txt
        └── removed/<id>/           custom listings deleted from Play Console
```

- **One source, every language.** `source/strings/<locale>.json` renders into `listing/<locale>/`. A new language is a new strings file plus its translated text files.
- **`listing/` is the record of what is on Google Play** and is committed, images included.
- Names use dashes, never underscores.

## Play's limits (the validator checks them)

| Item | Limit |
| --- | --- |
| Title | 30 characters |
| Short description | 80 characters |
| Full description | 4,000 characters |
| Icon | 512 x 512, 32-bit PNG, at most 1,024 KB |
| Feature graphic | 1024 x 500, 24-bit PNG or JPEG, no alpha |
| Screenshots | 2 to 8 per device type; sides 320 to 3,840 px; long side at most twice the short side; no alpha |
| Alt text | 140 characters per screenshot (`alt-text.txt`: `<file><TAB><text>` per line) |
| Custom listing keywords | up to 500, each up to 80 characters |
| Custom listings | up to 50 per app |

## Change a listing

- **Text:** edit `listing/<locale>/*.txt`; validate; upload.
- **Visuals:** edit `source/` and render (see `play-store-media`); it writes into `listing/`. Files rendered from `source/` can always be rebuilt from Git history. Files that came from elsewhere (device screenshots, a designer's export) cannot: copy them into `history/<yyyy-mm-dd>-<name>/` before replacing them.
- **Metadata:** write it into every new image with [`metadata`](../metadata/SKILL.md), from `source/metadata.json`.
- Run `node scripts/validate.mjs`, then upload in Play Console and paste the alt text.

## Custom store listings

A custom listing is the store page for one audience (for example developers learning Jetpack Compose). Play shows it to people who arrive by its link (`&listing=<id>`), from chosen countries (a country belongs to one custom listing at most), from chosen search keywords, or from Google Ads. Translations are not copied from the default listing.

`listing.json`:

```json
{
  "id": "<id>",
  "audience": "<who it is for, in one sentence>",
  "targeting": { "url": true, "countries": [], "keywords": "keywords.txt", "googleAds": false },
  "status": "planned | live | paused",
  "log": [{ "date": "YYYY-MM-DD", "status": "live", "note": "first published" }]
}
```

- `keywords.txt`: the searches that audience makes, one per line (Play Console's bulk add), specific first, no competitors' names. Build them with [`social-captions/references/keywords.md`](../social-captions/references/keywords.md).
- Its text is written for its audience; its visuals come from its own `source/`, or its `source/README.md` names the listing whose source it reuses with its own strings.
- **Status, never deletion.** Pausing or resuming is a change of `status` and a new log line. A listing deleted from Play Console moves to `custom/removed/<id>/` with a last log line and is never edited there; bringing it back means moving it out and continuing the log.

## Moving an existing listing in

Most apps keep the listing in `app/src/main/play/` or nowhere.

1. `git mv` the text files to `listings/default/listing/<locale>/` and the graphics to `listing/<locale>/graphics/<kind>/`, renamed with dashes (`play_store_feature_graphic.png` becomes `feature-graphic/feature-graphic.png`, `ic_launcher-playstore.png` becomes `icon/icon.png`, Chromebook screenshots go to `desktop-screenshots/`).
2. The contact files and `default-language.txt` go to `google-play/`.
3. Those images have no source: when new visuals replace them, copy them into `listings/default/history/` first. Extras that were never uploaded (framed screenshots) go there too.
4. Fix every link to the old paths (the app's README often shows the feature graphic and screenshots) and validate.

## Copy for the listing

- Title: the app name and, if room, one plain word of what it is.
- Short description: what the app does for the user, in one sentence, with the main search term.
- Full description: a first paragraph that works alone (it shows before "More"), then features as short paragraphs. Keywords written for people; Play penalises keyword stuffing.
- No claims the app can't back, no rankings, prices or promotions, no other apps' names.
