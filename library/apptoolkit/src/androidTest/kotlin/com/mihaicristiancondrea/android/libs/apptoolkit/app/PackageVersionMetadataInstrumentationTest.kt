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

package com.mihaicristiancondrea.android.libs.apptoolkit.app

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.packagemanager.getVersionMetadata
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.AppVersionInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.extensions.packagemanager.getVersionInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PackageVersionMetadataInstrumentationTest {
    @Suppress("DEPRECATION")
    @Test
    fun installedPackageAndCompatibilityAdapterMatchPlatform() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val manager = context.packageManager
        val info = manager.getPackageInfo(context.packageName, 0)
        val expectedCode = if (Build.VERSION.SDK_INT >= 28) info.longVersionCode else info.versionCode.toLong()
        val metadata = manager.getVersionMetadata(context.packageName)
        assertNotNull(metadata)
        assertEquals(info.versionName, metadata!!.versionName)
        assertEquals(expectedCode, metadata.versionCode)
        assertEquals(AppVersionInfo(info.versionName, expectedCode), manager.getVersionInfo(context.packageName))
        assertNull(manager.getVersionMetadata("invalid.missing.architecture.test.package"))
        assertNull(manager.getVersionInfo("invalid.missing.architecture.test.package"))
    }
}
