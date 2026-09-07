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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class ReactionTestPhase {
    Idle,
    Waiting,
    Signal,
    Result,
    FalseStart,
}

enum class ReactionRating {
    Lightning,
    Fast,
    Average,
    Slow,
}

@Immutable
data class ReactionTestToolState(
    val phase: ReactionTestPhase = ReactionTestPhase.Idle,
    val lastReactionTimeMs: Long? = null,
    val bestTimeMs: Long? = null,
    val averageTimeMs: Long? = null,
    val history: ImmutableList<Long> = persistentListOf(),
    val roundCount: Int = 0,
    val totalRounds: Int = 5,
    val rating: ReactionRating? = null,
)
