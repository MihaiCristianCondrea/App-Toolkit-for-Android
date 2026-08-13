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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.data.repositories.GeneralSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.contracts.GeneralSettingsAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.contracts.GeneralSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.states.GeneralSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.successData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * ViewModel that loads and validates general settings content by key.
 */
class GeneralSettingsViewModel(
    private val repository: GeneralSettingsRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<GeneralSettingsUiState, GeneralSettingsEvent, GeneralSettingsAction>(
    initialState = UiStateScreen(data = GeneralSettingsUiState()),
    firebaseController = firebaseController,
    screenName = "GeneralSettings",
) {

    private var observeJob: Job? = null

    override fun handleEvent(event: GeneralSettingsEvent) {
        when (event) {
            is GeneralSettingsEvent.Load -> loadContent(contentKey = event.contentKey)
        }
    }

    private fun loadContent(contentKey: String?) {
        val hasKey = !contentKey.isNullOrBlank()
        startOperation(
            action = Actions.LOAD_CONTENT,
            extra = mapOf(ExtraKeys.HAS_CONTENT_KEY to hasKey.toString())
        )

        if (!hasKey) {
            observeJob?.cancel()
            viewModelScope.launch {
                updateStateThreadSafe {
                    screenState.setErrors(
                        errors = listOf(
                            UiSnackbar(
                                message = UiTextHelper.StringResource(R.string.error_invalid_content_key)
                            )
                        )
                    )
                    screenState.updateState(ScreenState.NoData())
                }
            }
            return
        }

        observeJob = observeJob.restart {
            repository.getContentKey(contentKey)
                .flowOn(dispatchers.default)
                .map<String, DataState<String, Errors>> { key -> DataState.Success(key) }
                .onStart {
                    updateStateThreadSafe {
                        screenState.setErrors(emptyList())
                        screenState.setLoading()
                    }
                }
                .catchReport(action = Actions.LOAD_CONTENT) {
                    emit(DataState.Error(error = Errors.UseCase.INVALID_STATE))
                }
                .onEach { result ->
                    result
                        .onSuccess { key ->
                            updateStateThreadSafe {
                                screenState.setErrors(emptyList())
                                screenState.successData { copy(contentKey = key) }
                            }
                        }
                        .onFailure { error ->
                            updateStateThreadSafe {
                                val message = when (error) {
                                    Errors.UseCase.ILLEGAL_ARGUMENT ->
                                        UiTextHelper.StringResource(R.string.error_invalid_content_key)

                                    else -> error.asUiText()
                                }

                                screenState.setErrors(errors = listOf(UiSnackbar(message = message)))
                                screenState.updateState(ScreenState.NoData())
                            }
                        }
                }
                .launchIn(viewModelScope)
        }
    }

    private object Actions {
        const val LOAD_CONTENT: String = "loadContent"
    }

    private object ExtraKeys {
        const val HAS_CONTENT_KEY: String = "hasContentKey"
    }
}
