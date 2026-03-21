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

package com.d4rk.android.libs.apptoolkit.core.ui.views.text

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

/**
 * Renders trusted HTML content inside Compose using an Android [TextView] host.
 *
 * The HTML is parsed with [HtmlCompat.FROM_HTML_MODE_COMPACT], which supports common inline and
 * block formatting while collapsing unnecessary whitespace. Links are interactive because the
 * embedded [TextView] uses [LinkMovementMethod].
 *
 * Styling behavior:
 * - Text color follows `MaterialTheme.colorScheme.onSurface`.
 * - Font size uses [style] when specified, otherwise falls back to
 *   `MaterialTheme.typography.bodyMedium.fontSize`.
 *
 * Prefer this component when the content source is already HTML and you need clickable links.
 * Prefer Compose `Text` for plain strings or rich text produced fully in Compose.
 *
 * Security note: pass trusted or sanitized HTML input only.
 *
 * @param text HTML string to parse and display.
 * @param modifier Modifier applied to the host view.
 * @param style Base text style used to derive visual typography.
 */
@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val context = LocalContext.current
    val color = MaterialTheme.colorScheme.onSurface
    val fontSize =
        if (style.fontSize.isSpecified) style.fontSize.value else MaterialTheme.typography.bodyMedium.fontSize.value
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { textView: TextView ->
            textView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
            textView.setTextColor(color.toArgb())
            textView.textSize = fontSize
        }
    )
}
