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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models

import androidx.compose.runtime.Immutable

/**
 * Compact catalog metadata for the app grid. Description, screenshots, and links are loaded
 * separately when details are expanded.
 */
data class AppSummary(
    val name: String,
    val packageName: String,
    val iconUrl: String,
    val shortDescription: String = "",
    val category: AppCategory? = null,
)

/** Source-compatible name retained for existing list/widget integrations. */
typealias AppInfo = AppSummary

/**
 * Package details loaded when an app is expanded. Lists are produced once by DTO mapping and
 * must remain immutable to satisfy the Compose [Immutable] contract.
 */
@Immutable
data class AppDetails(
    val name: String,
    val packageName: String,
    val iconUrl: String,
    val description: String,
    val shortDescription: String = "",
    val category: AppCategory? = null,
    val screenshots: List<AppScreenshot> = emptyList(),
    val links: List<AppLink> = emptyList(),
    val latestVersion: AppLatestVersion? = null,
)

/** Category metadata provided by the remote developer-app catalog. */
data class AppCategory(
    val label: String,
    val id: String,
)

/** Screenshot metadata used to preserve the API-provided form factor and aspect ratio. */
data class AppScreenshot(
    val url: String,
    val aspectRatio: String,
    val deviceType: AppDeviceType,
)

/** Form factor associated with an API screenshot. */
enum class AppDeviceType {
    Phone,
    Tablet,
    Wear,
    Desktop,
    Unknown,
}

/** Ordered external link provided by the application metadata document. */
data class AppLink(
    val label: String,
    val url: String,
)

/** Optional latest legacy release metadata returned with full application details. */
data class AppLatestVersion(
    val versionName: String,
    val versionCode: Long,
    val releasedAt: String? = null,
    val summary: String? = null,
)
