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
