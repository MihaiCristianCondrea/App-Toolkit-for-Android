# Toolkit Tiles screen

The Toolkit Tiles screen is a top-level destination in the host app. It presents a Material 3
catalog of Quick Settings tile ideas inspired by the preview-only `resources/toolkit-tiles` project
without depending on that preview source at runtime.

## Architecture

- Domain models live under `app/tiles/domain/model` and expose resource IDs for UI text.
- `GetToolkitTilesUseCase` builds the curated catalog as a `Flow` so the ViewModel follows the same
  flow-based loading contract as the rest of App Toolkit.
- `ToolkitTilesViewModel` extends `LoggedScreenViewModel`, dispatches initialization through
  `ToolkitTilesEvent.Initialize`, and exposes `UiStateScreen<ToolkitTilesUiState>`.
- `ToolkitTilesRoute` handles Android platform add-tile requests after receiving
  `ToolkitTilesAction.RequestAddTile`; the ViewModel never receives an Android `Context`.

## Runtime tile services

The first usable Quick Settings services are intentionally small and self-contained:

- Battery: refreshes the current battery percentage.
- Clipboard Cleaner: clears the clipboard on tap.
- Coin Flip: shows heads or tails.
- Counter: increments an in-memory count while the tile service remains alive.
- Dice Roll: rolls a six-sided die.

Tiles that require sensors, accessibility, notification-policy access, or privileged settings are
shown as **Needs setup** or **Unsupported** until their permissions and platform behavior are added.
