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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

/** Floor that keeps the placeholder about the size of the ad it stands in for. */
private val PlaceholderMinHeight: Dp = 120.dp

/**
 * What an empty ad slot looks like on a debug build.
 *
 * In release an empty slot renders nothing at all, which is correct and also invisible: there is no
 * way to tell a slot that got no fill from a slot that never asked, or from a slot whose ad unit id
 * is wrong, without attaching a debugger. This is the same information the reporter writes to
 * Logcat, put where a developer is already looking.
 *
 * It draws only where [AdLoadReporter.showsDebugPlaceholder] is true, so it cannot reach a release
 * build. Callers own that check; this composable is only the drawing.
 *
 * @param slotName the placement, matching the name used in the logs.
 * @param reason why there is nothing to show.
 * @param detail the SDK's own words, when there are any.
 */
@Composable
fun AdSlotDebugPlaceholder(
    slotName: String,
    reason: AdSlotFailure,
    modifier: Modifier = Modifier,
    detail: String? = null,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = PlaceholderMinHeight),
        shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraSmallSize),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Campaign,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(size = SizeConstants.LargeSize),
                )
                Text(
                    text = "Ad slot: $slotName",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = SizeConstants.SmallSize),
                )
            }

            Text(
                text = when (reason) {
                    AdSlotFailure.NO_AD -> "No ad was returned. Usually no fill, which is normal."
                    AdSlotFailure.NOT_REQUESTED ->
                        "The request was never made. The Mobile Ads SDK was not ready."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (!detail.isNullOrBlank()) {
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = "Debug builds only. Release renders nothing here.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
