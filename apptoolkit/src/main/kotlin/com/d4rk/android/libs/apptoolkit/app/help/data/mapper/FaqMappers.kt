package com.d4rk.android.libs.apptoolkit.app.help.data.mapper

import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqQuestionDto
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqId
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem

internal fun Iterable<FaqQuestionDto>.toFaqItems(): List<FaqItem> =
        map(FaqQuestionDto::toDomain)

internal fun FaqQuestionDto.toDomain(): FaqItem =
        FaqItem(
            id = FaqId(id),
            question = question,
            answer = answer,
        )