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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.repository

import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.mapper.toCreateIssueRequest
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.model.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.repository.IssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
import kotlinx.coroutines.withContext

class DefaultIssueReporterRepository(
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
