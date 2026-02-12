package com.d4rk.android.libs.apptoolkit.app.display.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.display.ui.contract.DisplaySettingsAction
import com.d4rk.android.libs.apptoolkit.app.display.ui.contract.DisplaySettingsEvent
import com.d4rk.android.libs.apptoolkit.app.display.ui.state.DisplaySettingsUiState
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import kotlinx.coroutines.launch

/** ViewModel for the display settings screen lifecycle state. */
class DisplaySettingsViewModel(
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<DisplaySettingsUiState, DisplaySettingsEvent, DisplaySettingsAction>(
    initialState = UiStateScreen(data = DisplaySettingsUiState),
    firebaseController = firebaseController,
    screenName = "DisplaySettings",
) {
    init {
        onEvent(DisplaySettingsEvent.Load)
    }

    override fun handleEvent(event: DisplaySettingsEvent) {
        when (event) {
            DisplaySettingsEvent.Load -> onLoad()
        }
    }

    private fun onLoad() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.setSuccess(
                    data = DisplaySettingsUiState,
                    newState = ScreenState.Success(),
                )
            }
        }
    }
}
