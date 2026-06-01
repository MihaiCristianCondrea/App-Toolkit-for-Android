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

package com.wstxda.toolkit.ui.icon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Icon
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.wstxda.toolkit.R
import androidx.core.graphics.withRotation

class CompassIconProvider(private val context: Context) {

    private val arrowDrawable = ContextCompat.getDrawable(context, R.drawable.ic_compass)!!
    private val iconBitmap =
        createBitmap(arrowDrawable.intrinsicWidth, arrowDrawable.intrinsicHeight)
    private val canvas = Canvas(iconBitmap)

    fun getIcon(isActive: Boolean, degrees: Float): Icon {
        if (!isActive) {
            return Icon.createWithResource(context, R.drawable.ic_compass)
        }

        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
        canvas.withRotation(-degrees, iconBitmap.width / 2f, iconBitmap.height / 2f) {
            arrowDrawable.setBounds(0, 0, iconBitmap.width, iconBitmap.height)
            arrowDrawable.draw(this)
        }

        return Icon.createWithBitmap(iconBitmap)
    }
}