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

package com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model

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
