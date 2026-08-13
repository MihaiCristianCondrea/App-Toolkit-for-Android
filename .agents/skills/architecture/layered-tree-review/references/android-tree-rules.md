# Android Project Tree Rules

Use this reference only for file and package placement.

Architecture behavior belongs to the dedicated `android-data-layer` and
`android-domain-layer` skills. Do not duplicate their rules here.

Prefer small, mechanical moves. Do not redesign behavior just to match a tree.

## Feature tree

```text
feature-name/
├── ui/
├── data/
└── domain/        # optional
```

Create only folders that have a real responsibility.

## UI

```text
ui/
├── models/
├── mappers/
├── views/
├── contracts/
├── navigation/
├── states/
├── utils/
├── ScreenName.kt
└── ScreenNameViewModel.kt
```

Place:

- screens and ViewModels in `ui/`
- UI state in `ui/states/`
- render-specific models in `ui/models/`
- presentation mappings in `ui/mappers/`
- reusable Composables in `ui/views/`
- UI actions/events/contracts in `ui/contracts/`
- routes/navigation types in `ui/navigation/`
- UI-only helpers in `ui/utils/`

Do not place DTOs, entities, repository/data-source implementations, database,
filesystem, DataStore, or remote API code in `ui/`.

## Data

```text
data/
├── repositories/
├── models/
├── mappers/
├── local/
├── remote/
└── utils/
```

### Repositories

```text
data/repositories/
```

Place repository contracts and implementations here.

```text
NewsRepository.kt
DefaultNewsRepository.kt
OfflineFirstNewsRepository.kt
```

When a repository interface exists, keep it in the data layer with its
implementation.

### Local

```text
data/local/
├── models/
├── mappers/
├── interfaces/
└── utils/
```

Place Room, DataStore, files, caches, local/platform-backed storage, DAOs,
entities, local models, and local mappings here.

### Remote

```text
data/remote/
├── models/
├── mappers/
├── interfaces/
└── utils/
```

Place Ktor/Retrofit sources, network DTOs, API response models, remote
interfaces, and remote mappings here.

### Shared data

Use:

```text
data/models/
```

for application-facing models owned by data when a separate representation is
useful.

Use:

```text
data/mappers/
```

for mappings shared across sources or not specific to local/remote.

## Domain

`domain/` is optional.

```text
domain/
├── models/
├── mappers/
├── usecases/
└── utils/
```

Place:

- use cases in `domain/usecases/`
- genuinely domain-specific models in `domain/models/`
- domain-to-domain mappings in `domain/mappers/`
- domain-only helpers in `domain/utils/`

Do not place repository contracts here merely to imitate Clean Architecture.

## Mapper placement

```text
data/remote/mappers/   remote DTO/API mappings
data/local/mappers/    entity/DataStore/local mappings
data/mappers/          shared or cross-source data mappings
domain/mappers/        domain-to-domain mappings
ui/mappers/            application/domain -> UI mappings
```

Do not create extra model or mapper layers only to complete the tree.

## Quick reference

| Responsibility            | Location               |
|---------------------------|------------------------|
| Screen / Composable       | `ui/`                  |
| ViewModel                 | `ui/`                  |
| UI state                  | `ui/states/`           |
| UI model                  | `ui/models/`           |
| UI mapper                 | `ui/mappers/`          |
| Reusable UI               | `ui/views/`            |
| Navigation                | `ui/navigation/`       |
| Repository interface      | `data/repositories/`   |
| Repository implementation | `data/repositories/`   |
| Remote data source        | `data/remote/`         |
| Network DTO               | `data/remote/models/`  |
| Remote mapper             | `data/remote/mappers/` |
| DAO / local source        | `data/local/`          |
| Entity / local model      | `data/local/models/`   |
| Local mapper              | `data/local/mappers/`  |
| Shared data model         | `data/models/`         |
| Shared data mapper        | `data/mappers/`        |
| Use case                  | `domain/usecases/`     |
| Domain model              | `domain/models/`       |
| Domain mapper             | `domain/mappers/`      |

## Review

Check only:

1. wrong layer/folder
2. implementation-specific types leaking into the wrong tree
3. duplicate mapper placement
4. inconsistent naming/package structure

Prefer moving or renaming files over rewriting behavior.

If ownership is ambiguous, leave the file in place and defer to the relevant
architecture skill.
