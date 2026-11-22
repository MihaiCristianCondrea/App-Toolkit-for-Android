# Android Project Tree Rules

File, package, module placement and naming only. Behavior belongs to `android-data-layer`,
`android-domain-layer` and `android-ui-layer`; do not restate their rules here.

Rules marked **checked** are objective and enforced by App Toolkit's `checkModuleBoundaries`.
New responsibility categories below are required by this skill's placement convention, but are
not yet enforced automatically by App Toolkit's `checkModuleBoundaries`. Inspect the project's
actual architecture and existing contracts before moving files.

## Module structure

A feature module may contain:

```text
feature-name/
├── data/       # optional: a feature served by shared repositories has none
├── domain/     # optional: only when it holds a use case
├── di/         # optional: the module's bindings
├── workers/    # optional: WorkManager workers the module schedules
├── services/   # optional: services, tiles and AppFunctions the system starts
├── receivers/  # optional: broadcast receivers
└── ui/
```

`workers/`, `services/` and `receivers/` hold framework entry points: classes the system creates
and calls, the way it opens a screen. Like a screen, an entry point asks the data and domain layers
for the work and reports what came of it, product analytics included. It holds no business logic
and no storage of its own. A class that only wraps a platform API for a repository, such as a
WorkManager scheduler or a cleanup engine, is a data source and stays in `data/`.

A core or integration module uses the same layer packages when it has layers. A module that only
holds a shared vocabulary, such as analytics event names, may keep flat `models/` and `contracts/`
packages without a layer above them.

A shared analytics module holds only the vocabulary several features use: common parameter names,
the app-wide action event, transport-agnostic helpers. A feature's own events, parameters, builders
and GA4 contracts live in that feature's `ui/analytics/`, so the shared module does not grow with
every feature. A name in the shared module that only one feature uses moves to that feature. The app's screen
identifiers (`TrackedScreen`s) may stay together in one registry there, with a test that keeps their names unique:
GA4 screen names share one namespace across the app.

Create a responsibility's package as soon as it has a real file, even if the implementation is
small. Do not create empty folders or placeholder code. Resource-only modules need no placeholder
Kotlin file.

## Package vocabulary

Package names are lowercase plurals from this list. **Checked** for the singular forms and the
banned names below.

| Package                   | Holds                                                              |
|---------------------------|--------------------------------------------------------------------|
| `data/repositories/`      | repository interfaces and their implementations, side by side      |
| `data/contracts/`         | data-layer contracts shared by independent data responsibilities   |
| `data/models/`            | application models the repositories return                         |
| `data/mappers/`           | mappings shared by several data sources                            |
| `data/exceptions/`        | the module's own failures                                          |
| `data/local/`             | local data sources: DataStore, Room, files, platform APIs          |
| `data/local/models/`      | entities and stored representations                                |
| `data/local/mappers/`     | entity to application model                                        |
| `data/remote/`            | remote data sources: Ktor, Retrofit, SDK clients                   |
| `data/remote/models/`     | DTOs and wire representations                                      |
| `data/remote/mappers/`    | DTO to application model                                           |
| `data/local/converters/`  | Room type converters and storage encodings                         |
| `data/remote/converters/` | wire/API encoding and decoding when not a model mapper            |
| `data/reducers/`          | pure data-owned state transitions                                  |
| `data/transformers/`      | shared data streams and representation combinations                |
| `data/validators/`        | storage or transport data constraints                              |
| `data/formatters/`        | data-owned export or serialization text, not UI copy               |
| `data/adapters/`          | data-layer API or legacy interface bridges                         |
| `data/extensions/`        | data-layer extensions that are not mappers, formatters, etc.       |
| `data/constants/`         | data-layer internal constants and keys                             |
| `domain/usecases/`        | use cases                                                          |
| `domain/models/`          | models only a use case produces, next to that use case             |
| `domain/mappers/`         | domain to domain mappings                                          |
| `domain/reducers/`        | business-state transitions, when a domain layer already exists    |
| `domain/transformers/`    | domain-owned derivations for use cases                              |
| `domain/validators/`      | business-rule validation for use cases                             |
| `domain/formatters/`      | domain-owned representations, not UI display text                  |
| `domain/adapters/`        | domain boundary adapters only when justified                       |
| `domain/extensions/`      | domain-only extensions supporting use cases                        |
| `domain/constants/`       | business constants belonging to existing use cases                |
| `di/`                     | Koin, Hilt or provider bindings                                    |
| `ui/`                     | screens and ViewModels, flat                                       |
| `ui/contracts/`           | `XEvent` sealed interfaces                                         |
| `ui/states/`              | `XUiState` classes                                                 |
| `ui/models/`              | render models                                                      |
| `ui/mappers/`             | application model to UI model, failures to UI text                 |
| `ui/reducers/`            | pure `UiState` + action to next `UiState` operations                 |
| `ui/transformers/`        | combined UI streams and derived presentation data                  |
| `ui/validators/`          | input/form validation for UI feedback                              |
| `ui/formatters/`          | display-ready dates, sizes, labels, and other UI text              |
| `ui/adapters/`            | UI-specific API or component bridges                               |
| `ui/extensions/`          | UI-layer extensions that are not mappers, formatters, etc.         |
| `ui/constants/`           | UI-only constants such as presentation thresholds                  |
| `ui/analytics/`           | feature analytics identifiers and event builders                   |
| `ui/parsers/`             | encoded presentation content to structured UI content             |
| `ui/search/`              | typed settings search providers                                   |
| `ui/navigation/`          | keys and page registration                                         |
| `ui/providers/`           | host-supplied content contracts                                    |
| `ui/views/`               | stateless composables the screen uses                              |
| `ui/utils/`               | UI-only helpers                                                    |
| `ui/features/<name>/`     | a parent-owned destination or independently stateful surface (see below)          |
| `workers/`                | WorkManager workers: framework entry points                        |
| `services/`               | services, tile services and AppFunctions: framework entry points   |
| `receivers/`              | broadcast receivers: framework entry points                        |
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
| Repository          | `data/repositories/`                  | **checked** (`*Repository.kt`), interface and implementation; App Toolkit's `TelemetryRepository` family stays in `core.common.telemetry`, a cross-cutting contract the UI reports through |
| Data source         | `data/local/` or `data/remote/`       | no `datasource` package, **checked**                          |
| Use case            | `domain/usecases/`                    | **checked** (`*UseCase.kt`)                                   |
| Domain package      | `domain/`                             | exists only with a use case, **checked**                      |
| Mapper              | `mappers/` next to the boundary       | see the mapper table                                          |
| Reducer             | `ui/reducers/`, `data/reducers/`, or existing `domain/reducers/` | independently defined pure state transition |
| Transformer         | owning layer's `transformers/`        | separate any transformation responsibility; size is irrelevant |
| Validator           | owning layer's `validators/`          | distinguish UI input checks from business and storage rules   |
| Formatter           | `ui/formatters/` for display text     | data/domain only when the text is their own contract          |
| Converter           | `data/local/converters/` or `data/remote/converters/` | persistence/API encoding, not DTO-to-model mapping |
| Adapter             | owning layer's `adapters/`            | bridges genuinely incompatible APIs or contracts             |
| Layer extension     | owning layer's `extensions/`          | separate every extension that owns this responsibility       |
| Layer constant      | owning layer's `constants/`           | shared layer-owned constants by concern                |
| UI model            | `ui/models/`                          |                                                               |
| UI state            | `ui/states/`                          | **checked** (`*UiState.kt`)                                   |
| Event               | `ui/contracts/`                       |                                                               |
| ViewModel           | `ui/`, or `ui/features/<name>/`       | **checked** (`*ViewModel.kt` in a `ui` package, not `views`)  |
| Screen              | `ui/`, with its content in one file   | see `app-toolkit` for Toolkit screen contracts                                        |
| Stateless piece     | `ui/views/`                           | child composable with a separate rendering responsibility                   |
| DI bindings         | `di/`                                 |                                                               |
| Worker              | `workers/`                            | an entry point; may report analytics on its outcome           |
| Service or receiver | `services/` or `receivers/`           | an entry point; may report analytics on its outcome           |
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

## Reducers, transformations, validators and related responsibilities

The category describes the **operation's contract**, not whether it is a Kotlin extension,
function, class, or Flow operator. Use only the category that clarifies actual ownership.

| Kind | Responsibility | Example | Preferred package |
| --- | --- | --- | --- |
| Reducer | Calculates next state from current state and an action, without side effects | `reduce(state, action)` | `ui/reducers/` for UI state; data/domain equivalent only if it owns the state |
| Mapper | Maps one model across a representation boundary | `ArticleDto.toArticle()` | the boundary's `mappers/` (see above) |
| Extension | Adds useful operations to a type; placement follows the operation's meaning | `Context.isDarkMode()` | `ui/extensions/` if UI-only, otherwise owning layer's `extensions/` |
| Parser | Reads encoded content into a structured form | `MarkdownReleaseParser.parse(markdown)` | `ui/parsers/` when the result is presentation-owned; otherwise the data source owns the format |
| Transformer | Derives or combines information into a representation or stream | combining `Flow<Article>` streams for a UI model | owning layer's `transformers/` |
| Validator | Checks rules and returns an outcome/violation | `EmailValidator.validate(input)` | `ui/validators/` for form feedback; `domain/validators/` for business rules; `data/validators/` for storage constraints |
| Formatter | Produces display-ready text | `date.formatForDisplay()` | `ui/formatters/` for user-visible formatting |
| Converter | Encodes/decodes storage or wire representations | Room `@TypeConverter` | `data/local/converters/`; `data/remote/converters/` for wire formats |
| Adapter | Bridges incompatible APIs or interfaces | `LegacyApiAdapter` | `data/adapters/` for data integrations; another layer only if it owns the bridge |
| Use case / interactor | Encapsulates a meaningful, reusable business operation | `GetArticlesUseCase` | `domain/usecases/` only when the optional domain layer is warranted |

- **Reducers:** a UI reducer computes the next `UiState` from state and action without
  performing repository calls, navigation, GA4 logging, or persistence. The ViewModel or
  appropriate effect owner handles side effects. Always place a reducer in its own
  `reducers/` file, including a short reducer with a distinct contract. A local `setState { copy(...) }` or
  `MutableStateFlow.update { it.copy(...) }` does not require a reducer.
- **Transformers:** classify the operation by responsibility, not syntax. Place a named transformation with its own contract in `transformers/`. Keep a local Flow
  composition with its caller when it only assembles that operation's inputs. A DTO-to-model mapping inside a Flow still belongs
  to `mappers/`. Do not mix transformation and reduction responsibilities in one file.
- **Validators:** input formatting and presentation checks live in UI. Shared business invariants
  must not depend on UI; if they justify a use case, its domain rules can live in that domain
  layer. Data format, schema and persistence checks belong in data.
- **Formatters vs converters:** user-facing dates/sizes are UI formatters. `@TypeConverter` and
  wire encoding belong in data. Do not label unit-conversion business logic a Room converter.
- **Adapters:** bridge an actual API mismatch, not a routine model transformation. Do not wrap
  an existing compatible interface without a reason.

**Mandatory placement:** a distinct responsibility belongs in a descriptively named file in its
owning package. Size and reuse are not exemptions from placement. Use a separate operation when
there is an independent responsibility to name. A file may contain private implementation
details of its own responsibility, but cannot also own a distinct reducer, mapper, validator,
formatter, converter, transformer, adapter, or extension responsibility. Do not create catch-all
`Utils.kt`, `Extensions.kt`, or `Reducers.kt` files. Do not create artificial operations merely
to populate these folders.

For example, `CatalogItemDto.toCatalogItem()` crosses the wire-to-application boundary
and belongs in `data/remote/mappers/` even if the body is short. A local query update in
`setState { copy(...) }` stays in the ViewModel. A named operation that derives matching rows
is a transformer if it has an independent contract. Checking whether a query is valid is
validation, formatting a result label is formatting, and extension syntax changes none of
those responsibilities.

## Extensions and constants

- Classify an extension by what it does, not its `Receiver.method()` syntax. An extension that
  converts representations is a mapper; one that produces display text is a formatter; one
  that performs a state transition is a reducer. Use those packages instead of `extensions/`.
- Every extension whose responsibility is not already classified as a mapper, formatter, reducer,
  validator, or other category gets its own descriptive file in `data/extensions/`,
  `domain/extensions/` (when domain exists), or `ui/extensions/`. This applies even if it has
  one caller. More specialized source ownership can justify a narrower package.
- A truly layer-neutral extension shared across layers may live in the module's
  `utils/extensions/`. It must not create an invalid layer dependency. Move it to a core
  module only when independent modules actually need the same operation.
- Constants are organized in **each owning layer**: `data/constants/`,
  `domain/constants/` (only with a justified domain layer), and `ui/constants/`.
  Place layer-owned constants in concern-specific files. Put ViewModel operation names in
  `ui/constants/`, for example `CatalogOperationNames.kt`. Keep GA4 event names, parameters
  and builders together in `ui/analytics/` or their shared analytics contract. Inline simple
  single-use values when their meaning is clear.
  `utils/constants/` is reserved for genuinely layer-neutral constants needed across layers.
- Stable identifiers are contracts, not arbitrary constants: analytics names and parameters
  belong to the owning analytics contract, navigation keys to navigation, persisted keys to
  data/local, and public product IDs to their owning module. Do not move them merely to centralize.
- Avoid duplicating the same constant across layers. If two layers need a value, determine its
  true owner and dependency direction before relocating it.

## Canonical feature structure

This tree shows available responsibilities. Use the placement table for specialized packages such
as `data/remote/models/` and `data/remote/mappers/`. Add a package when its first file needs it.

```text
feature/
├── data/
│   ├── constants/
│   ├── contracts/
│   ├── exceptions/
│   ├── local/
│   ├── remote/
│   ├── models/
│   ├── mappers/
│   ├── transformers/
│   ├── repositories/
│   ├── validators/
│   └── extensions/
├── domain/
│   ├── models/
│   └── usecases/
├── di/
│   └── XModule.kt
└── ui/
    ├── XScreen.kt
    ├── XScreenPreview.kt
    ├── XViewModel.kt
    ├── analytics/
    ├── constants/
    ├── contracts/
    ├── extensions/
    ├── features/
    ├── formatters/
    ├── mappers/
    ├── models/
    ├── navigation/
    ├── parsers/
    ├── providers/
    ├── reducers/
    ├── search/
    ├── states/
    ├── transformers/
    ├── validators/
    └── views/
```

## Kotlin declaration defaults

| Responsibility | Filename | Declaration |
| --- | --- | --- |
| Screen and stateless content | `XScreen.kt` | Composable functions |
| Preview | `XScreenPreview.kt` or `XViewPreview.kt` | Private composable functions |
| ViewModel | `XViewModel.kt` | Class |
| UI state | `XUiState.kt` | Immutable data class |
| Events | `XEvent.kt` | Sealed interface |
| Related constants | `XConstants.kt` | Object with named constants |
| Operation names | `XOperationNames.kt` | Object with stable operation identifiers |
| Preference analytics keys | `XPreferenceKeys.kt` | Object in `ui/analytics/` |
| Repository | `XRepository.kt` | Interface |
| Repository implementation | `DefaultXRepository.kt` | Class |
| Mapper | `XMappers.kt` | Functions |
| Reducer | `XReducer.kt` | Pure functions |
| Transformer | `XTransformer.kt` | Functions |
| Formatter | `XFormatter.kt` | Functions |
| Parser | `XParser.kt` | Functions or a stateful class when parsing needs state |
| Validator | `XValidator.kt` | Functions returning a validation result |
| Settings search | `XSettingsSearchProvider.kt` | Typed property or factory function |
| Navigation registration | `XNavigation.kt` | Graph builder extension functions |
| DI | `XModule.kt` | Typed module property |
| Use case | `XUseCase.kt` | Class |

These defaults apply unless the responsibility requires a different declaration. Group related
constants in a named object. Keep independent mappers, formatters and extensions as functions.

## Naming

- For Compose previews, place the preview in a sibling file named after the composable followed
  by `Preview.kt`. For example, `ui/CatalogScreenPreview.kt` sits beside `ui/CatalogScreen.kt`,
  and `ui/views/CatalogRowPreview.kt` sits beside `ui/views/CatalogRow.kt`. The preview normally
  renders the stateless content.
- Keep a feature's endpoint configuration in its owning data package, and feature-specific
  extensions in the layer that uses them. Reserve module-level utils for code genuinely shared
  across layers. Use names that identify what the values or functions do.


- One top-level class or interface per file, named after it. Private helpers may share the file.
- An interface and its implementation are two files side by side. Name the implementation after
  how it differs, such as `OfflineFirstNewsRepository` or `AndroidTorchDataSource`, or `Default`
  when nothing differs.
- Data sources are named `<Thing><Local|Remote>DataSource`, or after the platform source they wrap.
- Screens, ViewModels, states and events follow `android-ui-layer`: `XScreen`, `XViewModel`,
  `XUiState`, `XEvent`.
- Use descriptive responsibility names for each file from the start, such as
  `ArticleUiReducer.kt`, `DateDisplayFormatter.kt`, `EmailValidator.kt`, or
  `RoomConverters.kt`. Do not combine unrelated responsibilities in a generic
  `Extensions.kt` file. These file-placement conventions are not yet automatically enforced
  by `checkModuleBoundaries`.

## Nested UI features

Prefer a standalone feature module when the functionality is reusable, is a distinct product
capability, has or could have its own navigation destination, owns data or integrations beyond its
parent, or would remain meaningful without the parent screen.

Use `ui/features/<name>/` only when all of these are true:

1. The parent feature exclusively owns the UI.
2. It is a registered destination or has its own ViewModel, lifecycle, or equivalent state holder.
3. It is not useful enough to justify a standalone module.
4. A separate module would add dependency or module overhead for nothing.

Keep a nested destination's entry point and screen content in the same file. Put its destination-only
analytics in its own `analytics/` package. Child views belong in `views/` when they own a separate
rendering responsibility. Stateful UI driven by the main screen's ViewModel stays in the main
`ui/` structure. Size or visual complexity alone never justifies `ui/features/`.

## Placement decision

1. Is it reusable or independently meaningful? Prefer a dedicated feature or core module.
2. Which layer owns its responsibility? Use the placement table.
3. Is it a mapper, reducer, transformer, validator, formatter, converter, adapter, extension,
   or layer-owned constant with independent ownership? Apply its package and file rule. Keep
   local implementation details with their owner.
4. Is it a parent-owned destination or independently stateful surface? `ui/features/<name>/` may fit.
5. Is ownership still unclear? Leave it in place and defer to the relevant architecture skill.

## Review

Report each problem with its file, the rule, and the smallest fix. Check for:

1. files in the wrong layer, package or module
2. a `domain/` package without a use case
3. package names outside the vocabulary
4. mappers named after a layer, or placed away from their boundary
5. reducers mixing state transitions with I/O or navigation; transformations or validators in
   the wrong layer; converters mistaken for mappers
6. extensions or constants outside their layer-owned, concern-specific files
7. implementation types leaking across module ownership boundaries
8. nested `ui/features/` that should be standalone modules
9. folders with no real responsibility

Prefer moving or renaming over rewriting behavior. Do not exempt a misplaced responsibility
because it is small, private, or currently has one caller. A move of a published package is a
breaking change: record it in the project's migration notes.
