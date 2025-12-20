package com.d4rk.android.libs.apptoolkit.core.ui.base

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Test

class ScreenViewModelTest {

    @Test
    fun `screenState exposes mutable state flow updates`() {
        val initial = UiStateScreen(
            screenState = ScreenState.IsLoading(),
            data = TestData(value = "initial"),
        )
        val viewModel = TestScreenViewModel(initial)

        val stateFlow = viewModel.exposedState()
        val updated = UiStateScreen(
            screenState = ScreenState.Success(),
            data = TestData(value = "updated"),
        )

        stateFlow.value = updated

        assertThat(viewModel.uiState.value).isEqualTo(updated)
    }

    @Test
    fun `screenData returns current state data`() {
        val initial = UiStateScreen(
            screenState = ScreenState.Success(),
            data = TestData(value = "initial"),
        )
        val viewModel = TestScreenViewModel(initial)

        assertThat(viewModel.exposedData()).isEqualTo(TestData(value = "initial"))

        val updated = UiStateScreen(
            screenState = ScreenState.Success(),
            data = TestData(value = "updated"),
        )
        viewModel.overwriteState(updated)

        assertThat(viewModel.exposedData()).isEqualTo(TestData(value = "updated"))
    }
}

private data class TestData(val value: String)

private class TestScreenViewModel(initial: UiStateScreen<TestData>) :
    ScreenViewModel<TestData, UiEvent, ActionEvent>(initial) {

    fun exposedState(): MutableStateFlow<UiStateScreen<TestData>> = screenState

    fun overwriteState(newState: UiStateScreen<TestData>) {
        screenState.value = newState
    }

    fun exposedData(): TestData? = screenData

    override fun onEvent(event: UiEvent) = Unit
}
