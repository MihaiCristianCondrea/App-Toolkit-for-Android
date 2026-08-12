# AGENTS.md

Work with the existing project architecture and conventions. Inspect surrounding code before making changes and prefer the smallest complete solution over broad refactors.

## General

* Reuse existing project patterns before introducing new abstractions.
* Do not perform unrelated cleanup or refactoring.
* Remove imports, declarations, parameters, and code made unused by your changes.
* Do not add dependencies unless they are genuinely necessary and consistent with the existing project.

## Localization

When changing user-facing strings, inspect the target module's existing resources and Gradle configuration first.

* Never assume or hardcode the supported locale list.
* Translate all locales required by the target module when translation is part of the task.
* Do not create new locale directories unless explicitly required.
* Preserve resource keys, placeholders, escaping, markup, and formatting tokens exactly.

## Documentation

Each active Gradle module documents its ownership, dependencies, contracts, logic flow, and risks in
the local `README.md`. Read the relevant module README before changing that module and update it when
a change affects those claims.

ViewModel, event/action, coroutine, Flow, and `UiStateScreen<T>` rules are documented with the
presentation contracts in `@./library/core/ui/README.md`.

Do not duplicate detailed module documentation into `AGENTS.md`.

Update documentation only when the change makes existing technical documentation inaccurate or changes a documented contract, architecture rule, or public API.

Do not add comments or KDoc that merely restate the implementation. Document contracts, invariants, and non-obvious reasoning where useful.
