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
 * The issue reporter as a modal bottom sheet.
 *
 * A host inside a composition shows this directly, the way the advanced settings row does. A caller
 * with no composition to attach to, the shake gesture, reaches the same composable through
 * [IssueReporterLauncher][com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.presentation.IssueReporterLauncher],
 * so there is one sheet implementation and not one per entry point.
 *
 * It opens expanded rather than partially. The description field plus a keyboard needs most of the
 * screen, and a half-expanded sheet would put the author's own text behind the IME on first focus.
 *
 * [onDismissRequest] is called once the sheet has settled out of view, so a caller can drop it from
 * composition immediately without cutting the exit animation.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IssueReporterBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Resolved here as well as in the content so dismissal can reach it. Both calls land on the
    // same instance, because they share a store owner and a key.
    val viewModel: IssueReporterViewModel = koinViewModel()

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    )

    // Tells the launcher a sheet is up, so a shake landing on a screen that already shows one does
    // not stack a second. Leaving composition is the only way a sheet ends, including when the
    // activity is torn down under it, so the flag cannot be left set.
    DisposableEffect(Unit) {
        IssueReporterPresence.onShown()
        onDispose { IssueReporterPresence.onHidden() }
    }

    // The report is composed in a ViewModel that outlives the sheet, so an abandoned draft would
    // come back the next time the sheet opened, along with the confirmation of a report that was
    // already filed. The reset belongs here, at the end of the interaction, and not to the network
    // answering: the author's text stays on screen for as long as the sheet does.
    val dismiss: () -> Unit = {
        viewModel.onEvent(IssueReporterEvent.Reset)
        onDismissRequest()
    }

    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        // Done on the confirmation is a dismissal like any other, so it goes through the same path
        // and gets the same reset.
        IssueReporterContent(onDone = dismiss, viewModel = viewModel)
    }
}
