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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.domain.usecases

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.repositories.FaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.domain.models.FaqId
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.domain.models.FaqItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetFaqUseCaseTest {

    private fun useCase(result: DataState<List<FaqItem>, Errors>): GetFaqUseCase =
        GetFaqUseCase(
            repository = object : FaqRepository {
                override fun fetchFaq(): Flow<DataState<List<FaqItem>, Errors>> = flowOf(result)
            }
        )

    private fun item(id: String, question: String, answer: String) =
        FaqItem(id = FaqId(id), question = question, answer = answer)

    @Test
    fun `trims surrounding whitespace from questions and answers`() = runTest {
        val result = useCase(
            DataState.Success(listOf(item("1", "  Why?  ", "\n Because. \t")))
        ).invoke().first()

        val data = (result as DataState.Success).data
        assertThat(data.single().question).isEqualTo("Why?")
        assertThat(data.single().answer).isEqualTo("Because.")
    }

    @Test
    fun `drops entries with a blank question or answer`() = runTest {
        val result = useCase(
            DataState.Success(
                listOf(
                    item("1", "Kept?", "Yes."),
                    item("2", "   ", "Orphan answer."),
                    item("3", "Orphan question?", "  "),
                )
            )
        ).invoke().first()

        assertThat((result as DataState.Success).data.map { it.id.value }).containsExactly("1")
    }

    @Test
    fun `keeps the first entry when ids repeat`() = runTest {
        val result = useCase(
            DataState.Success(
                listOf(
                    item("dup", "First question?", "First answer."),
                    item("dup", "Second question?", "Second answer."),
                )
            )
        ).invoke().first()

        val data = (result as DataState.Success).data
        assertThat(data).hasSize(1)
        assertThat(data.single().question).isEqualTo("First question?")
    }

    @Test
    fun `passes an error through untouched`() = runTest {
        val result = useCase(DataState.Error(error = Errors.Network.UNKNOWN)).invoke().first()

        assertThat(result).isInstanceOf(DataState.Error::class.java)
        assertThat((result as DataState.Error).error).isEqualTo(Errors.Network.UNKNOWN)
    }
}
