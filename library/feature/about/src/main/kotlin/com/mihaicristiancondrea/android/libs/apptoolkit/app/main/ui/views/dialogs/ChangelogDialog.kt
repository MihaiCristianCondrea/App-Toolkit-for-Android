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

/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.views.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.ChangelogViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.contracts.ChangelogEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.states.ChangelogUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.koin.compose.viewmodel.koinViewModel

/**
 * Displays the host application's package-aware changelog in a modal bottom sheet.
 *
 * Network and fallback decisions are owned by the injected [ChangelogViewModel]; this composable
 * only renders state and sends retry intent. The header and action remain fixed while the body
 * scrolls independently, so long release notes cannot push the dismissal action off-screen.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChangelogDialog(
    onDismiss: () -> Unit,
) {
    val viewModel: ChangelogViewModel = koinViewModel()
    val screenState: UiStateScreen<ChangelogUiState> by
    viewModel.uiState.collectAsStateWithLifecycle()
    val isError = screenState.screenState is ScreenState.Error
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    )

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(imageVector = Icons.Outlined.NewReleases, contentDescription = null)
                LargeHorizontalSpacer()
                Text(
                    text = stringResource(id = R.string.changelog_title),
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                ChangelogDialogContent(screenState = screenState)
            }
            GeneralButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (isError) {
                        viewModel.onEvent(event = ChangelogEvent.Retry)
                    } else {
                        onDismiss()
                    }
                },
                label = if (isError) {
                    stringResource(id = R.string.try_again)
                } else {
                    stringResource(id = R.string.done_button_content_description)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ChangelogDialogContent(
    screenState: UiStateScreen<ChangelogUiState>,
) {
    when (screenState.screenState) {
        is ScreenState.IsLoading -> Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            CircularWavyProgressIndicator()
            LargeHorizontalSpacer()
            Text(text = stringResource(id = R.string.loading_changelog_message))
        }

        is ScreenState.Error -> Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.error_loading_changelog_message))
        }

        is ScreenState.NoData,
        is ScreenState.Success -> {
            val markdown = screenState.data?.markdown.orEmpty().ifBlank {
                stringResource(id = R.string.no_new_updates_message)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    markdown = markdown,
                )
            }
        }
    }
}
