package com.d4rk.android.libs.apptoolkit.app.settings.general.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.actions.GeneralSettingsAction
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.actions.GeneralSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.state.GeneralSettingsUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.copyData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.setErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.setLoading
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class GeneralSettingsViewModel(
    private val repository: GeneralSettingsRepository
) : ScreenViewModel<GeneralSettingsUiState, GeneralSettingsEvent, GeneralSettingsAction>(
    initialState = UiStateScreen(data = GeneralSettingsUiState())
) {

    override fun onEvent(event : GeneralSettingsEvent) {
        when (event) {
            is GeneralSettingsEvent.Load -> loadContent(contentKey = event.contentKey)
        }
    }

    private var loadJob : Job? = null

    private fun loadContent(contentKey: String?) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getContentKey(contentKey)
                .onStart { screenState.setLoading() }
                .onCompletion { cause ->
                    if (cause == null) {
                        screenState.updateState(newValues = ScreenState.Success())
                    }
                }
                .catch {
                    screenState.setErrors(
                        errors = listOf(
                            UiSnackbar(
                                message = UiTextHelper.StringResource(
                                    resourceId = R.string.error_invalid_content_key
                                )
                            )
                        )
                    )
                    screenState.updateState(newValues = ScreenState.NoData())
                }
                .collect { key ->
                    screenState.setErrors(errors = emptyList())
                    screenState.copyData {
                        copy(contentKey = key)
                    }
                }
        }
    }
}