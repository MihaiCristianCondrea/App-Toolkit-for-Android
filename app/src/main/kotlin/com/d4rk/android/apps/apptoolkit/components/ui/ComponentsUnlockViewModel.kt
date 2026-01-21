package com.d4rk.android.apps.apptoolkit.components.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.components.domain.usecase.UnlockComponentsShowcaseUseCase
import com.d4rk.android.apps.apptoolkit.components.ui.contract.ComponentsUnlockAction
import com.d4rk.android.apps.apptoolkit.components.ui.contract.ComponentsUnlockEvent
import com.d4rk.android.apps.apptoolkit.components.ui.state.ComponentsUnlockUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * Coordinates unlocking the components showcase entry based on About screen taps.
 */
class ComponentsUnlockViewModel(
    private val unlockComponentsShowcase: UnlockComponentsShowcaseUseCase,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<ComponentsUnlockUiState, ComponentsUnlockEvent, ComponentsUnlockAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = ComponentsUnlockUiState(),
    )
) {
    private var unlockRequested: Boolean = false

    init {
        onEvent(ComponentsUnlockEvent.Initialize)
    }

    override fun onEvent(event: ComponentsUnlockEvent) {
        when (event) {
            ComponentsUnlockEvent.Initialize -> screenState.updateState(ScreenState.Success())
            is ComponentsUnlockEvent.VersionTapped -> handleVersionTap(event.tapCount)
        }
    }

    private fun handleVersionTap(tapCount: Int) {
        screenState.update { current ->
            current.copy(
                data = current.data?.copy(lastTapCount = tapCount)
            )
        }
        if (BuildConfig.DEBUG || unlockRequested) return
        if ((screenData?.isUnlocked == true) || tapCount < COMPONENTS_UNLOCK_TAP_THRESHOLD) return

        unlockRequested = true
        flow {
            unlockComponentsShowcase()
            emit(Unit)
        }
            .flowOn(dispatchers.io)
            .onEach {
                screenState.update { current ->
                    current.copy(
                        data = current.data?.copy(isUnlocked = true)
                    )
                }
            }
            .catch { throwable ->
                firebaseController.reportViewModelError(
                    viewModelName = "ComponentsUnlockViewModel",
                    action = "handleVersionTap",
                    throwable = throwable,
                )
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val COMPONENTS_UNLOCK_TAP_THRESHOLD: Int = 7
    }
}
