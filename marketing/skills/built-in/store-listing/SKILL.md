---
name: store-listing
description: >
  Keep an app's Google Play store listings in the repository under
  marketing/creative/store/google-play/listings/: the default listing and custom store
  listings, each with source/ (what renders it) and listing/<locale>/ (what is
  uploaded), plus status, keywords, retired uploads and a validator for Play's rules.
  Use when changing listing text, publishing new visuals, adding a language or
  a custom listing, or moving an existing listing (app/src/main/play) in.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-28'
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
marketing/creative/store/google-play/
├── README.md                       a table of the listings and their status
├── contact-email.txt, contact-website.txt, default-language.txt
├── scripts/validate.mjs            Play's rules for every listing and language (copy from this skill)
└── listings/
    ├── <name>-legacy/              a retired listing (an earlier default listing): its text and graphics as uploaded, a full README
    ├── default/                    the main store listing
    │   ├── source/                 code, strings/<locale>.json, metadata.json, README
    │   └── listing/
    │       └── <locale>/           title.txt, short-description.txt, full-description.txt, alt-text.txt
    │           └── graphics/       icon/, feature-graphic/, phone-screenshots/, tablet-screenshots/, desktop-screenshots/
    └── custom/
        ├── README.md
        └── <id>/                   a custom store listing: the same source/ and listing/, plus listing.json and keywords.txt
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
| Alt text | 140 characters per screenshot (`alt-text.txt`: `<kind>-screenshots/<file><TAB><text>` per line) |
| Custom listing keywords | up to 500, each up to 80 characters |
| Custom listings | up to 50 per app |

## Change a listing

- **Text:** edit `listing/<locale>/*.txt`; validate; upload.
- **Visuals:** edit `source/` and render (see `play-store-media`); it writes into `listing/`. Files rendered from `source/` can always be rebuilt from Git history. Files that came from elsewhere (device screenshots, a designer's export) cannot: before replacing them, keep the listing they belong to as a retired listing of its own, `listings/<name>-legacy/` beside `default/` (the same `listing/<locale>/` layout, a `listing.json` with status `retired`, and a full README), unless a custom listing still uses them (below). There is no history or bin folder.
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
  "status": "planned | live | paused | retired",
  "log": [{ "date": "YYYY-MM-DD", "status": "live", "note": "first published" }]
}
```

- `keywords.txt`: the searches that audience makes, one per line (Play Console's bulk add), specific first, no competitors' names. Build them with [`social-captions/references/keywords.md`](../social-captions/references/keywords.md). A handful is not enough: Play allows 500, so write a full set (a couple of hundred is typical), grouped by theme: the listing's own name and its variants, the field, each language and framework, each topic the app actually teaches or does, the learning or usage intent, and the app's distinctive features. Leave out topics the app doesn't cover, other apps and courses, and anything the app can't deliver (certifications, interview prep).
- Its text is written for its audience; its visuals come from its own `source/`, or its `source/README.md` names the listing whose source it reuses with its own strings. A listing that shows the default listing's graphics as they are is text only: no `graphics/` and no `alt-text.txt` of its own, nothing copied after a render; its `source/README.md` says to upload the default's graphics and `alt-text.txt` with it.
- **A compatibility listing.** When an app is renamed or redesigned, people still search for and follow links to the old one. A custom listing can keep what they recognise: the old title, icon, feature graphic and description, targeted by the old name's keywords. Check the old text against today's rules and today's app before reusing it: drop claims that are no longer true (open source, offline access), calls to action ("Download ... today") and anything else Play now rejects. Its screenshots show the app as it is: the default listing's, used as they are (text only, above); the old screenshots stay in the retired listing or in Git history.
- **Status, never deletion.** Pausing, resuming or retiring is a change of `status` and a new log line; the folder stays where it is. A retired listing is never edited; bringing it back means a new log line.

## A retired listing's README

Every retired listing has a README that stands alone: a table of status and date, locales, whether it has a source (device screenshots don't), what replaced it and which of its files are still used elsewhere; then each text file with its content or a summary and its length; each graphics folder with its files, sizes and what they show; any extras never uploaded (framed screenshots); and why it was retired (rules it no longer meets).

## Moving an existing listing in

Most apps keep the listing in `app/src/main/play/` or nowhere.

1. `git mv` the text files to `listings/default/listing/<locale>/` and the graphics to `listing/<locale>/graphics/<kind>/`, renamed with dashes (`play_store_feature_graphic.png` becomes `feature-graphic/feature-graphic.png`, `ic_launcher-playstore.png` becomes `icon/icon.png`, Chromebook screenshots go to `desktop-screenshots/`).
2. The contact files and `default-language.txt` go to `google-play/`.
3. Those images have no source: when new visuals replace them, the listing becomes a retired listing, `listings/<name>-legacy/`, with its README (or its files move into a custom listing that keeps using them, such as a compatibility listing). Extras that were never uploaded (framed screenshots) go with them.
4. Fix every link to the old paths (the app's README often shows the feature graphic and screenshots) and validate.
5. Fix what the validator finds without guessing. A PNG with an alpha channel whose pixels are all opaque can be saved
   without the channel: it looks the same and Play accepts it. Two feature graphics, or one at a size Play rejects
   (1025 x 500), mean only one was uploaded: keep the one Play accepts in `feature-graphic/` and put the other in
   `source/unused/`, named with its size, until the retired listing takes it.

## Copy for the listing

- Title: the app name and, if room, one plain word of what it is.
- Short description: what the app does for the user, in one sentence, with the main search term.
- Full description: a first paragraph that works alone (it shows before "More"), then features as short paragraphs. Keywords written for people; Play penalises keyword stuffing.
- No claims the app can't back, no rankings, prices or promotions, no other apps' names.
