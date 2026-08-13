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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.about.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.ui.contracts.AboutAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.ui.contracts.AboutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.ui.mappers.toUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.ui.states.AboutUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.showSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * ViewModel for the About screen, including device info sharing.
 */
open class AboutViewModel(
    private val aboutRepository: AboutRepository,
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
            flow { emit(aboutRepository.getAboutInfo()) }
                .flowOn(dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.setLoading()
                    }
                }
                .onEach { info ->
                    updateStateThreadSafe {
                        screenState.setSuccess(data = info.toUiState())
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

