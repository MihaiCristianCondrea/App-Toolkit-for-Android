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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.models

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary

/**
 * Presentation-only row model for the app grid.
 *
 * The grid interleaves catalog entries with advertisement slots, which is a rendering concern; the
 * data and domain layers only ever deal in [AppSummary].
 */
@Immutable
sealed interface AppListItem {
    /** A catalog application rendered as an app card. */
    @Immutable
    data class App(val appInfo: AppSummary) : AppListItem

    /**
     * Represents an advertisement placeholder in the app list.
     */
    @Immutable
    data object Ad : AppListItem
}
