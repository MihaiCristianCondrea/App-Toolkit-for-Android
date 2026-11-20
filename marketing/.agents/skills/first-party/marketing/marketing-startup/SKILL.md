---
name: marketing-startup
description: >
  Start marketing work in an app repository: read the product, set up the
  marketing/ folder (creative/, research/, briefs/) and a campaign under
  marketing/creative/social-campaigns/<campaign-slug>/, and write the campaign
  README. Task routing lives in marketing/AGENTS.md. Load when initializing
  marketing resources, creating a campaign, or establishing a product's
  marketing context.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-20'
  keywords:
  - marketing
  - startup
  - campaign
  - workflow
  - task-routing
  - social-media
  - product-launch
  - app-marketing
---

# Marketing startup

The initialization entry point for marketing work in an app repository. It inspects the product and sets up the marketing folder and campaigns. Task routing is owned by [`marketing/AGENTS.md`](../../../../../AGENTS.md); prose rules by [`writing-style`](../../../../../../.agents/skills/first-party/writing-style/SKILL.md).

## Always

- Read `../AGENTS.md` (if not already loaded) and [`marketing/AGENTS.md`](../../../../../AGENTS.md) first, loading each once.
- Check the [shared marketing assets](../../../../../assets/README.md) for reusable fonts, shapes, device frames and badges before sourcing replacements.
- For existing marketing resources that do not require campaign initialization, go straight to the task-specific skills in the routing table of `marketing/AGENTS.md` instead of recreating setup artifacts.

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
├── AGENTS.md                 # marketing, design exploration, and creative workflow rules
├── README.md                 # layout, rules, and the "Project metadata" block (metadata skill)
├── briefs/                   # brand.md, product-positioning.md
├── ui/                       # the UI kit: app screens as web components (marketing-ui skill)
├── assets/                   # logos, device frames, shapes, store badges, fonts
├── research/                 # analytics exports and other input, each with a README (source, dates); never quoted
├── creative/                 # everything that is made, each with its source
│   ├── store/
│   │   └── google-play/      # the store listing (play-store-listing skill)
│   ├── social-campaigns/
│   │   ├── <campaign-slug>/  # one folder per campaign in progress, every media type
│   │   └── completed/        # published campaigns
│   ├── templates/            # starting points for new work, such as ui-motion-starter (Remotion over the UI kit)
│   └── <content>/            # optional: a content series the app publishes, such as lessons/ or news/
├── scripts/                  # launchers that list and render campaigns, once there is one
└── .agents/
    ├── skills.json           # Antigravity manifest for nested marketing skills
    └── skills/
        ├── first-party/      # repository-maintained marketing skills: edit freely
        ├── official/         # vendor-published design and motion skills: never edit (except material-ui)
        └── third-party/      # community skills by author: never edit
```

Make a folder only when it gets its first file; an empty `creative/templates/` or `research/` is not added.
`research/` is input, not output: analytics and concept exports inform the briefs and the screens, but their numbers
never appear in copy, captions or store text.

Skills under `.agents/skills/official/` and `.agents/skills/third-party/` stay exactly as their creators published them; `marketing/AGENTS.md` names the one maintained exception (`official/google/material-ui`). To change how the others are used, update a first-party skill, not them.

## 3. A campaign

A campaign is one release or idea, published as any mix of video, images, carousels and text. Its slug is short, lowercase and hyphenated (`ask-studio-bot`, `stay-curious-trailer`).

```text
marketing/creative/social-campaigns/
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

When a campaign is published, move its folder to `creative/social-campaigns/completed/<campaign-slug>/` (fix any relative paths in `source/` for the extra folder level) and note the publish date in its README. Completed campaigns are a record: never re-render or edit them unless the owner asks. A new idea, even a variation of a completed one, is a new campaign.

`creative/social-campaigns/completed/` holds one `README.md` that says so, created with the folder:

```markdown
# Completed campaigns

Every campaign in this folder is published and finished. It is kept as a record of what was made and how.

- Do not modify, re-render or update these campaigns, unless the owner asks for one specifically.
- They may not follow the current marketing skills, folder layout or tooling. That is expected: they are legacy
  code that served its campaign, not a template to keep up to date.
- To reuse one, copy what you need into a new campaign under `creative/social-campaigns/` and bring it up to date there.
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

## 5. Next steps

After setup, follow the phase order in [the marketing routing table](../../../../../AGENTS.md). Bootstrap the UI hub when the deliverable needs reusable product screens. Apply publishing metadata after the final export, then load Social Captions for social publication.
