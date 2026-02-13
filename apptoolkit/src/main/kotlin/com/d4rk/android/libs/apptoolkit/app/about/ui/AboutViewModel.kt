/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.libs.apptoolkit.app.about.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.GetAboutInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutAction
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutEvent
import com.d4rk.android.libs.apptoolkit.app.about.ui.mapper.toUiState
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
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
import kotlinx.coroutines.launch

/**
 * ViewModel for the About screen, including device info sharing.
 */
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
            is AboutEvent.DismissSnackbar -> dismissSnackbar()
        }
    }

    private fun loadAboutInfo() {
        startOperation(action = Actions.LOAD_ABOUT_INFO)
        observeJob = observeJob.restart {
            getAboutInfo.invoke()
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.setLoading()
                    }
                }
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
                    updateStateThreadSafe {
                        screenState.setError(
                            message = UiTextHelper.StringResource(R.string.snack_device_info_failed)
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun copyDeviceInfo(label: String) {
        val deviceInfo = screenData?.deviceInfo.orEmpty()
        startOperation(action = Actions.COPY_DEVICE_INFO, extra = mapOf(ExtraKeys.LABEL to label))

        if (deviceInfo.isBlank()) {
            viewModelScope.launch {
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
                .launchIn(viewModelScope)
        }
    }

    private fun dismissSnackbar() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.dismissSnackbar()
            }
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
