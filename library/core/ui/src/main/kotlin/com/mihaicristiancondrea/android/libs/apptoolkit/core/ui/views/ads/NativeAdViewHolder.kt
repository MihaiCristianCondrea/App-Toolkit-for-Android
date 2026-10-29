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

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.isVisible
import com.google.android.libraries.ads.mobile.sdk.nativead.MediaView
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView

/**
 * Strong references to the views a native ad binds to, so binding never needs `findViewById`.
 *
 * @param decorate Runs at the end of [applyPalette], for a custom [NativeAdViewFactory] whose layout
 * has views the holder does not know about (a card behind part of the ad, a header icon) or that
 * finishes a known view differently (a disclosure label drawn as a title instead of a chip). It runs
 * on every repaint, so it follows theme changes the same way the holder's own views do.
 */
class NativeAdViewHolder(
    val presentation: NativeAdPresentation,
    val root: NativeAdView,
    val content: LinearLayout,
    val label: TextView,
    val media: MediaView?,
    val mediaFrame: View?,
    val icon: ImageView?,
    val iconFrame: View?,
    val headline: TextView,
    val body: TextView?,
    val advertiser: TextView,
    val callToAction: TextView?,
    private val decorate: (NativeAdViewHolder.(palette: NativeAdPalette, style: NativeAdStyle) -> Unit)? = null,
) {
    private val initialIconPadding = iconFrame?.let {
        Rect(it.paddingLeft, it.paddingTop, it.paddingRight, it.paddingBottom)
    }
    private val initialIconScaleType = icon?.scaleType
    private val initialIconClipToOutline = iconFrame?.clipToOutline ?: false

    init {
        root.tag = this
    }

    fun applyPalette(palette: NativeAdPalette, style: NativeAdStyle = NativeAdStyle()) {
        label.setTextColor(palette.primary)
        // The shallow bar strip has no room for the disclosure chip used by card presentations.
        label.background = if (presentation is NativeAdPresentation.BarRow) {
            null
        } else {
            roundedDrawable(
                color = palette.surfaceVariant,
                radiusPx = label.context.dp(value = LABEL_CORNER_RADIUS_DP),
            )
        }
        headline.setTextColor(style.headlineColor.orArgb(fallback = palette.onSurface))
        style.headlineTextSizeSp?.let { size ->
            headline.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        }
        style.headlineBold?.let { bold ->
            headline.useAppFont(weight = if (bold) FONT_WEIGHT_BOLD else FONT_WEIGHT_REGULAR)
        }

        body?.let { bodyView ->
            bodyView.setTextColor(style.bodyColor.orArgb(fallback = palette.onSurfaceVariant))
            style.bodyTextSizeSp?.let { size ->
                bodyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
            }
            style.bodyMaxLines?.let { lines -> bodyView.maxLines = lines }
        }
        advertiser.setTextColor(palette.onSurfaceVariant)

        iconFrame?.let { frame ->
            // Update the badge in place so shape and color changes do not recreate the ad view.
            val badgeColor: Int = style.badgeColor.orArgb(fallback = palette.surfaceVariant)
            val badgeShape: NativeAdBadgeShape? = style.badgeShape
            val iconInset: Int? = style.iconInsetDp?.let { frame.context.dp(it.coerceAtLeast(0)) }
            if (iconInset != null) {
                frame.setPadding(iconInset, iconInset, iconInset, iconInset)
                icon?.scaleType = ImageView.ScaleType.FIT_CENTER
            } else {
                initialIconPadding?.let { padding ->
                    frame.setPadding(padding.left, padding.top, padding.right, padding.bottom)
                }
                icon?.scaleType = initialIconScaleType
            }
            // A rounded outline would cut off parts of an arbitrary badge silhouette.
            frame.clipToOutline = badgeShape == null && initialIconClipToOutline

            frame.background = if (badgeShape != null) {
                pathDrawable(
                    color = badgeColor,
                    path = badgeShape.path,
                    sourceSize = badgeShape.sizePx,
                )
            } else {
                roundedDrawable(
                    color = badgeColor,
                    radiusPx = frame.context.dp(
                        value = style.badgeCornerRadiusDp ?: ICON_CORNER_RADIUS_DP,
                    ),
                )
            }
        }
        mediaFrame?.let { frame ->
            frame.background = roundedDrawable(
                color = palette.surfaceVariant,
                radiusPx = frame.context.dp(value = MEDIA_CORNER_RADIUS_DP),
            )
        }
        callToAction?.let { cta ->
            when (style.callToAction) {
                NativeAdCallToActionStyle.Filled -> {
                    cta.background = roundedDrawable(
                        color = palette.secondaryContainer,
                        radiusPx = cta.context.dp(value = CTA_CORNER_RADIUS_DP),
                    )
                    cta.setTextColor(palette.onSecondaryContainer)
                    cta.setPadding(
                        cta.context.dp(CTA_HORIZONTAL_PADDING_DP),
                        cta.context.dp(CTA_VERTICAL_PADDING_DP),
                        cta.context.dp(CTA_HORIZONTAL_PADDING_DP),
                        cta.context.dp(CTA_VERTICAL_PADDING_DP),
                    )
                }

                NativeAdCallToActionStyle.Text -> {
                    // Remove pill padding with the background so the CTA aligns with host text buttons.
                    cta.background = null
                    cta.setTextColor(palette.primary)
                    cta.setPadding(
                        cta.context.dp(CTA_TEXT_HORIZONTAL_PADDING_DP),
                        cta.context.dp(CTA_VERTICAL_PADDING_DP),
                        cta.context.dp(CTA_TEXT_HORIZONTAL_PADDING_DP),
                        cta.context.dp(CTA_VERTICAL_PADDING_DP),
                    )
                }
            }
        }
        if (presentation is NativeAdPresentation.BarRow) {
            content.setBackgroundColor(palette.surfaceContainer)
        }
        decorate?.invoke(this, palette, style)
    }

    fun bind(nativeAd: NativeAd, sponsoredLabel: String) {
        label.text = sponsoredLabel

        media?.let { mediaView ->
            mediaView.mediaContent = nativeAd.mediaContent
            mediaView.isVisible = true
        }

        root.headlineView = headline
        headline.text = nativeAd.headline

        body?.let { bodyView ->
            root.bodyView = bodyView
            bodyView.bindOptionalText(text = nativeAd.body)
        }

        root.advertiserView = advertiser
        advertiser.bindOptionalText(text = nativeAd.advertiser)

        icon?.let { iconView ->
            root.iconView = iconView
            val drawable = nativeAd.icon?.drawable
            iconView.setImageDrawable(drawable)
            iconFrame?.isVisible = drawable != null
            iconView.isVisible = drawable != null
        }

        callToAction?.let { cta ->
            root.callToActionView = cta
            cta.bindOptionalText(text = nativeAd.callToAction)
        }

        root.registerNativeAd(nativeAd, media)
    }
}

private fun Color.orArgb(fallback: Int): Int = if (isSpecified) toArgb() else fallback

private fun TextView.bindOptionalText(text: CharSequence?) {
    if (text.isNullOrEmpty()) {
        isVisible = false
    } else {
        this.text = text
        isVisible = true
    }
}
