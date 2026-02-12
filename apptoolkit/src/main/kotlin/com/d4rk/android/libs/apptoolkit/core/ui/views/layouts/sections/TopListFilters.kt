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

package com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.chip.CommonFilterChip
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallHorizontalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopListFilters(
    modifier: Modifier = Modifier,
    filters: ImmutableList<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    label: String = stringResource(id = R.string.sort_by),
    firebaseController: FirebaseController? = null,
    ga4EventProvider: ((String) -> Ga4EventData)? = null,
) {
    val listState: LazyListState = rememberLazyListState()
    val scope: CoroutineScope = rememberCoroutineScope()
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        LargeHorizontalSpacer()
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        LargeHorizontalSpacer()
        LazyRow(
            modifier = Modifier.weight(1f), state = listState
        ) {
            items(filters.size) { index: Int ->
                val filter: String = filters[index]
                CommonFilterChip(
                    selected = selectedFilter == filter,
                    onClick = {
                        onFilterSelected(filter)
                        scope.launch { listState.animateScrollToItem(index) }
                    },
                    label = filter,
                    firebaseController = firebaseController,
                    ga4Event = ga4EventProvider?.invoke(filter),
                )
                if (index < filters.size - 1) {
                    SmallHorizontalSpacer()
                }
            }
        }
    }
}
