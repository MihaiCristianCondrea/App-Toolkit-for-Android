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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.mappers

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.models.CachedAppCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.models.CachedAppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.models.DeveloperAppsCache
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.models.DeveloperAppsCacheData
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppSummary

fun DeveloperAppsCache.toDomain(): List<AppSummary> = data.apps.map { cached ->
    AppSummary(
        name = cached.name,
        packageName = cached.packageName,
        iconUrl = cached.iconUrl,
        shortDescription = cached.shortDescription.orEmpty(),
        category = cached.category?.let { AppCategory(label = it.label, id = it.id) },
    )
}

fun List<AppSummary>.toCache(): DeveloperAppsCache = DeveloperAppsCache(
    data = DeveloperAppsCacheData(
        apps = map { app ->
            CachedAppSummary(
                name = app.name,
                packageName = app.packageName,
                iconUrl = app.iconUrl,
                shortDescription = app.shortDescription,
                category = app.category?.let { CachedAppCategory(label = it.label, id = it.id) },
            )
        },
    ),
)
