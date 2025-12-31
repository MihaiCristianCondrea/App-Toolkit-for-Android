package com.d4rk.android.libs.apptoolkit.app.settings.settings.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.contract.SettingsAction
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.contract.SettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
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
 *
 * @see ScreenViewModel
 * @see SettingsConfig
 * @see SettingsEvent
 * @see SettingsAction
 */
class SettingsViewModel(
    private val settingsProvider: SettingsProvider,
    private val dispatchers: DispatcherProvider,
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
            var failure: Throwable? = null

            flow {
                val config = withContext(dispatchers.io) {
                    settingsProvider.provideSettingsConfig(context = context)
                }
                emit(config)
            }
                .onStart {
                    screenState.setErrors(emptyList())
                    screenState.setLoading()
                }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    failure = throwable
                }
                .onCompletion { cause ->
                    val error = cause ?: failure
                    when {
                        error is CancellationException -> Unit
                        error != null -> {
                            screenState.setErrors(
                                listOf(
                                    UiSnackbar(
                                        message = UiTextHelper.StringResource(
                                            R.string.error_an_error_occurred
                                        ),
                                    ),
                                ),
                            )
                            screenState.updateState(ScreenState.Error())
                        }
                    }
                }
                .collect { result ->
                    failure = null

                    if (result.categories.isNotEmpty()) {
                        screenState.successData {
                            copy(title = result.title, categories = result.categories)
                        }
                    } else {
                        screenState.setErrors(
                            listOf(
                                UiSnackbar(
                                    message = UiTextHelper.StringResource(
                                        R.string.error_no_settings_found,
                                    ),
                                ),
                            ),
                        )
                        screenState.updateState(ScreenState.NoData())
                    }
                }
        }
    }
}
