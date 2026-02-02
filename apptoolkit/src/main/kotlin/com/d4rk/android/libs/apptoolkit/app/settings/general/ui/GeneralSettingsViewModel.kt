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
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setErrors
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

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
            screenState.setErrors(errors = listOf(UiSnackbar(message = UiTextHelper.StringResource(R.string.error_invalid_content_key))))
            screenState.updateState(ScreenState.NoData())
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
