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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.Report
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.ExtraInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState

/** The draft as the report filed on GitHub, with [deviceInfo] attached. A blank email is left out. */
internal fun IssueReporterUiState.toReport(deviceInfo: DeviceInfo): Report = Report(
    title = title,
    description = description,
    deviceInfo = deviceInfo,
    extraInfo = ExtraInfo(),
    email = email.ifBlank { null },
)
