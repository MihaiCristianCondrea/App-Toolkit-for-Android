/*
 * Copyright (c) 2026 Mihai-Cristian Condrea
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.digits.AnimatedDigit

/** Keep place values stable across carries and expose one complete number to accessibility. */
@Composable
internal fun AnimatedToolNumber(value: String, textStyle: TextStyle, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            modifier = modifier.clipToBounds().clearAndSetSemantics {
                text = AnnotatedString(value)
                liveRegion = LiveRegionMode.Polite
            },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            value.forEachIndexed { index, char ->
                key(value.length - index) {
                    // Keep '?' in the same animation slot so the first result also rolls into view.
                    if (char.isDigit() || char == '?') {
                        AnimatedDigit(digit = char, textStyle = textStyle)
                    } else {
                        Text(char.toString(), style = textStyle)
                    }
                }
            }
        }
    }
}

/** The existing widget typography, fitted to keep signed Long results inside their card. */
@Composable
internal fun AnimatedWidgetValue(value: String, modifier: Modifier = Modifier, size: Int = 28) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val fit = with(density) { (maxWidth.toPx() / (value.length.coerceAtLeast(1) * .75f)).toSp() }
        val fontSize = if (fit.value < size) fit else size.sp
        AnimatedToolNumber(
            value = value,
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                fontSize = fontSize,
                lineHeight = (fontSize.value + 6).sp,
                fontWeight = FontWeight.Bold,
                fontFeatureSettings = "tnum",
            ),
        )
    }
}
