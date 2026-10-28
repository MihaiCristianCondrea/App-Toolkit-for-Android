# Google Play listing

App Toolkit's store listings as files: the default listing, and later custom store listings, each with the source
that renders its visuals and the files that are uploaded. Only the Play app is listed here; the library is not marketed.

```text
google-play/
├── contact-email.txt, contact-website.txt, default-language.txt
├── scripts/validate.mjs            Play's rules, for every listing and language
└── listings/
    ├── default/                    the main store listing
    │   ├── source/                 what makes it: code, strings/<locale>.json, metadata.json, README
    │   └── listing/<locale>/       what is uploaded: title, short and full description, graphics/
    │       └── graphics/           icon/, feature-graphic/, phone-, tablet-, desktop-screenshots/
    └── custom/                     one custom store listing per audience (none yet)
```

One source per listing renders every language: `strings/<locale>.json` becomes `listing/<locale>/`. The listing ships
in English (en-US) only for now.

| Listing | Status | Visuals |
| --- | --- | --- |
| [default](listings/default/source/README.md) | live | 4 phone and 4 tablet screenshots, feature graphic and icon, made outside this repository (no source) |
| [custom](listings/custom/README.md) | none yet | One listing per tool is planned, after the redesign |

## Change a listing

1. Text: edit `listing/<locale>/*.txt`.
2. Visuals: edit `source/` (code or strings), then render it; it writes straight into `listing/`. The default
   listing's current visuals have no source: before new ones replace them, they move to a retired listing of their own
   (`listings/<name>-legacy/` with a `listing.json` and a full README), as the `store-listing` skill describes.
   Anything rendered from `source/` can be rebuilt from Git history.
3. `node scripts/validate.mjs` here, then upload.
