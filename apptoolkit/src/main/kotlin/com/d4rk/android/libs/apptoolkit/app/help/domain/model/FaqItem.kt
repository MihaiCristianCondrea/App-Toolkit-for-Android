package com.d4rk.android.libs.apptoolkit.app.help.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class FaqId(val value: String)

/** Simple representation of a FAQ item */
data class FaqItem(
    val id: FaqId,
    val question: String,
    val answer: String,
)
