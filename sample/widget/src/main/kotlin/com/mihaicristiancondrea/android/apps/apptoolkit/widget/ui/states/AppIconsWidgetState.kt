/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.models.WidgetAppEntry
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface AppIconsWidgetState {
    data object Loading : AppIconsWidgetState
    data object Empty : AppIconsWidgetState
    data object Error : AppIconsWidgetState
    data class Content(val apps: ImmutableList<WidgetAppEntry>) : AppIconsWidgetState
}
