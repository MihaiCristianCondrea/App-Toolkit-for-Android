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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

/**
 * Covers the cases the launcher has to survive rather than crash on.
 *
 * The shake detector calls it from a sensor callback, so the activity it is handed can be halfway
 * through going away by the time the gesture lands. None of these is an error worth taking an app
 * down for, and each one has to be answered the same way: do not show a sheet.
 */
class IssueReporterLauncherTest {

    @Test
    fun `an activity that cannot host fragments is refused`() {
        val activity: Activity = mockk()

        assertThat(IssueReporterLauncher.show(activity = activity)).isFalse()
    }

    @Test
    fun `a finishing activity is refused`() {
        val activity: FragmentActivity = mockk {
            every { isFinishing } returns true
        }

        assertThat(IssueReporterLauncher.show(activity = activity)).isFalse()
    }

    @Test
    fun `an activity whose state is already saved is refused`() {
        val fragmentManager: FragmentManager = mockk {
            every { isStateSaved } returns true
        }
        val activity: FragmentActivity = mockk {
            every { isFinishing } returns false
            every { isDestroyed } returns false
            every { supportFragmentManager } returns fragmentManager
        }

        assertThat(IssueReporterLauncher.show(activity = activity)).isFalse()
    }

    @Test
    fun `a sheet that is already showing is not shown again`() {
        val fragmentManager: FragmentManager = mockk {
            every { isStateSaved } returns false
            every { isDestroyed } returns false
            every { findFragmentByTag(IssueReporterLauncher.FRAGMENT_TAG) } returns mockk()
        }
        val activity: FragmentActivity = mockk {
            every { isFinishing } returns false
            every { isDestroyed } returns false
            every { supportFragmentManager } returns fragmentManager
        }

        assertThat(IssueReporterLauncher.show(activity = activity)).isFalse()
    }
}
