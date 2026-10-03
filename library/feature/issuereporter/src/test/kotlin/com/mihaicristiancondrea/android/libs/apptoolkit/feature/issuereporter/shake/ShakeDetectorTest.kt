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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.shake

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Covers the guards that separate a repeated shake from ordinary handling.
 *
 * Each one is tested on its own, because any single guard passes for gestures the others reject,
 * which is the whole reason they all exist.
 */
class ShakeDetectorTest {

    private var reported: Int = 0

    private fun detector(): ShakeDetector = ShakeDetector(
        sensorManager = null,
        threshold = THRESHOLD,
        minimumShakes = MINIMUM_SHAKES,
        minimumDurationMillis = MINIMUM_DURATION_MILLIS,
        cooldownMillis = COOLDOWN_MILLIS,
        elapsedRealtime = { 0L },
        onShake = { reported++ },
    )

    /**
     * Feeds [shakes] shakes, [stepMillis] apart from [startMillis]: each a reading at [gForce],
     * then one at rest halfway to the next. Returns the time after the last one.
     */
    private fun ShakeDetector.shake(
        shakes: Int,
        gForce: Float = THRESHOLD + 1f,
        startMillis: Long = 0L,
        stepMillis: Long = SHAKE_STEP_MILLIS,
    ): Long {
        var now: Long = startMillis
        repeat(times = shakes) {
            onAcceleration(gForce = gForce, now = now)
            onAcceleration(gForce = REST_G, now = now + stepMillis / 2)
            now += stepMillis
        }
        return now
    }

    @Test
    fun `enough shakes over enough time are reported`() {
        detector().shake(shakes = MINIMUM_SHAKES)

        assertEquals(1, reported)
    }

    @Test
    fun `one shake short of the minimum is not reported`() {
        detector().shake(shakes = MINIMUM_SHAKES - 1, stepMillis = MINIMUM_DURATION_MILLIS)

        assertEquals(0, reported)
    }

    @Test
    fun `handling below the threshold is never reported`() {
        detector().shake(shakes = MINIMUM_SHAKES * 2, gForce = THRESHOLD - 0.1f)

        assertEquals(0, reported)
    }

    /**
     * Enough shakes, but over in less than the minimum duration. This is what putting a phone down
     * firmly, and the ringing that follows, looks like.
     */
    @Test
    fun `a single jolt is not long enough to count`() {
        detector().shake(shakes = MINIMUM_SHAKES, stepMillis = 1L)

        assertEquals(0, reported)
    }

    /** A reading that never settles is one shake, however long it lasts. */
    @Test
    fun `staying above the threshold is one shake`() {
        val detector = detector()
        repeat(times = MINIMUM_SHAKES * 4) { index ->
            detector.onAcceleration(gForce = THRESHOLD + 1f, now = index * SHAKE_STEP_MILLIS)
        }

        assertEquals(0, reported)
    }

    /** Dipping just under the threshold is not settling, so it does not start another shake. */
    @Test
    fun `a reading hovering around the threshold is one shake`() {
        val detector = detector()
        repeat(times = MINIMUM_SHAKES * 4) { index ->
            val gForce: Float = if (index % 2 == 0) THRESHOLD + 0.1f else THRESHOLD - 0.1f
            detector.onAcceleration(gForce = gForce, now = index * SHAKE_STEP_MILLIS)
        }

        assertEquals(0, reported)
    }

    @Test
    fun `shakes spread beyond the window start the count over`() {
        val detector = detector()
        val half: Int = MINIMUM_SHAKES / 2
        val after: Long = detector.shake(shakes = half)
        detector.shake(shakes = MINIMUM_SHAKES - half, startMillis = after + GESTURE_WINDOW_MILLIS)

        assertEquals(0, reported)
    }

    @Test
    fun `one gesture is reported once while it decays`() {
        val detector = detector()
        val after: Long = detector.shake(shakes = MINIMUM_SHAKES)
        detector.shake(shakes = MINIMUM_SHAKES, startMillis = after)

        assertEquals(1, reported)
    }

    @Test
    fun `a second gesture after the cooldown is reported again`() {
        val detector = detector()
        detector.shake(shakes = MINIMUM_SHAKES)
        detector.shake(shakes = MINIMUM_SHAKES, startMillis = COOLDOWN_MILLIS * 2)

        assertEquals(2, reported)
    }

    @Test
    fun `the defaults ignore a couple of shakes and report a sustained one`() {
        val detector = ShakeDetector(sensorManager = null, elapsedRealtime = { 0L }, onShake = { reported++ })

        val after: Long = detector.shake(
            shakes = ShakeDetector.DEFAULT_MINIMUM_SHAKES / 2,
            gForce = ShakeDetector.DEFAULT_THRESHOLD_G + 1f,
        )
        assertEquals(0, reported)

        detector.shake(
            shakes = ShakeDetector.DEFAULT_MINIMUM_SHAKES,
            gForce = ShakeDetector.DEFAULT_THRESHOLD_G + 1f,
            startMillis = after + GESTURE_WINDOW_MILLIS,
        )
        assertEquals(1, reported)
    }

    @Test
    fun `start reports failure when the device exposes no sensor`() {
        assertFalse(detector().start())
    }

    private companion object {
        const val THRESHOLD: Float = 2.5f
        const val REST_G: Float = 1f
        const val MINIMUM_SHAKES: Int = 6
        const val MINIMUM_DURATION_MILLIS: Long = 800L
        const val COOLDOWN_MILLIS: Long = 1_500L

        /** Longer than the detector's own window, so a gesture paused this long is forgotten. */
        const val GESTURE_WINDOW_MILLIS: Long = 2_001L

        /** Shakes this far apart put [MINIMUM_SHAKES] of them past [MINIMUM_DURATION_MILLIS]. */
        const val SHAKE_STEP_MILLIS: Long = 200L
    }
}
