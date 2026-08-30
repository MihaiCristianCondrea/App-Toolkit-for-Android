/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.data.repositories.ComponentsShowcaseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.contracts.ComponentsUnlockAction
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.contracts.ComponentsUnlockEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.states.ComponentsUnlockUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.updateState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * Coordinates unlocking the components showcase entry based on About screen taps.
 */
class ComponentsUnlockViewModel(
    private val componentsShowcaseRepository: ComponentsShowcaseRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<ComponentsUnlockUiState, ComponentsUnlockEvent, ComponentsUnlockAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = ComponentsUnlockUiState(),
    ),
    firebaseController = firebaseController,
    screenName = "ComponentsUnlock",
) {
    private var unlockRequested: Boolean = false

    init {
        handleEvent(ComponentsUnlockEvent.Initialize)
    }

    override fun handleEvent(event: ComponentsUnlockEvent) {
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
            componentsShowcaseRepository.unlock()
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
            .catchReport(action = Actions.HANDLE_VERSION_TAP) {
                // Error is reported by LoggedScreenViewModel
            }
            .launchIn(viewModelScope)
    }

    private object Actions {
        const val HANDLE_VERSION_TAP: String = "handleVersionTap"
    }

    private companion object {
        const val COMPONENTS_UNLOCK_TAP_THRESHOLD: Int = 7
    }
}
