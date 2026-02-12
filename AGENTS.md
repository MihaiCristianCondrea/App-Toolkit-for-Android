You are an experienced Android developer. Your job is to make correct, maintainable changes while
respecting module boundaries, UI rules, and documentation accuracy.

**Mandatory base classes & ViewModel rules (MUST FOLLOW)**
- All new ViewModels **must extend** `ScreenViewModel` (or `LoggedScreenViewModel` when Firebase breadcrumbs/error reporting are required).
- Do **not** create ViewModels that extend `androidx.lifecycle.ViewModel` directly.
- When creating/modifying ViewModels, follow the rules in:
  - `@./docs/viewmodel-rules-coroutines-flows-state.md` (source of truth for coroutines, flows, jobs, and `UiStateScreen<T>` update patterns)

---

## 1. Project Scope, Boundaries, and Protected Files

### 1.1 Protected files

- **Root `README.md`** is for marketing/human context. Do not modify unless explicitly requested.
- **`LICENSE`** must not be modified.

### 1.2 "Don’t touch" areas (when applicable)

- If the repo contains legacy/archived folders or old prototypes, do not edit them unless explicitly
  asked.
- Prefer working inside the current module structure and active modules only.

---

## 2. Architecture Rules (MUST FOLLOW)

### 2.1 Structure & boundaries

1) Feature-first: `app/<feature>/{data,domain,ui}`.
2) UI never calls DataStore, Firebase, HTTP, Room directly.
3) Domain defines repository interfaces + use cases + domain models.
4) Data implements repositories and owns DTOs/mappers/local+remote sources.
5) ViewModel owns dispatchers and threading decisions; prefer Flow + `flowOn` (see `@./docs/viewmodel-rules-coroutines-flows-state.md`).
6) ViewModel handles Events; emits Actions; state is `UiStateScreen<T>` via `ScreenViewModel` / `LoggedScreenViewModel`.
7) Use resource IDs in domain models for UI text; resolve with `stringResource` in UI.
8) No `runCatching` in ViewModels. Use Flow + `.catch` (see `@./docs/viewmodel-rules-coroutines-flows-state.md`).
9) Initialization is an Event and is sent from `init{}` in ViewModel (see `@./docs/viewmodel-rules-coroutines-flows-state.md`).

### 2.2 Contracts

- **Event:** user intent or lifecycle trigger (sent to `onEvent`).
- **Action:** one-off output (navigation, open link, toast/snackbar request if you use actions).
- **UiState:** everything needed to render, immutable (`UiStateScreen<T>`).

### 2.3 Where things go

- DTOs: `feature/data/remote/dto`
- Mappers: `feature/data/remote/mapper` or `feature/data/mapper`
- DataStore adapters: `core/data/datastore` implements feature-owned `*PreferencesDataSource*`
  interfaces
- Use cases: `feature/domain/usecase`
- ViewModel: `feature/ui/<Feature>ViewModel.kt` (must extend `ScreenViewModel` / `LoggedScreenViewModel`)
- Screen: `feature/ui/<Feature>Screen.kt`
- Components: `feature/ui/components`

---

## 3. Module Direction & DI

### 3.1 Module structure

- Modules: `app`, `core`, `data` (and feature packages under the app/library as defined)
- Dependency direction: `app → core → data`
- Data flows upward conceptually: `data → core → app` (via repository interfaces / exposed models)

### 3.2 Dependency Injection

- Use **Koin** for DI and ViewModel provisioning.
- Avoid global service locator patterns.

---

## 4. UI / UX Rules (Compose + Material 3)

- Build UI with **Jetpack Compose** and **Material 3 Expressive** components.
- Do not create new XML layouts for new UI.
- Keep composables as stateless as possible:
  - Prefer `@Composable fun X(state: ..., onEvent: ...)`
  - Avoid business logic in composables
- Prioritize smoothness:
  - Avoid unnecessary recompositions
  - Don’t allocate in composition hot paths

---

## 5. Localization & Translations Policy (STRICT)

### 5.1 Core translation rule

- If the task is “translate strings”, you must translate **all required locales** for the target
  module.
- Do **not** create new `values-xx` resource directories/files unless explicitly requested.
  - If a locale file is missing but the instruction says “translate”, first check the established
    structure and follow the project’s existing pattern.

### 5.2 Locale coverage source of truth

- **Do not hardcode locale lists in documentation.**
- The source of truth for supported locales is the Gradle configuration:
  - Inspect the relevant `build.gradle.kts` (module-level) to find the full locale list.
  - Note: locales can be added in the future; always re-check Gradle before doing translation
    work.

### 5.3 Translation correctness checklist

- Do not rename string keys.
- Preserve formatting tokens exactly (`%1$s`, `%d`, `\n`, apostrophes, HTML entities if used).
- Keep meaning consistent; avoid over-literal translations that break UI tone.
- Validate build after changes (see section 9).

---

## 6. Documentation Strategy (Sync Docs with Reality)

The agent is responsible for keeping project docs technically accurate.

- The repo contains reference docs under `./docs/`.
- If you change architecture, module wiring, navigation patterns, state contracts, or public APIs:
  - Update the relevant doc file(s) immediately.
- If a referenced doc section is missing or outdated, create or update it with concise guidance.

Doc entry points:

- ViewModel rules (coroutines/flows/state/jobs): `@./docs/viewmodel-rules-coroutines-flows-state.md`
- Architecture and principles: `@./docs/core/`
- UI/UX guidelines: `@./docs/ui-ux/`
- Coroutines and Flow: `@./docs/coroutines-flow/`
- Compose rules: `@./docs/compose/`
- Testing guidelines: `@./docs/tests/`
- General policies: `@./docs/general/`
- General app and libraries docs: `@./docs/screens/`

---

## 7. Documentation Requirements for Code Changes

- Public APIs and non-trivial logic must be documented with **KDoc**.
- When modifying existing logic, include a short **Change Rationale** in one of:
  - The PR description, OR
  - A nearby code comment when the rationale is non-obvious.

The rationale should state:

- What it did before,
- Why it was changed,
- Why the new approach is better,
- Any Material 3 / UX principle that influenced the change (if relevant).

Keep rationale concise.

---

## 8. Practical Guardrails (Quality)

- Prefer explicit, type-safe APIs over cleverness.
- Avoid duplication; extract shared logic into the appropriate layer/module.
- Keep performance in mind (Compose + flows can accidentally allocate a lot).
- Don’t introduce new dependencies without a clear reason and consistency with the existing stack.
