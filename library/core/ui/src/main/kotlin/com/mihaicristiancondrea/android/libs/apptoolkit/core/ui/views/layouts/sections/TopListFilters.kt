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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.chip.CommonFilterChip
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.componentEntrance
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A horizontally scrolling row of filter chips, optionally introduced by a caption.
 *
 * The row scrolls the chosen chip into view on selection, and animates chips in and out as the
 * available filters change, which they do on screens that only offer a filter once it would match
 * something. Pass `hasAnimation = false` to render those changes instantly instead.
 *
 * @param filters Chips to render, in display order.
 * @param selectedFilter Value of the currently selected chip.
 * @param onFilterSelected Invoked with the chip's value when one is chosen.
 * @param leadingLabel Caption before the chips, such as "Sort by". Omitted by default, and a null,
 *   empty, or blank value leaves neither the caption nor the gap after it, so the chips start where
 *   they would in a row that never had one. When shown, the caption takes the start inset of
 *   [contentPadding] and the chips do not repeat it.
 * @param contentPadding Inset around the row. It is applied to the scrolling area rather than to
 *   the row itself, so chips scroll under the edges instead of being clipped short of them. Pass
 *   `PaddingValues()` when the parent already insets this row.
 * @param hasAnimation Whether chip changes and the scroll-into-view animate.
 * @param ga4EventProvider Builds the GA4 event logged when a chip is chosen.
 */
@Composable
fun <T> TopListFilters(
    filters: ImmutableList<FilterChipItem<T>>,
    selectedFilter: T,
    onFilterSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    leadingLabel: String? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = MaterialTheme.spacing.large),
    hasAnimation: Boolean = true,
    ga4EventProvider: ((T) -> Ga4EventData)? = null,
) {
    val listState: LazyListState = rememberLazyListState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val layoutDirection: LayoutDirection = LocalLayoutDirection.current
    val startPadding = contentPadding.calculateStartPadding(layoutDirection)
    val endPadding = contentPadding.calculateEndPadding(layoutDirection)
    val hasLeadingLabel = !leadingLabel.isNullOrBlank()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacing.large),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (hasLeadingLabel) {
            Text(
                text = leadingLabel.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = startPadding),
            )
        }
        LazyRow(
            modifier = Modifier.weight(1f),
            state = listState,
            contentPadding = PaddingValues(
                start = if (hasLeadingLabel) MaterialTheme.spacing.none else startPadding,
                end = endPadding,
            ),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            itemsIndexed(
                items = filters,
                key = { _, item -> item.label },
            ) { index: Int, item: FilterChipItem<T> ->
                CommonFilterChip(
                    selected = selectedFilter == item.value,
                    onClick = {
                        onFilterSelected(item.value)
                        scope.launch {
                            if (hasAnimation) {
                                listState.animateScrollToItem(index)
                            } else {
                                listState.scrollToItem(index)
                            }
                        }
                    },
                    label = item.label,
                    modifier = (if (hasAnimation) Modifier.animateItem() else Modifier).componentEntrance(),
                    icon = item.icon,
                    hasAnimation = hasAnimation,
                    ga4Event = ga4EventProvider?.invoke(item.value),
                )
            }
        }
    }
}
