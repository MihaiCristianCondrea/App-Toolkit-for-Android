# Android Layer Rules

Use this reference when reviewing Android projects that follow a UI -> Domain -> Data architecture.

The goal is practical consistency, not theoretical purity.

Prefer small, mechanical, behavior-preserving improvements.

Do not redesign features unless explicitly asked.

Do not rewrite full flows when a small fix is enough.

## Dependency direction

Preferred direction:

    UI -> Domain -> Data abstraction
    Data implementation -> Domain interfaces/models

The data implementation may depend on domain interfaces and domain models.

The domain layer must not depend on data implementation details.

The UI layer should not know data implementation details.

## Expected feature structure

A feature should generally follow this shape:

    feature-name/
    ├── ui/
    ├── domain/
    └── data/

Some features may not need all folders immediately, but when a responsibility exists, it should live
in the correct layer.

## UI layer

The UI layer owns presentation.

Expected structure:

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

The exact `ScreenName.kt` and `ScreenNameViewModel.kt` names may vary by feature, but naming should
be consistent inside the project.

### UI can depend on

- Domain models
- Domain use cases
- UI models
- UI mappers
- UI state
- UI contracts
- Compose APIs
- Platform UI APIs
- Navigation types owned by the UI layer

### UI must not depend on

- DTOs
- Room entities
- DataStore models
- Repository implementation classes
- Local data source implementation classes
- Remote data source implementation classes
- Database APIs directly
- Remote API models directly
- File scanning APIs directly
- Platform/system APIs directly when they represent business or data behavior

### UI owns

- Render-ready models
- Screen state
- User actions
- Screen events
- One-off UI effects
- Composables
- Navigation routes
- Presentation formatting
- Domain-to-UI mapping

### UI does not own

- Business rules
- Cleanup decisions
- Cleanup execution
- Scan policy
- Storage policy
- Repository implementation
- Local/remote data source implementation
- Filesystem access
- Database access
- API access

### UI model rules

UI models should be render-ready.

Good UI model fields:

    title: String
    subtitle: String
    formattedSize: String
    formattedDate: String
    progressPercent: Float
    isActionEnabled: Boolean
    icon: UiIcon
    items: List<ItemUiModel>

Suspicious UI model fields:

    file: File
    uri: PlatformUri
    dto: ApiResponse
    entity: RoomEntity
    rawCursor: Cursor
    cleanupPolicy: BusinessPolicy
    repository: RepositoryImpl

Raw values are allowed in UI models only when they are genuinely needed for rendering or UI-only
behavior.

For example, `rawBytes: Long` may be acceptable if the UI needs to draw a chart, but repeated
formatting from raw bytes should usually live in a UI mapper.

## Domain layer

The domain layer owns business decisions.

Expected structure:

    domain/
    ├── models/
    ├── mappers/
    ├── interfaces/
    ├── usecases/
    └── utils/

### Domain can depend on

- Domain models
- Domain interfaces
- Domain use cases
- Domain mappers
- Kotlin common abstractions
- Coroutines and Flow, when part of the project architecture
- Value classes and sealed types that express business meaning

### Domain must not depend on

- Android framework classes
- Compose
- Room
- DataStore
- Retrofit DTOs
- Ktor DTOs
- SQLDelight entities
- Platform-specific filesystem APIs
- UI state classes
- UI models
- Repository implementation classes
- Local or remote data source implementation classes

### Domain owns

- Business models
- Business rules
- Use cases
- Repository/service interfaces
- Validation rules
- Cleanup decisions
- Scan rules
- Safety policy
- Reusable business transformations

### Domain does not own

- UI formatting
- UI state
- DTOs
- Entities
- Platform models
- Database implementation
- API implementation
- Filesystem implementation
- Concrete Android service calls

### Good domain model examples

    CleanableItem
    StorageCategory
    ScanResult
    CleanupCandidate
    PermissionState
    SystemHealthStatus
    CleanupPlan
    CleanupResult

### Suspicious domain model examples

    CleanableItemDto
    CleanableItemEntity
    CleanableItemUiModel
    AndroidStorageVolume
    RoomScanRecord
    DataStoreCleanupPreferences

## Data layer

The data layer owns local, remote, platform, persistence, and implementation details.

Expected structure:

    data/
    ├── models/
    ├── mappers/
    ├── local/
    ├── remote/
    └── utils/

The `local/` and `remote/` folders may each contain their own:

    models/
    mappers/
    interfaces/
    utils/

### Data can depend on

- Domain interfaces
- Domain models
- Local data sources
- Remote data sources
- System/platform APIs
- DTOs
- Entities
- Platform models
- Data mappers
- Persistence libraries
- Network libraries

### Data must not expose upward

- DTOs
- Entities
- DataStore preference objects
- Android framework objects
- File handles
- Raw cursors
- API response wrappers
- Platform-specific implementation classes

### Data owns

- Room access
- DataStore access
- Filesystem access
- System API access
- Remote API access
- DTOs
- Entities
- Platform models
- Repository implementations
- Data source implementations
- Low-level IO behavior
- Actual delete/cleanup operations

## Mapper rules

Preferred mapping chain:

    Remote DTO / Local Entity / Platform Model
    -> Domain Model
    -> UI Model

Avoid:

    DTO -> UI
    Entity -> UI
    Platform Model -> UI
    DTO -> Domain consumer without mapping
    Entity -> Domain consumer without mapping
    Platform Model -> Domain consumer without abstraction

### Mapper locations

Use:

    data/mappers/

For mappings shared across local and remote data.

Use:

    data/local/mappers/

For local entities, local database models, local preference models, and local platform models.

Use:

    data/remote/mappers/

For DTOs and API responses.

Use:

    domain/mappers/

Only for mapping between domain concepts.

Use:

    ui/mappers/

For mapping domain models to render-ready UI models.

## Compose-specific rules

### Compose UI should

- Receive render-ready state
- Emit actions/events upward
- Keep business decisions outside Composables
- Keep expensive work outside recomposition
- Use stable and intentional `LaunchedEffect` keys
- Keep reusable UI pieces under `views/`
- Keep screen state simple and presentation-oriented
- Use UI mappers for render formatting when needed

### Compose UI should not

- Read files directly
- Call cleanup/delete APIs directly
- Call repositories directly
- Perform business filtering
- Import DTO/entity classes
- Store persistence entities in UI state
- Duplicate logic that already exists in a use case
- Perform expensive mapping repeatedly during recomposition

### Preferred Compose flow

    Composable -> action
    ViewModel -> use case
    Use case -> domain interface
    Data implementation -> platform/API/storage
    Result -> domain model
    UI mapper -> UI model
    Composable -> render

## Performance red flags

Look for:

- Heavy filesystem scans triggered from UI
- System APIs called repeatedly from Composables
- Large lists remapped on every recomposition
- Flows collected multiple times without need
- State updated too broadly
- Cleanup progress causing entire screen recomposition when only one item changes
- Repositories doing repeated work that should be cached or scoped
- Use cases doing IO on the wrong dispatcher
- Missing cancellation for long-running scans
- Main-thread filesystem or database work

Do not add caching blindly.

First understand:

1. How often the code runs
2. Which layer triggers it
3. Whether the result can become stale
4. Whether cancellation matters
5. Whether the behavior affects business correctness

## Architecture red flags

### UI red flags

- ViewModel imports DTO/entity classes
- ViewModel depends on repository implementation instead of use case/domain interface
- Composable reads files directly
- Composable calls system cleanup APIs
- UI state contains persistence models
- UI decides whether something is safe to delete
- UI duplicates domain logic

### Domain red flags

- Use case imports Android framework classes
- Use case imports DTOs/entities
- Domain model has UI-only formatted fields
- Domain interface returns data-layer models
- Domain depends on implementation classes
- Domain knows Room/DataStore/API details

### Data red flags

- Repository returns DTOs directly to UI/domain
- Repository implementation is injected into UI directly
- Data source is used directly by ViewModel
- Mapper logic is duplicated across local/remote implementation files
- Platform model escapes into UI/domain contracts
- Data layer performs UI formatting

## Severity model

Use this severity model when reporting findings.

### Critical

- Domain depends on UI
- Domain depends on data implementation
- UI directly performs platform cleanup or filesystem deletion
- Data models leak into public domain/UI APIs
- Cleanup safety decisions are made in UI

### High

- Business logic exists in UI
- Use cases know platform details
- DTO/entity mapping is duplicated in several places
- ViewModels depend directly on data source implementations
- Platform-specific code leaks into shared public APIs

### Medium

- Folder naming is inconsistent
- Mappers are in the wrong folder but behavior is correct
- UI state contains mildly non-render-ready values
- Use case boundaries are unclear
- Repeated transformation logic exists but is not dangerous

### Low

- Naming polish
- Small structure cleanup
- Missing package organization that does not affect behavior
- Minor preview or UI consistency improvements

## Safe refactor priority

Prefer this order:

    1. Mechanical folder/name consistency
    2. Clear mapper relocation
    3. UI render-only cleanup
    4. Business logic moved from UI to domain
    5. Platform/data access moved behind interfaces
    6. DTO/entity leak removal
    7. Performance fixes with known call frequency
    8. Larger structural cleanup only when necessary

## Final review principle

If the fix requires guessing, do not change the code.

Leave a clear note explaining:

- What was found
- Why it was not changed
- What context is needed
- Which layer should probably own the behavior