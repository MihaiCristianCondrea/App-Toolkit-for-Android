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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface IssueReporterEvent : UiEvent {
    data class UpdateTitle(val value: String) : IssueReporterEvent
    data class UpdateDescription(val value: String) : IssueReporterEvent
    data class UpdateEmail(val value: String) : IssueReporterEvent
    data object RequestDeviceInfo : IssueReporterEvent
    data object Send : IssueReporterEvent

    /**
     * Returns the reporter to an empty report.
     *
     * The presentation outlives no state of its own, so closing the sheet has to say so: the
     * ViewModel is scoped to the screen that opened it, and without this an abandoned draft, or the
     * confirmation of a report already filed, would be waiting the next time the sheet opened.
     */
    data object Reset : IssueReporterEvent
    data object DismissSnackbar : IssueReporterEvent
}

