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

package com.d4rk.android.libs.apptoolkit.app.help.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data transfer object representing the FAQ catalog structure.
 *
 * @property schemaVersion The version of the catalog schema used for parsing.
 * @property products A list of available products and their associated FAQ sources.
 */
@Serializable
data class FaqCatalogDto(
    @SerialName("schemaVersion")
    val schemaVersion: Int,
    val products: List<FaqProductDto> = emptyList(),
)

@Serializable
data class FaqProductDto(
    val name: String,
    val productId: String,
    val key: String,
    @SerialName("questionSources")
    val questionSources: List<FaqQuestionSourceDto> = emptyList(),
)

@Serializable
data class FaqQuestionSourceDto(
    val url: String,
    val category: String,
)
