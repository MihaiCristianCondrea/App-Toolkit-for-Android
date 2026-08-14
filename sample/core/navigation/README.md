# `:sample:core:navigation` Logic Graph

## Purpose

The route vocabulary and entry-builder context that let a feature declare a destination without
knowing which shell renders it.

## Owns

- `AppNavKey` and the host route keys (`AppsListRoute`, `ToolkitTilesRoute`, `ComponentsRoute`).
- `AppNavigationEntryContext` and `RandomAppHandler`, the parameters every feature entry builder takes.
- `NavigationManager` and `MainNavigationDefaults` (bottom-bar items, FAB-supported routes).

## Does not own

- The list of entry builders. That names every feature, so it lives in `:sample:app`.
- Any destination content, owned by the feature modules.

## Depends on

- [`:sample:core:ui`](../ui/README.md) for the strings and icons the bottom bar uses.
- [`:library:navigation`](../../../library/navigation/README.md) and
  [`:library:navigation`](../../../library/navigation/README.md) for `StableNavKey` and destination
  types, plus [`:library:core:ui`](../../../library/core/ui/README.md) for shared UI state.

## Used by

- `:sample:feature:apps`, `:sample:feature:tiles`, `:sample:feature:components`,
  `:sample:feature:home`, `:sample:app`.

## Flow chart

```mermaid
flowchart TD
    Feature[Feature entry builder] --> Context[AppNavigationEntryContext]
    Feature --> Keys[Route keys]
    App[":sample:app"] --> Aggregate[appNavigationEntryBuilders]
    Aggregate --> Feature
    Shell[":sample:feature:home"] --> Keys
```

## Public contracts

- Route keys, `AppNavigationEntryContext`, `RandomAppHandler`, `NavigationManager`,
  `MainNavigationDefaults`.

## Internal implementations

- None; this module is contracts and constants.

## Current risks

`MainNavigationDefaults` hardcodes the bottom-bar item order. Adding a top-level destination means
editing this module as well as the feature that provides it.

## Migration notes

`AppNavigationEntryContext` was declared alongside `appNavigationEntryBuilders` in the main package.
Splitting them is what allows features to be leaves: the context is a parameter every feature needs,
while the builder list names them all. Keeping them together would have made each feature depend on
the module that wires all the others.
