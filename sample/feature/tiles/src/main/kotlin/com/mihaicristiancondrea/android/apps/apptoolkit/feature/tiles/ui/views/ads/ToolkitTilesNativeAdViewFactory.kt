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
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.DefaultNativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.ICON_PADDING_DP
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdViewHolder
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.SMALL_SPACING_DP
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.SPACING_DP
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.advertiserView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.bodyView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.callToActionView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.headlineView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.iconFrameView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.nativeAdRoot
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.sponsoredLabelView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.verticalContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.withTopMargin

/**
 * Specialized ad factory for the Toolkit Tiles screen.
 *
 * It implements the "Quick Tools" look: circular 44dp badge icons with internal padding
 * and specific card alignment.
 */
class ToolkitTilesNativeAdViewFactory : NativeAdViewFactory {
    private val defaultFactory = DefaultNativeAdViewFactory()

    override fun createViewHolder(
        context: Context,
        presentation: NativeAdPresentation
    ): NativeAdViewHolder {
        return if (presentation is NativeAdPresentation.Compact) {
            createTilesCompact(context)
        } else {
            defaultFactory.createViewHolder(context, presentation)
        }
    }

    private fun createTilesCompact(context: Context): NativeAdViewHolder {
        val root = nativeAdRoot(context = context)
        val content = verticalContent(context = context, padding = 0).apply {
            val horizontalPadding = context.dp(16)
            val verticalPadding = context.dp(12)
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        }
        val label = sponsoredLabelView(context = context)

        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            ).apply { topMargin = context.dp(SMALL_SPACING_DP) }
        }

        // Custom Quick Tools style: 44dp circular icon with internal padding
        val iconFrame = iconFrameView(
            context = context,
            sizeDp = 44,
            radiusDp = 22,
            paddingDp = 8
        )
        val icon = iconFrame.getChildAt(0) as ImageView

        val texts = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                .apply {
                    marginStart = context.dp(SPACING_DP)
                    marginEnd = context.dp(SMALL_SPACING_DP)
                }
        }
        val headline = headlineView(context = context, maxLines = 2)
        val body = bodyView(context = context, maxLines = 3)
            .withTopMargin(context.dp(ICON_PADDING_DP))
        val advertiser = advertiserView(context = context)
            .withTopMargin(context.dp(ICON_PADDING_DP))
        texts.addView(headline)
        texts.addView(body)
        texts.addView(advertiser)

        val callToAction = callToActionView(context = context)

        row.addView(iconFrame)
        row.addView(texts)
        row.addView(callToAction)

        content.addView(label)
        content.addView(row)
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
        )
    }
}
