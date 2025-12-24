package com.d4rk.android.libs.apptoolkit.app.advanced.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsAction
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.state.AdvancedSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class AdvancedSettingsViewModel(
    private val repository: CacheRepository,
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

    private fun clearCache() {
        screenState.setLoading()
        var hasResult = false

        repository.clearCache()
            .onEach { result ->
                hasResult = true
                screenState.updateState(ScreenState.Success())
                val messageResId = when (result) {
                    is Result.Success -> R.string.cache_cleared_success
                    is Result.Error -> R.string.cache_cleared_error
                }
                screenState.copyData { copy(cacheClearMessage = messageResId) }
            }
            .catch {
                hasResult = true
                screenState.updateState(ScreenState.Success())
                screenState.copyData { copy(cacheClearMessage = R.string.cache_cleared_error) }
            }
            .onCompletion { cause ->
                screenState.updateState(ScreenState.Success())
                if (!hasResult || cause != null) {
                    screenState.copyData { copy(cacheClearMessage = R.string.cache_cleared_error) }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onMessageShown() {
        screenState.copyData { copy(cacheClearMessage = null) }
    }
}
