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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell

import android.content.Context
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.DefaultNativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdBadgeShape
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class NativeAdBadgeTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val palette = NativeAdPalette(0, 0, 0, 0, 0, 0, 0, 0, 0)
    private val badge = NativeAdBadgeShape(
        path = Path().apply { addCircle(24f, 24f, 24f, Path.Direction.CW) },
        sizePx = 48f,
    )

    @Test
    fun an_inset_keeps_the_full_icon_inside_the_custom_badge() {
        val holder = DefaultNativeAdViewFactory().createViewHolder(context, NativeAdPresentation.Compact)
        val frame = requireNotNull(holder.iconFrame)
        val icon = requireNotNull(holder.icon)
        holder.root.iconView = icon

        holder.applyPalette(palette, NativeAdStyle(badgeShape = badge, iconInsetDp = 12))
        frame.measure(
            View.MeasureSpec.makeMeasureSpec(frame.layoutParams.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(frame.layoutParams.height, View.MeasureSpec.EXACTLY),
        )
        frame.layout(0, 0, frame.measuredWidth, frame.measuredHeight)

        val inset = context.dp(12)
        assertEquals(inset, icon.left)
        assertEquals(inset, icon.top)
        assertEquals(frame.width - inset, icon.right)
        assertEquals(frame.height - inset, icon.bottom)
        assertEquals(ImageView.ScaleType.FIT_CENTER, icon.scaleType)
        assertFalse(frame.clipToOutline)
        assertTrue(frame.background is ShapeDrawable)
        assertSame(icon, holder.root.iconView)
    }

    @Test
    fun removing_the_override_restores_the_presentations_icon_geometry() {
        val holder = DefaultNativeAdViewFactory().createViewHolder(context, NativeAdPresentation.Compact)
        val frame = requireNotNull(holder.iconFrame)
        val icon = requireNotNull(holder.icon)

        holder.applyPalette(palette, NativeAdStyle(badgeShape = badge, iconInsetDp = 12))
        holder.applyPalette(palette)

        assertEquals(0, frame.paddingLeft)
        assertEquals(0, frame.paddingTop)
        assertEquals(ImageView.ScaleType.CENTER_CROP, icon.scaleType)
        assertTrue(frame.clipToOutline)
    }

    @Test
    fun a_grid_row_keeps_its_own_inset_when_the_style_sets_only_a_shape() {
        val presentation = NativeAdPresentation.GridRow(
            iconSizeDp = 48,
            iconInsetDp = 12,
            contentPaddingDp = 16,
            iconSpacingDp = 16,
        )
        val holder = DefaultNativeAdViewFactory().createViewHolder(context, presentation)
        holder.applyPalette(palette, NativeAdStyle(badgeShape = badge))

        assertEquals(context.dp(12), requireNotNull(holder.iconFrame).paddingLeft)
        assertEquals(ImageView.ScaleType.FIT_CENTER, requireNotNull(holder.icon).scaleType)
    }
}
