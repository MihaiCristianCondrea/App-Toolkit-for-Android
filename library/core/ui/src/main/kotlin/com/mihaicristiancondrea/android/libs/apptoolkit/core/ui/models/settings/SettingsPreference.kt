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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * One row of a settings list.
 *
 * @property key Identifies the row for analytics and as the list item key.
 * @property destination The page the row opens, such as `DisplaySettingsRoute`. In a list page it
 *   opens beside the list on wide windows when the page is a detail.
 * @property action Something the row does outside the app, such as opening the system's
 *   notification settings. It returns whether it handled the click; when it did not, the row opens
 *   [destination] instead, so a system screen that is missing falls back to the app's own page.
 */
@Immutable
data class SettingsPreference(
    val key: String? = null,
    val icon: ImageVector? = null,
    val useIconContainer: Boolean = false,
    val iconColor: Color? = null,
    val iconContainerColor: Color? = null,
    val title: String? = null,
    val summary: String? = null,
    val destination: NavKey? = null,
    val action: (() -> Boolean)? = null,
)

