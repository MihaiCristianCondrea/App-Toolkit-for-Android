package com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

/**
 * A shimmer effect modifier for Jetpack Compose UI elements.
 *
 * This modifier applies a shimmering gradient background to a composable, creating a loading
 * or placeholder effect. The shimmer moves horizontally across the component in a continuous loop.
 *
 * The colors of the shimmer are derived from the current `MaterialTheme.colorScheme`.
 * It uses a linear gradient with colors transitioning from `surfaceContainer`,
 * to `outlineVariant`, to `surfaceContainerHighest`, and back to `surfaceContainer`.
 *
 * The animation is an infinite repeatable tween with a duration of 1500ms.
 *
 * @return A `Modifier` that applies the shimmer effect.
 *
 * @sample
 * ```
 * Box(
 *     modifier = Modifier
 *         .size(SizeConstants.OneHundredSize)
 *         .shimmerEffect()
 * )
 * ```
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceContainer,
        MaterialTheme.colorScheme.outlineVariant,
        MaterialTheme.colorScheme.surfaceContainerHighest,
        MaterialTheme.colorScheme.surfaceContainer,
    )

    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerProgress"
    ).value

    drawWithCache {
        onDrawBehind {
            val width = size.width
            val height = size.height
            val startX = (-2f * width) + (4f * width * progress)

            drawRect(
                brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(startX, 0f),
                    end = Offset(startX + width, height)
                )
            )
        }
    }
}
