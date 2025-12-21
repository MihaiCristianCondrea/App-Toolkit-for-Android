package com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.model.CustomSnackbarVisuals
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Material 3 [SnackbarHost] tailored for [CustomSnackbarVisuals].
 *
 * The host renders a dismiss action with sound and haptic feedback and
 * applies custom colors when the snackbar represents an error.
 *
 * @param snackbarState State that holds the current snackbar data.
 * @param modifier Modifier applied to the [SnackbarHost].
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DefaultSnackbarHost(snackbarState: SnackbarHostState, modifier: Modifier = Modifier) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    SnackbarHost(hostState = snackbarState, modifier = modifier) { snackbarData: SnackbarData ->
        (snackbarData.visuals as? CustomSnackbarVisuals)?.let { visuals: CustomSnackbarVisuals ->
            val isError: Boolean = visuals.isError
            Snackbar(
                modifier = Modifier.padding(all = SizeConstants.LargeSize),
                containerColor = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.inverseSurface,
                contentColor = if (isError) MaterialTheme.colorScheme.error else SnackbarDefaults.contentColor,
                action = {
                    IconButton(onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                        snackbarData.dismiss()
                    }, modifier = modifier.bounceClick(), shapes = IconButtonDefaults.shapes()) {
                        Icon(
                            modifier = Modifier.size(size = SizeConstants.ButtonIconSize),
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }) {
                Text(text = visuals.message)
            }
        }
    }
}