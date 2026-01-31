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
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onFailure
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.onSuccess
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.base.ScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.successData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateData
import com.d4rk.android.libs.apptoolkit.core.ui.state.updateState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Error as RootError

class IssueReporterViewModel(
    private val sendIssueReport: SendIssueReportUseCase,
    private val githubTarget: GithubTarget,
    @param:GithubToken private val githubToken: String,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val firebaseController: FirebaseController,
) : ScreenViewModel<IssueReporterUiState, IssueReporterEvent, IssueReporterAction>(
    initialState = UiStateScreen(
        screenState = ScreenState.Success(),
        data = IssueReporterUiState()
    )
) {
    override fun onEvent(event: IssueReporterEvent) {
        firebaseController.logBreadcrumb(
            message = "IssueReporterViewModel event",
            attributes = mapOf("event" to event::class.java.simpleName),
        )
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

        if (generalJob?.isActive == true) return

        firebaseController.logBreadcrumb(
            message = "Issue report send requested",
            attributes = mapOf(
                "hasTitle" to data.title.isNotBlank().toString(),
                "hasDescription" to data.description.isNotBlank().toString(),
                "anonymous" to data.anonymous.toString(),
            ),
        )
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

        generalJob = viewModelScope.launch {
            val preparedReport = try {
                val deviceInfo = deviceInfoProvider.capture()
                val extraInfo = ExtraInfo()
                Report(
                    title = data.title,
                    description = data.description,
                    deviceInfo = deviceInfo,
                    extraInfo = extraInfo,
                    email = data.email.ifBlank { null },
                )
            } catch (throwable: Throwable) {
                if (throwable is CancellationException) throw throwable
                showFailureSnackbar()
                return@launch
            }

            val params = SendIssueReportUseCase.Params(
                report = preparedReport,
                target = githubTarget,
                token = githubToken.takeIf { it.isNotBlank() }
            )

            sendIssueReport(params)
                .map { it.asDataState() }
                .onStart { screenState.setLoading() }
                .onCompletion { cause ->
                    if (cause is CancellationException) throw cause
                    if (screenState.value.screenState is ScreenState.IsLoading) {
                        updateStateThreadSafe { screenState.updateState(ScreenState.Success()) }
                    }
                    generalJob = null
                }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    firebaseController.reportViewModelError(
                        viewModelName = "IssueReporterViewModel",
                        action = "sendReport",
                        throwable = throwable,
                    )
                    emit(DataState.Error(error = IssueReporterError.Generic(message = throwable.message)))
                }
                .collect { result -> handleResult(result) }
        }
    }

    private suspend fun handleResult(outcome: DataState<String, IssueReporterError>) {
        outcome
            .onSuccess { url ->
                updateStateThreadSafe {
                    screenState.successData { copy(issueUrl = url) }
                    screenState.showSnackbar(
                        UiSnackbar(
                            message = UiTextHelper.StringResource(R.string.snack_report_success),
                            isError = false,
                            timeStamp = System.currentTimeMillis(),
                            type = ScreenMessageType.SNACKBAR,
                        )
                    )
                }
            }
            .onFailure { error ->
                val message = when (error) {
                    is IssueReporterError.Http -> error.toUiText()
                    is IssueReporterError.Generic -> error.toUiText()
                }
                updateStateThreadSafe { screenState.setError(message = message) }
            }
    }

    private suspend fun showFailureSnackbar(
        message: UiTextHelper = UiTextHelper.StringResource(R.string.snack_report_failed),
    ) {
        updateStateThreadSafe { screenState.setError(message = message) }
    }

    private fun IssueReportResult.asDataState(): DataState<String, IssueReporterError> {
        return when (this) {
            is IssueReportResult.Success -> DataState.Success(url)
            is IssueReportResult.Error -> DataState.Error(
                error = IssueReporterError.Http(status = status, message = message)
            )
        }
    }

    private sealed interface IssueReporterError : RootError {
        val message: String?

        data class Http(val status: HttpStatusCode, override val message: String?) :
            IssueReporterError

        data class Generic(override val message: String?) : IssueReporterError
    }

    private fun IssueReporterError.toUiText(): UiTextHelper {
        return when (this) {
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
    }
}
