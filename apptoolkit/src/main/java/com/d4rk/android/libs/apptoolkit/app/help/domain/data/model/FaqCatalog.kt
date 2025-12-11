package com.d4rk.android.libs.apptoolkit.app.help.domain.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FaqCatalog(
    @SerialName("schemaVersion") val schemaVersion: Int,
    val products: List<FaqProduct> = emptyList(),
)

@Serializable
internal data class FaqProduct(
    val name: String,
    val productId: String,
    val key: String,
    @SerialName("questionSources") val questionSources: List<FaqQuestionSource> = emptyList(),
)

@Serializable
internal data class FaqQuestionSource(
    val url: String,
    val category: String,
)

@Serializable
internal data class FaqQuestion(
    val id: String,
    val icon: String? = null,
    val question: String,
    val answer: String,
    val featured: Boolean = false,
    val tags: List<String> = emptyList(),
)
