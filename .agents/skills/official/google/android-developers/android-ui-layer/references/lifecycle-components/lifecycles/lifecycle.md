# Lifecycle in Jetpack Compose
Part of [Android Jetpack](https://developer.android.com/jetpack).

Lifecycle-aware components perform actions in response to a change in the
lifecycle status of the host activity. The
[`androidx.lifecycle.compose`](https://developer.android.com/reference/kotlin/androidx/lifecycle/compose/package-summary)
artifact provides dedicated APIs that automatically clean up resources when they
leave the screen or when the application goes into the background.

Key APIs include the following:

- [Flows](https://developer.android.com/kotlin/flow) for the current [`Lifecycle.State`](https://developer.android.com/reference/kotlin/androidx/lifecycle/Lifecycle.State).
- `LifecycleEffects`, which lets you run a block based on a specific [`Lifecycle.Event`](https://developer.android.com/reference/kotlin/androidx/lifecycle/Lifecycle.Event).

These integrations provide convenient hooks to manage lifecycles within the
Compose hierarchy. This document outlines how you can use them in your app.

> [!NOTE]
> **Note:** The APIs described in this document were introduced in [Lifecycle version 2.7.0](https://developer.android.com/jetpack/androidx/releases/lifecycle).

## Collect lifecycle state with flows

Lifecycle exposes a `currentStateFlow` property that provides the current
`Lifecycle.State` as a Kotlin `StateFlow`. You can collect this `Flow` as
`State`. This allows your app to read changes in the Lifecycle during
composition.

    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = lifecycleOwner.lifecycle.currentStateFlow
    ...
    val currentLifecycleState by stateFlow.collectAsState()

The preceding example is accessible using the `lifecycle-common` module. The
`currentStateAsState()` method is available in the `lifecycle-runtime-compose`
module, which lets you conveniently read the current Lifecycle state with a
single line. The following example demonstrates this:

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentLifecycleState = lifecycleOwner.lifecycle.currentStateAsState()

## Run code on lifecycle events

Instead of creating a separate class that implements
[`DefaultLifecycleObserver`](https://developer.android.com/reference/androidx/lifecycle/DefaultLifecycleObserver)
and manually adding it to the lifecycle, you can declare lifecycle logic inline
using specific effects. `LifecycleEffects` lets you run a block when a
particular `Lifecycle.Event` occurs directly within the composition.

### LifecycleEventEffect

Use [`LifecycleEventEffect`](https://developer.android.com/reference/kotlin/androidx/lifecycle/compose/package-summary#LifecycleEventEffect(androidx.lifecycle.Lifecycle.Event,androidx.lifecycle.LifecycleOwner,kotlin.Function0)) to run a block of code
when a specific event occurs. This is best for one-shot events like logging or
analytics, where neither success nor an immediate outcome is needed.

    @Composable
    fun AnalyticsTracker(screenName: String) {
        // Log an event when the app receives ON_RESUME (e.g. comes to foreground)
        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            Analytics.logView(screenName)
        }
    }

> [!WARNING]
> **Warning:** You can't use `LifecycleEventEffect` to listen for `Lifecycle.Event.ON_DESTROY` because the composition ends and the composable is disposed of before this signal is sent.

### LifecycleStartEffect

For paired start/stop operations that need to run while the app is started
(visible) and clean up when the app is stopped (in the background), use
[`LifecycleStartEffect`](https://developer.android.com/reference/kotlin/androidx/lifecycle/compose/package-summary#LifecycleStartEffect(kotlin.Any,androidx.lifecycle.LifecycleOwner,kotlin.Function1)).

Similar to other Compose effects (like `LaunchedEffect`), `LifecycleStartEffect`
accepts keys. When the key changes, it triggers the block to run again.

When a `Lifecycle.Event.ON_STOP` event occurs or the effect exits the
composition, it executes the `onStopOrDispose` block to clean up any work that
was part of the starting block.

    @Composable
    fun LocationMonitor(locationManager: LocationManager) {
        // Starts monitoring when ON_START is dispatched
        // Stops monitoring when ON_STOP is dispatched
        //   (or the composable leaves the screen)
        LifecycleStartEffect(locationManager) {
            val listener = LocationListener { location ->
                /* update UI */
            }
            locationManager.requestLocationUpdates(listener)
            // The cleanup block automatically runs on ON_STOP or on disposal
            onStopOrDispose {
                locationManager.removeUpdates(listener)
            }
        }
    }

> [!NOTE]
> **Note:** The `onStopOrDispose` block is always required for a `LifecycleStartEffect`. If your operation doesn't actually require cleanup, use `LifecycleEventEffect` instead, passing in `Lifecycle.Event.ON_START`.

For information about other types of side effects, see
[Side-effects in Compose](https://developer.android.com/develop/ui/compose/side-effects).

### LifecycleResumeEffect

The `LifecycleResumeEffect` works in the same way as the `LifecycleStartEffect`,
but it is tied to the `Lifecycle.Event.ON_RESUME` event instead. It also
provides an `onPauseOrDispose` block that performs the cleanup when `ON_PAUSE`
is dispatched or the composable leaves the screen.

This API is useful for resources that need to be active only when the user is
interacting with the app---for example, cameras or animations.

    @Composable
    fun CameraPreview(cameraController: CameraController) {
        LifecycleResumeEffect(cameraController) {
            cameraController.startPreview()

            onPauseOrDispose {
                cameraController.stopPreview()
            }
        }
    }

## Access the LifecycleOwner

In Compose, the
[`LifecycleOwner`](https://developer.android.com/reference/kotlin/androidx/lifecycle/LifecycleOwner) is
implicitly available through the `CompositionLocal` named `LocalLifecycleOwner`. By
default, the root host of your composition hierarchy provides this owner.

    val lifecycleOwner = LocalLifecycleOwner.current

For many apps, inspecting this default owner or passing it to lifecycle-aware
effects is sufficient. However, for custom navigation or complex layouts, you
might want to create your own `LifecycleOwner` to scope lifecycle states to
specific sections of the UI. For example, navigation libraries (like [Navigation
3](https://developer.android.com/guide/navigation/navigation-3)) do this automatically to give each
individual screen its own lifecycle.

### Create a custom LifecycleOwner

The `rememberLifecycleOwner()` API lets you create and remember a custom
`LifecycleOwner`. This is especially useful for components like a
`HorizontalPager`, where you want only the visible, settled page to be
`RESUMED`, while setting a `maxState` of `STARTED` for the adjacent, off-screen
pages.

    val pagerState = rememberPagerState(pageCount = { 10 })

    HorizontalPager(state = pagerState) { pageNum ->
        val pageLifecycleOwner = rememberLifecycleOwner(
            maxState = if (pagerState.settledPage == pageNum) {
                Lifecycle.State.RESUMED
            } else {
                Lifecycle.State.STARTED
            }
        )

        CompositionLocalProvider(LocalLifecycleOwner provides pageLifecycleOwner) {
            // Your pages here. Their lifecycle-aware components respect the
            // custom maxState defined above.
        }
    }

For more information on `CompositionLocal`, see [Locally scoped data with
CompositionLocal](https://developer.android.com/develop/ui/compose/compositionlocal).

## Best practices for lifecycle-aware components

- Keep your UI controllers as lean as possible. They should not try to acquire their own data; instead, use a [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel) to do that, and observe a `StateFlow` object to reflect the changes back to the UI.
- Try to write data-driven UIs where your UI controller's responsibility is to update the UI as data changes, or notify user actions back to the [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel).
- Put your data logic in your [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel) class. [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel) should serve as the connector between your UI controller and the rest of your app. Be careful though, it isn't [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel)'s responsibility to fetch data (for example, from a network). Instead, [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel) should call the appropriate component to fetch the data, then provide the result back to the UI controller.
- Use [Kotlin coroutines](https://developer.android.com/topic/libraries/architecture/coroutines) to manage long-running tasks and other operations that can run asynchronously.
- Keep start/stop logic inside the composable that actually needs it. This way, the logic is automatically removed if that specific UI element is removed from the screen (for example, inside a navigation graph or when visibility is conditional).
- Use `collectAsStateWithLifecycle` for data. Do not manually start or stop `Flow` collection based on lifecycle events. Instead, use `collectAsStateWithLifecycle` to convert streams into UI state efficiently. This saves battery and resources because `Flow`s are paused when the app is in the background.

For more information on `Flow`s, see [Other supported types of state](https://developer.android.com/develop/ui/compose/state#use-other-types-of-state-in-jetpack-compose).

## Use cases for lifecycle-aware components

Lifecycle-aware components can make it much easier for you to manage lifecycles
in a variety of cases. A few examples are:

- Switching between coarse and fine-grained location updates. Use `LifecycleStartEffect` to enable fine-grained location updates while your app is visible (`ON_START`), and automatically clean up the listener or switch to coarse-grained updates when the app is in the background (`ON_STOP`).
- Stopping and starting video buffering. Use `LifecycleResumeEffect` to defer actual video playback until the app is fully in the foreground and interactive (`ON_RESUME`), and to make sure that playback pauses and releases resources when the app is backgrounded (`ON_PAUSE`).
- Starting and stopping network streaming. Use `collectAsStateWithLifecycle` to observe continuous streams of data (like a Kotlin Flow from a network socket). This gives you live updating while an app is in the foreground and automatically cancels collection when the app goes into the background.
- Pausing and resuming heavy tasks. Use `LifecycleResumeEffect` to handle pausing heavy visual updates when the app is in the background, and resume them after the app is in the foreground.

## Handling ON_STOP events safely

Compose is designed to handle `ON_STOP` events safely.

- **State is safe:** You can update `MutableState` (for example, with `uiState.value = ...`) at any time, even when the app is in the background. Compose waits until the app is visible to render the changes.
- **Automatic cleanup:** With effects like `LifecycleStartEffect`, your cleanup block (`onStopOrDispose`) runs exactly when the lifecycle moves to `STOPPED`. This prevents you from holding heavy resources (like camera or location) while the app is in the background.

For more information on `MutableState`, see [State and Jetpack Compose](https://developer.android.com/develop/ui/compose/state).

## Additional resources

To learn more about handling lifecycles with lifecycle-aware components,
consult the following additional resources.

### Views content

- [Handling lifecycles with lifecycle-aware components (Views)](https://developer.android.com/topic/architecture/views/lifecycle-views)

## Recommended for you

- Note: link text is displayed when JavaScript is off
- [LiveData overview](https://developer.android.com/topic/libraries/architecture/livedata)
- [Use Kotlin coroutines with lifecycle-aware components](https://developer.android.com/topic/libraries/architecture/coroutines)
- [Saved State module for ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate)