package com.d4rk.android.libs.apptoolkit.app.settings.general.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.contract.GeneralSettingsAction
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.contract.GeneralSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.state.GeneralSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.state.setErrors
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.toError
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class GeneralSettingsViewModel(
    private val repository: GeneralSettingsRepository,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<GeneralSettingsUiState, GeneralSettingsEvent, GeneralSettingsAction>(
    initialState = UiStateScreen(data = GeneralSettingsUiState())
) {

    private var loadJob: Job? = null

    override fun onEvent(event: GeneralSettingsEvent) {
        when (event) {
            is GeneralSettingsEvent.Load -> loadContent(contentKey = event.contentKey)
        }
    }

    private fun loadContent(contentKey: String?) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getContentKey(contentKey)
                .flowOn(dispatchers.default)
                .map<String, DataState<String, Errors>> { key ->
                    DataState.Success(key)
                }
                .onStart { screenState.setLoading() }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable

                    emit(
                        DataState.Error(
                            error = throwable.toError(default = Errors.UseCase.INVALID_STATE)
                        )
                    )
                }
                .onEach { result ->
                    result
                        .onSuccess { key ->
                            screenState.setErrors(errors = emptyList())
                            screenState.copyData { copy(contentKey = key) }
                            screenState.updateState(newValues = ScreenState.Success())
                        }
                        .onFailure { error ->
                            screenState.setErrors(
                                errors = listOf(
                                    UiSnackbar(
                                        message = when (error) {
                                            Errors.UseCase.ILLEGAL_ARGUMENT -> UiTextHelper.StringResource(
                                                R.string.error_invalid_content_key
                                            )

                                            else -> error.asUiText()
                                        }
                                    )
                                )
                            )
                            screenState.updateState(newValues = ScreenState.NoData())
                        }
                }
                .onCompletion { cause ->
                    if (cause is CancellationException) return@onCompletion
                }
                .collect { }
        }
    }
}
