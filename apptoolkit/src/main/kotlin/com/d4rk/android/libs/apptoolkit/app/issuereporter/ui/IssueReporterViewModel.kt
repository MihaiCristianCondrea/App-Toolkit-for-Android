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
import com.d4rk.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.state.copyData
import com.d4rk.android.libs.apptoolkit.core.ui.state.dismissSnackbar
import com.d4rk.android.libs.apptoolkit.core.ui.state.setError
import com.d4rk.android.libs.apptoolkit.core.ui.state.setLoading
import com.d4rk.android.libs.apptoolkit.core.ui.state.setSuccess
import com.d4rk.android.libs.apptoolkit.core.ui.state.showSnackbar
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Error as RootError

class IssueReporterViewModel(
    private val sendIssueReport: SendIssueReportUseCase,
    private val githubTarget: GithubTarget,
    @param:GithubToken private val githubToken: String,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val dispatchers: com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider,
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
            is IssueReporterEvent.UpdateTitle -> updateForm { copy(title = event.value) }
            is IssueReporterEvent.UpdateDescription -> updateForm { copy(description = event.value) }
            is IssueReporterEvent.UpdateEmail -> updateForm { copy(email = event.value) }
            is IssueReporterEvent.SetAnonymous -> updateForm { copy(anonymous = event.anonymous) }
            is IssueReporterEvent.Send -> sendReport()
            is IssueReporterEvent.DismissSnackbar -> viewModelScope.launch {
                updateStateThreadSafe {
                    screenState.dismissSnackbar()
                }
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

    private suspend fun prepareReport(data: IssueReporterUiState): Report {
        val deviceInfo = deviceInfoProvider.capture()
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

    private fun IssueReportResult.asDataState(): DataState<String, IssueReporterError> =
        when (this) {
            is IssueReportResult.Success -> DataState.Success(url)
            is IssueReportResult.Error -> DataState.Error(
                error = IssueReporterError.Http(status = status, message = message),
            )
        }

    private sealed interface IssueReporterError : RootError {
        val message: String?

        data class Http(val status: HttpStatusCode, override val message: String?) :
            IssueReporterError
        data class Generic(override val message: String?) : IssueReporterError
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
