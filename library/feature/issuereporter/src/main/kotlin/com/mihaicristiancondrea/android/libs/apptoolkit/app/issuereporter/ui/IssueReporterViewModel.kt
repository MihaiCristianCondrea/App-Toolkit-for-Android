/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.github.ExtraInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.repositories.IssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.mappers.asDataState
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.models.IssueReporterError
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.mappers.toPlainText
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.usecases.SendIssueReportUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.contracts.IssueReporterAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.GithubToken
import com.mihaicristiancondrea.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.copyData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.showSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for composing and sending issue reports.
 */
class IssueReporterViewModel(
    private val sendIssueReport: SendIssueReportUseCase,
    private val githubTarget: GithubTarget,
    @param:GithubToken private val githubToken: String,
    private val repository: IssueReporterRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<IssueReporterUiState, IssueReporterEvent, IssueReporterAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = IssueReporterUiState(),
    ),
    firebaseController = firebaseController,
    screenName = "IssueReporter",
) {

    private var sendJob: Job? = null

    override fun handleEvent(event: IssueReporterEvent) {
        when (event) {
            is IssueReporterEvent.UpdateTitle -> updateTitle(event.value)
            is IssueReporterEvent.UpdateDescription -> updateDescription(event.value)
            is IssueReporterEvent.UpdateEmail -> updateEmail(event.value)
            is IssueReporterEvent.SetAnonymous -> updateAnonymous(event.anonymous)
            is IssueReporterEvent.RequestDeviceInfo -> loadDeviceInfoIfNeeded()
            is IssueReporterEvent.Send -> sendReport()
            is IssueReporterEvent.DismissSnackbar -> dismissSnackbar()
        }
    }

    private fun updateTitle(value: String) {
        updateForm { copy(title = value) }
    }

    private fun updateDescription(value: String) {
        updateForm { copy(description = value) }
    }

    private fun updateEmail(value: String) {
        updateForm { copy(email = value) }
    }

    private fun updateAnonymous(anonymous: Boolean) {
        updateForm { copy(anonymous = anonymous) }
    }

    private fun dismissSnackbar() {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.dismissSnackbar()
            }
        }
    }

    private fun updateForm(transform: IssueReporterUiState.() -> IssueReporterUiState) {
        viewModelScope.launch {
            updateStateThreadSafe {
                screenState.copyData { transform() }
            }
        }
    }

    private fun sendReport() {
        val data = screenData ?: return

        if (sendJob?.isActive == true) return

        if (data.title.isBlank() || data.description.isBlank()) {
            viewModelScope.launch {
                updateStateThreadSafe {
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.error_invalid_report),
                            timeStamp = System.nanoTime(),
                            isError = true,
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
            }
            return
        }

        sendJob = sendJob.restart {
            launchReport(
                action = Actions.SEND_REPORT,
                extra = mapOf(
                    ExtraKeys.HAS_TITLE to data.title.isNotBlank().toString(),
                    ExtraKeys.HAS_DESCRIPTION to data.description.isNotBlank().toString(),
                    ExtraKeys.ANONYMOUS to data.anonymous.toString(),
                ),
                block = {
                    updateStateThreadSafe {
                        screenState.dismissSnackbar()
                        screenState.setLoading()
                    }

                    val preparedReport = prepareReport(data)

                    val params = SendIssueReportUseCase.Params(
                        report = preparedReport,
                        target = githubTarget,
                        token = githubToken.takeIf { it.isNotBlank() },
                    )

                    sendIssueReport(params)
                        .flowOn(dispatchers.io)
                        .map { it.asDataState() }
                        .onEach { result -> handleResult(result) }
                        .catch { throwable ->
                            emit(DataState.Error(error = IssueReporterError.Generic(message = throwable.message)))
                        }
                        .collect { /* handled in onEach */ }
                },
                onError = {
                    showFailureSnackbar()
                },
            )
        }
    }

    /**
     * Loads device information lazily the first time the UI expands the section.
     *
     * Threading rationale:
     * - Device-info capture and string formatting are done off-main via [dispatchers.default]
     *   to avoid blocking Compose recompositions.
     */
    private fun loadDeviceInfoIfNeeded() {
        val currentDeviceInfo = screenData?.deviceInfoText
        if (currentDeviceInfo != null) return

        viewModelScope.launch {
            val captured = withContext(dispatchers.default) {
                repository.captureDeviceInfo().toPlainText()
            }
            updateStateThreadSafe {
                screenState.copyData {
                    if (deviceInfoText != null) this else copy(deviceInfoText = captured)
                }
            }
        }
    }

    private suspend fun prepareReport(data: IssueReporterUiState): Report {
        val deviceInfo = repository.captureDeviceInfo()
        val extraInfo = ExtraInfo()

        return Report(
            title = data.title,
            description = data.description,
            deviceInfo = deviceInfo,
            extraInfo = extraInfo,
            email = data.email.ifBlank { null },
        )
    }

    private suspend fun handleResult(outcome: DataState<String, IssueReporterError>) {
        outcome
            .onSuccess { url ->
                updateStateThreadSafe {
                    val updated = (screenData ?: IssueReporterUiState()).copy(issueUrl = url)
                    screenState.setSuccess(data = updated)

                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.snack_report_success),
                            isError = false,
                            timeStamp = System.nanoTime(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
            }
            .onFailure { error ->
                val message = error.toUiText()
                updateStateThreadSafe {
                    screenState.setError(message = message)
                }
            }
    }

    private suspend fun showFailureSnackbar(
        message: UiTextHelper = UiTextHelper.StringResource(R.string.snack_report_failed),
    ) {
        updateStateThreadSafe {
            screenState.setError(message = message)
        }
    }

    private fun IssueReporterError.toUiText(): UiTextHelper =
        when (this) {
            is IssueReporterError.Http -> when (status) {
                HttpStatusCode.Unauthorized -> UiTextHelper.StringResource(R.string.error_unauthorized)
                HttpStatusCode.Forbidden -> UiTextHelper.StringResource(R.string.error_forbidden)
                HttpStatusCode.Gone -> UiTextHelper.StringResource(R.string.error_gone)
                HttpStatusCode.UnprocessableEntity -> UiTextHelper.StringResource(R.string.error_unprocessable)
                else -> if (message.isNullOrBlank()) {
                    UiTextHelper.StringResource(R.string.snack_report_failed)
                } else {
                    UiTextHelper.DynamicString(message)
                }
            }

            is IssueReporterError.Generic -> message?.let { UiTextHelper.DynamicString(it) }
                ?: UiTextHelper.StringResource(R.string.snack_report_failed)
        }

    private object Actions {
        const val SEND_REPORT: String = "sendReport"
    }

    private object ExtraKeys {
        const val HAS_TITLE: String = "hasTitle"
        const val HAS_DESCRIPTION: String = "hasDescription"
        const val ANONYMOUS: String = "anonymous"
    }
}

