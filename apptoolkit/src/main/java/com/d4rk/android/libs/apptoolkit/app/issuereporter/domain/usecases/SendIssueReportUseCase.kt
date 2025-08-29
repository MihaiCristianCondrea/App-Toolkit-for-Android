package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.AppDispatchers
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SendIssueReportUseCase(
    private val repository: IssueReporterRepository,
    private val dispatchers: AppDispatchers,
) {

    data class Params(
        val report: Report,
        val target: GithubTarget,
        val token: String?
    )

    operator fun invoke(param: Params): Flow<IssueReportResult> =
        flow {
            val result = repository.sendReport(param.report, param.target, param.token)
            emit(result)
        }
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                emit(
                    IssueReportResult.Error.Unknown(
                        status = HttpStatusCode.InternalServerError,
                        message = throwable.message ?: "",
                    ),
                )
            }
            .flowOn(dispatchers.io)
}