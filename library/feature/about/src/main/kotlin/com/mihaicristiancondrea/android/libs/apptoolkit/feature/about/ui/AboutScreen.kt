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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenActionHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItems
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItemDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.analytics.ABOUT_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.analytics.aboutPreferenceTapEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.constants.AboutEasterEggConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.states.AboutUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.views.effects.TapCelebration
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.LicensesScreen
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.viewmodel.koinViewModel

/**
 * Displays the host and Toolkit versions, the device report and the open-source licenses link.
 *
 * @param onVersionTap Receives the cumulative number of version-row taps.
 */
@Composable
fun AboutScreen(
    onVersionTap: (Int) -> Unit = {},
) {
    val viewModel: AboutViewModel = koinViewModel()
    val state: AboutUiState by viewModel.state.collectAsStateWithLifecycle()
    val navigator: ShellNavigator = LocalShellNavigator.current

    val context: Context = LocalContext.current
    ScreenHost(screen = ABOUT_SCREEN, status = state.items, viewModel = viewModel) {
        TapCelebration(
            threshold = AboutEasterEggConstants.EASTER_EGG_TAPS,
            onTap = onVersionTap,
            onThresholdReached = { viewModel.onEvent(event = AboutEvent.EasterEggFound) },
            modifier = Modifier.fillMaxHeight(),
        ) { onBuildVersionTap ->
            ScreenActionHandler(
                onEvent = viewModel::onEvent,
                handleAction = { action, dispatch ->
                    when (action) {
                        is AboutItemAction.CopyToClipboard -> dispatch(
                            AboutEvent.CopyToClipboard(
                                label = action.label.asString(context = context),
                                text = action.text.asString(context = context),
                                successMessage = action.successMessage,
                            ),
                        )

                        is AboutItemAction.OpenLicenses -> navigator.navigate(key = LicensesScreen)
                        is AboutItemAction.VersionTap -> onBuildVersionTap()
                    }
                },
            ) { onAction ->
                AboutScreenContent(
                    state = state,
                    contentPadding = contentPadding(),
                    onRetry = { viewModel.onEvent(event = AboutEvent.Load) },
                    onAction = onAction,
                )
            }
        }
    }
}

/**
 * Renders the About entries and reports preference clicks and retry requests.
 *
 * @param contentPadding Shell insets applied inside the list and load-state surfaces.
 */
@Composable
internal fun AboutScreenContent(
    state: AboutUiState,
    onRetry: () -> Unit,
    onAction: (AboutItemAction) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    ScreenStateHandler(
        state = state.items,
        modifier = modifier.fillMaxHeight(),
        contentPadding = contentPadding,
        onRetry = onRetry,
        onSuccess = { ready: Loadable.Ready<ImmutableList<AboutItem>> ->
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(space = ListItemDefaults.SegmentedGap),
            ) {
                animatedItems(
                    items = ready.value,
                    key = { it.key },
                ) { item ->
                    when (item) {
                        is AboutItem.Header -> PreferenceCategoryItem(title = item.title.asString())

                        is AboutItem.Preference -> SettingsPreferenceItem(
                            title = item.title.asString(),
                            summary = item.summary.asString(),
                            shapes = PreferenceItemDefaults.segmentedShapes(index = item.index, count = item.count),
                            onClick = item.action?.let { action -> { onAction(action) } },
                            ga4Event = when (item.action) {
                                is AboutItemAction.CopyToClipboard,
                                is AboutItemAction.OpenLicenses -> aboutPreferenceTapEvent(preferenceKey = item.key)

                                is AboutItemAction.VersionTap, null -> null
                            },
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                        )
                    }
                }
            }
        },
    )
}
