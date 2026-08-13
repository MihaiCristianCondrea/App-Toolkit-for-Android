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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners

/**
 * The Compose container an ad is drawn in.
 *
 * Kept separate from [NativeAdRenderer] so the container is only ever composed once an ad exists:
 * the previous cards rendered their `OutlinedCard` shell up front and left an empty bordered card
 * behind whenever the load failed.
 *
 * @param position position inside a grouped list; [GroupedItemPosition.SINGLE] renders a standalone
 * rounded card.
 * @param showContainer `false` draws the ad with no card behind it, for hosts that already provide
 * their own surface.
 * @param containerColor overrides the card container for screens whose own cards are not default
 * cards. [Color.Unspecified] keeps the default.
 */
@Composable
internal fun NativeAdSurface(
    modifier: Modifier = Modifier,
    position: GroupedItemPosition = GroupedItemPosition.SINGLE,
    showContainer: Boolean = true,
    cornerRadius: Dp = SizeConstants.ExtraLargeSize,
    containerColor: Color = Color.Unspecified,
    content: @Composable () -> Unit,
) {
    if (!showContainer) {
        Box(modifier = modifier) { content() }
        return
    }

    val grouped: Boolean = position != GroupedItemPosition.SINGLE
    // The default is deliberately an unstyled card: an ad sits among ordinary content and should
    // read as ordinary content. Screens whose own cards are not default cards — and consumer apps
    // with their own surfaces — pass `containerColor` rather than getting a toolkit-wide tint that
    // is wrong everywhere else.
    Card(
        modifier = if (grouped) {
            modifier.groupedCorners(
                position = position,
                outerRadius = SizeConstants.ExtraLargeIncreasedSize,
            )
        } else {
            modifier
        },
        shape = if (grouped) RectangleShape else RoundedCornerShape(size = cornerRadius),
        colors = if (containerColor.isSpecified) {
            CardDefaults.cardColors(containerColor = containerColor)
        } else {
            CardDefaults.cardColors()
        },
    ) {
        content()
    }
}

/**
 * What an ad slot draws in `@Preview` and in the layout inspector, where no ad can load.
 */
@Composable
internal fun NativeAdPlaceholder(
    presentation: NativeAdPresentation,
    modifier: Modifier = Modifier,
    position: GroupedItemPosition = GroupedItemPosition.SINGLE,
    showContainer: Boolean = true,
    cornerRadius: Dp = SizeConstants.ExtraLargeSize,
    containerColor: Color = Color.Unspecified,
) {
    NativeAdSurface(
        modifier = modifier,
        position = position,
        showContainer = showContainer,
        cornerRadius = cornerRadius,
        containerColor = containerColor,
    ) {
        Box(
            modifier = if (presentation is NativeAdPresentation.Grid) {
                Modifier.fillMaxSize()
            } else {
                Modifier.fillMaxWidth()
            }.padding(all = SizeConstants.LargeSize),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.sponsored_ad_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
