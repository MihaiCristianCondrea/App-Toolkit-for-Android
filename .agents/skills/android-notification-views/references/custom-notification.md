To make your notifications look their best across different versions of Android,
use the [standard notification
template](https://developer.android.com/training/notify-user/build-notification) to build your
notifications. If you want to provide more content in your notification,
consider using one of the [expandable notification
templates](https://developer.android.com/training/notify-user/expanded).

However, if the system templates don't meet your needs, you can use your own
layout for the notification.

> [!CAUTION]
> **Caution:** When using a custom notification layout, take special care to ensure your custom
> layout works with different device orientations and resolutions. Although this advice applies to all
> UI layouts, it's especially important for notifications because the space in the notification drawer
> is restricted. The height available for a custom notification layout depends on the Android version.
> On some versions, collapsed view layouts are limited to as little as 48 dp, heads-up view layouts
> are limited to as little as 88 dp, and expanded view layouts are limited to as little as 252 dp.

## Create custom layout for the content area

If you need a custom layout, you can apply
[
`NotificationCompat.DecoratedCustomViewStyle`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.DecoratedCustomViewStyle)
to your notification. This API lets you provide a custom layout for the content
area normally occupied by the title and text content, while still using system
decorations for the notification icon, timestamp, sub-text, and action buttons.

This API works similarly to
the [expandable notification templates](https://developer.android.com/training/notify-user/expanded)
by building on the basic notification
layout as follows:

1. Build
   a [basic notification](https://developer.android.com/training/notify-user/build-notification)
   with [
   `NotificationCompat.Builder`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder).
2. Call [
   `setStyle()`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setStyle(androidx.core.app.NotificationCompat.Style)),
   passing it an instance of [
   `NotificationCompat.DecoratedCustomViewStyle`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.DecoratedCustomViewStyle).
3. Inflate your custom layout as an instance of [
   `RemoteViews`](https://developer.android.com/reference/android/widget/RemoteViews).
4. Call [
   `setCustomContentView()`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setCustomContentView(android.widget.RemoteViews))
   to set the layout for the collapsed notification.
5. Optionally, also call [
   `setCustomBigContentView()`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setCustomBigContentView(android.widget.RemoteViews))
   to set a different layout for the expanded notification.

> [!NOTE]
> **Note:** If you're creating a customized notification for media playback controls, follow the
> same recommendations but use the `NotificationCompat.DecoratedMediaCustomViewStyle` class instead.

### Prepare the layouts

You need a `small` and `large` layout. For this example, the `small` layout
might look like this:

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <TextView
            android:id="@+id/notification_title"
            style="@style/TextAppearance.Compat.Notification.Title"
            android:layout_width="wrap_content"
            android:layout_height="0dp"
            android:layout_weight="1"
            android:text="Small notification, showing only a title" />
    </LinearLayout>

And the `large` layout might look like this:

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:orientation="vertical">

        <TextView
            android:id="@+id/notification_title"
            style="@style/TextAppearance.Compat.Notification.Title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="Large notification, showing a title and a body." />

        <TextView
            android:id="@+id/notification_body"
            style="@style/TextAppearance.Compat.Notification.Line2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="This is the body. The height is manually forced to 300dp." />
    </LinearLayout>

### Build and show the notification

After the layouts are ready, you can use them as shown in the following example:

### Kotlin

```kotlin
val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

// Get the layouts to use in the custom notification.
val notificationLayout = RemoteViews(packageName, R.layout.notification_small)
val notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_large)

// Apply the layouts to the notification.
val customNotification = NotificationCompat.Builder(context, CHANNEL_ID)
    .setSmallIcon(R.drawable.notification_icon)
    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
    .setCustomContentView(notificationLayout)
    .setCustomBigContentView(notificationLayoutExpanded)
    .build()

notificationManager.notify(666, customNotification)
```

### Java

```java
NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// Get the layouts to use in the custom notification
RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);

// Apply the layouts to the notification.
Notification customNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(notificationLayout)
        .setCustomBigContentView(notificationLayoutExpanded)
        .build();

notificationManager.

notify(666,customNotification);
```

Be aware that the background color for the notification can vary across devices
and versions. Apply Support Library styles such as
`TextAppearance_Compat_Notification` for the text and
`TextAppearance_Compat_Notification_Title` for the title in your custom layout,
as shown in the following example. These styles adapt to the color variations so
you don't end up with black-on-black or white-on-white text.

```xml

<TextView android:layout_width="wrap_content" android:layout_height="match_parent"
    android:layout_weight="1" android:text="@string/notification_title"
    android:id="@+id/notification_title" style="@style/TextAppearance.Compat.Notification.Title" />
```

Avoid setting a background image on your `RemoteViews` object, because your text
might become unreadable.

When you trigger a notification while the user is using an app, the result is
similar to figure 1:
![An image showing a collapsed notification](https://developer.android.com/static/images/ui/notifications/custom_notification_small_inapp.png)
**Figure 1.** A small notification layout appears while using other apps.

Tapping the expander arrow expands the notification, as shown in figure 2:
![An image showing an expanded notification in the system bar](https://developer.android.com/static/images/ui/notifications/custom_notification_large_inapp.png)
**Figure 2.** A large notification layout appears while using other apps.

After the notification timeout runs out, the notification is visible only in the
system bar, which looks like figure 3:
![An image showing a collapsed notification in the system bar](https://developer.android.com/static/images/ui/notifications/custom_notification_small_system.png)
**Figure 3.** How the small notification layout appears in the system bar.

Tapping the expander arrow expands the notification, as shown in figure 4:
![An image showing an expanded notification in the system bar](https://developer.android.com/static/images/ui/notifications/custom_notification_large_system.png)
**Figure 4.** A large notification layout appears in the system bar.

## Create a fully custom notification layout

> [!NOTE]
> **Note:** Apps targeting [Android
> 12](https://developer.android.com/about/versions/12/behavior-changes-12#custom-notifications) (API
> level 31) or later can't create fully custom notifications. Instead, the system applies a standard
> template nearly identical to the behavior of [
`Notification.DecoratedCustomViewStyle`](https://developer.android.com/reference/android/app/Notification.DecoratedCustomViewStyle).

If you don't want your notification decorated with the standard notification
icon and header, follow the preceding steps but *don't* call `setStyle()`.

> [!CAUTION]
> **Caution:** We don't recommend using an undecorated notification as this doesn't match other
> notifications and can cause significant layout compatibility issues on devices that apply different
> styling to the notification area.