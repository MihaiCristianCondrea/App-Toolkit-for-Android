---
name: ga4-app-analytics
description: Review and improve GA4 analytics instrumentation for Android host apps, especially apps that use App Toolkit. Inspect the project first, design a project-specific analytics strategy, present implementation options, ask for approval, then implement the selected strategy using the existing analytics architecture.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-03-30'
  keywords:
  - ga4
  - google analytics
  - analytics
  - android
  - app toolkit
  - firebase analytics
  - telemetry
  - instrumentation
---

# GA4 App Analytics

Use this skill when reviewing, planning, or implementing Google Analytics 4 instrumentation in an
Android application.

The goal is not to maximize event count. The goal is to measure meaningful product behavior with a
clean, stable analytics vocabulary that helps with product decisions, marketing, experimentation,
retention, and monetization analysis.

This skill is designed for host applications that use App Toolkit, but the event design principles
are general.

## Core principles

1. Inspect the project before proposing analytics changes.
2. Understand what the application actually does before choosing events.
3. Prefer automatically collected events when they already cover the behavior.
4. Prefer a Google recommended event when its documented meaning genuinely matches the product
   behavior.
5. Use custom events freely when the product has important behavior that Google recommendations do
   not describe.
6. Do not force a recommended event onto unrelated behavior just because the event name exists.
7. Do not duplicate events already collected automatically by Firebase, GA4, AdMob, or an existing
   project integration.
8. Keep event names stable and parameters bounded.
9. Never send sensitive, identifying, user-entered, or high-cardinality values without a clear and
   safe reason.
10. Separate analysis from implementation. Propose first. Implement only after approval unless the
    user explicitly requests direct implementation.

## Required workflow

### Phase 1: Inspect the project

Do not start by adding events.

Review enough of the project to understand:

- Application purpose and major user journeys.
- Navigation destinations and meaningful screens.
- Existing analytics modules, contracts, constants, tests, and helper functions.
- App Toolkit usage, especially `FirebaseController`, `LoggedScreenViewModel`, `TrackScreenView`,
  `TrackScreenState`, `AnalyticsEvent`, `AnalyticsValue`, `Ga4EventData`, and reusable UI components
  with GA4 support.
- Existing Firebase Analytics, Crashlytics, Performance, consent, AdMob, mediation, purchases,
  authentication, onboarding, search, content browsing, sharing, subscriptions, and Remote Config
  usage.
- Current automatic, recommended, and custom events.
- Existing event parameters and user properties.
- Existing screen tracking.
- Duplicate, misleading, overly broad, overly specific, unsafe, or noisy events.

Read `references/project-review.md` and `references/app-toolkit-analytics.md` during this phase.

### Phase 2: Build an analytics map

Create a project-specific map of important behavior.

For each important journey, identify:

- Entry point.
- Meaningful user intent.
- Important intermediate steps.
- Successful completion.
- Failure or cancellation when analytically useful.
- Relevant content or feature category.
- Whether the behavior can influence activation, retention, monetization, marketing, or
  experimentation.

Classify existing and proposed events as:

- Automatically collected.
- Google recommended.
- Product-specific custom.
- Operational or diagnostics telemetry.

Read `references/event-strategy.md` and `references/recommended-events.md` during this phase.

### Phase 3: Review screens

Inspect the navigation structure and top-level Compose destinations.

Verify that meaningful destinations are tracked once when they become visible, not on every
recomposition.

Use stable screen names and classes. Do not encode IDs, URLs, search text, IP addresses, file paths,
timestamps, or user data into screen names.

Read `references/screen-tracking.md`.

### Phase 4: Review monetization and marketing value

For every proposed event, decide what it is useful for.

Possible purposes include:

- Product analytics.
- Funnel analysis.
- Retention analysis.
- Monetization analysis.
- AdMob personalization through documented automatic or recommended signals.
- GA4 audiences and remarketing.
- Google Ads key events and campaign optimization.
- Firebase experiments and Remote Config measurement.

Do not claim that an arbitrary custom event directly improves AdMob ad personalization unless
current Google documentation explicitly supports that behavior.

Custom events can still be highly valuable because they can explain which features, content, or
journeys correlate with retention, ad revenue, purchases, or high-value users.

Read `references/monetization-and-marketing.md`.

### Phase 5: Validate privacy and data quality

Before recommending an event or parameter, check whether it can contain:

- Personally identifiable information.
- User-entered text.
- Email addresses or phone numbers.
- Device identifiers.
- IP addresses.
- URLs or URIs.
- File names or file paths.
- Precise location.
- Exception messages or stack traces.
- Random IDs, timestamps, or values likely to create high cardinality.

Prefer bounded categories such as `success`, `timeout`, `cancelled`, `wifi`, `cellular`, `beginner`,
or `advanced`.

Read `references/privacy-and-data-quality.md`.

### Phase 6: Present options before implementation

Present a small number of strategies based on the actual project. Usually three is enough.

A useful default structure is:

| Option    | Scope                                                                                          |
|-----------|------------------------------------------------------------------------------------------------|
| Essential | Critical screens, important recommended events, core product outcomes, obvious analytics fixes |
| Balanced  | Essential plus important custom product events, funnels, categories, and monetization signals  |
| Detailed  | Balanced plus deeper feature instrumentation useful for advanced analysis and experiments      |

Do not mechanically use these exact names when another grouping better matches the project.

For each option explain:

- What would be added.
- What would be changed or removed.
- Why it helps.
- Which events are recommended versus custom.
- Where events should be emitted.
- Important parameters.
- Privacy or duplication concerns.
- Expected analytics, marketing, or monetization value.

Choose one preferred strategy and explain why it best fits the project.

Then ask for approval with a direct question in this form:

> I think the Balanced strategy is the best fit for this project because it captures the important
> product journey without adding noisy analytics. Can I implement it?

Do not modify the project before this approval unless the user already explicitly requested
implementation without a review gate.

### Phase 7: Implement after approval

After approval:

1. Keep the existing project architecture.
2. Reuse App Toolkit analytics APIs when the project uses App Toolkit.
3. Keep product-specific event names and schemas owned by the host application.
4. Reuse Toolkit-owned analytics vocabulary where the Toolkit already defines the behavior.
5. Prefer a stable event contract rather than scattering raw event strings across features.
6. Emit events at the point where the behavior is known accurately.
7. Avoid duplicate logging between UI components and ViewModels.
8. Update or add tests for event names, required parameters, forbidden parameters, screen
   identifiers, and important emission paths.
9. Run the relevant tests and build checks.
10. Summarize the implemented strategy and call out any analytics decisions that remain
    intentionally unimplemented.

Read `references/implementation-and-testing.md` before editing.

## Custom product events are first-class

Recommended GA4 events are not a complete description of an application.

A cleaner app may care more about which cleaner is opened and completed than about search behavior.
A tutorials app may care more about Kotlin lesson usage than Python lesson usage. A network utility
may care about scan completion, speed tests, or Wake-on-LAN usage.

Use custom events for important product behavior that recommended events do not describe.

Prefer a shared event with bounded parameters when several features represent the same underlying
action.

Example:

```text
cleaner_completed
cleaner_type = empty_folders | duplicates | large_files
outcome = success | cancelled | error
```

This is usually better than creating separate event names for every cleaner and every outcome.

Use separate events when the behaviors have materially different product meaning.

Example:

```text
speed_test_completed
wake_on_lan_sent
lesson_completed
cleanup_completed
```

Do not collapse unrelated product concepts into one vague `feature_action` event.

## Recommended events still matter

Use Google recommended events when the semantics match the application.

Examples include:

- `tutorial_begin`
- `tutorial_complete`
- `select_content`
- `search`
- `share`
- `login`
- `sign_up`
- `purchase`

Use the documented parameters correctly.

For example, do not use `search` for a network probe or LAN discovery. A real GA4 search should
represent an actual user search and use the documented search parameters.

## Event design rules

Prefer events that describe meaningful intent or outcomes:

```text
lesson_opened
lesson_completed
scan_started
scan_completed
cleanup_completed
share
purchase
```

Avoid logging every small UI action unless it answers a useful question:

```text
button_clicked
icon_pressed
row_tapped
column_clicked
```

Use parameters to describe bounded context:

```text
lesson_opened
language = kotlin
level = intermediate
```

Do not create a new event name for every parameter value.

## App Toolkit ownership

When the host uses App Toolkit:

- App Toolkit owns Firebase transport and common analytics helpers.
- The host application owns its product-specific event vocabulary and screen identifiers.
- `FirebaseController` is the normal path for event delivery.
- `LoggedScreenViewModel` provides standardized operation and error telemetry.
- `TrackScreenView` handles explicit screen views for Compose destinations.
- `TrackScreenState` can record bounded screen state transitions.
- Reusable Toolkit UI components may already support `Ga4EventData` and direct interaction logging.

Always inspect existing component analytics support before adding another manual event around the
same click.

## Event purpose classification

When presenting recommendations, classify each event by its main value:

| Purpose               | Meaning                                                                |
|-----------------------|------------------------------------------------------------------------|
| Product insight       | Helps understand feature or content usage                              |
| Funnel                | Helps understand progression and abandonment                           |
| Retention             | Helps identify behavior associated with returning users                |
| Monetization analysis | Helps correlate behavior with ad or purchase revenue                   |
| Marketing             | Useful for audiences, key events, or campaign optimization             |
| Ad personalization    | Only claim this when documented by current Google guidance             |
| Experimentation       | Useful as a goal or segment for A/B tests or Remote Config experiments |
| Diagnostics           | Helps understand application operation rather than user value          |

## Sources and freshness

GA4, Firebase, AdMob, Google Ads, and privacy guidance changes over time.

When a recommendation depends on current platform behavior, especially monetization, attribution,
automatic events, limits, or privacy rules, verify the current official Google documentation before
making a strong claim.

Use `references/official-sources.md` as the starting point.

## Reference files

- `references/app-toolkit-analytics.md`: How App Toolkit sends events, tracks screens, handles
  consent, and separates Toolkit-owned from host-owned analytics.
- `references/project-review.md`: Mandatory project inspection checklist and strategy discovery
  process.
- `references/event-strategy.md`: Event taxonomy, custom event design, parameter design, and event
  quality rules.
- `references/recommended-events.md`: How to decide when a Google recommended event genuinely fits.
- `references/screen-tracking.md`: Screen tracking rules for Compose and navigation-based Android
  apps.
- `references/monetization-and-marketing.md`: AdMob, GA4, Google Ads, key events, audiences,
  experiments, and monetization analysis.
- `references/privacy-and-data-quality.md`: Privacy, cardinality, naming, sensitive data, and data
  quality safeguards.
- `references/implementation-and-testing.md`: App Toolkit implementation patterns, approval gate,
  tests, and validation.
- `references/official-sources.md`: Official documentation links to re-check when current behavior
  matters.
