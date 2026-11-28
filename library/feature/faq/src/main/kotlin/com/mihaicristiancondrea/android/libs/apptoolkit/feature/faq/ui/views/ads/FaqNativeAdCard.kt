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

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdCallToActionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdSlot
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberNativeAdBadgeShape

/**
 * Displays a compact native ad styled for the FAQ question and support cards.
 * [NativeAdSlot] owns the ad lifecycle.
 *
 * @param adUnitId Native ad unit configured by the host.
 * @param shape Shape applied to the card surface.
 * @param containerColor Optional host surface override.
 * @param onAdLoaded Reports whether an ad is currently displayed.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FaqNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    shape: Shape = CardDefaults.shape,
    containerColor: Color = Color.Unspecified,
    onAdLoaded: (Boolean) -> Unit = {},
) {
    val style = NativeAdStyle(
        badgeShape = rememberNativeAdBadgeShape(
            shape = MaterialShapes.Cookie12Sided.toShape(),
            size = 48.dp,
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
        shape = shape,
        containerColor = containerColor,
        style = style,
        onAdLoaded = onAdLoaded,
    )
}
