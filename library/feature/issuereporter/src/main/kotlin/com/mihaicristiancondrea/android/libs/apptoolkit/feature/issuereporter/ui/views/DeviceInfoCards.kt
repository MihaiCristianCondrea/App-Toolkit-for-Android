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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItemDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R

/**
 * What will be attached to the report, as two grouped components: a header and its content.
 * Expansion is saved per instance with [rememberSaveable].
 *
 * @param onExpandedChange Called with the new expansion after each toggle, so the caller can load
 * the details the first time and report the toggle.
 */
@Composable
internal fun DeviceInfoSection(
    deviceInfo: Loadable<String>,
    onExpandedChange: (expanded: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded: Boolean by rememberSaveable { mutableStateOf(value = false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = ListItemDefaults.SegmentedGap),
    ) {
        DeviceInfoHeaderCard(
            expanded = expanded,
            onToggle = {
                expanded = !expanded
                onExpandedChange(expanded)
            },
        )

        DeviceInfoContentCard(expanded = expanded, deviceInfo = deviceInfo)
    }
}

/**
 * Header of the device-info group.
 *
 * The entire card is a single interactive target so tapping anywhere on the header expands or
 * collapses the details without nesting a second clickable button inside the card.
 *
 * The bottom corners animate between the group's outer and inner radius, which is what keeps the
 * header joined to the content while expanded and a single rounded row while collapsed.
 */
@Composable
private fun DeviceInfoHeaderCard(
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val arrowRotation: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "DeviceInfoArrowRotation",
    )
    // Shape tokens expose no Dp to animate, so these mirror shapes.extraSmall and
    // shapes.largeIncreased, the seam and outer corners segmentedShapes gives the rest of the group.
    val bottomRadius: Dp by animateDpAsState(
        targetValue = if (expanded) 4.dp else 20.dp,
        label = "DeviceInfoHeaderCorners",
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.largeIncreased.copy(
            bottomStart = CornerSize(size = bottomRadius),
            bottomEnd = CornerSize(size = bottomRadius),
        ),
        onClick = onToggle,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = MaterialTheme.spacing.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.device_info),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(weight = 1f),
            )

            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = stringResource(id = R.string.cd_expand_device_info),
                modifier = Modifier.graphicsLayer { rotationZ = arrowRotation },
            )
        }
    }
}

/**
 * Device details with vertical expansion, keeping the group aligned to the surrounding form. A
 * failed capture shows its message in place of the details.
 */
@Composable
private fun DeviceInfoContentCard(
    expanded: Boolean,
    deviceInfo: Loadable<String>,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = PreferenceItemDefaults.segmentedShapes(index = 1, count = 2).shape,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(state = rememberScrollState())
                    .padding(all = MaterialTheme.spacing.large),
            ) {
                Text(
                    text = when (deviceInfo) {
                        is Loadable.Ready -> deviceInfo.value
                        is Loadable.Failed -> deviceInfo.message.asString()
                        Loadable.Loading, is Loadable.Empty -> ""
                    },
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
