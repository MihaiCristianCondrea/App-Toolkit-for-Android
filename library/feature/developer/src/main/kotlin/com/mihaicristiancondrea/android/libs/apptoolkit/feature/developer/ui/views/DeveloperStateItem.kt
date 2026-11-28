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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItemDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperStateLine

@Composable
internal fun DeveloperStateItem(
    line: DeveloperStateLine,
    modifier: Modifier = Modifier,
    shapes: ListItemShapes = PreferenceItemDefaults.segmentedShapes(index = 0, count = 1),
) {
    SegmentedListItem(
        shapes = shapes,
        modifier = modifier.fillMaxWidth(),
        colors = if (line.selected) {
            PreferenceItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        } else {
            PreferenceItemDefaults.colors()
        },
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
        supportingContent = {
            Text(text = line.summary, style = MaterialTheme.typography.bodyMedium)
        },
        content = {
            Text(
                text = line.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        },
    )
}
