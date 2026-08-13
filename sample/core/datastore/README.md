# `:sample:core:datastore` Logic Graph

## Purpose

The host's preference storage: everything the sample persists beyond what the toolkit already stores.

## Owns

- `DatastoreInterface` and its implementation, covering the components-showcase unlock flag,
  favorites, and the host's startup destination.

## Does not own

- Toolkit preferences (ads enabled, consent, onboarding completion), owned by
  [`:library:core:datastore`](../../../library/core/datastore/README.md).
- Any decision about what the stored values mean; that belongs to the repositories that read them.

## Depends on

- [`:library:core:datastore`](../../../library/core/datastore/README.md) for `CommonDataStore`.
- [`:library:core:ui`](../../../library/core/ui/README.md) for `StableNavKey` and the startup-destination extension.

## Used by

- `:sample:feature:apps`, `:sample:feature:components`, `:sample:feature:home`,
  `:sample:feature:settings`, `:sample:app`.

## Flow chart

```mermaid
flowchart LR
    Repos[Host repositories] --> Interface[DatastoreInterface]
    Interface --> Store[DataStore preferences]
    Interface --> Common[CommonDataStore]
```

## Public contracts

- `DatastoreInterface`.

## Internal implementations

- Preference keys and the DataStore instance.

## Current risks

`DataStoreTest` is `@Disabled`. It needs Robolectric's runner for `ApplicationProvider`, and this
build runs the JUnit platform with no vintage engine, so `@RunWith` is never honoured — the test has
never executed. It was silently skipped while the sample was one module and only surfaced when it
became this module's only test. Re-enabling it needs either a Robolectric JUnit 5 integration or a
constructor that takes the DataStore file path so no Android context is required.
