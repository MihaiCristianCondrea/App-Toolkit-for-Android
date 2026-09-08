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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.ISSUE_REPORTER_SCREEN_NAME
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.IssueReportForm
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.DeviceInfoContentCard
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.DeviceInfoHeaderCard
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views.IssueSubmittedCard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraExtraLargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ISSUE_REPORTER_SCREEN_CLASS = "IssueReporterScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueReporterScreen(onBackClicked: (() -> Unit)? = null) {
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

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.bug_report),
        onBackClicked = backClicked,
        snackbarHostState = snackBarHostState,
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
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
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
    ) {
        if (!data.issueUrl.isNullOrEmpty()) {
            IssueSubmittedCard(
                issueUrl = data.issueUrl,
                firebaseController = firebaseController,
                modifier = Modifier.padding(horizontal = SizeConstants.LargeSize)
            )
        }

        IssueReportForm(data = data, onEvent = onEvent)

        LargeVerticalSpacer()

        DeviceInfoHeaderCard(
            firebaseController = firebaseController,
            onExpandRequested = { onEvent(IssueReporterEvent.RequestDeviceInfo) },
        )

        DeviceInfoContentCard(
            deviceInfoText = data.deviceInfoText,
        )

        repeat(2) {
            ExtraExtraLargeVerticalSpacer()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IssueReporterScreenPreview() {
    val dummyData = IssueReporterUiState(
        title = "Sample Bug Title",
        description = "This is a detailed description of the bug encounter in the sample application.",
        email = "user@example.com",
        deviceInfoText = "Device: Pixel 7\nOS: Android 14\nApp Version: 1.0.0"
    )
    val dummyController = object : FirebaseController {
        override fun updateConsent(analyticsGranted: Boolean, adStorageGranted: Boolean, adUserDataGranted: Boolean, adPersonalizationGranted: Boolean) {}
        override fun setAnalyticsEnabled(enabled: Boolean) {}
        override fun setCrashlyticsEnabled(enabled: Boolean) {}
        override fun setPerformanceEnabled(enabled: Boolean) {}
        override fun logBreadcrumb(message: String, attributes: Map<String, String>) {}
        override fun reportViewModelError(viewModelName: String, action: String, throwable: Throwable, extraKeys: Map<String, String>) {}
        override fun recordNonFatal(throwable: Throwable, attributes: Map<String, String>) {}
        override fun logEvent(event: AnalyticsEvent) {}
        override fun logScreenView(screenName: String, screenClass: String?) {}
        override fun setUserProperty(name: String, value: String?) {}
    }
    MaterialTheme {
        IssueReporterScreenContent(
            firebaseController = dummyController,
            paddingValues = PaddingValues(16.dp),
            onEvent = {},
            data = dummyData
        )
    }
}


