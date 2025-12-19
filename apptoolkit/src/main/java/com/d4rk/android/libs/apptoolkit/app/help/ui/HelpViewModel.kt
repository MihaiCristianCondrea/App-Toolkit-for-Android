package com.d4rk.android.libs.apptoolkit.app.help.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpAction
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.app.help.ui.state.HelpUiState
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.copyData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.logging.FAQ_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class HelpViewModel(
    private val helpRepository: HelpRepository ,
    private val dispatchers: DispatcherProvider ,
) : ScreenViewModel<HelpUiState, HelpEvent, HelpAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.IsLoading(),
        data = HelpUiState()
    )
) {

    override fun onEvent(event: HelpEvent) {
        when (event) {
            HelpEvent.LoadFaq -> loadFaq()
            HelpEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun loadFaq() {
        helpRepository.fetchFaq()
                .flowOn(dispatchers.io)
                .onStart {
                    screenState.updateState(ScreenState.IsLoading())
                }
                .onEach { questions ->
                    val immutable = questions.toImmutableList()
                    when {
                        immutable.isEmpty() -> {
                            screenState.updateData(newState = ScreenState.NoData()) { current ->
                                current.copy(questions = immutable)
                            }
                        }

                        else -> {
                            screenState.updateData(newState = ScreenState.Success()) { current ->
                                current.copy(questions = immutable)
                            }
                        }
                    }
                }
                .catch { t ->
                    if (t is CancellationException) throw t

                    Log.w(FAQ_LOG_TAG , "Failed to load FAQs" , t)

                    screenState.updateState(ScreenState.Error())
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.DynamicString(t.message ?: "Failed to load FAQs"),
                            isError = true ,
                            timeStamp = System.currentTimeMillis() ,
                            type = ScreenMessageType.SNACKBAR ,
                        )
                    )
                }
                .launchIn(viewModelScope)
    }
}