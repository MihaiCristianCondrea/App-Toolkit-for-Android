# `:sample:core:analytics` Logic Graph

## Purpose

Defines the sample application's stable telemetry vocabulary without duplicating the toolkit's
Firebase transport or Compose tracking APIs.

## Owns

- `AppScreenTracking`, the canonical name/class pairs for sample-owned screens.
- `AppGa4Contract`, event names, parameter names, required schemas, and forbidden parameter keys.
- Contract tests that reject invalid event names, duplicate screens, and unsafe required fields.

## Does not own

- Firebase initialization, event delivery, or screen-tracking composables. Those remain reusable
  library responsibilities.
- Feature interaction timing or payload values, which remain with the feature that emits them.

## Depends on

- Compose runtime only because all sample library modules currently apply the shared Compose
  convention plugin.

## Used by

- `:sample:core:shell` and sample feature modules when emitting screen views or app events.

## Architectural decisions

Identifiers are app-owned constants. Features consume them directly, which avoids inline strings
drifting while keeping the reusable library free of product-specific event names.
