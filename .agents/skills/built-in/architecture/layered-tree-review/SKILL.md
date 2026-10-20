---
name: layered-tree-review
description: Review and decide Android/Kotlin file, package, and module placement and naming. Use when creating or moving screens, ViewModels, repositories, data sources, DTOs, entities, models, mappers, extensions, constants, use cases, navigation, DI modules, or UI state, when naming a mapper or a package, when deciding whether a domain package should exist, or when auditing a project tree. Structure and naming only; the data, domain and UI layer skills own behavior.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-20'
  keywords:
  - android
  - feature organization
  - file placement
  - layered architecture
  - module structure
  - package organization
  - project structure
  - mapper naming
  - code conventions
---

# Android Project Tree

Decide where a file goes and what it is called, so that any contributor, human or agent, puts the
same file in the same place.

Read `references/android-tree-rules.md` before placing, moving or naming code. It holds the package
vocabulary, the placement table, the naming rules and the review checklist.

The rules follow Google's Android architecture guidance, not generic Clean Architecture:

- The [domain layer](https://developer.android.com/topic/architecture/domain-layer) is optional and
  holds use cases. A `domain/` package with no use case does not exist.
- Application models belong to the [data layer](https://developer.android.com/topic/architecture/data-layer)
  that produces them, in `data/models/`, not in `domain/models/`.
- Repository interfaces live with their implementations in `data/repositories/`.

## Sibling skills

Use them when behavior, not placement, decides the answer:

- `android-data-layer`: repositories, data sources, models, errors, threading, caching.
- `android-domain-layer`: whether a use case should exist.
- `android-ui-layer`: the files a screen needs, its state, events and errors.

Do not duplicate or override their rules here.

## How to apply

1. Classify the file by what it does, using the placement table, not by its syntax. An extension
   function that maps a DTO is a mapper; an extension that formats a string for search is a utility.
2. Place it in the module that owns the responsibility, then in the package the table names.
3. Name it with the naming rules.
4. Create a folder only when a file belongs in it. Never add empty layers for symmetry.

When reviewing, prefer small mechanical moves and renames over behavior changes, and report only
meaningful placement problems. When ownership depends on behavior, defer to the sibling skill.

## Enforcement

Rules marked **checked** in the reference are objective. App Toolkit's `checkModuleBoundaries`
task rejects violations of them in `:library:*` and `:sample:*`. A consumer application only has
that guarantee if it carries an equivalent check; otherwise treat them as review rules.
