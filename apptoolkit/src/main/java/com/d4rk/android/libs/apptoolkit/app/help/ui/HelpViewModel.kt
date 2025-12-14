package com.d4rk.android.libs.apptoolkit.app.help.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.app.help.domain.actions.HelpAction
import com.d4rk.android.libs.apptoolkit.app.help.domain.actions.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.ui.UiHelpScreen
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.copyData
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.updateState
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class HelpViewModel(
    private val helpRepository: HelpRepository,
) : ScreenViewModel<UiHelpScreen, HelpEvent, HelpAction>(
    initialState = UiStateScreen(screenState = ScreenState.IsLoading(), data = UiHelpScreen())
) {

    override fun onEvent(event: HelpEvent) {
        when (event) {
            HelpEvent.LoadFaq -> loadFaq()
            HelpEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun loadFaq() {
        var latestQuestions = UiHelpScreen().questions

        helpRepository.fetchFaq()
            .onStart { screenState.updateState(ScreenState.IsLoading()) }
            .onEach { questions ->
                val immutableQuestions = questions.toImmutableList()
                latestQuestions = immutableQuestions
                screenState.copyData { copy(questions = immutableQuestions) }
            }
            .onCompletion { cause ->
                when {
                    cause is CancellationException -> return@onCompletion
                    cause != null -> screenState.updateState(ScreenState.Error())
                    latestQuestions.isEmpty() -> screenState.updateState(ScreenState.NoData())
                    else -> screenState.updateState(ScreenState.Success())
                }
            }
            .catch { error ->
                if (error is CancellationException) throw error
                screenState.showSnackbar(
                    UiSnackbar(
                        message = UiTextHelper.DynamicString(
                            error.message ?: "Failed to load FAQs"
                        ),
                        type = ScreenMessageType.SNACKBAR,
                        isError = true,
                        timeStamp = System.currentTimeMillis(),
                    )
                )
            }
            .launchIn(viewModelScope)
    }
}

