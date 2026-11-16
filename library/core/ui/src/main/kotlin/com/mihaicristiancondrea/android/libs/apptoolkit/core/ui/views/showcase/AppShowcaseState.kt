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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

/** What an [AppShowcase] shows: the apps, or why there are none yet. */
@Immutable
sealed interface AppShowcaseState {
    data object Loading : AppShowcaseState
    data class Ready(val apps: ImmutableList<ShowcaseApp>) : AppShowcaseState
    data object Empty : AppShowcaseState
    data object Failed : AppShowcaseState
}
