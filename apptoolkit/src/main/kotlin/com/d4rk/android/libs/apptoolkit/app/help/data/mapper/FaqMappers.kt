package com.d4rk.android.libs.apptoolkit.app.help.data.mapper

import com.d4rk.android.libs.apptoolkit.app.help.data.remote.model.FaqQuestionDto
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqId
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem

/**
 * Maps an [Iterable] of [FaqQuestionDto] to a [List] of [FaqItem] domain models.
 *
 * @return A list of FAQ items converted from DTOs.
 */
internal fun Iterable<FaqQuestionDto>.toFaqItems(): List<FaqItem> = map(FaqQuestionDto::toDomain)

/**
 * Maps a [FaqQuestionDto] data transfer object to a [FaqItem] domain model.
 *
 * @return A [FaqItem] containing the mapped ID, question, and answer.
 */
internal fun FaqQuestionDto.toDomain(): FaqItem = // TODO: move to domain/mapper
    FaqItem(
        id = FaqId(id),
        question = question,
        answer = answer,
    )