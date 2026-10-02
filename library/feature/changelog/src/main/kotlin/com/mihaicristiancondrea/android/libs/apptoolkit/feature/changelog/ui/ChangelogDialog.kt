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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers.HorizontalWavyDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.contracts.ChangelogEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.states.ChangelogUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.utils.extensions.splitAtThematicBreaks
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.koin.compose.viewmodel.koinViewModel

/**
 * Displays the host application's package-aware changelog in a modal bottom sheet.
 *
 * This is the stateful half. It owns the [ChangelogViewModel], which decides the network source
 * and the fallback, and the sheet itself, and hands what the sheet shows to
 * [ChangelogDialogContent].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangelogDialog(
    onDismiss: () -> Unit,
) {
    val viewModel: ChangelogViewModel = koinViewModel()
    val state: ChangelogUiState by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    )

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        ChangelogDialogContent(
            state = state,
            onRetry = { viewModel.onEvent(ChangelogEvent.Load) },
            onDismiss = onDismiss,
        )
    }
}

/**
 * What the changelog sheet shows for [state]: the title, the release notes or why there are none,
 * and one action.
 *
 * The stateless half of [ChangelogDialog]. The header and action stay fixed while the body scrolls,
 * so long release notes cannot push the action off-screen. The action retries a failure that can
 * be retried, and otherwise dismisses.
 *
 * @param onRetry Loads the changelog again.
 * @param onDismiss Closes the sheet.
 */
@Composable
internal fun ChangelogDialogContent(
    state: ChangelogUiState,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val markdown: Loadable<String> = state.markdown
    val canRetry: Boolean = markdown is Loadable.Failed && markdown.retryable

    Column(
        modifier = modifier
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
            ChangelogBody(markdown = markdown)
        }
        GeneralButton(
            modifier = Modifier.fillMaxWidth(),
            // The sheet's only action, and the one every reader ends on.
            measurements = ButtonMeasurements.Medium,
            onClick = if (canRetry) onRetry else onDismiss,
            label = if (canRetry) {
                stringResource(id = CoreUiR.string.try_again)
            } else {
                stringResource(id = CoreUiR.string.done_button_content_description)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ChangelogBody(markdown: Loadable<String>) {
    when (markdown) {
        Loadable.Loading -> Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            CircularWavyProgressIndicator()
            LargeHorizontalSpacer()
            Text(text = stringResource(id = R.string.loading_changelog_message))
        }

        is Loadable.Failed -> Text(text = markdown.message.asString())

        is Loadable.Empty -> Text(text = stringResource(id = R.string.no_new_updates_message))

        is Loadable.Ready -> {
            // Each release on its own, with the Toolkit's wavy line where the Markdown has a rule:
            // the renderer's own rule is a flat line.
            val sections = remember(markdown.value) { markdown.value.splitAtThematicBreaks() }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                sections.forEachIndexed { index, section ->
                    if (index > 0) {
                        HorizontalWavyDivider(modifier = Modifier.padding(vertical = SizeConstants.LargeSize))
                    }
                    MarkdownText(
                        modifier = Modifier.fillMaxWidth(),
                        markdown = section,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 480)
@Composable
private fun ChangelogDialogContentPreview() {
    MaterialTheme {
        ChangelogDialogContent(
            state = ChangelogUiState(
                markdown = Loadable.Ready("# 2.0.0\n- A new settings search\n\n---\n\n# 1.9.0\n- Faster start"),
            ),
            onRetry = {},
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true, heightDp = 480)
@Composable
private fun ChangelogDialogContentFailedPreview() {
    MaterialTheme {
        ChangelogDialogContent(
            state = ChangelogUiState(
                markdown = Loadable.Failed(UiTextHelper.DynamicString("No internet connection.")),
            ),
            onRetry = {},
            onDismiss = {},
        )
    }
}
