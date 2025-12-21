package com.d4rk.android.libs.apptoolkit.app.startup.ui

import com.d4rk.android.libs.apptoolkit.app.startup.ui.contract.StartupAction
import com.d4rk.android.libs.apptoolkit.app.startup.ui.contract.StartupEvent
import com.d4rk.android.libs.apptoolkit.app.startup.ui.state.StartupUiState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData

class StartupViewModel : ScreenViewModel<StartupUiState, StartupEvent, StartupAction>(
    initialState = UiStateScreen(data = StartupUiState())
) {
    override fun onEvent(event: StartupEvent) {
        when (event) {
            StartupEvent.ConsentFormLoaded -> screenState.updateData(
                newState = ScreenState.Success()
            ) { current -> current.copy(consentFormLoaded = true) }

            StartupEvent.Continue -> sendAction(StartupAction.NavigateNext)
        }
    }
}
