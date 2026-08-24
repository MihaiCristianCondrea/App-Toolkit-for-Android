# `:library:feature:support` Logic Graph

## Purpose

Presents donation/support products and coordinates purchases through the billing integration.

## Owns

- Support screen/activity/ViewModel and state/event/action contracts.
- Donation product IDs, product-detail mapping helpers, and support-facing billing result types.
- The support-level billing repository abstraction consumed by its UI.

## Does not own

- Google Play BillingClient lifecycle, owned by `:library:integration:billing`.
- Generic shared UI/ad primitives, owned by `:library:core:ui`.

## Depends on

- `:library:core:common`, `:library:core:network`, and `:library:core:ui` for shared
  billing/Firebase contracts, errors, and Compose foundations.
- [`:library:integration:billing`](../../integration/billing/README.md) for Play Billing access.
- [`:library:navigation`](../../navigation/README.md) for navigation support.

## Used by

- `:sample`, `:library:apptoolkit`, and `:library:feature:about`.

## Flow chart

```mermaid
flowchart TD
    Screen[SupportScreen] -->|setup| VM[SupportViewModel]
    VM -->|query donation IDs| Billing[BillingRepository]
    Billing --> Play[Play BillingClient]
    Play -->|product details| ProductFlow[Replaying productDetails Flow]
    ProductFlow --> VM
    VM --> Options[DonationOptionUiState map]
    Options --> Screen
    Screen -->|donate with valid Activity| VM
    VM -->|launch one-time purchase| Billing
    Play -->|purchase callback| Billing
    Billing -->|consume completed donation| Play
    Billing --> Result[PurchaseResult Flow]
    Result --> VM
    VM --> State[Loading / no-data / success / snackbar]
    State --> Screen
```

## Architectural decisions

- The support feature depends on the billing contract, not `BillingClient`; Play types are limited
  to the boundary values required to launch the actual offer.
- Product IDs belong to the feature because they define the donation catalog. Connection lifecycle,
  retries, purchase recovery, and consumption belong to the billing integration.
- Product details have no synthetic initial value. The screen remains loading until the first query
  emits, then distinguishes an empty catalog from available options.
- A purchase launch is guarded against invalid activities and duplicate taps, with a UI timeout so
  an absent SDK callback cannot leave the screen permanently busy.

## Public contracts

- Support presentation entry points/contracts and its billing abstraction/result.

## Internal implementations

- Product grouping/formatting and donation UI behavior.

## Current risks

Billing contracts/results exist in common, integration, and support namespaces, creating overlapping
concepts that can be confused during changes.
