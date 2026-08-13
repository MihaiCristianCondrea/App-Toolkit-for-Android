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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material3.Button
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
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.contract.ChangelogEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.state.ChangelogUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.ScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.state.UiStateScreen
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                ChangelogDialogContent(screenState = screenState)
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (isError) {
                        viewModel.onEvent(event = ChangelogEvent.Retry)
                    } else {
                        onDismiss()
                    }
                },
            ) {
                Text(
                    text = if (isError) {
                        stringResource(id = R.string.try_again)
                    } else {
                        stringResource(id = R.string.done_button_content_description)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ChangelogDialogContent(
    screenState: UiStateScreen<ChangelogUiState>,
) {
    when (screenState.screenState) {
        is ScreenState.IsLoading -> Row(verticalAlignment = Alignment.CenterVertically) {
            CircularWavyProgressIndicator()
            LargeHorizontalSpacer()
            Text(text = stringResource(id = R.string.loading_changelog_message))
        }

        is ScreenState.Error -> Column(verticalArrangement = Arrangement.Center) {
            Text(text = stringResource(id = R.string.error_loading_changelog_message))
        }

        is ScreenState.NoData,
        is ScreenState.Success -> {
            val markdown = screenState.data?.markdown.orEmpty().ifBlank {
                stringResource(id = R.string.no_new_updates_message)
            }
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                markdown = markdown,
            )
        }
    }
}
