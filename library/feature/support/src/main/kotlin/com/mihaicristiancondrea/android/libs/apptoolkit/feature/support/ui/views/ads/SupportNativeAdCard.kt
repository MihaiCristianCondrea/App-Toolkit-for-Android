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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.views.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdSlot

/**
 * Support-screen placement using the shared media-led [NativeAdSlot], which owns ad loading and
 * disposal.
 *
 * @param containerColor Optional host surface override.
 * @param onAdLoaded Reports whether an ad is currently displayed so the host can collapse the
 * slot.
 */
@Composable
fun SupportNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    containerColor: Color = Color.Unspecified,
    onAdLoaded: (Boolean) -> Unit = {},
) {
    NativeAdSlot(
        adUnitId = adUnitId,
        presentation = NativeAdPresentation.Featured,
        modifier = modifier,
        containerColor = containerColor,
        onAdLoaded = onAdLoaded,
    )
}
