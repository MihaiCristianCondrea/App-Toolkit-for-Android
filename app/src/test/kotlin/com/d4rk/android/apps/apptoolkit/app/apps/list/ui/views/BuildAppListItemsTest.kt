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

package com.d4rk.android.apps.apptoolkit.app.apps.list.ui.views

import com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.utils.buildAppListItems
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Test
import kotlin.test.assertEquals

class BuildAppListItemsTest {

    @Test
    fun `buildAppListItems inserts ads at configured frequency`() {
        val apps = makeApps(count = 8)

        val items: List<AppListItem> =
            buildAppListItems(apps, adsEnabled = true, adFrequency = 4)

        val expected: List<AppListItem> =
            expectedItems(apps, adsEnabled = true, adFrequency = 4)

        assertEquals(expected, items)
    }

    @Test
    fun `buildAppListItems adds trailing ad when apps count not multiple of frequency`() {
        val apps = makeApps(count = 5)

        val items: List<AppListItem> =
            buildAppListItems(apps, adsEnabled = true, adFrequency = 4)

        val expected: List<AppListItem> =
            expectedItems(apps, adsEnabled = true, adFrequency = 4)

        assertEquals(expected, items)
    }

    @Test
    fun `buildAppListItems returns only apps when ads disabled`() {
        val apps = makeApps(count = 5)

        val items: List<AppListItem> =
            buildAppListItems(apps, adsEnabled = false, adFrequency = 4)

        val expected: List<AppListItem> =
            apps.map<AppInfo, AppListItem> { app -> AppListItem.App(app) }

        assertEquals(expected, items)
    }

    private fun makeApps(count: Int): ImmutableList<AppInfo> =
        (1..count).map { i ->
            AppInfo(
                name = "App$i",
                packageName = "pkg$i",
                iconUrl = "icon$i",
                description = "Description $i",
                screenshots = persistentListOf(),
            )
        }.toImmutableList()

    /**
     * Reference implementation used only in tests, matching the behavior your tests
     * currently assert: insert an ad after every [adFrequency] apps, and add a trailing ad
     * if there are remaining apps.
     */
    private fun expectedItems(
        apps: ImmutableList<AppInfo>,
        adsEnabled: Boolean,
        adFrequency: Int
    ): List<AppListItem> {
        if (!adsEnabled || adFrequency <= 0) {
            return apps.map<AppInfo, AppListItem> { app -> AppListItem.App(app) }
        }

        val out = ArrayList<AppListItem>(apps.size + (apps.size / adFrequency) + 1)
        apps.forEachIndexed { index, app ->
            out.add(AppListItem.App(app))
            val isBoundary = (index + 1) % adFrequency == 0
            if (isBoundary) out.add(AppListItem.Ad)
        }

        val hasRemainder = apps.isNotEmpty() && (apps.size % adFrequency != 0)
        if (hasRemainder) out.add(AppListItem.Ad)

        return out
    }
}
