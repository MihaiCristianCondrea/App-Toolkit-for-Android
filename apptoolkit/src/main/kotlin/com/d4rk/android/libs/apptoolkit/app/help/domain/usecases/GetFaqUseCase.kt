package com.d4rk.android.libs.apptoolkit.app.help.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.FaqRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetFaqUseCase(
    private val repository: FaqRepository,
    private val firebaseController: FirebaseController,
) {

    operator fun invoke(): Flow<DataState<List<FaqItem>, Errors>> {
        return repository.fetchFaq()
            .onStart {
                firebaseController.logBreadcrumb(
                    message = "FAQ fetch started",
                    attributes = mapOf("source" to "GetFaqUseCase"),
                )
            }
            .map { result ->
                when (result) {
                    is DataState.Success -> {
                        val data = result.data
                            .asSequence()
                            .map { faqItem ->
                                faqItem.copy(
                                    question = faqItem.question.trim(),
                                    answer = faqItem.answer.trim()
                                )
                            }
                            .filter { it.question.isNotBlank() && it.answer.isNotBlank() }
                            .distinctBy { it.id.value }
                            .toList()

                        DataState.Success(data = data)
                    }

                    is DataState.Error -> DataState.Error(error = result.error)

                    is DataState.Loading -> DataState.Loading()
                }
            }
    }
}
