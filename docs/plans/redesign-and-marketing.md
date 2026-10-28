# Plan: App Toolkit redesign and marketing reorganization

Status: plan, October 28, 2026. Nothing below is implemented yet. It builds on
[`quick_tools.md`](quick_tools.md) (the tool redistribution that is already under way) and does not
replace it.

## The goal

Make App Toolkit the next app that shows what the shared stack can do, on a base that is already
proven:

1. **Stable bases first.** Material Next (0.0.7) runs the marketing UI of every app; App Toolkit's
   own library (`3.0.0-pre30`) is validated before the app is redesigned on top of it. Validation
   is local builds and the owner's manual testing on devices; the plan adds no GitHub Actions work.
2. **A redesigned app.** The sample app on Google Play (`com.d4rk.android.apps.apptoolkit`) gets a
   coherent design, screen by screen, built from features it already has: no portfolio duplicates,
   no new sensitive permissions.
3. **Marketing organized like Android Insider's, for the app only.** App Toolkit is both the
   app and the library, but the library serves only the owner's own apps and is not marketed;
   all marketing is about the Play app. One `marketing/creative/` folder for everything
   that is made (store listing, campaigns, templates), with one editable source per output, so new
   store graphics and promotions come from the redesigned screens in the Material Next hub.

Each phase below has a "done when" line. A phase starts only when the one it depends on is done.

## What exists today

| Area | State |
| --- | --- |
| App screens | Bottom bar: Quick Tools, Numbers, Apps. Numbers is complete (`quick_tools.md`); Quick Tools still hosts most tools; settings, about, FAQ, onboarding and support come from the library. |
| Library | `3.0.0-pre30` with breaking API changes (`AospPageIndicator`, `TelemetryOnboardingPage`, `ScreenHost` layout). Open crash reports in `docs/crashes/open/`. |
| Testing | Manual, by the owner, on devices. GitHub Actions minutes are too limited for these builds, so the plan adds no CI. |
| Marketing UI | `marketing/ui` on Material Next 0.0.7: one-page hub (gallery and prototype), no drawer or footer, 38 browser checks. Screens: Quick Tools, Numbers, Apps. |
| Store listing | `sample/app/src/main/play/` (en-US only): 4 phone and 4 tablet screenshots (welcome, apps, onboarding theme, privacy), two feature graphics, icon. No source; nothing in Gradle reads this folder (no Play publisher plugin); `sample/README.md` links its images. |
| Campaigns | None. No `creative/`, no templates. |
| Research | `marketing/analytics/` (6 GA CSV exports) and `marketing/assets/ideas/` (one PNG and four Stitch ZIPs, 2.2 MB) sit beside the brand assets. |
| README metadata | `marketing/README.md` keeps project metadata in an HTML comment; Insider and the `metadata` skill use a JSON block. |

Android Insider (`marketing/creative/{store,lessons,news,social-campaigns}`) is the reference layout.
Smart Cleaner already follows it (`creative/social-campaigns`, `creative/templates`). The skills in
android-skills still describe the older layout (`marketing/store/google-play`,
`marketing/social-campaigns`).

## Target marketing layout

```text
marketing/
├── README.md               layout table, rules, project metadata (JSON block, as Insider)
├── STARTUP.md              task router (unchanged rules, new paths)
├── briefs/                 brand.md, product-positioning.md (rewritten after the redesign)
├── ui/                     Material Next hub: the app's screens as web components
├── assets/                 logos, illustrations, shapes, device frames, Play badge (no research)
├── research/               analytics/ (GA exports), with a README: source, date, what it may and
│                           may not be used for
├── creative/
│   ├── store/
│   │   └── google-play/    contact files, default-language.txt, scripts/validate.mjs
│   │       └── listings/
│   │           ├── default/          source/ (renders visuals) and listing/<locale>/ (uploaded)
│   │           ├── retired-2025/     today's screenshots and graphics, kept as a record
│   │           └── custom/<id>/      custom store listings, text only (later)
│   ├── social-campaigns/   <campaign-slug>/ in progress, completed/ when published
│   ├── templates/          ui-motion-starter (from Smart Cleaner): Remotion over the UI kit
│   └── tools/              optional, later: one explainer per tool, as Insider's lessons/
├── scripts/                render launcher for campaigns (as Insider's render.cmd/.ps1)
└── skills/                 copies from android-skills
```

`creative/tools/` is App Toolkit's counterpart to Insider's `lessons/`: short explainers ("Add the
Ruler to Quick Settings") that can become posts or store custom listings. Create it only when the
first explainer is written; empty folders are not added.

## Phase 0: stable bases

| Work | Done when |
| --- | --- |
| Validate `3.0.0-pre30`: build the sample app and each consumer app against it, check the renamed and replaced APIs, and look at indicators, onboarding pages and `ScreenHost` layouts on a phone and a tablet | Consumers compile; a short note in `library/CHANGELOG.md` lists anything they had to change |
| Decide every report in `docs/crashes/open/`: fixed, won't fix (with reason) or blocking the release | `open/` is empty or each remaining file says why it stays |
| Close the cross-repo gaps already known: Cleaner's Windows screenshot paths, Profile's Jest/`deepmerge` advisory, deployed consumers checked on 0.0.7 | Each repository's full check passes |

## Phase 1: reorganize marketing (no new content)

A move, not a redesign: every file that exists today keeps working at its new path.

1. **Store listing.** Create `creative/store/google-play/` with the default listing in Insider's
   shape. Move `sample/app/src/main/play/listings/en-US/*.txt` to
   `listings/default/listing/en-US/`, the contact and language files to the folder root, and copy
   Insider's `scripts/validate.mjs`. Today's screenshots and feature graphics have no source, so
   they go to `listings/retired-2025/` (with `listing.json` and a README saying what they show and
   when they were uploaded) before anything replaces them. Update the image links in
   `sample/README.md`, then delete `sample/app/src/main/play/`. Run `validate.mjs`.
2. **Research.** Move `analytics/` to `research/analytics/` with a README giving its source and
   date. The GA exports are reported context, like Smart Cleaner's redesign plan treats them, not
   proof of what to build. `assets/ideas/` (the Stitch exports and concept PNG) is redesign input
   only: it stays where it is until Phase 2 has used it, then it is deleted, ZIPs and images
   alike.
3. **Campaign scaffolding.** Create `creative/social-campaigns/completed/README.md` (the only file
   in `completed/` until something is published) and copy Smart Cleaner's
   `creative/templates/ui-motion-starter/`, pointed at App Toolkit's UI kit.
4. **README and STARTUP.** Rewrite `marketing/README.md` as Insider's: layout table, campaign table,
   preview and render commands, product fidelity rules, and the project metadata as a JSON block
   (convert the HTML comment). Update the paths in `STARTUP.md`. Add `scripts/render.cmd` and
   `render.ps1` once the first campaign exists.

**Done when:** the tree matches the target layout, `validate.mjs` passes, `sample/README.md` shows
its images, `marketing/ui` still passes its browser checks, and no file was lost (compare
`git ls-files` before and after, allowing for moves).

## Phase 2: redesign the app, screen by screen

Principles, from `quick_tools.md` and the earlier redesign notes:

- Use only capabilities App Toolkit already has. No features owned by Shopping Cart Calculator,
  Smart Cleaner, Net Probe or the sound meter app. No new sensitive permissions (no
  `RECORD_AUDIO`, no broad storage or usage access).
- Material 3 Expressive, App Toolkit's generated color roles, Google Sans Flex, `MaterialShapes`.
  The library owns settings, about, FAQ, onboarding and support; the app does not fork them.
- Each screen exists in Compose and in the marketing UI. The marketing UI is where a concept is
  reviewed first, because it can be shown at phone, fold and desktop sizes side by side.

For each screen, in this order: **Quick Tools** (largest, still being redistributed), **Apps**,
**Numbers** (complete; polish only), then the library screens the app shows (onboarding, settings).

| Step | What | Done when |
| --- | --- | --- |
| 1. Review | Current screen in Compose and in the UI kit; what each element is for; what users reach for (from `research/analytics`, as context only); the Stitch concepts in `assets/ideas/` for that screen | A short review in `docs/plans/redesign/<screen>.md` with the problems found |
| 2. Concept | Change the screen in `marketing/ui` first. Use `redesign-skill` and `ui-unslop` on it; check light and dark, gesture and 3-button navigation, phone, fold and desktop | The prototype page shows the concept; the owner approves it |
| 3. Build | Implement in Compose against `3.0.0-pre30` or later | Builds locally; previews cover the new states; the owner has tested it on a device |
| 4. Parity | Bring the UI kit screen to match the shipped Compose screen exactly (strings, spacing, states) | Side-by-side check at the same sizes shows no drift; `npm run qa` passes |

One screen ships before the next starts, so the app is never half-redesigned on `develop`.
When the last screen's concept is approved, delete `marketing/assets/ideas/`: it is not kept.

## Phase 3: store listing and first campaign from the redesigned app

1. **Default listing source.** Start `listings/default/source/` from Insider's (the
   `play-store-media` skill's template): phone, tablet and desktop screenshots and the feature
   graphic rendered from the UI kit screens, `strings/en-US.json` for the captions. The listing
   ships in English (en-US) only for now; more languages are a later, separate decision. Render only
   after Phase 2 parity, tied to a tested app revision, showing implemented features, with no ad
   placements in campaign or store output.
2. **Listing text.** Rewrite title, short and full description with `store-listing` and
   `writing-style`, from the new `product-positioning.md`.
3. **Custom listings (optional).** Text-only custom listings for search themes, as Insider has
   (`custom/<id>/`), for example Quick Settings tiles or everyday calculators.
4. **First campaign.** One launch short from `templates/ui-motion-starter` under
   `creative/social-campaigns/<slug>/`, following `marketing-startup`, `motion-video`, `metadata`
   and `social-captions`.

**Done when:** `validate.mjs` passes for every listing, the default listing renders from source in
every listed language, and the campaign README records its QA (contact sheet checked for clipped
text, missing fonts and UI drift).

## Phase 4: update the shared skills

In android-skills first, then copied to every repository that carries them:

- `marketing-startup`: the `creative/` layout (`creative/store/google-play`,
  `creative/social-campaigns`, `creative/templates`, optional content folders such as `lessons/` or
  `tools/`), `research/` for analytics and concept material.
- `store-listing` and `play-store-media`: the `creative/store/google-play` path, the
  `retired-<year>` rule for visuals without source, and moving a listing out of
  `app/src/main/play`.
- `marketing-ui`: bootstrap a hub with Material Next (`mountMarketingHub`,
  `navigationMode: 'none'`, `screen.html` for captures) instead of the infrastructure the skill
  still describes building by hand.
- `STARTUP.md` template: the new paths.

**Done when:** a new repository set up from the skills ends up with the same layout as App Toolkit
and Insider, and the copies in Android Insider, App Toolkit and Smart Cleaner match android-skills.

## Order

```text
Phase 0 (bases) ──► Phase 1 (move) ──► Phase 2 (redesign, one screen at a time) ──► Phase 3 (store, campaign)
                         └──────────────► Phase 4 (skills) can run alongside Phase 2
```

Phase 1 is safe to do now: it moves files and changes no screen. Phase 4 should land before the
next repository is set up, so it starts from the right layout.

## Decisions

Made by the owner on October 28, 2026:

1. **Marketing covers the Play app only.** The library is for the owner's own apps and is not
   promoted: no library brief, no developer campaigns, no `creative/developers/`.
2. **The Stitch exports are not kept.** They are input for the redesign; they are deleted after
   Phase 2 has used them, with no images kept in their place.
3. **English only** for the store listing for now.

Still open:

4. Whether `creative/tools/` explainers are wanted, and for which tools first.
