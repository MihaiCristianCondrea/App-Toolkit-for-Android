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
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

open class AboutViewModel(
    private val getAboutInfo: GetAboutInfoUseCase,
    private val copyDeviceInfo: CopyDeviceInfoUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<AboutUiState, AboutEvent, AboutAction>(
    initialState = UiStateScreen(data = AboutUiState()),
    firebaseController = firebaseController,
    screenName = "About",
) {
    private var observeJob: Job? = null
    private var copyJob: Job? = null

    init {
        onEvent(AboutEvent.Load)
    }

    override fun handleEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.Load -> loadAboutInfo()
            is AboutEvent.CopyDeviceInfo -> copyDeviceInfo(label = event.label)
            is AboutEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun loadAboutInfo() {
        startOperation(action = Actions.LOAD_ABOUT_INFO)
        observeJob = observeJob.restart {
            getAboutInfo.invoke()
                .flowOn(dispatchers.io)
                .onStart { screenState.setLoading() }
                .onEach { result ->
                    result
                        .onSuccess { info ->
                            updateStateThreadSafe {
                                screenState.setSuccess(data = info.toUiState())
                            }
                        }
                        .onFailure { error ->
                            updateStateThreadSafe {
                                screenState.setError(message = error.asUiText())
                            }
                        }
                }
                .catchReport(action = Actions.LOAD_ABOUT_INFO) {
                    screenState.setError(message = UiTextHelper.StringResource(R.string.snack_device_info_failed))
                }
                .launchIn(viewModelScope)
        }
    }

    private fun copyDeviceInfo(label: String) {
        val deviceInfo = screenData?.deviceInfo.orEmpty()
        startOperation(action = Actions.COPY_DEVICE_INFO, extra = mapOf(ExtraKeys.LABEL to label))

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

        copyJob = copyJob.restart {
            copyDeviceInfo.invoke(label = label, deviceInfo = deviceInfo)
                .flowOn(dispatchers.io)
                .onEach { result ->
                    result
                        .onSuccess { copyResult ->
                            updateStateThreadSafe {
                                val messageRes = if (copyResult.copied) {
                                    R.string.snack_device_info_copied
                                } else {
                                    R.string.snack_device_info_failed
                                }

                                if (!copyResult.copied || copyResult.shouldShowFeedback) {
                                    screenState.showSnackbar(
                                        UiSnackbar(
                                            message = UiTextHelper.StringResource(messageRes),
                                            isError = !copyResult.copied,
                                            timeStamp = System.nanoTime(),
                                            type = ScreenMessageType.SNACKBAR,
                                        )
                                    )
                                }
                            }
                        }
                        .onFailure {
                            updateStateThreadSafe {
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
                .catchReport(
                    action = Actions.COPY_DEVICE_INFO,
                    extra = mapOf(ExtraKeys.LABEL to label)
                ) {
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.snack_device_info_failed),
                            isError = true,
                            timeStamp = System.nanoTime(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
                .launchIn(viewModelScope)
        }
    }

    private object Actions {
        const val LOAD_ABOUT_INFO: String = "loadAboutInfo"
        const val COPY_DEVICE_INFO: String = "copyDeviceInfo"
    }

    private object ExtraKeys {
        const val LABEL: String = "label"
    }
}
