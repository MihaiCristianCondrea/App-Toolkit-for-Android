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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.views

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.utils.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R

/**
 * Collapsible panel showing what will be attached to the report.
 *
 * Expansion state is owned here rather than by the screen: nothing outside this card reacts to it,
 * and [onExpandRequested] is what tells the caller to load the text lazily the first time.
 */
@Composable
internal fun DeviceInfoCard(
    deviceInfoText: String?,
    firebaseController: FirebaseController,
    onExpandRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    Card(shape = MaterialTheme.shapes.medium, modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            LargeHorizontalSpacer()

            Row(
                modifier = Modifier
                    .bounceClick()
                    .clickable {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)

                        expanded = !expanded
                        if (expanded) onExpandRequested()

                        firebaseController.logEvent(
                            issueReporterActionEvent(
                                actionName = IssueReporterActionNames.TOGGLE_DEVICE_INFO,
                                params = mapOf("expanded" to AnalyticsValue.Bool(expanded)),
                            )
                        )
                    }
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
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = stringResource(id = R.string.cd_expand_device_info),
                )
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = deviceInfoText.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(SizeConstants.LargeSize)
                        .horizontalScroll(rememberScrollState()),
                )
            }
        }
    }
}
