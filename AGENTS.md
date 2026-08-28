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

Update the module `README.md` only when a change meaningfully affects its documented responsibilities, dependencies, contracts, important flows, or architectural risks.

Do not update it for cosmetic changes, routine maintenance, or internal refactors that preserve the documented behavior.

## Localization

When changing user-facing strings, inspect the target module's existing resources and Gradle configuration first.

- Never assume or hardcode the supported locale list.
- Translate all locales required by the target module when translation is part of the task.
- Do not create new locale directories unless explicitly required.
- Preserve resource keys, placeholders, escaping, markup, and formatting tokens exactly.

## Documentation

Documentation must describe the current code.

Update technical documentation only when your change makes it inaccurate or changes a documented contract, architecture decision, or public API.

Do not duplicate module documentation or skill guidance into `AGENTS.md`.

Do not add comments or KDoc that merely restate the implementation. When touching code, add, improve, or correct relevant KDoc when it helps explain public APIs, contracts, invariants, side effects, ownership, assumptions, or non-obvious behavior. If existing KDoc is inaccurate or outdated, update it to match the current implementation.

## Changelog

When a change may require a changelog update, use the project's changelog skill.

Do not duplicate changelog rules or formatting guidance here. The changelog skill defines when an entry is needed, where it belongs, and how it should be written.