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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LottieIconPlaybackTest {
    @Test
    fun `restart always targets forward including rapid clicks`() {
        val state = LottieIconPlayback(false)
        assertEquals(
            LottiePlaybackTarget(0f, false),
            state.target(false, 0, ToolkitIconReplayMode.Restart)
        )
        for (click in 1..3) {
            assertEquals(
                LottiePlaybackTarget(1f, true),
                state.target(false, click, ToolkitIconReplayMode.Restart)
            )
        }
        assertEquals(
            LottiePlaybackTarget(1f, false),
            state.target(false, 3, ToolkitIconReplayMode.Restart)
        )
    }

    @Test
    fun `reverse is opt in and alternates direction`() {
        val state = LottieIconPlayback(false)
        assertEquals(
            LottiePlaybackTarget(1f, false),
            state.target(false, 1, ToolkitIconReplayMode.Reverse)
        )
        assertEquals(
            LottiePlaybackTarget(0f, false),
            state.target(false, 2, ToolkitIconReplayMode.Reverse)
        )
    }

    @Test
    fun `selection changes restore endpoint without replay`() {
        val state = LottieIconPlayback(false)
        state.target(true, 1, ToolkitIconReplayMode.Restart)
        assertEquals(
            LottiePlaybackTarget(0f, false),
            state.target(false, 1, ToolkitIconReplayMode.Restart)
        )
        assertEquals(
            LottiePlaybackTarget(1f, false),
            state.target(true, 1, ToolkitIconReplayMode.Restart)
        )
    }

    @Test
    fun `loading after clicks starts latest replay and initial selection rests`() {
        val state = LottieIconPlayback(true)
        assertEquals(
            LottiePlaybackTarget(1f, false),
            state.target(true, 0, ToolkitIconReplayMode.Restart)
        )
        assertEquals(
            LottiePlaybackTarget(1f, true),
            state.target(true, 3, ToolkitIconReplayMode.Restart)
        )
    }

    @Test
    fun `lottie selected icon is resolved after interaction and defaults to restart`() {
        val static = ToolkitIcon.Resource(1)
        val animated = ToolkitIcon.Lottie(2)
        assertEquals(ToolkitIconReplayMode.Restart, animated.replayMode)
        assertEquals(static, resolveToolkitIcon(static, animated, false))
        assertEquals(
            animated, resolveToolkitIcon(
                static, animated,
                selected = false,
                interacted = true
            )
        )
        assertEquals(animated, resolveToolkitIcon(static, animated, true))
    }
}
