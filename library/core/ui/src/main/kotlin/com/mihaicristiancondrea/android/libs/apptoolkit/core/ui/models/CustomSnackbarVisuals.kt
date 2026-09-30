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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon

/** The Toolkit's two snackbar looks: the inverse surface of Material, and the error colours. */
enum class ToolkitSnackbarStyle {
    Normal,
    Error,
}

/**
 * Colours for one snackbar, replacing those of its [ToolkitSnackbarStyle].
 *
 * @property containerColor Behind the snackbar.
 * @property contentColor The message, the icon and the dismiss button.
 * @property actionColor The action's label.
 */
@Immutable
data class ToolkitSnackbarColors(
    val containerColor: Color,
    val contentColor: Color,
    val actionColor: Color,
)

/**
 * A custom implementation of [androidx.compose.material3.SnackbarVisuals] to provide additional styling options,
 * such as displaying an error state.
 *
 * @property message The primary text to be displayed in the snackbar.
 * @property actionLabel The text to be displayed for the action button. If null, no action will be shown.
 * @property withDismissAction Whether the snackbar should have a dismiss action.
 * @property duration The duration for which the snackbar will be displayed.
 * @property isError A boolean flag to indicate if the snackbar represents an error. This can be used
 * to apply different styling (e.g., a red background color) to the snackbar.
 * @property icon Drawn before the message.
 * @property colors Replace the colours of the snackbar's [style].
 * @property content Draws this snackbar instead of the Toolkit's, for a screen that wants its own.
 * It receives the snackbar's data, to perform its action or dismiss it.
 */
data class CustomSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val isError: Boolean = false,
    val icon: ToolkitIcon? = null,
    val colors: ToolkitSnackbarColors? = null,
    val content: (@Composable (SnackbarData) -> Unit)? = null,
) : SnackbarVisuals {
    /** The look [isError] picks. */
    val style: ToolkitSnackbarStyle
        get() = if (isError) ToolkitSnackbarStyle.Error else ToolkitSnackbarStyle.Normal
}
