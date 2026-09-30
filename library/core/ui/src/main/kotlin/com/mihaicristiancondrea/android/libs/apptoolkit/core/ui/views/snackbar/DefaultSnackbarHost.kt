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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.CustomSnackbarVisuals
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ToolkitSnackbarColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ToolkitSnackbarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle

/**
 * Material 3 [SnackbarHost] that draws each snackbar the Toolkit's way, with [ToolkitSnackbar], or
 * with the [CustomSnackbarVisuals.content] a screen gave it.
 *
 * @param snackbarState State that holds the current snackbar data.
 * @param modifier Modifier applied to the [SnackbarHost].
 */
@Composable
fun DefaultSnackbarHost(snackbarState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = snackbarState, modifier = modifier) { snackbarData: SnackbarData ->
        val custom = (snackbarData.visuals as? CustomSnackbarVisuals)?.content
        if (custom != null) custom(snackbarData) else ToolkitSnackbar(snackbarData)
    }
}

/**
 * One snackbar as the Toolkit draws it: the colours of its [ToolkitSnackbarStyle] or its own, an
 * optional icon, the message, the action when it has one, and a dismiss button with the Toolkit's
 * sound and haptic feedback when it asks for one.
 *
 * Snackbars shown with plain [androidx.compose.material3.SnackbarVisuals] are drawn in the normal
 * style.
 */
@Composable
fun ToolkitSnackbar(data: SnackbarData, modifier: Modifier = Modifier) {
    val visuals = data.visuals
    val custom = visuals as? CustomSnackbarVisuals
    val colors = custom?.colors ?: ToolkitSnackbarDefaults.colors(custom?.style ?: ToolkitSnackbarStyle.Normal)
    val actionLabel = visuals.actionLabel
    Snackbar(
        modifier = modifier.padding(all = SizeConstants.LargeSize),
        containerColor = colors.containerColor,
        contentColor = colors.contentColor,
        action = actionLabel?.let { label ->
            {
                GeneralButton(
                    style = GeneralButtonStyle.Text,
                    onClick = data::performAction,
                    label = label,
                    contentColor = colors.actionColor,
                )
            }
        },
        dismissAction = if (visuals.withDismissAction) {
            {
                GeneralButton(
                    style = GeneralButtonStyle.Text,
                    onClick = data::dismiss,
                    icon = ToolkitIcon.Vector(Icons.Outlined.Close),
                    contentDescription = stringResource(android.R.string.cancel),
                    contentColor = colors.contentColor,
                )
            }
        } else {
            null
        },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            custom?.icon?.let { icon ->
                ToolkitIconContent(
                    icon = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(20.dp),
                    tint = colors.contentColor,
                )
            }
            Text(text = visuals.message)
        }
    }
}

/** The colours of the Toolkit's snackbar styles. */
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
