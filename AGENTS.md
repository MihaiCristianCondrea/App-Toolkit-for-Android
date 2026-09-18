# AGENTS.md

Work with the existing project architecture and conventions. Inspect surrounding code and the relevant module documentation before making changes. Prefer the smallest complete solution over broad refactors.

## General

- Reuse existing project patterns before introducing new abstractions.
- Do not perform unrelated cleanup or refactoring.
- Remove code made unused by your changes.
- Do not add dependencies unless genuinely necessary and consistent with the project.
- Use the relevant project skills for architecture, Android APIs, Compose, testing, performance, and other specialized guidance. Do not duplicate those rules here.

## Module context

Each active module or feature may contain a local `README.md`. Treat it as the primary source of context for that module.

Before making substantial changes:

- Read the relevant module `README.md` when one exists.
- Respect its documented ownership, dependencies, public contracts, flows, and known risks.
- Inspect the actual implementation when documentation and code need to be reconciled.
- Do not invent intended architecture that is not supported by the current code.

To ensure modules remain up-to-date when modifying code:

- Update the module's `README.md` whenever a change affects its documented responsibilities, dependencies, contracts, important flows, or architectural risks.
- Point to and use the `android-localization` skill when user-facing strings or localizations need to be added or updated.
- Point to and use the `changelog` skill when a change requires a changelog entry for the module or application.

Do not update module documentation for cosmetic changes, routine maintenance, or internal refactors that preserve documented behavior.

Note: App Toolkit library works on the same patter, its features are documented in the respective modules README files.

## Module layers

Follow the `architecture` skills for placement. Two rules this project has settled that the skills
leave open:

- A `domain/` package holding only models, with no use cases, is fine. The layer names the concept,
  not the operation: a model that crosses the repository, the state holder and the UI is the
  application's vocabulary and belongs there. Do not move such models into `data/models/` and do
  not invent a use case to justify the package.
- Do not add a use case that only forwards a repository call or tidies its result. Trimming,
  filtering and de-duplicating a response is transforming a data-source model into an application
  model, which is repository work. A state holder may depend on a repository directly.

## Localization

When changing user-facing strings, inspect the target module's existing resources and Gradle configuration first, and use the `android-localization` skill.

- Never assume or hardcode the supported locale list.
- Translate all locales required by the target module when translation is part of the task.
- Do not create new locale directories unless explicitly required.
- Preserve resource keys, placeholders, escaping, markup, and formatting tokens exactly.

### Strings belong to the module that owns the feature

Put a string in the module that owns the surface rendering it, not in whichever module already
happened to have a `res/` directory.

- A feature's strings live in that feature module, across every supported locale.
- A body of content that stands on its own, such as an FAQ, gets its own module rather than riding
  along in a general-purpose one. `:library:feature:faq` declares empty placeholder slots and
  `:sample:feature:faq` answers them with the sample's translated copy. Adding or rewording a
  question is then a change to one module, and it touches no code.
- A resource-only module is a legitimate module. It needs no Kotlin sources.
- A string used by two unrelated features belongs in the shared module both already depend on, such
  as `:library:core:ui` for button labels. Duplicating a name across sibling feature modules is
  allowed by resource merging, but prefer one owner.
- A library module that renders host-supplied copy declares the names as `translatable="false"`
  placeholders and documents which host module fills them, so lint does not demand translations of
  empty strings.

## Documentation

Documentation lives per module in the module's local `README.md` and in KDoc—not in the `docs/` folder.

The `docs/` folder is reserved for general guidelines applying across all projects or special cases, such as crash records:
- **Open / Active Crashes**: [open](docs/crashes/open)
- **Fixed / Resolved Crashes**: [fixed](docs/crashes/fixed)

Technical documentation must describe the current code:

- Update technical documentation only when your change makes it inaccurate or changes a documented contract, architecture decision, or public API.
- Do not duplicate module documentation or skill guidance into `AGENTS.md`.
- Do not add comments or KDoc that merely restate the implementation. When touching code, add, improve, or correct relevant KDoc when it helps explain public APIs, contracts, invariants, side effects, ownership, assumptions, or non-obvious behavior. If existing KDoc is inaccurate or outdated, update it to match the current implementation.

## Changelog

When a change may require a changelog update, use the project's `changelog` skill.

Do not duplicate changelog rules or formatting guidance here. The changelog skill defines when an entry is needed, where it belongs, and how it should be written.
