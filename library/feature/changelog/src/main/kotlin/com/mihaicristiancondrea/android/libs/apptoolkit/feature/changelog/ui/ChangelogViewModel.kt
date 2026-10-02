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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.domain.usecases.GetChangelogUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.contracts.ChangelogEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.states.ChangelogUiState
import kotlinx.coroutines.Job

/**
 * Owns changelog loading and retry for the changelog dialog.
 *
 * The use case and the repository behind it are main-safe, so it needs no dispatcher.
 */
class ChangelogViewModel(
    private val getChangelogUseCase: GetChangelogUseCase,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<ChangelogUiState, ChangelogEvent>(
    initialState = ChangelogUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "Changelog",
    viewModelName = "ChangelogViewModel",
) {
    private var loadJob: Job? = null

    init {
        onEvent(ChangelogEvent.Load)
    }

    override fun handleEvent(event: ChangelogEvent) {
        when (event) {
            ChangelogEvent.Load -> loadChangelog()
        }
    }

    private fun loadChangelog() {
        loadJob = loadJob.restart {
            launchReport(
                action = Actions.LOAD_CHANGELOG,
                onError = { error -> setState { copy(markdown = error.toFailed(fallback = LoadFailedText)) } },
            ) {
                setState { copy(markdown = Loadable.Loading) }
                val markdown = getChangelogUseCase()
                setState { copy(markdown = if (markdown.isBlank()) Loadable.Empty() else Loadable.Ready(markdown)) }
            }
        }
    }

    private object Actions {
        const val LOAD_CHANGELOG: String = "loadChangelog"
    }

    private companion object {
        val LoadFailedText = UiTextHelper.StringResource(R.string.error_loading_changelog_message)
    }
}
