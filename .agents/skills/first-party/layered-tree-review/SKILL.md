---
name: layered-tree-review
description: Review and decide Android/Kotlin file, package, and module placement and naming. Use when creating or moving screens, ViewModels, repositories, data sources, DTOs, entities, models, mappers, reducers, transformers, validators, formatters, converters, adapters, extensions, layer-local constants, use cases, navigation, DI modules, or UI state, when deciding package ownership or naming, whether a domain package should exist, or when auditing a project tree. Structure and naming only; the data, domain and UI layer skills own behavior.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-11-22'
  keywords:
  - android
  - feature organization
  - file placement
  - layered architecture
  - module structure
  - package organization
  - project structure
  - mapper naming
  - reducer placement
  - extensions
  - validators
  - layer constants
  - code conventions
---

# Android Project Tree

Give each responsibility its designated file and package so contributors can find it. Apply the
placement rules even when the implementation is short.

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
- `android-ui-layer`: Google's guidance on UI state, events, and state holders.
- `app-toolkit`: Toolkit screen contracts and implementation conventions.

This skill owns module, package, and file placement. Use the sibling skills for behavior and
`writing-style` for KDoc and module documentation.

## How to apply

1. Classify by responsibility before syntax. A DTO-to-model extension is a mapper; a pure
   UI-state transition is a reducer; a display-string function is a formatter even if it is an
   extension function. A Flow operator that owns a transformation follows transformer placement.
2. Place each independently extracted responsibility and layer-owned constant in its designated
   package and responsibility-named file. Keep local state copies and one-off implementation values with their owner. Follow the
   reference for operation identifiers and analytics placement. Placement rules do not require
   extracting a helper.
3. Keep each responsibility in the owning module and layer. Use the package and file naming rules;
   keep layer-specific constants and extensions in their layer's packages.
4. Create packages when their first real file is needed. Do not create empty directories, an
   unnecessary use case or domain layer, or an adapter with no actual interface to bridge.
5. During reviews, flag misplaced or mixed responsibilities even for one-line functions;
   recommend the smallest behavior-preserving extraction or move first.

When reviewing, prefer small mechanical moves and renames over behavior changes, and report only
meaningful placement problems. When ownership depends on behavior, defer to the sibling skill.

## Enforcement

Rules marked **checked** in the reference are objective. App Toolkit's `checkModuleBoundaries`
task rejects violations of them in `:library:*` and `:sample:*`. A consumer application only has
that guarantee if it carries an equivalent check; otherwise treat them as review rules.
