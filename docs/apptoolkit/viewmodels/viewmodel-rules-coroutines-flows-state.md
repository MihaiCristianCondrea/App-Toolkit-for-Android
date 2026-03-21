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

