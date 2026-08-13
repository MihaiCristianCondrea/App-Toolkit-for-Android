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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.permissions.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.permissions.data.repositories.PermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.permissions.ui.contracts.PermissionsAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.permissions.ui.contracts.PermissionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.domain.models.SettingsConfig
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
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for the permissions screen.
 *
 * This ViewModel is responsible for orchestrating the retrieval of permission configurations
 * from the [PermissionsRepository] and exposing them to the UI. It handles the loading state,
 * success state with the configuration data, and various error states (e.g., network errors,
 * no permissions found).
 *
 * It extends [ScreenViewModel] to manage the UI state ([UiStateScreen]) and handle UI events
 * ([PermissionsEvent]) and actions ([PermissionsAction]).
 *
 * @param permissionsRepository The repositories responsible for fetching permissions data info.
 * @param firebaseController Reports ViewModel flow failures to Firebase.
 */
class PermissionsViewModel(
    private val permissionsRepository: PermissionsRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<SettingsConfig, PermissionsEvent, PermissionsAction>(
    initialState = UiStateScreen(
        data = SettingsConfig(
            title = "",
            categories = emptyList(),
        )
    ),
    firebaseController = firebaseController,
    screenName = "Permissions",
) {

    private var observeJob: Job? = null

    override fun handleEvent(event: PermissionsEvent) {
        when (event) {
            PermissionsEvent.Load -> loadPermissions()
        }
    }

    private fun loadPermissions() {
        observeJob = observeJob.restart {
            startOperation(action = Actions.LOAD_PERMISSIONS)

            permissionsRepository.getPermissionsConfig()
                .flowOn(dispatchers.io)
                .map<SettingsConfig, DataState<SettingsConfig, Errors>> { config ->
                    if (config.categories.isEmpty()) {
                        DataState.Error(
                            data = config,
                            error = Errors.UseCase.NO_DATA,
                        )
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
                .catchReport(action = Actions.LOAD_PERMISSIONS) {
                    emit(
                        DataState.Error(
                            error = Errors.UseCase.INVALID_STATE,
                        )
                    )
                }
                .onEach { result ->
                    result
                        .onSuccess { config ->
                            updateStateThreadSafe {
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
                                    }
                                }
                            }
                        }

                }
                .launchIn(viewModelScope)
        }
    }

    private object Actions {
        const val LOAD_PERMISSIONS: String = "loadPermissions"
    }
}

