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

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Renders the icon of a clickable component, mixing the unselected and selected icons with the
 * click driven playback of Animated Vector Drawables.
 *
 * Behavior, per icon combination:
 * - Two static icons (vector or drawable resource): the icon is swapped on selection, nothing
 *   animates.
 * - Static unselected icon + animated selected icon: the static icon is shown at rest; the first
 *   click swaps in the AVD and plays it forward, and every further click plays it again, so a
 *   repeatedly clicked action such as *Share* animates every single time.
 * - A single AVD used for both states: the drawable rests on its first frame while unselected and on
 *   its last frame while selected, and still replays on every click.
 * - Animated unselected icon + static selected icon: the AVD plays while the component is
 *   unselected, and the static icon takes over once it is selected.
 *
 * A repeated click restarts the animation from its first frame by default. Drawables that morph
 * between two distinct shapes can instead travel back by declaring
 * [ToolkitIconReplayMode.Reverse] on the [ToolkitIcon.AnimatedVector].
 *
 * An icon that declares [ToolkitIcon.Animated.loop] opts out of all of this once its loop is
 * running: it animates on its own for as long as it is composed, so clicks and selection no longer
 * drive its playback. With [ToolkitIconLoopTrigger.OnInteraction] the loop only starts on the first
 * click or selection, and until then the icon behaves like every other animated one.
 *
 * Components without a selected state, such as buttons, pass only [icon] and [clickCount].
 *
 * @param icon Icon shown while the component is unselected.
 * @param clickCount Number of clicks the component received while composed. Incrementing it replays
 *   the animation; components that are never selected rely on this alone. Owners that never animate
 *   can leave it at `0`.
 * @param contentDescription Accessibility description of the icon.
 * @param modifier The [Modifier] to apply to the icon.
 * @param selectedIcon Icon shown while the component is selected. Defaults to [icon].
 * @param selected Whether the component is currently selected. Defaults to `false`.
 * @param tint Tint color to apply to the icon, defaults to [LocalContentColor].
 */
@Composable
fun AnimatedToolkitIcon(
    icon: ToolkitIcon,
    clickCount: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    selectedIcon: ToolkitIcon = icon,
    selected: Boolean = false,
    tint: Color = LocalContentColor.current,
) {
    val interacted: Boolean = clickCount > 0 || selected
    val displayedIcon: ToolkitIcon = resolveToolkitIcon(
        icon = icon,
        selectedIcon = selectedIcon,
        selected = selected,
        interacted = clickCount > 0,
    )
    val looping: Boolean = resolveToolkitIconLoop(icon = displayedIcon, interacted = interacted)

    // A running loop drives itself inside the shared renderer, so none of the click and selection
    // bookkeeping below applies to it.
    if (displayedIcon is ToolkitIcon.Animated && looping) {
        ToolkitIconContent(
            icon = displayedIcon,
            contentDescription = contentDescription,
            modifier = modifier,
            atEnd = selected || displayedIcon.atEnd,
            interacted = interacted,
            tint = tint,
        )
        return
    }

    if (displayedIcon is ToolkitIcon.Lottie) {
        LottieToolkitIcon(
            icon = displayedIcon,
            contentDescription = contentDescription,
            modifier = modifier,
            atEnd = selected || displayedIcon.atEnd,
            tint = tint,
            looping = false,
            clickCount = clickCount,
        )
        return
    }

    if (displayedIcon !is ToolkitIcon.AnimatedVector) {
        ToolkitIconContent(
            icon = displayedIcon,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
        return
    }

    val restingAtEnd: Boolean = selected || displayedIcon.atEnd

    // Restarting the animation means dropping the running painter: a new one is created on its first
    // frame, which is why the click count keys the composition instead of only flipping a flag.
    val playbackKey: Int =
        if (displayedIcon.replayMode == ToolkitIconReplayMode.Restart) clickCount else 0

    key(displayedIcon, playbackKey) {
        var atEnd: Boolean by remember(displayedIcon.resId) {
            mutableStateOf(value = playbackKey == 0 && restingAtEnd)
        }
        var lastSelected: Boolean by remember(displayedIcon.resId) {
            mutableStateOf(value = selected)
        }

        LaunchedEffect(selected, clickCount) {
            // Let the painter draw the current frame once before flipping, otherwise the drawable is
            // created already at its target state and jumps instead of animating.
            withFrameNanos { }
            atEnd = when {
                selected != lastSelected -> {
                    lastSelected = selected
                    restingAtEnd
                }

                displayedIcon.replayMode == ToolkitIconReplayMode.Restart ->
                    restingAtEnd || clickCount > 0

                clickCount > 0 -> !atEnd

                else -> atEnd
            }
        }

        ToolkitIconContent(
            icon = displayedIcon,
            contentDescription = contentDescription,
            modifier = modifier,
            atEnd = atEnd,
            tint = tint,
        )
    }
}
