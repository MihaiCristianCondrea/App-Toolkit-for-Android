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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.componentEntrance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch

/**
 * Prominent toggle card backed by Material 3 [ListItem] with caller-owned [switchState]. The whole
 * card owns the toggle interaction and exposes [Role.Switch] semantics; callers handle persistence.
 * [title] supplies the primary label.
 *
 * @param ga4EventProvider Resolves analytics from the toggled value at interaction time, taking
 * precedence over [ga4Event] when it returns an event.
 */
@Composable
fun SwitchCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    enabled: Boolean = true,
    switchState: State<Boolean>,
    onSwitchToggled: (Boolean) -> Unit,
    checkIcon: ImageVector = Icons.Filled.Check,
    ga4Event: Ga4EventData? = null,
    ga4EventProvider: ((Boolean) -> Ga4EventData?)? = null,
) {
    val telemetry = LocalTelemetry.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val checked: Boolean = switchState.value
    val pillShape = MaterialTheme.shapes.extraLarge

    ListItem(
        onClick = {
            if (!enabled) return@ListItem
            val updatedValue = !checked
            hapticFeedback.performHapticFeedback(
                hapticFeedbackType = if (updatedValue) {
                    HapticFeedbackType.ToggleOn
                } else {
                    HapticFeedbackType.ToggleOff
                },
            )
            telemetry.logGa4Event(ga4EventProvider?.invoke(updatedValue) ?: ga4Event)
            onSwitchToggled(updatedValue)
        },
        modifier = modifier
            .componentEntrance()
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .semantics {
                role = Role.Switch
                toggleableState = ToggleableState(value = checked)
            },
        enabled = enabled,
        shapes = ListItemShapes(
            shape = pillShape,
            selectedShape = pillShape,
            pressedShape = pillShape,
            focusedShape = pillShape,
            hoveredShape = pillShape,
            draggedShape = pillShape,
        ),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            leadingContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            trailingContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.38f),
        ),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
        leadingContent = icon?.let { vector ->
            {
                Icon(
                    imageVector = vector,
                    contentDescription = null,
                    modifier = Modifier.padding(end = MaterialTheme.spacing.extraSmall),
                )
            }
        },
        trailingContent = {
            CustomSwitch(
                checked = checked,
                enabled = enabled,
                onCheckedChange = null,
                checkIcon = checkIcon,
            )
        },
        content = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
            )
        },
    )
}
