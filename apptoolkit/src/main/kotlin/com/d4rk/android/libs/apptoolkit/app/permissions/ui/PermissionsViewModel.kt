package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository.PermissionsRepository
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsAction
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setErrors
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

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
 */
class PermissionsViewModel(
    private val permissionsRepository: PermissionsRepository,
) :
    ScreenViewModel<SettingsConfig, PermissionsEvent, PermissionsAction>(
        initialState = UiStateScreen(data = SettingsConfig(title = "", categories = emptyList()))
    ) {

    override fun onEvent(event: PermissionsEvent) {
        when (event) {
            PermissionsEvent.Load -> loadPermissions()
        }
    }

    private fun loadPermissions() {
        viewModelScope.launch {
            permissionsRepository.getPermissionsConfig()
                .map<SettingsConfig, DataState<SettingsConfig, Error>> { config ->
                    if (config.categories.isEmpty()) {
                        DataState.Error(data = config, error = Error("No settings found"))
                    } else {
                        DataState.Success(config)
                    }
                }
                .onStart {
                    screenState.setErrors(emptyList())
                    screenState.setLoading()
                }
                .catch { error ->
                    if (error is CancellationException) throw error
                    emit(DataState.Error(error = Error(error.message)))
                }
                .collect { result ->
                    result
                        .onSuccess { config ->
                            screenState.successData {
                                copy(title = config.title, categories = config.categories)
                            }
                        }
                        .onFailure { error ->
                            val message = if (error.message.isNullOrBlank()) {
                                UiTextHelper.StringResource(R.string.error_an_error_occurred)
                            } else {
                                UiTextHelper.DynamicString(error.message!!)
                            }

                            if (configCategoriesAreEmpty(result)) {
                                screenState.setErrors(
                                    listOf(
                                        UiSnackbar(
                                            message = UiTextHelper.StringResource(R.string.error_no_settings_found)
                                        )
                                    )
                                )
                                screenState.updateState(ScreenState.NoData())
                            } else {
                                screenState.setErrors(
                                    listOf(
                                        UiSnackbar(
                                            message = message
                                        )
                                    )
                                )
                                screenState.updateState(ScreenState.Error())
                            }
                        }
                }
        }
    }

    private fun configCategoriesAreEmpty(result: DataState<SettingsConfig, Error>): Boolean {
        val data = when (result) {
            is DataState.Success -> result.data
            is DataState.Error -> result.data
            is DataState.Loading -> result.data
        }
        return data?.categories.isNullOrEmpty()
    }
}
