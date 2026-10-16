Android 13 (API level 33) and higher supports
a [runtime permission](https://developer.android.com/guide/topics/permissions/overview#runtime) for
sending [non-exempt](https://developer.android.com/develop/ui/compose/notifications/notification-permission#exemptions) (
including Foreground Services (FGS)) notifications from
an app: [
`POST_NOTIFICATIONS`](https://developer.android.com/reference/android/Manifest.permission#POST_NOTIFICATIONS).
This change helps users focus on the
notifications that are most important to them.

We highly recommend that you target Android 13 or higher as soon
as possible to benefit from the additional control and flexibility of this
feature. If you continue to target 12L (API level 32) or lower, you
lose some flexibility with [requesting the permission in the context of your
app's functionality](https://developer.android.com/develop/ui/compose/notifications/notification-permission#request-in-context).

> [!NOTE]
> **Note:** Apps don't need to request the `POST_NOTIFICATIONS` permission in order to launch a
> foreground service. However, apps must include a notification when they start a foreground service,
> just as they do on previous versions of Android.

## Declare the permission

To request the new notification permission from your app, update your app to
target Android 13 and complete a similar process compared to
[requesting other runtime permissions](https://developer.android.com/training/permissions/requesting),
as shown in the following sections.

The permission that you need
to [declare in your app's manifest file](https://developer.android.com/training/permissions/declaring)
appears
in the following code snippet:

```xml

<manifest ...><uses-permission
android:name="android.permission.POST_NOTIFICATIONS" /><application ...>...</application></manifest>
```

## App capabilities depend on user choice in permissions dialog

In this dialog, users have the following actions available to them:

- [Select **allow
  **](https://developer.android.com/develop/ui/compose/notifications/notification-permission#user-select-allow)
- [Select **don't allow
  **](https://developer.android.com/develop/ui/compose/notifications/notification-permission#user-select-dont-allow)
- [Swipe away from the dialog](https://developer.android.com/develop/ui/compose/notifications/notification-permission#user-swipe-away),
  without pressing either button

The following sections describe how your app behaves, based on which action the
user takes.

### User selects "Allow"

If the user selects the **allow** option, your app can do the following:

- Send notifications.
  All [notification channels](https://developer.android.com/develop/ui/compose/notifications/channels)
  are allowed.
-
Post [notifications related to foreground services](https://developer.android.com/develop/ui/compose/notifications#foreground-service).
These notifications appear in
the [notification drawer](https://developer.android.com/develop/ui/compose/notifications#bar-and-drawer).

### User selects "Don't allow"

If the user selects the **don't allow** option, your app can't send
notifications unless it qualifies for
an [exemption](https://developer.android.com/develop/ui/compose/notifications/notification-permission#exemptions).
All notification
channels are blocked, except for a few specific roles. This is similar to the
behavior that occurs when the user manually turns off all notifications for your
app in system settings.

> [!CAUTION]
> **Caution:** If your app targets 12L or lower and the user taps **Don't allow**, even just once,
> they aren't prompted again until they uninstall and reinstall your app, or you update your app to
> target Android 13 or higher.

### User swipes away from dialog

If the user swipes away from the dialog---that is, they don't select either
**allow** or **don't allow**---the state of the notification permission doesn't
change.

## Effects on newly-installed apps

If a user installs your app on a device that runs Android 13 or
higher, your app's **notifications are off by default**. Your app must wait to
send notifications until after you request the new permission and the user
grants that permission to your app.

The time at which the permissions dialog appears is based on your app's target
SDK version:

- If your app targets Android 13 or higher, your app has complete control over when the permission
  dialog is displayed. Use this opportunity to explain to users why the app needs this permission,
  encouraging them to grant it.
- If your app targets 12L (API level 32) or lower, the system shows the permission dialog the first
  time your app starts an activity after you create
  a [notification channel](https://developer.android.com/develop/ui/compose/notifications/channels),
  or when your app starts an activity and then creates its first notification channel. This is
  usually on app startup. In single-activity apps, such as most Compose apps, the system shows the
  dialog when the `MainActivity` starts.

## Effects on updates to existing apps

To minimize disruptions associated with the notification permission, the system
automatically pre-grants the permission to
all [eligible apps](https://developer.android.com/develop/ui/compose/notifications/notification-permission#eligibility)
when the user
upgrades their device to Android 13 or higher. In other words,
these apps can continue to send notifications to users, and users don't see a
runtime permission prompt.

> [!NOTE]
> **Note:** Consider the case where an eligible app was installed on a device running 12L or lower
> that the user is discarding, and the user allowed notifications on that old device. The user now has
> a new device that runs Android 13 or higher and restores the app using the [backup
> and restore](https://support.google.com/android/answer/2819582) feature.
>
> In this situation, the system considers your app to be an "existing app," so the system
> automatically grants the permission to your app so that your app can continue to send notifications.

## Eligibility for permission pre-grant

For your app to be *eligible* for an automatic pre-grant, it must have an
existing notification channel and not have its notifications explicitly disabled
by the user on a device that runs 12L or lower.

If the user disabled notifications for your app on a device that runs
12L or lower, that denial persists when the device
upgrades to Android 13 or higher.

## Exemptions

This section contains the set of notifications and apps that are exempt from the
notification permission behavior change. On Android 13 (API level 33) or higher,
if the user denies the notification permission, they still see notices related
to foreground services in
the [Task Manager](https://developer.android.com/develop/background-work/services/fgs/handle-user-stopping)
but don't see them in the
[notification drawer](https://developer.android.com/develop/ui/compose/notifications#bar-and-drawer).

### Media sessions

Notifications related
to [media sessions](https://developer.android.com/media/media3/session/control-playback) are exempt
from this behavior
change.

### Apps configured to self-manage phone calls

If your app configures itself to self-manage phone calls, you don't need the
`POST_NOTIFICATIONS` permission in order for your app to send notifications that
use the [
`Notification.CallStyle`](https://developer.android.com/reference/android/app/Notification.CallStyle)
notification style.

The system considers your app to have configured itself for self-managing phone
calls if it does each of the following:

1. Declares the [
   `MANAGE_OWN_CALLS`](https://developer.android.com/reference/android/Manifest.permission#MANAGE_OWN_CALLS)
   permission.
2. Implements the [
   `ConnectionService`](https://developer.android.com/reference/android/telecom/ConnectionService)
   interface.
3. Registers with the device's telecom provider by calling [
   `registerPhoneAccount()`](https://developer.android.com/reference/android/telecom/TelecomManager#registerPhoneAccount(android.telecom.PhoneAccount)).

## Test your app

You can evaluate how the notification permission affects your app when it's
first used on a device that runs Android 13 or higher. The
following sets
of [Android Debug Bridge (ADB)](https://developer.android.com/studio/command-line/adb) commands
allow you to
simulate the most common sequences of user choices and device upgrades without
needing to reset your test device:

- App is newly installed on a device that runs Android 13 or
  higher:

      adb shell pm revoke PACKAGE_NAME android.permission.POST_NOTIFICATIONS
      adb shell pm clear-permission-flags PACKAGE_NAME \
        android.permission.POST_NOTIFICATIONS user-set
      adb shell pm clear-permission-flags PACKAGE_NAME \
        android.permission.POST_NOTIFICATIONS user-fixed

- The user keeps notifications enabled when the app is installed on a device
  that runs 12L or lower, then the device upgrades to
  Android 13 or higher:

      adb shell pm grant PACKAGE_NAME android.permission.POST_NOTIFICATIONS
      adb shell pm set-permission-flags PACKAGE_NAME \
        android.permission.POST_NOTIFICATIONS user-set
      adb shell pm clear-permission-flags PACKAGE_NAME \
        android.permission.POST_NOTIFICATIONS user-fixed

- The user manually disables notifications when the app is installed on a
  device that runs 12L or lower, then the device
  upgrades to Android 13 or higher:

      adb shell pm revoke PACKAGE_NAME android.permission.POST_NOTIFICATIONS
      adb shell pm set-permission-flags PACKAGE_NAME \
        android.permission.POST_NOTIFICATIONS user-set
      adb shell pm clear-permission-flags PACKAGE_NAME \
        android.permission.POST_NOTIFICATIONS user-fixed

## Best practices

This section describes several ways that you can use the new notification
permission most effectively in your app.

### Update your app's target SDK version

To give your app more flexibility over when the permission dialog appears,
update your app so that it targets Android 13 or higher.

### Wait to show notification permission prompt

Before you ask users to grant any permissions, let them familiarize themselves
with your app.

New users may want to explore the app and realize first-hand the benefits of
each individual notification request. You
can [trigger a permissions prompt](https://developer.android.com/training/permissions/requesting#request-permission)
from a user action. The following list shows several examples of when it's a
good time to show the notification permission prompt:

- The user taps an "alert bell" button.
- The user chooses to follow someone's social media account.
- The user submits an order for food delivery.

Figure 1 shows a recommended workflow for requesting the notification
permission. Unless [
`shouldShowRequestPermissionRationale()`](https://developer.android.com/reference/androidx/core/app/ActivityCompat#shouldShowRequestPermissionRationale(android.app.Activity,%20java.lang.String))
returns
`true`, your app doesn't need to display the middle screen---the one that has the
title text "Get notified!". In Compose, you can find the current `Activity` by
accessing [
`LocalContext.current`](https://developer.android.com/reference/kotlin/androidx/compose/ui/platform/package-summary#LocalContext())
and casting it to an `Activity`.

Alternatively, you can set a request to appear after you give users a chance to
familiarize themselves with your app. For example, you might wait until the
third or fourth time the user launches your app.
![After the user signs in, they're presented with an
invitation to get notified of trip updates. After the user presses the
I'm in button, the app requests the new permission, which causes the system
dialog to appear](https://developer.android.com/static/images/permissions/notification-permission-a13-recommended-flow.svg)
**Figure 1.** A recommended user-driven workflow for requesting the notification permission. The
middle screen is necessary only if `shouldShowRequestPermissionRationale()` returns `true`.

## Request the permission

To request the permission within your Compose UI, use the
[
`rememberLauncherForActivityResult()`](https://developer.android.com/reference/kotlin/androidx/activity/compose/package-summary#rememberLauncherForActivityResult(androidx.activity.result.contract.ActivityResultContract,kotlin.Function1))
API with the
[
`ActivityResultContracts.RequestPermission()`](https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.RequestPermission)
contract.

The following snippet shows how to request the notification permission in
response to a user interaction:

### Kotlin

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, you can now send notifications.
        } else {
            // Permission denied, handle accordingly.
        }
    }

    // ... in your UI ...
    Button(onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }) {
        Text("Enable Notifications")
    }

### Request the permission in context

When you request notification permissions within your app, do so in the correct
context, so that it's explicitly clear what the notifications are used for and
why the user should opt in. For example, an email app might include options to
send notifications for every new email, or only the emails where the user is the
only recipient.

Use this opportunity to give transparency to your intentions, and users are more
likely to grant the notification permission to your app.

### Check whether your app can send notifications

Before your app sends a notification, confirm whether the user has enabled
notifications for your app. To do so, call [
`areNotificationsEnabled()`](https://developer.android.com/reference/android/app/NotificationManager#areNotificationsEnabled()).

### Use the permission responsibly

After you receive approval to send notifications, remember to use the permission
responsibly. Users can see the number of daily notifications that your app
sends,
and [they can revoke the permission](https://developer.android.com/training/permissions/requesting#handle-denial)
at any time.