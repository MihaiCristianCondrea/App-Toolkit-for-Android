package com.d4rk.android.libs.apptoolkit.app.help.domain.data.model

import androidx.compose.runtime.Immutable

/** Simple representation of a FAQ item */
@Immutable
data class UiHelpQuestion(
    val id: Int,
    val question: String,
    val answer: String,
)
