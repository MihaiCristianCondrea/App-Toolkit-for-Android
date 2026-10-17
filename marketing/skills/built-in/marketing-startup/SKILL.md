---
name: marketing-startup
description: >
  Start any marketing task in an app repository: read the product, set up the
  marketing/ folder and a campaign under marketing/social-campaigns/<campaign-slug>/, write
  the campaign README, and hand off to the skills that make the media, tag it
  and caption it. Load first for any campaign, video, carousel, store graphic or
  press asset.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-16'
  keywords:
  - marketing
  - startup
  - campaign
  - workflow
  - social-media
  - product-launch
  - app-marketing
---

# Marketing startup

The entry point for marketing work in any app repository. It sets up the folders and the campaign, then hands off. Which skills follow for which task is in [`STARTUP.md`](../../../STARTUP.md). All text follows [`writing-style`](../writing-style/SKILL.md).

## 1. Read the product

Before writing a line of copy or a frame of video, read:

- the app's `README.md`, `build.gradle.kts` (`applicationId`, version) and `strings.xml` (app name);
- the theme (colours, typography, shapes), the launcher icon and the fonts;
- the store listing and screenshots, if the app is published;
- `marketing/README.md` (including its "Project metadata" block) and `marketing/briefs/`, if they exist.

If `marketing/briefs/` is missing, write two short files from what you read, and ask the developer only about what the project doesn't say:

- `briefs/brand.md`: voice, colours, type, logo use, what the brand never does.
- `briefs/product-positioning.md`: who the app is for, the problem, the promise, the three features that prove it.

## 2. The marketing folder

```text
marketing/
├── README.md                 # layout, rules, and the "Project metadata" block (metadata skill)
├── STARTUP.md                # task router: which skills, in which order
├── briefs/                   # brand.md, product-positioning.md
├── ui/                       # the UI kit: app screens as web components (marketing-ui skill)
├── assets/                   # logos, device frames, shapes, store badges, fonts
├── store/
│   └── google-play/          the store listing (store-listing skill)
├── social-campaigns/
│   ├── <campaign-slug>/      # one folder per campaign in progress, every media type
│   └── completed/            # published campaigns
└── skills/
    ├── llms.txt              # one line per skill
    ├── built-in/             # these skills: edit freely
    ├── design-taste/         # installed from their authors: never edit
    └── google/               # installed from Google: never edit
```

Skills under `design-taste/` and `google/` stay exactly as their creators published them. To change how they are used, change `STARTUP.md` or a built-in skill, not them.

## 3. A campaign

A campaign is one release or idea, published as any mix of video, images, carousels and text. Its slug is short, lowercase and hyphenated (`ask-studio-bot`, `stay-curious-trailer`).

```text
marketing/social-campaigns/
├── <campaign-slug>/                    # in progress
│   ├── README.md                       # what it is, how to build it, QA notes (template below)
│   ├── <campaign-slug>-metadata.json   # metadata skill
│   ├── .gitignore
│   ├── captions/                       # social-captions skill: one paste-ready file per platform
│   ├── source/                         # the code that makes the media (Remotion project, render scripts)
│   └── output/                         # finished files by type (video/, images/, audio/); never committed
└── completed/
    └── <campaign-slug>/                # published campaigns, same layout, frozen
```

The campaign's `.gitignore`:

```gitignore
node_modules/
.remotion/
.vite/
.cache/
dist/
build/
output/
source/out/
source/public/audio/
*.log
.DS_Store
Thumbs.db
```

`output/` is never committed: every file in it is rebuilt from `source/`, and the finished files go to the platforms and the owner's archive, not to Git. Reviewers get a preview sent to them, not a commit.

When a campaign is published, move its folder to `social-campaigns/completed/<campaign-slug>/` (fix any relative paths in `source/` for the extra folder level) and note the publish date in its README. Completed campaigns are a record: never re-render or edit them unless the owner asks. A new idea, even a variation of a completed one, is a new campaign.

`social-campaigns/completed/` holds one `README.md` that says so, created with the folder:

```markdown
# Completed campaigns

Every campaign in this folder is published and finished. It is kept as a record of what was made and how.

- Do not modify, re-render or update these campaigns, unless the owner asks for one specifically.
- They may not follow the current marketing skills, folder layout or tooling. That is expected: they are legacy
  code that served its campaign, not a template to keep up to date.
- To reuse one, copy what you need into a new campaign under `social-campaigns/` and bring it up to date there.
```

When only a few campaigns are completed, the owner may still choose to bring them up to date; with many, the rule above is what keeps the folder maintainable.

## 4. The campaign README

```markdown
# <Campaign title>

<One or two sentences: what it shows, who it is for, where it is published.>

| | |
| --- | --- |
| Status | draft, approved or published (with the date) |
| Media | e.g. 33 s trailer at 2K 90 fps and 1080p60; 4-slide carousel at 1600x2000 |
| Captions | `captions/youtube.md`, `captions/instagram.md` |

## Build

<The exact commands, from a clean clone, to produce every file in output/.>

## Structure

<For video: a table of scenes with times and what each shows. For carousels: one row per slide.>

## QA

- [ ] The app is shown as it is (UI kit parity; see marketing-ui)
- [ ] Text never clipped or overlapped; descenders intact
- [ ] Metadata written and verified in every output file
- [ ] Captions and alt text written; store badge rules followed

<Known limits and decisions, with reasons.>
```

## 5. Hand off

Go to [`STARTUP.md`](../../../STARTUP.md), find the task, and follow its chain. Every chain ends the same way: [`metadata`](../metadata/SKILL.md) on the finished files, then [`social-captions`](../social-captions/SKILL.md).
