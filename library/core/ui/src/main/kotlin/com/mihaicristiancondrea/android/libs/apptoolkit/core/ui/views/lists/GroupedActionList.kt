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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.componentEntrance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItemDefaults
import kotlinx.collections.immutable.ImmutableList

/**
 * A card of tappable rows rendered as one visual group.
 *
 * The outer card holds Material 3 [SegmentedListItem] rows whose corners round only at the ends of
 * the group, so the rows read as a single unit rather than as separate cards. It is the layout the
 * help screen's feedback sheet uses, extracted so other apps can present their own actions the same
 * way.
 *
 * Corner rounding is derived from each item's index through
 * [PreferenceItemDefaults.segmentedShapes], so a single-item list rounds both ends and callers
 * cannot mislabel row positions.
 *
 * @param actions Rows to render, in order. An empty list renders nothing.
 */
@Composable
fun GroupedActionList(
    actions: ImmutableList<GroupedAction>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    itemColor: Color = MaterialTheme.colorScheme.surfaceContainer,
) {
    if (actions.isEmpty()) return

    Card(
        modifier = modifier
            .componentEntrance()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            modifier = Modifier.padding(all = MaterialTheme.spacing.extraSmallIncreased),
            verticalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacing.extraSmall),
        ) {
            actions.forEachIndexed { index, action ->
                SegmentedListItem(
                    onClick = action.onClick,
                    shapes = PreferenceItemDefaults.segmentedShapes(
                        index = index,
                        count = actions.size,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = itemColor,
                        leadingContentColor = contentColorFor(backgroundColor = itemColor),
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(
                        horizontal = MaterialTheme.spacing.large,
                        vertical = MaterialTheme.spacing.medium,
                    ),
                    leadingContent = {
                        Icon(imageVector = action.icon, contentDescription = null)
                    },
                    supportingContent = {
                        Text(text = action.description)
                    },
                    content = {
                        Text(text = action.title)
                    },
                )
            }
        }
    }
}

/**
 * Overload for callers that build their rows inline rather than from a list.
 *
 * Kept for call sites that need something other than a [GroupedAction] in a row; prefer the
 * list-based overload, which owns the corner rounding.
 */
@Composable
fun GroupedActionList(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier
            .componentEntrance()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            modifier = Modifier.padding(all = MaterialTheme.spacing.extraSmallIncreased),
            content = content,
        )
    }
}
