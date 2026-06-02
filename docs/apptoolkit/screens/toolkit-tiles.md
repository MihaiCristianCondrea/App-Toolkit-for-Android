# Quick Tools screen

The Quick Tools screen is a top-level destination in the host app. It presents a Material 3
catalog of quick dialog tools and expanded workflows inspired by the preview-only
`resources/toolkit-tiles` project without depending on that preview source at runtime. Tile-ready
entries show an explicit **Tile** chip so they are distinguishable from app-only tools.

## Architecture

- Domain models live under `app/tiles/domain/model` and expose resource IDs for UI text, quick-versus-expanded tool kind, optional quick-tool dialog identity, and optional tile request keys.
- `GetToolkitTilesUseCase` builds the curated catalog as a `Flow` so the ViewModel follows the same
  flow-based loading contract as the rest of App Toolkit.
- `ToolkitTilesViewModel` extends `LoggedScreenViewModel`, dispatches initialization through
  `ToolkitTilesEvent.Initialize`, and exposes `UiStateScreen<ToolkitTilesUiState>`.
- `ToolkitTilesRoute` handles Android platform add-tile requests after receiving
  `ToolkitTilesAction.RequestAddTile`; the ViewModel never receives an Android `Context`.

## Runtime tile services and quick tools

The first usable Quick Settings services are intentionally small and self-contained. Tool cards
remain tile-ready, and click previews show contained dialogs for tools such as Coin Flip, Compass,
Bubble Level, and Flashlight without making the ViewModel own Android UI objects:

- Battery: refreshes the current battery percentage.
- Clipboard Cleaner: clears the clipboard on tap.
- Coin Flip: shows heads or tails.
- Counter: increments an in-memory count while the tile service remains alive.
- Dice Roll: rolls a six-sided die.

Clicking a Quick Tool card opens a contained dialog. The dialog intentionally ignores outside/back
dismiss and closes only from its explicit close button so tool content can own a clear, deliberate
interaction flow. The Material Colors quick tool uses this pattern to compare the current app color
scheme with Android Material You accent and neutral palettes when the platform exposes them. Tiles
that require sensors, accessibility, notification-policy access, or privileged settings are shown as
**Needs setup** or **Unsupported** until their permissions and platform behavior are added.
