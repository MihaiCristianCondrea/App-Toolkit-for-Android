---
name: metadata
description: >
  Write search, rights and accessibility metadata into every finished marketing
  file (images, audio, video, SVG, HTML, PDF, captions) and verify that nothing
  else changed. Keeps the project's metadata in marketing/README.md and each
  campaign's in <campaign-slug>-metadata.json. Run it whenever final outputs are
  rendered or exported, in every marketing workflow.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-16'
  keywords:
  - metadata
  - seo
  - xmp
  - exif
  - id3
  - schema-org
  - json-ld
  - open-graph
---

# Metadata

Every file a campaign publishes carries its title, description, keywords, creator, copyright and links inside the file, so search engines, stores, players and screen readers can read them. This skill runs at the end of every workflow, after the final render or export and before anything is posted. Text in the metadata follows [`writing-style`](../writing-style/SKILL.md).

## Where the values live

Three layers, later ones winning:

| Layer | File | Holds |
| --- | --- | --- |
| Project | `marketing/README.md`, section "Project metadata" | App name, package, store and site URLs, repository, category, language, project keywords, credits |
| Campaign | `marketing/social-campaigns/<campaign-slug>/<campaign-slug>-metadata.json` | Campaign title and date, the copy (title, description, alt text, keywords, hashtags) |
| File | the manifest's `files` map | Overrides for one file, keyed by its path relative to the manifest |

### First run in a project: write the project block

If `marketing/README.md` has no "Project metadata" section, add it before tagging anything. Take each value from the project first (the app's `README.md`, `build.gradle.kts` for `applicationId`, `strings.xml` for the app name, the Play Store listing, `marketing/briefs/`). Ask the developer only for what the project doesn't say. Credits default to Mihai-Cristian Condrea unless the developer says otherwise.

````markdown
## Project metadata

Read by the `metadata` skill. Change it here, not in individual campaigns.

<!-- project-metadata -->
```json
{
  "project": {
    "name": "<App name>",
    "packageId": "<com.example.app>",
    "storeUrl": "https://play.google.com/store/apps/details?id=<com.example.app>",
    "url": "<product or landing page URL>",
    "repository": "<repository URL>",
    "category": "<Play Store category>",
    "language": "en",
    "keywords": ["<App name>", "Android", "<5 to 10 terms people search for>"]
  },
  "credits": {
    "creator": "Mihai-Cristian Condrea",
    "copyrightHolder": "Mihai-Cristian Condrea",
    "country": "Romania",
    "license": "All rights reserved",
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
````

### Each campaign: write the manifest

```json
{
  "schemaVersion": "3.0",
  "campaign": { "slug": "<campaign-slug>", "title": "<Campaign title>", "releaseDate": "YYYY-MM-DD", "seriesCode": "<optional>" },
  "software": "Android Studio",
  "copy": {
    "title": "<App name>: <what this is>",
    "headline": "<the line on screen>",
    "description": "<150 to 160 characters, what the viewer sees and why it matters>",
    "longDescription": "<2 to 4 sentences for video synopsis and store pages>",
    "altText": "<what the image shows, under 125 characters>",
    "url": "<where this is published, once known>",
    "image": "<absolute URL of the cover or thumbnail, once known>",
    "keywords": ["<10 to 20 specific terms, most important first>"],
    "hashtags": ["#<AppName>", "#Android"]
  },
  "files": {
    "output/images/slide-01.png": { "title": "<App name>: <slide title>", "altText": "<what slide 1 shows>" }
  }
}
```

Give every image its own `altText` in `files`: alt text describes one picture, so a shared one is wrong for all but one.

## SEO rules for the values

- **Title:** under 60 characters, the app name first, then what the file is. No clickbait.
- **Description:** 150 to 160 characters. Say what the viewer sees and what the app does, with the main search term in the first sentence.
- **Alt text:** under 125 characters, describes the picture itself, not the campaign. Read it aloud; it should make sense without the image.
- **Keywords:** 10 to 20 terms people actually search for, specific before general. Build them for the product's audience ([`social-captions/references/keywords.md`](../social-captions/references/keywords.md)), not from the tech stack. The app name and "Android" always; no keyword stuffing, no competitors' names.
- **Hashtags:** 3 to 8, the app's own tag first.
- **No tool or AI names.** Leave out how the media was generated: no "Claude", "AI", "Remotion", "Playwright" or similar in any field, keyword or description. The `software` field (written as CreatorTool) names the main authoring app the developer uses, by default Android Studio, or stays empty. Editing apps used afterwards (After Effects, Premiere) write their own.
- **Never fabricate.** No camera EXIF, GPS, ISRC, UPC or ratings. Leave a field out when there is no true value.

## Run it

Needs Python 3.10+ with `pillow` and `mutagen`, plus `exiftool` and `ffmpeg` for the formats that use them.

```bash
pip install pillow mutagen      # exiftool: https://exiftool.org ; ffmpeg: https://ffmpeg.org
S=marketing/skills/built-in/metadata/scripts/inject_metadata.py
M=marketing/social-campaigns/<campaign-slug>/<campaign-slug>-metadata.json

python3 $S --manifest $M --dry-run marketing/social-campaigns/<campaign-slug>/output/**/*   # what would be written
python3 $S --manifest $M --jsonld marketing/social-campaigns/<campaign-slug>/output/schema.jsonld \
  marketing/social-campaigns/<campaign-slug>/output/**/*                                    # write and verify
python3 $S --manifest $M --check marketing/social-campaigns/<campaign-slug>/output/**/*     # audit later
```

The script is idempotent: run it again after every re-render.

## What gets written

| Format | Written as | Main fields |
| --- | --- | --- |
| PNG | iTXt chunks and an XMP packet (pure Python) | Title, Author, Description, Copyright, Keywords, Comment, Source, URL; XMP dc, xmpRights, photoshop, IPTC alt text |
| JPEG, TIFF | EXIF, IPTC and XMP (exiftool) | Artist, Copyright, ImageDescription, and the Windows XP fields (Title, Subject, Tags, Comments, Authors); IPTC ObjectName, By-line, Caption, Keywords; XMP as above |
| WebP, AVIF, HEIC | EXIF and XMP (exiftool) | as above, without IPTC |
| GIF | XMP (exiftool) | XMP as above |
| PDF | Info dictionary and XMP (exiftool, incremental update) | Title, Author, Subject, Keywords, Creator |
| SVG | `<title>`, `<desc>`, RDF `<metadata>`, `role="img"` | title, alt text, Dublin Core and rights |
| HTML | `<title>`, meta description and keywords, Open Graph, Twitter card, canonical link, JSON-LD | everything a search engine and a link preview read |
| WAV | RIFF LIST INFO and an ID3 chunk | INAM, IART, ICMT, ICOP, ICRD, IGNR, IKEY; ID3 as below |
| MP3, AIFF | ID3v2.4 | TIT2, TPE1, TALB, TCOM, TCOP, TDRC, TCON, TPUB, COMM, keywords, WOAF, WOAR |
| FLAC, OGG, Opus | Vorbis comments | TITLE, ARTIST, ALBUM, COPYRIGHT, DATE, GENRE, DESCRIPTION, KEYWORDS, LICENSE |
| M4A, AAC | MP4 atoms | title, artist, album, composer, copyright, date, genre, comment, description, keywords |
| MP4, MOV, M4V | container tags with `ffmpeg -c copy`, then exiftool for the iTunes item list, Microsoft's Xtra atom and XMP | everything Windows Explorer shows under Details: Title, Subtitle, Tags, Comments, Contributing artists, Year, Genre, Directors, Producers, Writers, Publisher, Content provider, Encoded by, Author URL, Promotion URL, Copyright; plus album, description, keywords and XMP rights |
| WebM, MKV | Matroska tags, `ffmpeg -c copy` | as above |
| SRT, VTT, ZIP, anything else | an XMP sidecar `<file>.xmp` | the full XMP packet |
| all files | `--jsonld` writes one schema.org graph | ImageObject, VideoObject, AudioObject or WebPage, with the creator, copyright and the app as `about` |

Paste the JSON-LD entries into the page where a file is published (the site's video or press page), so search engines index it.

## Verification

Every write is checked before the script moves on:

- **Content unchanged:** decoded pixels for images (every frame of a GIF), stream packet hashes for audio and video, the visible markup for SVG and HTML, and the original document for PDF must match before and after.
- **Read back:** the title is read again with an independent reader (ffprobe, mutagen, exiftool, the markup).

A file that fails either check is restored from its backup and reported, and the script exits non-zero. Report a failure to the developer; never post a file the script could not verify.

Platforms strip some fields on upload (Instagram and X remove most image metadata). Tag the files anyway: the masters are the archive, and the website, YouTube and the Play Store keep more of it. On YouTube the description comes from [`social-captions`](../social-captions/SKILL.md), which reads the same manifest.
