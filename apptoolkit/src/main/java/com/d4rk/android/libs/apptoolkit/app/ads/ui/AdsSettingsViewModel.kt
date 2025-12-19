package com.d4rk.android.libs.apptoolkit.app.ads.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsAction
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.ui.state.AdsSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.setLoading
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/** ViewModel for Ads settings screen. */
class AdsSettingsViewModel(
    private val repository: AdsSettingsRepository,
) : ScreenViewModel<AdsSettingsUiState, AdsSettingsEvent, AdsSettingsAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = AdsSettingsUiState()
    )
) {

    init {
        viewModelScope.launch {
            var hasEmitted = false
            var failure: Throwable? = null

            repository.observeAdsEnabled()
                .onStart { screenState.setLoading() }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    failure = throwable
                }
                .onCompletion { cause ->
                    val error = cause ?: failure
                    when {
                        error is CancellationException -> Unit
                        error != null -> screenState.updateData(newState = ScreenState.Error()) { current ->
                            if (hasEmitted) current else current.copy(adsEnabled = repository.defaultAdsEnabled)
                        }
                        hasEmitted -> Unit
                        else -> screenState.updateData(newState = ScreenState.Success()) { current ->
                            current.copy(adsEnabled = repository.defaultAdsEnabled)
                        }
                    }
                }
                .collect { enabled ->
                    hasEmitted = true
                    failure = null

                    screenState.updateData(newState = ScreenState.Success()) { current ->
                        current.copy(adsEnabled = enabled)
                    }
                }
        }
    }

    override fun onEvent(event: AdsSettingsEvent) {
        when (event) {
            is AdsSettingsEvent.SetAdsEnabled -> setAdsEnabled(event.enabled)
        }
    }

    private fun setAdsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            when (repository.setAdsEnabled(enabled)) {
                is Result.Success -> Unit
                is Result.Error -> screenState.updateData(newState = ScreenState.Error()) { current ->
                    current.copy(adsEnabled = !enabled)
                }
            }
        }
    }
}

