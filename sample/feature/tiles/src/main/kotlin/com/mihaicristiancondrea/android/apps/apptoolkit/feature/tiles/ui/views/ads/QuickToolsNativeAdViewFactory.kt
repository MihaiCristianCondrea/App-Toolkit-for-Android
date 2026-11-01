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

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.DefaultNativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.FONT_WEIGHT_MEDIUM
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.FONT_WEIGHT_SEMI_BOLD
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdViewHolder
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.advertiserView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.bodyView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.callToActionView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.headlineView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.iconFrameView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.nativeAdRoot
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.roundedDrawable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.sponsoredLabelView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.useAppFont
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.withTopMargin

/**
 * The Quick Tools ad as the marketing kit draws it (`.qt-ad` in marketing/ui/src/kit/kit.css): a
 * "Sponsored content" title like the sections' own, then a widget card with the advertiser's icon
 * in a shape, its headline, line and name, and one full-width tonal action.
 *
 * The title is the ad's disclosure label, so it stays inside the `NativeAdView` as AdMob requires;
 * it is only drawn as a section title instead of a chip. Every other presentation keeps the
 * library's layout.
 */
class QuickToolsNativeAdViewFactory : NativeAdViewFactory {
    private val defaultFactory = DefaultNativeAdViewFactory()

    override fun createViewHolder(context: Context, presentation: NativeAdPresentation): NativeAdViewHolder =
        if (presentation == NativeAdPresentation.Compact) createQuickToolsAd(context) else defaultFactory.createViewHolder(context, presentation)
}

private fun createQuickToolsAd(context: Context): NativeAdViewHolder {
    val root = nativeAdRoot(context)
    val content = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    // The section title: a 20dp icon in the primary colour and the label in the title type, as ToolSection draws it.
    val header = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPaddingRelative(context.dp(4), 0, 0, 0)
    }
    val headerIcon = ImageView(context).apply {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sponsored_content))
        importantForAccessibility = ImageView.IMPORTANT_FOR_ACCESSIBILITY_NO
        layoutParams = LinearLayout.LayoutParams(context.dp(20), context.dp(20))
    }
    val label = sponsoredLabelView(context).apply {
        isAllCaps = false
        setPadding(0, 0, 0, 0)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        useAppFont(weight = FONT_WEIGHT_SEMI_BOLD)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            .apply { marginStart = context.dp(8) }
    }
    header.addView(headerIcon)
    header.addView(label)

    // The widget card: 20dp in, the face and the text side by side, the action across the bottom.
    val card = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        val padding = context.dp(20)
        setPadding(padding, padding, padding, padding)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            .apply { topMargin = context.dp(12) }
    }
    val row = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }
    val iconFrame = iconFrameView(context, sizeDp = FACE_SIZE_DP)
    val icon = iconFrame.getChildAt(0) as ImageView
    val texts = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply { marginStart = context.dp(16) }
    }
    val headline = headlineView(context, maxLines = 2).apply {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        useAppFont(weight = FONT_WEIGHT_SEMI_BOLD)
    }
    val body = bodyView(context, maxLines = 2).withTopMargin(context.dp(2))
    val advertiser = advertiserView(context).withTopMargin(context.dp(4)).apply {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        useAppFont(weight = FONT_WEIGHT_MEDIUM)
    }
    texts.addView(headline)
    texts.addView(body)
    texts.addView(advertiser)
    row.addView(iconFrame)
    row.addView(texts)

    val callToAction = callToActionView(context).apply {
        minHeight = context.dp(40)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            .apply { topMargin = context.dp(16) }
    }
    card.addView(row)
    card.addView(callToAction)

    content.addView(header)
    content.addView(card)
    root.addView(content)

    return NativeAdViewHolder(
        presentation = NativeAdPresentation.Compact,
        root = root,
        content = content,
        label = label,
        media = null,
        mediaFrame = null,
        icon = icon,
        iconFrame = iconFrame,
        headline = headline,
        body = body,
        advertiser = advertiser,
        callToAction = callToAction,
        decorate = { palette, _ ->
            // The label reads as the section's title, not as a chip, and the card is the widgets' surface.
            label.background = null
            label.setTextColor(palette.onSurface)
            headerIcon.imageTintList = ColorStateList.valueOf(palette.primary)
            card.background = roundedDrawable(color = palette.surfaceContainerHigh, radiusPx = context.dp(28))
        },
    )
}

private const val FACE_SIZE_DP = 88
