package com.d4rk.android.apps.apptoolkit.app.apps.list.data.mapper

import com.d4rk.android.apps.apptoolkit.app.apps.list.data.remote.model.AppCategoryDto
import com.d4rk.android.apps.apptoolkit.app.apps.list.data.remote.model.AppInfoDto
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppCategory
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.utils.constants.PlayStoreUrls
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.sanitizeUrlOrNull

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
