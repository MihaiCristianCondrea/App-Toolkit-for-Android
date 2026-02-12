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

package com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.utils

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Builds the list of [AppListItem] entries by interleaving ads between app items when enabled.
 */
fun buildAppListItems(
    apps: ImmutableList<AppInfo>,
    adsEnabled: Boolean,
    adFrequency: Int
): ImmutableList<AppListItem> {
    if (!adsEnabled || adFrequency <= 0) {
        return apps.map { AppListItem.App(it) }.toImmutableList()
    }

    val listItems = ArrayList<AppListItem>(apps.size + (apps.size / adFrequency))
    apps.forEachIndexed { index, app ->
        listItems += AppListItem.App(app)
        val isTimeForAd = (index + 1) % adFrequency == 0
        if (isTimeForAd) listItems += AppListItem.Ad
    }
    return listItems.toImmutableList()
}
