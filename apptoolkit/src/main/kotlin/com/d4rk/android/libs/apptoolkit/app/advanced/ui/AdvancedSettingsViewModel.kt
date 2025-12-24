package com.d4rk.android.libs.apptoolkit.app.advanced.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsAction
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.state.AdvancedSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.cancellation.CancellationException

class AdvancedSettingsViewModel(
    private val repository: CacheRepository ,
    private val dispatchers: DispatcherProvider ,
) : ScreenViewModel<AdvancedSettingsUiState, AdvancedSettingsEvent, AdvancedSettingsAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = AdvancedSettingsUiState()
    ),
) {

    private var clearCacheJob: Job? = null

    override fun onEvent(event: AdvancedSettingsEvent) {
        when (event) {
            AdvancedSettingsEvent.ClearCache -> clearCache()
            AdvancedSettingsEvent.MessageShown -> onMessageShown()
        }
    }

    private fun clearCache() {
        clearCacheJob?.cancel()

        clearCacheJob = repository.clearCache()
                .flowOn(dispatchers.io)
                .onEach { result ->
                    screenState.updateState(ScreenState.Success())

                    val messageResId = when (result) {
                        is Result.Success -> R.string.cache_cleared_success
                        is Result.Error -> R.string.cache_cleared_error
                    }

                    screenState.copyData { copy(cacheClearMessage = messageResId) }
                }
                .catch { t ->
                    if (t is CancellationException) throw t
                    screenState.updateState(ScreenState.Success())
                    screenState.copyData { copy(cacheClearMessage = R.string.cache_cleared_error) }
                }
                .launchIn(viewModelScope)
    }

    private fun onMessageShown() {
        screenState.copyData { copy(cacheClearMessage = null) }
    }
}