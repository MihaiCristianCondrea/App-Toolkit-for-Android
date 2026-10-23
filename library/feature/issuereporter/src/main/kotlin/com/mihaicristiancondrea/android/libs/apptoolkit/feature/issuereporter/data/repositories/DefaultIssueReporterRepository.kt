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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.networkCall
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.local.DeviceInfoDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.mappers.toCreateIssueRequest

/**
 * Sends reports through [remoteDataSource] and captures device details through
 * [deviceInfoDataSource], which moves its own platform reads off the main thread. Ktor suspends, so
 * sending needs no dispatcher.
 */
class DefaultIssueReporterRepository(
    private val remoteDataSource: IssueReporterRemoteDataSource,
    private val deviceInfoDataSource: DeviceInfoDataSource,
    private val diagnostics: DiagnosticsReporter,
) : IssueReporterRepository {

    override suspend fun captureDeviceInfo(): DeviceInfo = deviceInfoDataSource.capture()

    /** The breadcrumb names the target repository and whether a token is set, never the token. */
    override suspend fun sendReport(
        report: Report,
        target: GithubTarget,
        token: String?,
    ): String {
        diagnostics.logBreadcrumb(
            message = "Issue report sending",
            attributes = mapOf(
                "targetRepo" to target.repository,
                "hasToken" to (!token.isNullOrBlank()).toString(),
            ),
        )
        return networkCall {
            remoteDataSource.createIssue(
                payload = report.toCreateIssueRequest(),
                target = target,
                token = token,
            )
        }
    }
}
