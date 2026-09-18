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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.states.PrivacyUiState
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val PRIVACY_SCREEN_NAME = "Privacy"
private const val PRIVACY_SCREEN_CLASS = "PrivacySettingsList"

/**
 * Renders the privacy and legal preference entries owned by [PrivacyViewModel].
 *
 * @param paddingValues Content padding, typically supplied by the hosting scaffold.
 */
@Composable
fun PrivacySettingsList(
    paddingValues: PaddingValues = PaddingValues(),
) {
    val context: Context = LocalContext.current
    val viewModel: PrivacyViewModel = koinViewModel()
    val screenState: UiStateScreen<PrivacyUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    val firebaseController: FirebaseController = koinInject()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = PRIVACY_SCREEN_NAME,
        screenClass = PRIVACY_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = PRIVACY_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    LaunchedEffect(viewModel) {
        viewModel.actionEvent.collect { action ->
            when (action) {
                is PrivacyAction.OpenUrl -> context.openUrl(action.url)
            }
        }
    }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = { NoDataScreen(paddingValues = paddingValues) },
        onSuccess = { data: PrivacyUiState ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
            ) {
                items(
                    items = data.items,
                    key = { it.key },
                ) { item ->
                    when (item) {
                        is PrivacyItem.Header -> {
                            PreferenceCategoryItem(title = item.title.asString())
                        }

                        is PrivacyItem.Preference -> {
                            SettingsPreferenceItem(
                                title = item.title.asString(),
                                summary = item.summary.asString(),
                                onClick = {
                                    viewModel.onEvent(
                                        event = PrivacyEvent.ItemClicked(action = item.action),
                                    )
                                },
                                firebaseController = firebaseController,
                                ga4Event = privacyPreferenceTapEvent(preferenceKey = item.key),
                                modifier = Modifier.groupedPreferenceItem(
                                    position = item.position,
                                    outerRadius = SizeConstants.LargeMediumSize,
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}

private fun privacyPreferenceTapEvent(preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(PRIVACY_SCREEN_NAME),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}
