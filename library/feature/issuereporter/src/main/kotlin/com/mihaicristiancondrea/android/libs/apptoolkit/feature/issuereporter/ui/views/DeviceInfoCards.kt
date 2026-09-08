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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.getGroupedShape
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.issueReporterActionEvent

/**
 * What will be attached to the report, as two grouped components: a header and its content.
 *
 * Expansion state lives here, not in a file-level property: a top-level `mutableStateOf` is shared
 * by every instance and by every screen the process ever shows, so it survives navigation, leaks
 * between callers, and is not part of saved instance state. [rememberSaveable] keeps it per
 * instance and across configuration changes and process death instead.
 *
 * [onExpandRequested] tells the caller to load the text lazily the first time.
 */
@Composable
internal fun DeviceInfoSection(
    deviceInfoText: String?,
    firebaseController: FirebaseController,
    onExpandRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded: Boolean by rememberSaveable { mutableStateOf(value = false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        DeviceInfoHeaderCard(
            expanded = expanded,
            onToggle = {
                expanded = !expanded
                if (expanded) onExpandRequested()

                firebaseController.logEvent(
                    issueReporterActionEvent(
                        actionName = IssueReporterActionNames.TOGGLE_DEVICE_INFO,
                        params = mapOf("expanded" to AnalyticsValue.Bool(expanded)),
                    )
                )
            },
        )

        DeviceInfoContentCard(expanded = expanded, deviceInfoText = deviceInfoText)
    }
}

/**
 * Header of the device-info group.
 *
 * Only the arrow is interactive. The row itself carries no click handling and no press animation,
 * so the affordance and the touch target are the same thing, and the header does not react to a tap
 * meant for the text beside it.
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
    val bottomRadius: Dp by animateDpAsState(
        targetValue = if (expanded) SizeConstants.ExtraTinySize else ISSUE_GROUP_OUTER_RADIUS,
        label = "DeviceInfoHeaderCorners",
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = ISSUE_GROUP_OUTER_RADIUS,
            topEnd = ISSUE_GROUP_OUTER_RADIUS,
            bottomStart = bottomRadius,
            bottomEnd = bottomRadius,
        ),
        onClick = onToggle,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = SizeConstants.LargeSize,
                    end = SizeConstants.SmallSize,
                    top = SizeConstants.SmallSize,
                    bottom = SizeConstants.SmallSize,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.device_info),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(weight = 1f),
            )

            GeneralButton(
                onClick = onToggle,
                style = GeneralButtonStyle.Text,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.ExpandMore),
                contentDescription = stringResource(id = R.string.cd_expand_device_info),
                modifier = Modifier.rotate(degrees = arrowRotation),
            )
        }
    }
}

/**
 * Content of the device-info group.
 *
 * Vertical expansion only: growing horizontally as well made the card appear to unfold from its
 * centre, which read as a different component arriving rather than the header's own content opening
 * underneath it. [animateContentSize] covers the second growth, when the text itself arrives after
 * being captured lazily.
 */
@Composable
private fun DeviceInfoContentCard(
    expanded: Boolean,
    deviceInfoText: String?,
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
            shape = getGroupedShape(
                position = GroupedItemPosition.LAST,
                outerRadius = ISSUE_GROUP_OUTER_RADIUS,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(state = rememberScrollState())
                    .padding(all = SizeConstants.LargeSize),
            ) {
                Text(
                    text = deviceInfoText.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
