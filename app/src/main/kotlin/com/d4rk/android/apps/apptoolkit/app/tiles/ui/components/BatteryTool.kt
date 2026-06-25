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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.components

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BatteryChargingFull
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun BatteryTool() {
    val context = LocalContext.current
    var percent by remember { mutableIntStateOf(context.currentBatteryPercent()) }
    val safePercent = percent.coerceAtLeast(0)

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.BatteryChargingFull,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = if (percent >= 0) {
                    stringResource(id = R.string.tile_service_percent, safePercent)
                } else {
                    stringResource(id = R.string.tool_battery_unknown)
                },
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        LinearProgressIndicator(
            progress = { if (percent >= 0) safePercent / 100f else 0f },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedButton(onClick = { percent = context.currentBatteryPercent() }) {
            Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(SizeConstants.SmallSize))
            Text(text = stringResource(id = R.string.tool_battery_refresh))
        }
    }
}

private fun Context.currentBatteryPercent(): Int {
    val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level =
        batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, UNKNOWN_LEVEL) ?: UNKNOWN_LEVEL
    val scale =
        batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, DEFAULT_SCALE) ?: DEFAULT_SCALE
    return if (level >= 0 && scale > 0) level * 100 / scale else UNKNOWN_LEVEL
}

private const val UNKNOWN_LEVEL: Int = -1
private const val DEFAULT_SCALE: Int = 100
