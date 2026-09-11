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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay

/**
 * Renders a single [ToolkitIcon] exactly as described: a Compose [ImageVector], a static
 * drawable/vector resource, a runtime bitmap, or one frame state of an animated source.
 *
 * This composable is intentionally stateless. Deciding *which* icon a navigation item shows,
 * and whether an AVD currently rests on its first or last frame, is the job of
 * [AnimatedToolkitIcon].
 *
 * @param icon The [ToolkitIcon] to display.
 * @param contentDescription Optional description of the icon for accessibility.
 * @param modifier The [Modifier] to apply to the icon.
 * @param atEnd For [ToolkitIcon.AnimatedVector] only: `true` renders the last frame of the
 *   animation, `false` the first one. Changing this value animates between the two. It is ignored
 *   by an animation that is currently looping, which drives its own playback.
 * @param interacted Whether the component drawing this icon has been clicked or selected. It only
 *   matters for an icon that loops [ToolkitIconLoopTrigger.OnInteraction]: `false`, the default,
 *   keeps such an icon resting, and a stateless caller with nothing to interact with can leave it
 *   there.
 * @param tint Tint color to apply to tintable icon sources, defaults to [LocalContentColor].
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun ToolkitIconContent(
    icon: ToolkitIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    atEnd: Boolean = false,
    interacted: Boolean = false,
    tint: Color = LocalContentColor.current,
) {
    val looping: Boolean = resolveToolkitIconLoop(icon = icon, interacted = interacted)
    when (icon) {
        is ToolkitIcon.Lottie -> LottieToolkitIcon(
            icon = icon,
            contentDescription = contentDescription,
            modifier = modifier,
            atEnd = atEnd,
            tint = tint,
            looping = looping,
        )

        is ToolkitIcon.Vector -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }

        is ToolkitIcon.Resource -> {
            Icon(
                painter = painterResource(id = icon.resId),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }

        is ToolkitIcon.Bitmap -> {
            Image(
                bitmap = icon.imageBitmap,
                contentDescription = contentDescription,
                modifier = modifier,
                colorFilter = if (icon.tintable && tint != Color.Unspecified) {
                    ColorFilter.tint(color = tint)
                } else {
                    null
                },
            )
        }

        is ToolkitIcon.AnimatedVector -> {
            if (looping) {
                LoopingAnimatedVectorIcon(
                    icon = icon,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    restingAtEnd = atEnd,
                    tint = tint,
                )
                return
            }
            val image = AnimatedImageVector.animatedVectorResource(id = icon.resId)
            val painter = rememberAnimatedVectorPainter(
                animatedImageVector = image,
                atEnd = atEnd,
            )
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }
    }
}

/**
 * Plays an [ToolkitIcon.AnimatedVector] over and over for as long as it is composed, one cycle per
 * [AnimatedImageVector.totalDuration].
 *
 * The two replay modes shape the cycle the same way they shape a click:
 * [ToolkitIconReplayMode.Restart] drops the painter between cycles so every cycle runs forward from
 * the first frame, while [ToolkitIconReplayMode.Reverse] keeps one painter and flips its target, so
 * the drawable travels forward and back.
 *
 * A reversing loop picks [restingAtEnd] up as its first frame, so a loop that starts on an
 * interaction travels on from the frame the icon was resting on rather than snapping to the other
 * one. A restarting loop always opens on the first frame, which is the cycle it repeats.
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun LoopingAnimatedVectorIcon(
    icon: ToolkitIcon.AnimatedVector,
    contentDescription: String?,
    modifier: Modifier,
    restingAtEnd: Boolean,
    tint: Color,
) {
    val image = AnimatedImageVector.animatedVectorResource(id = icon.resId)
    val restarts: Boolean = icon.replayMode == ToolkitIconReplayMode.Restart
    // A drawable that declares no duration would otherwise schedule the next cycle immediately and
    // spin this loop, so a malformed resource costs one frame per cycle instead of the frame clock.
    val cycleMillis: Long = image.totalDuration.toLong().coerceAtLeast(minimumValue = OneFrameMillis)
    var cycle: Int by remember(icon) { mutableIntStateOf(value = 0) }

    key(if (restarts) cycle else 0) {
        var atEnd: Boolean by remember(icon) { mutableStateOf(value = !restarts && restingAtEnd) }

        LaunchedEffect(icon, cycle) {
            // The painter has to draw the frame it starts on before the target flips, otherwise the
            // drawable is created already at that target and the cycle never animates.
            withFrameNanos { }
            atEnd = if (restarts) true else !atEnd
            delay(timeMillis = cycleMillis)
            cycle++
        }

        Icon(
            painter = rememberAnimatedVectorPainter(animatedImageVector = image, atEnd = atEnd),
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
    }
}

/** Floor for one loop cycle, so a duration-less animation cannot outrun the frame clock. */
private const val OneFrameMillis: Long = 16L
