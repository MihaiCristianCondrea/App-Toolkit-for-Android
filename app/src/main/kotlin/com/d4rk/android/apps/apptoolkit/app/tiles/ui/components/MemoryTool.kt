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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.MemoryInfo
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun MemoryTool(info: MemoryInfo?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (info != null) {
            val usedBytes = info.totalBytes - info.availableBytes
            val usedPercent = (usedBytes.toFloat() / info.totalBytes.toFloat()).coerceIn(0f, 1f)

            ResultPill(
                label = "${(usedBytes / 1024 / 1024 / 1024f).format(1)} GB / ${
                    (info.totalBytes / 1024 / 1024 / 1024f).format(
                        1
                    )
                } GB"
            )

            Box(contentAlignment = Alignment.Center) {
                androidx.compose.material3.CircularProgressIndicator(
                    progress = { usedPercent },
                    modifier = Modifier.size(120.dp),
                    strokeWidth = 12.dp,
                    color = if (info.isLowMemory) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${(usedPercent * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            ResultPill(label = stringResource(id = R.string.tile_preview_default_result))
        }
    }
}

private fun Float.format(digits: Int) = "%.${digits}f".format(java.util.Locale.ENGLISH, this)
