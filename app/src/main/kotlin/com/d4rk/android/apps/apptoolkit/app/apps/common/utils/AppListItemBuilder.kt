package com.d4rk.android.apps.apptoolkit.app.apps.common.utils

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppListItem
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
