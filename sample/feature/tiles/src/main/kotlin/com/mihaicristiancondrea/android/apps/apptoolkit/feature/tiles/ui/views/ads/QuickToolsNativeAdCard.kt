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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdCallToActionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdSlot
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberNativeAdBadgeShape

/**
 * The Quick Tools ad, laid out by [QuickToolsNativeAdViewFactory] as the marketing kit draws it: a
 * "Sponsored content" title, then a widget card with the advertiser's icon in a nine-sided cookie on
 * the tertiary container and one tonal action. [NativeAdSlot] owns the ad's lifecycle; the slot
 * draws no container of its own because the card sits under the title.
 *
 * @param initiallyLoaded Suppresses the first `false` status when moving an already visible ad
 * from preloading into the list, preventing a transient layout gap.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QuickToolsNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    initiallyLoaded: Boolean = false,
    onStatusChanged: (Boolean) -> Unit = {},
) {
    var isFirstReport: Boolean by remember(adUnitId) { mutableStateOf(value = true) }

    val style = NativeAdStyle(
        badgeShape = rememberNativeAdBadgeShape(MaterialShapes.Cookie9Sided.toShape(), 88.dp),
        badgeColor = MaterialTheme.colorScheme.tertiaryContainer,
        iconInsetDp = 16,
        bodyMaxLines = 2,
        callToAction = NativeAdCallToActionStyle.Filled,
    )

    NativeAdSlot(
        adUnitId = adUnitId,
        presentation = NativeAdPresentation.Compact,
        modifier = modifier.fillMaxWidth(),
        showContainer = false,
        style = style,
        label = stringResource(R.string.quick_tools_sponsored_content),
        onAdLoaded = { isLoaded ->
            val suppressTransientReset: Boolean = isFirstReport && initiallyLoaded && !isLoaded
            isFirstReport = false
            if (!suppressTransientReset) {
                onStatusChanged(isLoaded)
            }
        },
    )
}
