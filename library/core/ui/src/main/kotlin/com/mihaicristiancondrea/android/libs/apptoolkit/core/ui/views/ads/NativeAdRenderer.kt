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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.ads.mobile.sdk.nativead.MediaView
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R

/**
 * Renders [nativeAd] inside a programmatically built [NativeAdView].
 *
 * "No XML" does not mean "no `NativeAdView`". Ad assets must still be rendered inside a registered
 * [NativeAdView] with a real [MediaView] and a `registerNativeAd` call, drawing headline, icon and
 * CTA as pure Compose would break AdMob policy and click reporting. What the Kotlin renderer removes
 * is the layout inflater and `findViewById`, not the ad view.
 *
 * The view tree is created once per [presentation] in the `AndroidView` factory; the palette and the
 * ad are applied in `update`, so a theme change repaints without recreating the ad view.
 */
@Composable
internal fun NativeAdRenderer(
    presentation: NativeAdPresentation,
    nativeAd: NativeAd,
    palette: NativeAdPalette,
    modifier: Modifier = Modifier,
    style: NativeAdStyle = NativeAdStyle(),
    label: String? = null,
) {
    val factory = LocalNativeAdViewFactory.current
    val sponsoredLabel: String = label ?: stringResource(id = R.string.sponsored_ad_label_plain)

    // Grid cells are square and their content is centred, so the ad view has to fill the cell;
    // every other presentation wraps its own height.
    val sizeModifier: Modifier = if (presentation is NativeAdPresentation.Grid) {
        Modifier.fillMaxSize()
    } else {
        Modifier.fillMaxWidth()
    }

    key(presentation) {
        AndroidView(
            modifier = modifier.then(sizeModifier),
            factory = { context -> factory.createViewHolder(context, presentation).root },
            update = { view ->
                val holder: NativeAdViewHolder = view.holder ?: return@AndroidView
                holder.applyPalette(palette = palette, style = style)
                holder.bind(nativeAd = nativeAd, sponsoredLabel = sponsoredLabel)
            },
        )
    }
}

private val NativeAdView.holder: NativeAdViewHolder?
    get() = tag as? NativeAdViewHolder
