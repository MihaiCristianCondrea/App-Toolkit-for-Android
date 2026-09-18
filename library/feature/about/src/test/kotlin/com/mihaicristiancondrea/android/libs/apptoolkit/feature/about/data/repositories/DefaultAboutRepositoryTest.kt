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

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.providers.GooglePlayServicesVersionProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestDefaultAboutRepository {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val deviceProvider = object : AboutSettingsProvider {
        override val deviceInfo: String = "device-info"
    }

    private val buildInfoProvider = object : BuildInfoProvider {
        override val appVersion: String = "1.0"
        override val appVersionCode: Int = 1
        override val packageName: String = "pkg"
        override val isDebugBuild: Boolean = false
    }

    private fun repository(
        gmsVersionProvider: GooglePlayServicesVersionProvider = mockk {
            every { getVersion() } returns "24.01.12"
        },
        toolkitVersionProvider: () -> String = { "3.0.0-test" },
    ): DefaultAboutRepository =
        DefaultAboutRepository(
            deviceProvider = deviceProvider,
            buildInfoProvider = buildInfoProvider,
            firebaseController = mockk<FirebaseController>(relaxed = true),
            gmsVersionProvider = gmsVersionProvider,
            toolkitVersionProvider = toolkitVersionProvider,
        )

    @Test
    fun `getAboutInfo returns host and toolkit metadata`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repo = repository()

            val result: AboutInfo = repo.getAboutInfo()

            assertThat(result).isEqualTo(
                AboutInfo(
                    appVersion = "1.0",
                    appVersionCode = 1,
                    appToolkitVersion = "3.0.0-test",
                    googlePlayServicesVersion = "24.01.12",
                    deviceInfo = deviceProvider.deviceInfo,
                )
            )
        }

    @Test
    fun `getAboutInfo reports absent google play services as null`() =
        runTest(dispatcherExtension.testDispatcher) {
            val gmsProvider = mockk<GooglePlayServicesVersionProvider> {
                every { getVersion() } returns null
            }
            val repo = repository(gmsVersionProvider = gmsProvider)

            val result: AboutInfo = repo.getAboutInfo()

            assertThat(result.googlePlayServicesVersion).isNull()
        }

    @Test
    fun `getAboutInfo reports a blank toolkit version as supplied`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repo = repository(toolkitVersionProvider = { "" })

            val result: AboutInfo = repo.getAboutInfo()

            assertThat(result.appToolkitVersion).isEmpty()
        }
}
