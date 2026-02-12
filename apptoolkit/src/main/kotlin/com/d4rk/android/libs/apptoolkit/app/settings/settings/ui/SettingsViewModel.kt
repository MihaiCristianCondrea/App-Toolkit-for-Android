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

package com.d4rk.android.libs.apptoolkit.app.settings.settings.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.contract.SettingsAction
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.contract.SettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setErrors
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.setNoData
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

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
            is SettingsEvent.Load -> loadSettings(context = event.context)
        }
    }

    private fun loadSettings(context: Context) {
        startOperation(
            action = Actions.LOAD_SETTINGS,
            extra = mapOf(ExtraKeys.CONTEXT to context::class.java.name)
        )
        observeJob = observeJob.restart {
            flow {
                val config = withContext(dispatchers.io) {
                    settingsProvider.provideSettingsConfig(context = context)
                }
                emit(config)
            }
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
                .catchReport(
                    action = Actions.LOAD_SETTINGS,
                    extra = mapOf(ExtraKeys.CONTEXT to context::class.java.name)
                ) {
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
                                    screenState.updateData(newState = com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState.Error()) { current ->
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

    private object ExtraKeys {
        const val CONTEXT: String = "context"
    }
}
