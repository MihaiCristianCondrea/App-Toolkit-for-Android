Starting with Android 8.0 (API level 26), notification badges---also known as
notification dots---appear on a launcher icon when the associated app has an
active notification. Users can
touch \& hold the app icon to reveal the notifications, along with any
[app shortcuts](https://developer.android.com/guide/topics/ui/shortcuts), as shown in
figure 1.

These dots appear by default in launcher apps that support them, and there's
nothing your app needs to do. However, there might be situations in which you
don't want the to notification dot to appear or you want to control exactly
which notifications appear there.

![](https://developer.android.com/static/images/ui/notifications/badges-open_2x.png)

**Figure 1.** Notification badges and the touch \& hold menu.

<br />

## Disable badging

There are cases where badges don't make sense for your notifications, so you
can disable them on a per-channel basis by calling
[
`setShowBadge(false)`](https://developer.android.com/reference/android/app/NotificationChannel#setShowBadge(boolean))
on your [
`NotificationChannel`](https://developer.android.com/reference/android/app/NotificationChannel)
object.

For example, you might want to disable notification badges in the following
situations:

- Ongoing notifications: most ongoing notifications, such as image processing, media playback
  controls, or current navigation instructions, don't make sense as a badge.
- Calendar reminders: avoid badging events occurring at the current time.
- Clock or alarm events: avoid badging notifications related to current alarms.

The following sample code demonstrates how to hide badges for
a notification channel:

### Kotlin

```kotlin
val id = "my_channel_01"
val name = getString(R.string.channel_name)
val descriptionText = getString(R.string.channel_description)
val importance = NotificationManager.IMPORTANCE_LOW
val mChannel = NotificationChannel(id, name, importance).apply {
    description = descriptionText
    setShowBadge(false)
}
val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
notificationManager.createNotificationChannel(mChannel)
```

### Java

```java
String id = "my_channel_01";
CharSequence name = getString(R.string.channel_name);
String description = getString(R.string.channel_description);
int importance = NotificationManager.IMPORTANCE_LOW;
NotificationChannel mChannel = new NotificationChannel(id, name, importance);
mChannel.

setDescription(description);
mChannel.

setShowBadge(false);

NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
notificationManager.

createNotificationChannel(mChannel);
```

## Set custom notification count

By default, each notification increments a number displayed on the touch \& hold
menu, as shown in figure 1, but you can override this number for your app.
For example, this might be useful if you're using just one notification to
represent multiple new messages but want the count to represent the
number of total new messages.

To set a custom number, call
[
`setNumber()`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setNumber(int))
on the notification, as shown here:

### Kotlin

```kotlin
var notification = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
    .setContentTitle("New Messages")
    .setContentText("You've received 3 new messages.")
    .setSmallIcon(R.drawable.ic_notify_status)
    .setNumber(messageCount)
    .build()
```

### Java

```java
Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
        .setContentTitle("New Messages")
        .setContentText("You've received 3 new messages.")
        .setSmallIcon(R.drawable.ic_notify_status)
        .setNumber(messageCount)
        .build();
```

## Modify a notification's touch \& hold menu icon

The touch \& hold menu displays the large or small icon associated with a
notification if available. By default, the system displays the large icon, but
you can call
[
`Notification.Builder.setBadgeIconType()`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setBadgeIconType(int))
and pass in the [
`BADGE_ICON_SMALL`](https://developer.android.com/reference/androidx/core/app/NotificationCompat#BADGE_ICON_SMALL())
constant to display the small icon.

### Kotlin

```kotlin
var notification = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
    .setContentTitle("New Messages")
    .setContentText("You've received 3 new messages.")
    .setSmallIcon(R.drawable.ic_notify_status)
    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
    .build()
```

### Java

```java
Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
        .setContentTitle("New Messages")
        .setContentText("You've received 3 new messages.")
        .setSmallIcon(R.drawable.ic_notify_status)
        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        .build();
```

## Hide a duplicate shortcut

If your app creates a notification that duplicates
an [app shortcut](https://developer.android.com/guide/topics/ui/shortcuts), you can
temporarily hide the shortcut while the notification is active by calling
[
`setShortcutId()`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setShortcutId(java.lang.String)).

For more sample code that uses notifications, see
the [SociaLite sample app](https://github.com/android/socialite).