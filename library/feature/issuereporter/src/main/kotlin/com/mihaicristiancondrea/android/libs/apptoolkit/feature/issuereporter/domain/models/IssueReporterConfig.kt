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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models

/**
 * Host-level configuration for the issue reporter.
 *
 * The reporter is a library feature shipped into several apps, and shake-to-report is the one part
 * of it that costs something when nobody asked for it: an accelerometer listener registered for as
 * long as an activity is resumed. It is therefore opt-in rather than on by default, and each host
 * decides by passing its own instance into the toolkit's Koin graph.
 *
 * @property shakeToReportEnabled Whether shaking the device opens the report sheet. When false the
 * shake manager registers nothing, so no sensor is ever read.
 */
data class IssueReporterConfig(
    val shakeToReportEnabled: Boolean = false,
)
