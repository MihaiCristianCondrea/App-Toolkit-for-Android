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

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import kotlin.math.sqrt

/**
 * Reports a deliberate shake of the device.
 *
 * Three guards separate a shake from ordinary handling, and all three are needed. [threshold] alone
 * fires on a single jolt, which is what putting a phone down on a table produces, so a gesture must
 * also stay above it across [minimumDurationMillis] and [MINIMUM_SAMPLES] separate readings. The
 * samples are counted over a rolling window rather than required to be consecutive, because a real
 * shake oscillates and its magnitude passes back through rest at every direction reversal.
 * [cooldownMillis] then keeps one gesture from reporting several times as it decays.
 *
 * The defaults are a starting point tuned to be hard to trigger by accident; they are constructor
 * parameters so they can be tightened against real devices without touching this logic.
 *
 * Nothing is registered until [start], and [stop] leaves no listener behind. The detector reads
 * acceleration only, which needs no runtime permission.
 *
 * @param sensorManager Platform sensor service, null on a device that does not expose one.
 * @param threshold Acceleration counted as shaking, as a multiple of gravity.
 * @param minimumDurationMillis How long the gesture must last before it counts.
 * @param cooldownMillis Quiet period after a reported shake.
 * @param elapsedRealtime Monotonic clock, replaceable in tests.
 * @param onShake Invoked on the main thread, which is where sensor callbacks are delivered.
 */
class ShakeDetector(
    private val sensorManager: SensorManager?,
    private val threshold: Float = DEFAULT_THRESHOLD_G,
    private val minimumDurationMillis: Long = DEFAULT_MINIMUM_DURATION_MILLIS,
    private val cooldownMillis: Long = DEFAULT_COOLDOWN_MILLIS,
    private val elapsedRealtime: () -> Long = SystemClock::elapsedRealtime,
    private val onShake: () -> Unit,
) : SensorEventListener {

    private val accelerometer: Sensor? by lazy {
        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private var listening: Boolean = false
    private var shakingSince: Long = NO_TIMESTAMP
    private var shakingSamples: Int = 0
    private var lastShakeAt: Long = NO_TIMESTAMP

    /**
     * Registers for accelerometer updates.
     *
     * Returns false when there is nothing to listen to, so a caller can tell a device without an
     * accelerometer from one that simply has not been shaken yet. Calling it while already
     * listening does nothing.
     */
    fun start(): Boolean {
        if (listening) return true

        val sensor: Sensor = accelerometer ?: return false
        val registered: Boolean = sensorManager?.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_GAME,
        ) == true

        if (registered) {
            listening = true
            resetGesture()
        }
        return registered
    }

    /**
     * Unregisters the listener.
     *
     * Sensors keep drawing power for as long as anything is listening, so this is called as soon as
     * the reporter has no activity to open over, not when the process ends.
     */
    fun stop() {
        if (!listening) return
        sensorManager?.unregisterListener(this)
        listening = false
        resetGesture()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return
        if (event.values.size < AXIS_COUNT) return

        val x: Float = event.values[0]
        val y: Float = event.values[1]
        val z: Float = event.values[2]

        onAcceleration(
            gForce = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH,
            now = elapsedRealtime(),
        )
    }

    /**
     * The gesture decision, separated from the sensor that feeds it.
     *
     * `SensorEvent` cannot be constructed outside the framework, so the thresholds could otherwise
     * only be exercised on a device. This is the seam the tests drive; production code reaches it
     * through [onSensorChanged].
     */
    internal fun onAcceleration(gForce: Float, now: Long) {
        if (gForce < threshold) return

        if (shakingSince == NO_TIMESTAMP || now - shakingSince > GESTURE_WINDOW_MILLIS) {
            shakingSince = now
            shakingSamples = 1
            return
        }

        shakingSamples++

        val sustained: Boolean = now - shakingSince >= minimumDurationMillis &&
                shakingSamples >= MINIMUM_SAMPLES
        if (!sustained) return

        val cooling: Boolean = lastShakeAt != NO_TIMESTAMP && now - lastShakeAt < cooldownMillis
        resetGesture()
        if (cooling) return

        lastShakeAt = now
        onShake()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun resetGesture() {
        shakingSince = NO_TIMESTAMP
        shakingSamples = 0
    }

    companion object {
        /** Acceleration, in multiples of gravity, above which the device counts as shaking. */
        const val DEFAULT_THRESHOLD_G: Float = 2.5f

        /** How long a gesture must stay above the threshold before it is reported. */
        const val DEFAULT_MINIMUM_DURATION_MILLIS: Long = 300L

        /** Quiet period after a reported shake, so one gesture reports once. */
        const val DEFAULT_COOLDOWN_MILLIS: Long = 1_500L

        /** How long readings are gathered before a half-finished gesture is forgotten. */
        private const val GESTURE_WINDOW_MILLIS: Long = 1_000L

        /** Readings above the threshold a gesture needs, which a single jolt cannot reach. */
        private const val MINIMUM_SAMPLES: Int = 4

        private const val AXIS_COUNT: Int = 3
        private const val NO_TIMESTAMP: Long = -1L
    }
}
