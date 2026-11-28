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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.componentEntrance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch

/**
 * Switch preference backed by Material 3 [SegmentedListItem] with caller-owned [checked] state.
 * The whole row owns the toggle interaction and exposes [Role.Switch] semantics; callers handle
 * persistence. Use [summary] to explain the setting's effect.
 */
@Composable
fun SwitchPreferenceItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    summary: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    shapes: ListItemShapes = PreferenceItemDefaults.segmentedShapes(index = 0, count = 1),
    colors: ListItemColors = PreferenceItemDefaults.colors(),
    ga4Event: Ga4EventData? = null,
) {
    val telemetry = LocalTelemetry.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current

    SegmentedListItem(
        onClick = {
            val updatedChecked = !checked
            hapticFeedback.performHapticFeedback(
                hapticFeedbackType = if (updatedChecked) {
                    HapticFeedbackType.ToggleOn
                } else {
                    HapticFeedbackType.ToggleOff
                },
            )
            telemetry.logGa4Event(ga4Event)
            onCheckedChange(updatedChecked)
        },
        shapes = shapes,
        modifier = modifier
            .componentEntrance()
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .semantics {
                role = Role.Switch
                toggleableState = ToggleableState(value = checked)
            },
        enabled = enabled,
        colors = colors,
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
        leadingContent = icon?.let { vector ->
            { ControlRowLeadingIcon(imageVector = vector) }
        },
        supportingContent = summary?.let { text ->
            { PreferenceSummaryText(text = text) }
        },
        trailingContent = {
            CustomSwitch(
                checked = checked,
                onCheckedChange = null,
                enabled = enabled,
                modifier = Modifier.padding(start = MaterialTheme.spacing.largeIncreased),
            )
        },
        content = {
            PreferenceTitleText(text = title)
        },
    )
}
