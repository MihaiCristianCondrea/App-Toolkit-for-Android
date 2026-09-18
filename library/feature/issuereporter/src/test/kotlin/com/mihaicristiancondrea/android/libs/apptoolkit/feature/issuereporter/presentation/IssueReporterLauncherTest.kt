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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation

import android.app.Activity
import androidx.activity.ComponentActivity
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

/**
 * Covers the cases the launcher has to survive rather than crash on.
 *
 * The shake detector calls it from a sensor callback, so the activity it is handed can be halfway
 * through going away by the time the gesture lands, and the screen underneath may already be
 * showing a sheet of its own. None of these is worth taking an app down for, and each has to be
 * answered the same way: do not mount a sheet.
 */
class IssueReporterLauncherTest {

    @AfterEach
    fun clearPresence() {
        while (IssueReporterPresence.isShowing) {
            IssueReporterPresence.onHidden()
        }
    }

    @Test
    fun `an activity that cannot own a composition is refused`() {
        val activity: Activity = mockk()

        assertThat(IssueReporterLauncher.show(activity = activity)).isFalse()
    }

    @Test
    fun `a finishing activity is refused`() {
        val activity: ComponentActivity = mockk {
            every { isFinishing } returns true
        }

        assertThat(IssueReporterLauncher.show(activity = activity)).isFalse()
    }

    @Test
    fun `a destroyed activity is refused`() {
        val activity: ComponentActivity = mockk {
            every { isFinishing } returns false
            every { isDestroyed } returns true
        }

        assertThat(IssueReporterLauncher.show(activity = activity)).isFalse()
    }

    @Test
    fun `a sheet a host is already showing is not stacked on`() {
        // The screen underneath composed its own sheet, which the launcher cannot see as a view.
        IssueReporterPresence.onShown()
        val activity: ComponentActivity = mockk {
            every { isFinishing } returns false
            every { isDestroyed } returns false
        }

        assertThat(IssueReporterLauncher.show(activity = activity)).isFalse()
    }
}
