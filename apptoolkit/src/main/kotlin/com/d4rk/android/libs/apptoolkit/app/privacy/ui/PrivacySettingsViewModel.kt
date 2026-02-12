package com.d4rk.android.libs.apptoolkit.app.privacy.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.privacy.ui.contract.PrivacySettingsAction
import com.d4rk.android.libs.apptoolkit.app.privacy.ui.contract.PrivacySettingsEvent
import com.d4rk.android.libs.apptoolkit.app.privacy.ui.state.PrivacySettingsUiState
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import kotlinx.coroutines.launch

/** ViewModel for the privacy settings screen lifecycle state. */
class PrivacySettingsViewModel(
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<PrivacySettingsUiState, PrivacySettingsEvent, PrivacySettingsAction>(
    initialState = UiStateScreen(data = PrivacySettingsUiState),
    firebaseController = firebaseController,
    screenName = "Privacy",
) {
    init {
        onEvent(PrivacySettingsEvent.Load)
    }

    override fun handleEvent(event: PrivacySettingsEvent) {
        when (event) {
            PrivacySettingsEvent.Load -> onLoad()
        }
    }

    private fun onLoad() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.setSuccess(
                    data = PrivacySettingsUiState,
                    newState = ScreenState.Success(),
                )
            }
        }
    }
}
