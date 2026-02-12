/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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