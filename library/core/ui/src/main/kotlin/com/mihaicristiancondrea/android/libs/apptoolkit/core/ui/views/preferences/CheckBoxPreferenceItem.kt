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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size.size
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.componentEntrance

/**
 * Checkbox preference backed by Material 3 [SegmentedListItem] with caller-owned [checked] state.
 * The row owns the toggle interaction; callers handle persistence. Provide a clear [title] and
 * supporting [summary].
 */
@Composable
fun CheckBoxPreferenceItem(
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
        checked = checked,
        onCheckedChange = { isChecked: Boolean ->
            hapticFeedback.performHapticFeedback(
                hapticFeedbackType = if (isChecked) {
                    HapticFeedbackType.ToggleOn
                } else {
                    HapticFeedbackType.ToggleOff
                },
            )
            telemetry.logGa4Event(ga4Event)
            onCheckedChange(isChecked)
        },
        shapes = shapes,
        modifier = modifier
            .componentEntrance()
            .fillMaxWidth(),
        enabled = enabled,
        colors = colors,
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(
            start = MaterialTheme.spacing.large,
            top = MaterialTheme.spacing.large,
            bottom = MaterialTheme.spacing.large,
        ),
        leadingContent = icon?.let { vector ->
            { ControlRowLeadingIcon(imageVector = vector) }
        },
        supportingContent = summary?.let { text ->
            { PreferenceSummaryText(text = text) }
        },
        trailingContent = {
            Box(
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.largeIncreased)
                    .width(width = MaterialTheme.size.extraExtraLarge),
                contentAlignment = Alignment.Center,
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = null,
                    enabled = enabled,
                )
            }
        },
        content = {
            PreferenceTitleText(text = title)
        },
    )
}
