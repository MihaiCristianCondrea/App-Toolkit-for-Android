package com.d4rk.android.libs.apptoolkit.app.about.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.GetAboutInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutAction
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutEvent
import com.d4rk.android.libs.apptoolkit.app.about.ui.mapper.toUiState
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class AboutViewModel(
    private val getAboutInfo: GetAboutInfoUseCase,
    private val copyDeviceInfo: CopyDeviceInfoUseCase,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<AboutUiState, AboutEvent, AboutAction>(
    initialState = UiStateScreen(data = AboutUiState())
) {

    private var loadJob: Job? = null
    private var copyJob: Job? = null

    init {
        loadAboutInfo()
    }

    override fun onEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.CopyDeviceInfo -> copyDeviceInfo(event.label)
            is AboutEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun loadAboutInfo() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            screenState.setLoading()

            runCatching {
                withContext(dispatchers.io) { getAboutInfo() }
            }.onSuccess { info ->
                screenState.successData { info.toUiState() }
            }.onFailure { t ->
                if (t is CancellationException) throw t

                screenState.updateState(ScreenState.Error())
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.StringResource(R.string.snack_device_info_failed),
                        isError = true,
                        timeStamp = System.nanoTime(),
                        type = ScreenMessageType.SNACKBAR,
                    )
                )
            }
        }
    }

    private fun copyDeviceInfo(label: String) {
        val deviceInfo = screenData?.deviceInfo.orEmpty()
        if (deviceInfo.isBlank()) {
            screenState.showSnackbar(
                UiSnackbar(
                    message = UiTextHelper.StringResource(R.string.snack_device_info_failed),
                    isError = true,
                    timeStamp = System.nanoTime(),
                    type = ScreenMessageType.SNACKBAR,
                )
            )
            return
        }

        copyJob?.cancel()
        copyJob = viewModelScope.launch {
            runCatching {
                withContext(dispatchers.main) { copyDeviceInfo(label, deviceInfo) }
            }.onSuccess {
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.StringResource(R.string.snack_device_info_copied),
                        isError = false,
                        timeStamp = System.nanoTime(),
                        type = ScreenMessageType.SNACKBAR,
                    )
                )
            }.onFailure { t ->
                if (t is CancellationException) throw t
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.StringResource(R.string.snack_device_info_failed),
                        isError = true,
                        timeStamp = System.nanoTime(),
                        type = ScreenMessageType.SNACKBAR,
                    )
                )
            }
        }
    }
}
