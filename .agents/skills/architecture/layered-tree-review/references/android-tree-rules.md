# Android Project Tree Rules

Use this skill only for file, package, and module placement.

Architecture behavior belongs to the dedicated `android-data-layer` and `android-domain-layer` skills. Do not duplicate those rules here.

Prefer small structural changes. Do not redesign behavior only to satisfy a directory tree.

## Feature module structure

A feature module may contain:

```text
feature-name/
├── ui/
├── data/
├── domain/        # optional
└── di/            # optional
```

Create only folders that own a real responsibility.

* `ui`: presentation, state, navigation, and UI models
* `data`: repositories, data sources, persistence, SDK mappings, and data models
* `domain`: useful business operations and genuinely domain-specific models
* `di`: module-specific dependency injection bindings

`domain/` and `di/` are optional.

A feature that consumes shared repositories can omit its own `data/`.

## Prefer modules over nested UI features

When functionality is meaningful enough to stand on its own, prefer creating or extracting a dedicated feature module instead of placing it under another feature's `ui/features/`.

Prefer a standalone feature module when the functionality:

* can be reused by multiple screens or modules
* represents a distinct product capability
* has its own navigation destination or could reasonably become one
* owns data, domain logic, or integrations beyond the parent screen
* should have an independent dependency boundary
* would remain meaningful without the parent screen

Fictional Example:

```text
feature/
├── cart/
│   └── ...
└── filters/
    └── ...
```

Do not keep reusable functionality nested inside `cart/ui/features/filters/` merely because Cart currently uses it first.

## UI

Keep the main screen flat inside `ui/`.

```text
ui/
├── contracts/
├── mappers/
├── models/
├── navigation/
├── states/
├── utils/
├── views/
├── CartScreen.kt
└── CartViewModel.kt
```

Create only the folders the screen actually needs.

### `views/`

`views/` contains reusable stateless UI owned by that screen or feature.

Do not place ViewModels, state coordinators, repositories, or lifecycle-owning components in `views/`.

### Nested UI features are a last resort

Use:

```text
ui/features/<feature-name>/
```

only for UI functionality that is tightly coupled to one parent screen but still owns an independent presentation lifecycle.

Typical examples include:

* a bottom sheet with its own ViewModel
* a dialog with its own ViewModel
* a screen-local sub-flow with independent state and lifecycle

Example:

```text
cart/
└── ui/
    ├── contracts/
    ├── models/
    ├── states/
    ├── views/
    ├── CartScreen.kt
    ├── CartViewModel.kt
    └── features/
        └── filtersheet/
            ├── contracts/
            ├── models/
            ├── states/
            ├── views/
            ├── FilterSheet.kt
            └── FilterSheetViewModel.kt
```

Use `ui/features/` only when all of these are true:

1. The UI is owned exclusively by the parent feature.
2. It has its own ViewModel, lifecycle, or equivalent state coordinator.
3. It is not useful enough to justify a standalone feature module.
4. Moving it into a separate module would add unnecessary dependency or module overhead.

If it is reusable or independently meaningful, create a feature module instead.

If it is stateless UI, place it in `views/`.

If it is ordinary stateful UI controlled by the main screen ViewModel, keep it in the main `ui/` structure.

Do not create `ui/features/` merely because a composable is large or visually complex.

## Standard UI placement

```text
ui/
├── contracts/      # UI actions, events, contracts
├── mappers/        # application or domain to UI mappings
├── models/         # render-specific models
├── navigation/     # routes and navigation types
├── states/         # UI state
├── utils/          # UI-only helpers
└── views/          # stateless reusable composables
```

Main screens and their ViewModels live directly in `ui/`.

Do not place DTOs, entities, repositories, data sources, database code, filesystem code, DataStore code, or remote API implementations in `ui/`.

## Data

```text
data/
├── repositories/
├── models/
├── mappers/
├── local/
│   ├── models/
│   ├── mappers/
│   ├── interfaces/
│   └── utils/
├── remote/
│   ├── models/
│   ├── mappers/
│   ├── interfaces/
│   └── utils/
└── utils/
```

### Repositories

Place repository contracts and implementations in:

```text
data/repositories/
```

Example:

```text
NewsRepository.kt
DefaultNewsRepository.kt
OfflineFirstNewsRepository.kt
```

Do not move repository interfaces into `domain/` only to imitate a Clean Architecture template.

### Local data

Use `data/local/` for:

* Room
* DataStore
* files
* caches
* DAOs
* entities
* local models
* platform-backed storage
* local mappings

### Remote data

Use `data/remote/` for:

* Ktor or Retrofit sources
* network DTOs
* API response models
* remote interfaces
* remote mappings

### Shared data

Use:

```text
data/models/
```

for application-facing data models when a separate representation is useful.

Use:

```text
data/mappers/
```

for mappings shared across data sources.

Do not create extra models or mappers only to complete the tree.

## Domain

`domain/` is optional.

```text
domain/
├── models/
├── mappers/
├── usecases/
└── utils/
```

Use it for:

* meaningful business operations
* genuinely domain-specific models
* domain-to-domain mappings
* domain-only helpers

Do not create a domain layer when the UI can cleanly consume the repository directly.

## Dependency injection

`di/` is optional.

```text
di/
```

Place module-specific Koin, Hilt, provider, or binding definitions here.

Composition roots assemble feature modules and provide host configuration or extension points.

## Mapper placement

```text
data/remote/mappers/   DTO or API mappings
data/local/mappers/    entity, DataStore, or local mappings
data/mappers/          shared data mappings
domain/mappers/        domain-to-domain mappings
ui/mappers/            application or domain to UI mappings
```

Choose the location based on the source and destination responsibility.

## Resources

Feature-specific resources belong in the owning feature module's `src/main/res`.

Move resources into a core module only when their responsibility is genuinely shared.

Resource-only Android modules do not need placeholder Kotlin source files.

## Placement decision

When deciding where something belongs, use this order:

1. Is it reusable or independently meaningful?

    * Prefer a dedicated feature or core module.

2. Is it part of the main screen?

    * Keep it directly in the feature's normal `ui/` structure.

3. Is it a stateless reusable composable?

    * Place it in `ui/views/`.

4. Is it a parent-owned secondary UI surface with its own ViewModel or lifecycle?

    * `ui/features/<name>/` may be appropriate.

5. Is ownership unclear?

    * Leave it in place and defer to the relevant architecture skill.

## Review

Check only for:

1. files in the wrong layer or module
2. implementation-specific types leaking across ownership boundaries
3. duplicate or incorrect mapper placement
4. inconsistent package or directory naming
5. nested `ui/features/` that should instead be standalone modules
6. unnecessary folders with no real responsibility

Prefer moving or renaming files over rewriting behavior.
