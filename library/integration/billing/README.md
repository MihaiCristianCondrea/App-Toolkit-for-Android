# `:library:integration:billing` Logic Graph

## Purpose

Wraps Google Play Billing behind a reusable repository and Koin module.

## Owns

- Billing client lifecycle, product queries, purchase launches, and purchase-state exposure.
- The `BillingRepository` contract and its `DefaultBillingRepository` Play Billing implementation.
- The Play Billing manifest permission merged into consuming applications.

## Does not own

- Donation product IDs or support-screen behavior, owned by `:library:feature:support`.
- Generic purchase models/contracts, owned by `:library:core:common`.

## Depends on

- [`:library:core:common`](../../core/common/README.md) for `BillingCore`, purchase results, and
  shared lifecycle helpers.
  `BillingRepository` extends `BillingCore`, so the core manager can close billing without seeing
  the purchase surface.

## Used by

- `:sample` and `:library:apptoolkit` for application/integration assembly.
- `:library:feature:support` to query products and launch support purchases.

## Flow chart

```mermaid
flowchart TD
    Feature[Support feature] -->|query product IDs| Repo[BillingRepository singleton]
    Repo --> Connection{BillingClient ready?}
    Connection -->|no| Retry[Bounded connection retry]
    Retry --> Client[Play BillingClient]
    Connection -->|yes| Client
    Client -->|product query| Details[Replaying productDetails Flow]
    Details --> Feature
    Feature -->|launch selected offer| Repo
    Repo --> Client
    Client --> Callback[PurchasesUpdatedListener]
    Callback --> State{Purchase state}
    State -->|purchased| Consume[Consume one-time donation]
    State -->|pending| Result[PurchaseResult Flow]
    State -->|cancelled or error| Result
    Consume --> Result
    Repo -->|startup / reconnect| Past[Query past purchases]
    Past --> Consume
    Result --> Feature
```

## Architectural decisions

- One process-scoped repository owns the single `BillingClient`, its callback listener, connection
  retries, and purchase recovery.
- One-time support purchases are consumable donations. Completed unconsumed purchases are recovered
  on setup/reconnect and consumed before success is emitted.
- Product details replay the latest query result; purchase outcomes do not replay because they are
  one-off events.
- `BillingRepository` extends the narrow `BillingCore` teardown contract so common lifecycle code
  can close billing without depending on Play Billing APIs.

## Public contracts

- `BillingRepository` and `BillingModule`. Consumers inject the interface: the implementation is a
  process-wide singleton behind a private constructor, which a feature module can neither substitute
  nor fake in a test.

## Internal implementations

- `DefaultBillingRepository`: BillingClient callbacks, connection management, product-detail
  replay, retry policy, past-purchase queries, and donation consumption.

## Current risks

Billing client and callback behavior is compatibility-sensitive to Play Billing Library upgrades.
