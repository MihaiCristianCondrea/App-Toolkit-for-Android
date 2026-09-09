# Implementation and Testing

## Approval gate

Do not implement during the strategy review unless the user explicitly requested direct
implementation.

After presenting options, recommend one and ask:

> I think this strategy is the best fit for the project because it captures the important product
> behavior without adding unnecessary analytics. Can I implement it?

Implementation begins only after approval.

## App Toolkit implementation rules

When the host app uses App Toolkit:

1. Reuse `FirebaseController`.
2. Reuse `AnalyticsEvent` and `AnalyticsValue`.
3. Reuse `Ga4EventData` for Toolkit UI components that support it.
4. Reuse `TrackScreenView` for explicit Compose screen views.
5. Reuse `TrackScreenState` only when screen states have analytical value.
6. Reuse `LoggedScreenViewModel` operation telemetry instead of duplicating operation start and
   error events manually.
7. Keep product-specific event contracts in the host application.
8. Keep Toolkit-owned analytics vocabulary in the Toolkit.

## Choose the correct emission point

### ViewModel or domain-facing product analytics

Good for:

- Operation completion.
- Business outcome.
- Failure outcome.
- State transitions known by the ViewModel.

Examples:

```text
cleanup_completed
speed_test_completed
lesson_completed
```

### Compose or UI interaction point

Good for:

- Pure UI selection.
- Opening a sheet.
- Selecting a tab.
- Choosing content when the interaction itself is the event.

Before adding a manual event, check whether the Toolkit component already accepts `ga4Event`.

### Screen helper

Use for screen visibility, not general feature events.

## Avoid duplicates

Common duplicate patterns include:

```text
Component logs click
+
Caller logs same click
```

```text
ViewModel logs completion
+
UI logs completion after observing state
```

```text
Automatic AdMob event
+
manual ad event with same meaning
```

```text
Activity automatic screen view
+
manual Compose screen view with same semantics
```

Inspect the full path before adding instrumentation.

## Event contract tests

A host analytics contract should normally test:

- Event names are valid.
- Event names are unique.
- Required parameters are defined correctly.
- Forbidden parameter keys are rejected.
- Screen names are stable and unique where expected.
- Bounded enum values stay stable.

## Emission tests

Important ViewModels or product analytics helpers should test that:

- Start events fire at the intended point.
- Completion events fire only when the operation completes.
- Failure outcomes use the correct bounded value.
- Cancelled operations do not accidentally report success.
- Duplicate calls are avoided.
- Sensitive values are not included.

## Screen tests

Where practical, verify that screen identifiers are stable and that screen tracking is not tied to
dynamic content values.

## Build validation

After implementation:

1. Run analytics contract tests.
2. Run affected feature tests.
3. Build the affected modules or application.
4. Review the final event inventory.
5. Confirm there are no new direct Firebase Analytics calls when App Toolkit already provides the
   required path.
6. Confirm no user data, raw targets, file paths, URLs, or exception text were added to GA4
   parameters.

## Final implementation summary

Report:

- Added events.
- Changed or removed events.
- Recommended versus custom events.
- Screen tracking changes.
- Key parameters.
- Tests added or updated.
- Build result.
- Any follow-up work that requires GA4 console, Firebase console, AdMob, or Google Ads configuration
  outside the codebase.
