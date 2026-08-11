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

package com.d4rk.android.libs.apptoolkit.core.ui.views.ads

import android.content.Context
import android.graphics.Outline
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.d4rk.android.libs.apptoolkit.R
import com.google.android.libraries.ads.mobile.sdk.nativead.MediaView
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView

/**
 * Renders [nativeAd] inside a programmatically built [NativeAdView].
 *
 * "No XML" does not mean "no `NativeAdView`". Ad assets must still be rendered inside a registered
 * [NativeAdView] with a real [MediaView] and a `registerNativeAd` call — drawing headline, icon and
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
) {
    val sponsoredLabel: String = stringResource(id = R.string.sponsored_ad_label)

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
            factory = { context -> createNativeAdView(context, presentation).root },
            update = { view ->
                val holder: NativeAdViewHolder = view.holder ?: return@AndroidView
                holder.applyPalette(palette = palette)
                holder.bind(nativeAd = nativeAd, sponsoredLabel = sponsoredLabel)
            },
        )
    }
}

/**
 * Strong references to the views a native ad binds to, so binding never needs `findViewById`.
 */
private class NativeAdViewHolder(
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
) {
    init {
        root.tag = this
    }

    fun applyPalette(palette: NativeAdPalette) {
        label.setTextColor(palette.primary)
        headline.setTextColor(palette.onSurface)
        body?.setTextColor(palette.onSurfaceVariant)
        advertiser.setTextColor(palette.onSurfaceVariant)

        iconFrame?.let { frame ->
            frame.background = roundedDrawable(
                color = palette.surfaceContainerHighest,
                radiusPx = frame.context.dp(value = ICON_CORNER_RADIUS_DP),
            )
        }
        mediaFrame?.let { frame ->
            frame.background = roundedDrawable(
                color = palette.surfaceContainerHighest,
                radiusPx = frame.context.dp(value = MEDIA_CORNER_RADIUS_DP),
            )
        }
        callToAction?.let { cta ->
            cta.background = roundedDrawable(
                color = palette.secondaryContainer,
                radiusPx = cta.context.dp(value = CTA_CORNER_RADIUS_DP),
            )
            cta.setTextColor(palette.onSecondaryContainer)
        }
        if (presentation is NativeAdPresentation.BarRow) {
            content.setBackgroundColor(palette.surfaceContainer)
        }
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

private val NativeAdView.holder: NativeAdViewHolder?
    get() = tag as? NativeAdViewHolder

private fun TextView.bindOptionalText(text: CharSequence?) {
    if (text.isNullOrEmpty()) {
        isVisible = false
    } else {
        this.text = text
        isVisible = true
    }
}

private const val ICON_CORNER_RADIUS_DP: Int = 12
private const val MEDIA_CORNER_RADIUS_DP: Int = 20

/** Media aspect ratio for the Featured presentation, matching the layout it replaced. */
private const val MEDIA_ASPECT_RATIO: Float = 16f / 9f
private const val CTA_CORNER_RADIUS_DP: Int = 20

private const val CTA_HORIZONTAL_PADDING_DP: Int = 20
private const val CTA_VERTICAL_PADDING_DP: Int = 8

private const val CARD_PADDING_DP: Int = 8
private const val BAR_HORIZONTAL_PADDING_DP: Int = 16
private const val BAR_VERTICAL_PADDING_DP: Int = 12
private const val SPACING_DP: Int = 16
private const val SMALL_SPACING_DP: Int = 8
private const val LABEL_HORIZONTAL_PADDING_DP: Int = 8
private const val LABEL_VERTICAL_PADDING_DP: Int = 4

private const val COMPACT_ICON_SIZE_DP: Int = 48
private const val BAR_ICON_SIZE_DP: Int = 32
private const val GRID_ICON_SIZE_DP: Int = 56
private const val ICON_PADDING_DP: Int = 4

private const val LABEL_TEXT_SIZE_SP: Float = 11f
private const val ADVERTISER_TEXT_SIZE_SP: Float = 12f
private const val BODY_TEXT_SIZE_SP: Float = 14f
private const val HEADLINE_TEXT_SIZE_SP: Float = 16f
private const val CTA_TEXT_SIZE_SP: Float = 14f

private fun createNativeAdView(
    context: Context,
    presentation: NativeAdPresentation,
): NativeAdViewHolder = when (presentation) {
    NativeAdPresentation.Featured -> createFeatured(context = context)
    NativeAdPresentation.Compact -> createCompact(context = context)
    NativeAdPresentation.Grid -> createGrid(context = context)
    NativeAdPresentation.BarRow -> createBarRow(context = context)
}

private fun createFeatured(context: Context): NativeAdViewHolder {
    val root = nativeAdRoot(context = context)
    val content = verticalContent(context = context, padding = context.dp(CARD_PADDING_DP))
    val label = sponsoredLabelView(context = context)

    // The MediaView is sized by the creative, so on its own it renders at whatever height the asset
    // happens to have — which is what made this card look arbitrary from one ad to the next. The
    // 16:9 frame reproduces the constraint the XML layout used to impose.
    val mediaFrame = AspectRatioFrameLayout(context = context, widthToHeightRatio = MEDIA_ASPECT_RATIO)
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

    val headline = headlineView(context = context, maxLines = 2).withTopMargin(context.dp(SPACING_DP))
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
        layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            .apply { marginStart = context.dp(SMALL_SPACING_DP) }
    }
    val callToAction = callToActionView(context = context).apply {
        (layoutParams as LinearLayout.LayoutParams).marginStart = context.dp(SMALL_SPACING_DP)
    }
    footer.addView(iconFrame)
    footer.addView(advertiser)
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

private fun nativeAdRoot(
    context: Context,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
): NativeAdView = NativeAdView(context).apply {
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
}

private fun verticalContent(context: Context, padding: Int): LinearLayout = LinearLayout(context)
    .apply {
        orientation = LinearLayout.VERTICAL
        setPadding(padding, padding, padding, padding)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

private fun sponsoredLabelView(context: Context): TextView = TextView(context).apply {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, LABEL_TEXT_SIZE_SP)
    isAllCaps = true
    setPadding(
        context.dp(LABEL_HORIZONTAL_PADDING_DP),
        context.dp(LABEL_VERTICAL_PADDING_DP),
        context.dp(LABEL_HORIZONTAL_PADDING_DP),
        context.dp(LABEL_VERTICAL_PADDING_DP),
    )
    layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}

private fun headlineView(context: Context, maxLines: Int): TextView = TextView(context).apply {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, HEADLINE_TEXT_SIZE_SP)
    setTypeface(typeface, Typeface.BOLD)
    this.maxLines = maxLines
    ellipsize = TextUtils.TruncateAt.END
    layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}

private fun bodyView(context: Context, maxLines: Int): TextView = TextView(context).apply {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, BODY_TEXT_SIZE_SP)
    this.maxLines = maxLines
    ellipsize = TextUtils.TruncateAt.END
    layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}

private fun advertiserView(context: Context): TextView = TextView(context).apply {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, ADVERTISER_TEXT_SIZE_SP)
    maxLines = 1
    ellipsize = TextUtils.TruncateAt.END
    layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}

/**
 * The call to action.
 *
 * It carries no forced height: the button wraps its label plus padding, the way the rest of the
 * app's buttons do. A hardcoded minimum height made the CTA taller than its neighbours in every
 * presentation that puts it on a row with an icon or an advertiser line.
 */
private fun callToActionView(context: Context): TextView = TextView(context).apply {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, CTA_TEXT_SIZE_SP)
    maxLines = 1
    gravity = Gravity.CENTER
    setPadding(
        context.dp(CTA_HORIZONTAL_PADDING_DP),
        context.dp(CTA_VERTICAL_PADDING_DP),
        context.dp(CTA_HORIZONTAL_PADDING_DP),
        context.dp(CTA_VERTICAL_PADDING_DP),
    )
    layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}

/**
 * Icon container. The icon itself is the registered `iconView`; the frame carries the rounded
 * background so the icon can keep its own padding without clipping the tint.
 */
private fun iconFrameView(context: Context, sizeDp: Int): LinearLayout = LinearLayout(context).apply {
    val size: Int = context.dp(sizeDp)
    gravity = Gravity.CENTER
    clipToOutline = true
    outlineProvider = roundedOutline(radiusPx = context.dp(ICON_CORNER_RADIUS_DP))
    layoutParams = LinearLayout.LayoutParams(size, size)
    addView(
        ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            contentDescription = null
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
    )
}

private fun <T : View> T.withTopMargin(margin: Int): T = apply {
    (layoutParams as? LinearLayout.LayoutParams)?.topMargin = margin
}

private fun TextView.centerHorizontally(): TextView = apply {
    gravity = Gravity.CENTER_HORIZONTAL
    (layoutParams as? LinearLayout.LayoutParams)?.gravity = Gravity.CENTER_HORIZONTAL
}

/**
 * A frame that always measures [widthToHeightRatio], whatever its child reports.
 */
private class AspectRatioFrameLayout(
    context: Context,
    private val widthToHeightRatio: Float,
) : FrameLayout(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int = MeasureSpec.getSize(widthMeasureSpec)
        if (width <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        val height: Int = (width / widthToHeightRatio).toInt()
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY),
        )
    }
}

private fun roundedDrawable(color: Int, radiusPx: Int): GradientDrawable = GradientDrawable().apply {
    shape = GradientDrawable.RECTANGLE
    cornerRadius = radiusPx.toFloat()
    setColor(color)
}

private fun roundedOutline(radiusPx: Int): ViewOutlineProvider = object : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(0, 0, view.width, view.height, radiusPx.toFloat())
    }
}

private fun Context.dp(value: Int): Int =
    (value * resources.displayMetrics.density).toInt()
