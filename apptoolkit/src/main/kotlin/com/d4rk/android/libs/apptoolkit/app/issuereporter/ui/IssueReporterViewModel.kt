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
import kotlinx.coroutines.flow.map
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
                        screenState.updateState(ScreenState.Success())
                    }
                    sendJob = null
                }
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    emit(DataState.Error(error = IssueReporterError.Generic(message = throwable.message)))
                }
                .collect(::handleResult)
        }
    }

    private fun handleResult(outcome: DataState<String, IssueReporterError>) {
        outcome
            .onSuccess { url ->
                screenState.update { current ->
                    val updated = current.data?.copy(issueUrl = url)
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
            .onFailure { error ->
                val message = when (error) {
                    is IssueReporterError.Http -> error.toUiText()
                    is IssueReporterError.Generic -> error.toUiText()
                }
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

    private fun IssueReportResult.asDataState(): DataState<String, IssueReporterError> {
        return when (this) {
            is IssueReportResult.Success -> DataState.Success(url)
            is IssueReportResult.Error -> DataState.Error(
                error = IssueReporterError.Http(status = status, message = message)
            )
        }
    }

    private sealed class IssueReporterError(open val message: String?) : Error(message) {
        data class Http(val status: HttpStatusCode, override val message: String?) : IssueReporterError(message)
        data class Generic(override val message: String?) : IssueReporterError(message)
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
