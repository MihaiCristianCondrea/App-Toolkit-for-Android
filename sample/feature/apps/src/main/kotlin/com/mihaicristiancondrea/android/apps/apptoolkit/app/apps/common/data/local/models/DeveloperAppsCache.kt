package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeveloperAppsCache(
    @SerialName("data") val data: DeveloperAppsCacheData = DeveloperAppsCacheData(),
)

@Serializable
data class DeveloperAppsCacheData(
    @SerialName("apps") val apps: List<CachedAppSummary> = emptyList(),
)

@Serializable
data class CachedAppSummary(
    @SerialName("name") val name: String,
    @SerialName("package_name") val packageName: String,
    @SerialName("icon_logo") val iconUrl: String,
    @SerialName("short_description") val shortDescription: String? = null,
    @SerialName("category") val category: CachedAppCategory? = null,
)

@Serializable
data class CachedAppCategory(
    @SerialName("label") val label: String,
    @SerialName("category_id") val id: String,
)
