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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts

/**
 * What the author can ask the issue reporter's ViewModel to do.
 */
sealed interface IssueReporterEvent {
    data class UpdateTitle(val value: String) : IssueReporterEvent

    data class UpdateDescription(val value: String) : IssueReporterEvent

    data class UpdateEmail(val value: String) : IssueReporterEvent

    /** Captures the device details for the panel, unless they are already shown or loading. */
    data object RequestDeviceInfo : IssueReporterEvent

    data object Send : IssueReporterEvent

    /**
     * Returns the reporter to an empty report when the sheet closes. The ViewModel outlives the
     * sheet, so without it a draft or a filed report's confirmation would show on the next opening.
     */
    data object Reset : IssueReporterEvent
}
