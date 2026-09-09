When you start an activity from a notification, you must preserve the user's
expected navigation experience. Tapping the Back button must take the user back
through the app's normal work flow to the Home screen, and opening the Recents
screen must show the activity as a separate task. To preserve this navigation
experience, start the activity in a fresh task.

The basic approach to set the tap behavior for your notification is described in
[Create a basic
notification](https://developer.android.com/develop/ui/views/notifications/build-notification#SimpleNotification).
This page describes how to set up a
[`PendingIntent`](https://developer.android.com/reference/android/app/PendingIntent) for your
notification's action so it creates a fresh [task and back
stack](https://developer.android.com/guide/components/activities/tasks-and-back-stack). How you do
this
depends on which type of activity you're starting:

Regular activity
:   This is an activity that exists as a part of your app's normal UX flow. When
the user arrives in the activity from the notification, the new task must
include a complete back stack, letting the user tap the Back button to navigate
up the app hierarchy.

Special activity
:   The user only sees this activity if it's started from a notification. In a
sense, this activity extends the notification UI by providing information that
is difficult to display in the notification itself. This activity doesn't need a
back stack.

## Set up a regular activity PendingIntent

To start a regular activity from your notification, set up the `PendingIntent`
using [
`TaskStackBuilder`](https://developer.android.com/reference/androidx/core/app/TaskStackBuilder)
so that it creates a new back stack as follows.

### Define your app's Activity hierarchy

Define the natural hierarchy for your activities by adding the
[
`android:parentActivityName`](https://developer.android.com/guide/topics/manifest/activity-element#parent)
attribute to each [
`<activity>`](https://developer.android.com/guide/topics/manifest/activity-element)
element in your app manifest file. See the following example:

```xml

<activity android:name=".MainActivity" android:label="@string/app_name">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity><!-- MainActivity is the parent for ResultActivity. -->
<activity android:name=".ResultActivity" android:parentActivityName=".MainActivity" />...</activity>
```

### Build a PendingIntent with a back stack

To start an activity that includes a back stack of activities, create an
instance of `TaskStackBuilder` and call
[
`addNextIntentWithParentStack()`](https://developer.android.com/reference/androidx/core/app/TaskStackBuilder#addNextIntentWithParentStack(android.content.Intent)),
passing it the [`Intent`](https://developer.android.com/reference/android/content/Intent) for the
activity you want to start.

As long as you define the parent activity for each activity as described
earlier, you can call
[
`getPendingIntent()`](https://developer.android.com/reference/androidx/core/app/TaskStackBuilder#getPendingIntent(int,int))
to receive a `PendingIntent` that includes the entire back stack.

### Kotlin

```kotlin
// Create an Intent for the activity you want to start.
val resultIntent = Intent(this, ResultActivity::class.java)
// Create the TaskStackBuilder.
val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
    // Add the intent, which inflates the back stack.
    addNextIntentWithParentStack(resultIntent)
    // Get the PendingIntent containing the entire back stack.
    getPendingIntent(
        0,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
```

### Java

```java
// Create an Intent for the activity you want to start.
Intent resultIntent = new Intent(this, ResultActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back
// stack.
TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
stackBuilder.

addNextIntentWithParentStack(resultIntent);

// Get the PendingIntent containing the entire back stack.
PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
```

If necessary, you can add arguments to `Intent` objects in the stack by calling
[
`TaskStackBuilder.editIntentAt()`](https://developer.android.com/reference/androidx/core/app/TaskStackBuilder#editIntentAt(int)).
This is sometimes necessary to ensure that an activity in the back stack
displays meaningful data when the user navigates to it.

Then you can pass the `PendingIntent` to the notification as usual:

### Kotlin

```kotlin
val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
    setContentIntent(resultPendingIntent)
    ...
}
with(NotificationManagerCompat.from(this)) {
    notify(NOTIFICATION_ID, builder.build())
}
```

### Java

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
builder.

setContentIntent(resultPendingIntent);
...
NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
notificationManager.

notify(NOTIFICATION_ID, builder.build());
```

## Set up a special activity PendingIntent

Because a special activity that starts from a notification doesn't need a back
stack, you can create the `PendingIntent` by calling
[
`getActivity()`](https://developer.android.com/reference/android/app/PendingIntent#getActivity(android.content.Context,%20int,%20android.content.Intent,%20int)).
However, define the appropriate task options in the manifest.

1. In your manifest, add the following attributes to the `<activity>` element.

`https://developer.android.com/guide/topics/manifest/activity-element#aff=""`
:
Combined with the
`https://developer.android.com/reference/android/content/Intent#FLAG_ACTIVITY_NEW_TASK`
flag that you use in code, set this attribute blank to ensure
this activity doesn't go into the app's default task. Any
existing tasks that have the app's default affinity aren't
affected.

`https://developer.android.com/guide/topics/manifest/activity-element#exclude="true"`
:
Excludes the new task from the Recents screen so that the user
can't accidentally navigate back to it.

This is shown in the following example:

   ```xml

<activity android:name=".ResultActivity" android:launchMode="singleTask" android:taskAffinity=""
    android:excludeFromRecents="true"></activity>
   ```

2. Build and issue the notification:
    1. Create an `Intent` that starts the
       `https://developer.android.com/reference/android/app/Activity`.
    2. Set the `Activity` to start in a new, empty task by calling
       `https://developer.android.com/reference/android/content/Intent#setFlags(int)` with the flags
       `FLAG_ACTIVITY_NEW_TASK` and
       `https://developer.android.com/reference/android/content/Intent#FLAG_ACTIVITY_CLEAR_TASK`.
    3. Create a `PendingIntent` by calling `getActivity()`.

This is shown in the following example:

### Kotlin

   ```kotlin
   val notifyIntent = Intent(this, ResultActivity::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}
val notifyPendingIntent = PendingIntent.getActivity(
    this, 0, notifyIntent,
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)
   ```

### Java

   ```java
   Intent notifyIntent = new Intent(this, ResultActivity.class);
// Set the Activity to start in a new, empty task.
   notifyIntent.

setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                       |Intent.FLAG_ACTIVITY_CLEAR_TASK);

// Create the PendingIntent.
PendingIntent notifyPendingIntent = PendingIntent.getActivity(
        this, 0, notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
);
   ```

3. Pass the `PendingIntent` to the notification as usual:

   ### Kotlin

   ```kotlin
   val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
       setContentIntent(notifyPendingIntent)
       ...
   }
   with(NotificationManagerCompat.from(this)) {
       notify(NOTIFICATION_ID, builder.build())
   }
   ```

   ### Java

   ```java
   NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
   builder.setContentIntent(notifyPendingIntent);
   ...
   NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
   notificationManager.notify(NOTIFICATION_ID, builder.build());
   ```

For more information about the various task options and how the back stack
works,
see [Tasks and the back stack](https://developer.android.com/guide/components/activities/tasks-and-back-stack).