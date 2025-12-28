package com.d4rk.android.libs.apptoolkit.app.help.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.help.domain.usecases.GetFaqUseCase
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpAction
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.ui.state.HelpUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class HelpViewModel(
    private val getFaqUseCase: GetFaqUseCase,
    private val dispatchers: DispatcherProvider,
) : ScreenViewModel<HelpUiState, HelpEvent, HelpAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = HelpUiState()
    )
) {

    private var loadFaqJob: Job? = null

    init {
        onEvent(HelpEvent.LoadFaq)
    }

    override fun onEvent(event: HelpEvent) {
        when (event) {
            HelpEvent.LoadFaq -> loadFaq()
            HelpEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun loadFaq() {
        loadFaqJob?.cancel()
        loadFaqJob = getFaqUseCase()
            .flowOn(dispatchers.io)
            .onStart { screenState.updateState(ScreenState.IsLoading()) }
            .onEach { result ->
                when (result) {
                    is DataState.Loading -> screenState.updateState(ScreenState.IsLoading())

                    is DataState.Success -> {
                        val payload = result.data
                        val screenStateForData =
                            if (payload.isEmpty()) ScreenState.NoData() else ScreenState.Success()
                        screenState.update { current ->
                            current.copy(
                                screenState = screenStateForData,
                                data = HelpUiState(questions = payload.toImmutableList())
                            )
                        }
                    }

                    is DataState.Error -> {
                        screenState.updateState(ScreenState.Error())
                        screenState.showSnackbar(
                            UiSnackbar(
                                message = UiTextHelper.DynamicString(result.error.toString()),
                                isError = true,
                                timeStamp = System.currentTimeMillis(),
                                type = ScreenMessageType.SNACKBAR,
                            )
                        )
                    }
                }
            }
            .catch { t ->
                screenState.updateState(ScreenState.Error())
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.DynamicString(t.message ?: "Failed to load FAQs"),
                        isError = true,
                        timeStamp = System.currentTimeMillis(),
                        type = ScreenMessageType.SNACKBAR,
                    )
                )
            }
            .launchIn(viewModelScope)
    }

}
