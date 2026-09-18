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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.copyTextToClipboard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.showSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.mappers.toUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.states.AboutUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the About screen, including tap-to-copy of the entries it renders.
 *
 * Writing to the clipboard is a system UI interaction, so it runs on the main dispatcher rather
 * than on IO, and it reports the outcome in-app on every platform version. Android 13 and newer
 * normally raise a system clipboard preview, but OEMs disable or restyle it, so relying on it left
 * users with no confirmation at all.
 */
open class AboutViewModel(
    private val aboutRepository: AboutRepository,
    private val context: Context,
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

            is AboutEvent.CopyToClipboard -> copyToClipboard(
                label = event.label,
                text = event.text,
                successMessage = event.successMessage,
            )

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

    private fun copyToClipboard(
        label: String,
        text: String,
        successMessage: UiTextHelper?,
    ) {
        val extra: Map<String, String> = mapOf(ExtraKeys.LABEL to label)

        copyJob = copyJob.restart {
            launchReport(
                action = Actions.COPY_TO_CLIPBOARD,
                extra = extra,
                block = {
                    val copied: Boolean = withContext(dispatchers.main) {
                        context.copyTextToClipboard(label = label, text = text)
                    }
                    check(copied) { "Clipboard rejected the copy for \"$label\"" }
                    showSnackbar(
                        message = successMessage
                            ?: UiTextHelper.StringResource(R.string.snack_copied_to_clipboard),
                        isError = false,
                    )
                },
                onError = {
                    showSnackbar(
                        message = UiTextHelper.StringResource(R.string.snack_copy_failed),
                        isError = true,
                    )
                },
            )
        }
    }

    private suspend fun showSnackbar(message: UiTextHelper, isError: Boolean) {
        updateStateThreadSafe {
            screenState.showSnackbar(
                UiSnackbar(
                    message = message,
                    isError = isError,
                    timeStamp = System.nanoTime(),
                    type = ScreenMessageType.SNACKBAR,
                )
            )
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
        const val COPY_TO_CLIPBOARD: String = "copyToClipboard"
    }

    private object ExtraKeys {
        const val LABEL: String = "label"
    }
}
