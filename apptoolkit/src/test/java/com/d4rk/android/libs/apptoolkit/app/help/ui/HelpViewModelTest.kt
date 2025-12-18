package com.d4rk.android.libs.apptoolkit.app.help.ui

import com.d4rk.android.libs.apptoolkit.app.help.domain.actions.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HelpViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `loadFaq sets NoData when repository returns empty`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = object : HelpRepository {
            override fun fetchFaq() = flowOf(emptyList<FaqItem>())
        }
        val viewModel = HelpViewModel(repo)

        viewModel.onEvent(HelpEvent.LoadFaq)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.screenState)
            .isInstanceOf(ScreenState.NoData::class.java)
    }

    @Test
    fun `loadFaq sets error state and shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = object : HelpRepository {
            override fun fetchFaq() = flow<List<FaqItem>> { throw IOException("boom") }
        }
        val viewModel = HelpViewModel(repo)

        viewModel.onEvent(HelpEvent.LoadFaq)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Error::class.java)
        assertThat(state.snackbar).isNotNull()
    }

    @Test
    fun `dismiss snackbar clears snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = object : HelpRepository {
            override fun fetchFaq() = flow<List<FaqItem>> { throw IOException("boom") }
        }
        val viewModel = HelpViewModel(repo)

        viewModel.onEvent(HelpEvent.LoadFaq)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNotNull()

        viewModel.onEvent(HelpEvent.DismissSnackbar)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNull()
    }

    @Test
    fun `additional emissions update questions`() = runTest(dispatcherExtension.testDispatcher) {
        val faqFlow = MutableSharedFlow<List<FaqItem>>()
        val repo = object : HelpRepository {
            override fun fetchFaq() = faqFlow
        }
        val viewModel = HelpViewModel(repo)

        viewModel.onEvent(HelpEvent.LoadFaq)
        faqFlow.emit(listOf(FaqItem(id = 0, question = "Q1", answer = "A1")))
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.data?.questions?.single()?.question)
            .isEqualTo("Q1")

        faqFlow.emit(listOf(FaqItem(id = 1, question = "Q2", answer = "A2")))
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.data?.questions?.single()?.question)
            .isEqualTo("Q2")
    }
}

