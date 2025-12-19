package com.d4rk.android.libs.apptoolkit.app.help.domain.model

import androidx.compose.runtime.Immutable

/** Simple representation of a FAQ item */
@Immutable
data class FaqItem(
    val id: Int,
    val question: String,
    val answer: String,
)
