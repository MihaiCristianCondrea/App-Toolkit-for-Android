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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.extensions.packagemanager

import android.content.pm.PackageManager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.platform.AppVersionMetadata
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.getVersionMetadata
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.AppVersionInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PackageVersionCompatibilityTest {
    @Test
    fun preservesNullableNameLongCodeAndAbsence() {
        mockkStatic(PackageManager::getVersionMetadata)
        try {
            val manager = mockk<PackageManager>()
            val code = Int.MAX_VALUE.toLong() + 10
            every { manager.getVersionMetadata("example.app") } returns AppVersionMetadata(null, code)
            assertEquals(AppVersionInfo(null, code), manager.getVersionInfo("example.app"))
            every { manager.getVersionMetadata("example.app") } returns null
            assertNull(manager.getVersionInfo("example.app"))
        } finally {
            unmockkStatic(PackageManager::getVersionMetadata)
        }
    }
}
