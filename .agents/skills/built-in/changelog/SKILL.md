---
name: changelog
description: >
  Update and review CHANGELOG.md. Use when a change may be relevant to library
  consumers, sample app users, app users, release notes, or production release history.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-22'
  keywords:
    - changelog
    - releases
    - release-notes
    - versioning
---

# Changelog

Maintain the project's changelog as a curated record of meaningful changes, not a commit log.

Inspect the existing changelog and relevant implementation before editing it.

## Project Structure Determination

Determine the project structure to structure the changelog correctly:

- **Library with Sample App**: The project consists of library modules and a sample app. The
  library and the sample each keep their own changelog (`library/CHANGELOG.md` and
  `sample/CHANGELOG.md` in App Toolkit). Update the one, or both, where the change took place.
- **Normal App (No Library)**: The project is a standalone application without a library module.
  Update the changelog directly as an app changelog.

## What Belongs

Add entries for changes that meaningfully affect:

- Library consumers (for library changes);
- Sample App or App users (for application changes);
- Public behavior, APIs, integration, compatibility, reliability, performance, accessibility, or
  important bug fixes.

Skip routine refactors, formatting, tests, documentation, file moves, dependency bumps, and internal
cleanup when externally observable behavior is unchanged.

Describe the outcome rather than the implementation for non-developer users.

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

- Keep each entry to the outcome and what a reader needs to act on it, usually one or two sentences.
- **Never use em dashes (`—`)** or overly verbose AI phrasing. Use commas, parentheses, or short direct sentences.
- Use flat bullet points under the categories above, without nested sub-lists.
- Combine several implementation commits when they represent one meaningful outcome.
- Do not invent versions, release dates, behavior, or release history.
