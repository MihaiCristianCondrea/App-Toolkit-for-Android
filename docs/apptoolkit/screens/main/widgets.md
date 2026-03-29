# Home Screen Widget (Glance)

This document describes the App Toolkit home-screen widget implementation in
`app/widgets/apps` and the constraints to preserve when evolving it.

## Current behavior

- The widget is implemented with **Jetpack Glance** (`AppIconsWidget`) and registered through
  `AppIconsWidgetReceiver` in the application manifest.
- It renders a **3x3 app-launch grid** (up to 9 actions) and uses
  `OpenAppOrStoreAction` to open the installed app first, then fallback to the Play Store.
- It uses responsive breakpoints (`120dp`, `180dp`, `250dp`) through `SizeMode.Responsive`.
  - small: icon-first compact grid
  - medium/large: grid plus a lightweight title row for orientation

## Reliability and UX safeguards

- `provideGlance` loads app data on `Dispatchers.IO` before composition, keeping composition work
  lightweight and avoiding main-thread data orchestration.
- Widget loading now caps to visible entries before icon decoding (first 9 only), reducing update
  cost and bitmap churn.
- `onCompositionError` falls back to `widget_app_icons_error.xml`, with a retry button that
  triggers a full `updateAll` refresh path.
- `app_icons_widget_info.xml` defines a dedicated `previewImage` drawable for better picker
  quality on hosts that rely on static previews.

## Update model

- The widget remains passive and stateless; source-of-truth data lives in domain/data layers.
- Refreshes are triggered through Glance update APIs (`updateAll`) from receiver callbacks.
- Prefer opportunistic updates (user interaction / data refresh) over aggressive periodic updates.

## Extension guidelines

When extending this widget:

1. Keep data orchestration outside composables.
2. Keep each cell action short; offload long work to workers.
3. Prefer bounded responsive sizes instead of fully exact relayouts for smoother resize behavior.
4. Keep error handling actionable (`errorUiLayout` + retry intent path).
5. If layout semantics change significantly, update this document in the same PR.
