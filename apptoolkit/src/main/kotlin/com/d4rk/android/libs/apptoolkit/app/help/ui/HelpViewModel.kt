package com.d4rk.android.libs.apptoolkit.app.help.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.usecases.GetFaqUseCase
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpAction
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.ui.state.HelpUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class HelpViewModel(
    private val getFaqUseCase: GetFaqUseCase,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<HelpUiState, HelpEvent, HelpAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = HelpUiState()
    )
) {

    init {
        onEvent(event = HelpEvent.LoadFaq)
    }

    override fun onEvent(event: HelpEvent) {
        when (event) {
            HelpEvent.LoadFaq -> loadFaq()
            HelpEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun loadFaq() {
        generalJob?.cancel()
        generalJob = getFaqUseCase()
            .flowOn(context = dispatchers.io)
            .onStart { screenState.setLoading() }
            .onEach { result: DataState<List<FaqItem>, Errors> ->
                updateStateThreadSafe {
                    result
                        .onSuccess { faqs: List<FaqItem> ->
                            val screenStateForData: ScreenState =
                                if (faqs.isEmpty()) ScreenState.NoData() else ScreenState.Success()
                            screenState.update { current ->
                                current.copy(
                                    screenState = screenStateForData,
                                    data = HelpUiState(questions = faqs.toImmutableList())
                                )
                            }
                        }
                        .onFailure { error: Errors ->
                            screenState.updateState(newValues = ScreenState.Error())
                            screenState.showSnackbar(
                                UiSnackbar(
                                    message = error.asUiText(),
                                    isError = true,
                                    timeStamp = System.currentTimeMillis(),
                                    type = ScreenMessageType.SNACKBAR
                                )
                            )
                        }
                }
            }
            .catch {
                if (it is CancellationException) throw it
                firebaseController.reportViewModelError(
                    viewModelName = "HelpViewModel",
                    action = "loadFaq",
                    throwable = it,
                )
                updateStateThreadSafe {
                    screenState.updateState(newValues = ScreenState.Error())
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.error_failed_to_load_faq),
                            isError = true,
                            timeStamp = System.currentTimeMillis(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
            }
            .launchIn(scope = viewModelScope)
    }
}
