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

/**
 * State rendered by the Dice Roll tool.
 *
 * @property result The face shown, from 1 to 6.
 * @property request Counts the rolls, so the die animates again when a number comes up twice.
 */
@Immutable
data class DiceRollToolUiState(
    val result: Int = 1,
    val request: Int = 0,
)
