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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.gms.data.repositories.GooglePlayServicesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.local.AboutDeviceInfoDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestDefaultAboutRepository {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val deviceInfoDataSource = object : AboutDeviceInfoDataSource {
        override val deviceInfo: String = "device-info"
    }

    private val buildInfoRepository = object : BuildInfoRepository {
        override val appVersion: String = "1.0"
        override val appVersionCode: Int = 1
        override val packageName: String = "pkg"
        override val isDebugBuild: Boolean = false
    }

    private fun repository(
        googlePlayServices: GooglePlayServicesRepository = mockk {
            coEvery { getVersion() } returns "24.01.12"
        },
        toolkitVersionProvider: () -> String = { "3.0.0-test" },
    ): DefaultAboutRepository =
        DefaultAboutRepository(
            deviceInfoDataSource = deviceInfoDataSource,
            buildInfoRepository = buildInfoRepository,
            diagnostics = mockk<TelemetryRepository>(relaxed = true),
            googlePlayServices = googlePlayServices,
            toolkitVersionProvider = toolkitVersionProvider,
        )

    @Test
    fun `getAboutInfo returns host and toolkit metadata`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repo = repository()

            val result: AboutInfo = repo.getAboutInfo()

            assertEquals(
                AboutInfo(
                    appVersion = "1.0",
                    appVersionCode = 1,
                    appToolkitVersion = "3.0.0-test",
                    googlePlayServicesVersion = "24.01.12",
                    deviceInfo = deviceInfoDataSource.deviceInfo,
                ),
                result,
            )
        }

    @Test
    fun `getAboutInfo reports absent google play services as null`() =
        runTest(dispatcherExtension.testDispatcher) {
            val gmsProvider = mockk<GooglePlayServicesRepository> {
                coEvery { getVersion() } returns null
            }
            val repo = repository(googlePlayServices = gmsProvider)

            val result: AboutInfo = repo.getAboutInfo()

            assertNull(result.googlePlayServicesVersion)
        }

    @Test
    fun `getAboutInfo reports a blank toolkit version as supplied`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repo = repository(toolkitVersionProvider = { "" })

            val result: AboutInfo = repo.getAboutInfo()

            assertEquals("", result.appToolkitVersion)
        }
}
