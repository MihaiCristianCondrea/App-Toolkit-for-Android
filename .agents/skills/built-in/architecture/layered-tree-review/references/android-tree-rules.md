# Android Project Tree Rules

File, package, module placement and naming only. Behavior belongs to `android-data-layer`,
`android-domain-layer` and `android-ui-layer`; do not restate their rules here.

Rules marked **checked** are objective and enforced by App Toolkit's `checkModuleBoundaries`.

## Module structure

A feature module may contain:

```text
feature-name/
├── data/       # optional: a feature served by shared repositories has none
├── domain/     # optional: only when it holds a use case
├── di/         # optional: the module's bindings
└── ui/
```

A core or integration module uses the same layer packages when it has layers. A module that only
holds a shared vocabulary, such as analytics event names, may keep flat `models/` and `contracts/`
packages without a layer above them.

Create only the folders that own a real responsibility. Resource-only modules need no placeholder
Kotlin file.

## Package vocabulary

Package names are lowercase plurals from this list. **Checked** for the singular forms and the
banned names below.

| Package                   | Holds                                                              |
|---------------------------|--------------------------------------------------------------------|
| `data/repositories/`      | repository interfaces and their implementations, side by side      |
| `data/models/`            | application models the repositories return                         |
| `data/mappers/`           | mappings shared by several data sources                            |
| `data/exceptions/`        | the module's own failures                                          |
| `data/local/`             | local data sources: DataStore, Room, files, platform APIs          |
| `data/local/models/`      | entities and stored representations                                |
| `data/local/mappers/`     | entity to application model                                        |
| `data/remote/`            | remote data sources: Ktor, Retrofit, SDK clients                   |
| `data/remote/models/`     | DTOs and wire representations                                      |
| `data/remote/mappers/`    | DTO to application model                                           |
| `domain/usecases/`        | use cases                                                          |
| `domain/models/`          | models only a use case produces, next to that use case             |
| `domain/mappers/`         | domain to domain mappings                                          |
| `di/`                     | Koin, Hilt or provider bindings                                    |
| `ui/`                     | screens and ViewModels, flat                                       |
| `ui/contracts/`           | `XEvent` sealed interfaces                                         |
| `ui/states/`              | `XUiState` classes                                                 |
| `ui/models/`              | render models                                                      |
| `ui/mappers/`             | application model to UI model, failures to UI text                 |
| `ui/navigation/`          | keys and page registration                                         |
| `ui/providers/`           | host-supplied content contracts                                    |
| `ui/views/`               | stateless composables the screen uses                              |
| `ui/utils/`               | UI-only helpers                                                    |
| `ui/features/<name>/`     | a parent-owned surface with its own ViewModel (see below)          |
| `utils/extensions/`       | extensions reused across the module's layers                       |
| `utils/constants/`        | constants reused across the module's layers                        |

Never use, **checked**:

| Name                          | Use instead                                         |
|-------------------------------|-----------------------------------------------------|
| `presentation`                | `ui`                                                |
| `datasource`, `datasources`   | the data source directly in `data/local` or `data/remote` |
| `base`                        | the shared screen parts of the UI layer             |
| `helper`, `helpers`, `util`   | `utils`                                             |
| `usecase`, `repository`, `mapper`, `model`, `state`, `view`, `contract` | the plural |

A data source sits directly in `data/local/` or `data/remote/`. Several data sources for distinct
platform sources may each get a subfolder named after the source, such as `data/local/torch/` and
`data/local/sensors/`.

## Placement table

Classify by responsibility first and Kotlin syntax second.

| Kind                | Lives in                              | Rule                                                          |
|---------------------|---------------------------------------|---------------------------------------------------------------|
| DTO                 | `data/remote/models/`                 | **checked** (`*Dto.kt`)                                       |
| Entity              | `data/local/models/`                  |                                                               |
| Application model   | `data/models/`                        | never `domain/models/` without a use case                     |
| Repository          | `data/repositories/`                  | **checked** (`*Repository.kt`), interface and implementation  |
| Data source         | `data/local/` or `data/remote/`       | no `datasource` package, **checked**                          |
| Use case            | `domain/usecases/`                    | **checked** (`*UseCase.kt`)                                   |
| Domain package      | `domain/`                             | exists only with a use case, **checked**                      |
| Mapper              | `mappers/` next to the boundary       | see the mapper table                                          |
| UI model            | `ui/models/`                          |                                                               |
| UI state            | `ui/states/`                          | **checked** (`*UiState.kt`)                                   |
| Event               | `ui/contracts/`                       |                                                               |
| ViewModel           | `ui/`, or `ui/features/<name>/`       | **checked** (`*ViewModel.kt` in a `ui` package, not `views`)  |
| Screen              | `ui/`, with its content in one file   | see `android-ui-layer`                                        |
| Stateless piece     | `ui/views/`                           | no ViewModel, repository or lifecycle owner                   |
| DI bindings         | `di/`                                 |                                                               |
| Feature resources   | the feature's `src/main/res`          | shared modules only for genuinely shared text                 |

## Mappers

Place a mapper by the boundary it crosses:

```text
data/remote/mappers/   DTO to application model
data/local/mappers/    entity or stored form to application model, and back
data/mappers/          mappings shared by several data sources
domain/mappers/        domain to domain
ui/mappers/            application or domain model to UI model, failures to UI text
```

Name a mapper after the type it returns, never after a layer. **Checked**: `toDomain()`,
`toModel()`, `toData()`, `toUi()`, `toEntity()` and `toDto()` are rejected.

```kotlin
internal fun ArticleDto.toArticle(): Article
internal fun Iterable<ArticleDto>.toArticles(): List<Article>
internal fun Article.toArticleUiModel(): ArticleUiModel
internal fun Article.toArticleEntity(): ArticleEntity
```

A mapper is deterministic. It does not perform I/O, read preferences, or silently drop a field the
destination requires. Keep mappers `internal` unless another module needs them.

## Extensions and constants

- An extension that converts between representations is a mapper, and goes in `mappers/`.
- An extension used by one file stays private in that file.
- An extension reused across one module's layers goes in that module's `utils/extensions/`.
- Move an extension to a core module only when modules that do not depend on each other need it.
  Being callable from more than one class is not a reason.
- Constants follow the same rule in `utils/constants/`. A constant that is a stable external
  identifier, such as an analytics name or a product ID, belongs to the module that owns that
  contract.

## Naming

- One top-level class or interface per file, named after it. Private helpers may share the file.
- An interface and its implementation are two files side by side. Name the implementation after
  how it differs, such as `OfflineFirstNewsRepository` or `AndroidTorchDataSource`, or `Default`
  when nothing differs.
- Data sources are named `<Thing><Local|Remote>DataSource`, or after the platform source they wrap.
- Screens, ViewModels, states and events follow `android-ui-layer`: `XScreen`, `XViewModel`,
  `XUiState`, `XEvent`.

## Nested UI features

Prefer a standalone feature module when the functionality is reusable, is a distinct product
capability, has or could have its own navigation destination, owns data or integrations beyond its
parent, or would remain meaningful without the parent screen.

Use `ui/features/<name>/` only when all of these are true:

1. The parent feature exclusively owns the UI.
2. It has its own ViewModel, lifecycle, or equivalent state holder.
3. It is not useful enough to justify a standalone module.
4. A separate module would add dependency or module overhead for nothing.

Stateless UI goes in `views/`. Stateful UI driven by the main screen's ViewModel stays in the main
`ui/` structure. Size or visual complexity alone never justifies `ui/features/`.

## Placement decision

1. Is it reusable or independently meaningful? Prefer a dedicated feature or core module.
2. Which layer owns its responsibility? Use the placement table.
3. Is it a mapper, extension or constant? Use those sections.
4. Is it a parent-owned surface with its own ViewModel? `ui/features/<name>/` may fit.
5. Is ownership still unclear? Leave it in place and defer to the relevant architecture skill.

## Review

Report each problem with its file, the rule, and the smallest fix. Check for:

1. files in the wrong layer, package or module
2. a `domain/` package without a use case
3. package names outside the vocabulary
4. mappers named after a layer, or placed away from their boundary
5. implementation types leaking across module ownership boundaries
6. nested `ui/features/` that should be standalone modules
7. folders with no real responsibility

Prefer moving or renaming over rewriting behavior. A move of a published package is a breaking
change: record it in the project's migration notes.
