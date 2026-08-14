# `:library:feature:about` Logic Graph

## Purpose

Owns AppToolkit about, licenses, changelog, privacy, and shared main-navigation surfaces used by
several settings/help flows.

## Owns

- About information/copy-device-info domain and presentation flows.
- Changelog retrieval/presentation and in-app-update triggering.
- Licenses and library-extras screens.
- Privacy/about provider contracts, typed AppToolkit route keys, bars/rails, and related navigation
  helpers.
- The default repository for hosts that use the four standard drawer entries unchanged.

## Does not own

- Host main screen and host route keys, owned by `:sample`.
- Support, consent, review, and update implementations, owned by their feature/integration modules.
- Root Navigation 3 entry assembly, owned by `:library:apptoolkit`.

## Depends on

- `:library:core:common`, `:library:core:datastore`, `:library:core:network`, `:library:core:ui`,
  and `:library:navigation` for shared state, persistence, HTTP, Compose, and navigation.
- `:library:integration:consent`, `:library:integration:review`, and `:library:integration:update`
  for privacy and Play flows.
- [`:library:feature:support`](../support/README.md) for support navigation/content integration.
- [`:library:navigation`](../../navigation/README.md) for drawer models, routes, and its host-facing
  repository contract.

## Used by

- `:sample`, `:library:apptoolkit`, `:library:feature:help`, and `:library:feature:settings`.

## Flow chart

```mermaid
flowchart TD
    AboutScreen --> AboutVM[AboutViewModel]
    AboutVM --> AboutRepo[AboutRepository]
    AboutVM --> CopyUC[CopyDeviceInfoUseCase]
    ChangelogUI[Changelog dialog] --> ChangelogVM
    ChangelogVM --> ChangelogRepo[ChangelogRepository]
    ChangelogVM --> Update[In-app update integration]
    Nav[Shared route keys and bars] --> Consumers[Host/help/settings]
```

## Public contracts

- About/privacy provider interfaces, typed route keys, `DefaultNavigationRepository`,
  about/changelog repositories/models, `CopyDeviceInfoUseCase`, `GetChangelogUseCase`, and
  screen/navigation composables.

## Internal implementations

- Device/build-info mapping, clipboard behavior, changelog HTTP/fallback logic, screen composition,
  and update-host creation.

## Current risks

The module's scope extends beyond “about” into navigation UI, changelog, privacy, licenses, and
updates. Drawer contracts and string routes have moved to `:library:navigation`, but the remaining
presentation surface is still a broad and change-sensitive feature boundary.
