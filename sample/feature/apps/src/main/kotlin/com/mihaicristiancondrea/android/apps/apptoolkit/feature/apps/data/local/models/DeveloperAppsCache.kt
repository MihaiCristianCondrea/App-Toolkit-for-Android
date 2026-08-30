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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.local.models

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
