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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.ads

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdCallToActionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdSlot
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberNativeAdBadgeShape
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition

/**
 * Feature-owned native ad row styled to match the grouped FAQ questions and contact card.
 * [NativeAdSlot] owns loading and disposal; the screen owns placement policy.
 *
 * @param groupedPosition Position in a grouped FAQ section, or `null` when standalone.
 * @param containerColor Optional host surface override.
 * @param onAdLoaded Reports whether an ad is currently displayed.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FaqNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    groupedPosition: GroupedItemPosition? = null,
    containerColor: Color = Color.Unspecified,
    onAdLoaded: (Boolean) -> Unit = {},
) {
    val style = NativeAdStyle(
        badgeShape = rememberNativeAdBadgeShape(
            shape = MaterialShapes.Cookie12Sided.toShape(),
            size = SizeConstants.LauncherIconSize,
        ),
        badgeColor = MaterialTheme.colorScheme.primaryContainer,
        iconInsetDp = 12,
        headlineTextSizeSp = MaterialTheme.typography.titleMedium.fontSize.value,
        headlineBold = false,
        bodyTextSizeSp = MaterialTheme.typography.bodyMedium.fontSize.value,
        callToAction = NativeAdCallToActionStyle.Text,
    )

    NativeAdSlot(
        adUnitId = adUnitId,
        presentation = NativeAdPresentation.Compact,
        modifier = modifier,
        position = groupedPosition ?: GroupedItemPosition.SINGLE,
        cornerRadius = SizeConstants.MediumSize,
        containerColor = containerColor,
        style = style,
        onAdLoaded = onAdLoaded,
    )
}
