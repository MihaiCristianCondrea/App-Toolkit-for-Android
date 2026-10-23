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

package com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.repositories

import android.graphics.Bitmap
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.local.AndroidWidgetAppsDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.remote.CoilWidgetIconDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.InstalledAppsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DefaultWidgetAppsRepositoryTest {
    private val catalog = mockk<DeveloperAppsRepository>()
    private val local = mockk<AndroidWidgetAppsDataSource>()
    private val remote = mockk<CoilWidgetIconDataSource>()
    private val installedApps = mockk<InstalledAppsRepository>()
    private val repository = DefaultWidgetAppsRepository(catalog, local, remote, installedApps)
    private val app = AppSummary(name = "App", packageName = "example.app", iconUrl = "https://example.test/icon.png", shortDescription = "")
    private val icon = mockk<Bitmap>()

    init {
        coEvery { installedApps.getLaunchIntent(any()) } returns null
        coEvery { local.installedIcon(any(), any()) } returns null
        coEvery { remote.icon(any(), any()) } returns null
        coEvery { local.fallbackIcon(any()) } returns icon
        coEvery { catalog.fetchDeveloperApps() } returns listOf(app)
    }

    @Test
    fun `installed icons avoid remote loading`() = runTest {
        coEvery { local.installedIcon(app.packageName, any()) } returns icon
        assertSame(icon, repository.fetchApps(limit = 9).single().icon)
        coVerify(exactly = 0) { remote.icon(any(), any()) }
        coVerify(exactly = 0) { local.fallbackIcon(any()) }
    }

    @Test
    fun `catalog icons avoid the fallback icon`() = runTest {
        coEvery { remote.icon(app.iconUrl, any()) } returns icon
        assertSame(icon, repository.fetchApps(limit = 9).single().icon)
        coVerify(exactly = 0) { local.fallbackIcon(any()) }
    }

    @Test
    fun `missing installed and catalog icons use the app icon`() = runTest {
        assertSame(icon, repository.fetchApps(limit = 9).single().icon)
        coVerify(exactly = 1) { local.fallbackIcon(any()) }
    }

    @Test
    fun `only the nine visible apps load icons`() = runTest {
        coEvery { catalog.fetchDeveloperApps() } returns List(12) { app.copy(packageName = "example.app" + it) }
        assertEquals(9, repository.fetchApps(limit = 9).size)
        coVerify(exactly = 9) { local.installedIcon(any(), any()) }
    }

    @Test
    fun `an absent saved catalog does no icon or network work`() = runTest {
        coEvery { catalog.savedDeveloperApps() } returns null
        assertNull(repository.savedApps(limit = 9))
        coVerify(exactly = 0) { catalog.fetchDeveloperApps() }
        coVerify(exactly = 0) { remote.icon(any(), any()) }
    }

    @Test
    fun `icon loading cancellation propagates`() = runTest {
        coEvery { remote.icon(any(), any()) } throws CancellationException("cancelled")
        assertThrows<CancellationException> { repository.fetchApps(limit = 9) }
        coVerify(exactly = 0) { local.fallbackIcon(any()) }
    }
}
