[Kotlin coroutines](https://developer.android.com/kotlin/coroutines.md.txt) provide an API that lets you write asynchronous
code. With Kotlin coroutines, you can define a [`CoroutineScope`](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/), which
helps you to manage when your coroutines should run. Each asynchronous operation
runs within a particular scope.

[Lifecycle-aware components](https://developer.android.com/topic/libraries/architecture/lifecycle.md.txt) provide first-class support for coroutines for
logical scopes in your app. This document explains how to use coroutines
effectively with lifecycle-aware components.

## Add dependencies

The built-in coroutine scopes described in this topic are contained in the
Lifecycle API. Be sure to add the appropriate dependencies when using these
scopes.

- For ViewModel utilities in Compose, use `implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")`.
- For Lifecycle utilities in Compose, use `implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")`.

## Lifecycle-aware coroutine scopes

Compose and the Lifecycle libraries provide the following built-in scopes that
you can use in your app.

### ViewModelScope

A `ViewModelScope` is defined for each [`ViewModel`](https://developer.android.com/topic/libraries/architecture/viewmodel.md.txt) in your app. Any
coroutine launched in this scope is automatically canceled if the `ViewModel` is
cleared. Coroutines are useful here for when you have work that needs to be done
only if the `ViewModel` is active. For example, if you are computing some data
for a layout, you should scope the work to the `ViewModel` so that if the
`ViewModel` is cleared, the work is canceled automatically to avoid consuming
resources.

You can access the `CoroutineScope` of a `ViewModel` through the
`viewModelScope` property of the `ViewModel`, as shown in the following example:

```kotlin
class MyViewModel: ViewModel() {
    init {
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
        }
    }
}
```

For more advanced use cases, you can pass a custom `CoroutineScope` directly
into the ViewModel's constructor to replace the default `viewModelScope`. This
approach offers more control and flexibility, particularly for:

- Testing: It lets you inject a `TestScope`, making it easier to control time
  and verify coroutine behavior in unit tests.

- Custom configuration: You can configure the scope with a specific
  `CoroutineDispatcher` (like `Dispatchers.Default` for heavy computation) or
  a custom `CoroutineExceptionHandler` before the ViewModel even starts its
  work.

### Composition-bound scopes

Side effects such as animations, network calls, or timers must be scoped to the
lifecycle of the composable. This way, when a composable leaves the screen
(exits the composition), any running coroutines are automatically canceled to
prevent memory leaks.

Compose provides the [`LaunchedEffect`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary.md.txt#LaunchedEffect(kotlin.Any,kotlin.coroutines.SuspendFunction1)) API to handle Composition scoping
declaratively.

`LaunchedEffect` creates a [`CoroutineScope`](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/) that lets you run suspend
functions. The scope is tied to the Composition lifecycle of the composable, not
the Lifecycle of the host Activity.

- Enter: The coroutine starts when the composable enters the composition.
- Exit: The coroutine is canceled when the composable leaves the composition.
- Relaunch: If any key passed to `LaunchedEffect` changes, the existing coroutine is canceled and a new one is launched.

> [!NOTE]
> **Note:** Because `LaunchedEffect` is tied to the Composition, it can run even if the composable is not currently visible to the user. For example, a `HorizontalPager` often composes neighboring pages off-screen to prepare for a swipe. If your side effect relies on the user actually viewing the screen (like an analytics event), use standard Lifecycle-aware APIs (like `LifecycleEventEffect`) instead of `LaunchedEffect`.

The following example demonstrates how to use `LaunchedEffect` to create a
pulsing animation. The coroutine is tied to the composable's presence in the
composition and reacts to configuration changes:


```kotlin
// Allow the pulse rate to be configured, so it can be sped up if the user is running
// out of time
var pulseRateMs by remember { mutableLongStateOf(3000L) }
val alpha = remember { Animatable(1f) }
LaunchedEffect(pulseRateMs) { // Restart the effect when the pulse rate changes
    while (isActive) {
        delay(pulseRateMs) // Pulse the alpha every pulseRateMs to alert the user
        alpha.animateTo(0f)
        alpha.animateTo(1f)
    }
}
```

<br />

For more information on `LaunchedEffect`, see [Side-effects in Compose](https://developer.android.com/develop/ui/compose/side-effects.md.txt#launchedeffect).

## Lifecycle-aware flow collection

To safely collect flows in Jetpack Compose, use the
[`collectAsStateWithLifecycle`](https://developer.android.com/reference/kotlin/androidx/lifecycle/compose/package-summary.md.txt#extension-functions) API. This single function converts a `Flow`
into a Compose `State` object and automatically manages the lifecycle
subscription for you. By default, collection begins when the lifecycle is
`STARTED` and stops when the lifecycle is `STOPPED`. To override this default
behavior, pass in the `minActiveState` parameter with the lifecycle method you
want, like `Lifecycle.State.RESUMED`.

The following example demonstrates how to collect a ViewModel's `StateFlow` in a
composable:


```kotlin
@Composable
private fun ConversationScreen(
    conversationViewModel: ConversationViewModel = viewModel()
) {

    val messages by conversationViewModel.messages.collectAsStateWithLifecycle()

    ConversationScreen(
        messages = messages,
        onSendMessage = { message: Message -> conversationViewModel.sendMessage(message) }
    )
}

@Composable
private fun ConversationScreen(
    messages: List<Message>,
    onSendMessage: (Message) -> Unit
) {

    MessagesList(messages, onSendMessage)
    /* ... */
}
```

<br />

### Parallel collection of multiple flows

In Compose, you can collect multiple flows in parallel by declaring multiple
state variables. Because `collectAsStateWithLifecycle` manages its own
underlying scope, parallel collection is handled automatically:

```kotlin
@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    // Both flows are collected safely in parallel and will emit updates when either changes, the composables will recompose
    val userData by viewModel.userFlow.collectAsStateWithLifecycle()
    val feedData by viewModel.feedFlow.collectAsStateWithLifecycle()

    // ...
}
```

## Calculate values asynchronously using Flows

When you need to calculate values asynchronously, use `StateFlow` with the
`stateIn` operator.

The following snippet uses a standard `Flow` converted to a `StateFlow`. The
[`WhileSubscribed(5000)`](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-while-subscribed.html) parameter keeps the subscription active for five
seconds after the UI disappears to handle configuration changes.

```kotlin
val uiState: StateFlow<Result> = flow {
    emit(repository.fetchData())
}
.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = Result.Loading
)
```

Use `collectAsStateWithLifecycle` to convert the collected values into Compose
[`State`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/State.md.txt), so that your UI can reactively update whenever the data changes.

> [!NOTE]
> **Note:** For one-off asynchronous UI tasks that don't need to survive configuration changes (like decoding a bitmap or loading a localized string), you can use the [`produceState`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary.md.txt#produceState(kotlin.Any,kotlin.coroutines.SuspendFunction1)) API directly in your composable. However, for loading screen data, `StateFlow` and `ViewModel` are recommended.

For more information about state, see [State and Jetpack Compose](https://developer.android.com/develop/ui/compose/state.md.txt).

## Additional resources

### Views content

- [Use Kotlin coroutines with lifecycle-aware components (Views)](https://developer.android.com/topic/libraries/architecture/views/coroutines-views.md.txt)

## Samples

## Recommended for you

- Note: link text is displayed when JavaScript is off
- [Handling Lifecycles with Lifecycle-Aware Components](https://developer.android.com/topic/libraries/architecture/lifecycle.md.txt)
- [Load and display paged data](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data.md.txt)