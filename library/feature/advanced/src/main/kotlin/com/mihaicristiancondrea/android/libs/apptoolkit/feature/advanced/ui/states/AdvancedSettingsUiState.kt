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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states

import androidx.compose.runtime.Immutable

/**
 * Everything the advanced settings page renders. Its rows are there from the start, so nothing
 * here loads.
 *
 * @property cacheClear Where the last cache clear stands, reported as the page's `screen_state`.
 * @property developerOptionsUnlocked Whether the About screen's version easter egg has been found,
 * which offers the developer options row.
 */
@Immutable
data class AdvancedSettingsUiState(
    val cacheClear: CacheClearStatus = CacheClearStatus.Idle,
    val developerOptionsUnlocked: Boolean = false,
)
