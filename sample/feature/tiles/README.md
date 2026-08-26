# `:sample:feature:tiles` Logic Graph

## Purpose

Quick tools: the in-app tool catalogue and the Quick Settings tile services behind it.

## Owns

- `ToolkitTilesRepository`, which owns the source-neutral catalogue and coordinates current tile
  status with persisted category expansion preferences.
- Local data sources for preferences, Quick Settings, sensors/display, ringer mode and music
  search, haptics, caffeine-service control, and torch access.
- `SensorRepository`, `BreathingRepository`, `CaffeineRepository`, `SystemRepository`,
  `TorchRepository`, `MorseRepository`, and `SosRepository`, which remain the data-layer entry
  points and own coordination or runtime state.
- UI catalogue models and mappers, the screen and dedicated tool ViewModels, tool composables,
  `toolkitTilesEntryBuilder`, and the Quick Settings services.
- Localized Quick Tools strings and plurals.
- Feature-owned manifest permissions for haptics, wake locks, flashlight access, and the caffeine
  foreground service.

## Does not own

- The route key it registers against, owned by
  [`:sample:core:navigation`](../../core/navigation/README.md).
- Native ad rendering, owned by [`:library:core:ui`](../../../library/core/ui/README.md); this
  module supplies only the quick-tools card styling.
- How often native ads appear, owned by the host's `SampleAdsPolicy` in
  [`:sample:core:common`](../../core/common/README.md); this module decides only where they go in
  the catalogue.

## Depends on

- `:sample:core:navigation`, `:sample:core:common`, `:sample:core:ui`.
- [`:library:apptoolkit`](../../../library/apptoolkit/README.md) for ad slots and screen contracts.

## Used by

- `:sample:app`, for DI and the navigation graph.

## Flow chart

```mermaid
flowchart TD
    Screen[ToolkitTilesScreen] -->|catalog events| VM[ToolkitTilesViewModel]
    VM --> Tiles[ToolkitTilesRepository]
    Tiles --> Catalogue[Source-neutral catalog]
    Tiles --> Preferences[ToolkitTilesPreferencesDataSource]
    Preferences --> Store[Preferences DataStore]
    Tiles --> QS[QuickSettingsTilesLocalDataSource]
    Tiles --> TorchCapability[TorchRepository capability Flow]
    TorchCapability --> Filter{Flashlight available?}
    Catalogue --> Filter
    Filter -->|no| Remove[Remove SOS / Morse / dimmer]
    Filter -->|yes| Published[Published catalog state]
    Remove --> Published
    Published --> VM
    VM --> UiMap[Resource and artwork UI mappers]
    UiMap --> Screen
    Screen --> ToolVMs[Dedicated stateful tool ViewModels]
    ToolVMs --> Repositories[Sensor / breathing / caffeine / system / torch repositories]
    Repositories --> Sources[Feature-local Android data sources]
    Sos[SosRepository] --> Morse[MorseRepository single playback job]
    Morse --> Torch[TorchRepository shared state]
    Services[Quick Settings and caffeine services] --> Repositories
```

## Architectural decisions

- Repositories are the only entry points to platform-backed sources; ViewModels and framework
  services do not call sensor, torch, haptics, or settings sources directly.
- The catalog is source-neutral. Resource IDs, icons, descriptions, and ad styling are mapped in the
  UI layer after availability filtering.
- Torch state is shared because in-app tools and system-created services can operate concurrently.
  One Morse playback job serializes patterned output, and SOS delegates to it.
- Only stateful/platform-backed tools receive dedicated ViewModels; stateless decision tools remain
  local UI behavior rather than creating pass-through layers.

## Public contracts

- `ToolkitTilesRepository`, `TorchRepository`, `MorseRepository`, `ToolkitTilesViewModel`, the
  dedicated tool ViewModels, `toolkitTilesEntryBuilder`, and the source-neutral tile models.

## Internal implementations

- Camera2 torch discovery/control, sensor sampling, haptics, caffeine-service control, Quick
  Settings inspection, UI catalogue mapping, and tool composables.

## Source ownership and risks

Android platform APIs are isolated in `data/local` sources. ViewModels and services use
repositories as the data-layer entry points, while source interfaces provide deterministic test
boundaries around external resources.

`ToolkitTilesRepository` removes SOS, Morse and Flash Dimmer from the published catalogue when the
shared torch capability reports that no flashlight is available. Catalogue resource IDs and
artwork identifiers live in UI models and mappers; the repository publishes only source-neutral
IDs, status, behavior kind, and platform request keys.

`TorchRepository` has a contract because Morse/SOS, the in-app dimmer and a system-created Quick
Settings service share one observable source of truth. `MorseRepository` owns the only patterned
torch playback job; SOS delegates its fixed message to it so custom Morse, SOS and steady
brightness controls cannot compete. Torch strength is exposed on Android 13+ when hardware reports
multiple levels; older and single-level devices fall back to a binary toggle.

## Migration notes

The catalogue and status pass formerly lived in pass-through use cases. They remain repository
work, while all Android resource and artwork mapping now happens in the UI layer. Stateful or
platform-backed bottom-sheet tools retain dedicated ViewModels; the catalogue ViewModel owns only
catalogue, filter, expansion, ad and add/setup state.
