# Home Screen Widget (Glance)

This document describes the current App Toolkit app widget implementation in
`app/widgets/apps` and how to evolve it safely.

## Current behavior

- The widget is implemented with **Jetpack Glance** (`AppIconsWidget`) and registered via
  `AppIconsWidgetReceiver`.
- It loads developer app entries from `FetchDeveloperAppsUseCase` and renders a scrollable list of app rows.
- Tapping a row executes `OpenAppOrStoreAction`, which launches the installed app or falls back to its Play Store listing.
- The widget uses responsive breakpoints (`120dp`, `180dp`, `250dp`) to adapt density:
  - small: compact list
  - medium/large: includes a header and more visible rows

## Reliability and UX safeguards

- `provideGlance` loads app data on `Dispatchers.IO` before composition, to keep main-thread work minimal.
- `onCompositionError` falls back to `widget_app_icons_error.xml`, with a retry button that triggers a full widget refresh.

## Update model

- The widget remains passive and stateless; source-of-truth data stays in domain/data layers.
- Refreshes are triggered through Glance update APIs (`updateAll`) from receiver callbacks.
- Avoid frequent periodic updates; update opportunistically after data changes or explicit user actions.

## Extension guidelines

When extending this widget:

1. Keep business/data orchestration outside composables.
2. Keep rows lightweight; avoid allocation-heavy operations in composition.
3. Prefer predefined breakpoints over fully exact re-layouts for smoother resize transitions.
4. Keep action callbacks short; offload long work to workers.
5. Ensure non-composition failures still provide actionable UI through error fallback paths.
