# `:library:core:datastore` Logic Graph

## Purpose

Provides the toolkit's shared Preferences DataStore implementation and preference-source contracts
used by onboarding, consent, ads, diagnostics, review, and theming.

## Owns

- `CommonDataStore` and DataStore creation/access extensions.
- Preference data-source contracts for onboarding, consent, and usage diagnostics.
- Persisted theme, review, ads, and consent-related values.
- `AdsCoreManager` and the Koin DataStore module.

## Does not own

- Feature business decisions about consent, reviews, onboarding, or diagnostics.
- Compose theme rendering, owned by `:library:core:designsystem` and `:library:core:ui`.

## Depends on

- [`:library:core:common`](../common/README.md) for shared contracts, preference constants, and
  common models.

## Used by

- `:library:apptoolkit` for host DI assembly.
- `:library:core:designsystem` for persisted theme state.
- `:library:feature:about`, `:library:feature:help`, `:library:feature:onboarding`, and
  `:library:feature:settings`.
- `:library:integration:ads`, `:library:integration:consent`, and `:library:integration:review`.

## Flow chart

```mermaid
flowchart LR
    Feature[Feature repository] --> Contract[Preference data-source contract]
    Contract --> Store[CommonDataStore]
    Store --> Preferences[Preferences DataStore]
    Preferences --> Flow[Typed Flow values]
```

## Public contracts

- `CommonDataStore`, its preference flows/setters, preference-source interfaces, and
  `dataStoreModule`.

## Internal implementations

- DataStore key access, serialization-free preference mapping, and ads initialization coordination.

## Current risks

`CommonDataStore` serves several unrelated features directly, so changes to keys or default values
are compatibility-sensitive across many modules.

## Migration notes

### Ads initialization must share the UI preference source

An earlier initialization path sampled the ads preference with a debug-dependent default while the
Compose ad surfaces used `true`. In a debug build with no stored preference, `AdsCoreManager`
skipped
`MobileAds.initialize` while native/banner views attempted SDK requests. Those SDK entry points can
throw synchronously, including from composition. A removed host-side unconditional initialization
had previously masked the disagreement.

Preserve these invariants:

- `CommonDataStore.adsEnabledFlow`, including its host-configured `defaultAdsEnabled`, is the single
  source of truth for both `AdsCoreManager` and ad UI. Callers must not supply local defaults.
- `AdsCoreManager` observes the flow instead of sampling it once, so enabling ads at runtime starts
  initialization without a process restart.
- Initialization is idempotent and mutex-protected, uses the validated host-manifest AdMob app ID,
  and is skipped when no valid ID exists.
- `AdsSdkState.isReady` is published after initialization so UI can delay requests until the SDK is
  usable.
- `AdsSdkInitializer` remains the test seam around the SDK's non-mock-friendly initialization API.

The JUnit 5 `AdsCoreManagerInitializationTest` is the executable regression suite for these rules.
Do not rely on the legacy JUnit 4 `TestAdsCoreManager`: the module uses the JUnit Platform without a
Vintage engine, so that class compiles but is not executed.
