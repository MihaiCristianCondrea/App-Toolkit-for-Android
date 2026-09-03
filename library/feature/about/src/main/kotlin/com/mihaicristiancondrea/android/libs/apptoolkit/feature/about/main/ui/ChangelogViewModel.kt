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

/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.main.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.main.domain.usecases.GetChangelogUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.main.ui.contracts.ChangelogAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.main.ui.contracts.ChangelogEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.main.ui.states.ChangelogUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/** Owns changelog loading and retry state for the changelog dialog. */
class ChangelogViewModel(
    private val getChangelogUseCase: GetChangelogUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<ChangelogUiState, ChangelogEvent, ChangelogAction>(
    initialState = UiStateScreen(data = ChangelogUiState()),
    firebaseController = firebaseController,
    screenName = "Changelog",
) {
    private var loadJob: Job? = null

    init {
        onEvent(event = ChangelogEvent.Load)
    }

    override fun handleEvent(event: ChangelogEvent) {
        when (event) {
            ChangelogEvent.Load,
            ChangelogEvent.Retry -> loadChangelog()
        }
    }

    private fun loadChangelog() {
        startOperation(action = Actions.LOAD_CHANGELOG)
        loadJob = loadJob.restart {
            getChangelogUseCase()
                .flowOn(context = dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.setLoading()
                    }
                }
                .onEach { result: DataState<String, Errors> ->
                    result
                        .onSuccess { markdown ->
                            updateStateThreadSafe {
                                screenState.setSuccess(
                                    data = ChangelogUiState(markdown = markdown),
                                )
                            }
                        }
                        .onFailure {
                            updateStateThreadSafe {
                                screenState.setError(
                                    message = UiTextHelper.StringResource(
                                        R.string.error_loading_changelog_message,
                                    ),
                                )
                            }
                        }
                }
                .catchReport(action = Actions.LOAD_CHANGELOG) {
                    updateStateThreadSafe {
                        screenState.setError(
                            message = UiTextHelper.StringResource(
                                R.string.error_loading_changelog_message,
                            ),
                        )
                    }
                }
                .launchIn(scope = viewModelScope)
        }
    }

    private object Actions {
        const val LOAD_CHANGELOG: String = "loadChangelog"
    }
}

