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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.analytics.ABOUT_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.constants.AboutOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.ClipboardRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logUnlockAchievement
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.SeasonalThemeRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.analytics.SEASONAL_THEMES_ACHIEVEMENT
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.mappers.toAboutItems
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.states.AboutUiState
import kotlinx.coroutines.Job

/**
 * Loads About entries and handles clipboard requests and the seasonal themes unlock.
 *
 * Successful clipboard writes show a message when the platform provides no confirmation.
 */
class AboutViewModel(
    private val aboutRepository: AboutRepository,
    private val clipboardRepository: ClipboardRepository,
    telemetryRepository: TelemetryRepository,
    private val seasonalThemes: SeasonalThemeRepository,
) : LoggedScreenViewModel<AboutUiState, AboutEvent>(
    initialState = AboutUiState(),
    telemetryRepository = telemetryRepository,
    screenName = ABOUT_SCREEN.name,
    viewModelName = "AboutViewModel",
) {
    private var loadJob: Job? = null

    init {
        onEvent(event = AboutEvent.Load)
    }

    override fun handleEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.Load -> loadAboutInfo()

            is AboutEvent.CopyToClipboard -> copyToClipboard(
                label = event.label,
                text = event.text,
                successMessage = event.successMessage,
            )

            is AboutEvent.EasterEggFound -> unlockSeasonalThemes()
        }
    }

    /** Loads and maps About metadata for the screen. A new request replaces the previous load. */
    private fun loadAboutInfo() {
        loadJob = loadJob.restart {
            launchReport(
                action = AboutOperationNames.LOAD_ABOUT_INFO,
                onError = { error ->
                    setState {
                        copy(
                            items = error.toFailed(
                                fallback = UiText.StringResource(resourceId = R.string.snack_device_info_failed),
                            ),
                        )
                    }
                },
            ) {
                setState { copy(items = Loadable.Loading) }
                val items = aboutRepository.getAboutInfo().toAboutItems()
                setState { copy(items = Loadable.Ready(value = items)) }
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
        successMessage: UiText?,
    ) {
        launchReport(
            action = AboutOperationNames.COPY_TO_CLIPBOARD,
            onError = { error ->
                showMessage(error.toErrorMessage(fallback = UiText.StringResource(resourceId = R.string.snack_copy_failed)))
            },
        ) {
            clipboardRepository.copyText(label = label, text = text)
            if (!clipboardRepository.confirmsCopies) {
                showMessage(
                    UiMessage(
                        text = successMessage ?: UiText.StringResource(resourceId = R.string.snack_copied_to_clipboard)
                    ),
                )
            }
        }
    }

    /**
     * A failed unlock write is reported without interrupting the celebration; the unlock can be
     * retried on a later visit.
     */
    private fun unlockSeasonalThemes() {
        launchReport(action = AboutOperationNames.UNLOCK_SEASONAL_THEMES) {
            if (seasonalThemes.unlockSeasonalThemes()) {
                telemetryRepository.logUnlockAchievement(achievementId = SEASONAL_THEMES_ACHIEVEMENT)
                showMessage(UiMessage(text = UiText.StringResource(resourceId = R.string.snack_seasonal_themes_unlocked)))
            }
        }
    }
}
