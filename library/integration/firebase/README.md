# `:library:integration:firebase` Logic Graph

## Purpose

Implements the toolkit's Firebase analytics/crash-reporting contract and provides Firebase messaging
service wiring.

## Owns

- `data.repositories.FirebaseTelemetryRepository` for analytics events, breadcrumbs, and crash
  reporting. The old names `DefaultFirebaseController` and `FirebaseControllerImpl` stay beside it
  as deprecated aliases.
- `firebaseModule` Koin bindings.
- `data.notifications.FirebaseNotificationsService` and its notification/wake-lock manifest
  permissions.

## Does not own

- The SDK-neutral `TelemetryRepository` contract, owned by `:library:core:common`.
- User consent decisions and persisted diagnostics preferences, owned by consent/settings/DataStore
  modules.

## Depends on

- [`:library:core:common`](../../core/common/README.md) for the `TelemetryRepository` contract and shared
  analytics values.

Firebase Analytics, Crashlytics, Performance, and Messaging materially define this module.

## Used by

- `:sample` and `:library:apptoolkit` to install the production Firebase implementation.

## Flow chart

```mermaid
flowchart TD
    Features[Features and LoggedScreenViewModel] --> Contract[TelemetryRepository]
    Consent[Diagnostics and consent state] --> Contract
    Contract -->|Koin binding| Impl[FirebaseTelemetryRepository]
    Impl --> Analytics[Firebase Analytics]
    Impl --> Crash[Crashlytics breadcrumbs and non-fatals]
    Impl --> Performance[Firebase Performance enablement]
    FCM[Firebase Cloud Messaging] --> Service[FirebaseNotificationsService]
    Service --> Notification[Android notification]
    Manifest[Permissions and service declaration] --> Service
```

## Architectural decisions

- Features depend on the SDK-neutral `TelemetryRepository`; only this module imports concrete
  Firebase products.
- Consent updates are applied through the same repository as analytics/crash/performance calls so
  enablement policy does not leak into every feature.
- Messaging remains a framework-created service and is wired through the manifest rather than the
  Koin-created repository lifecycle.

## Public contracts

- `firebaseModule` and the Android messaging service declared in the manifest.

## Internal implementations

- Firebase SDK calls and notification handling.

## Current risks

The module bundles several Firebase products, so hosts cannot currently select analytics, crash
reporting, performance, and messaging independently at the module boundary.
