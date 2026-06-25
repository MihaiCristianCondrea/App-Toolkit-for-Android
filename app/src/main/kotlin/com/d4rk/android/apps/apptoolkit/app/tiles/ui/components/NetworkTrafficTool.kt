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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.NetworkTraffic
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun NetworkTrafficTool(traffic: NetworkTraffic?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (traffic != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Outlined.ArrowDownward, contentDescription = null)
                    Text(
                        text = traffic.rxBytesPerSecond.toSpeedString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "Download", style = MaterialTheme.typography.labelSmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Outlined.ArrowUpward, contentDescription = null)
                    Text(
                        text = traffic.txBytesPerSecond.toSpeedString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "Upload", style = MaterialTheme.typography.labelSmall)
                }
            }
        } else {
            ResultPill(label = stringResource(id = R.string.tile_preview_default_result))
        }
    }
}

private fun Long.toSpeedString(): String {
    val kb = this / 1024f
    return if (kb > 1024) {
        "${(kb / 1024f).format(1)} MB/s"
    } else {
        "${kb.toInt()} KB/s"
    }
}

private fun Float.format(digits: Int) = "%.${digits}f".format(java.util.Locale.ENGLISH, this)
