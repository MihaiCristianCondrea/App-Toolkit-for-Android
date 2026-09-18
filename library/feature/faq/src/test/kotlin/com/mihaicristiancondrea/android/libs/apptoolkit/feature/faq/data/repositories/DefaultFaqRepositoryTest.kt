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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.repositories

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.local.FaqLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.FaqRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqCatalogDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqProductDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqQuestionDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqQuestionSourceDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqId
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

private const val CATALOG_URL = "https://example.test/catalog"
private const val PRODUCT_ID = "com.example.product"
private const val QUESTIONS_URL = "https://example.test/questions"

class DefaultFaqRepositoryTest {

    private fun repository(
        remoteQuestions: List<FaqQuestionDto>? = emptyList(),
        localQuestions: List<FaqItem> = emptyList(),
    ): DefaultFaqRepository {
        val remote = mockk<FaqRemoteDataSource>()
        coEvery { remote.fetchCatalog(CATALOG_URL) } returns FaqCatalogDto(
            schemaVersion = 1,
            products = listOf(
                FaqProductDto(
                    name = "Product",
                    productId = PRODUCT_ID,
                    key = PRODUCT_ID,
                    questionSources = listOf(
                        FaqQuestionSourceDto(url = QUESTIONS_URL, category = "general"),
                    ),
                ),
            ),
        )
        if (remoteQuestions == null) {
            coEvery { remote.fetchQuestions(QUESTIONS_URL) } throws IllegalStateException("offline")
        } else {
            coEvery { remote.fetchQuestions(QUESTIONS_URL) } returns remoteQuestions
        }

        val local = mockk<FaqLocalDataSource>()
        every { local.loadLocalQuestions() } returns localQuestions

        return DefaultFaqRepository(
            localDataSource = local,
            remoteDataSource = remote,
            catalogUrl = CATALOG_URL,
            productId = PRODUCT_ID,
            firebaseController = mockk(relaxed = true),
        )
    }

    private fun dto(id: String, question: String, answer: String) =
        FaqQuestionDto(id = id, question = question, answer = answer)

    private fun item(id: String, question: String, answer: String) =
        FaqItem(id = FaqId(id), question = question, answer = answer)

    @Test
    fun `trims surrounding whitespace from remote questions and answers`() = runTest {
        val result = repository(
            remoteQuestions = listOf(dto("1", "  Why?  ", "\n Because. \t")),
        ).fetchFaq().first()

        val data = (result as DataState.Success).data
        assertThat(data.single().question).isEqualTo("Why?")
        assertThat(data.single().answer).isEqualTo("Because.")
    }

    @Test
    fun `drops entries with a blank question or answer`() = runTest {
        val result = repository(
            remoteQuestions = listOf(
                dto("1", "Kept?", "Yes."),
                dto("2", "   ", "Orphan answer."),
                dto("3", "Orphan question?", "  "),
            ),
        ).fetchFaq().first()

        assertThat((result as DataState.Success).data.map { it.id.value }).containsExactly("1")
    }

    @Test
    fun `keeps the first entry when ids repeat`() = runTest {
        val result = repository(
            remoteQuestions = listOf(
                dto("dup", "First question?", "First answer."),
                dto("dup", "Second question?", "Second answer."),
            ),
        ).fetchFaq().first()

        val data = (result as DataState.Success).data
        assertThat(data).hasSize(1)
        assertThat(data.single().question).isEqualTo("First question?")
    }

    @Test
    fun `falls back to the local questions when the remote catalog is all blanks`() = runTest {
        val result = repository(
            remoteQuestions = listOf(dto("1", "   ", "   ")),
            localQuestions = listOf(item("local", "Bundled?", "Yes.")),
        ).fetchFaq().first()

        val data = (result as DataState.Success).data
        assertThat(data.map { it.id.value }).containsExactly("local")
    }

    @Test
    fun `falls back to the local questions when the remote fetch throws`() = runTest {
        val result = repository(
            remoteQuestions = null,
            localQuestions = listOf(item("local", "Bundled?", "Yes.")),
        ).fetchFaq().first()

        assertThat((result as DataState.Success).data.single().question).isEqualTo("Bundled?")
    }

    @Test
    fun `normalizes the local questions too`() = runTest {
        val result = repository(
            remoteQuestions = emptyList(),
            localQuestions = listOf(
                item("local", "  Bundled?  ", " Yes. "),
                item("blank", "  ", "  "),
            ),
        ).fetchFaq().first()

        val data = (result as DataState.Success).data
        assertThat(data).hasSize(1)
        assertThat(data.single().question).isEqualTo("Bundled?")
    }

    @Test
    fun `reports an error when neither source yields anything` () = runTest {
        val result = repository(remoteQuestions = emptyList(), localQuestions = emptyList())
            .fetchFaq().first()

        assertThat(result).isInstanceOf(DataState.Error::class.java)
        assertThat((result as DataState.Error).error).isEqualTo(Errors.UseCase.FAILED_TO_LOAD_FAQ)
    }
}
