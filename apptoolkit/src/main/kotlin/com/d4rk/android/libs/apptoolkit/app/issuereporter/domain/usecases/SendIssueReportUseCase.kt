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

package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SendIssueReportUseCase(
    private val repository: IssueReporterRepository,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) {

    data class Params(
        val report: Report,
        val target: GithubTarget,
        val token: String?
    )

    operator fun invoke(param: Params): Flow<IssueReportResult> =
        flow {
            firebaseController.logBreadcrumb(
                message = "Issue report send started",
                attributes = mapOf(
                    "targetRepo" to param.target.repository,
                    "hasToken" to (!param.token.isNullOrBlank()).toString(),
                ),
            )
            val result = repository.sendReport(param.report, param.target, param.token)
            emit(result)
        }
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                emit(
                    IssueReportResult.Error(
                        status = HttpStatusCode.InternalServerError,
                        message = throwable.message ?: "",
                    ),
                )
            }
            .flowOn(dispatchers.io)
}
