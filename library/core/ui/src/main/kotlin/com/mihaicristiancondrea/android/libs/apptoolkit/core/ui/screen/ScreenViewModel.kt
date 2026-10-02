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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base ViewModel for a screen: owns its state [S], handles its events [E], and queues the messages
 * it shows.
 *
 * [S] is whatever the feature declares, usually a data class. The status of each piece of content
 * that loads is a field of it, often a [Loadable], so a screen that loads several things gives
 * each its own status, and a screen that starts with its content needs none. Every change goes
 * through [setState], one atomic update, so coroutines writing at the same time never drop each
 * other's change.
 *
 * Messages are kept apart from [S] because every screen shows them the same way: [showMessage]
 * queues one and [MessageHost] shows the queue in order, calling [messageShown] as each leaves.
 * Being state rather than a one-off event, a message survives a configuration change.
 *
 * Subclasses implement [handleEvent]; [onEvent] is the entry point the UI calls.
 */
abstract class ScreenViewModel<S, E : Any>(initialState: S) : ViewModel() {

    private val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)

    val state: StateFlow<S> = mutableState.asStateFlow()

    protected val currentState: S
        get() = mutableState.value

    private val mutableMessages: MutableStateFlow<PersistentList<UiMessage>> =
        MutableStateFlow(persistentListOf())

    /** Messages waiting to be shown, oldest first. */
    val messages: StateFlow<ImmutableList<UiMessage>> = mutableMessages.asStateFlow()

    fun onEvent(event: E) {
        onEventReceived(event)
        handleEvent(event)
    }

    /** Handles an event the UI sent through [onEvent]. */
    protected abstract fun handleEvent(event: E)

    /**
     * Called with each event before [handleEvent]. Empty here; [LoggedScreenViewModel] logs the
     * event from it.
     */
    protected open fun onEventReceived(event: E) {}

    /** Replaces the state with [reduce] applied to it, atomically. */
    protected fun setState(reduce: S.() -> S) {
        mutableState.update(reduce)
    }

    /** Queues [message] to be shown after the ones already waiting. */
    protected fun showMessage(message: UiMessage) {
        mutableMessages.update { queue -> queue.add(message) }
    }

    /** Removes the message with [id] from the queue, once it has been shown. */
    fun messageShown(id: Long) {
        mutableMessages.update { queue -> queue.removeAll { it.id == id } }
    }

    /** Cancels this job, if any, and starts the one [start] returns. */
    protected fun Job?.restart(start: () -> Job): Job {
        this?.cancel()
        return start()
    }
}
