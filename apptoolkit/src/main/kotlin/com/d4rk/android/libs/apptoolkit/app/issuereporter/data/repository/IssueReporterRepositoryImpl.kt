package com.d4rk.android.libs.apptoolkit.app.issuereporter.data.repository

import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.mapper.toCreateIssueRequest
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.withContext

class IssueReporterRepositoryImpl(
    private val remoteDataSource: IssueReporterRemoteDataSource,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
) : IssueReporterRepository {

    override suspend fun sendReport(
        report: Report,
        target: GithubTarget,
        token: String?,
    ): IssueReportResult = withContext(dispatchers.io) {
        firebaseController.logBreadcrumb(
            message = "Issue report sending",
            attributes = mapOf(
                "targetRepo" to target.repository,
                "hasToken" to (!token.isNullOrBlank()).toString(),
            ),
        )
        val payload = report.toCreateIssueRequest()
        remoteDataSource.createIssue(
            payload = payload,
            target = target,
            token = token,
        )
    }
}
