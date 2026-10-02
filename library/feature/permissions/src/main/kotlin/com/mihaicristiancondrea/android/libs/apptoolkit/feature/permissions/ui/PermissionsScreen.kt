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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.contracts.PermissionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.states.PermissionsUiState
import org.koin.compose.viewmodel.koinViewModel

private const val PERMISSIONS_SCREEN_NAME = "Permissions"
private const val PERMISSIONS_SCREEN_CLASS = "PermissionsScreen"

/**
 * Descriptive permission catalog registered by `permissionsPage()`. It explains why permissions
 * are used rather than reporting their current grant state. Owns [PermissionsViewModel] and
 * screen tracking; [PermissionsScreenContent] handles rendering.
 */
@Composable
fun PermissionsScreen() {
    val viewModel: PermissionsViewModel = koinViewModel()
    val state: PermissionsUiState by viewModel.state.collectAsStateWithLifecycle()

    TrackScreenView(
        screenName = PERMISSIONS_SCREEN_NAME,
        screenClass = PERMISSIONS_SCREEN_CLASS,
    )
    TrackScreenState(
        screenName = PERMISSIONS_SCREEN_NAME,
        state = state.config,
    )

    PermissionsScreenContent(
        state = state,
        contentPadding = contentPadding(),
        onRetry = { viewModel.onEvent(PermissionsEvent.Load) },
    )
}

/**
 * The stateless half of [PermissionsScreen]: the catalog, or its loading, empty or failure state.
 *
 * @param onRetry The empty or failure state's retry was tapped.
 */
@Composable
internal fun PermissionsScreenContent(
    state: PermissionsUiState,
    contentPadding: PaddingValues,
    onRetry: () -> Unit,
) {
    ScreenStateHandler(
        state = state.config,
        contentPadding = contentPadding,
        onRetry = onRetry,
        onEmpty = { empty ->
            NoDataScreen(
                icon = Icons.Outlined.Settings,
                message = empty.message?.asString(),
                showRetry = true,
                onRetry = onRetry,
                paddingValues = contentPadding,
            )
        },
        onError = { failed ->
            NoDataScreen(
                icon = Icons.Outlined.Settings,
                message = failed.message.asString(),
                isError = true,
                showRetry = failed.retryable,
                onRetry = onRetry,
                paddingValues = contentPadding,
            )
        },
        onSuccess = { ready ->
            PermissionsContent(
                paddingValues = contentPadding,
                settingsConfig = ready.value,
            )
        },
    )
}

/** The permission catalog: one titled group per category. */
@Composable
fun PermissionsContent(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        settingsConfig.categories.forEachIndexed { categoryIndex, category ->
            val categoryKey = category.title ?: "category_$categoryIndex"

            item(
                key = "permission_category_$categoryKey",
                contentType = "permission_category",
            ) {
                category.title?.let { title ->
                    PreferenceCategoryItem(title = title)
                }
            }

            itemsIndexed(
                items = category.preferences,
                key = { index, preference ->
                    val preferenceKey = preference.key ?: preference.title ?: index
                    "permission_${categoryKey}_$preferenceKey"
                },
                contentType = { _, _ -> "permission_preference" },
            ) { index, preference ->
                SettingsPreferenceItem(
                    title = preference.title,
                    summary = preference.summary,
                    onClick = { preference.action?.invoke() },
                    modifier = Modifier.groupedPreferenceItem(
                        position = groupedItemPosition(
                            index = index,
                            size = category.preferences.size
                        ),
                        outerRadius = SizeConstants.LargeMediumSize,
                    )
                )
            }
        }
    }
}

// Only the loaded and loading states have previews: the empty and failure states draw
// `NoDataScreen`, which resolves its ad unit through Koin.
@Preview(showBackground = true)
@Composable
private fun PermissionsScreenContentPreview() {
    MaterialTheme {
        PermissionsScreenContent(
            state = PermissionsUiState(
                config = Loadable.Ready(
                    SettingsConfig(
                        title = "Permissions",
                        categories = listOf(
                            SettingsCategory(
                                title = "Normal",
                                preferences = listOf(
                                    SettingsPreference(
                                        title = "Internet",
                                        summary = "Loads the changelog and help pages.",
                                    ),
                                    SettingsPreference(
                                        title = "Billing",
                                        summary = "Lets you support the app.",
                                    ),
                                ),
                            ),
                            SettingsCategory(
                                title = "Runtime",
                                preferences = listOf(
                                    SettingsPreference(
                                        title = "Notifications",
                                        summary = "Shows reminders you ask for.",
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
            contentPadding = PaddingValues(),
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionsScreenContentLoadingPreview() {
    MaterialTheme {
        PermissionsScreenContent(
            state = PermissionsUiState(config = Loadable.Loading),
            contentPadding = PaddingValues(),
            onRetry = {},
        )
    }
}
