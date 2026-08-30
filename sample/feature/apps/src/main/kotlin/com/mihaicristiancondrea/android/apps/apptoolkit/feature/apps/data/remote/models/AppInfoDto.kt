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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class AppSummaryDto(
    @SerialName("name") val name: String,
    @SerialName("package_name") val packageName: String,
    @SerialName("icon_logo") val iconUrl: String,
    @SerialName("short_description") val shortDescription: String? = null,
    @SerialName("category") val category: AppCategoryDto? = null,
)

@Serializable
data class AppDetailsDto(
    @SerialName("name") val name: String,
    @SerialName("package_name") val packageName: String,
    @SerialName("icon_logo") val iconUrl: String,
    @SerialName("description") val description: String,
    @SerialName("short_description") val shortDescription: String? = null,
    @SerialName("category") val category: AppCategoryDto? = null,
    @SerialName("screenshots") val screenshots: List<AppScreenshotDto> = emptyList(),
    @SerialName("links") val links: List<AppLinkDto> = emptyList(),
    @SerialName("latest_version") val latestVersion: AppLatestVersionDto? = null,
)

@Serializable
data class AppScreenshotDto(
    @SerialName("url") val url: String,
    @SerialName("aspect_ratio") val aspectRatio: String,
    @SerialName("device_type") val deviceType: String,
)

@Serializable
data class AppLinkDto(
    @SerialName("label") val label: String,
    @SerialName("url") val url: String,
)

@Serializable
data class AppLatestVersionDto(
    @SerialName("version_name") val versionName: String,
    @SerialName("version_code") val versionCode: Long,
    @SerialName("released_at") val releasedAt: String? = null,
    @SerialName("summary") val summary: String? = null,
)

@Serializable
data class AppCategoryDto(
    @SerialName("label") val label: String,
    @SerialName("category_id") val categoryId: String,
)
