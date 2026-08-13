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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.IssueReportResult
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.github.GithubTarget

/**
 * Repository contract for sending issue reports.
 */
interface IssueReporterRepository {

    /**
     * Captures the device details attached to a report, and shown in the report screen's device
     * panel.
     *
     * Exposed here rather than letting callers hold `DeviceInfoProvider`: that interface is
     * implemented by a data source, and the UI reached it directly. The repository is the data
     * layer's entry point.
     */
    suspend fun captureDeviceInfo(): DeviceInfo

    suspend fun sendReport(
        report: Report,
        target: GithubTarget,
        token: String? = null,
    ): IssueReportResult
}
