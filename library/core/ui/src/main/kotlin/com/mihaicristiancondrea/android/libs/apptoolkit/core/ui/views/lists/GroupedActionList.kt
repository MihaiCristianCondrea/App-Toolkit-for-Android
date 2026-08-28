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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.ImmutableList

/**
 * One row of a [GroupedActionList]: what to show, and what to do when it is tapped.
 *
 * @param title Primary line, e.g. "Send a feature request".
 * @param description Supporting line under [title].
 * @param icon Leading icon.
 * @param onClick Invoked when the row is tapped.
 */
@Immutable
data class GroupedAction(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

/**
 * A card of tappable rows rendered as one visual group.
 *
 * The outer card holds inner cards whose corners round only at the ends of the group, so the rows
 * read as a single unit rather than as separate cards. It is the layout the help screen's feedback
 * sheet uses, extracted so other apps can present their own actions the same way.
 *
 * Corner rounding is derived from each item's index rather than passed in. A caller cannot label a
 * middle row as the last one, and a single-item list correctly rounds both ends, a case the
 * previous hand-passed first/middle/last enum could not express.
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
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(modifier = Modifier.padding(all = GROUP_PADDING)) {
            actions.forEachIndexed { index, action ->
                GroupedActionRow(
                    action = action,
                    isFirst = index == 0,
                    isLast = index == actions.lastIndex,
                    itemColor = itemColor,
                )
                if (index != actions.lastIndex) {
                    Spacer(modifier = Modifier.height(ITEM_SPACING))
                }
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
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(modifier = Modifier.padding(all = GROUP_PADDING), content = content)
    }
}

@Composable
private fun GroupedActionRow(
    action: GroupedAction,
    isFirst: Boolean,
    isLast: Boolean,
    itemColor: Color,
) {
    val topCorner = if (isFirst) SizeConstants.LargeSize else SizeConstants.ExtraTinySize
    val bottomCorner = if (isLast) SizeConstants.LargeSize else SizeConstants.ExtraTinySize

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = topCorner,
            topEnd = topCorner,
            bottomStart = bottomCorner,
            bottomEnd = bottomCorner,
        ),
        colors = CardDefaults.cardColors(containerColor = itemColor),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = action.onClick)
                .padding(horizontal = ROW_HORIZONTAL_PADDING, vertical = ROW_VERTICAL_PADDING),
        ) {
            Icon(imageVector = action.icon, contentDescription = null)
            Spacer(modifier = Modifier.width(ROW_ICON_SPACING))
            Column {
                Text(text = action.title)
                Text(
                    text = action.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private val GROUP_PADDING = 6.dp
private val ITEM_SPACING = 4.dp
private val ROW_HORIZONTAL_PADDING = 16.dp
private val ROW_VERTICAL_PADDING = 12.dp
private val ROW_ICON_SPACING = 12.dp
