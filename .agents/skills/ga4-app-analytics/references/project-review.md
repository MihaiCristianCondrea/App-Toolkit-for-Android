# Project Review

## Purpose

Every analytics strategy must start with the application itself.

Do not design events from a generic checklist before understanding the project.

## Review order

### 1. Understand the product

Read the project description, README files, navigation, feature modules, and main screens.

Identify:

- Core user problem.
- Main features.
- Repeated workflows.
- Content types.
- Success states.
- Monetization model.
- Important user value moments.

Examples of value moments:

```text
Cleaner app       cleanup completed
Tutorials app     lesson completed
Network utility   successful scan or speed test
Shopping app      checkout completed
Media app         meaningful playback or subscription action
```

### 2. Map the major journeys

For each major feature, identify:

```text
entry
intent
important step
completion
failure or cancellation
```

Not every step needs an event.

Track the smallest set that can answer useful questions about adoption, progression, abandonment,
retention, and value.

### 3. Inspect existing analytics

Search for:

```text
FirebaseController
AnalyticsEvent
AnalyticsValue
Ga4EventData
logEvent
logGa4Event
logScreenView
TrackScreenView
TrackScreenState
LoggedScreenViewModel
FirebaseAnalytics
setUserProperty
```

Also inspect analytics contracts, constants, tests, and README files.

Build an inventory containing:

- Event name.
- Parameters.
- Emission point.
- Event type: automatic, recommended, custom, diagnostics.
- Purpose.
- Potential duplication.
- Potential privacy or cardinality concerns.

### 4. Inspect screens and navigation

Find meaningful destinations in Navigation, Navigation 3, activities, sheets, dialogs, and adaptive
layouts.

Decide which surfaces deserve a screen view.

### 5. Inspect product capabilities

Check whether the app has:

- Onboarding.
- Authentication.
- Search.
- Content browsing.
- Sharing.
- Purchases.
- Subscriptions.
- Ads.
- AdMob mediation.
- Third-party mediation.
- Remote Config.
- A/B Testing.
- Notifications.
- Favorites.
- History.
- Feature categories.
- User customization.

These capabilities can change which events are relevant.

### 6. Inspect monetization

If ads are present, determine:

- AdMob or another SDK.
- Mediation setup.
- Native, banner, interstitial, rewarded, or app-open ads.
- Whether Firebase and AdMob are linked.
- Whether ad revenue is already automatically available in GA4.
- Whether manual paid event or impression revenue logging exists.
- Whether automatic ad events may already cover proposed analytics.

If purchases are present, inspect how purchase events and revenue are reported before adding new
events.

### 7. Inspect consent and privacy

Understand how analytics collection is enabled or disabled and where consent is applied.

Do not introduce a second consent policy in feature code when the project already controls
collection centrally.

## Questions the strategy should answer

A good project review should make it possible to answer questions such as:

- Which features are used most?
- Which features are completed successfully?
- Which features are opened but abandoned?
- Which content categories attract the most engagement?
- Which behaviors are associated with retention?
- Which behaviors correlate with ad or purchase revenue?
- Which flows have meaningful failure rates?
- Which user journeys deserve to become key events?
- Which events are currently misleading or duplicated?

## Do not implement during review

The review phase produces a strategy, not code changes.

After the review, present options and ask for implementation approval unless the user explicitly
requested immediate implementation.
