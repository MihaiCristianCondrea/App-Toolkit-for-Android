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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.switches.CustomSwitch
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry

/**
 * Switch card with caller-owned [switchState]. Card and switch taps emit [onSwitchToggled];
 * callers handle persistence. [title] supplies the primary label.
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
    val telemetryRepository = LocalTelemetry.current
    val view: View = LocalView.current
    Card(
        enabled = enabled,
        shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = modifier,
        onClick = {
            if (!enabled) return@Card
            val updatedValue = !switchState.value
            view.playSoundEffect(SoundEffectConstants.CLICK)
            telemetryRepository.logGa4Event(ga4EventProvider?.invoke(updatedValue) ?: ga4Event)
            onSwitchToggled(updatedValue)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = SizeConstants.LargeSize),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                icon?.let {
                    Icon(imageVector = it, contentDescription = null)
                    LargeHorizontalSpacer()
                }
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            }
            CustomSwitch(
                checked = switchState.value,
                enabled = enabled,
                onCheckedChange = { isChecked ->
                    telemetryRepository.logGa4Event(ga4EventProvider?.invoke(isChecked) ?: ga4Event)
                    onSwitchToggled(isChecked)
                },
                checkIcon = checkIcon
            )
        }
    }
}
