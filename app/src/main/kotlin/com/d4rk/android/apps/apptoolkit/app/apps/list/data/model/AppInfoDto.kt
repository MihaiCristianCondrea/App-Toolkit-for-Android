package com.d4rk.android.apps.apptoolkit.app.apps.list.data.model

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppCategory
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.utils.constants.PlayStoreUrls
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.sanitizeUrlOrNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


fun AppInfoDto.toDomain(): AppInfo = AppInfo(
    name = name,
    packageName = packageName,
    iconUrl = iconUrl.sanitizeUrlOrNull() ?: PlayStoreUrls.DEFAULT_ICON_URL,
    description = description ?: "",
    screenshots = screenshots
        ?.mapNotNull { screenshot ->
            val aspectRatio = screenshot.aspectRatio?.trim()
            val sanitizedUrl = screenshot.url?.sanitizeUrlOrNull()
            if (aspectRatio == "9:16") sanitizedUrl else null
        }
        ?: emptyList(),
    category = category?.toDomain(),
)

@Serializable
data class AppInfoDto(
    @SerialName("name") val name: String,
    @SerialName("packageName") val packageName: String,
    @SerialName("iconLogo") val iconUrl: String,
    @SerialName("category") val category: AppCategoryDto? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("screenshots") val screenshots: List<AppScreenshotDto>? = null
)

@Serializable
data class AppScreenshotDto(
    @SerialName("url") val url: String? = null,
    @SerialName("aspectRatio") val aspectRatio: String? = null,
)

@Serializable
data class AppCategoryDto(
    @SerialName("label") val label: String,
    @SerialName("category_id") val categoryId: String,
)

fun AppCategoryDto.toDomain(): AppCategory = AppCategory(
    label = label,
    id = categoryId,
)
