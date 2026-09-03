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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.providers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.DeviceInfo

/**
 * Captures the device details attached to a report.
 *
 * A `fun interface` so a test can supply one as a lambda. Callers outside the data layer should go
 * through `IssueReporterRepository.captureDeviceInfo()`, this is the data source's own contract.
 */
fun interface DeviceInfoProvider {
    suspend fun capture(): DeviceInfo
}

