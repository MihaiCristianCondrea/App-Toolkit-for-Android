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
