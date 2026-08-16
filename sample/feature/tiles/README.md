# `:sample:feature:tiles` Logic Graph

## Purpose

Quick tools: the in-app tool catalogue and the Quick Settings tile services behind it.

## Owns

- `ToolkitTilesRepository` and `DefaultToolkitTilesRepository`, which own the tile catalogue and
  read
  which tiles are added to Quick Settings.
- The platform-wrapping repositories: `SensorRepository`, `BreathingRepository`,
  `CaffeineRepository`, `SystemRepository`, `TorchRepository`, `MorseRepository`,
  `SosRepository`.
- `ToolkitTilesViewModel`, dedicated stateful-tool ViewModels, the tool composables, the bottom
  sheet, and `toolkitTilesEntryBuilder`.
- The Quick Settings tile services, including Flash Dimmer, and `CaffeineService`.

## Does not own

- The route key it registers against, owned by [
  `:sample:core:navigation`](../../core/navigation/README.md).
- Native ad rendering, owned by [`:library:core:ui`](../../../library/core/ui/README.md); this
  module
  supplies only the quick-tools card styling.

## Depends on

- `:sample:core:navigation`, `:sample:core:common`, `:sample:core:ui`.
- [`:library:apptoolkit`](../../../library/apptoolkit/README.md) for ad slots and screen contracts.

## Used by

- `:sample:app`, for DI and the navigation graph.

## Flow chart

```mermaid
flowchart TD
    Screen[ToolkitTilesScreen] --> VM[ToolkitTilesViewModel]
    VM --> Tiles[ToolkitTilesRepository]
    Tiles --> Catalogue[Tile catalogue]
    Tiles --> QS[Quick Settings state]
    ToolVMs[Dedicated tool ViewModels] --> Platform[Sensor / caffeine / system / torch repositories]
    Screen --> ToolVMs
    Sos[SOS repository] --> Morse[Morse repository]
    Morse --> Torch
    Dimmer[Flash Dimmer tile service] --> Torch
    Services[Tile services] --> Platform
```

## Public contracts

- `ToolkitTilesRepository`, `TorchRepository`, `MorseRepository`, `ToolkitTilesViewModel`, the dedicated tool
  ViewModels, `toolkitTilesEntryBuilder`, and the tile models.

## Internal implementations

- Camera2 torch discovery/control, sensor sampling, the caffeine foreground service, ringer-mode
  cycling, and tool composables.

## Current risks

The platform repositories stay concrete classes with no interface. Each wraps one Android source and
has no alternate implementation, so an interface would add substitution nobody uses — but it also
means those paths cannot be faked in a unit test.

`ToolkitTilesRepository` is the exception and has a contract, because it serves the catalogue as
well
as reading the platform. It removes SOS, Morse and Flash Dimmer from the published catalogue when
the shared torch capability reports that no flashlight is available.

`TorchRepository` also has a contract because Morse/SOS, the in-app dimmer and a system-created
Quick Settings service share one observable source of truth. `MorseRepository` owns the only
patterned torch playback job; SOS delegates its fixed message to it so custom Morse, SOS and steady
brightness controls cannot compete. Torch strength is exposed on Android 13+ when hardware reports
multiple levels; older and single-level devices fall back to a binary toggle. Closing any torch
bottom sheet stops patterned playback and turns the light off.

## Migration notes

The catalogue was `GetToolkitTilesUseCase` and the status pass was `SyncToolkitTileStatusesUseCase`.
Neither was a use case — one was a hardcoded data set in the domain layer, the other read the
repository and mapped over the result — so both moved into the repository, where `tileCategories()`
now applies statuses itself.

`CaffeineService` used to build its notification intent from `MainActivity::class.java`. It resolves
the launcher activity through the package manager instead, so a quick-tool service does not depend
on
the application module.

Tool runtime state used to be aggregated in `ToolkitTilesViewModel`. Each stateful or
platform-backed bottom-sheet tool now owns a dedicated ViewModel; the catalogue ViewModel owns only
catalogue, filter, expansion, ad and add/setup state. Animation-only state remains in Compose.
