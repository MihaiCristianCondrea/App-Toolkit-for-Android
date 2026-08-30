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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.mappers

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppCategoryDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppDetailsDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppLatestVersionDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppLinkDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppScreenshotDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppSummaryDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDeviceType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppLatestVersion
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppLink
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppScreenshot
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.string.sanitizeUrlOrNull

private const val DEFAULT_ICON_URL = "https://c.clc2l.com/t/g/o/google-playstore-Iauj7q.png"

fun AppSummaryDto.toDomain(): AppSummary = AppSummary(
    name = name,
    packageName = packageName,
    iconUrl = iconUrl.sanitizeUrlOrNull() ?: DEFAULT_ICON_URL,
    shortDescription = shortDescription.orEmpty(),
    category = category?.toDomain(),
)

fun AppDetailsDto.toDomain(): AppDetails = AppDetails(
    name = name,
    packageName = packageName,
    iconUrl = iconUrl.sanitizeUrlOrNull() ?: DEFAULT_ICON_URL,
    description = description,
    shortDescription = shortDescription.orEmpty(),
    category = category?.toDomain(),
    screenshots = screenshots.mapNotNull(AppScreenshotDto::toDomain),
    links = links.mapNotNull(AppLinkDto::toDomain),
    latestVersion = latestVersion?.toDomain(),
)

private fun AppCategoryDto.toDomain(): AppCategory = AppCategory(label = label, id = categoryId)

private fun AppScreenshotDto.toDomain(): AppScreenshot? = AppScreenshot(
    url = url.sanitizeUrlOrNull() ?: return null,
    aspectRatio = aspectRatio,
    deviceType = when (deviceType.lowercase()) {
        "phone" -> AppDeviceType.Phone
        "tablet" -> AppDeviceType.Tablet
        "wear" -> AppDeviceType.Wear
        "desktop" -> AppDeviceType.Desktop
        else -> AppDeviceType.Unknown
    },
)

private fun AppLinkDto.toDomain(): AppLink? = AppLink(
    label = label.trim().takeIf(String::isNotEmpty) ?: return null,
    url = url.sanitizeUrlOrNull() ?: return null,
)

private fun AppLatestVersionDto.toDomain(): AppLatestVersion = AppLatestVersion(
    versionName = versionName,
    versionCode = versionCode,
    releasedAt = releasedAt,
    summary = summary,
)
