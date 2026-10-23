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

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import com.google.android.libraries.ads.mobile.sdk.nativead.MediaView

/**
 * Default implementation of [NativeAdViewFactory] providing the toolkit's standard presentations.
 */
class DefaultNativeAdViewFactory : NativeAdViewFactory {
    override fun createViewHolder(
        context: Context,
        presentation: NativeAdPresentation
    ): NativeAdViewHolder = when (presentation) {
        NativeAdPresentation.Featured -> createFeatured(context = context)
        NativeAdPresentation.Compact -> createCompact(context = context)
        NativeAdPresentation.Grid -> createGrid(context = context)
        is NativeAdPresentation.GridRow -> createGridRow(
            context = context,
            presentation = presentation,
        )

        NativeAdPresentation.BarRow -> createBarRow(context = context)
    }
}

private fun createFeatured(context: Context): NativeAdViewHolder {
    val root = nativeAdRoot(context = context)
    val content = verticalContent(context = context, padding = context.dp(CARD_PADDING_DP))
    val label = sponsoredLabelView(context = context)

    // Constrain creative-dependent media height to the Featured presentation's 16:9 frame.
    val mediaFrame =
        AspectRatioFrameLayout(context = context, widthToHeightRatio = MEDIA_ASPECT_RATIO)
            .apply {
                clipToOutline = true
                outlineProvider = roundedOutline(radiusPx = context.dp(MEDIA_CORNER_RADIUS_DP))
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                ).apply { topMargin = context.dp(SPACING_DP) }
            }
    val media = MediaView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }
    mediaFrame.addView(media)

    val headline =
        headlineView(context = context, maxLines = 2).withTopMargin(context.dp(SPACING_DP))
    val body = bodyView(context = context, maxLines = 3).withTopMargin(context.dp(SMALL_SPACING_DP))

    val footer = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply { topMargin = context.dp(SPACING_DP) }
    }
    val iconFrame = iconFrameView(context = context, sizeDp = BAR_ICON_SIZE_DP)
    val icon = iconFrame.getChildAt(0) as ImageView
    val advertiser = advertiserView(context = context).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply { marginStart = context.dp(SMALL_SPACING_DP) }
    }
    // Weight the spacer because an absent advertiser view occupies no space.
    val footerSpacer = Space(context).apply {
        layoutParams = LinearLayout.LayoutParams(0, 0, 1f)
    }
    val callToAction = callToActionView(context = context).apply {
        (layoutParams as LinearLayout.LayoutParams).marginStart = context.dp(SMALL_SPACING_DP)
    }
    footer.addView(iconFrame)
    footer.addView(advertiser)
    footer.addView(footerSpacer)
    footer.addView(callToAction)

    content.addView(label)
    content.addView(mediaFrame)
    content.addView(headline)
    content.addView(body)
    content.addView(footer)
    root.addView(content)

    return NativeAdViewHolder(
        presentation = NativeAdPresentation.Featured,
        root = root,
        content = content,
        label = label,
        media = media,
        mediaFrame = mediaFrame,
        icon = icon,
        iconFrame = iconFrame,
        headline = headline,
        body = body,
        advertiser = advertiser,
        callToAction = callToAction,
    )
}

private fun createCompact(context: Context): NativeAdViewHolder {
    val root = nativeAdRoot(context = context)
    val content = verticalContent(context = context, padding = context.dp(CARD_PADDING_DP))
    val label = sponsoredLabelView(context = context)

    val row = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply { topMargin = context.dp(SMALL_SPACING_DP) }
    }
    val iconFrame = iconFrameView(context = context, sizeDp = COMPACT_ICON_SIZE_DP)
    val icon = iconFrame.getChildAt(0) as ImageView

    val texts = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            .apply {
                marginStart = context.dp(SPACING_DP)
                marginEnd = context.dp(SMALL_SPACING_DP)
            }
    }
    // Limit text height so Compact ads fit between Help question rows.
    val headline = headlineView(context = context, maxLines = 1)
    val body = bodyView(context = context, maxLines = 2)
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

/**
 * Square cell that sits among ordinary content cards in a grid.
 *
 * Everything is centred, matching the app cards it is interleaved with: a left-aligned column read
 * as a different kind of tile.
 */
private fun createGrid(context: Context): NativeAdViewHolder {
    val root = nativeAdRoot(context = context, height = ViewGroup.LayoutParams.MATCH_PARENT)
    val content = verticalContent(context = context, padding = context.dp(SPACING_DP)).apply {
        gravity = Gravity.CENTER
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }
    val label = sponsoredLabelView(context = context).centerHorizontally()
    val iconFrame = iconFrameView(context = context, sizeDp = GRID_ICON_SIZE_DP).apply {
        (layoutParams as LinearLayout.LayoutParams).apply {
            topMargin = context.dp(SPACING_DP)
            gravity = Gravity.CENTER_HORIZONTAL
        }
    }
    val icon = iconFrame.getChildAt(0) as ImageView
    val headline = headlineView(context = context, maxLines = 1)
        .centerHorizontally()
        .withTopMargin(context.dp(SPACING_DP))
    val advertiser = advertiserView(context = context)
        .centerHorizontally()
        .withTopMargin(context.dp(ICON_PADDING_DP))
    val body = bodyView(context = context, maxLines = 2)
        .centerHorizontally()
        .withTopMargin(context.dp(ICON_PADDING_DP))

    content.addView(label)
    content.addView(iconFrame)
    content.addView(headline)
    content.addView(advertiser)
    content.addView(body)
    root.addView(content)

    return NativeAdViewHolder(
        presentation = NativeAdPresentation.Grid,
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
        callToAction = null,
    )
}

private fun createBarRow(context: Context): NativeAdViewHolder {
    val root = nativeAdRoot(context = context)
    val content = verticalContent(context = context, padding = 0).apply {
        setPadding(
            context.dp(BAR_HORIZONTAL_PADDING_DP),
            context.dp(SMALL_SPACING_DP),
            context.dp(BAR_HORIZONTAL_PADDING_DP),
            context.dp(BAR_VERTICAL_PADDING_DP),
        )
    }
    val label = sponsoredLabelView(context = context)

    val row = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply { topMargin = context.dp(ICON_PADDING_DP) }
    }
    val iconFrame = iconFrameView(context = context, sizeDp = BAR_ICON_SIZE_DP)
    val icon = iconFrame.getChildAt(0) as ImageView

    val texts = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            .apply {
                marginStart = context.dp(SPACING_DP)
                marginEnd = context.dp(SPACING_DP)
            }
    }
    val headline = headlineView(context = context, maxLines = 1)
    val body = bodyView(context = context, maxLines = 1)
    val advertiser = advertiserView(context = context)
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
        presentation = NativeAdPresentation.BarRow,
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

/**
 * One row of a grouped grid.
 *
 * Everything is on a single row, and the disclosure chip sits inline with the body rather than on a
 * line of its own: a row that stacked the chip above the icon came out taller than the cells it is
 * interleaved with, which is exactly what makes an ad read as an intruder rather than as one more
 * row of the block. The badge, padding, gap and headline size come from the grid's size class, so
 * the row lines up with the cells above and below it.
 */
private fun createGridRow(
    context: Context,
    presentation: NativeAdPresentation.GridRow,
): NativeAdViewHolder {
    val root = nativeAdRoot(context = context)
    val padding: Int = context.dp(presentation.contentPaddingDp)

    val content = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(padding, padding, padding, padding)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    val iconFrame = gridRowIconFrameView(context = context, presentation = presentation)
    val icon = iconFrame.getChildAt(0) as ImageView

    val texts = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            .apply {
                marginStart = context.dp(presentation.iconSpacingDp)
                marginEnd = context.dp(SMALL_SPACING_DP)
            }
    }

    val headline = headlineView(context = context, maxLines = 1)

    val label = sponsoredLabelView(context = context)

    val body = bodyView(context = context, maxLines = 1).apply {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, GRID_ROW_BODY_TEXT_SIZE_SP)
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply { marginStart = context.dp(GRID_ROW_LABEL_SPACING_DP) }
    }

    val labelAndBody = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply { topMargin = context.dp(ICON_PADDING_DP) }
        addView(label)
        addView(body)
    }

    val advertiser = advertiserView(context = context)
        .withTopMargin(context.dp(ICON_PADDING_DP))

    texts.addView(headline)
    texts.addView(labelAndBody)
    texts.addView(advertiser)

    val callToAction = callToActionView(context = context)

    content.addView(iconFrame)
    content.addView(texts)
    content.addView(callToAction)
    root.addView(content)

    return NativeAdViewHolder(
        presentation = presentation,
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

/**
 * The badge behind a grid row's ad icon.
 *
 * The icon is inset and centred rather than filling the badge, which is what lets the badge carry
 * an arbitrary silhouette: the shape is drawn as the background, antialiased, and the icon never
 * reaches its edge, so nothing has to be clipped to a path a view outline could not express. The
 * inset also draws the ad icon at the size of the glyphs in the cells around it.
 */
private fun gridRowIconFrameView(
    context: Context,
    presentation: NativeAdPresentation.GridRow,
): LinearLayout = LinearLayout(context).apply {
    val size: Int = context.dp(presentation.iconSizeDp)
    val inset: Int = context.dp(presentation.iconInsetDp)
    gravity = Gravity.CENTER
    setPadding(inset, inset, inset, inset)
    layoutParams = LinearLayout.LayoutParams(size, size)
    addView(
        ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            contentDescription = null
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
    )
}
