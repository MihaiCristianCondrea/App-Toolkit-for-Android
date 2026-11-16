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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.runtime.Composable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ToolkitSnackbarColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ToolkitSnackbarStyle

object ToolkitSnackbarDefaults {

    /** Material's inverse surface for [ToolkitSnackbarStyle.Normal], the error colours for the other. */
    @Composable
    fun colors(style: ToolkitSnackbarStyle): ToolkitSnackbarColors = when (style) {
        ToolkitSnackbarStyle.Normal -> ToolkitSnackbarColors(
            containerColor = SnackbarDefaults.color,
            contentColor = SnackbarDefaults.contentColor,
            actionColor = SnackbarDefaults.actionColor,
        )

        ToolkitSnackbarStyle.Error -> ToolkitSnackbarColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error,
            actionColor = MaterialTheme.colorScheme.error,
        )
    }
}
