# `:sample:core:common` Logic Graph

## Purpose

Host-wide constants and error types that carry no UI and no Android state.

## Owns

- Help-screen constants and log tags.
- `AppErrors`, the host's extension of the toolkit `Errors` hierarchy.

## Does not own

- Ad unit ids and placement qualifiers, owned by
  [`:sample:integration:ads`](../../integration/ads/README.md).
- Anything that needs `R`. Error-to-text mapping lives in [`:sample:core:ui`](../ui/README.md),
  because it returns `UiTextHelper` and resolves string resources.

## Depends on

- [`:library:core:network`](../../../library/core/network/README.md) for the `Errors` hierarchy
  `AppErrors` extends.

## Used by

- `:sample:core:ui`, `:sample:feature:apps`, `:sample:app`.

## Flow chart

```mermaid
flowchart TD
    Help[HelpConstants] --> Features[Apps, tiles, and app composition]
    Logs[LogTags] --> Features
    Network[Toolkit Errors hierarchy] --> Errors[AppErrors]
    Errors --> Mapper[Feature-owned UI text mapper]
```

## Architectural decisions

- This module contains only cross-feature values that need neither Android state nor resources.
- Resource-backed mappings stay with the consuming UI because moving `R` here would turn a
  constants/error module into a presentation dependency.
- Fixed tuning values are source constants; build-dependent identities use each module's own
  generated `BuildConfig`.

## Public contracts

- `HelpConstants`, `LogTags`, `AppErrors`.

## Internal implementations

- None. Every declaration here is a constant or a data type.

## Current risks

Ad unit IDs and placement qualifiers moved to
[`:sample:integration:ads`](../../integration/ads/README.md), which owns everything ads-related.
That module reads its own `BuildConfig.DEBUG` to pick between debug and release ad
units. That is per-module but tracks the same build type, so it stays correct; a module compiled in
isolation against a different build type would not.

## Migration notes

`APPS_LIST_AD_FREQUENCY` was an application-module `buildConfigField`. No library module can read
the
application's generated class, and the value is fixed tuning rather than a build input, so it became
a constant here when `:sample:feature:apps` was split out.
