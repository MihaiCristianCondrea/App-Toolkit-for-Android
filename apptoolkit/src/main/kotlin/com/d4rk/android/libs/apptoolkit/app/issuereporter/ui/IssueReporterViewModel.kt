package com.d4rk.android.libs.apptoolkit.app.issuereporter.ui

import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.ExtraInfo
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.providers.DeviceInfoProvider
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.usecases.SendIssueReportUseCase
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.contract.IssueReporterAction
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.contract.IssueReporterEvent
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.state.IssueReporterUiState
import com.d4rk.android.libs.apptoolkit.core.di.GithubToken
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IssueReporterViewModel(
    private val sendIssueReport: SendIssueReportUseCase,
    private val githubTarget: GithubTarget,
    @param:GithubToken private val githubToken: String,
    private val deviceInfoProvider: DeviceInfoProvider,
) : ScreenViewModel<IssueReporterUiState, IssueReporterEvent, IssueReporterAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = IssueReporterUiState()
    )
) {
    private var sendJob: Job? = null
    override fun onEvent(event: IssueReporterEvent) {
        when (event) {
            is IssueReporterEvent.UpdateTitle -> update { it.copy(title = event.value) }
            is IssueReporterEvent.UpdateDescription -> update { it.copy(description = event.value) }
            is IssueReporterEvent.UpdateEmail -> update { it.copy(email = event.value) }
            is IssueReporterEvent.SetAnonymous -> update { it.copy(anonymous = event.anonymous) }
            is IssueReporterEvent.Send -> sendReport()
            is IssueReporterEvent.DismissSnackbar -> screenState.dismissSnackbar()
        }
    }

    private fun update(mutator: (IssueReporterUiState) -> IssueReporterUiState) {
        screenState.updateData(newState = screenState.value.screenState) { current ->
            mutator(current)
        }
    }

    private fun sendReport() {
        val data = screenState.value.data ?: return

        if (sendJob?.isActive == true) return

        if (data.title.isBlank() || data.description.isBlank()) {
            screenState.showSnackbar(
                snackbar = UiSnackbar(
                    message = UiTextHelper.StringResource(R.string.error_invalid_report),
                    timeStamp = System.currentTimeMillis(),
                    isError = true,
                    type = ScreenMessageType.SNACKBAR
                )
            )
            return
        }

        sendJob = viewModelScope.launch {
            var report: Report? = null

            runCatching {
                val deviceInfo = deviceInfoProvider.capture()
                val extraInfo = ExtraInfo()
                Report(
                    title = data.title,
                    description = data.description,
                    deviceInfo = deviceInfo,
                    extraInfo = extraInfo,
                    email = data.email.ifBlank { null },
                )
            }
                .onSuccess { createdReport ->
                    report = createdReport
                }
                .onFailure { throwable ->
                    if (throwable is CancellationException) throw throwable
                    showFailureSnackbar()
                }

            val preparedReport = report ?: return@launch

            val params = SendIssueReportUseCase.Params(
                report = preparedReport,
                target = githubTarget,
                token = githubToken.takeIf { it.isNotBlank() }
            )

            sendIssueReport(params)
                .onStart { screenState.setLoading() }
                .onCompletion { cause ->
                    when {
                        cause != null && cause !is CancellationException -> showFailureSnackbar()
                        screenState.value.screenState is ScreenState.IsLoading ->
                            screenState.updateState(ScreenState.Success())
                    }
                    sendJob = null
                }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                }
                .collect { outcome -> handleResult(outcome) }
        }
    }

    private fun handleResult(outcome: IssueReportResult) {
        when (outcome) {
            is IssueReportResult.Success -> {
                screenState.update { current ->
                    val updated = current.data?.copy(issueUrl = outcome.url)
                    current.copy(
                        screenState = ScreenState.Success(),
                        data = updated,
                        snackbar = UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.snack_report_success),
                            isError = false,
                            timeStamp = System.currentTimeMillis(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
            }

            is IssueReportResult.Error -> {
                val msg = when (outcome.status) {
                    HttpStatusCode.Unauthorized -> UiTextHelper.StringResource(R.string.error_unauthorized)
                    HttpStatusCode.Forbidden -> UiTextHelper.StringResource(R.string.error_forbidden)
                    HttpStatusCode.Gone -> UiTextHelper.StringResource(R.string.error_gone)
                    HttpStatusCode.UnprocessableEntity -> UiTextHelper.StringResource(R.string.error_unprocessable)
                    else -> UiTextHelper.StringResource(R.string.snack_report_failed)
                }
                showFailureSnackbar(msg)
            }
        }
    }

    private fun showFailureSnackbar(
        message: UiTextHelper = UiTextHelper.StringResource(R.string.snack_report_failed),
    ) {
        screenState.update { current ->
            current.copy(
                screenState = ScreenState.Error(),
                snackbar = UiSnackbar(
                    message = message,
                    isError = true,
                    timeStamp = System.currentTimeMillis(),
                    type = ScreenMessageType.SNACKBAR,
                )
            )
        }
    }
}
