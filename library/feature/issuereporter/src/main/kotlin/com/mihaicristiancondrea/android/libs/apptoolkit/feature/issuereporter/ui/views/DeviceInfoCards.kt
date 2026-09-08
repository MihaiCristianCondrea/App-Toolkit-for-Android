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

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R

/**
 * Global expansion state for device info to coordinate between header and content cards.
 */
private var deviceExpansionState by mutableStateOf(false)

@Composable
internal fun DeviceInfoHeaderCard(
    firebaseController: FirebaseController,
    onExpandRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .groupedPreferenceItem(
                position = if (deviceExpansionState) GroupedItemPosition.FIRST else GroupedItemPosition.SINGLE,
                outerRadius = SizeConstants.LargeMediumSize
            )
            .bounceClick()
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)

                deviceExpansionState = !deviceExpansionState
                if (deviceExpansionState) onExpandRequested()

                firebaseController.logEvent(
                    issueReporterActionEvent(
                        actionName = IssueReporterActionNames.TOGGLE_DEVICE_INFO,
                        params = mapOf("expanded" to AnalyticsValue.Bool(deviceExpansionState)),
                    )
                )
            },
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SizeConstants.LargeSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.device_info),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = SizeConstants.LargeSize),
            )
            Icon(
                modifier = Modifier.padding(end = SizeConstants.LargeSize),
                imageVector = if (deviceExpansionState) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = stringResource(id = R.string.cd_expand_device_info),
            )
        }
    }
}

@Composable
internal fun DeviceInfoContentCard(
    deviceInfoText: String?,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = deviceExpansionState,
        enter = expandVertically() + expandHorizontally(expandFrom = Alignment.CenterHorizontally) + fadeIn(),
        exit = shrinkVertically() + shrinkHorizontally(shrinkTowards = Alignment.CenterHorizontally) + fadeOut(),
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .groupedPreferenceItem(
                    position = GroupedItemPosition.LAST,
                    outerRadius = SizeConstants.LargeMediumSize
                ),
            shape = RectangleShape
        ) {
            Box(
                modifier = Modifier
                    .padding(SizeConstants.LargeSize)
                    .horizontalScroll(rememberScrollState())
            ) {
                Text(
                    text = deviceInfoText.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
