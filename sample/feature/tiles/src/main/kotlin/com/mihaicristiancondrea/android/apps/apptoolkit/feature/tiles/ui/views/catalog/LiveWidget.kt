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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.StatusColors

/*
 * The live widgets of the overview tabs, as the marketing kit draws them (`.qt-live` in
 * marketing/ui/src/kit/kit.css): a small uppercase label, the tool's face in its shape, a bold value
 * and a note in the tool's accent. A signal widget (the torch and SOS) is tinted with its accent
 * and stretches its content; the others sit on the surface.
 */

/** The small uppercase name above a widget's face. */
@Composable
internal fun WidgetLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    letterSpacing: Float = 0.5f,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier.padding(start = 4.dp),
        style = WidgetLabelStyle.copy(letterSpacing = letterSpacing.sp),
        color = color,
    )
}

/** The bold reading under a face: 28 sp on a sensor widget, 24 sp on a signal widget. */
@Composable
internal fun WidgetValue(text: String, modifier: Modifier = Modifier, size: Int = 28, color: Color = Color.Unspecified) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontSize = size.sp,
            lineHeight = (size + 6).sp,
            fontWeight = FontWeight.Bold,
            fontFeatureSettings = "tnum",
        ),
        color = color,
        maxLines = 1,
    )
}

/** The line under a value, in the tool's accent. */
@Composable
internal fun WidgetNote(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = color,
        maxLines = 1,
    )
}

/**
 * A live widget: [label], the tool's [face] drawn in [faceShape] on a tint of its [accent], then
 * [value] and [note].
 *
 * @param signal Tints the whole widget with the accent and stretches its content, as the torch and
 * SOS widgets are.
 */
@Composable
internal fun LiveWidget(
    label: String,
    accent: StatusColors,
    faceShape: Shape,
    value: String,
    note: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    signal: Boolean = false,
    faceModifier: Modifier = Modifier,
    face: @Composable BoxScope.() -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val container = if (signal) lerp(scheme.surfaceContainerHigh, accent.container, 0.16f) else scheme.surfaceContainerHigh
    val faceColor = lerp(scheme.surfaceContainerLowest, accent.container, if (signal) 0.30f else 0.22f)
    val faceSize: Dp = if (signal) 156.dp else 148.dp
    Surface(onClick = onClick, modifier = modifier, shape = shape, color = container, contentColor = scheme.onSurface) {
        Column(
            modifier = Modifier.padding(
                start = if (signal) 16.dp else 12.dp,
                end = if (signal) 16.dp else 12.dp,
                top = 16.dp,
                bottom = 20.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WidgetLabel(
                text = label,
                modifier = Modifier.align(Alignment.Start),
                color = if (signal) accent.content else scheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = faceModifier.size(faceSize).clip(faceShape).background(faceColor),
                contentAlignment = Alignment.Center,
                content = face,
            )
            Spacer(Modifier.height(if (signal) 12.dp else 8.dp))
            WidgetValue(value, size = if (signal) 24 else 28, modifier = if (signal) Modifier.align(Alignment.Start) else Modifier)
            WidgetNote(note, accent.content, modifier = if (signal) Modifier.align(Alignment.Start) else Modifier)
        }
    }
}

private val WidgetLabelStyle: TextStyle
    @Composable get() = MaterialTheme.typography.labelMedium.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp,
    )
