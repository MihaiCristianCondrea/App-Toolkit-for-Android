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

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Covers the three guards that separate a shake from ordinary handling.
 *
 * Each one is tested on its own, because any single guard passes for gestures the other two reject,
 * which is the whole reason all three exist.
 */
class ShakeDetectorTest {

    private var shakes: Int = 0

    private fun detector(): ShakeDetector = ShakeDetector(
        sensorManager = null,
        threshold = THRESHOLD,
        minimumDurationMillis = MINIMUM_DURATION_MILLIS,
        cooldownMillis = COOLDOWN_MILLIS,
        elapsedRealtime = { 0L },
        onShake = { shakes++ },
    )

    /** Feeds [samples] readings at [gForce], [stepMillis] apart, starting at [startMillis]. */
    private fun ShakeDetector.shake(
        samples: Int,
        gForce: Float = THRESHOLD + 1f,
        startMillis: Long = 0L,
        stepMillis: Long = SAMPLE_STEP_MILLIS,
    ): Long {
        var now: Long = startMillis
        repeat(times = samples) {
            onAcceleration(gForce = gForce, now = now)
            now += stepMillis
        }
        return now
    }

    @Test
    fun `a sustained gesture above the threshold is reported`() {
        detector().shake(samples = SUSTAINED_SAMPLES)

        assertThat(shakes).isEqualTo(1)
    }

    @Test
    fun `handling below the threshold is never reported`() {
        detector().shake(samples = SUSTAINED_SAMPLES * 2, gForce = THRESHOLD - 0.1f)

        assertThat(shakes).isEqualTo(0)
    }

    @Test
    fun `a single jolt is not long enough to count`() {
        // Above the threshold for the whole burst, but over in less than the minimum duration: this
        // is what putting a phone down firmly looks like.
        detector().shake(samples = SUSTAINED_SAMPLES, stepMillis = 1L)

        assertThat(shakes).isEqualTo(0)
    }

    @Test
    fun `too few readings do not count however long they are spread out`() {
        detector().shake(samples = 2, stepMillis = MINIMUM_DURATION_MILLIS)

        assertThat(shakes).isEqualTo(0)
    }

    @Test
    fun `one gesture is reported once while it decays`() {
        val detector = detector()
        val after: Long = detector.shake(samples = SUSTAINED_SAMPLES)
        detector.shake(samples = SUSTAINED_SAMPLES, startMillis = after)

        assertThat(shakes).isEqualTo(1)
    }

    @Test
    fun `a second gesture after the cooldown is reported again`() {
        val detector = detector()
        detector.shake(samples = SUSTAINED_SAMPLES)
        detector.shake(samples = SUSTAINED_SAMPLES, startMillis = COOLDOWN_MILLIS * 2)

        assertThat(shakes).isEqualTo(2)
    }

    @Test
    fun `start reports failure when the device exposes no sensor`() {
        assertThat(detector().start()).isFalse()
    }

    private companion object {
        const val THRESHOLD: Float = 2.5f
        const val MINIMUM_DURATION_MILLIS: Long = 300L
        const val COOLDOWN_MILLIS: Long = 1_500L
        const val SAMPLE_STEP_MILLIS: Long = 100L

        /** Enough readings, spread over enough time, to satisfy both sustain conditions. */
        const val SUSTAINED_SAMPLES: Int = 5
    }
}
