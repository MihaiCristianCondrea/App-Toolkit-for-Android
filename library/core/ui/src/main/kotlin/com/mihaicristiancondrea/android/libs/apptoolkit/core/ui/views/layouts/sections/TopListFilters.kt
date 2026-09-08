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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.chip.CommonFilterChip
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * One chip in a [TopListFilters] row.
 *
 * @property value The filter this chip selects. Rows compare it against the current selection, so
 *   any type with a meaningful `equals` works, an enum entry being the usual one.
 * @property label Resolved chip text. Rows key their items on it, so keep it unique within a row.
 * @property icon Icon shown while the chip is not selected. Selected chips show a checkmark.
 */
@Immutable
data class FilterChipItem<T>(
    val value: T,
    val label: String,
    val icon: ToolkitIcon? = null,
)

/**
 * A horizontally scrolling row of filter chips with an optional leading label.
 *
 * The row scrolls the chosen chip into view on selection, and animates chips in and out as the
 * available filters change, which they do on screens that only offer a filter once it would match
 * something. Pass `hasAnimation = false` to render those changes instantly instead.
 *
 * @param filters Chips to render, in display order.
 * @param selectedFilter Value of the currently selected chip.
 * @param onFilterSelected Invoked with the chip's value when one is chosen.
 * @param modifier The [Modifier] applied to the row.
 * @param label Text before the chips. Pass `null` for a row of chips alone.
 * @param contentPadding Inset around the row. It is applied to the scrolling area rather than to
 *   the row itself, so chips scroll under the edges instead of being clipped short of them. Pass
 *   `PaddingValues()` when the parent already insets this row.
 * @param hasAnimation Whether chip changes and the scroll-into-view animate.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4EventProvider Builds the GA4 event logged when a chip is chosen.
 */
@Composable
fun <T> TopListFilters(
    filters: ImmutableList<FilterChipItem<T>>,
    selectedFilter: T,
    onFilterSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = stringResource(id = R.string.sort_by),
    contentPadding: PaddingValues = PaddingValues(horizontal = SizeConstants.LargeSize),
    hasAnimation: Boolean = true,
    firebaseController: FirebaseController? = null,
    ga4EventProvider: ((T) -> Ga4EventData)? = null,
) {
    val listState: LazyListState = rememberLazyListState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val layoutDirection: LayoutDirection = LocalLayoutDirection.current
    val startPadding = contentPadding.calculateStartPadding(layoutDirection)
    val endPadding = contentPadding.calculateEndPadding(layoutDirection)
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = startPadding),
            )
            LargeHorizontalSpacer()
        }
        LazyRow(
            modifier = Modifier.weight(1f),
            state = listState,
            // The start inset belongs to the label when there is one, so it is not applied twice.
            contentPadding = PaddingValues(
                start = if (label == null) startPadding else SizeConstants.ZeroSize,
                end = endPadding,
            ),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
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
                    modifier = if (hasAnimation) Modifier.animateItem() else Modifier,
                    icon = item.icon,
                    hasAnimation = hasAnimation,
                    firebaseController = firebaseController,
                    ga4Event = ga4EventProvider?.invoke(item.value),
                )
            }
        }
    }
}
