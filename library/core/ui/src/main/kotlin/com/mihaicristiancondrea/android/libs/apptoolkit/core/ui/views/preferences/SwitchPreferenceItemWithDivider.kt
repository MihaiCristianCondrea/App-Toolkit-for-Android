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

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry

/**
 * Preference row with a separately clickable switch. Row taps call [onClick]; switch changes
 * call [onCheckedChange]. The caller owns [checked] and persistence. Use [summary] to explain
 * what the switch controls.
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
    onSwitchClick: (Boolean) -> Unit,
    ga4Event: Ga4EventData? = null,
) {
    val telemetryRepository = LocalTelemetry.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                    telemetryRepository.logGa4Event(ga4Event)
                    onClick()
                }), verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                LargeHorizontalSpacer()
                Icon(imageVector = it, contentDescription = null)
                LargeHorizontalSpacer()
            }
            Column(
                modifier = Modifier
                    .padding(all = SizeConstants.LargeSize)
                    .weight(weight = 1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = summary, style = MaterialTheme.typography.bodyMedium)
            }
            ExtraSmallHorizontalSpacer()
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(size = SizeConstants.MediumSize),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            ExtraSmallHorizontalSpacer()
            VerticalDivider(
                modifier = Modifier
                    .height(height = SizeConstants.MediumSize * 3)
                    .align(alignment = Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                thickness = SizeConstants.ExtraTinySize / 2
            )
            CustomSwitch(
                checked = checked,
                onCheckedChange = { isChecked ->
                    telemetryRepository.logGa4Event(ga4Event)
                    onCheckedChange(isChecked)
                    onSwitchClick(isChecked)
                },
                modifier = Modifier.padding(all = SizeConstants.LargeSize)
            )
        }
    }
}
