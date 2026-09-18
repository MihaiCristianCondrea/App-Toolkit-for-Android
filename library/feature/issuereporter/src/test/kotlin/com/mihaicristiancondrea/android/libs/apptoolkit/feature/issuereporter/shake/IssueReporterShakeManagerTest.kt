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

import android.app.Application
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReporterConfig
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

/**
 * Covers what the manager promises a host: nothing happens unless the host asked for it.
 *
 * Registering activity callbacks is the observable half of that promise, because it is what puts
 * the manager in a position to register a sensor at all.
 */
class IssueReporterShakeManagerTest {

    private val application: Application = mockk(relaxed = true)

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
}
