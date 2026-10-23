/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.local.AndroidWidgetAppsDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.models.WidgetApp
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.remote.CoilWidgetIconDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/** Loads only the requested visible apps, preferring installed icons, then catalog icons, then ours. */
internal class DefaultWidgetAppsRepository(
    private val developerApps: DeveloperAppsRepository,
    private val local: AndroidWidgetAppsDataSource,
    private val remote: CoilWidgetIconDataSource,
) : WidgetAppsRepository {
    override suspend fun savedApps(limit: Int): List<WidgetApp>? = developerApps.savedDeveloperApps()?.loadIcons(limit)

    override suspend fun fetchApps(limit: Int): List<WidgetApp> = developerApps.fetchDeveloperApps().loadIcons(limit)

    private suspend fun List<AppSummary>.loadIcons(limit: Int): List<WidgetApp> = coroutineScope {
        take(limit).map { app ->
            async {
                WidgetApp(
                    app = app,
                    icon = local.installedIcon(app.packageName, ICON_SIZE_PX)
                        ?: remote.icon(app.iconUrl, ICON_SIZE_PX)
                        ?: local.fallbackIcon(ICON_SIZE_PX),
                    launchIntent = local.launchIntent(app.packageName),
                )
            }
        }.awaitAll()
    }

    private companion object {
        const val ICON_SIZE_PX = 72
    }
}
