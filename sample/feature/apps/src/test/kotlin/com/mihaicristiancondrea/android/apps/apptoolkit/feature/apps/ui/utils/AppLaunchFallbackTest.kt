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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.utils

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.provider.Settings
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.ClipboardRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.InstalledAppsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [32, 33])
class AppLaunchFallbackTest {
    private val context = mockk<Context>(relaxed = true)
    private val installedApps = mockk<InstalledAppsRepository>()
    private val launcher = AndroidAppActionLauncher(context, mockk<ClipboardRepository>(), installedApps)

    @Test
    fun unavailableLaunchersFallBackToAppSettings() = runTest {
        coEvery { installedApps.getLaunchIntentSender("example.app") } returns null
        coEvery { installedApps.getLaunchIntent("example.app") } returns null
        assertTrue(launcher.openApp("example.app"))
        verify(exactly = 1) {
            context.startActivity(match { it.action == Settings.ACTION_APPLICATION_DETAILS_SETTINGS })
        }
        verify(exactly = 0) { context.packageManager }
    }

    @Test
    fun aRejectedLaunchTokenFallsBackToTheLauncherIntent() = runTest {
        val sender = mockk<IntentSender>()
        val intent = Intent(Intent.ACTION_MAIN)
        coEvery { installedApps.getLaunchIntentSender("example.app") } returns sender
        coEvery { installedApps.getLaunchIntent("example.app") } returns intent
        every { context.startIntentSender(sender, null, 0, 0, 0) } throws IntentSender.SendIntentException()
        assertTrue(launcher.openApp("example.app"))
        coVerify(exactly = 1) { installedApps.getLaunchIntent("example.app") }
        verify(exactly = 1) { context.startActivity(intent) }
    }

    @Test
    fun cancellationDoesNotOpenTheSettingsFallback() = runTest {
        coEvery { installedApps.getLaunchIntentSender("example.app") } returns null
        coEvery { installedApps.getLaunchIntent("example.app") } throws CancellationException("cancelled")
        assertFailsWith<CancellationException> { launcher.openApp("example.app") }
        verify(exactly = 0) { context.startActivity(any()) }
    }
}
