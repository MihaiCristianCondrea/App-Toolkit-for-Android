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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemKey
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AboutMappersTest {

    private val aboutInfo = AboutInfo(
        appVersion = "1.0",
        appVersionCode = 1,
        appToolkitVersion = "3.0.0-test",
        googlePlayServicesVersion = "24.01.12",
        deviceInfo = "device-info",
    )

    @Test
    fun `maps full about info to ordered items`() {
        val items = aboutInfo.toAboutItems()

        assertEquals(
            listOf(
                AboutItemKey.HEADER_APP_INFO,
                AboutItemKey.APP_NAME,
                AboutItemKey.APP_BUILD_VERSION,
                AboutItemKey.APP_TOOLKIT_VERSION,
                AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION,
                AboutItemKey.OSS_LICENSES,
                AboutItemKey.HEADER_DEVICE_INFO,
                AboutItemKey.DEVICE_INFO,
            ),
            items.map { it.key },
        )

        val appName = items.preference(AboutItemKey.APP_NAME)
        assertEquals(GroupedItemPosition.FIRST, appName.position)

        val buildVersion = items.preference(AboutItemKey.APP_BUILD_VERSION)
        assertEquals("1.0 (1)", (buildVersion.summary as UiTextHelper.DynamicString).content)

        val licenses = items.preference(AboutItemKey.OSS_LICENSES)
        assertEquals(GroupedItemPosition.LAST, licenses.position)
        assertEquals(AboutItemAction.OpenLicenses, licenses.action)
    }

    @Test
    fun `device info item carries the displayed report as its copy payload`() {
        val deviceInfoItem = aboutInfo.toAboutItems().preference(AboutItemKey.DEVICE_INFO)

        assertEquals("device-info", (deviceInfoItem.summary as UiTextHelper.DynamicString).content)
        assertEquals(GroupedItemPosition.SINGLE, deviceInfoItem.position)
        assertEquals(
            AboutItemAction.CopyToClipboard(
                label = UiTextHelper.StringResource(R.string.device_info),
                text = UiTextHelper.DynamicString("device-info"),
                successMessage = UiTextHelper.StringResource(R.string.snack_device_info_copied),
            ),
            deviceInfoItem.action,
        )
    }

    @Test
    fun `version rows copy their own value without a custom confirmation`() {
        val items = aboutInfo.toAboutItems()

        assertEquals(
            AboutItemAction.CopyToClipboard(
                label = UiTextHelper.StringResource(R.string.app_toolkit_version),
                text = UiTextHelper.DynamicString("3.0.0-test"),
            ),
            items.preference(AboutItemKey.APP_TOOLKIT_VERSION).action,
        )
        assertEquals(
            AboutItemAction.CopyToClipboard(
                label = UiTextHelper.StringResource(R.string.google_play_services_version),
                text = UiTextHelper.DynamicString("24.01.12"),
            ),
            items.preference(AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION).action,
        )
    }

    @Test
    fun `omits google play services when it is not installed`() {
        val items = aboutInfo.copy(googlePlayServicesVersion = null).toAboutItems()

        assertTrue(items.none { it.key == AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION })
        assertEquals(GroupedItemPosition.LAST, items.preference(AboutItemKey.OSS_LICENSES).position)
    }

    @Test
    fun `omits the toolkit version when it is blank`() {
        val items = aboutInfo.copy(appToolkitVersion = "").toAboutItems()

        assertTrue(items.none { it.key == AboutItemKey.APP_TOOLKIT_VERSION })
    }

    @Test
    fun `omits the device info section when there is nothing to show`() {
        val items = aboutInfo.copy(deviceInfo = "   ").toAboutItems()

        assertTrue(items.none { it.key == AboutItemKey.HEADER_DEVICE_INFO })
        assertTrue(items.none { it.key == AboutItemKey.DEVICE_INFO })
        assertEquals(AboutItemKey.OSS_LICENSES, items.last().key)
    }

    /** A row that looked clickable but did nothing is what made most of this screen seem broken. */
    @Test
    fun `every row that shows a value copies it, except licenses and the build version`() {
        val items = aboutInfo.toAboutItems()
        val preferences = items.filterIsInstance<AboutItem.Preference>()

        val notCopyable = preferences
            .filterNot { it.action is AboutItemAction.CopyToClipboard }
            .map { it.key }

        assertEquals(
            listOf(AboutItemKey.OSS_LICENSES, AboutItemKey.APP_BUILD_VERSION).sorted(),
            notCopyable.sorted(),
        )
        assertEquals(AboutItemAction.OpenLicenses, preferences.first { it.key == AboutItemKey.OSS_LICENSES }.action)
    }

    @Test
    fun `the app name row copies the name itself`() {
        val appName = aboutInfo.toAboutItems().preference(AboutItemKey.APP_NAME)

        val action = appName.action as AboutItemAction.CopyToClipboard
        assertEquals(appName.title, action.text)
    }

    @Test
    fun `the build version row feeds the version tap counter and copies nothing`() {
        val buildVersion = aboutInfo.toAboutItems().preference(AboutItemKey.APP_BUILD_VERSION)

        assertTrue(buildVersion.countsVersionTap)
        assertEquals(UiTextHelper.DynamicString("1.0 (1)"), buildVersion.summary)
        assertNull(buildVersion.action)
    }

    @Test
    fun `no other row feeds the version tap counter`() {
        val counting = aboutInfo.toAboutItems()
            .filterIsInstance<AboutItem.Preference>()
            .filter { it.countsVersionTap }
            .map { it.key }

        assertEquals(listOf(AboutItemKey.APP_BUILD_VERSION), counting)
    }

    private fun List<AboutItem>.preference(key: String): AboutItem.Preference =
        first { it.key == key } as AboutItem.Preference
}
