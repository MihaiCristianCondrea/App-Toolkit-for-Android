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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.UiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Base class for ViewModels used throughout the toolkit.
 *
 * The class exposes state updates via [uiState] and one-off events via
 * [actionEvent]. Concrete implementations handle incoming events through
 * [onEvent] and can emit new actions with [sendAction].
 *
 * @param S type representing the UI state
 * @param E type of events coming from the UI layer
 * @param A type of one-off actions to be processed by the UI
 * @param initialState initial value of the state
 */
abstract class BaseViewModel<S : UiState, E : UiEvent, A : ActionEvent>(initialState: S) :
    ViewModel() {

    private val stateMutex = Mutex()

    protected val uiStateFlow: MutableStateFlow<S> = MutableStateFlow(value = initialState)

    /** Current state exposed to the UI as a [StateFlow]. */
    val uiState: StateFlow<S> = uiStateFlow.asStateFlow()

    private val actions: MutableSharedFlow<A> =
        MutableSharedFlow(extraBufferCapacity = ACTION_BUFFER_CAPACITY)
    private val pendingActions: ArrayDeque<A> = ArrayDeque()
    private val pendingLock = Any()

    /**
     * One-off actions that the UI should react to.
     *
     * Every collector sees every action, because a screen and the Activity hosting it routinely
     * watch the same ViewModel and each act on a different part of what it emits. Actions sent
     * while nothing is collecting are held and handed to whatever subscribes first, so an Activity
     * that asks its ViewModel for work in `onCreate` and starts collecting a moment later still
     * hears the answer.
     */
    val actionEvent: Flow<A> = actions.onSubscription {
        drainPendingActions().forEach { action -> emit(action) }
    }

    protected val currentState: S
        get() = uiState.value

    /** Handles a new UI [event]. */
    abstract fun onEvent(event: E)

    /** Emits an [action] for the UI to handle. */
    fun sendAction(action: A) {
        synchronized(pendingLock) {
            if (actions.subscriptionCount.value == 0) {
                if (pendingActions.size >= ACTION_BUFFER_CAPACITY) pendingActions.removeFirst()
                pendingActions.addLast(action)
                return
            }
        }

        if (!actions.tryEmit(action)) {
            viewModelScope.launch { actions.emit(action) }
        }
    }

    private fun drainPendingActions(): List<A> = synchronized(pendingLock) {
        if (pendingActions.isEmpty()) {
            emptyList()
        } else {
            pendingActions.toList().also { pendingActions.clear() }
        }
    }

    /**
     * Updates the current UI state in a thread-safe manner using a [Mutex].
     *
     * This ensures that concurrent state updates do not result in race conditions,
     * guaranteeing atomicity when modifying the [uiStateFlow].
     *
     * @param update A lambda function containing the logic to update the state.
     */
    protected suspend fun updateStateThreadSafe(update: () -> Unit) {
        stateMutex.withLock {
            update()
        }
    }

    /**
     * Updates [UiStateScreen.data] only when [UiStateScreen.screenState] is [ScreenState.Success].
     *
     * Mirrors the original "updateSuccessState" pattern (mutex + success-only update),
     * adapted for [UiStateScreen] since [ScreenState] is not generic in this codebase.
     */
    protected suspend fun <T> updateSuccessState(
        screenData: MutableStateFlow<UiStateScreen<T>>,
        updateData: (T) -> T,
    ) {
        stateMutex.withLock {
            getSuccessData(screenData)?.let { data ->
                screenData.value = screenData.value.copy(data = updateData(data))
            }
        }
    }

    /**
     * Returns the current non-null [UiStateScreen.data] only when the state is [ScreenState.Success].
     */
    protected fun <T> getSuccessData(screenData: MutableStateFlow<UiStateScreen<T>>): T? {
        val current = screenData.value
        if (current.screenState !is ScreenState.Success) return null
        return current.data
    }

    private companion object {
        /** How many actions are held for a UI that has not started collecting yet. */
        const val ACTION_BUFFER_CAPACITY = 64
    }
}
