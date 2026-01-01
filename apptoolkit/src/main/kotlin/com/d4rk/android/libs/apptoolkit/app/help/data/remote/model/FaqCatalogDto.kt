package com.d4rk.android.libs.apptoolkit.app.help.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
