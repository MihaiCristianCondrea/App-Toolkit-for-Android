package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository

import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget

/**
 * Repository contract for sending issue reports.
 */
interface IssueReporterRepository {
    suspend fun sendReport(
        report: Report,
        target: GithubTarget,
        token: String? = null,
    ): IssueReportResult
}
