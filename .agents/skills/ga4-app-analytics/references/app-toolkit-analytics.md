# App Toolkit Analytics

## Purpose

This reference explains the analytics path used by App Toolkit host applications.

The host application should normally build on this system rather than creating a second Firebase
Analytics path.

## Main flow

```text
Host feature or reusable Toolkit UI
        ↓
AnalyticsEvent or Ga4EventData
        ↓
FirebaseController
        ↓
FirebaseControllerImpl
        ↓
Firebase Analytics
```

`FirebaseController` lives in App Toolkit core common code. Host features depend on this contract
instead of importing the Firebase Analytics SDK directly.

The Firebase integration provides the concrete implementation through dependency injection.

## Analytics models

App Toolkit uses a small set of value types for event parameters:

```kotlin
sealed interface AnalyticsValue {
    data class Str(val value: String) : AnalyticsValue
    data class LongVal(val value: Long) : AnalyticsValue
    data class DoubleVal(val value: Double) : AnalyticsValue
    data class Bool(val value: Boolean) : AnalyticsValue
}

data class AnalyticsEvent(
    val name: String,
    val params: Map<String, AnalyticsValue> = emptyMap(),
)
```

Host applications can define their own event contracts using these types.

## FirebaseController responsibilities

`FirebaseController` provides the common path for:

- Analytics events.
- Screen views.
- User properties.
- Analytics enablement.
- Consent updates.
- Crashlytics breadcrumbs.
- ViewModel error reports.
- Non-fatal crash reports.
- Firebase Performance enablement.

Feature code should normally call the controller instead of directly accessing Firebase SDK classes.

## FirebaseControllerImpl behavior

The Firebase implementation validates and converts Toolkit events before sending them to Firebase
Analytics.

Current protections include:

- Event name validation.
- Parameter name validation.
- Reserved prefix rejection.
- Maximum parameter count.
- String value trimming.
- User property name and value limits.

Do not rely only on transport validation for analytics quality. A technically valid event can still
be semantically wrong, noisy, unsafe, or useless.

## LoggedScreenViewModel

`LoggedScreenViewModel` provides standard operational telemetry.

It records ViewModel lifecycle and UI event breadcrumbs for Crashlytics and can emit operation
events such as:

```text
vm_op_start
vm_op_error
```

These events describe application operations. They are not a replacement for product analytics.

For example, a lesson completion or cleanup completion should still have a meaningful product event
when that behavior matters to the host app.

## Screen tracking

`TrackScreenView` is the normal App Toolkit helper for explicit Compose screen tracking.

It sends a screen view through `FirebaseController.logScreenView()` when the stable screen name
changes.

Use it at meaningful top-level destinations.

`TrackScreenState` can additionally report bounded states such as:

```text
loading
success
no_data
error
```

Use screen state tracking when it answers a real product or reliability question. Do not add it
mechanically to every small component.

## Reusable UI analytics

App Toolkit supports `Ga4EventData` for reusable UI components.

A reusable component can receive:

```text
firebaseController
ga4Event
```

and log the event at the exact interaction point.

Before adding a manual event around a Toolkit button, preference, chip, field, or FAB, inspect
whether the component already supports GA4 logging. Duplicate click events can otherwise be emitted
from both the component and the caller.

## Host ownership

The host application should own:

- Product-specific event names.
- Product-specific parameters.
- Required event schemas.
- Stable screen identifiers for host-owned screens.
- Product-specific analytics tests.

App Toolkit should own:

- Firebase transport.
- Generic Toolkit tracking helpers.
- Toolkit-owned event vocabularies for Toolkit behavior.
- Consent application.
- Shared analytics value models.

## Consent

App Toolkit applies analytics and advertising consent centrally.

Features should not normally wrap every event in their own consent check.

The central consent system controls:

- Analytics storage consent.
- Ad storage consent.
- Ad user data consent.
- Ad personalization consent.
- Analytics collection enablement.
- Crashlytics collection enablement.
- Firebase Performance collection enablement.

Keep product instrumentation separate from consent policy.

## Practical rule

When reviewing an App Toolkit host app, first ask:

1. Does the Toolkit already track this behavior?
2. Does a reusable component already emit the interaction event?
3. Is this operational telemetry or product analytics?
4. Does the host already have an analytics contract for this product behavior?
5. Can the existing architecture express the new event without introducing another analytics layer?
