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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.componentEntrance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch

/**
 * Split-action preference row backed by Material 3 [SegmentedListItem] with a separately
 * interactive trailing switch. Row taps call [onClick]; switch changes call [onCheckedChange].
 * The caller owns [checked] and persistence. Use [summary] to explain what the switch controls.
 */
@Composable
fun SwitchPreferenceItemWithDivider(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    summary: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shapes: ListItemShapes = PreferenceItemDefaults.segmentedShapes(index = 0, count = 1),
    colors: ListItemColors = PreferenceItemDefaults.colors(),
    ga4Event: Ga4EventData? = null,
) {
    val telemetry = LocalTelemetry.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current

    SegmentedListItem(
        onClick = {
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
            telemetry.logGa4Event(ga4Event)
            onClick()
        },
        shapes = shapes,
        modifier = modifier
            .componentEntrance()
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        enabled = enabled,
        colors = colors,
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
        leadingContent = icon?.let { vector ->
            { ControlRowLeadingIcon(imageVector = vector) }
        },
        supportingContent = {
            PreferenceSummaryText(text = summary)
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = MaterialTheme.spacing.small)
                        .size(size = 12.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
                ExtraSmallHorizontalSpacer()
                VerticalDivider(
                    modifier = Modifier.height(height = 36.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
                LargeHorizontalSpacer()
                CustomSwitch(
                    checked = checked,
                    enabled = enabled,
                    onCheckedChange = { isChecked: Boolean ->
                        telemetry.logGa4Event(ga4Event)
                        onCheckedChange(isChecked)
                    },
                )
            }
        },
        content = {
            PreferenceTitleText(text = title)
        },
    )
}
