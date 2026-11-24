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
import android.hardware.SensorManager
import android.os.Bundle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.IssueReporterConfig

/**
 * Turns a shake of the device into the issue reporter, for every activity in the app.
 *
 * It is application-scoped on purpose. An app built on this toolkit has several activities, and
 * copying a sensor listener into each of them would be the same code repeated per screen and a
 * listener that outlives the screen that registered it. Only the foreground activity can present
 * anything, so one listener that follows the resumed activity is all the feature needs.
 *
 * The accelerometer is registered when an activity resumes and unregistered when it pauses, which
 * is the platform's own guidance: a sensor left registered keeps drawing power with nothing to show
 * for it. That also means a backgrounded app reads no sensor at all.
 *
 * Nothing happens unless [IssueReporterConfig.shakeToReportEnabled] is set, and nothing happens
 * until [install] is called, so a host that does not want the gesture never pays for it.
 *
 * @param application The app whose activities are followed.
 * @param config Host configuration deciding whether the gesture is active.
 */
class IssueReporterShakeManager(
    private val application: Application,
    private val config: IssueReporterConfig,
) : Application.ActivityLifecycleCallbacks {

    private val shakeDetector: ShakeDetector by lazy {
        ShakeDetector(
            sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as? SensorManager,
            onShake = ::onShakeDetected,
        )
    }

    private var resumedActivity: Activity? = null
    private var installed: Boolean = false
    private var suppressionCount: Int = 0

    /**
     * Starts following the app's activities.
     *
     * Does nothing when the gesture is disabled or already installed, so a host can call it
     * unconditionally from `Application.onCreate`.
     */
    fun install() {
        if (installed || !config.shakeToReportEnabled) return
        application.registerActivityLifecycleCallbacks(this)
        installed = true
    }

    /** Stops following activities and releases the sensor. */
    fun uninstall() {
        if (!installed) return
        application.unregisterActivityLifecycleCallbacks(this)
        installed = false
        shakeDetector.stop()
        resumedActivity = null
    }

    /**
     * Temporarily stops shake-to-report until the returned handle is closed.
     *
     * Use this while a screen or tool owns the shake gesture itself so shaking the device does not
     * open the report sheet. Unregistering the accelerometer also clears any half-finished gesture,
     * preventing a carry-over trigger right after suppression ends.
     *
     * Overlapping callers are reference-counted, and closing a handle more than once has no
     * effect. Returns a no-op handle when [IssueReporterConfig.shakeToReportEnabled] is false. Must
     * be called on the main thread.
     */
    fun suppress(): AutoCloseable {
        if (!config.shakeToReportEnabled) return AutoCloseable { }
        suppressionCount++
        if (installed && (suppressionCount == 1)) {
            shakeDetector.stop()
        }
        var closed = false
        return AutoCloseable {
            if (closed) return@AutoCloseable
            closed = true
            if (suppressionCount > 0) {
                suppressionCount--
            }
            if ((suppressionCount == 0) && installed && (resumedActivity != null)) {
                shakeDetector.start()
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        resumedActivity = activity
        if (suppressionCount == 0) {
            shakeDetector.start()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (resumedActivity === activity) {
            shakeDetector.stop()
            resumedActivity = null
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (resumedActivity === activity) {
            shakeDetector.stop()
            resumedActivity = null
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    /**
     * Opens the reporter over whatever is in front.
     *
     * Sensor callbacks are delivered on the main thread, so this reaches the fragment manager from
     * the right thread. A second shake while the sheet is open is absorbed by the launcher, which
     * will not show a sheet that is already showing.
     */
    private fun onShakeDetected() {
        val activity: Activity = resumedActivity ?: return
        IssueReporterLauncher.show(activity = activity)
    }
}
