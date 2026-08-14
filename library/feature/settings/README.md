# `:library:feature:settings` Logic Graph

## Purpose

Composes the toolkit's root, general, display, advanced, and usage/diagnostics settings experiences
and exposes host provider extension points.

## Owns

- Settings screen/activity/ViewModel and configurable category/preference models.
- `DisplaySettingsViewModel`, which owns display-preference observation and persistence while the
  display list and selection dialogs remain presentation-only.
- `ThemeSettingsViewModel`, which owns theme preference observation and mutations for the dedicated
  theme settings surface.
- General settings repository/presentation flow.
- Advanced cache repository and settings flow.
- Usage-and-diagnostics repository/model/presentation flow.
- `SettingsProvider`, display/advanced provider contracts, and default content providers.

## Does not own

- Host-specific settings categories/content, owned by `:sample` provider implementations.
- Consent SDK operations, delegated to `:library:integration:consent`.
- About/help/issue-reporter feature implementations, owned by their modules.
- Theme primitives and persisted storage, owned by core design system/DataStore.

## Depends on

- `:library:core:common`, `:library:core:datastore`, `:library:core:network`, `:library:core:ui`,
  and `:library:navigation` for shared infrastructure.
- [`:library:integration:consent`](../../integration/consent/README.md) for diagnostics consent
  updates.
- `:library:feature:about`, `:library:feature:help`, and `:library:feature:issuereporter` to compose
  related settings destinations/content.

## Used by

- `:sample`, `:library:apptoolkit`, `:library:feature:onboarding`, and
  `:library:feature:permissions`.

## Flow chart

```mermaid
flowchart TD
    Provider[Host SettingsProvider] --> Root[SettingsScreen]
    Root --> General[General settings]
    Root --> Advanced[Advanced/cache settings]
    Root --> Diagnostics[Usage and diagnostics]
    General --> Repos[Feature repositories / preference state holders]
    Diagnostics --> Consent[ConsentRepository]
    Repos --> Store[CommonDataStore / Android cache]
```

## Public contracts

- Settings/provider interfaces, settings category/preference/config models, repository contracts,
  and screen/ViewModel contracts. Host startup dialogs receive the current route and return only a
  confirmed selection; the toolkit state holder performs persistence.

## Internal implementations

- Default content providers, cache operations, DataStore-backed repositories, settings lists, and
  consent-section UI.

## Current risks

This module directly depends on three other feature modules and is itself a dependency of onboarding
and permissions. The resulting feature-level coupling makes route/provider changes likely to ripple
across the graph.
