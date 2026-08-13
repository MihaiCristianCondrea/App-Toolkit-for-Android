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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.ui

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui.HelpActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsContent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.contracts.GeneralSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.domain.models.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.domain.models.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.domain.models.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.providers.GeneralSettingsContentProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.utils.extensions.context.openActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.rememberWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val SETTINGS_SCREEN_NAME = "Settings"
private const val SETTINGS_SCREEN_CLASS = "SettingsScreen"
private const val UNKNOWN_PREFERENCE_KEY = "unknown"

private object SettingsActionNames {
    const val BACK_CLICK: String = "back_click"
    const val RETRY_LOAD: String = "retry_load"
    const val OPEN_HELP: String = "open_help"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isEmbedded: Boolean = false,
) {
    val viewModel: SettingsViewModel = koinViewModel()
    val contentProvider: GeneralSettingsContentProvider = koinInject()
    val screenState: UiStateScreen<SettingsConfig> by viewModel.uiState.collectAsStateWithLifecycle()
    val context: Context = LocalContext.current

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

    val content: @Composable (PaddingValues) -> Unit = { paddingValues ->
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
                SettingsScreenContent(
                    paddingValues = paddingValues,
                    settingsConfig = config,
                    contentProvider = contentProvider,
                    firebaseController = firebaseController,
                )
            },
        )
    }

    if (isEmbedded) {
        content(PaddingValues())
    } else {
        LargeTopAppBarWithScaffold(
            title = stringResource(id = R.string.settings),
            onBackClicked = {
                firebaseController.logGa4Event(
                    ga4Event = settingsActionGa4Event(actionName = SettingsActionNames.BACK_CLICK)
                )
                (context as? android.app.Activity)?.finish()
            },
            content = content
        )
    }
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
    contentProvider: GeneralSettingsContentProvider,
    firebaseController: FirebaseController,
) {
    val windowWidthSizeClass: AppWindowWidthSizeClass = rememberWindowWidthSizeClass()
    if (windowWidthSizeClass == AppWindowWidthSizeClass.Compact) {
        PhoneSettingsScreen(
            paddingValues = paddingValues,
            settingsConfig = settingsConfig,
            firebaseController = firebaseController,
        )
    } else {
        TabletSettingsScreen(
            paddingValues = paddingValues,
            settingsConfig = settingsConfig,
            contentProvider = contentProvider,
            firebaseController = firebaseController,
        )
    }
}

@Composable
fun PhoneSettingsScreen(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
    firebaseController: FirebaseController,
) {
    SettingsList(
        paddingValues = paddingValues,
        settingsConfig = settingsConfig,
        firebaseController = firebaseController,
        onPreferenceClick = { preference -> preference.action() },
    )
}

@Composable
fun TabletSettingsScreen(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
    contentProvider: GeneralSettingsContentProvider,
    firebaseController: FirebaseController,
) {
    var selected: SettingsPreference? by remember { mutableStateOf(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            SettingsList(
                paddingValues = paddingValues,
                settingsConfig = settingsConfig,
                firebaseController = firebaseController,
                onPreferenceClick = { selected = it },
            )
        }
        Box(modifier = Modifier.weight(2f)) {
            AnimatedContent(targetState = selected) { preference ->
                preference?.let {
                    SettingsDetail(
                        preference = it,
                        paddingValues = paddingValues,
                        contentProvider = contentProvider,
                    )
                } ?: SettingsDetailPlaceholder(paddingValues = paddingValues)
            }
        }
    }
}

@Composable
fun SettingsDetailPlaceholder(paddingValues: PaddingValues) {
    val context: Context = LocalContext.current
    val firebaseController: FirebaseController = koinInject()

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxHeight(),
    ) {
        item {
            Card(
                modifier = Modifier
                    .padding(top = SizeConstants.LargeSize, end = SizeConstants.LargeSize)
                    .fillMaxSize()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize),
            ) {
                Column(
                    modifier = Modifier.padding(all = SizeConstants.MediumSize * 2),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    AsyncImage(
                        model = R.drawable.il_settings,
                        contentDescription = null,
                        modifier = Modifier
                            .size(size = SizeConstants.TwoHundredFiftyEightSize)
                            .fillMaxWidth(),
                    )
                    LargeVerticalSpacer()
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                    SmallVerticalSpacer()
                    Text(
                        text = stringResource(id = R.string.settings_placeholder_description),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
                GeneralOutlinedButton(
                    modifier = Modifier
                        .padding(all = SizeConstants.MediumSize * 2)
                        .align(alignment = Alignment.Start),
                    onClick = {
                        context.openActivity(
                            HelpActivity::class.java,
                        )
                    },
                    vectorIcon = Icons.AutoMirrored.Outlined.ContactSupport,
                    label = stringResource(id = R.string.get_help),
                    firebaseController = firebaseController,
                    ga4Event = settingsActionGa4Event(actionName = SettingsActionNames.OPEN_HELP),
                )
            }
        }
    }
}

@Composable
fun SettingsDetail(
    preference: SettingsPreference,
    paddingValues: PaddingValues,
    contentProvider: GeneralSettingsContentProvider,
) {
    val viewModel: GeneralSettingsViewModel = koinViewModel()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val firebaseController: FirebaseController = koinInject()
    TrackScreenView(
        firebaseController = firebaseController,
        screenName = "GeneralSettings",
        screenClass = preference.key,
    )

    LaunchedEffect(key1 = preference.key) {
        viewModel.onEvent(event = GeneralSettingsEvent.Load(contentKey = preference.key))
    }

    val uiStateScreen = viewModel.uiState.collectAsState().value

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = "GeneralSettings",
        screenState = uiStateScreen.screenState,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        GeneralSettingsContent(
            screenState = uiStateScreen,
            contentProvider = contentProvider,
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
        )
    }
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

