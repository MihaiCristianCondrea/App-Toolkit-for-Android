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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.platform.AppVersionMetadata
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** JVM Android stubs exercise the legacy branch; the modern API is covered on-device. */
class PackageVersionMetadataTest {
    @Suppress("DEPRECATION")
    @Test
    fun preservesNullableNamesAndLegacyVersionCodes() {
        val manager = mockk<PackageManager>()
        val info = mockk<PackageInfo>()
        info.versionName = null
        info.versionCode = 123
        every { manager.getPackageInfo("example.app", 0) } returns info
        assertEquals(AppVersionMetadata(null, 123L), manager.getVersionMetadata("example.app"))
        info.versionName = "1.2.3"
        assertEquals(AppVersionMetadata("1.2.3", 123L), manager.getVersionMetadata("example.app"))
    }

    @Suppress("DEPRECATION")
    @Test
    fun unreadablePackagesReturnNull() {
        val manager = mockk<PackageManager>()
        every { manager.getPackageInfo("missing.app", 0) } throws SecurityException("Hidden")
        assertNull(manager.getVersionMetadata("missing.app"))
    }
}
