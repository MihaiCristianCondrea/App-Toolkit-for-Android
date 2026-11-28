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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.models.ChangelogRelease
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.analytics.CHANGELOG_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.contracts.ChangelogEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.states.ChangelogUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.views.lists.ChangelogReleaseList
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.viewmodel.koinViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * Displays the host application's full release history in a modal bottom sheet.
 *
 * @param onDismiss Closes the bottom sheet.
 * @param releasesUrl Optional page opened from the title's info button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangelogDialog(
    onDismiss: () -> Unit,
    releasesUrl: String? = null,
) {
    val context = LocalContext.current
    val viewModel: ChangelogViewModel = koinViewModel()
    val state: ChangelogUiState by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
    )

    LaunchedEffect(key1 = viewModel) {
        val currentReleases = viewModel.state.value.releases
        if ((currentReleases is Loadable.Failed) || (currentReleases is Loadable.Empty)) {
            viewModel.onEvent(event = ChangelogEvent.Load)
        }
    }

    ScreenHost(screen = CHANGELOG_SCREEN, status = state.releases) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            onDismissRequest = onDismiss,
            sheetState = sheetState,
        ) {
            ChangelogDialogContent(
                state = state,
                onRetry = { viewModel.onEvent(event = ChangelogEvent.Load) },
                onDismiss = onDismiss,
                onOpenReleases = releasesUrl
                    ?.takeIf { it.isNotBlank() }
                    ?.let { url -> { context.openUrl(url = url) } },
            )
        }
    }
}

/**
 * Renders changelog state with the header and action fixed outside the scrolling release list.
 *
 * @param onRetry Triggered when the load failure offers Retry.
 * @param onDismiss Closes the sheet.
 * @param onOpenReleases Optional action for the title's info button.
 */
@Composable
internal fun ChangelogDialogContent(
    state: ChangelogUiState,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenReleases: (() -> Unit)? = null,
) {
    val releases: Loadable<ImmutableList<ChangelogRelease>> = state.releases
    val canRetry: Boolean = (releases is Loadable.Failed) && releases.retryable

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(
                start = MaterialTheme.spacing.large,
                end = MaterialTheme.spacing.large,
                bottom = MaterialTheme.spacing.large,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = Icons.Outlined.NewReleases, contentDescription = null)
            LargeHorizontalSpacer()
            Text(
                text = stringResource(id = R.string.changelog_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(weight = 1f),
            )
            if (onOpenReleases != null) {
                GeneralButton(
                    style = GeneralButtonStyle.Text,
                    onClick = onOpenReleases,
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Info),
                    contentDescription = stringResource(id = R.string.changelog_all_releases),
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(weight = 1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            ChangelogBody(releases = releases)
        }
        GeneralButton(
            modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun ChangelogBody(releases: Loadable<ImmutableList<ChangelogRelease>>) {
    ScreenStateHandler(
        state = releases,
        showAd = false,
        onLoading = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularWavyProgressIndicator()
                LargeHorizontalSpacer()
                Text(text = stringResource(id = R.string.loading_changelog_message))
            }
        },
        onEmpty = { Text(text = stringResource(id = R.string.no_new_updates_message)) },
        onError = { failure -> Text(text = failure.message.asString()) },
    ) { ready ->
        ChangelogReleaseList(releases = ready.value)
    }
}
