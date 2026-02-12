# ViewModel rules for coroutines, flows, and UI state

This document defines the rules for implementing ViewModels in AppToolkit and in apps that use the
library. Follow these rules to keep ViewModels predictable, testable, and consistent.

## Scope

These rules apply to:

- All ViewModels that extend `ScreenViewModel` (including `LoggedScreenViewModel`)
- All `Flow` collections started from `viewModelScope`
- All updates to `MutableStateFlow<UiStateScreen<T>>` (`screenState` in AppToolkit ViewModels)

---

## Rule 1: Prefer flows for ongoing work

Prefer `Flow` when a use case can emit values over time (observe/stream) or when you want consistent
collection patterns (`flowOn`, `onStart`, `catchReport`, `launchIn`).

- Use `launchIn(viewModelScope)` for long-lived work.
- Use `collect` only when you must do sequential suspending work per emission.

### Example

```kotlin
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.usecases.GetFaqUseCase
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpAction
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.ui.state.HelpUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections*

class HelpViewModel(
    private val getFaqUseCase: GetFaqUseCase,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<HelpUiState, HelpEvent, HelpAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = HelpUiState()
    )
) {

    private var loadFaqJob: Job? = null

    init {
        onEvent(event = HelpEvent.LoadFaq)
    }

    override fun onEvent(event: HelpEvent) {
        when (event) {
            is HelpEvent.LoadFaq -> loadFaq()
            is HelpEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun loadFaq() {
        loadFaqJob?.cancel()
        loadFaqJob = getFaqUseCase()
            .flowOn(context = dispatchers.io)
            .onStart { screenState.setLoading() }
            .onEach { result: DataState<List<FaqItem>, Errors> ->
                result
                    .onSuccess { faqs: List<FaqItem> ->
                        val screenStateForData: ScreenState =
                            if (faqs.isEmpty()) ScreenState.NoData() else ScreenState.Success()

                        screenState.update { current: UiStateScreen<HelpUiState> ->
                            current.copy(
                                screenState = screenStateForData,
                                data = HelpUiState(questions = faqs.toImmutableList())
                            )
                        }
                    }
                    .onFailure { error: Errors ->
                        screenState.setError(message = error.asUiText())
                    }
            }
            .catchReport(
                action = "loadFaq",
                fallbackMessage = UiTextHelper.StringResource(R.string.error_failed_to_load_faq),
            ) { throwable ->
                screenState.setError(
                    message = throwable.asUiText(
                        fallback = UiTextHelper.StringResource(R.string.error_failed_to_load_faq)
                    )
                )
            }
            .launchIn(scope = viewModelScope)
    }
}
````

---

## Rule 2: Set coroutine context with `flowOn`

Use `flowOn` to move upstream work off the main thread:

* Use `dispatchers.io` for I/O (network, disk, database).
* Use `dispatchers.default` for CPU-bound work.

Do not switch context inside flow builders with `withContext` to call `emit`. Use `flowOn` instead.

---

## Rule 3: Report unexpected flow failures with `catchReport`

Do not use simple `.catch { ... }` for ViewModel flow error reporting. Use `catchReport(...)` so
logging/reporting and cancellation handling stay consistent.

* Use `action = ...` so reports include where the error happened.
* Keep user-facing UI state updates inside the `catchReport` lambda.
* Provide `fallbackMessage` when the UI needs a safe default string.

### Example

```kotlin
package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.base.catchReport
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper

private fun observeApps() {
    getAppsUseCase()
        .catchReport(
            action = "observeApps",
            fallbackMessage = UiTextHelper.StringResource(R.string.error_failed_to_load_apps),
        ) { throwable ->
            screenState.setError(
                message = throwable.asUiText(
                    fallback = UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
                )
            )
        }
        .launchIn(viewModelScope)
}
```

---

## Rule 4: Show snackbars with `UiSnackbar`

Use `screenState.showSnackbar(UiSnackbar(...))` to show a snackbar.

* Use `System.nanoTime()` for a unique timestamp.
* Set `type = ScreenMessageType.SNACKBAR`.
* Set `isError = true` for error messages.

### Example

```kotlin
package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper

private fun <T> showLoadError(
    screenState: kotlinx.coroutines.flow.MutableStateFlow<
        com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen<T>
    >
) {
    screenState.showSnackbar(
        UiSnackbar(
            message = UiTextHelper.StringResource(R.string.error_failed_to_load_apps),
            isError = true,
            timeStamp = System.nanoTime(),
            type = ScreenMessageType.SNACKBAR,
        )
    )
}
```

---

## Rule 5: Handle `DataState` using `onSuccess`, `onFailure`, and optional `onLoading`

When you receive a `DataState`, handle it using the provided extension functions:

* Use `onSuccess { ... }` for success UI.
* Use `onFailure { ... }` for error UI.
* Use `onLoading { ... }` only when you need to reflect loading state during active emission.

---

## Rule 6: Use `launchReport` for one-off, non-Flow work

When a ViewModel needs to trigger a one-off operation that is not modeled as a `Flow`, use
`launchReport` instead of ad-hoc `viewModelScope.launch` + `runCatching`.

This keeps Firebase breadcrumbs, analytics, and error reporting consistent while ensuring errors are
handled in one place.

* Use `launchReport` for fire-and-forget work (e.g., billing launch, toggles without a `Flow`).
* Reserve `viewModelScope.launch` for local UI-only state updates.

### Example

```kotlin
package com.d4rk.android.apps.apptoolkit.app.apps.favorites.ui

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onLoading
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess

private fun handleResult(result: DataState<Unit, Errors>) {
    result
        .onLoading { /* optional */ }
        .onSuccess { /* success */ }
        .onFailure { /* error */ }
}
```

---

## Rule 7: Trigger initial work through an event in `init`

When a screen needs initial loading/observing, dispatch it as an event from `init`.

Do not start loading directly in `init` without using the event contract.

### Example

```kotlin
package com.d4rk.android.apps.apptoolkit.app.main.ui

class ExampleViewModel : androidx.lifecycle.ViewModel() {
    init {
        // Use the public event pipeline.
        // onEvent(ExampleEvent.Load)
    }
}
```

---

## Rule 8: Inject `DispatcherProvider` only in ViewModels

Inject `DispatcherProvider` into ViewModels to apply `flowOn` consistently.

Do not inject dispatchers into repositories, data sources, or use cases unless there is a specific
testing need that cannot be addressed otherwise.

### Example

```kotlin
package com.d4rk.android.libs.apptoolkit.core.di

interface DispatcherProvider {
    val io: kotlinx.coroutines.CoroutineDispatcher
    val default: kotlinx.coroutines.CoroutineDispatcher
    val main: kotlinx.coroutines.CoroutineDispatcher
}
```

---

## Rule 9: Use `UiStateScreen(data = ...)` as the default initial state

The default screen state is `ScreenState.IsLoading()`.

Set a different `screenState` only when the screen must start in a non-loading state.

### Example

```kotlin
package com.d4rk.android.libs.apptoolkit.core.ui.state

val initial = UiStateScreen(data = Any())
```

---

## Rule 10: Set loading state with `onStart` and `setLoading()`

If a flow represents a fetch/load operation, set loading in `onStart` using the existing extension:

```kotlin
.onStart { screenState.setLoading() }
```

### Example

```kotlin
package com.d4rk.android.libs.apptoolkit.app.help.ui

import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart

fun <T> observeLoading(
    screenState: kotlinx.coroutines.flow.MutableStateFlow<
        com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen<T>
    >,
    source: Flow<Unit>,
    scope: kotlinx.coroutines.CoroutineScope,
) {
    source
        .onStart { screenState.setLoading() }
        .launchIn(scope)
}
```

---

## Rule 11: Use mutex helpers for atomic UI updates

When multiple coroutines can update state concurrently, serialize UI updates:

* Use `updateStateThreadSafe { ... }` for multi-step updates.
* Use `updateSuccessState(screenState) { ... }` for success-only data changes.
* Keep mutex-protected sections short and non-suspending.

### Example

```kotlin
package com.d4rk.android.libs.apptoolkit.core.ui.base

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class ExampleBase {
    private val stateMutex = Mutex()

    protected suspend fun updateStateThreadSafe(update: () -> Unit) {
        stateMutex.withLock { update() }
    }
}
```

---

## Job rules

### Use `generalJob` for single-operation screens

If a ViewModel has one “active operation” job at a time, use `generalJob`:

* Cancel the previous job before starting a new one.
* Use a dedicated job only when the ViewModel needs multiple operations concurrently.

---

## Summary

Follow these rules:

* Prefer flows and use `launchIn(viewModelScope)` for ongoing work.
* Use `flowOn` to move upstream work off the main thread.
* Report unexpected failures with `catchReport(...)` (not simple `.catch { ... }`).
* Show snackbars with `screenState.showSnackbar(UiSnackbar(...))`.
* Handle results with `onSuccess` and `onFailure` (and optional `onLoading`).
* Trigger initial work through an event from `init`.
* Inject `DispatcherProvider` only in ViewModels.
* Use `UiStateScreen(data = ...)` as the default initial state.
* Use `.onStart { screenState.setLoading() }` for loading transitions.
* Serialize concurrent UI updates with mutex helpers.

---

# Thread-safe state updates in `BaseViewModel`

This document describes the thread-safety helpers added to `BaseViewModel` in AppToolkit. Use these
helpers to make state updates predictable when multiple coroutines can update the same UI state.

## Background

`MutableStateFlow` is safe to update from multiple threads. However, a *sequence of state updates*
is not automatically atomic. If multiple coroutines update the same state around the same time,
updates can interleave and produce inconsistent UI behavior, such as:

* Lost updates (one update overwrites another)
* Incorrect ordering of `Loading`, `Error`, and `Success` transitions
* Snackbar updates that appear out of order
* Multiple “single operation” jobs running at the same time

To address these issues, AppToolkit adds a `Mutex` to `BaseViewModel` and provides helper functions
that serialize state updates.

## What changed in `BaseViewModel`

`BaseViewModel` includes:

* A private `Mutex` (`stateMutex`) for serializing state changes
* `updateStateThreadSafe()` for atomic state mutations
* `updateSuccessState()` for success-only updates to `UiStateScreen<T>`
* `generalJob` for screens that need one cancellable operation at a time

## Additions

### `stateMutex`

`BaseViewModel` includes a private mutex:

```kotlin
import kotlinx.coroutines.sync.Mutex

private val stateMutex = Mutex()
```

Use the mutex through the helper functions described below. Keep mutex-protected sections short.

### `updateStateThreadSafe()`

Use `updateStateThreadSafe()` to apply multiple related state changes as a single atomic update.

```kotlin
import kotlinx.coroutines.sync.withLock

protected suspend fun updateStateThreadSafe(update: () -> Unit) {
    stateMutex.withLock { update() }
}
```

#### When to use

Use `updateStateThreadSafe()` when:

* A collector updates state and UI messages together (for example, state + snackbar)
* Multiple coroutines can update the same state concurrently
* You need ordering guarantees for back-to-back emissions

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

This pattern is useful for changing a field in your `data` model without changing the overall
screen state.

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
3. Use `updateSuccessState()` for success-only mutations to `UiStateScreen<T>.data`.
4. Replace one-off job fields with `generalJob` when the ViewModel only needs one cancellable
   operation.

## Summary

Use these helpers to make state updates reliable:

* `updateStateThreadSafe { ... }` for atomic UI-state changes
* `updateSuccessState(...)` for success-only updates to `UiStateScreen<T>`
* `generalJob` for ViewModels that have one cancellable operation at a time

```

Key improvements made (without changing your references/essence):
- Removed the pasted `onCompletion` operator KDoc (it’s generic Kotlin docs and creates noise).
- Fixed the duplicate “Rule 6” numbering and reduced repeated wording.
- Clarified what is allowed for `viewModelScope.launch` (UI-only updates) vs `launchReport`.
- Made the loading helper example type-correct (`CoroutineScope` instead of `LifecycleCoroutineScope` + no unused import).
- Kept your original code examples and naming, but cleaned indentation and tightened phrasing.
