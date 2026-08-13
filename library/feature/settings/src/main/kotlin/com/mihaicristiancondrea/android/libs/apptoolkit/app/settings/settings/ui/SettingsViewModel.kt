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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.domain.models.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.ui.contracts.SettingsAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setNoData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateData
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel responsible for managing the state and logic of the settings screen.
 *
 * This ViewModel handles loading settings configurations and updating the UI state accordingly.
 * It communicates with a [SettingsProvider] to fetch the settings data.
 *
 * @param settingsProvider An implementation of [SettingsProvider] that supplies the settings configuration.
 * @param dispatchers A provider for coroutine dispatchers, used for managing background tasks.
 * @param firebaseController Reports ViewModel flow failures to Firebase.
 *
 * @see LoggedScreenViewModel
 * @see SettingsConfig
 * @see SettingsEvent
 * @see SettingsAction
 */
class SettingsViewModel(
    private val settingsProvider: SettingsProvider,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<SettingsConfig, SettingsEvent, SettingsAction>(
    initialState = UiStateScreen(data = SettingsConfig(title = "")),
    firebaseController = firebaseController,
    screenName = "Settings",
) {
    private var observeJob: Job? = null

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Load -> loadSettings()
        }
    }

    private fun loadSettings() {
        startOperation(action = Actions.LOAD_SETTINGS)
        observeJob = observeJob.restart {
            flow { emit(settingsProvider.provideSettingsConfig()) }
                .flowOn(dispatchers.io)
                .map<SettingsConfig, DataState<SettingsConfig, Errors>> { config ->
                    if (config.categories.isEmpty()) {
                        DataState.Error(data = config, error = Errors.UseCase.NO_DATA)
                    } else {
                        DataState.Success(config)
                    }
                }
                .onStart {
                    updateStateThreadSafe {
                        screenState.setErrors(emptyList())
                        screenState.setLoading()
                    }
                }
                .catchReport(action = Actions.LOAD_SETTINGS) {
                    emit(DataState.Error(error = Errors.UseCase.INVALID_STATE))
                }
                .onEach { result ->
                    result
                        .onSuccess { config ->
                            updateStateThreadSafe {
                                screenState.setErrors(emptyList())
                                screenState.setSuccess(data = config)
                            }
                        }
                        .onFailure { error ->
                            updateStateThreadSafe {
                                val fallback = (result as? DataState.Error)?.data ?: SettingsConfig(
                                    title = "",
                                    categories = emptyList()
                                )
                                if (error == Errors.UseCase.NO_DATA) {
                                    screenState.setErrors(
                                        listOf(
                                            UiSnackbar(
                                                message = UiTextHelper.StringResource(
                                                    R.string.error_no_settings_found
                                                )
                                            )
                                        )
                                    )
                                    screenState.setNoData(data = fallback)
                                } else {
                                    screenState.setErrors(listOf(UiSnackbar(message = error.asUiText())))
                                    screenState.updateData(newState = com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState.Error()) { current ->
                                        current
                                    }.also {
                                        if (screenState.value.data == null) {
                                            screenState.setSuccess(data = fallback)
                                        }
                                    }
                                }
                            }
                        }
                }
                .launchIn(viewModelScope)
        }
    }

    private object Actions {
        const val LOAD_SETTINGS: String = "loadSettings"
    }

}

