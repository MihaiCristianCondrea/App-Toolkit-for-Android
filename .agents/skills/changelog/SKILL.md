---
name: changelog
description: >
  Update and review CHANGELOG.md. Use when a change may be relevant to library
  consumers, Sample App users, release notes, or production release history.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-08-27'
  keywords:
    - changelog
    - releases
    - release-notes
    - versioning
---

# Changelog

Maintain `CHANGELOG.md` as a curated record of meaningful changes, not a commit log.

Inspect the existing changelog and relevant implementation before editing it.

## What belongs

Add entries for changes that meaningfully affect:

- App Toolkit Library consumers;
- Sample App users;
- public behavior, APIs, integration, compatibility, reliability, performance, accessibility, or
  important bug fixes.

Skip routine refactors, formatting, tests, documentation, file moves, dependency bumps, and internal
cleanup when externally observable behavior is unchanged.

Describe the outcome rather than the implementation for non-developer users.

## Structure

Separate changes into:

- `## Library Changes`
- `## Sample App Changes`

Use existing categories where appropriate:

- `### Added`
- `### Changed`
- `### Improved`
- `### Fixed`

Only include categories that contain entries.

If one change affects both the Library and Sample App, describe each distinct impact in the
appropriate section without duplicating the same entry.

## Unreleased and releases

New work belongs under `# Unreleased`.

Do not create a release section from source-control versions, version bumps, tags, or library
prereleases alone.

Sample App release history follows actual Google Play production releases. When production release
information is available, treat it as the source of truth for version names, version codes, dates,
and release boundaries.

Changes from development versions that never reached production belong to the next production
release that actually shipped them.

## Writing

Keep entries concise, specific, factual, and understandable without reading the source code.

Combine several implementation commits when they represent one meaningful outcome.

Do not invent version codes, release dates, behavior, or release history.