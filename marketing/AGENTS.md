# Marketing AGENTS.md

This file defines the marketing, design exploration, and creative production workflow for Android
applications, with or without App Toolkit.

It works alongside the repository's root `AGENTS.md`.

## Relationship with the root AGENTS.md

Before starting a task, read `../AGENTS.md` if it has not already been loaded.

The root file owns Android architecture, Compose implementation, code quality, build validation, and
when Android tasks cross into design exploration. This file extends those instructions for design
prototyping, motion and video production, Play Store assets, metadata, and campaigns.

Read each `AGENTS.md` file at most once per session. Never reload either file recursively.

When a marketing or design-first task affects Android code (reading Compose screens for parity, or
implementing an approved prototype), follow the root `AGENTS.md` rules and Android development
skills for the Android side.

## Design workflow

Root `AGENTS.md` defines the sequence for new Android screens and substantial redesigns:
inspect, plan, Material design, interactive prototype, approval, Compose implementation,
validation, parity. On the marketing side:

- `material-ui` supplies the Material specification (components, tokens, typography, states,
  Expressive choices). `marketing-ui` builds the interactive prototype in `marketing/ui` from it.
- Inspect the existing application theme, components, and Compose screens before creating or
  changing a prototype.
- Present proposed designs for approval and record the approved specification using
  `marketing/.agents/skills/first-party/marketing/marketing-ui/references/design-handoff.md`.
- Do not modify Compose/Kotlin UI files before design approval unless explicitly authorized.
- After Android implementation, verify parity (`npm run qa`, `npm run parity`, or side-by-side
  renders) across supported states and form factors, keeping the Android app authoritative for
  runtime behavior and technical contracts.
- For existing screens, `marketing-ui` also works in reverse: it translates the Compose source into
  the kit for synchronization and marketing assets.
- Do not treat conceptual marketing previews as evidence that an application feature already exists.

Load a skill when its phase starts. Naming a skill in this file does not require reading it for every
task.

## Skill selection and routing

Select only the skills required for the requested task. Do not load entire third-party collections
(`remotion/`, `tiktok-skills/`, `imagegen-skills/`) unless the task requires them. Paths are relative
to `marketing/.agents/skills/` unless they start with `.agents/`. Skills are listed in working order.

| Task | Skills | Notes and exclusions |
| --- | --- | --- |
| Set up `marketing/` or start a campaign | `first-party/marketing/marketing-startup` | Bootstrap with `marketing-ui` when reusing app screens. Set up the project metadata block with `metadata` when preparing publishable assets. |
| Design a new screen or redesign one | `official/google/material-ui`, `first-party/marketing/marketing-ui` (+ `references/design-handoff.md`), optional `official/lottiefiles/motion-design`, optional `third-party/microck/ui-unslop` (web kit review) | Add `official/google/stitch-skill` only when updating `DESIGN.md`; exclude `remotion/*`, `social-captions`, `play-store-*` |
| Add, fix, or sync a screen in `marketing/ui` | `marketing-ui`, `material-ui` (tokens, components, Material Web limits), the authoritative Compose source | Run `npm run qa` and `npm run parity`; no campaign files |
| Material 3 component, token, typography, layout, or motion question | `material-ui` | Uses its offline `documentation/`; no prototype needed |
| Instagram Reel or promotional short | [Marketing Startup](.agents/skills/first-party/marketing/marketing-startup/SKILL.md) for campaign setup, [Material UI](.agents/skills/official/google/material-ui/SKILL.md) for Material-based products and [Taste](.agents/skills/third-party/leonxlnx/taste-skill/SKILL.md) for creative direction, [Marketing UI](.agents/skills/first-party/marketing/marketing-ui/SKILL.md) for product screens, [Motion Design](.agents/skills/official/lottiefiles/motion-design/SKILL.md) for choreography, [Motion Video](.agents/skills/first-party/motion-video/SKILL.md) and [Remotion Best Practices](.agents/skills/official/remotion/remotion-best-practices/SKILL.md) for production, then [Metadata](.agents/skills/first-party/metadata/files-metadata/SKILL.md) and [Social Captions](.agents/skills/first-party/metadata/social-captions/SKILL.md) | Check the channel's delivery specs before storyboarding. Validate the encoded render before writing publishing assets. Load only Remotion skills needed for the cut, such as [Remotion Render](.agents/skills/official/remotion/remotion-render/SKILL.md) and [Remotion Captions](.agents/skills/official/remotion/remotion-captions/SKILL.md) when adding subtitles. |
| Application trailer | App brand and [Material UI](.agents/skills/official/google/material-ui/SKILL.md) where applicable, [Marketing UI](.agents/skills/first-party/marketing/marketing-ui/SKILL.md) for product screens, [Taste](.agents/skills/third-party/leonxlnx/taste-skill/SKILL.md) when exploring concepts, then [Motion Design](.agents/skills/official/lottiefiles/motion-design/SKILL.md), [Motion Video](.agents/skills/first-party/motion-video/SKILL.md) and [Remotion Best Practices](.agents/skills/official/remotion/remotion-best-practices/SKILL.md) | Review the animatic and the final render. Apply [Metadata](.agents/skills/first-party/metadata/files-metadata/SKILL.md) and prepare channel-specific assets, including [Social Captions](.agents/skills/first-party/metadata/social-captions/SKILL.md) for social posts. Use [Marketing Startup](.agents/skills/first-party/marketing/marketing-startup/SKILL.md) for a new campaign. |
| Instagram carousel or single post | [Marketing Startup](.agents/skills/first-party/marketing/marketing-startup/SKILL.md) for a new campaign, app brand and [Material UI](.agents/skills/official/google/material-ui/SKILL.md) where applicable, [Taste](.agents/skills/third-party/leonxlnx/taste-skill/SKILL.md) for composition, [Marketing UI](.agents/skills/first-party/marketing/marketing-ui/SKILL.md) for product screens, then [Metadata](.agents/skills/first-party/metadata/files-metadata/SKILL.md) and [Social Captions](.agents/skills/first-party/metadata/social-captions/SKILL.md) | Composite real UI captures with existing assets. Use image generation only for a needed visual, such as an illustration or a brand concept, and review the finished images before applying metadata. |
| Play Store screenshots, icon, or feature graphic | [Marketing UI](.agents/skills/first-party/marketing/marketing-ui/SKILL.md) for real screens, [Material UI](.agents/skills/official/google/material-ui/SKILL.md) for Material guidance, [Play Store Media](.agents/skills/first-party/play-store/play-store-media/SKILL.md) for production and validation, then [Metadata](.agents/skills/first-party/metadata/files-metadata/SKILL.md) and [Play Store Listing](.agents/skills/first-party/play-store/play-store-listing/SKILL.md) | Use Taste when exploring composition. Review every exported image and apply the store-media rules to each device and locale. |
| Play Store listing text or custom listings | `play-store-listing` | Text follows `writing-style` |
| Play Store What's new notes | `first-party/play-store/play-store-changelog` | Distinct from `.agents/skills/first-party/app-changelog` (`CHANGELOG.md`) |
| Brand kit or visual identity concepts | `marketing-startup` (briefs), `third-party/leonxlnx/imagegen-skills/brandkit`, `metadata` | |
| Standalone landing page or press page | App brand and [Material UI](.agents/skills/official/google/material-ui/SKILL.md) when applicable, [Taste](.agents/skills/third-party/leonxlnx/taste-skill/SKILL.md) for creative direction, web implementation, [UI Unslop](.agents/skills/third-party/microck/ui-unslop/SKILL.md) for visual review, then [Metadata](.agents/skills/first-party/metadata/files-metadata/SKILL.md) | Use [Marketing Startup](.agents/skills/first-party/marketing/marketing-startup/SKILL.md) when setting up marketing. Load Marketing UI only when the page reuses product screens. |
| Redesign or clean up an existing web page | `ui-unslop`, `taste-skill`, `metadata` | Web pages only |
| Concept images of app screens or sites | `third-party/leonxlnx/imagegen-skills/imagegen-frontend-mobile` or `imagegen-frontend-web`, then `image-to-code` | Concepts are not app features |
| Social post copy, or captions for existing media | `first-party/metadata/files-metadata` (manifest, if missing), `first-party/metadata/social-captions`, plus `third-party/sergebulaev/tiktok-skills` (if TikTok) | Never for ordinary Android feature work |
| Marketing research (GA4, Search Console, Trends) | `.agents/skills/third-party/zenith779/ga-gsc-skills/<skill>` | Reporting only; never changes Android instrumentation |

The [shared asset library](assets/README.md) documents reusable fonts, shapes, frames and the Google Play badge. Load an image-generation skill only when a brief calls for new artwork. Use [Brand Kit](.agents/skills/third-party/leonxlnx/imagegen-skills/brandkit/SKILL.md) for identity concepts. Keep generated artwork outside the app-screen representation.

Campaign deliverables always finish with `metadata` on the final files, then `social-captions` when
the work is published socially.

## Ownership and precedence

Each concern has one owner. Other skills link to it instead of repeating it.

- **Material rules: `material-ui` and the app's theme.** Material 3 tokens, typography, and component
  behavior govern Android targets and M3 prototypes in `marketing/ui`.
- **Prototype, handoff, and parity: `marketing-ui`.**
- **Motion principles: `motion-design`.** Choreography, timing, and easing for UI and video.
  Material's own motion values (springs, schemes, transitions) come from `material-ui`. Android UI
  animations use native Compose and Material 3 APIs.
- **Rendered video: `motion-video`**, which drives Remotion through the official `remotion/*`
  skills. Remotion is never used for app UI.
- **Store visuals: `play-store-media`; store files and listings: `play-store-listing`.** Both mount
  screens from `marketing-ui` rather than rebuilding them.
- **`taste-skill` is directional only.** It may inform composition for web pages or store visuals,
  but must never override Material 3 component contracts or impose web-specific rules (such as
  banning Roboto or forcing web hero layouts) onto Android screens.
- **`ui-unslop` targets web polish.** Use it to strip generic AI styling from web prototypes and
  landing pages. Do not copy web CSS patterns literally into Jetpack Compose.
- **Prose: `.agents/skills/first-party/writing-style/SKILL.md`.** All copy in prototypes, store
  text, metadata, and captions follows it. Skills add only their domain rules (character limits,
  platform formats).

`official/` and `third-party/` skills stay as their authors published them, with one exception:
`official/google/material-ui` is maintained in this repository. Its `documentation/` and `visuals/`
mirror Google's material and are never edited.

## Research and measurement

For marketing research, choose the matching skill from
`.agents/skills/third-party/zenith779/ga-gsc-skills/`:

- `google-analytics/SKILL.md` for existing GA4 traffic, engagement, and audience or channel
  performance data.
- `google-search-console/SKILL.md` for search queries, page performance, CTR, and indexability.
- `google-trends/SKILL.md` for topic and keyword interest.

Use findings as inputs to campaign and design proposals. Explain which source and date range support
each conclusion. Do not present missing or unavailable data as measured results.

For Android analytics event design or implementation, defer to root `AGENTS.md` and
`.agents/skills/first-party/ga4-app-analytics/SKILL.md` rather than treating reporting skills as
instrumentation guidance.

## Media metadata and captions

Use `marketing/.agents/skills/first-party/metadata/files-metadata/SKILL.md` (`name: metadata`) for
publishable marketing deliverables, including embedded file metadata, copyright and creator
attribution, descriptions, alt text, and project/campaign manifests.

Maintain one source of truth for shared values:

- `marketing/README.md` holds project-level metadata when the project uses the metadata skill's
  schema.
- Each campaign's `<campaign-slug>-metadata.json` holds campaign values and per-file overrides.
- Each finished file receives only metadata relevant to its format and intended publication.

Do not invent copyright ownership, credits, camera information, location, dates, or publication
URLs. Follow `files-metadata` rules rather than copying generic fields into every file.

Use `marketing/.agents/skills/first-party/metadata/social-captions/SKILL.md`
(`name: social-captions`) only when deliverables are intended for social publication. Generate only
the requested platform files. Keep social post captions distinct from alt text, subtitles, and
embedded media metadata. Do not produce social captions or campaign manifests for ordinary Android
feature implementations.

Finalize media first, update file-specific details, run `files-metadata` after the final render or
export, and verify the outputs. Reapply and verify metadata after any re-render that replaces a
file.

For non-campaign outputs such as store graphics, press assets, or standalone images, follow the
file-level workflow in `files-metadata` without inventing a social campaign or unnecessary caption
files.

## Marketing production

Maintain consistent branding and truthful product representation across deliverables. Preserve
approved designs and existing campaign decisions unless changes are requested, and validate outputs
using the checks required by their corresponding skills.
