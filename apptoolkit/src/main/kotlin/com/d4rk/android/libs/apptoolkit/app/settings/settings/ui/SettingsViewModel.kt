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
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setErrors
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.toError
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
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
 * @see ScreenViewModel
 * @see SettingsConfig
 * @see SettingsEvent
 * @see SettingsAction
 */
class SettingsViewModel(
    private val settingsProvider: SettingsProvider,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<SettingsConfig, SettingsEvent, SettingsAction>(
    initialState = UiStateScreen(data = SettingsConfig(title = "", categories = emptyList())),
) {

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Load -> loadSettings(context = event.context)
        }
    }

    private fun loadSettings(context: Context) {
        viewModelScope.launch {
            flow {
                val config = withContext(dispatchers.io) {
                    settingsProvider.provideSettingsConfig(context = context)
                }
                emit(config)
            }
                .map<SettingsConfig, DataState<SettingsConfig, Errors>> { config ->
                    if (config.categories.isEmpty()) {
                        DataState.Error(
                            data = config,
                            error = Errors.UseCase.NO_DATA
                        )
                    } else {
                        DataState.Success(config)
                    }
                }
                .onStart {
                    screenState.setErrors(emptyList())
                    screenState.setLoading()
                }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    firebaseController.reportViewModelError(
                        viewModelName = "SettingsViewModel",
                        action = "loadSettings",
                        throwable = throwable,
                    )
                    emit(
                        DataState.Error(
                            error = throwable.toError(default = Errors.UseCase.INVALID_STATE)
                        )
                    )
                }
                .collect { result ->
                    result
                        .onSuccess { config ->
                            screenState.successData {
                                copy(title = config.title, categories = config.categories)
                            }
                        }
                        .onFailure { error ->
                            val snackbarMessage = when {
                                configCategoriesAreEmpty(result) -> UiTextHelper.StringResource(
                                    R.string.error_no_settings_found,
                                )

                                else -> error.asUiText()
                            }

                            screenState.setErrors(
                                listOf(
                                    UiSnackbar(
                                        message = snackbarMessage,
                                    ),
                                ),
                            )

                            val newState = if (configCategoriesAreEmpty(result)) {
                                ScreenState.NoData()
                            } else {
                                ScreenState.Error()
                            }
                            screenState.updateState(newState)
                        }
                }
        }
    }

    private fun configCategoriesAreEmpty(result: DataState<SettingsConfig, Errors>): Boolean {
        val data = when (result) {
            is DataState.Success -> result.data
            is DataState.Error -> result.data
            is DataState.Loading -> result.data
        }
        return data?.categories.isNullOrEmpty()
    }
}
