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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperStateLine

@Composable
internal fun DeveloperStateItem(line: DeveloperStateLine, modifier: Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = if (line.selected) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        } else {
            CardDefaults.cardColors()
        },
    ) {
        Column(modifier = Modifier.padding(all = SizeConstants.LargeSize)) {
            Text(
                text = line.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(text = line.summary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
