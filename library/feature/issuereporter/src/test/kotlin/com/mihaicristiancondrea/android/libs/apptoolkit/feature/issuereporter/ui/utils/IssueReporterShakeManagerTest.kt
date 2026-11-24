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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.IssueReporterConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

/**
 * Covers what the manager promises a host: nothing happens unless the host asked for it, and
 * temporary suppression stops the accelerometer until every active caller releases it.
 */
class IssueReporterShakeManagerTest {

    private val accelerometer: Sensor = mockk()
    private val sensorManager: SensorManager = mockk(relaxed = true) {
        every { getDefaultSensor(Sensor.TYPE_ACCELEROMETER) } returns accelerometer
        every {
            registerListener(
                any<SensorEventListener>(),
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME,
            )
        } returns true
    }
    private val application: Application = mockk(relaxed = true) {
        every { getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
    }

    private fun manager(shakeToReportEnabled: Boolean): IssueReporterShakeManager =
        IssueReporterShakeManager(
            application = application,
            config = IssueReporterConfig(shakeToReportEnabled = shakeToReportEnabled),
        )

    @Test
    fun `install does nothing when the host has not enabled the gesture`() {
        manager(shakeToReportEnabled = false).install()

        verify(exactly = 0) { application.registerActivityLifecycleCallbacks(any()) }
    }

    @Test
    fun `install follows the app's activities when the gesture is enabled`() {
        manager(shakeToReportEnabled = true).install()

        verify(exactly = 1) { application.registerActivityLifecycleCallbacks(any()) }
    }

    @Test
    fun `installing twice registers once`() {
        val manager = manager(shakeToReportEnabled = true)
        manager.install()
        manager.install()

        verify(exactly = 1) { application.registerActivityLifecycleCallbacks(any()) }
    }

    @Test
    fun `uninstall stops following activities`() {
        val manager = manager(shakeToReportEnabled = true)
        manager.install()
        manager.uninstall()

        verify(exactly = 1) { application.unregisterActivityLifecycleCallbacks(any()) }
    }

    @Test
    fun `uninstall without install does nothing`() {
        manager(shakeToReportEnabled = true).uninstall()

        verify(exactly = 0) { application.unregisterActivityLifecycleCallbacks(any()) }
    }

    @Test
    fun `suppress stops listening on a resumed activity and resumes when closed`() {
        val manager = manager(shakeToReportEnabled = true)
        val activity: Activity = mockk()
        manager.install()
        manager.onActivityResumed(activity = activity)

        val handle = manager.suppress()
        verify(exactly = 1) { sensorManager.unregisterListener(any<SensorEventListener>()) }

        handle.close()
        verify(exactly = 2) {
            sensorManager.registerListener(
                any<SensorEventListener>(),
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME,
            )
        }
    }

    @Test
    fun `overlapping suppressions wait for every handle and ignore repeated close calls`() {
        val manager = manager(shakeToReportEnabled = true)
        val activity: Activity = mockk()
        manager.install()
        manager.onActivityResumed(activity = activity)

        val first = manager.suppress()
        val second = manager.suppress()

        first.close()
        first.close()
        verify(exactly = 1) {
            sensorManager.registerListener(
                any<SensorEventListener>(),
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME,
            )
        }

        second.close()
        verify(exactly = 2) {
            sensorManager.registerListener(
                any<SensorEventListener>(),
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME,
            )
        }
    }

    @Test
    fun `activity resume does not register the sensor while suppressed`() {
        val manager = manager(shakeToReportEnabled = true)
        val activity: Activity = mockk()
        manager.install()

        val handle = manager.suppress()
        manager.onActivityResumed(activity = activity)
        verify(exactly = 0) {
            sensorManager.registerListener(
                any<SensorEventListener>(),
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME,
            )
        }

        manager.onActivityPaused(activity = activity)
        handle.close()
        verify(exactly = 0) {
            sensorManager.registerListener(
                any<SensorEventListener>(),
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME,
            )
        }

        manager.onActivityResumed(activity = activity)
        verify(exactly = 1) {
            sensorManager.registerListener(
                any<SensorEventListener>(),
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME,
            )
        }
    }

    @Test
    fun `suppress does not touch sensors when the gesture is disabled`() {
        val handle = manager(shakeToReportEnabled = false).suppress()
        handle.close()

        verify(exactly = 0) { application.getSystemService(Context.SENSOR_SERVICE) }
    }

    @Test
    fun `pausing an older activity does not stop listening for the newly resumed activity`() {
        val manager = manager(shakeToReportEnabled = true)
        val firstActivity: Activity = mockk()
        val secondActivity: Activity = mockk()
        manager.install()
        manager.onActivityResumed(activity = firstActivity)
        manager.onActivityResumed(activity = secondActivity)
        manager.onActivityPaused(activity = firstActivity)

        verify(exactly = 0) { sensorManager.unregisterListener(any<SensorEventListener>()) }
    }
}
