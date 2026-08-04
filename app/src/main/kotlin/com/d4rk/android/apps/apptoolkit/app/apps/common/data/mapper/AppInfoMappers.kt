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

package com.d4rk.android.apps.apptoolkit.app.apps.common.data.mapper

import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppCategoryDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppDetailsDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppLatestVersionDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppLinkDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppScreenshotDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppSummaryDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppCategory
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppDetails
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppDeviceType
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppLatestVersion
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppLink
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppScreenshot
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppSummary
import com.d4rk.android.apps.apptoolkit.app.apps.list.utils.constants.PlayStoreUrls
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.sanitizeUrlOrNull

fun AppSummaryDto.toDomain(): AppSummary = AppSummary(
    name = name,
    packageName = packageName,
    iconUrl = iconUrl.sanitizeUrlOrNull() ?: PlayStoreUrls.DEFAULT_ICON_URL,
    shortDescription = shortDescription.orEmpty(),
    category = category?.toDomain(),
)

fun AppDetailsDto.toDomain(): AppDetails = AppDetails(
    name = name,
    packageName = packageName,
    iconUrl = iconUrl.sanitizeUrlOrNull() ?: PlayStoreUrls.DEFAULT_ICON_URL,
    description = description,
    shortDescription = shortDescription.orEmpty(),
    category = category?.toDomain(),
    screenshots = screenshots.mapNotNull(AppScreenshotDto::toDomain),
    links = links.mapNotNull(AppLinkDto::toDomain),
    latestVersion = latestVersion?.toDomain(),
)

fun AppCategoryDto.toDomain(): AppCategory = AppCategory(
    label = label,
    id = categoryId,
)

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
