package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Composition loading survives replays; progress is read during drawing, not by the parent UI.
 *
 * An icon that declares [ToolkitIcon.Animated.loop] ignores [atEnd] and [clickCount] and animates
 * continuously instead: [ToolkitIconReplayMode.Restart] snaps back and runs forward every cycle,
 * [ToolkitIconReplayMode.Reverse] travels forward and back.
 */
@Composable
internal fun LottieToolkitIcon(
    icon: ToolkitIcon.Lottie,
    contentDescription: String?,
    modifier: Modifier,
    atEnd: Boolean,
    tint: Color,
    clickCount: Int = 0,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(icon.resId))
    val progress = remember(icon) { Animatable(if (atEnd && clickCount == 0) 1f else 0f) }
    val playback = remember(icon) { LottieIconPlayback(atEnd && clickCount == 0) }

    if (icon.loop) {
        LaunchedEffect(composition, icon) {
            val loaded = composition ?: return@LaunchedEffect
            // A composition that reports no duration would finish every cycle at once and spin
            // this loop, so it is floored to a single frame.
            val cycle = tween<Float>(
                durationMillis = loaded.duration.roundToInt().coerceAtLeast(minimumValue = 16),
                easing = LinearEasing,
            )
            var forward = true
            while (true) {
                if (icon.replayMode == ToolkitIconReplayMode.Restart) {
                    progress.snapTo(targetValue = 0f)
                    progress.animateTo(targetValue = 1f, animationSpec = cycle)
                } else {
                    progress.animateTo(
                        targetValue = if (forward) 1f else 0f,
                        animationSpec = cycle,
                    )
                    forward = !forward
                }
            }
        }
    } else {
        LaunchedEffect(composition, icon, atEnd, clickCount) {
            val loaded = composition ?: return@LaunchedEffect
            val target = playback.target(atEnd, clickCount, icon.replayMode)
            if (target.restart) progress.snapTo(0f)
            progress.animateTo(
                targetValue = target.progress,
                animationSpec = tween(
                    durationMillis = (loaded.duration * abs(target.progress - progress.value)).roundToInt(),
                    easing = LinearEasing,
                ),
            )
        }
    }

    val properties = if (icon.tintable && tint != Color.Unspecified) {
        val filter = remember(tint) { PorterDuffColorFilter(tint.toArgb(), PorterDuff.Mode.SRC_IN) }
        rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(LottieProperty.COLOR_FILTER, filter, "**"),
        )
    } else null
    val semantics = if (contentDescription != null) Modifier.semantics {
        this.contentDescription = contentDescription
        role = Role.Image
    } else Modifier

    LottieAnimation(
        composition = composition,
        progress = { progress.value },
        modifier = modifier.size(24.dp).then(semantics),
        dynamicProperties = properties,
    )
}

internal data class LottiePlaybackTarget(val progress: Float, val restart: Boolean)

/** Keeps click replays distinct from selection changes, including clicks before JSON finishes loading. */
internal class LottieIconPlayback(private var restingAtEnd: Boolean) {
    private var lastClickCount = 0
    private var targetAtEnd = restingAtEnd

    fun target(atEnd: Boolean, clickCount: Int, mode: ToolkitIconReplayMode): LottiePlaybackTarget {
        val clicked = clickCount != lastClickCount
        val selectionChanged = atEnd != restingAtEnd
        targetAtEnd = when {
            clicked && mode == ToolkitIconReplayMode.Restart -> true
            selectionChanged -> atEnd
            clicked -> !targetAtEnd
            else -> targetAtEnd
        }
        restingAtEnd = atEnd
        lastClickCount = clickCount
        return LottiePlaybackTarget(if (targetAtEnd) 1f else 0f, clicked && mode == ToolkitIconReplayMode.Restart)
    }
}
