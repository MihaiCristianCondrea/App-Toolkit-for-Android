---
name: changelog
description: >
  Maintain CHANGELOG.md for the correct audience. App and sample-app changelogs
  appear inside the application and summarize meaningful user-facing changes since
  the previous production release; library changelogs document consumer-facing APIs
  and compatibility. Use for release history, unreleased changes, and changelog reviews.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-20'
  keywords:
    - changelog
    - releases
    - release-notes
    - versioning
---

# Changelog

Maintain the project's changelog as a curated release history, not a commit log.

Inspect the existing changelog, the last actual published release, and the relevant implementation
before editing it. Choose the audience first: application users read app and sample-app changelogs
**inside the app**; library consumers read the library changelog as developers.

## Project Structure Determination

Determine the project structure to structure the changelog correctly:

- **Library with Sample App**: The project consists of library modules and a sample app. The
  library and the sample each keep their own changelog (`library/CHANGELOG.md` and
  `sample/CHANGELOG.md` in App Toolkit). Update the one, or both, where the change took place.
- **Normal App (No Library)**: The project is a standalone application without a library module.
  Update the changelog directly as an app changelog.

## What Belongs

Add entries for changes that meaningfully affect:

- Library consumers (for library changes): published APIs, migration requirements, integrations,
  compatibility, and externally observable changes.
- Sample-app or application users (for app changes): useful features, behavior, navigation, visual
  improvements, accessibility, reliability, and recognizable bug fixes.

Skip routine refactors, formatting, tests, documentation, file moves, dependency bumps, and internal
cleanup when externally observable behavior is unchanged.

## App and sample-app changelogs: written for users

The app changelog is shown inside the application. Its audience is someone who wants to know
**what changed since the version they previously used**, not how developers built it.

- Use the previous actual **production release** as the comparison baseline. Inspect release history
  and all relevant changes since that release. For an unreleased build, summarize the cumulative
  user-visible delta in `# Unreleased`, including work carried over from testing-only versions.
  Do not treat every commit, merge, build, or test-track upload as a separate user-facing change.
- Curate the release as a whole, not per commit. Merge related changes into one clear entry and
  update that entry as the feature evolves. Drop superseded, reverted, unfinished, hidden, and
  duplicate items. A substantial implementation may merit one bullet; several commits may merit none.
- Describe the effect in plain language, using terms users see in the app. Prefer short, direct,
  readable bullets under `Added`, `Changed`, `Improved`, `Removed`, or `Fixed`.
  No technical implementation terminology unless it is itself the feature users interact with.
- Mention important fixes specifically enough to recognize the problem. Avoid vague filler such as
  "various optimizations" or "many bug fixes" when concrete improvements are known.
- Be selective, not arbitrarily brief. An app changelog can explain more than Google Play's
  500-character "What's new" field, but every entry must earn its place.
- Make performance, security, and reliability claims only when the observed or verified user
  benefit supports them. Do not infer "faster", "safer", or "battery efficient" from a refactor alone.
- Never include internal architecture, dependency updates, CI, tests, module/package names, logging,
  SDK changes, or implementation languages unless they create a real and important user-facing
  difference. Explain that difference, not the internal mechanism.

Examples of app-facing entries:

```md
### Added

- Search tools directly from the top bar.

### Improved

- Tool controls are easier to use on larger screens.

### Fixed

- Fixed an issue where the selected category could reset when returning to the screen.
```

Do not write an app entry such as "Migrated the scanner to Rust", "Refactored ViewModels",
"Updated Gradle dependencies", or "Improved code quality". If a measurable user benefit exists,
describe that benefit without inventing performance figures.

For the stricter Play Console release-note output, use
[`play-store-changelog`](../../../../marketing/.agents/skills/first-party/play-store/play-store-changelog/SKILL.md)
from the repository's marketing collection when installed; do not force its character limit on
the in-app changelog.

## Library changelogs: written for developers

The library changelog has a different audience and may name public classes, APIs, behavior,
migration steps, deprecations, compatibility requirements, and breaking changes. Be precise enough
for consumers to act on it. Do not turn it into a list of internal development commits.

For an App Toolkit-like library with a sample app, update the library and sample-app changelogs
independently by their respective audience and impact.

## Structure

### Library with Sample App

- If a change affects only the library, update the library changelog.
- If a change affects only the sample app, update the sample changelog.
- If a change affects both, describe each distinct impact in its own changelog without duplicating
  the same entry.

### Normal App (No Library)

Maintain a single app changelog directly under `# Unreleased` (or the relevant release section)
using category headers, without splitting into Library or Sample App sections.

### Categories

Use existing categories under the relevant section:

- `### Added`
- `### Changed`
- `### Improved`
- `### Removed`
- `### Fixed`

Only include categories that contain entries.

## File Format

Every changelog has the same shape: one `# Changelog` title, then its sections, newest first, with
a `---` divider before each section. `# Unreleased`, when there is new work, is the first section.

```md
# Changelog

---

# Unreleased

### Added

- ...

---

# October 20, 2026

**Version:** `26.10.16`, `26.10.18`, `26.10.19`

### Fixed

- ...

---

# November 1, 2025

**Version:** `1.2.3`
```

- Keep a blank line around every heading, version line and divider.
- `# Unreleased` has no version line, and nothing but its categories sits between its heading and the
  next divider.
- Never put a divider between a section's heading and its categories, or two dividers in a row.

## Unreleased and Releases

New work belongs under `# Unreleased`.

Do not create a release section from source-control versions, version bumps, tags, internal or
testing tracks, or library prereleases alone.

A release section is headed by its release date, then one version line:

```md
# Month Day, Year

**Version:** `x.x.x`
```

- The version line lists version names only (`versionName` for an app, the published version for a
  library), never version codes or build numbers.
- Several releases that shipped the same changes, such as a fix uploaded twice in a row, share one
  section: list their versions on the one line, oldest first, separated by commas
  (`` `26.10.16`, `26.10.18` ``), and date the section by the last of them.
- Use the actual release date as the heading.

App release history follows actual Google Play production releases. When production release
information is available, treat it as the source of truth for version names, dates and release
boundaries. Builds that only reached internal or testing tracks are not releases: their changes stay
under `# Unreleased` until a production release ships them.

Library release history follows actual published library releases.

Changes from development versions that never reached production belong to the next production
release that actually shipped them.

## Writing

Shared prose rules (punctuation, banned phrasing, tone) are in
[`writing-style`](../writing-style/SKILL.md). The rules below are specific to changelogs.

- Keep app entries short, concrete, and understandable without knowing the codebase. Most need one
  sentence; longer explanations are appropriate only when users need context or an action.
- Keep library entries specific about consumer-facing APIs and migration impact.
- Use flat bullet points under the categories above, without nested sub-lists.
- Combine several implementation commits when they represent one meaningful outcome.
- Do not invent versions, release dates, behavior, or release history.
