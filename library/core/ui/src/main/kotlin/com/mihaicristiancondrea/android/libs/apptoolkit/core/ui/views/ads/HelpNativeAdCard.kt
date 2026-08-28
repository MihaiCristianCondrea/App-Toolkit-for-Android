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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition

/**
 * Icon-led native ad row used inside the grouped Help content list.
 *
 * Change rationale: this used to inflate `R.layout.native_ad_help_card` through `NativeAdViewHost`
 * and bind it with `findViewById`. It is now a thin wrapper over [NativeAdSlot], which builds the
 * same `NativeAdView` in Kotlin, see [SupportNativeAdCard] for the behaviour changes that come
 * with the shared renderer.
 *
 * Integration and compliance notes:
 * - Render this composable only after consent/ads settings allow ad serving.
 * - Placement policy stays with the screen; this is a view-layer primitive.
 *
 * @param groupedPosition position inside a grouped Help section, or `null` when standalone.
 * @param containerColor overrides the card container for hosts whose surfaces are their own.
 * @param onAdLoaded reports whether an ad is currently displayed.
 */
@Composable
fun HelpNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    groupedPosition: GroupedItemPosition? = null,
    containerColor: Color = Color.Unspecified,
    onAdLoaded: (Boolean) -> Unit = {},
) {
    NativeAdSlot(
        adUnitId = adUnitId,
        presentation = NativeAdPresentation.Compact,
        modifier = modifier,
        position = groupedPosition ?: GroupedItemPosition.SINGLE,
        // Matches QuestionCard, the item this row is interleaved with.
        cornerRadius = SizeConstants.MediumSize,
        containerColor = containerColor,
        onAdLoaded = onAdLoaded,
    )
}
