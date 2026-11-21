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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.analytics.CHANGELOG_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.constants.ChangelogOperationNames
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.domain.usecases.GetChangelogUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.contracts.ChangelogEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.states.ChangelogUiState
import kotlinx.coroutines.Job

/** Loads changelog history on creation and retries through [ChangelogEvent.Load]. */
class ChangelogViewModel(
    private val getChangelogUseCase: GetChangelogUseCase,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<ChangelogUiState, ChangelogEvent>(
    initialState = ChangelogUiState(),
    telemetryRepository = telemetryRepository,
    screenName = CHANGELOG_SCREEN.name,
    viewModelName = "ChangelogViewModel",
) {
    private var loadJob: Job? = null

    init {
        onEvent(event = ChangelogEvent.Load)
    }

    override fun handleEvent(event: ChangelogEvent) {
        when (event) {
            is ChangelogEvent.Load -> loadChangelog()
        }
    }

    /** Loads the release history and installed version, sharing an active fetch with duplicate requests. */
    private fun loadChangelog() {
        loadJob = loadJob.ifIsActive {
            launchReport(
                action = ChangelogOperationNames.LOAD_CHANGELOG,
                onError = { error ->
                    setState {
                        copy(
                            markdown = error.toFailed(
                                fallback = UiText.StringResource(resourceId = R.string.error_loading_changelog_message),
                            ),
                        )
                    }
                },
            ) {
                setState { copy(markdown = Loadable.Loading) }
                val history = getChangelogUseCase()
                setState {
                    copy(
                        markdown = if (history.markdown.isBlank()) {
                            Loadable.Empty()
                        } else {
                            Loadable.Ready(value = history.markdown)
                        },
                        currentVersion = history.currentVersion,
                    )
                }
            }
        }
    }
}
