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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.local.FaqLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqId
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.FaqRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqCatalogDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqProductDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqQuestionDto
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.models.FaqQuestionSourceDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.net.UnknownHostException

private const val CATALOG_URL = "https://example.test/catalog"
private const val PRODUCT_ID = "com.example.product"
private const val QUESTIONS_URL = "https://example.test/questions"

class DefaultFaqRepositoryTest {

    /**
     * @param remoteQuestions The question source's answer; `null` makes that source fail.
     * @param catalogFailure Makes the catalog itself fail with this, so nothing remote loads.
     */
    private fun repository(
        remoteQuestions: List<FaqQuestionDto>? = emptyList(),
        localQuestions: List<FaqItem> = emptyList(),
        catalogFailure: Throwable? = null,
    ): DefaultFaqRepository {
        val remote = mockk<FaqRemoteDataSource>()
        if (catalogFailure != null) {
            coEvery { remote.fetchCatalog(CATALOG_URL) } throws catalogFailure
        } else {
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
        }
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
        val questions = repository(
            remoteQuestions = listOf(dto("1", "  Why?  ", "\n Because. \t")),
        ).getFaq()

        assertThat(questions.single().question).isEqualTo("Why?")
        assertThat(questions.single().answer).isEqualTo("Because.")
    }

    @Test
    fun `drops entries with a blank question or answer`() = runTest {
        val questions = repository(
            remoteQuestions = listOf(
                dto("1", "Kept?", "Yes."),
                dto("2", "   ", "Orphan answer."),
                dto("3", "Orphan question?", "  "),
            ),
        ).getFaq()

        assertThat(questions.map { it.id.value }).containsExactly("1")
    }

    @Test
    fun `keeps the first entry when ids repeat`() = runTest {
        val questions = repository(
            remoteQuestions = listOf(
                dto("dup", "First question?", "First answer."),
                dto("dup", "Second question?", "Second answer."),
            ),
        ).getFaq()

        assertThat(questions).hasSize(1)
        assertThat(questions.single().question).isEqualTo("First question?")
    }

    @Test
    fun `falls back to the local questions when the remote catalog is all blanks`() = runTest {
        val questions = repository(
            remoteQuestions = listOf(dto("1", "   ", "   ")),
            localQuestions = listOf(item("local", "Bundled?", "Yes.")),
        ).getFaq()

        assertThat(questions.map { it.id.value }).containsExactly("local")
    }

    @Test
    fun `falls back to the local questions when a question source fails`() = runTest {
        val questions = repository(
            remoteQuestions = null,
            localQuestions = listOf(item("local", "Bundled?", "Yes.")),
        ).getFaq()

        assertThat(questions.single().question).isEqualTo("Bundled?")
    }

    @Test
    fun `falls back to the local questions when the catalog fails`() = runTest {
        val questions = repository(
            catalogFailure = UnknownHostException(),
            localQuestions = listOf(item("local", "Bundled?", "Yes.")),
        ).getFaq()

        assertThat(questions.single().question).isEqualTo("Bundled?")
    }

    @Test
    fun `normalizes the local questions too`() = runTest {
        val questions = repository(
            remoteQuestions = emptyList(),
            localQuestions = listOf(
                item("local", "  Bundled?  ", " Yes. "),
                item("blank", "  ", "  "),
            ),
        ).getFaq()

        assertThat(questions).hasSize(1)
        assertThat(questions.single().question).isEqualTo("Bundled?")
    }

    @Test
    fun `returns no questions when both sources load empty`() = runTest {
        val questions = repository(remoteQuestions = emptyList(), localQuestions = emptyList()).getFaq()

        assertThat(questions).isEmpty()
    }

    @Test
    fun `throws the catalog failure, translated, when there is nothing to fall back to`() = runTest {
        val failure = runCatching {
            repository(catalogFailure = UnknownHostException(), localQuestions = emptyList()).getFaq()
        }.exceptionOrNull()

        assertThat(failure).isInstanceOf(NetworkException::class.java)
        assertThat((failure as NetworkException).reason).isEqualTo(NetworkException.Reason.NO_INTERNET)
    }
}
