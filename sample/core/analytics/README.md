# `:sample:core:analytics` Logic Graph

## Purpose

Defines the sample application's stable telemetry vocabulary without duplicating the toolkit's
Firebase transport or Compose tracking APIs.

## Owns

- `models.AppScreenTracking`, the sample's screens as `TrackedScreen`s, passed to `ScreenHost`.
- `contracts.AppGa4Contract`, event names, parameter names, required schemas, and forbidden
  parameter keys.
- Contract tests that reject invalid event names, duplicate screens, and unsafe required fields.

## Does not own

- Firebase initialization, event delivery, or screen-tracking composables. Those remain reusable
  library responsibilities.
- Feature interaction timing or payload values, which remain with the feature that emits them.

## Depends on

- [`:library:core:common`](../../../library/core/common/README.md) for `TrackedScreen`, the
  library's UI-free screen descriptor that `ScreenHost` reports.
- Compose runtime only because all sample library modules currently apply the shared Compose
  convention plugin.

## Used by

- `:sample:app` and sample feature modules when emitting screen views or app events.

## Architectural decisions

Identifiers are app-owned constants. Features consume them directly, which avoids inline strings
drifting while keeping the reusable library free of product-specific event names.


## Tool journey events

| Event | Parameters | Meaning |
| --- | --- | --- |
| `tool_opened` | `tool_id`, `workspace` | A sheet or full-screen tool entered composition. |
| `tool_used` | `tool_id`, `workspace` (new payloads) | First meaningful use within the feature's use-session. |
| `tool_cycle_finished` | `tool_id`, `workspace`, `outcome` | Water Eject cycle completed, cancelled or failed. |

These custom events describe tool journeys rather than ecommerce. Existing screen names and
other event names remain stable. Quick Tools' historical `view_item` opening series ends when
`tool_opened` starts; do not add their counts together as separate funnel steps.

Feature builders only accept known catalogue IDs and bounded outcomes. They live in the UI
package and deliver through `TelemetryRepository`, preserving centralized collection consent.
Operational `vm_op_start` and `vm_op_error` remain diagnostics, not successful product outcomes.

For reporting, register event-scoped dimensions for `tool_id`, `workspace` and `outcome` in GA4
if they are not already registered. No property configuration or key-event designation is performed
by the app. Compare opening-to-use and use-to-completion journeys by tool and workspace; inline
use need not have a preceding sheet opening. Ad revenue remains supplied by the existing integration.

Validate release-like consent on and off, each workspace, reopen/recomposition, inline use, and
Water Eject completion/Stop/failure in Firebase DebugView on a development device. Enable with
`adb shell setprop debug.firebase.analytics.app PACKAGE_NAME` using the installed variant's ID;
disable with `adb shell setprop debug.firebase.analytics.app .none.`.

References: [Firebase DebugView](https://firebase.google.com/docs/analytics/debugview) and
[custom event parameters](https://developers.google.com/analytics/devguides/collection/ga4/event-parameters).
