package com.d4rk.android.libs.apptoolkit.app.help.data.mapper

import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqQuestionDto
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem

internal fun List<FaqQuestionDto>.toFaqItems(): List<FaqItem> =
    mapIndexed { index, question ->
        FaqItem(
            id = index,
            question = question.question,
            answer = question.answer.trim(),
        )
    }.filter { faqItem -> faqItem.question.isNotBlank() && faqItem.answer.isNotBlank() }
