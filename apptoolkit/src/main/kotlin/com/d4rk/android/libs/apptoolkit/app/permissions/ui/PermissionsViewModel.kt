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

package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository.PermissionsRepository
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsAction
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
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
 * @param permissionsRepository The repository responsible for fetching permissions data info.
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
                                    screenState.updateData(newState = com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState.Error()) { current ->
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
