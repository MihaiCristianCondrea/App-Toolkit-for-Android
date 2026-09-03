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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.ViewCarousel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSurface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.carousel.CustomCarousel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedActionList
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.MediumHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun LayoutShowcase(
    dropdownOptions: ImmutableList<String>,
    carouselState: PagerState,
) {
    ShowcaseHeader(
        title = "Feedback & Rich Layouts",
        icon = Icons.Outlined.Extension,
    )
    val primaryTitle = stringResource(id = R.string.components_preference_title)
    val primarySummary = stringResource(id = R.string.components_preference_summary)
    val secondaryTitle = stringResource(id = R.string.components_preference_secondary_title)
    val secondarySummary =
        stringResource(id = R.string.components_preference_secondary_summary)
    val groupedActions = remember(
        primaryTitle,
        primarySummary,
        secondaryTitle,
        secondarySummary,
    ) {
        persistentListOf(
            GroupedAction(
                title = primaryTitle,
                description = primarySummary,
                icon = Icons.Outlined.StarOutline,
                onClick = {},
            ),
            GroupedAction(
                title = secondaryTitle,
                description = secondarySummary,
                icon = Icons.Outlined.Info,
                onClick = {},
            ),
        )
    }
    ShowcaseSection {
        Text(
            text = "Grouped Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = SizeConstants.SmallSize),
        )
        GroupedActionList(
            actions = groupedActions,
            modifier = Modifier.groupedCorners(GroupedItemPosition.FIRST),
        )
        ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.ViewCarousel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
                MediumHorizontalSpacer()
                Text(
                    text = "Interactive Carousel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            SmallVerticalSpacer()
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
                shape = MaterialTheme.shapes.large,
            ) {
                CustomCarousel(
                    items = dropdownOptions,
                    sidePadding = SizeConstants.SmallSize,
                    pagerState = carouselState,
                ) { option ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SizeConstants.LargeSize),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = when (option) {
                                    "Alpha" -> Icons.Outlined.Favorite
                                    "Beta" -> Icons.Outlined.StarOutline
                                    else -> Icons.Outlined.Info
                                },
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            SmallVerticalSpacer()
                            Text(
                                text = option,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Sample variant $option content",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
        ShowcaseSurface(position = GroupedItemPosition.LAST) {
            InfoMessageSection(
                message = "This info section demonstrates integrated learning actions.",
                learnMoreText = "Documentation",
                learnMoreAction = {},
                newLine = false,
            )
        }
    }
}
