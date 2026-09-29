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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val SETTINGS_SCREEN_NAME = "Settings"
private const val SETTINGS_SCREEN_CLASS = "SettingsScreen"
private const val UNKNOWN_PREFERENCE_KEY = "unknown"

private object SettingsActionNames {
    const val RETRY_LOAD: String = "retry_load"
}

/**
 * The settings list: the categories the host's `SettingsProvider` supplies.
 *
 * The body of the settings page, a list page: on wide windows the page a row opens sits beside it
 * with a draggable separator, and on phones it opens over it. A row opens its `destination`, after
 * giving its `action` the chance to handle the click outside the app.
 */
@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<SettingsConfig> by viewModel.uiState.collectAsStateWithLifecycle()
    val navigator = LocalShellNavigator.current
    val paddingValues = contentPadding()

    val firebaseController: FirebaseController = koinInject()
    TrackScreenView(
        firebaseController = firebaseController,
        screenName = SETTINGS_SCREEN_NAME,
        screenClass = SETTINGS_SCREEN_CLASS,
    )
    TrackScreenState(
        firebaseController = firebaseController,
        screenName = SETTINGS_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(event = SettingsEvent.Load)
    }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = {
            NoDataScreen(
                icon = Icons.Outlined.Settings,
                showRetry = true,
                onRetry = {
                    firebaseController.logGa4Event(
                        ga4Event = settingsActionGa4Event(actionName = SettingsActionNames.RETRY_LOAD)
                    )
                    viewModel.onEvent(event = SettingsEvent.Load)
                },
                paddingValues = paddingValues,
            )
        },
        onSuccess = { config: SettingsConfig ->
            SettingsList(
                paddingValues = paddingValues,
                settingsConfig = config,
                firebaseController = firebaseController,
                onPreferenceClick = { preference ->
                    if (preference.action?.invoke() != true) {
                        preference.destination?.let(navigator::navigate)
                    }
                },
            )
        },
    )
}

@Composable
fun SettingsList(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
    firebaseController: FirebaseController,
    onPreferenceClick: (SettingsPreference) -> Unit = {},
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        settingsConfig.categories.forEachIndexed { categoryIndex: Int, category: SettingsCategory ->
            if (category.preferences.isNotEmpty()) {
                item(key = "settings_category_spacing_$categoryIndex") {
                    LargeVerticalSpacer()
                }

                itemsIndexed(
                    items = category.preferences,
                    key = { index: Int, preference: SettingsPreference ->
                        preference.key?.let { key ->
                            "settings_preference_${categoryIndex}_$key"
                        } ?: "settings_preference_${categoryIndex}_$index"
                    },
                ) { index: Int, preference: SettingsPreference ->
                    val position = groupedItemPosition(
                        index = index,
                        size = category.preferences.size,
                    )

                    SettingsPreferenceItem(
                        icon = preference.icon,
                        title = preference.title,
                        summary = preference.summary,
                        useIconContainer = preference.useIconContainer,
                        iconColor = preference.iconColor,
                        iconContainerColor = preference.iconContainerColor,
                        firebaseController = firebaseController,
                        ga4EventProvider = {
                            Ga4EventData(
                                name = SettingsAnalytics.Events.PREFERENCE_VIEW,
                                params = mapOf(
                                    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                        SETTINGS_SCREEN_NAME
                                    ),
                                    SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(
                                        preference.key ?: UNKNOWN_PREFERENCE_KEY
                                    ),
                                ),
                            )
                        },
                        onClick = { onPreferenceClick(preference) },
                        modifier = Modifier.groupedPreferenceItem(
                            position = position,
                            outerRadius = SizeConstants.ExtraLargeSize,
                        ),
                    )
                }
            }
        }
    }
}

private fun settingsActionGa4Event(actionName: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(SETTINGS_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}

