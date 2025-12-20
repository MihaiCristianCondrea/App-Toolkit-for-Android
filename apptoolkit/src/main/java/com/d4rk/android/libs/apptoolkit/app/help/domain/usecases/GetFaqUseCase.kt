package com.d4rk.android.libs.apptoolkit.app.help.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.HelpRepository
import com.d4rk.android.libs.apptoolkit.app.help.ui.state.HelpUiState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFaqUseCase(
    private val repository: HelpRepository,
) {

    operator fun invoke(): Flow<DataState<UiStateScreen<HelpUiState>, Errors>> {
        return repository.fetchFaq()
            .map { result ->
                when (result) {
                    is DataState.Success -> {
                        val sanitized = result.data
                            .asSequence()
                            .map {
                                it.copy(
                                    question = it.question.trim(),
                                    answer = it.answer.trim()
                                )
                            }
                            .filter { it.question.isNotBlank() && it.answer.isNotBlank() }
                            .distinctBy { it.question }
                            .toList()
                            .toImmutableList()

                        val screenState =
                            if (sanitized.isEmpty()) ScreenState.NoData() else ScreenState.Success()

                        DataState.Success(
                            data = UiStateScreen(
                                screenState = screenState,
                                data = HelpUiState(questions = sanitized),
                            )
                        )
                    }

                    is DataState.Error -> DataState.Error(error = result.error)

                    is DataState.Loading -> DataState.Loading()
                }
            }
    }
}
