package com.d4rk.android.libs.apptoolkit.app.startup.ui

import com.d4rk.android.libs.apptoolkit.app.startup.domain.actions.StartupAction
import com.d4rk.android.libs.apptoolkit.app.startup.domain.actions.StartupEvent
import com.d4rk.android.libs.apptoolkit.app.startup.ui.state.StartupUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel

class StartupViewModel : ScreenViewModel<StartupUiState, StartupEvent, StartupAction>(
    initialState = UiStateScreen(data = StartupUiState())
) {
    override fun onEvent(event : StartupEvent) {
        when (event) {
            StartupEvent.ConsentFormLoaded -> screenState.updateData(
                newState = ScreenState.Success()
            ) { current -> current.copy(consentFormLoaded = true) }

            StartupEvent.Continue -> sendAction(StartupAction.NavigateNext)
        }
    }
}
