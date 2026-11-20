Live updates provide a summary of important updates so users can track progress
without opening the app. The system promotes Live Update notifications although
users can temporarily dismiss or demote a live update notification, to a
standard notification. Promoted notifications appear more prominently on system
surfaces, including at the top of the notification drawer and the lock screen,
and as a chip in the status bar. Live update notifications should follow
notification principles for delivering brief, timely, and relevant information.
![](https://developer.android.com/static/images/design/ui/mobile/notifications-live-update.png) *
*Figure 1.** An example of a Live Update notification.

Promoted notification cards have the following appearance characteristics:

- Expanded by default
- Uncollapsible

Your notification must meet the following requirements to qualify as a live
update:

- Must be Standard Style, [
  `BigTextStyle`](https://developer.android.com/reference/android/app/Notification.BigTextStyle), [
  `CallStyle`](https://developer.android.com/reference/android/app/Notification.CallStyle), [
  `ProgressStyle`](https://developer.android.com/reference/android/app/Notification.ProgressStyle),
  or [`MetricStyle`](https://developer.android.com/reference/android/app/Notification.MetricStyle).
- Must request the following non-runtime permission in the android manifest [
  `android.permission.POST_PROMOTED_NOTIFICATIONS`](https://developer.android.com/reference/android/Manifest.permission#POST_PROMOTED_NOTIFICATIONS).
- Must request promotion using [
  `EXTRA_REQUEST_PROMOTED_ONGOING`](https://developer.android.com/reference/android/app/Notification#EXTRA_REQUEST_PROMOTED_ONGOING)
  or [
  `NotificationCompat.Builder#setRequestPromotedOngoing`](https://developer.android.com/reference/android/app/Notification.Builder#setRequestPromotedOngoing(boolean)).
- Must be [
  `ongoing`](https://developer.android.com/reference/android/app/Notification.Builder#setOngoing(boolean)) (
  set [
  `FLAG_ONGOING_EVENT`](https://developer.android.com/reference/android/app/Notification.Builder#setOngoing(boolean)))).
- Must have a `contentTitle` set.
- Must **NOT** have any [
  `customContentView`](https://developer.android.com/reference/android/app/Notification.Builder#setCustomContentView(android.widget.RemoteViews))
  set (no `RemoteViews`).
- Must **NOT** be the summary of a group using [
  `setGroupSummary`](https://developer.android.com/reference/android/app/Notification.Builder#setGroupSummary(boolean)).
- Must **NOT** [
  `setColorized`](https://developer.android.com/reference/android/app/Notification.Builder#setColorized(boolean))
  to `TRUE`.
- The notification channel must **NOT** have [
  `IMPORTANCE_MIN`](https://developer.android.com/reference/android/app/NotificationManager#IMPORTANCE_MIN).

## Promotion characteristics

The following APIs help you determine whether the system will promote your
notification:

- `Notification.FLAG_PROMOTED_ONGOING` indicates whether the notification is promoted.
- `Notification.hasPromotableCharacteristics()` validates whether the system can promote the
  notification. This method does not consider whether the user disabled Live Updates for the app in
  settings.
- `NotificationManager.canPostPromotedNotifications()` checks whether your app can post a promoted
  notification, for example, if the user enabled or disabled it in settings.
- `Settings.ACTION_MANAGE_APP_PROMOTED_NOTIFICATIONS` is the intent action that allows apps to send
  users to Settings to enable this feature.

> [!NOTE]
> **Note:** Original Equipment Manufacturers (OEMs) can enforce additional criteria for Live update
> eligibility. Check their respective documentation for details.

## Usage criteria

Use Live Updates for activities that are ongoing, user-initiated, and
[time-sensitive](https://developer.android.com/develop/ui/views/notifications/time-sensitive).
**Figure 2.** A live update on your home and lock screen, and as a chip.

### Ongoing

A Live Update must represent an activity that is actively in progress, with a
distinct start and end. If an activity occurs in the past, don't use a Live
Update. Instead, use a standard notification. This is also true for events that
have not yet begun, although events that are about to start can use a Live
Update.

Live Updates represent ongoing activities. Don't use Live Updates to offer
accelerated access to app functionality. If you want to do this, use an app
widget or a custom Quick Settings tile.

- **Appropriate uses:** Active navigation, ongoing phone calls, active rideshare tracking, and
  active food delivery tracking.
- **Inappropriate uses:** Ads, promotions, chat messages, alerts, upcoming calendar events, and
  quick access to app features.

### User-initiated

Most Live Updates should represent activities that are explicitly triggered by
the user, such as starting a workout, initiating driving navigation, or hailing
a rideshare. Don't show ambient information, such as that about the user's
environment, interests, or upcoming events, in a Live Update. Don't allow
activities triggered by other parties to generate Live Updates.

Sometimes, a user might perform an action that initiates an activity for some
time in the future. For example, if the user purchases tickets for a flight or
concert, signs up for a tournament, or otherwise indicates their future
attendance to a time-sensitive event. In these cases, it may be appropriate to
automatically show a Live Update when the scheduled event begins. However, apps
must tune their triggers to only appear when the activity is imminent. If the
user explicitly indicates that they want to begin monitoring a background event
such as a sports game, you can begin posting Live Updates for that event.
However, you should also include an **Unpin** action in the associated
notification.

### Time Sensitive

Show a Live Update only if it requires the user's attention throughout the
activity. A key use case for Live Updates is monitoring, when the user gets
significant benefit from glancing at the Live Update to keep an eye on the
evolving state of the activity.

A Live Update is often appropriate for activities that transition between Live
Updates and normal notifications. For example, showing a boarding pass
notification is appropriate many hours before a user's flight, but the
notification should become a Live Update only when the user has a pressing need,
such as when they have arrived at the airport or venue or once boarding has
begun. In contrast, a Live Update isn't appropriate for tracking a package as
the user doesn't need to constantly monitor this.

## UX practices

Use the tracker icon to visualize the state. This can be updated dynamically.
![custom tracker on a progress bar](https://developer.android.com/static/images/ui/notifications/notifications-live-progress-tracker.webp)
**Figure 3.** A custom tracker icon.

When an ETA is calculating or not yet available, don't show an empty state.
Provide active placeholder text such as "Thinking..." or "Rerouting..." to
indicate the system is working, as shown in the example.
![nonempty state to show thinking](https://developer.android.com/static/images/ui/notifications/notifications-live-progress-ambiguous.webp)
**Figure 4.** A nonempty state for your live update.

## Status Chips

Status chips allow users to keep track of Live Updates when the notification is
not in view. Use [
`setShortCriticalText`](https://developer.android.com/reference/android/app/Notification.Builder#setShortCriticalText(java.lang.String))
or [
`setWhen`](https://developer.android.com/reference/android/app/Notification.Builder#setWhen(long))
to convey
important state information regarding your progress centric notification.
![status chip with icon](https://developer.android.com/static/images/ui/notifications/status-chip-1.png)
**Figure 5.** Indeterminate state displays the small icon,
`Notification.Builder#setSmallIcon`. ![status chip with time](https://developer.android.com/static/images/ui/notifications/status-chip-2.png)
**Figure 6.** Use `Notification.Builder#setShortCriticalText` to show absolute
time. ![status chip with info](https://developer.android.com/static/images/ui/notifications/status-chip-3.png)
**Figure 7.** Use `Notification.Builder#setShortCriticalText` to convey critical information.

### When time

The when time triggers a countdown for the longevity of the notification, unless
the notification is dismissed or updated. The following bullets describe how
when time works in various situations:

- The when time is at least 2 minutes in the future: If the current time is 10:05 am and the when
  time is set to 10:10 am, then the chip says **5min**.
- The when time is in the past: The text isn't shown.
- A timer in the chip can be shown when using Chronometer for when time. See [
  `setUsesChronometer`](https://developer.android.com/reference/android/app/Notification.Builder#setUsesChronometer(boolean))
  and [
  `setChronometerCountdown`](https://developer.android.com/reference/android/app/Notification.Builder#setChronometerCountDown(boolean)).
  The Chronometer timer is shown in the chip as long as it is positive.
- You don't want the when time to show on your notification: Use [
  `setShowWhen`](https://developer.android.com/reference/android/app/Notification.Builder#setShowWhen(boolean))
  to `FALSE`.

### Status chip appearance

The status chip always includes an icon, and optionally includes text. The chip
has a maximum width of 96dp. The text will show only if the entire text can fit
in the chip. The text displays depending on the following criteria:

- If less than 7 characters, show the whole text.
- If less than half of the text will display, show the icon only.
- If more than half of the text will display, show as much text as possible.

### Wear OS

Live Updates are bridged to some connected Wear OS devices. For guidance on this
bridging behavior, as well as guidance on how to create local Live Updates for
Wear OS, see
the [Wear OS Live Update guide](https://developer.android.com/training/wearables/notifications/live-updates).

## Dismissal

Users can control notification visibility in the notification shade. Posting
unwanted Live Updates might cause users to revoke an app's posting permission.

To prevent users from completely disabling Live Updates, avoid posting updates
that users might dismiss. Don't repost Live Updates that the user dismissed. Use
`setDeleteIntent` to detect dismissed updates.

See
the [sample app](https://github.com/android/platform-samples/tree/main/samples/user-interface/live-updates)
to experiment with these APIs.

## FAQ

**Question:** What is the white dot at the end of the progress bar?
![end of progress accessibility visualization indicator](https://developer.android.com/static/images/ui/notifications/end-of-progress.png)
**Figure 8.** End of journey accessibility visualization

**Answer:** The white dot at the end of the progress bar
visually flags the end of the progress bar.

**Question:** Why aren't custom notifications supported for Live Updates?

**Answer:** Custom notifications make consistent testing and UX difficult
as their behavior differs significantly across Android versions and device
manufacturers. Avoid custom notifications using `RemoteViews`.