package com.d4rk.android.libs.apptoolkit.app.about.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutAction
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutEvent
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.ObserveAboutInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.setLoading
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.successData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

open class AboutViewModel(
    private val observeAboutInfo: ObserveAboutInfoUseCase,
    private val copyDeviceInfo: CopyDeviceInfoUseCase,
) :
    ScreenViewModel<AboutUiState, AboutEvent, AboutAction>(initialState = UiStateScreen(data = AboutUiState())) {

    init {
        loadAboutInfo()
    }

    override fun onEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.CopyDeviceInfo -> copyDeviceInfo(event.label)
            is AboutEvent.DismissSnackbar -> dismissSnack()
        }
    }

    private fun loadAboutInfo() {
        viewModelScope.launch {
            screenState.setLoading()
            var hasEmissions = false
            observeAboutInfo()
                .onCompletion { cause ->
                    when {
                        cause is CancellationException -> Unit
                        cause != null -> {
                            screenState.updateState(ScreenState.Error())
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = UiTextHelper.StringResource(resourceId = R.string.snack_device_info_failed),
                                    isError = true,
                                    timeStamp = System.nanoTime(),
                                    type = ScreenMessageType.SNACKBAR,
                                )
                            )
                        }
                        !hasEmissions -> screenState.updateState(ScreenState.NoData())
                        else -> Unit
                    }
                }
                .catch { error ->
                    if (error is CancellationException) {
                        throw error
                    }
                }
                .collect { info ->
                    hasEmissions = true
                    screenState.successData { info }
                }
        }
    }

    private fun copyDeviceInfo(label: String) {
        screenData?.let { data ->
            viewModelScope.launch {
                runCatching {
                    copyDeviceInfo(label = label, deviceInfo = data.deviceInfo)
                }.onSuccess {
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(resourceId = R.string.snack_device_info_copied),
                            isError = false,
                            timeStamp = System.nanoTime(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }.onFailure { error ->
                    if (error is CancellationException) {
                        throw error
                    }
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(resourceId = R.string.snack_device_info_failed),
                            isError = true,
                            timeStamp = System.nanoTime(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
            }
        }
    }

    private fun dismissSnack() {
        screenState.dismissSnackbar()
    }
}