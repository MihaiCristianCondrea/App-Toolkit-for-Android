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
import android.os.Build
import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logUnlockAchievement
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.copyTextToClipboard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
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

/** Reported as `unlock_achievement` the first time the version-tap easter egg is found. */
private const val SEASONAL_THEMES_ACHIEVEMENT: String = "seasonal_themes"

/**
 * ViewModel for the About screen, including tap-to-copy of the entries it renders.
 *
 * Writing to the clipboard is a system UI interaction, so it happens on the main thread, which is
 * where `viewModelScope` already runs. Android 13 raised its own clipboard preview for every copy,
 * so a successful copy is confirmed in-app only below that, where nothing else tells the user
 * anything happened. A failed copy raises no system UI at all, so it is always reported.
 *
 * The version-tap easter egg also unlocks the seasonal themes controls on the theme screen. The
 * first unlock is announced, since nothing else points at where the reward went.
 *
 * @param seasonalThemes Records the easter egg unlock.
 * @param sdkIntProvider Supplies the running platform level, so the confirmation rule is testable
 * without a device.
 */
open class AboutViewModel(
    private val aboutRepository: AboutRepository,
    private val context: Context,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
    private val seasonalThemes: SeasonalThemeRepository,
    private val sdkIntProvider: () -> Int = { Build.VERSION.SDK_INT },
) : LoggedScreenViewModel<AboutUiState, AboutEvent, AboutAction>(
    initialState = UiStateScreen(data = AboutUiState()),
    firebaseController = firebaseController,
    screenName = "About",
) {
    private var observeJob: Job? = null

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

            is AboutEvent.EasterEggFound -> unlockSeasonalThemes()
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

    /**
     * Copies [text] under [label], then confirms it where the platform will not.
     *
     * Each copy is its own job on purpose. An earlier version restarted a shared `copyJob`, which
     * bought nothing, a clipboard write is instant and idempotent, and gave cancellation a way to
     * drop a copy the user had already asked for. There is no `withContext` either: the write has
     * to happen on the main thread and `viewModelScope` is already there, so hopping dispatchers
     * only moved the write off the click's own frame.
     */
    private fun copyToClipboard(
        label: String,
        text: String,
        successMessage: UiTextHelper?,
    ) {
        launchReport(
            action = Actions.COPY_TO_CLIPBOARD,
            extra = mapOf(ExtraKeys.LABEL to label),
            block = {
                val copied: Boolean = context.copyTextToClipboard(label = label, text = text)
                check(copied) { "Clipboard rejected the copy for \"$label\"" }
                if (!showsSystemClipboardPreview()) {
                    showSnackbar(
                        message = successMessage
                            ?: UiTextHelper.StringResource(R.string.snack_copied_to_clipboard),
                        isError = false,
                    )
                }
            },
            onError = {
                showSnackbar(
                    message = UiTextHelper.StringResource(R.string.snack_copy_failed),
                    isError = true,
                )
            },
        )
    }

    private fun unlockSeasonalThemes() {
        launchReport(
            action = Actions.UNLOCK_SEASONAL_THEMES,
            block = {
                if (seasonalThemes.unlockSeasonalThemes()) {
                    firebaseController.logUnlockAchievement(
                        achievementId = SEASONAL_THEMES_ACHIEVEMENT,
                    )
                    showSnackbar(
                        message = UiTextHelper.StringResource(R.string.snack_seasonal_themes_unlocked),
                        isError = false,
                    )
                }
            },
            // Konfetti already played; a failed write only means the unlock is offered next time.
            onError = {},
        )
    }

    /**
     * True when the platform raises its own clipboard preview, making an in-app confirmation a
     * duplicate report of the same copy.
     */
    private fun showsSystemClipboardPreview(): Boolean =
        sdkIntProvider() > Build.VERSION_CODES.S_V2

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
        const val UNLOCK_SEASONAL_THEMES: String = "unlockSeasonalThemes"
    }

    private object ExtraKeys {
        const val LABEL: String = "label"
    }
}
