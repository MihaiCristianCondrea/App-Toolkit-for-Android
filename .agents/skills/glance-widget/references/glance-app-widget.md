The following sections describe how to update `GlanceAppWidget` and manage its state.

## Manage `GlanceAppWidget` state

The provided `GlanceAppWidget` class is instantiated whenever the widget is created or requires an update, so it should be *stateless and passive*.

> [!IMPORTANT]
> **Key Point:** App widgets live in a different process. While the defined UI content (meaning the underlying `RemoteViews`) is restored by the system, any state kept in-memory, for example in the
> app's scope, can be destroyed at any time.

The concept of state can be divided into the following:

- **Application state**: The state or content of the app that is required by the widget. For example, a list of stored destinations (i.e., database) defined by the user.
- **Glance state**: The specific state that is only relevant to the app widget and does not necessarily modify or affect the app's state. For example, a checkbox was selected in the widget or a
  counter was increased.

### Use application state

App widgets should be passive. Each application is responsible for managing the data layer and handling the states, such as idle, loading, and error reflecting in the widget UI.

For example, the following code retrieves the destinations from the in-memory cache from the repository layer, provides the stored list of destinations, and displays a different UI depending on its
state:

<br />

```kotlin
class DestinationAppWidget: GlanceAppWidget() {

    // ...

    @Composable
    fun MyContent() {
        val repository = remember { DestinationsRepository.getInstance() }
        // Retrieve the cache data everytime the content is refreshed
        val destinations by repository.destinations.collectAsState(State.Loading)

        when (destinations) {
            is State.Loading -> {
                // show loading content
            }

            is State.Error -> {
                // show widget error content
            }

            is State.Completed -> {
                // show the list of destinations
            }
        }
    }
}

```

<br />

Whenever the state or the data changes, it is the app's responsibility to notify and update the widget.
See [Update GlanceAppWidget](https://developer.android.com/develop/ui/compose/glance/glance-app-widget#update-glance-appwidget) for more information.

> [!NOTE]
> **Note:** See the [Optimizations for updating widget content](https://developer.android.com/guide/topics/appwidgets/advanced#update-widgets) section in the app widgets guide to understand how and
> when to update.

## Update `GlanceAppWidget`

You can request to update your widget content using `GlanceAppWidget`. As explained in the [Manage
`GlanceAppWidget` state](https://developer.android.com/develop/ui/compose/glance/glance-app-widget#manage-state) section, app widgets are hosted in a different process. Glance translates the content
into actual `RemoteViews` and sends them to the host. To update the content, Glance must recreate the `RemoteViews` and send them again.

To send the update, call the `update` method of the `GlanceAppWidget` instance, providing the `context` and the `glanceId`:

<br />

```kotlin
MyAppWidget().update(context, glanceId)

```

<br />

To obtain the `glanceId`, query the `GlanceAppWidgetManager`:

<br />

```kotlin
val manager = GlanceAppWidgetManager(context)
val widget = GlanceSizeModeWidget()
val glanceIds = manager.getGlanceIds(widget.javaClass)
glanceIds.forEach { glanceId ->
    widget.update(context, glanceId)
}

```

<br />

Alternatively, use one of the `GlanceAppWidget update` extensions:

<br />

```kotlin
// Updates all placed instances of MyAppWidget
MyAppWidget().updateAll(context)

// Iterate over all placed instances of MyAppWidget and update if the state of
// the instance matches the given predicate
MyAppWidget().updateIf<State>(context) { state ->
    state == State.Completed
}

```

<br />

These methods can be called from any part of your application. Because they are `suspend` functions, we recommend launching them outside of the main thread scope. In the following example, they are
launched in a `CoroutineWorker`:

<br />

```kotlin
class DataSyncWorker(
    val context: Context,
    val params: WorkerParameters,
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Fetch data or do some work and then update all instance of your widget
        MyAppWidget().updateAll(context)
        return Result.success()
    }
}

```

<br />

See [Kotlin Coroutines on Android](https://developer.android.com/kotlin/coroutines) for more details on coroutines.

### When to update widgets

Update widgets either immediately or periodically.

Your widget can update immediately when your app is awake. For example:

- When a user interacts with a widget, triggering an action, a lambda call, or an intent to launch an activity.
- When your user interacts with your app in the foreground, or while the app is already updating in response to a Firebase Cloud Messaging (FCM) message or a broadcast.

In these cases, call the [`update`](https://developer.android.com/reference/kotlin/androidx/glance/appwidget/GlanceAppWidget#update(android.content.Context,androidx.glance.GlanceId)) method as
described in this guide.

Your widget can update periodically when your app isn't awake. For example:

- Use [`updatePeriodMillis`](https://developer.android.com/reference/android/appwidget/AppWidgetProviderInfo#updatePeriodMillis) to update the widget up to once every 30 minutes.
- Use `WorkManager` to schedule more frequent updates, such as every 15 minutes.
- Update the widget in response to a broadcast.

> [!IMPORTANT]
> **Important:** Avoid updating your widget every minute when the app isn't awake, as frequent updates drain your users' battery.

## Resources

- [Create a widget with Glance](https://developer.android.com/codelabs/glance) (Codelab)
- [Building for the Future of Android: Widgets chapter](https://www.youtube.com/watch?v=YKPqjsYBFvI&t=487s) (Video)