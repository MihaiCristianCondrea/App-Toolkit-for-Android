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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui

import android.app.Activity
import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqId
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.repositories.FaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts.FaqEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewOutcome
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.ForceInAppReviewUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class FaqViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val firebaseController = FakeFirebaseController()
    private val reviewUseCase: ForceInAppReviewUseCase = mockk()
    private val reviewHost = object : ReviewHost {
        override val activity: Activity = mockk()
    }

    // Already normalized: trimming, blank-dropping and de-duplication are the repository's job,
    // covered by DefaultFaqRepositoryTest.
    private val question = FaqItem(id = FaqId("remote-1"), question = "Q", answer = "A")

    private fun createViewModel(repository: FaqRepository = FakeFaqRepository()): FaqViewModel =
        FaqViewModel(
            faqRepository = repository,
            forceInAppReviewUseCase = reviewUseCase,
            firebaseController = firebaseController,
        )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private val Loadable.Failed.resourceId: Int
        get() = (message as UiTextHelper.StringResource).resourceId

    @Test
    fun `the first load shows the questions`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeFaqRepository(questions = listOf(question)))
        advance()

        val questions = viewModel.state.value.questions as Loadable.Ready
        assertThat(questions.value).containsExactly(question)
    }

    @Test
    fun `no questions shows the empty state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(FakeFaqRepository(questions = emptyList()))
        advance()

        assertThat(viewModel.state.value.questions).isInstanceOf(Loadable.Empty::class.java)
    }

    @Test
    fun `a failure with no text of its own shows the help page's text with a retry`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(FakeFaqRepository(failure = IllegalStateException("bug")))
            advance()

            val questions = viewModel.state.value.questions as Loadable.Failed
            assertThat(questions.resourceId).isEqualTo(R.string.error_failed_to_load_faq)
            assertThat(questions.retryable).isTrue()
            assertThat(firebaseController.loggedEvents.map { it.name }).contains("vm_op_error")
        }

    @Test
    fun `being offline shows the offline text`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            FakeFaqRepository(failure = NetworkException(NetworkException.Reason.NO_INTERNET))
        )
        advance()

        val questions = viewModel.state.value.questions as Loadable.Failed
        assertThat(questions.resourceId).isEqualTo(CoreUiR.string.screen_error_no_internet)
    }

    @Test
    fun `retrying after a failure shows the questions`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeFaqRepository(questions = listOf(question), failure = IllegalStateException("bug"))
        val viewModel = createViewModel(repository)
        advance()
        assertThat(viewModel.state.value.questions).isInstanceOf(Loadable.Failed::class.java)

        repository.failure = null
        viewModel.onEvent(FaqEvent.Load)
        advance()

        assertThat(viewModel.state.value.questions).isInstanceOf(Loadable.Ready::class.java)
    }

    @Test
    fun `a shown review asks for nothing else`() = runTest(dispatcherExtension.testDispatcher) {
        coEvery { reviewUseCase(host = any()) } returns ReviewOutcome.Launched
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(FaqEvent.RequestReview(host = reviewHost))
        advance()

        assertThat(viewModel.state.value.openStoreListing).isFalse()
    }

    @Test
    fun `an unavailable review asks for the store listing once`() = runTest(dispatcherExtension.testDispatcher) {
        coEvery { reviewUseCase(host = any()) } returns ReviewOutcome.Unavailable
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(FaqEvent.RequestReview(host = reviewHost))
        advance()
        assertThat(viewModel.state.value.openStoreListing).isTrue()

        viewModel.onEvent(FaqEvent.StoreListingOpened)
        assertThat(viewModel.state.value.openStoreListing).isFalse()
    }

    @Test
    fun `a failed review request still asks for the store listing`() =
        runTest(dispatcherExtension.testDispatcher) {
            coEvery { reviewUseCase(host = any()) } throws IllegalStateException("review")
            val viewModel = createViewModel()
            advance()

            viewModel.onEvent(FaqEvent.RequestReview(host = reviewHost))
            advance()

            assertThat(viewModel.state.value.openStoreListing).isTrue()
            assertThat(firebaseController.loggedEvents.map { it.name }).contains("vm_op_error")
        }

    private class FakeFaqRepository(
        private val questions: List<FaqItem> = emptyList(),
        var failure: Throwable? = null,
    ) : FaqRepository {
        override suspend fun getFaq(): List<FaqItem> {
            failure?.let { throw it }
            return questions
        }
    }
}
