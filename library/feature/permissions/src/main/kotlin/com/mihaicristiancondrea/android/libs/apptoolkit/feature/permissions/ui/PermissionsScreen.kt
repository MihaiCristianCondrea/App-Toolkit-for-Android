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

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.contracts.PermissionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.domain.models.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val PERMISSIONS_SCREEN_NAME = "Permissions"
private const val PERMISSIONS_SCREEN_CLASS = "PermissionsScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    isEmbedded: Boolean = false,
) {
    val viewModel: PermissionsViewModel = koinViewModel()
    val screenState: UiStateScreen<SettingsConfig> by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val firebaseController: FirebaseController = koinInject()
    TrackScreenView(
        firebaseController = firebaseController,
        screenName = PERMISSIONS_SCREEN_NAME,
        screenClass = PERMISSIONS_SCREEN_CLASS,
    )
    TrackScreenState(
        firebaseController = firebaseController,
        screenName = PERMISSIONS_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(PermissionsEvent.Load)
    }

    val content: @Composable (PaddingValues) -> Unit = { paddingValues ->
        ScreenStateHandler(
            screenState = screenState,
            onLoading = { LoadingScreen() },
            onEmpty = {
                NoDataScreen(
                    icon = Icons.Outlined.Settings,
                    showRetry = true,
                    onRetry = { viewModel.onEvent(PermissionsEvent.Load) },
                    paddingValues = paddingValues,
                )
            },
            onError = {
                NoDataScreen(
                    icon = Icons.Outlined.Settings,
                    isError = true,
                    showRetry = true,
                    onRetry = { viewModel.onEvent(PermissionsEvent.Load) },
                    paddingValues = paddingValues,
                )
            },
            onSuccess = { settingsConfig ->
                PermissionsContent(
                    paddingValues = paddingValues,
                    settingsConfig = settingsConfig,
                )
            },
        )
    }

    if (isEmbedded) {
        content(PaddingValues())
    } else {
        LargeTopAppBarWithScaffold(
            title = stringResource(id = R.string.permissions),
            onBackClicked = { (context as Activity).finish() },
            content = content
        )
    }
}


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
                    onClick = { preference.action.invoke() },
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
