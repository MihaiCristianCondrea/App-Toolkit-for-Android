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
import kotlin.test.assertEquals
import kotlin.test.assertIs

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
            coEvery { remote.fetchCatalog(url = CATALOG_URL) } throws catalogFailure
        } else {
            coEvery { remote.fetchCatalog(url = CATALOG_URL) } returns FaqCatalogDto(
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
            coEvery { remote.fetchQuestions(url = QUESTIONS_URL) } throws IllegalStateException("offline")
        } else {
            coEvery { remote.fetchQuestions(url = QUESTIONS_URL) } returns remoteQuestions
        }

        val local = mockk<FaqLocalDataSource>()
        every { local.loadLocalQuestions() } returns localQuestions

        return DefaultFaqRepository(
            localDataSource = local,
            remoteDataSource = remote,
            catalogUrl = CATALOG_URL,
            productId = PRODUCT_ID,
            diagnostics = mockk(relaxed = true),
        )
    }

    private fun dto(id: String, question: String, answer: String) =
        FaqQuestionDto(id = id, question = question, answer = answer)

    private fun item(id: String, question: String, answer: String) =
        FaqItem(id = FaqId(value = id), question = question, answer = answer)

    @Test
    fun `trims surrounding whitespace from remote questions and answers`() = runTest {
        val questions = repository(
            remoteQuestions = listOf(dto(id = "1", question = "  Why?  ", answer = "\n Because. \t")),
        ).getFaq()

        assertEquals(expected = "Why?", actual = questions.single().question)
        assertEquals(expected = "Because.", actual = questions.single().answer)
    }

    @Test
    fun `drops entries with a blank question or answer`() = runTest {
        val questions = repository(
            remoteQuestions = listOf(
                dto(id = "1", question = "Kept?", answer = "Yes."),
                dto(id = "2", question = "   ", answer = "Orphan answer."),
                dto(id = "3", question = "Orphan question?", answer = "  "),
            ),
        ).getFaq()

        assertEquals(expected = listOf(element = "1"), actual = questions.map { it.id.value })
    }

    @Test
    fun `keeps the first entry when ids repeat`() = runTest {
        val questions = repository(
            remoteQuestions = listOf(
                dto(id = "dup", question = "First question?", answer = "First answer."),
                dto(id = "dup", question = "Second question?", answer = "Second answer."),
            ),
        ).getFaq()

        assertEquals(expected = 1, actual = questions.size)
        assertEquals(expected = "First question?", actual = questions.single().question)
    }

    @Test
    fun `falls back to the local questions when the remote catalog is all blanks`() = runTest {
        val questions = repository(
            remoteQuestions = listOf(dto(id = "1", question = "   ", answer = "   ")),
            localQuestions = listOf(item(id = "local", question = "Bundled?", answer = "Yes.")),
        ).getFaq()

        assertEquals(expected = listOf(element = "local"), actual = questions.map { it.id.value })
    }

    @Test
    fun `falls back to the local questions when a question source fails`() = runTest {
        val questions = repository(
            remoteQuestions = null,
            localQuestions = listOf(item(id = "local", question = "Bundled?", answer = "Yes.")),
        ).getFaq()

        assertEquals(expected = "Bundled?", actual = questions.single().question)
    }

    @Test
    fun `falls back to the local questions when the catalog fails`() = runTest {
        val questions = repository(
            catalogFailure = UnknownHostException(),
            localQuestions = listOf(item(id = "local", question = "Bundled?", answer = "Yes.")),
        ).getFaq()

        assertEquals(expected = "Bundled?", actual = questions.single().question)
    }

    @Test
    fun `normalizes the local questions too`() = runTest {
        val questions = repository(
            remoteQuestions = emptyList(),
            localQuestions = listOf(
                item(id = "local", question = "  Bundled?  ", answer = " Yes. "),
                item(id = "blank", question = "  ", answer = "  "),
            ),
        ).getFaq()

        assertEquals(expected = 1, actual = questions.size)
        assertEquals(expected = "Bundled?", actual = questions.single().question)
    }

    @Test
    fun `returns no questions when both sources load empty`() = runTest {
        val questions = repository(remoteQuestions = emptyList(), localQuestions = emptyList()).getFaq()

        assertEquals(expected = emptyList(), actual = questions)
    }

    @Test
    fun `throws the catalog failure, translated, when there is nothing to fall back to`() = runTest {
        val failure = runCatching {
            repository(catalogFailure = UnknownHostException(), localQuestions = emptyList()).getFaq()
        }.exceptionOrNull()

        assertIs<NetworkException>(value = failure)
        assertEquals(expected = NetworkException.Reason.NO_INTERNET, actual = failure.reason)
    }
}
