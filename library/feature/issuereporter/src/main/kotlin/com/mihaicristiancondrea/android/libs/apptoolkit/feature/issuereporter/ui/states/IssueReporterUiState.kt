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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable

/**
 * Everything the issue reporter sheet renders. The form fields survive submission and are cleared
 * when the sheet is dismissed.
 *
 * @property submissionState Which of the editor and the confirmation shows.
 * @property deviceInfo The device details as plain text. Stays [Loadable.Empty] until the device
 * panel is first opened.
 */
@Immutable
data class IssueReporterUiState(
    val title: String = "",
    val description: String = "",
    val email: String = "",
    val submissionState: IssueSubmissionState = IssueSubmissionState.Editing,
    val deviceInfo: Loadable<String> = Loadable.Empty(),
)
