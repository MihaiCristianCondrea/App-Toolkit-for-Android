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
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.setNoData
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.asUiText
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for the FAQ/help screen.
 */
class HelpViewModel(
    private val getFaqUseCase: GetFaqUseCase,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<HelpUiState, HelpEvent, HelpAction>(
    initialState = UiStateScreen(data = HelpUiState()),
    firebaseController = firebaseController,
    screenName = "Help",
) {
    private var observeJob: Job? = null

    init {
        onEvent(event = HelpEvent.LoadFaq)
    }

    override fun handleEvent(event: HelpEvent) {
        when (event) {
            is HelpEvent.LoadFaq -> loadFaq()
            is HelpEvent.DismissSnackbar -> dismissSnackbar()
        }
    }

    private fun loadFaq() {
        startOperation(action = "loadFaq")
        observeJob = observeJob.restart {
            getFaqUseCase.invoke()
                .flowOn(context = dispatchers.io)
                .onStart {
                    updateStateThreadSafe {
                        screenState.setLoading()
                    }
                }
                .onEach { result: DataState<List<FaqItem>, Errors> ->
                    result
                        .onSuccess { faqs: List<FaqItem> ->
                            updateStateThreadSafe {
                                val data = HelpUiState(questions = faqs.toImmutableList())
                                if (faqs.isEmpty()) {
                                    screenState.setNoData(data = data)
                                } else {
                                    screenState.setSuccess(data = data)
                                }
                            }
                        }
                        .onFailure { error: Errors ->
                            updateStateThreadSafe {
                                screenState.setError(message = error.asUiText())
                            }
                        }
                }
                .catchReport(action = "loadFaq") {
                    updateStateThreadSafe {
                        screenState.setError(
                            message = UiTextHelper.StringResource(R.string.error_failed_to_load_faq)
                        )
                    }
                }
                .launchIn(scope = viewModelScope)
        }
    }

    private fun dismissSnackbar() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.dismissSnackbar()
            }
        }
    }
}
