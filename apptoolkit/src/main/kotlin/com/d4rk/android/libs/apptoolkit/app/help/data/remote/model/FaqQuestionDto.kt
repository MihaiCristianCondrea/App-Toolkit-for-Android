package com.d4rk.android.libs.apptoolkit.app.help.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FaqQuestionDto(
    val id: String,
    val icon: String? = null,
    val question: String,
    val answer: String,
    val featured: Boolean = false,
    val tags: List<String> = emptyList(),
)
