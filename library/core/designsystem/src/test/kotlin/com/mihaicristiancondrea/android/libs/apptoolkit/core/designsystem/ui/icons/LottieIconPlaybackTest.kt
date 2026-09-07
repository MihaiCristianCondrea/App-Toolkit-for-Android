package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LottieIconPlaybackTest {
    @Test
    fun `restart always targets forward including rapid clicks`() {
        val state = LottieIconPlayback(false)
        assertEquals(LottiePlaybackTarget(0f, false), state.target(false, 0, ToolkitIconReplayMode.Restart))
        for (click in 1..3) {
            assertEquals(LottiePlaybackTarget(1f, true), state.target(false, click, ToolkitIconReplayMode.Restart))
        }
        assertEquals(LottiePlaybackTarget(1f, false), state.target(false, 3, ToolkitIconReplayMode.Restart))
    }

    @Test
    fun `reverse is opt in and alternates direction`() {
        val state = LottieIconPlayback(false)
        assertEquals(LottiePlaybackTarget(1f, false), state.target(false, 1, ToolkitIconReplayMode.Reverse))
        assertEquals(LottiePlaybackTarget(0f, false), state.target(false, 2, ToolkitIconReplayMode.Reverse))
    }

    @Test
    fun `selection changes restore endpoint without replay`() {
        val state = LottieIconPlayback(false)
        state.target(true, 1, ToolkitIconReplayMode.Restart)
        assertEquals(LottiePlaybackTarget(0f, false), state.target(false, 1, ToolkitIconReplayMode.Restart))
        assertEquals(LottiePlaybackTarget(1f, false), state.target(true, 1, ToolkitIconReplayMode.Restart))
    }

    @Test
    fun `loading after clicks starts latest replay and initial selection rests`() {
        val state = LottieIconPlayback(true)
        assertEquals(LottiePlaybackTarget(1f, false), state.target(true, 0, ToolkitIconReplayMode.Restart))
        assertEquals(LottiePlaybackTarget(1f, true), state.target(true, 3, ToolkitIconReplayMode.Restart))
    }

    @Test
    fun `lottie selected icon is resolved after interaction and defaults to restart`() {
        val static = ToolkitIcon.Resource(1)
        val animated = ToolkitIcon.Lottie(2)
        assertEquals(ToolkitIconReplayMode.Restart, animated.replayMode)
        assertEquals(static, resolveToolkitIcon(static, animated, false))
        assertEquals(animated, resolveToolkitIcon(static, animated, false, true))
        assertEquals(animated, resolveToolkitIcon(static, animated, true))
    }
}
