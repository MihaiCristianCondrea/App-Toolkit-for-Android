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
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the "About" screen, responsible for handling business logic and managing UI state.
 *
 * This ViewModel orchestrates the fetching and display of application and device information.
 * It also handles user actions such as copying device information to the clipboard.
 * It interacts with use cases to perform these operations and updates the UI state accordingly.
 *
 * @param getAboutInfo An instance of [GetAboutInfoUseCase] to fetch application and device information.
 * @param copyDeviceInfo An instance of [CopyDeviceInfoUseCase] to handle copying device info text.
 * @param dispatchers A provider for coroutine dispatchers, used to switch between I/O and main threads.
 *
 * @see ScreenViewModel
 * @see AboutUiState
 * @see AboutEvent
 * @see AboutAction
 */
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
        onEvent(AboutEvent.Load)
    }

    override fun onEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.Load -> loadAboutInfo()
            is AboutEvent.CopyDeviceInfo -> copyDeviceInfo(event.label)
            is AboutEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun loadAboutInfo() {
        loadJob?.cancel()
        loadJob = getAboutInfo()
            .flowOn(dispatchers.io)
            .onStart { screenState.setLoading() }
            .onEach { result ->
                result
                    .onSuccess { info ->
                        screenState.update { current ->
                            current.copy(
                                screenState = ScreenState.Success(),
                                data = info.toUiState()
                            )
                        }
                    }
                    .onFailure { error ->
                        screenState.updateState(ScreenState.Error())
                        screenState.showSnackbar(
                            UiSnackbar(
                                message = error.asUiText(),
                                isError = true,
                                timeStamp = System.nanoTime(),
                                type = ScreenMessageType.SNACKBAR
                            )
                        )
                    }
            }
            .catch {
                screenState.updateState(ScreenState.Error())
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.StringResource(R.string.snack_device_info_failed),
                        isError = true,
                        timeStamp = System.nanoTime(),
                        type = ScreenMessageType.SNACKBAR
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    private fun copyDeviceInfo(label: String) {
        val deviceInfo = screenData?.deviceInfo.orEmpty()
        if (deviceInfo.isBlank()) {
            screenState.showSnackbar(
                UiSnackbar(
                    message = UiTextHelper.StringResource(R.string.snack_device_info_failed),
                    isError = true,
                    timeStamp = System.nanoTime(),
                    type = ScreenMessageType.SNACKBAR
                )
            )
            return
        }

        copyJob?.cancel()
        copyJob = copyDeviceInfo(label = label, deviceInfo = deviceInfo)
            .flowOn(dispatchers.io)
            .onEach { result ->
                result
                    .onSuccess { copied ->
                        val messageRes = if (copied) {
                            R.string.snack_device_info_copied
                        } else {
                            R.string.snack_device_info_failed
                        }
                        screenState.showSnackbar(
                            UiSnackbar(
                                message = UiTextHelper.StringResource(
                                    messageRes
                                ),
                                isError = !copied,
                                timeStamp = System.nanoTime(),
                                type = ScreenMessageType.SNACKBAR
                            )
                        )
                    }
                    .onFailure { error ->
                        screenState.showSnackbar(
                            UiSnackbar(
                                message = error.asUiText(),
                                isError = true,
                                timeStamp = System.nanoTime(),
                                type = ScreenMessageType.SNACKBAR
                            )
                        )
                    }
            }
            .catch {
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.StringResource(R.string.snack_device_info_failed),
                        isError = true,
                        timeStamp = System.nanoTime(),
                        type = ScreenMessageType.SNACKBAR
                    )
                )
            }
            .launchIn(viewModelScope)
    }
}
