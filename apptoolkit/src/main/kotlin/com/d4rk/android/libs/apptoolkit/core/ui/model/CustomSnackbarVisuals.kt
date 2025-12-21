package com.d4rk.android.libs.apptoolkit.core.ui.model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

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
 */
data class CustomSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val isError: Boolean = false
) : SnackbarVisuals