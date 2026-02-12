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

package com.d4rk.android.libs.apptoolkit.app.advanced.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.contract.AdvancedSettingsEvent
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.state.AdvancedSettingsUiState
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.IssueReporterActivity
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openActivity
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ADVANCED_SETTINGS_SCREEN_NAME = "AdvancedSettings"
private const val ADVANCED_SETTINGS_SCREEN_CLASS = "AdvancedSettingsList"

/**
 * A Composable function that displays a list of advanced settings.
 *
 * This screen fetches its state from [AdvancedSettingsViewModel] and handles different states
 * such as loading, empty, and success. On success, it displays a list of settings categorized
 * into "Error Reporting" and "Cache Management".
 *
 * It includes options to:
 * - Navigate to an issue reporter screen.
 * - Clear the application's cache, showing a toast message upon completion.
 *
 * @param paddingValues The padding values to be applied to the root layout of the list,
 * typically provided by a Scaffold. Defaults to an empty `PaddingValues`.
 */
@Composable
fun AdvancedSettingsList(
    paddingValues: PaddingValues = PaddingValues(),
) {
    val viewModel: AdvancedSettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<AdvancedSettingsUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    val firebaseController: FirebaseController = koinInject()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = ADVANCED_SETTINGS_SCREEN_NAME,
        screenClass = ADVANCED_SETTINGS_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = ADVANCED_SETTINGS_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    val context = LocalContext.current
    val appContext = remember(context) { context.applicationContext }

    val messageRes: Int? = screenState.data?.cacheClearMessage
    val toastText: String? = messageRes?.let { stringResource(id = it) }

    LaunchedEffect(messageRes) {
        toastText?.let {
            Toast.makeText(appContext, toastText, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(AdvancedSettingsEvent.MessageShown)
        }
    }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = { NoDataScreen(paddingValues = paddingValues) },
        onError = { NoDataScreen(isError = true, paddingValues = paddingValues) },
        onSuccess = {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxHeight()
            ) {
                item {
                    PreferenceCategoryItem(title = stringResource(id = R.string.error_reporting))
                    SmallVerticalSpacer()
                    Column(
                        modifier = Modifier
                            .padding(horizontal = SizeConstants.LargeSize)
                            .clip(RoundedCornerShape(size = SizeConstants.LargeSize))
                    ) {
                        SettingsPreferenceItem(
                            title = stringResource(id = R.string.bug_report),
                            summary = stringResource(id = R.string.summary_preference_settings_bug_report),
                            onClick = {
                                context.openActivity(IssueReporterActivity::class.java)
                            },
                        )
                    }
                }

                item {
                    PreferenceCategoryItem(title = stringResource(id = R.string.cache_management))
                    SmallVerticalSpacer()
                    Column(
                        modifier = Modifier
                            .padding(horizontal = SizeConstants.LargeSize)
                            .clip(RoundedCornerShape(size = SizeConstants.LargeSize))
                    ) {
                        SettingsPreferenceItem(
                            title = stringResource(id = R.string.clear_cache),
                            summary = stringResource(id = R.string.summary_preference_settings_clear_cache),
                            onClick = { viewModel.onEvent(AdvancedSettingsEvent.ClearCache) },
                        )
                    }
                }
            }
        },
    )
}