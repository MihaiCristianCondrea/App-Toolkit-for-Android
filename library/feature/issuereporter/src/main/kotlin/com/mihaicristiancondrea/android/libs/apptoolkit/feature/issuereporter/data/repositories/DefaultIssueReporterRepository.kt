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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.networkCall
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.mappers.toCreateIssueRequest
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.providers.DeviceInfoProvider

/**
 * Sends reports through [remoteDataSource] and captures device details through
 * [deviceInfoProvider], which moves its own platform reads off the main thread. Ktor suspends, so
 * sending needs no dispatcher.
 */
class DefaultIssueReporterRepository(
    private val remoteDataSource: IssueReporterRemoteDataSource,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val telemetryRepository: TelemetryRepository,
) : IssueReporterRepository {

    override suspend fun captureDeviceInfo(): DeviceInfo = deviceInfoProvider.capture()

    /** The breadcrumb names the target repository and whether a token is set, never the token. */
    override suspend fun sendReport(
        report: Report,
        target: GithubTarget,
        token: String?,
    ): String {
        telemetryRepository.logBreadcrumb(
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
