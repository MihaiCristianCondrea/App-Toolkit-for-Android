package com.d4rk.android.libs.apptoolkit.app.advanced.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsAction
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.state.AdvancedSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.cancellation.CancellationException

/**
 * ViewModel for the advanced settings screen.
 *
 * This ViewModel manages the UI state and business logic for advanced settings functionalities,
 * such as clearing the application cache. It interacts with a [CacheRepository] to perform
 * data operations and updates the UI state accordingly.
 *
 * @param repository The repository responsible for cache-related operations.
 * @param dispatchers Provides coroutine dispatchers for different contexts (IO, Main, etc.).
 * @param firebaseController Reports ViewModel flow failures to Firebase.
 */
class AdvancedSettingsViewModel(
    private val repository: CacheRepository,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<AdvancedSettingsUiState, AdvancedSettingsEvent, AdvancedSettingsAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = AdvancedSettingsUiState()
    ),
) {

    override fun onEvent(event: AdvancedSettingsEvent) {
        when (event) {
            AdvancedSettingsEvent.ClearCache -> clearCache()
            AdvancedSettingsEvent.MessageShown -> onMessageShown()
        }
    }

    /**
     * Initiates the cache clearing process.
     *
     * This function cancels any ongoing cache clearing operation to prevent multiple concurrent executions.
     * It then calls the repository to clear the cache on an IO thread. The UI state is updated based on
     * the result of the operation (success or error), setting an appropriate message to be displayed
     * to the user. It also handles potential exceptions during the flow execution.
     */
    private fun clearCache() {
        generalJob?.cancel()

        generalJob = repository.clearCache()
            .flowOn(dispatchers.io)
            .map<Result<Unit>, DataState<Unit, Errors.Database>> { result ->
                when (result) {
                    is Result.Success -> DataState.Success(Unit)
                    is Result.Error -> DataState.Error(
                        error = Errors.Database.DATABASE_OPERATION_FAILED
                    )
                }
            }
            .onStart { screenState.setLoading() }
            .onEach { result ->
                result
                    .onSuccess {
                        screenState.updateState(ScreenState.Success())
                        screenState.copyData { copy(cacheClearMessage = R.string.cache_cleared_success) }
                    }
                    .onFailure {
                        screenState.updateState(ScreenState.Error())
                        screenState.copyData { copy(cacheClearMessage = R.string.cache_cleared_error) }
                    }
            }
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                firebaseController.reportViewModelError(
                    viewModelName = "AdvancedSettingsViewModel",
                    action = "clearCache",
                    throwable = throwable,
                )
                screenState.updateState(ScreenState.Error())
                screenState.copyData { copy(cacheClearMessage = R.string.cache_cleared_error) }
            }
            .launchIn(viewModelScope)
    }

    private fun onMessageShown() {
        screenState.copyData { copy(cacheClearMessage = null) }
    }
}
