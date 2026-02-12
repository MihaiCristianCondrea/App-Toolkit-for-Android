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

package com.d4rk.android.libs.apptoolkit.core.ui.views.digits

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset

object AnimatedDigitTransitions {

    private const val DURATION = 400
    private val slideSpec = tween<IntOffset>(DURATION)
    private val fadeSpec = tween<Float>(DURATION)

    val increase by lazy {
        (slideInVertically(slideSpec) { -it } + fadeIn(fadeSpec)).togetherWith(
            slideOutVertically(slideSpec) { it } + fadeOut(fadeSpec)
        ).apply { SizeTransform(clip = false) }
    }

    val decrease by lazy {
        (slideInVertically(slideSpec) { it } + fadeIn(fadeSpec)).togetherWith(
            slideOutVertically(slideSpec) { -it } + fadeOut(fadeSpec)
        ).apply { SizeTransform(clip = false) }
    }
}

@Composable
fun AnimatedDigit(
    digit: Char,
    color: Color = LocalContentColor.current,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall
) {
    AnimatedContent(
        targetState = digit,
        transitionSpec = {
            if (targetState > initialState) {
                AnimatedDigitTransitions.increase
            } else {
                AnimatedDigitTransitions.decrease
            }
        }
    ) { targetDigit: Char ->
        CompositionLocalProvider(LocalContentColor provides color) {
            Text(
                text = targetDigit.toString(),
                style = textStyle,
                fontWeight = textStyle.fontWeight
                    ?: MaterialTheme.typography.headlineSmall.fontWeight
            )
        }
    }
}
