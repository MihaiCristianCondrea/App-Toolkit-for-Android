Starting in Android 8.0 (API level 26), all notifications must be assigned to a
channel. For each channel, you can set the visual and auditory behavior that is
applied to all notifications in that channel. Users can change these settings
and decide which notification channels from your app can be intrusive or
visible.

Check out the following video for an overview of channels and other notification
features in Android 8.0.
[Video](https://www.youtube.com/watch?v=zGIw4MIJn5o)

The user settings for notification channels are available for each app in the
system settings, as shown in figure 1.
![](https://developer.android.com/static/images/ui/notifications/channel-settings_2x.png) Figure 1.
Notification settings for the Clock app and one of its channels.

> [!NOTE]
> **Note:** The user interface refers to notification channels as "categories."

After you create a notification channel, you can't change the notification
behaviors. The user has complete control at that point. However, you can still
change a channel's name and description.

Create a channel for each type of notification you need to send. You can also
create notification channels to reflect choices made by users. For example, you
can set up separate notification channels for each conversation group created by
a user in a messaging app.

> [!CAUTION]
> **Caution:** If you target Android 8.0 (API level 26) or higher and post a notification without
> specifying a notification channel, the notification doesn't appear and the system logs an error.

When you target Android 8.0 (API level 26) or higher, you must implement one or
more notification channels. If your `targetSdkVersion` is set to 25 or lower,
when your app runs on Android 8.0 (API level 26) or higher, it behaves the same
as on devices running Android 7.1 (API level 25) or lower.

> [!NOTE]
> **Note:** As of Android 8.0 (API level 26), you can turn on a setting on your development device
> to display an on-screen warning that appears as
> a [toast](https://developer.android.com/guide/topics/ui/notifiers/toasts) when an app targeting
> Android 8.0 (API level 26) or higher attempts to post without a notification channel. To turn on the
> setting for a development device running Android 8.0 (API level 26) or higher, navigate to *
*Settings** \> **Developer options** and enable **Show notification channel warnings**.

## Create a notification channel

To create a notification channel, follow these steps:

1. Construct a [
   `NotificationChannel`](https://developer.android.com/reference/android/app/NotificationChannel)
   object with a unique channel ID,
   user-visible name, and importance level.

2. Optionally, specify the description that the user sees in the system settings
   with [
   `setDescription()`](https://developer.android.com/reference/android/app/NotificationChannel#setDescription(java.lang.String)).

3. Register the notification channel by passing it to
   [
   `createNotificationChannel()`](https://developer.android.com/reference/android/app/NotificationManager#createNotificationChannel(android.app.NotificationChannel)).

> [!CAUTION]
> **Caution:** Guard this code with a condition on the [
`SDK_INT`](https://developer.android.com/reference/android/os/Build.VERSION#SDK_INT) version to run
> only on Android 8.0 (API level 26) and higher, because the notification channels APIs aren't
> available in the Support Library.

The following example shows how to create and register a notification channel:

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    // Create the NotificationChannel.
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
    mChannel.description = descriptionText
    // Register the channel with the system. You can't change the importance
    // or other notification behaviors after this.
    val notificationManager =
        context.getSystemService(NotificationManager::class.java)
    notificationManager?.createNotificationChannel(mChannel)
}
```

<br />

Recreating an existing notification channel with its original values performs no
operation, so it's safe to call this code when starting an app.

By default, all notifications posted to a given channel use the visual and
auditory behaviors defined by the importance level from the
[
`NotificationManagerCompat`](https://developer.android.com/reference/androidx/core/app/NotificationManagerCompat)
class, such as [
`IMPORTANCE_DEFAULT`](https://developer.android.com/reference/androidx/core/app/NotificationManagerCompat#IMPORTANCE_DEFAULT())
or
[
`IMPORTANCE_HIGH`](https://developer.android.com/reference/androidx/core/app/NotificationManagerCompat#IMPORTANCE_HIGH()).
See the next section for more information about
[importance levels](https://developer.android.com/develop/ui/compose/notifications/channels#importance).

If you want to further customize your channel's default notification behaviors,
you can call methods such as [
`enableLights()`](https://developer.android.com/reference/android/app/NotificationChannel#enableLights(boolean)), [
`setLightColor()`](https://developer.android.com/reference/android/app/NotificationChannel#setLightColor(int)),
and [
`setVibrationPattern()`](https://developer.android.com/reference/android/app/NotificationChannel#setVibrationPattern(long%5B%5D))
on the `NotificationChannel`. Remember that
once you create the channel, you can't change these settings, and the user has
final control over whether these behaviors are active.

You can also create multiple notification channels in a single operation by
calling [
`createNotificationChannels()`](https://developer.android.com/reference/android/app/NotificationManager#createNotificationChannels(java.util.List%3Candroid.app.NotificationChannel%3E)).

> [!NOTE]
> **Note:** In addition to adding each notification to your app-specific channels, consider adding
> each notification to one of the system-wide categories, such as [
`CATEGORY_ALARM`](https://developer.android.com/reference/androidx/core/app/NotificationCompat#CATEGORY_ALARM())
> or [
`CATEGORY_REMINDER`](https://developer.android.com/reference/androidx/core/app/NotificationCompat#CATEGORY_REMINDER()).

### Set the importance level

Channel importance affects the interruption level of all notifications posted in
the channel. Specify it in the `NotificationChannel` constructor, using one of
five importance levels, ranging from [
`IMPORTANCE_NONE(0)`](https://developer.android.com/reference/android/app/NotificationManager#IMPORTANCE_NONE)
to
[
`IMPORTANCE_HIGH(4)`](https://developer.android.com/reference/android/app/NotificationManager#IMPORTANCE_HIGH).

To support devices running Android 7.1 (API level 25) or lower, you must also
call [
`setPriority()`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setPriority(int))
for each notification, using a priority constant from
the [
`NotificationCompat`](https://developer.android.com/reference/androidx/core/app/NotificationCompat)
class.

The importance (`NotificationManager.IMPORTANCE_*`) and priority
(`NotificationCompat.PRIORITY_*`) constants map to the user-visible importance
options, as shown in the following table.

| User-visible importance level                                          | Importance (Android 8.0 and higher)                                                                                | Priority (Android 7.1 and lower)                                                                                                                                                                                                 |
|------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Urgent** Makes a sound and appears as a heads-up notification.       | [`IMPORTANCE_HIGH`](https://developer.android.com/reference/android/app/NotificationManager#IMPORTANCE_HIGH)       | [`PRIORITY_HIGH`](https://developer.android.com/reference/androidx/core/app/NotificationCompat#PRIORITY_HIGH()) or [`PRIORITY_MAX`](https://developer.android.com/reference/androidx/core/app/NotificationCompat#PRIORITY_MAX()) |
| **High** Makes a sound.                                                | [`IMPORTANCE_DEFAULT`](https://developer.android.com/reference/android/app/NotificationManager#IMPORTANCE_DEFAULT) | [`PRIORITY_DEFAULT`](https://developer.android.com/reference/androidx/core/app/NotificationCompat#PRIORITY_DEFAULT())                                                                                                            |
| **Medium** Makes no sound.                                             | [`IMPORTANCE_LOW`](https://developer.android.com/reference/android/app/NotificationManager#IMPORTANCE_LOW)         | [`PRIORITY_LOW`](https://developer.android.com/reference/androidx/core/app/NotificationCompat#PRIORITY_LOW())                                                                                                                    |
| **Low** Makes no sound and doesn't appear in the status bar.           | [`IMPORTANCE_MIN`](https://developer.android.com/reference/android/app/NotificationManager#IMPORTANCE_MIN)         | [`PRIORITY_MIN`](https://developer.android.com/reference/androidx/core/app/NotificationCompat#PRIORITY_MIN())                                                                                                                    |
| **None** Makes no sound and doesn't appear in the status bar or shade. | [`IMPORTANCE_NONE`](https://developer.android.com/reference/android/app/NotificationManager#IMPORTANCE_NONE)       | `N/A`                                                                                                                                                                                                                            |

All notifications, regardless of importance, appear in non-interruptive system
UI locations, such as in the notification drawer and as
[a badge on the launcher icon](https://developer.android.com/develop/ui/compose/notifications#icon-badge),
though you can
[modify the appearance of the notification badge](https://developer.android.com/develop/ui/views/notifications/badges).

Once you submit the channel to the [
`NotificationManager`](https://developer.android.com/reference/android/app/NotificationManager), you
can't change
the importance level. However, the user can change their preferences for your
app's channels at any time.

For information about choosing an appropriate priority level, see "Priority
levels" in
the [Notifications design guide](https://material.io/design/platform-guidance/android-notifications.html#settings).

## Read notification channel settings

Users can modify the settings for notification channels, including behaviors
such as vibration and alert sound. If you want to know the settings a user
applies to your notification channels, follow these steps:

1. Get the `NotificationChannel` object by calling
   [
   `getNotificationChannel()`](https://developer.android.com/reference/android/app/NotificationManager#getNotificationChannel(java.lang.String))
   or [
   `getNotificationChannels()`](https://developer.android.com/reference/android/app/NotificationManager#getNotificationChannels()).

2. Query specific channel settings such as [
   `getVibrationPattern()`](https://developer.android.com/reference/android/app/NotificationChannel#getVibrationPattern()),
   [
   `getSound()`](https://developer.android.com/reference/android/app/NotificationChannel#getSound()),
   and [
   `getImportance()`](https://developer.android.com/reference/android/app/NotificationChannel#getImportance()).

If you detect a channel setting that you believe inhibits the intended behavior
for your app, you can suggest that the user change it and provide an action to
open the channel settings, as shown in the next section.

## Open the notification channel settings

After you create a notification channel, you can't change the notification
channel's visual and auditory behaviors programmatically. Only the user can
change the channel behaviors from the system settings. To provide your users
quick access to these notification settings, add an item in your app's
settings UI that opens these system settings.

You can open the system settings for notification channels with an
[`Intent`](https://developer.android.com/reference/android/content/Intent) that uses the [
`ACTION_CHANNEL_NOTIFICATION_SETTINGS`](https://developer.android.com/reference/android/provider/Settings#ACTION_CHANNEL_NOTIFICATION_SETTINGS)
action.

For example, the following sample code shows how you can redirect a user to the
settings for a notification channel:

```kotlin
val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
}
Button(onClick = {
    context.startActivity(intent)
}) {
    Text("Open Channel Settings")
}
```

<br />

Notice that the intent requires two extras that specify your app's package name
(also known as the application ID) and the channel to edit.

## Delete a notification channel

You can delete notification channels by calling
[
`deleteNotificationChannel()`](https://developer.android.com/reference/android/app/NotificationManager#deleteNotificationChannel(java.lang.String)).
The following sample code demonstrates how to complete this process:

```kotlin
val notificationManager =
    ContextCompat.getSystemService<NotificationManager>(context, NotificationManager::class.java)
notificationManager?.deleteNotificationChannel(channelId)
```

<br />

> [!NOTE]
> **Note:** The notification settings screen displays the number of deleted channels, as a spam
> prevention mechanism. You can clear test channels on development devices by reinstalling the app or
> clearing the data associated with your copy of the app.

## Create a notification channel group

If you want to further organize the appearance of your channels in the
settings UI, you can create channel groups. This is a good idea when your app
supports multiple user accounts because it lets you create a notification
channel group for each account. Channel groups help users differentiate and
control multiple notification channels that have identical names.
![](https://developer.android.com/static/images/ui/notifications/channel-groups_2x.png) Figure 2.
Notification channel settings with groups for personal and work accounts.

For example, a social networking app might include support for personal and work
accounts. In this scenario, each account might require multiple notification
channels with identical functions and names, such as the following:

- A personal account with two channels:

    - New comments

    - Post recommendations

- A business account with two channels:

    - New comments

    - Post recommendations

Organizing the notification channels into groups for each account lets users
distinguish between them.

Each notification channel group requires an ID, which must be unique within your
package, as well as a user-visible name. The following snippet demonstrates how
to create a notification channel group.

```kotlin
fun createNotificationChannelGroup(context: Context, groupId: String, groupName: String) {
    val notificationManager =
        ContextCompat.getSystemService(context, NotificationManager::class.java)
    notificationManager?.createNotificationChannelGroup(
        NotificationChannelGroup(
            groupId,
            groupName
        )
    )
}
```

<br />

After you create a new group, you can call
[
`setGroup()`](https://developer.android.com/reference/android/app/NotificationChannel#setGroup(java.lang.String))
to associate a new `NotificationChannel` object with the group.

Once you submit the channel to the notification manager, you can't change the
association between notification channel and group.