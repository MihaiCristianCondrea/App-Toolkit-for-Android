package com.d4rk.android.libs.apptoolkit.app.help.ui

import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqId
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.FaqRepository
import com.d4rk.android.libs.apptoolkit.app.help.domain.usecases.GetFaqUseCase
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.review.domain.usecases.ForceInAppReviewUseCase
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import io.mockk.mockk

@OptIn(ExperimentalCoroutinesApi::class)
class HelpViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val firebaseController = FakeFirebaseController()
    private lateinit var reviewUseCase: ForceInAppReviewUseCase

    @BeforeEach
    fun setup() {
        reviewUseCase = mockk(relaxed = true)
    }

    @Test
    fun `loadFaq sets success state when repository returns data`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = object : FaqRepository {
                override fun fetchFaq(): Flow<DataState<List<FaqItem>, Errors>> =
                    flowOf(
                        DataState.Success(
                            data = listOf(
                                FaqItem(
                                    id = FaqId("remote-1"),
                                    question = " Q ",
                                    answer = " A "
                                )
                            )
                        )
                    )
            }
            val viewModel = HelpViewModel(
                getFaqUseCase = GetFaqUseCase(repository, firebaseController),
                forceInAppReviewUseCase = reviewUseCase,
                dispatchers = testDispatcherProvider(),
                firebaseController = firebaseController,
            )

            viewModel.onEvent(HelpEvent.LoadFaq)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state.screenState is ScreenState.Success)
            assertEquals(1, state.data?.questions?.size)
            assertEquals("Q", state.data?.questions?.first()?.question)
            assertEquals("A", state.data?.questions?.first()?.answer)
        }

    @Test
    fun `loadFaq sets error state when repository throws`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = object : FaqRepository {
                override fun fetchFaq(): Flow<DataState<List<FaqItem>, Errors>> = flow {
                    emit(DataState.Loading())
                    throw IllegalStateException("error")
                }
            }
            val viewModel = HelpViewModel(
                getFaqUseCase = GetFaqUseCase(repository, firebaseController),
                forceInAppReviewUseCase = reviewUseCase,
                dispatchers = testDispatcherProvider(),
                firebaseController = firebaseController,
            )

            viewModel.onEvent(HelpEvent.LoadFaq)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.screenState is ScreenState.Error)
        }

    private fun testDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override val main = dispatcherExtension.testDispatcher
        override val io = dispatcherExtension.testDispatcher
        override val default = dispatcherExtension.testDispatcher
        override val unconfined = dispatcherExtension.testDispatcher
    }
}
