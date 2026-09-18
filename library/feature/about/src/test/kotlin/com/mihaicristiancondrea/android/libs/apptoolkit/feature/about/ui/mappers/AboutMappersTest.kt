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

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemKey
import org.junit.jupiter.api.Test

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
        val items = aboutInfo.toUiState().items

        assertThat(items.map { it.key }).containsExactly(
            AboutItemKey.HEADER_APP_INFO,
            AboutItemKey.APP_NAME,
            AboutItemKey.APP_BUILD_VERSION,
            AboutItemKey.APP_TOOLKIT_VERSION,
            AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION,
            AboutItemKey.OSS_LICENSES,
            AboutItemKey.HEADER_DEVICE_INFO,
            AboutItemKey.DEVICE_INFO,
        ).inOrder()

        val appName = items.preference(AboutItemKey.APP_NAME)
        assertThat(appName.position).isEqualTo(GroupedItemPosition.FIRST)

        val buildVersion = items.preference(AboutItemKey.APP_BUILD_VERSION)
        assertThat((buildVersion.summary as UiTextHelper.DynamicString).content).isEqualTo("1.0 (1)")
        assertThat(buildVersion.action).isEqualTo(AboutItemAction.VersionEasterEgg)

        val licenses = items.preference(AboutItemKey.OSS_LICENSES)
        assertThat(licenses.position).isEqualTo(GroupedItemPosition.LAST)
        assertThat(licenses.action).isEqualTo(AboutItemAction.OpenLicenses)
    }

    @Test
    fun `device info item carries the displayed report as its copy payload`() {
        val deviceInfoItem = aboutInfo.toUiState().items.preference(AboutItemKey.DEVICE_INFO)

        assertThat((deviceInfoItem.summary as UiTextHelper.DynamicString).content)
            .isEqualTo("device-info")
        assertThat(deviceInfoItem.position).isEqualTo(GroupedItemPosition.SINGLE)
        assertThat(deviceInfoItem.action)
            .isEqualTo(AboutItemAction.CopyDeviceInfo(deviceInfo = "device-info"))
    }

    @Test
    fun `omits google play services when it is not installed`() {
        val items = aboutInfo.copy(googlePlayServicesVersion = null).toUiState().items

        assertThat(items.none { it.key == AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION }).isTrue()
        assertThat(items.preference(AboutItemKey.OSS_LICENSES).position)
            .isEqualTo(GroupedItemPosition.LAST)
    }

    @Test
    fun `omits the toolkit version when it is blank`() {
        val items = aboutInfo.copy(appToolkitVersion = "").toUiState().items

        assertThat(items.none { it.key == AboutItemKey.APP_TOOLKIT_VERSION }).isTrue()
    }

    @Test
    fun `omits the device info section when there is nothing to show`() {
        val items = aboutInfo.copy(deviceInfo = "   ").toUiState().items

        assertThat(items.none { it.key == AboutItemKey.HEADER_DEVICE_INFO }).isTrue()
        assertThat(items.none { it.key == AboutItemKey.DEVICE_INFO }).isTrue()
        assertThat(items.last().key).isEqualTo(AboutItemKey.OSS_LICENSES)
    }

    private fun List<AboutItem>.preference(key: String): AboutItem.Preference =
        first { it.key == key } as AboutItem.Preference
}
