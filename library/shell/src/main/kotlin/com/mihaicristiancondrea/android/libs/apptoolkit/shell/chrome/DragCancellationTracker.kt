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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource

/** Tracks whether the latest drag interaction ended via cancellation rather than normal release. */
internal class DragCancellationTracker(
    private val delegate: MutableInteractionSource = MutableInteractionSource(),
) : MutableInteractionSource by delegate {
    var cancelled: Boolean = false
        private set

    override suspend fun emit(interaction: Interaction) {
        update(interaction)
        delegate.emit(interaction)
    }

    override fun tryEmit(interaction: Interaction): Boolean {
        update(interaction)
        return delegate.tryEmit(interaction)
    }

    private fun update(interaction: Interaction) {
        when (interaction) {
            is DragInteraction.Start -> cancelled = false
            is DragInteraction.Stop -> cancelled = false
            is DragInteraction.Cancel -> cancelled = true
        }
    }
}
