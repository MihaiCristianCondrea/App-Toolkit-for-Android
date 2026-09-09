---
name: changelog
description: >
  Update and review CHANGELOG.md. Use when a change may be relevant to library
  consumers, sample app users, app users, release notes, or production release history.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-08-28'
  keywords:
    - changelog
    - releases
    - release-notes
    - versioning
---

# Changelog

Maintain `CHANGELOG.md` as a curated record of meaningful changes, not a commit log.

Inspect the existing changelog and relevant implementation before editing it.

## Project Structure Determination

Determine the project structure to structure the changelog correctly:

- **Library with Sample App**: The project consists of a library module and a sample/demo app module. Update the relevant section (`## Library Changes`, `## Sample App Changes`, or both) depending on where the change took place.
- **Normal App (No Library)**: The project is a standalone application without a library module. Update the changelog directly as an app changelog.

## What Belongs

Add entries for changes that meaningfully affect:

- Library consumers (for library changes);
- Sample App or App users (for application changes);
- Public behavior, APIs, integration, compatibility, reliability, performance, accessibility, or important bug fixes.

Skip routine refactors, formatting, tests, documentation, file moves, dependency bumps, and internal cleanup when externally observable behavior is unchanged.

Describe the outcome rather than the implementation for non-developer users.

## Structure

### Library with Sample App

Separate changes into:

- `## Library Changes`
- `## Sample App Changes`

Update the section relevant to your change:
- If a change affects only the library, update `## Library Changes`.
- If a change affects only the sample app, update `## Sample App Changes`.
- If a change affects both, describe each distinct impact in its appropriate section without duplicating the same entry.

### Normal App (No Library)

Maintain a single app changelog directly under `# Unreleased` (or the relevant release section) using category headers, without splitting into Library or Sample App sections.

### Categories

Use existing categories under the relevant section:

- `### Added`
- `### Changed`
- `### Improved`
- `### Fixed`

Only include categories that contain entries.

## Unreleased and Releases

New work belongs under `# Unreleased`.

Do not create a release section from source-control versions, version bumps, tags, or library prereleases alone.

When creating an actual release section:

For App / Sample App releases:

```md
# Month Day, Year

**Version:** `x.x.x` (`versioncode`)
```

For Library releases:

```md
# Month Day, Year

**Version:** `x.x.x`
```

Use the actual release date as the heading.

For App releases, include both `versionName` and `versionCode` when known.

For Library releases, include the published library version.

App release history follows actual Google Play production releases. When production release information is available, treat it as the source of truth for version names, version codes, dates, and release boundaries.

Library release history follows actual published library releases.

Changes from development versions that never reached production belong to the next production release that actually shipped them.

## Writing

Keep entries concise, specific, factual, and understandable without reading the source code.

Combine several implementation commits when they represent one meaningful outcome.

Do not invent version codes, release dates, behavior, or release history.
