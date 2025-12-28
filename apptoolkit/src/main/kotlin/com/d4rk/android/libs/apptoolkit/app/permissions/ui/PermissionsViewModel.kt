package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository.PermissionsRepository
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsAction
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


/** ViewModel responsible for exposing the permissions configuration to the UI layer. */
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
            var latestConfig: SettingsConfig? = null
            var failure: Throwable? = null

            permissionsRepository.getPermissionsConfig()
                .onStart { screenState.setLoading() }
                .onCompletion { cause ->
                    val error = cause ?: failure
                    when {
                        error is CancellationException -> return@onCompletion
                        error != null -> screenState.updateState(ScreenState.Error())
                        latestConfig?.categories.isNullOrEmpty() -> {
                            screenState.setErrors(
                                listOf(
                                    UiSnackbar(message = UiTextHelper.DynamicString("No settings found"))
                                )
                            )
                            screenState.updateState(ScreenState.NoData())
                        }

                        else -> screenState.updateState(ScreenState.Success())
                    }
                }
                .catch { error ->
                    if (error is CancellationException) throw error

                    failure = error
                    screenState.setErrors(
                        listOf(
                            UiSnackbar(
                                message = UiTextHelper.DynamicString(
                                    error.message ?: "Something went wrong"
                                )
                            )
                        )
                    )
                }
                .collect { result: SettingsConfig ->
                    failure = null
                    latestConfig = result

                    if (result.categories.isNotEmpty()) {
                        screenState.successData {
                            copy(title = result.title, categories = result.categories)
                        }
                    } else {
                        screenState.setErrors(
                            listOf(
                                UiSnackbar(message = UiTextHelper.DynamicString("No settings found"))
                            )
                        )
                    }
                }
        }
    }
}
