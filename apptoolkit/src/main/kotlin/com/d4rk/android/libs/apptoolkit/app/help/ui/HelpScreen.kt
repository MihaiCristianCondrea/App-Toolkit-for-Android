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

package com.d4rk.android.libs.apptoolkit.app.help.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpAction
import com.d4rk.android.libs.apptoolkit.app.help.ui.contract.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.ui.state.HelpUiState
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.content.HelpScreenContent
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.dropdowns.HelpScreenMenuActions
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.utils.constants.analytics.SettingsAnalytics
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.activity.isInAppReviewAvailable
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.findActivity
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val HELP_SCREEN_NAME = "Help"
private const val HELP_SCREEN_CLASS = "HelpScreen"

private object HelpPreferenceKeys {
    const val REVIEW_OR_ONLINE_HELP: String = "review_or_online_help"
    const val FAQ_ITEM: String = "faq_item"
    const val CONTACT_US: String = "contact_us"
}

private object HelpActionNames {
    const val BACK_CLICK: String = "back_click"
    const val RETRY_LOAD: String = "retry_load"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    config: AppVersionInfo,
) {
    val viewModel: HelpViewModel = koinViewModel()
    val firebaseController: FirebaseController = koinInject()

    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val isInAppReviewAvailable = rememberSaveable { mutableStateOf(false) }
    val reviewHost = remember(activity) {
        activity?.let { hostActivity ->
            object : ReviewHost {
                override val activity = hostActivity
            }
        }
    }
    val scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())
    val isFabExtended = rememberSaveable { mutableStateOf(true) }
    val showDialog = rememberSaveable { mutableStateOf(false) }

    val screenState: UiStateScreen<HelpUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = HELP_SCREEN_NAME,
        screenClass = HELP_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = HELP_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    LaunchedEffect(Unit) {
        isInAppReviewAvailable.value =
            activity?.isInAppReviewAvailable() ?: false
    }

    LaunchedEffect(Unit) {
        viewModel.actionEvent.collect { action ->
            when (action) {
                is HelpAction.OpenOnlineHelp -> context.openUrl(action.url)
                is HelpAction.ReviewOutcomeReported -> Unit
            }
        }
    }

    LaunchedEffect(scrollBehavior) {
        snapshotFlow { scrollBehavior.state.contentOffset >= 0f }
            .distinctUntilChanged()
            .collect { extended ->
                isFabExtended.value = extended
            }
    }

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.help),
        onBackClicked = {
            firebaseController.logEvent(
                helpActionEvent(actionName = HelpActionNames.BACK_CLICK)
            )
            activity?.finish()
        },
        actions = {
            HelpScreenMenuActions(
                config = config,
                showDialog = showDialog.value,
                onShowDialogChange = { showDialog.value = it }
            )
        },
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
            AnimatedExtendedFloatingActionButton(
                visible = true,
                expanded = isFabExtended.value,
                onClick = {
                    reviewHost?.let { host ->
                        viewModel.onEvent(HelpEvent.RequestReview(host = host))
                    }
                },
                firebaseController = firebaseController,
                ga4Event = helpPreferenceTapEvent(preferenceKey = HelpPreferenceKeys.REVIEW_OR_ONLINE_HELP),
                text = {
                    val textRes = if (isInAppReviewAvailable.value) {
                        R.string.feedback
                    } else {
                        R.string.online_help
                    }
                    Text(text = stringResource(id = textRes))
                },
                icon = {
                    val icon = if (isInAppReviewAvailable.value) {
                        Icons.Outlined.RateReview
                    } else {
                        Icons.AutoMirrored.Outlined.ContactSupport
                    }
                    Icon(imageVector = icon, contentDescription = null)
                }
            )
        }
    ) { paddingValues ->
        ScreenStateHandler(
            screenState = screenState,
            onLoading = { LoadingScreen() },
            onEmpty = {
                NoDataScreen(
                    showRetry = true,
                    onRetry = {
                        firebaseController.logEvent(helpActionEvent(actionName = HelpActionNames.RETRY_LOAD))
                        viewModel.onEvent(HelpEvent.LoadFaq)
                    },
                    paddingValues = paddingValues
                )
            },
            onError = {
                NoDataScreen(
                    isError = true,
                    showRetry = true,
                    onRetry = {
                        firebaseController.logEvent(helpActionEvent(actionName = HelpActionNames.RETRY_LOAD))
                        viewModel.onEvent(HelpEvent.LoadFaq)
                    },
                    paddingValues = paddingValues
                )
            },
            onSuccess = { data: HelpUiState ->
                HelpScreenContent(
                    questions = data.questions,
                    paddingValues = paddingValues,
                    firebaseController = firebaseController,
                )
            }
        )
    }
}


private fun helpPreferenceTapEvent(preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(HELP_SCREEN_NAME),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}

private fun helpActionEvent(actionName: String): AnalyticsEvent {
    return AnalyticsEvent(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(HELP_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}
