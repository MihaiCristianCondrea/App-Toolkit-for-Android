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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation.IssueReporterPresence
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import org.koin.compose.viewmodel.koinViewModel

/**
 * Shared report sheet for Compose hosts and
 * [IssueReporterLauncher][com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation.IssueReporterLauncher].
 * It opens fully expanded so the editor remains usable above the keyboard.
 *
 * Presence is registered for the composition lifetime to prevent duplicate sheets across entry
 * points. Dismissal resets the shared ViewModel so drafts and completed reports do not
 * reappear; an in-flight submission finishes before its reset.
 *
 * [onDismissRequest] runs after the sheet settles out of view, allowing removal from
 * composition without cutting the exit animation.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IssueReporterBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Shares the content ViewModel through the same store owner and key.
    val viewModel: IssueReporterViewModel = koinViewModel()

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    )

    DisposableEffect(Unit) {
        IssueReporterPresence.onShown()
        onDispose { IssueReporterPresence.onHidden() }
    }

    val dismiss: () -> Unit = {
        viewModel.onEvent(IssueReporterEvent.Reset)
        onDismissRequest()
    }

    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        IssueReporterContent(onDone = dismiss, viewModel = viewModel)
    }
}
