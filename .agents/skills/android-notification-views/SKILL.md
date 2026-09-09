---
name: android-notification-views
description: >
  Implement, review, refactor, migrate, and debug Android notification
  experiences. Use when working with NotificationCompat, Notification.Builder,
  custom notification layouts, RemoteViews, DecoratedCustomViewStyle,
  MetricStyle, notification badges, time-sensitive notifications,
  full-screen intents, grouped notifications, notification navigation,
  PendingIntent behavior, notification actions, notification channels,
  notification updates, or Android version-specific notification behavior.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-09-09'
  source: Android Developers
  keywords:
    - android
    - notifications
    - notificationcompat
    - remoteviews
    - custom-notification
    - metricstyle
    - badges
    - time-sensitive
    - full-screen-intent
    - notification-groups
    - pendingintent
    - navigation
    - notification-channels
    - live-updates
---

# Android Notification Views

Build Android notifications that use the platform correctly, remain understandable across Android
versions, and fit the actual product behavior.

Use standard Android notification templates whenever they can represent the required experience. Use
a custom notification layout only when the system templates do not provide the content or
interaction model the feature needs.

The notification shade is system UI. The application controls the notification data and supported
layout APIs, but Android controls the final presentation.

## Start by inspecting the project

Before changing notification code, understand how the application currently creates, updates,
groups, and opens notifications.

Inspect:

- `AndroidManifest.xml`
- application initialization
- notification channel creation
- notification builders
- notification IDs and tags
- `PendingIntent` creation
- receivers and services used by notification actions
- foreground services
- WorkManager workers that post notifications
- navigation and deep links
- custom `RemoteViews` layouts
- drawable and string resources used by notifications
- notification permission handling
- API-level checks
- tests around notification behavior

Search for common APIs and concepts such as:

```text
NotificationCompat.Builder
Notification.Builder
NotificationManager
NotificationManagerCompat
NotificationChannel
RemoteViews
DecoratedCustomViewStyle
MetricStyle
PendingIntent
TaskStackBuilder
setContentIntent
addAction
setGroup
setGroupSummary
setFullScreenIntent
setNumber
notify
cancel
POST_NOTIFICATIONS
USE_FULL_SCREEN_INTENT
```

Determine:

- `minSdk`
- `targetSdk`
- `compileSdk`
- AndroidX Core version
- whether the app already uses platform-only notification APIs
- whether the notification belongs to a foreground service
- whether the notification is ongoing
- whether it represents a single event or an ongoing state
- whether it needs navigation into a normal application destination
- whether it requires urgent interruption
- whether multiple notifications represent related items
- whether badge behavior should be enabled or disabled
- whether a standard style already solves the design requirement

Do not start by replacing the existing notification builder.

## Choose the notification model first

Before writing code, classify the notification.

Examples:

```text
Simple informational notification
        -> standard notification

Long text or rich standard content
        -> system expandable style

Health, fitness, timer, or travel metrics
        -> consider MetricStyle when the platform requirements are met

Ongoing user-visible journey
        -> consider the appropriate system style and current Live Update guidance

Alarm, incoming call, or genuinely urgent event
        -> review time-sensitive notification guidance

Several independent related notifications
        -> notification group

Custom content that system templates cannot express
        -> custom notification content area using RemoteViews
```

Do not use a custom layout simply to make the notification look different from Android.

System templates generally provide better compatibility with:

- different Android versions
- notification shade sizes
- lock screen presentation
- accessibility
- system decorations
- heads-up presentation
- OEM variations
- future Android notification surfaces

Read the matching reference before implementing the chosen model.

## Reference guide

Read the reference that matches the notification behavior being changed. Several references may
apply to the same notification. For example, a custom notification may also need channel,
permission, navigation, grouping, or Live Update guidance.

### MetricStyle

Read:

[`references/metric-style.md`](references/metric-style.md)

Use it when the product needs Android's metric-oriented notification template.

Current Android documentation describes `MetricStyle` as a platform template for cases such as:

- health and fitness
- timers
- travel

Check the project's `compileSdk`, runtime Android version, and compatibility requirements before
using it.

Do not recreate a metric layout with `RemoteViews` when the platform style is the better match.

### Notification badges

Read:

[`references/badges.md`](references/badges.md)

Use it when working with:

- launcher notification dots
- per-channel badge behavior
- disabling badges for ongoing notifications
- custom notification counts
- badge-related notification design decisions

Badges are related to active notifications and launcher support.

Do not create a second application-managed badge system unless the product explicitly requires one.

### Time-sensitive notifications

Read:

[`references/time-sensitive.md`](references/time-sensitive.md)

Use it for genuinely urgent events such as:

- alarms
- incoming calls
- other immediate events where delayed attention would break the feature

Treat interruption as a product behavior, not a visual preference.

Do not make a normal notification time-sensitive simply to increase visibility.

Check:

- notification permission
- channel importance
- notification category
- full-screen intent requirements
- background activity launch restrictions
- lock-screen behavior
- Android version requirements
- Play policy requirements when applicable

### Custom notification layouts

Read:

[`references/custom-notification.md`](references/custom-notification.md)

Use custom layouts only when the standard Android notification templates cannot represent the
required content.

Prefer:

```text
NotificationCompat.Builder
        +
NotificationCompat.DecoratedCustomViewStyle
        +
RemoteViews
```

when a custom content area is needed and the AndroidX compatibility path applies.

Keep system decorations whenever possible.

A custom notification must be designed for restricted system space. Test:

- collapsed layout
- expanded layout
- heads-up layout when applicable
- light and dark system presentation
- narrow widths
- font scaling
- long localized text
- different Android versions
- relevant OEM devices when the feature is important

Do not assume a normal application layout will behave correctly when used as a notification
`RemoteViews`.

### Notification channels

Read:

[`references/channels.md`](references/channels.md)

Use it when working with:

- notification channel creation
- channel IDs
- channel importance
- sound and vibration behavior
- badge behavior controlled by a channel
- foreground-service notification channels
- migration from pre-Android 8.0 notification behavior
- user-visible notification categories

Notification channels are part of the public behavior of the app.

Create channels around stable categories that users can understand.

Examples:

```text
Downloads
Playback
Reminders
Security alerts
Background processing
```

Do not create a new channel for each notification.

Do not encode temporary state, timestamps, IDs, or user content into channel IDs.

Remember that after a channel is created, the user controls many of its behaviors. Changing builder
priority does not replace channel importance on Android 8.0 and newer.

### Notification permission

Read:

[`references/notification-permission.md`](references/notification-permission.md)

Use it when working with Android 13 and newer notification permission behavior.

Review:

- `POST_NOTIFICATIONS`
- manifest declaration
- runtime permission request
- when the request appears
- what the app does after denial
- notification behavior when permission is unavailable
- system settings where notifications can be disabled
- exemptions and special cases
- target SDK behavior
- foreground-service notification requirements

Do not request notification permission at an unrelated moment just because the app has started.

Prefer asking when the user can understand what notifications the feature will provide.

### Live Updates

Read:

[`references/live-update.md`](references/live-update.md)

Use it when a notification represents an ongoing, user-visible activity that may qualify for Android
Live Updates or promoted ongoing presentation.

Examples can include:

- active navigation
- rides or deliveries
- timers
- travel progress
- fitness or metric tracking
- other ongoing activities supported by current Android guidance

Before using Live Update behavior, inspect:

- Android version requirements
- supported notification styles
- promotion requirements
- ongoing notification behavior
- update frequency
- notification channel configuration
- foreground-service relationship when applicable
- fallback behavior on older Android versions

Do not assume every ongoing notification should become a Live Update.

Do not combine a custom `RemoteViews` layout with Live Update behavior when current Android guidance
does not support that combination.

### Notification groups

Read:

[`references/group.md`](references/group.md)

Group notifications when several related child notifications are still useful as individual
notifications.

Examples include:

- multiple messages
- multiple downloads
- multiple completed jobs
- several independent alerts from the same category

A group is not the same thing as a notification channel group.

Before creating several child notifications, consider whether updating one existing notification
would communicate the state more clearly.

When grouping, review:

- stable group key
- child notification IDs
- group summary
- summary content
- group alert behavior
- sorting
- cancellation behavior
- what happens as children are removed

### Navigation

Read:

[`references/navigation.md`](references/navigation.md)

Every actionable notification should open the correct application destination and preserve expected
navigation behavior.

Inspect whether the destination is:

- a normal destination in the application's navigation flow
- a special activity used only from the notification
- a deep link
- a single-activity Compose destination
- an action handled without opening UI

Review:

- `PendingIntent`
- request codes
- mutability flags
- update behavior
- task creation
- back stack
- deep-link arguments
- stale notification data
- authentication or permission gates
- destination validity after process death

Do not route every notification tap to the application home screen when the notification represents
a specific destination.

## Custom views should remain notification-sized

Custom notification layouts are not miniature application screens.

Keep them focused on:

- current state
- the most important value
- short supporting text
- essential actions

Avoid:

- dense dashboards
- large blocks of text
- many tiny buttons
- scrollable application-style layouts
- decorative elements that compete with the system header
- interaction that requires precision tapping
- duplicating information already supplied by the system decorations

The notification should still be understandable when collapsed.

## Use stable notification identity

Decide explicitly whether each update represents:

- a new notification
- an update to an existing notification
- a child in a notification group

Reuse the same notification ID when updating an existing notification.

Use distinct stable IDs when separate notifications must coexist.

If tags are used, keep the `(tag, id)` identity stable.

Avoid timestamp-derived IDs unless every event intentionally needs a separate notification.

## Updating notifications

When the state changes, update the existing notification instead of posting a new notification when
it represents the same ongoing item.

Consider:

```text
same ongoing operation
        -> same notification ID

new independent operation
        -> separate notification identity
```

For frequent updates:

- avoid unnecessary reposting
- avoid alerting on every update
- consider `setOnlyAlertOnce()`
- respect platform update behavior and rate limits
- stop updates when the underlying operation is complete

The visible notification state should not lag behind the application's committed state.

## Notification channels

Notification channel design is part of notification behavior.

Inspect existing channels before adding another one.

Channels should represent stable user-understandable notification categories.

Examples:

```text
Downloads
Playback
Reminders
Security alerts
Background processing
```

Do not create a new channel for every individual notification.

Do not encode volatile state into channel IDs.

Remember that after a channel is created, the user controls many of its behaviors.

Changing builder priority does not replace channel importance on Android 8.0 and newer.

## Permission handling

Android 13 and newer uses the `POST_NOTIFICATIONS` runtime permission for non-exempt notifications.

Do not assume that posting a notification means it will be visible.

Before designing notification behavior, inspect how the app:

- declares the permission
- requests the permission
- explains the reason for the permission
- handles denial
- handles notifications disabled in system settings

Do not request notification permission at an unrelated moment merely because the app has started.

Ask when the user can understand what notifications will provide.

## PendingIntent safety

Use the narrowest `PendingIntent` behavior required.

Review:

- immutable versus mutable
- explicit intent destination
- unique request code where separate pending intents need separate identity
- `FLAG_UPDATE_CURRENT`
- stale extras
- action identity
- exported component behavior

Prefer immutable pending intents unless mutability is required by the Android API or feature.

Do not reuse one `PendingIntent` identity for unrelated notification actions when their destinations
or extras differ.

## Actions

Notification actions should perform a clear task.

Examples:

- pause
- resume
- stop
- reply
- dismiss
- retry
- open details

Avoid actions that:

- duplicate the main notification tap without purpose
- silently perform destructive work without appropriate confirmation
- depend on an Activity already being alive
- lose required state after process death

When an action can be completed without opening the app, consider a receiver, service, worker, or
other appropriate component instead of launching UI only to execute the action.

## Foreground services

When the notification belongs to a foreground service, treat the notification as part of the service
contract.

Review:

- foreground service type
- manifest declarations
- service start restrictions
- notification channel
- notification ID stability
- ongoing state
- stop action
- process recreation behavior
- Android version requirements

Do not remove or delay a required foreground-service notification to simplify UI behavior.

## Grouping versus updating

Use this decision:

```text
Does the user need to see each item independently?
        |
        +-- Yes -> consider a notification group
        |
        +-- No -> update one existing notification
```

Do not create a group merely because several updates occurred.

## Custom layout versus system style

Use this decision:

```text
Can a standard Android notification style represent the content?
        |
        +-- Yes -> use the system style
        |
        +-- No -> is a custom content area actually necessary?
                    |
                    +-- Yes -> use the documented custom layout path
                    +-- No -> simplify the notification
```

Before using `RemoteViews`, inspect whether:

- standard notification
- BigTextStyle
- BigPictureStyle
- InboxStyle
- MessagingStyle
- CallStyle
- MediaStyle
- ProgressStyle
- MetricStyle

better matches the content.

## Android version handling

Do not assume a notification feature behaves identically across all Android versions.

For every platform-specific feature:

1. identify the API where it became available
2. inspect `minSdk`, `targetSdk`, and `compileSdk`
3. decide the behavior on older Android versions
4. use AndroidX compatibility APIs where appropriate
5. test the fallback behavior

Avoid version checks scattered throughout the UI when the notification construction can be
centralized.

## Testing

Notification testing should cover both construction logic and Android runtime behavior.

Local tests can verify:

- notification model selection
- stable IDs
- channel selection
- action mapping
- deep-link arguments
- group keys
- state-to-notification mapping
- version-dependent decisions when platform access is abstracted

Device or emulator validation should cover behavior that local tests cannot prove, such as:

- actual notification rendering
- collapsed and expanded custom layouts
- heads-up behavior
- lock-screen behavior
- permission flow
- launcher badges
- full-screen intents
- group presentation
- notification taps and back stack
- action buttons
- foreground-service integration
- OEM-specific rendering when relevant

Record the Android API level for platform-specific notification validation.

## Implementation workflow

Use this sequence:

```text
Inspect current notification system
        ↓
Identify notification use case
        ↓
Choose system style or custom layout
        ↓
Read the relevant reference
        ↓
Check permissions, channels, navigation, and identity
        ↓
Implement the smallest correct change
        ↓
Add or update tests
        ↓
Run targeted tests
        ↓
Validate on Android runtime when required
        ↓
Run repository-wide verification
```

## Review checklist

Before finishing, confirm:

- the chosen notification style matches the actual use case
- a custom layout is justified
- notification IDs are stable
- channels are appropriate
- permission handling is correct
- actions have correct `PendingIntent` identity
- navigation reaches the intended destination
- back navigation is sensible
- group behavior is intentional
- badge behavior is intentional
- urgent interruption is justified
- Android version fallback exists
- custom layouts fit notification constraints
- notification updates do not create accidental duplicates
- foreground-service requirements remain valid
- strings are localized
- accessibility is not damaged by the custom design
- tests cover notification construction logic
- runtime behavior is validated when local tests cannot prove it

## References

The files under `references/` are intended to contain the corresponding Android Developers
documentation.

Keep the copied documentation aligned with the official pages:

- `metric-style.md`
- `badges.md`
- `time-sensitive.md`
- `custom-notification.md`
- `group.md`
- `navigation.md`
- `channels.md`
- `notification-permission.md`
- `live-update.md`

When the platform documentation changes, update the corresponding reference rather than duplicating
the full documentation inside this file.
