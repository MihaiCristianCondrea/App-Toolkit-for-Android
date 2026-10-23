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
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.graphics.Outline
import android.graphics.Path
import android.graphics.Typeface
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView

const val ICON_CORNER_RADIUS_DP: Int = 12

const val LABEL_CORNER_RADIUS_DP: Int = 8

const val MEDIA_CORNER_RADIUS_DP: Int = 20

/**
 * Aspect ratio of media in the Featured presentation.
 */
const val MEDIA_ASPECT_RATIO: Float = 16f / 9f

const val CTA_CORNER_RADIUS_DP: Int = 20

const val CTA_HORIZONTAL_PADDING_DP: Int = 20

const val CTA_TEXT_HORIZONTAL_PADDING_DP: Int = 12

const val CTA_VERTICAL_PADDING_DP: Int = 8

const val CARD_PADDING_DP: Int = 8

const val BAR_HORIZONTAL_PADDING_DP: Int = 16

const val BAR_VERTICAL_PADDING_DP: Int = 12

const val SPACING_DP: Int = 16

const val SMALL_SPACING_DP: Int = 8

const val LABEL_HORIZONTAL_PADDING_DP: Int = 8

const val LABEL_VERTICAL_PADDING_DP: Int = 4

const val GRID_ROW_LABEL_SPACING_DP: Int = 6

const val GRID_ROW_BODY_TEXT_SIZE_SP: Float = 12f

const val COMPACT_ICON_SIZE_DP: Int = 48

const val BAR_ICON_SIZE_DP: Int = 32

const val GRID_ICON_SIZE_DP: Int = 56

const val ICON_PADDING_DP: Int = 4

const val LABEL_TEXT_SIZE_SP: Float = 11f

const val ADVERTISER_TEXT_SIZE_SP: Float = 12f

const val BODY_TEXT_SIZE_SP: Float = 14f

const val HEADLINE_TEXT_SIZE_SP: Float = 16f

const val CTA_TEXT_SIZE_SP: Float = 14f

/**
 * Fills [path] with [color], rescaled from the size it was measured at to whatever bounds the view
 * ends up with.
 */
fun pathDrawable(color: Int, path: Path, sourceSize: Float): ShapeDrawable =
    ShapeDrawable(PathShape(path, sourceSize, sourceSize)).apply { paint.color = color }

fun nativeAdRoot(
    context: Context,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
): NativeAdView = NativeAdView(context).apply {
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
}

fun verticalContent(context: Context, padding: Int): LinearLayout = LinearLayout(context)
    .apply {
        orientation = LinearLayout.VERTICAL
        setPadding(padding, padding, padding, padding)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

fun sponsoredLabelView(context: Context): TextView = TextView(context).apply {
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

/**
 * The ad's headline, bold by default.
 *
 * A screen whose own titles are not bold turns it off through `NativeAdStyle.headlineBold` rather
 * than here, so one placement can differ without every factory restating the default.
 */
fun headlineView(context: Context, maxLines: Int): TextView = TextView(context).apply {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, HEADLINE_TEXT_SIZE_SP)
    setTypeface(typeface, Typeface.BOLD)
    this.maxLines = maxLines
    ellipsize = TextUtils.TruncateAt.END
    layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}

fun bodyView(context: Context, maxLines: Int): TextView = TextView(context).apply {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, BODY_TEXT_SIZE_SP)
    this.maxLines = maxLines
    ellipsize = TextUtils.TruncateAt.END
    layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}

fun advertiserView(context: Context): TextView = TextView(context).apply {
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
fun callToActionView(context: Context): TextView = TextView(context).apply {
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
fun iconFrameView(
    context: Context,
    sizeDp: Int,
    radiusDp: Int = ICON_CORNER_RADIUS_DP,
    paddingDp: Int = 0,
): LinearLayout = LinearLayout(context).apply {
    val size: Int = context.dp(sizeDp)
    val padding: Int = context.dp(paddingDp)
    setPadding(padding, padding, padding, padding)
    gravity = Gravity.CENTER
    clipToOutline = true
    outlineProvider = roundedOutline(radiusPx = context.dp(radiusDp))
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

fun <T : View> T.withTopMargin(margin: Int): T = apply {
    (layoutParams as? LinearLayout.LayoutParams)?.topMargin = margin
}

fun TextView.centerHorizontally(): TextView = apply {
    gravity = Gravity.CENTER_HORIZONTAL
    (layoutParams as? LinearLayout.LayoutParams)?.gravity = Gravity.CENTER_HORIZONTAL
}

fun roundedDrawable(color: Int, radiusPx: Int): GradientDrawable = GradientDrawable().apply {
    shape = GradientDrawable.RECTANGLE
    cornerRadius = radiusPx.toFloat()
    setColor(color)
}

fun roundedOutline(radiusPx: Int): ViewOutlineProvider = object : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(0, 0, view.width, view.height, radiusPx.toFloat())
    }
}

fun Context.dp(value: Int): Int =
    (value * resources.displayMetrics.density).toInt()
