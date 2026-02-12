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

package com.d4rk.android.libs.apptoolkit.app.help.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.FaqRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFaqUseCase(
    private val repository: FaqRepository,
) {

    operator fun invoke(): Flow<DataState<List<FaqItem>, Errors>> {
        return repository.fetchFaq()
            .map { result ->
                when (result) {
                    is DataState.Success -> {
                        val data = result.data
                            .asSequence()
                            .map { faqItem ->
                                faqItem.copy(
                                    question = faqItem.question.trim(),
                                    answer = faqItem.answer.trim()
                                )
                            }
                            .filter { it.question.isNotBlank() && it.answer.isNotBlank() }
                            .distinctBy { it.id.value }
                            .toList()

                        DataState.Success(data = data)
                    }

                    is DataState.Error -> DataState.Error(error = result.error)

                    is DataState.Loading -> DataState.Loading()
                }
            }
    }
}
