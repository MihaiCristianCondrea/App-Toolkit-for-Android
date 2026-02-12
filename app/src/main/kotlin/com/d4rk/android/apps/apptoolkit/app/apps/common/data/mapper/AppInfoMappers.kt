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
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppInfoDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppCategory
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.utils.constants.PlayStoreUrls
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.sanitizeUrlOrNull

fun AppInfoDto.toDomain(): AppInfo = AppInfo(
    name = name,
    packageName = packageName,
    iconUrl = iconUrl.sanitizeUrlOrNull() ?: PlayStoreUrls.DEFAULT_ICON_URL,
    description = description.orEmpty(),
    screenshots = screenshots
        ?.mapNotNull { s ->
            val ratio = s.aspectRatio?.trim()
            val url = s.url?.sanitizeUrlOrNull()
            if (ratio == "9:16") url else null
        }
        .orEmpty(),
    category = category?.toDomain(),
)

fun AppCategoryDto.toDomain(): AppCategory = AppCategory(
    label = label,
    id = categoryId,
)
