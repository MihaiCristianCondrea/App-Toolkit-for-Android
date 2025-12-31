package com.d4rk.android.libs.apptoolkit.app.ads.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsAction
import com.d4rk.android.libs.apptoolkit.app.ads.ui.contract.AdsSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.ads.ui.state.AdsSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** ViewModel for Ads settings screen. */
class AdsSettingsViewModel(
    private val repository: AdsSettingsRepository,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<AdsSettingsUiState, AdsSettingsEvent, AdsSettingsAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = AdsSettingsUiState()
    )
) {

    init {
        observeAdsEnabled()
    }

    override fun onEvent(event: AdsSettingsEvent) {
        when (event) {
            is AdsSettingsEvent.SetAdsEnabled -> setAdsEnabled(event.enabled)
        }
    }

    private fun observeAdsEnabled() {
        repository.observeAdsEnabled()
            .flowOn(dispatchers.io)
            .onStart { screenState.setLoading() }
            .onEach { enabled ->
                screenState.updateData(newState = ScreenState.Success()) { current ->
                    current.copy(adsEnabled = enabled)
                }
            }
            .catch { t ->
                if (t is CancellationException) throw t
                screenState.updateData(newState = ScreenState.Error()) { current ->
                    current.copy(adsEnabled = repository.defaultAdsEnabled)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun setAdsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val result = withContext(dispatchers.io) {
                repository.setAdsEnabled(enabled)
            }

            if (result is Result.Error) {
                screenState.updateData(newState = ScreenState.Error()) { current ->
                    current.copy(adsEnabled = !enabled)
                }
            }
        }
    }
}
