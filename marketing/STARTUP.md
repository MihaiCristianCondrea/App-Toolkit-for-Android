# Marketing startup

Read this first for any marketing work. It says which skills to load for each task, and in which order. The skills are in `marketing/skills/`; `skills/llms.txt` has one line on each.

Before any task, read `marketing/README.md` (with its "Project metadata" block), `marketing/briefs/`, and the app's own `README.md`.

## Always

- [`writing-style`](skills/built-in/writing-style/SKILL.md) applies to every word written: copy, captions, metadata, docs, comments, commits.
- [`metadata`](skills/built-in/metadata/SKILL.md) runs on every finished file, at the end of every task below. On its first run in a project it adds the "Project metadata" block to `marketing/README.md`.
- Skills in `skills/design-taste/`, `skills/google/` and `skills/social-media/` are used as their authors published them. Never edit them; adapt how they are used here or in a built-in skill.

## Tasks

| Task | Skills, in order |
| --- | --- |
| Set up marketing in a new repository | [`marketing-startup`](skills/built-in/marketing-startup/SKILL.md), [`marketing-ui`](skills/built-in/marketing-ui/SKILL.md) (bootstrap the hub), [`metadata`](skills/built-in/metadata/SKILL.md) (project block) |
| Promo video, trailer or short | `marketing-startup` (campaign), `marketing-ui` (screens, parity), [`motion-video`](skills/built-in/motion-video/SKILL.md), `metadata`, [`social-captions`](skills/built-in/social-captions/SKILL.md) |
| Instagram carousel or single post | `marketing-startup`, `marketing-ui` (screens, captures), a look from `design-taste/` if the brief asks for one, `metadata`, `social-captions` |
| Play Store visuals: phone, tablet and desktop screenshots, feature graphic, icon | [`play-store-media`](skills/built-in/play-store-media/SKILL.md), `marketing-ui` (screens), a `design-taste/` style, [`store-listing`](skills/built-in/store-listing/SKILL.md) (publish, replaced uploads), `metadata` |
| Play Store listing text, custom listings, keywords | [`store-listing`](skills/built-in/store-listing/SKILL.md), `writing-style` |
| Add or fix a screen in the UI kit | `marketing-ui` |
| Brand kit or visual identity concepts | `marketing-startup` (briefs), [`brandkit`](skills/design-taste/brandkit/SKILL.md), `metadata` |
| Landing page or press page | `marketing-startup`, [`taste-skill`](skills/design-taste/taste-skill/SKILL.md) (or another `design-taste/` style the brief names), [`output-skill`](skills/design-taste/output-skill/SKILL.md), `metadata` (HTML tags and JSON-LD) |
| Redesign an existing page | [`redesign-skill`](skills/design-taste/redesign-skill/SKILL.md), [`output-skill`](skills/design-taste/output-skill/SKILL.md), `metadata` |
| Design rules for AI UI tools (DESIGN.md) | [`stitch-skill`](skills/google/stitch-skill/SKILL.md) |
| Concept images of app screens or sites | [`imagegen-frontend-mobile`](skills/design-taste/imagegen-frontend-mobile/SKILL.md) or [`imagegen-frontend-web`](skills/design-taste/imagegen-frontend-web/SKILL.md), then [`image-to-code-skill`](skills/design-taste/image-to-code-skill/SKILL.md) to build one |
| TikTok video or account (hooks, captions, trends, plan) | `social-captions`, then the matching skill in [`skills/social-media/tiktok/`](skills/social-media/tiktok) |
| Material 3 component, token, layout or motion question | [`material-ui/m3`](skills/built-in/material-ui/m3/SKILL.md), with the documentation in `material-ui/m3-documentation/` |
| Captions only, for media that already exists | `metadata` (manifest, if missing), `social-captions` |

## Video projects

Install Remotion's official agent skills once per machine: `npx skills add remotion-dev/skills`.

## Campaign layout

Every campaign lives in `marketing/creative/social-campaigns/<campaign-slug>/`, whatever its media, and moves to `creative/social-campaigns/completed/` once published: `README.md`, `<campaign-slug>-metadata.json`, `captions/`, `source/`, `output/` (never committed). Start a video from [`creative/templates/ui-motion-starter`](creative/templates/ui-motion-starter/README.md). Details in [`marketing-startup`](skills/built-in/marketing-startup/SKILL.md).
