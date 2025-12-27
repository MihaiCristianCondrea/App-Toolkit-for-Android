package com.d4rk.android.apps.apptoolkit.app.apps.list.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
