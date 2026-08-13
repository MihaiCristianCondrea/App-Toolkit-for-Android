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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui

import android.view.SoundEffectConstants
import android.view.View
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.utils.ISSUE_REPORTER_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.utils.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.views.DeviceInfoCard
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.views.IssueReportFormCard
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.views.IssueReporterAccountOptions
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.views.IssueSubmittedCard
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.SmallFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraExtraLargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ISSUE_REPORTER_SCREEN_CLASS = "IssueReporterScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueReporterScreen(onBackClicked: (() -> Unit)? = null) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val isFabExtended by remember {
        derivedStateOf { scrollBehavior.state.contentOffset >= 0f }
    }

    val firebaseController: FirebaseController = koinInject()
    val viewModel: IssueReporterViewModel = koinViewModel()

    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val uiStateScreen: UiStateScreen<IssueReporterUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    val target: GithubTarget = koinInject()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = ISSUE_REPORTER_SCREEN_NAME,
        screenClass = ISSUE_REPORTER_SCREEN_CLASS,
    )

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = ISSUE_REPORTER_SCREEN_NAME,
        screenState = uiStateScreen.screenState,
    )

    val defaultBackClicked: () -> Unit = remember(activity) { { activity?.finish() } }
    val backClicked: () -> Unit = remember(onBackClicked, defaultBackClicked, firebaseController) {
        {
            firebaseController.logEvent(
                issueReporterActionEvent(actionName = IssueReporterActionNames.BACK_CLICK)
            )
            (onBackClicked ?: defaultBackClicked).invoke()
        }
    }

    val issuesUrl = remember(target) {
        "https://github.com/${target.username}/${target.repository}/issues"
    }

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.bug_report),
        onBackClicked = backClicked,
        snackbarHostState = snackBarHostState,
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                SmallFloatingActionButton(
                    modifier = Modifier.padding(bottom = SizeConstants.MediumSize),
                    isVisible = true,
                    isExtended = true,
                    icon = Icons.Outlined.Link,
                    onClick = {
                        firebaseController.logEvent(
                            issueReporterActionEvent(actionName = IssueReporterActionNames.OPEN_ISSUES_LIST)
                        )
                        context.openUrl(issuesUrl)
                    }
                )

                AnimatedExtendedFloatingActionButton(
                    visible = true,
                    expanded = isFabExtended,
                    onClick = {
                        firebaseController.logEvent(
                            issueReporterActionEvent(
                                actionName = IssueReporterActionNames.SEND_ISSUE,
                                params = mapOf(
                                    "title_length" to AnalyticsValue.LongVal(
                                        uiStateScreen.data?.title?.length?.toLong() ?: 0L
                                    ),
                                    "description_length" to AnalyticsValue.LongVal(
                                        uiStateScreen.data?.description?.length?.toLong() ?: 0L
                                    ),
                                    "has_email" to AnalyticsValue.Bool(!uiStateScreen.data?.email.isNullOrBlank()),
                                    "anonymous" to AnalyticsValue.Bool(
                                        uiStateScreen.data?.anonymous ?: true
                                    ),
                                ),
                            )
                        )
                        viewModel.onEvent(IssueReporterEvent.Send)
                    },
                    text = { Text(text = stringResource(id = R.string.issue_send)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.BugReport,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    ) { paddingValues: PaddingValues ->

        ScreenStateHandler(
            screenState = uiStateScreen,
            onLoading = { LoadingScreen() },
            onEmpty = { NoDataScreen(paddingValues = paddingValues) },
            onError = { NoDataScreen(isError = true, paddingValues = paddingValues) },
            onSuccess = { data: IssueReporterUiState ->
                IssueReporterScreenContent(
                    firebaseController = firebaseController,
                    paddingValues = paddingValues,
                    onEvent = viewModel::onEvent,
                    data = data
                )
            }
        )

        DefaultSnackbarHandler(
            screenState = uiStateScreen,
            snackbarHostState = snackBarHostState,
            getDismissEvent = { IssueReporterEvent.DismissSnackbar },
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun IssueReporterScreenContent(
    firebaseController: FirebaseController,
    paddingValues: PaddingValues,
    onEvent: (IssueReporterEvent) -> Unit,
    data: IssueReporterUiState,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding(),
            start = SizeConstants.LargeSize,
            end = SizeConstants.LargeSize
        ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        item {
            if (!data.issueUrl.isNullOrEmpty()) {
                IssueSubmittedCard(
                    issueUrl = data.issueUrl,
                    firebaseController = firebaseController,
                )
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.issue_section_label),
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            IssueReportFormCard(data = data, onEvent = onEvent)
        }

        item {
            Text(
                text = stringResource(id = R.string.login_section_label),
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            IssueReporterAccountOptions(
                anonymous = data.anonymous,
                firebaseController = firebaseController,
                onEvent = onEvent,
            )
        }

        item {
            DeviceInfoCard(
                deviceInfoText = data.deviceInfoText,
                firebaseController = firebaseController,
                onExpandRequested = { onEvent(IssueReporterEvent.RequestDeviceInfo) },
            )

            repeat(2) {
                ExtraExtraLargeVerticalSpacer()
            }
        }
    }
}


