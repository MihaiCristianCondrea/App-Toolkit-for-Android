/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.withContext

/** Reads installed-package metadata and draws package icons away from the caller's thread. */
internal class AndroidWidgetAppsDataSource(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) {
    suspend fun installedIcon(packageName: String, sizePx: Int): Bitmap? {
        val drawable = withContext(dispatchers.io) {
            try {
                context.packageManager.getApplicationIcon(packageName)
            } catch (_: Exception) {
                null
            }
        } ?: return null
        return drawable.toBitmap(sizePx)
    }

    suspend fun fallbackIcon(sizePx: Int): Bitmap = withContext(dispatchers.io) {
        context.packageManager.getApplicationIcon(context.packageName)
    }.toBitmap(sizePx)

    private suspend fun Drawable.toBitmap(sizePx: Int): Bitmap = withContext(dispatchers.default) {
        createBitmap(sizePx, sizePx).also { bitmap ->
            val canvas = Canvas(bitmap)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
        }
    }
}
