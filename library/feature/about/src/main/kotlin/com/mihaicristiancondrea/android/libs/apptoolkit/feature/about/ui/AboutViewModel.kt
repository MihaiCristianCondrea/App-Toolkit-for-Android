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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.ClipboardRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logUnlockAchievement
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.mappers.toAboutItems
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.states.AboutUiState
import kotlinx.coroutines.Job

/** Reported as `unlock_achievement` the first time the version-tap easter egg is found. */
private const val SEASONAL_THEMES_ACHIEVEMENT: String = "seasonal_themes"

/**
 * ViewModel for the About screen, including tap-to-copy of the entries it renders.
 *
 * Writing to the clipboard is a system UI interaction, so it happens on the main thread, which is
 * where `viewModelScope` already runs. A successful copy is confirmed in-app only where the system
 * does not confirm it itself ([ClipboardRepository.confirmsCopies]). A failed copy raises no system
 * UI at all, so it is always reported.
 *
 * The version-tap easter egg also unlocks the seasonal themes controls on the theme screen and the
 * developer options entry in the advanced settings. The first unlock is announced, since nothing
 * else points at where the reward went.
 *
 * @param clipboardRepository Receives the copied entries, so this ViewModel holds no `Context`.
 * @param seasonalThemes Records the easter egg unlock.
 */
open class AboutViewModel(
    private val aboutRepository: AboutRepository,
    private val clipboardRepository: ClipboardRepository,
    telemetryRepository: TelemetryRepository,
    private val seasonalThemes: SeasonalThemeRepository,
) : LoggedScreenViewModel<AboutUiState, AboutEvent>(
    initialState = AboutUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "About",
    viewModelName = "AboutViewModel",
) {
    private var loadJob: Job? = null

    init {
        onEvent(AboutEvent.Load)
    }

    override fun handleEvent(event: AboutEvent) {
        when (event) {
            AboutEvent.Load -> loadAboutInfo()

            is AboutEvent.CopyToClipboard -> copyToClipboard(
                label = event.label,
                text = event.text,
                successMessage = event.successMessage,
            )

            AboutEvent.EasterEggFound -> unlockSeasonalThemes()
        }
    }

    /**
     * Loads the entries. [AboutRepository.getAboutInfo] moves its own package manager lookup off
     * the main thread, so this needs no dispatcher.
     */
    private fun loadAboutInfo() {
        loadJob = loadJob.restart {
            launchReport(
                action = Actions.LOAD_ABOUT_INFO,
                onError = { error ->
                    setState { copy(items = error.toFailed(fallback = UiTextHelper.StringResource(R.string.snack_device_info_failed))) }
                },
            ) {
                setState { copy(items = Loadable.Loading) }
                val items = aboutRepository.getAboutInfo().toAboutItems()
                setState { copy(items = Loadable.Ready(items)) }
            }
        }
    }

    /**
     * Copies on the main thread and confirms the write when the platform does not. Each request
     * runs independently so a later click cannot cancel an earlier copy.
     */
    private fun copyToClipboard(
        label: String,
        text: String,
        successMessage: UiTextHelper?,
    ) {
        launchReport(
            action = Actions.COPY_TO_CLIPBOARD,
            extra = mapOf(ExtraKeys.LABEL to label),
            onError = { error ->
                showMessage(error.toErrorMessage(fallback = UiTextHelper.StringResource(R.string.snack_copy_failed)))
            },
        ) {
            clipboardRepository.copyText(label = label, text = text)
            if (!clipboardRepository.confirmsCopies) {
                showMessage(
                    UiMessage(successMessage ?: UiTextHelper.StringResource(R.string.snack_copied_to_clipboard)),
                )
            }
        }
    }

    /**
     * A failed unlock write is reported without interrupting the celebration; the unlock can be
     * retried on a later visit.
     */
    private fun unlockSeasonalThemes() {
        launchReport(action = Actions.UNLOCK_SEASONAL_THEMES) {
            if (seasonalThemes.unlockSeasonalThemes()) {
                telemetryRepository.logUnlockAchievement(achievementId = SEASONAL_THEMES_ACHIEVEMENT)
                showMessage(UiMessage(UiTextHelper.StringResource(R.string.snack_seasonal_themes_unlocked)))
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
