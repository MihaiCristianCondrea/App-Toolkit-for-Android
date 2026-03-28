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

package com.d4rk.android.libs.apptoolkit.app.main.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey

/**
 * Represents an item rendered in bottom navigation surfaces (bottom bar and navigation rail).
 *
 * @property route Stable route key used for navigation.
 * @property icon Icon displayed when the destination is not selected.
 * @property selectedIcon Icon displayed when the destination is selected.
 * @property title String resource id used for both label and content description.
 * @property badgeText Optional badge text. When blank, no badge is shown.
 */
@Immutable
data class BottomBarItem<T : StableNavKey>(
    val route: T,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val title: Int,
    val badgeText: String = "",
)
