# Thread-safe state updates in `BaseViewModel`

This document describes the thread-safety helpers added to `BaseViewModel` in AppToolkit. Use these
helpers to make state updates predictable when multiple coroutines can update the same UI state.

## Background

`MutableStateFlow` is safe to update from multiple threads. However, *a sequence of state updates*
is not automatically atomic. If multiple coroutines update the same state around the same time,
updates can interleave and produce inconsistent UI behavior, such as:

- Lost updates (one update overwrites another)
- Incorrect ordering of `Loading`, `Error`, and `Success` transitions
- Snackbar updates that appear out of order
- Multiple “single operation” jobs running at the same time

To address these issues, AppToolkit adds a `Mutex` to `BaseViewModel` and provides helper functions
that serialize state updates.

## What changed in `BaseViewModel`

`BaseViewModel` now includes:

- A private `Mutex` (`stateMutex`) for serializing state changes
- `updateStateThreadSafe()` for atomic state mutations
- `updateSuccessState()` for success-only updates to `UiStateScreen<T>`
- `generalJob` for screens that need one cancellable operation at a time

## Additions

### `stateMutex`

`BaseViewModel` includes a private mutex:

```kotlin
import kotlinx.coroutines.sync.Mutex

private val stateMutex = Mutex()
````

Use the mutex through the helper functions described in the next sections. Keep mutex-protected
sections short.

### `updateStateThreadSafe()`

Use `updateStateThreadSafe()` to apply multiple related state changes as a single atomic update.

```kotlin
import kotlinx.coroutines.sync.withLock

/**
 * Updates the current UI state in a thread-safe manner using a [Mutex].
 *
 * This ensures that concurrent state updates do not result in race conditions,
 * guaranteeing atomicity when modifying the [uiStateFlow].
 *
 * @param update A lambda function containing the logic to update the state.
 */
protected suspend fun updateStateThreadSafe(update: () -> Unit) {
    stateMutex.withLock {
        update()
    }
}
```

#### When to use

Use `updateStateThreadSafe()` when:

* A collector updates state and UI messages together (for example, state + snackbar)
* Multiple coroutines can update the same state concurrently
* You need to ensure ordering for back-to-back emissions

#### Example

```kotlin
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper

updateStateThreadSafe {
    screenState.updateState(ScreenState.Error())
    screenState.showSnackbar(
        UiSnackbar(
            message = UiTextHelper.StringResource(R.string.error_generic),
            isError = true,
            timeStamp = System.currentTimeMillis(),
            type = ScreenMessageType.SNACKBAR,
        )
    )
}
```

#### Guidance

* Do not perform IO inside the mutex.
* Do not call suspend functions inside the `update` lambda.

Do suspend work first, then lock only for the state update.

### `updateSuccessState()` and `getSuccessData()`

`updateSuccessState()` updates `UiStateScreen.data` only when the current screen state is
`ScreenState.Success`.

```kotlin
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.withLock

/**
 * Updates [UiStateScreen.data] only when [UiStateScreen.screenState] is [ScreenState.Success].
 *
 * Mirrors the original "updateSuccessState" pattern (mutex + success-only update),
 * adapted for [UiStateScreen] since [ScreenState] is not generic in this codebase.
 */
protected suspend fun <T> updateSuccessState(
    screenData: MutableStateFlow<UiStateScreen<T>>,
    updateData: (T) -> T,
) {
    stateMutex.withLock {
        getSuccessData(screenData)?.let { data ->
            screenData.value = screenData.value.copy(data = updateData(data))
        }
    }
}

/**
 * Returns the current non-null [UiStateScreen.data] only when the state is [ScreenState.Success].
 */
protected fun <T> getSuccessData(screenData: MutableStateFlow<UiStateScreen<T>>): T? {
    val current = screenData.value
    if (current.screenState !is ScreenState.Success) return null
    return current.data
}
```

#### When to use

Use `updateSuccessState()` when:

* The screen is expected to have valid `data` only in `Success`
* You want to ignore late updates while the screen is `Loading`, `Error`, or `NoData`

This pattern is useful for updating a field in your `data` model without changing the overall screen
state.

#### Example

```kotlin
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private fun setBillingInProgress(inProgress: Boolean) {
    viewModelScope.launch {
        updateSuccessState(screenState) { current ->
            current.copy(isBillingInProgress = inProgress)
        }
    }
}
```

### `generalJob`

`generalJob` is a shared job reference that supports the “one active operation” pattern.

```kotlin
import kotlinx.coroutines.Job

/**
 * General-purpose job for screens that only need one cancellable operation at a time.
 *
 * Prefer specialized job properties when multiple concurrent jobs are required.
 */
protected var generalJob: Job? = null
```

#### When to use

Use `generalJob` when the ViewModel only needs one cancellable operation at a time, such as:

* Loading content (FAQ, settings content, navigation items)
* Requesting consent
* Sending a report
* Clearing cache

If the ViewModel needs multiple independent operations (for example, observing a list and toggling
favorites), keep dedicated job properties.

#### Example (Flow-based job)

```kotlin
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn

generalJob?.cancel()
generalJob = getFaqUseCase()
    .launchIn(viewModelScope)
```

#### Example (Coroutine job)

```kotlin
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

generalJob?.cancel()
generalJob = viewModelScope.launch {
    // Work that should replace any previous general job.
}
```

## Recommended patterns

### Serialize collector updates

If a flow collector performs multiple updates per emission, wrap the updates in
`updateStateThreadSafe()`.

```kotlin
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState

// Inside a flow collector:
updateStateThreadSafe {
    result
        .onSuccess { data ->
            screenState.updateData(newState = ScreenState.Success()) { current ->
                current.copy(items = data)
            }
        }
        .onFailure {
            screenState.updateState(ScreenState.Error())
        }
}
```

### Keep mutex-protected sections small

Lock only the code that must be atomic. Do not lock around:

* IO work
* long computations
* delays
* other suspend operations

## Migration guidance

When updating an existing ViewModel:

1. Identify places where state is updated from multiple coroutines.
2. Wrap multi-step state mutations in `updateStateThreadSafe()`.
3. Use `updateSuccessState()` for success-only mutations to `UiStateScreen.data`.
4. Replace one-off job fields with `generalJob` when the ViewModel only needs one cancellable
   operation.

## Summary

Use these helpers to make state updates reliable:

* `updateStateThreadSafe { ... }` for atomic UI-state changes
* `updateSuccessState(...)` for success-only updates to `UiStateScreen<T>`
* `generalJob` for ViewModels that have one cancellable operation at a time