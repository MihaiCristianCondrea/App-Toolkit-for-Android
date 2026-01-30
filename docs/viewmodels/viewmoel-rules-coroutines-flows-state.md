# ViewModel rules for coroutines, flows, and UI state

This document defines the rules for implementing ViewModels in AppToolkit and in apps that use the
library. Follow these rules to keep ViewModels predictable, testable, and consistent.

## Scope

These rules apply to:

- All ViewModels that extend `ScreenViewModel`
- All flows collected in `viewModelScope`
- All updates to `MutableStateFlow<UiStateScreen<T>>`

---

## Rule 1: Prefer flows for ongoing work

Prefer `Flow` when a use case can emit values over time or when you want consistent collection
patterns.

- Use `launchIn(viewModelScope)` for long-lived work.
- Use `collect` only when you need to do work sequentially inside a coroutine.

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
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

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
            .catch {
                if (it is CancellationException) throw it
                firebaseController.reportViewModelError(
                    viewModelName = "HelpViewModel",
                    action = "loadFaq",
                    throwable = it,
                )
                screenState.setError(message = UiTextHelper.StringResource(R.string.error_failed_to_load_faq))
            }
            .launchIn(scope = viewModelScope)
    }
}
```

---

## Rule 2: Set coroutine context with `flowOn`

Use `flowOn` to move upstream work off the main thread.

* Use `dispatchers.io` for I/O (network, disk, database).
* Use `dispatchers.default` for CPU-bound work.

Do not switch context inside flow builders with `withContext` to call `emit`. Use `flowOn` instead.

---

## Rule 3: Report ViewModel flow failures in `catch`

When a flow fails unexpectedly, report it with `firebaseController.reportViewModelError` in `catch`.

* Rethrow `CancellationException`.
* Log the ViewModel name and the action where the error happened.
* After logging, update UI state and show user feedback when appropriate.

### Example

```kotlin
package com.d4rk.android.apps.apptoolkit.app.apps.list.ui

import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlin.coroutines.cancellation.CancellationException

private fun reportFlowFailure(
    firebaseController: FirebaseController,
    throwable: Throwable,
) {
    if (throwable is CancellationException) throw throwable
    firebaseController.reportViewModelError(
        viewModelName = "AppsListViewModel",
        action = "observeFetch",
        throwable = throwable,
    )
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

private fun <T> showLoadError(screenState: kotlinx.coroutines.flow.MutableStateFlow<com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen<T>>) {
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

## Rule 5: Handle results with `onSuccess`, `onFailure`, and optional `onLoading`

When you receive a `DataState`, handle it using the provided extension functions.

* Use `onSuccess { ... }` for success UI.
* Use `onFailure { ... }` for error UI.
* Use `onLoading { ... }` only when you need to reflect loading state during active emission.

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

## Rule 6: Trigger initial loading through an event in `init`

When a screen needs initial loading, dispatch it as an event from `init`.

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

## Rule 7: Inject `DispatcherProvider` only in ViewModels

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

## Rule 8: Use `UiStateScreen(data = ...)` as the default initial state

The default screen state is `ScreenState.IsLoading()`.

Set a different `screenState` only when the screen must start in a non-loading state.

### Example

```kotlin
package com.d4rk.android.libs.apptoolkit.core.ui.state

val initial = UiStateScreen(data = Any())
```

---

## Rule 9: Set loading state with `onStart` and `setLoading()`

If a flow represents a fetch/load operation, set loading in `onStart` using the existing extension:

```kotlin
.onStart { screenState.setLoading() }
```

### Example

```kotlin
package com.d4rk.android.libs.apptoolkit.app.help.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart

fun <T> observeLoading(
    screenState: kotlinx.coroutines.flow.MutableStateFlow<com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen<T>>,
    source: Flow<Unit>,
    scope: androidx.lifecycle.LifecycleCoroutineScope,
) {
    source
        .onStart { screenState.setLoading() }
        .launchIn(scope)
}
```

---

## Rule 10: Use mutex helpers for atomic UI updates

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
* Log unexpected failures in `catch` with `firebaseController.reportViewModelError`.
* Show snackbars with `screenState.showSnackbar(UiSnackbar(...))`.
* Handle results with `onSuccess` and `onFailure` (and optional `onLoading`).
* Trigger initial loading through an event from `init`.
* Inject `DispatcherProvider` only in ViewModels.
* Use `UiStateScreen(data = ...)` as the default initial state.
* Use `.onStart { screenState.setLoading() }` for loading transitions.
* Serialize concurrent UI updates with mutex helpers.