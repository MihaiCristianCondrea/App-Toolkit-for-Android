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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions.IssueReportRejectedException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.GithubTarget

/**
 * The data layer's entry point for issue reports. Both calls are safe from the main thread.
 */
interface IssueReporterRepository {

    /**
     * Captures the device details attached to a report and shown in the sheet's device panel.
     */
    suspend fun captureDeviceInfo(): DeviceInfo

    /**
     * Files [report] as an issue in [target], authenticated with [token] when there is one, and
     * returns the created issue's web URL.
     *
     * @throws IssueReportRejectedException when GitHub refuses the token, the repository or the
     * issue's fields.
     * @throws NetworkException when the request fails for any other reason.
     */
    suspend fun sendReport(
        report: Report,
        target: GithubTarget,
        token: String? = null,
    ): String
}
