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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.hapticPagerSwipe
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalPageSnackbarHostState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.models.OnboardingPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.providers.OnboardingProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingCompletion
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.views.controls.OnboardingFooter
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.views.pages.default.DefaultOnboardingPage
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ONBOARDING_SCREEN_NAME = "Onboarding"
private const val ONBOARDING_SCREEN_CLASS = "OnboardingScreen"

/** How wide an onboarding page grows on a large window; it is centred in the rest. */
internal val OnboardingContentMaxWidth: Dp = 640.dp

/**
 * The onboarding start screen: the host's pages from [OnboardingProvider], with Skip, Back and
 * Next. There is no shell under it, so back on the first page leaves the app.
 *
 * Consent is requested on every resume. Once completion is saved the screen enters the shell for
 * good. A page's own ViewModel shows its messages through this screen's snackbar host.
 */
@Composable
fun OnboardingScreen() {
    val viewModel: OnboardingViewModel = koinViewModel()
    val state: OnboardingUiState by viewModel.state.collectAsStateWithLifecycle()
    val onboardingProvider: OnboardingProvider = koinInject()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val navigator = LocalShellNavigator.current
    val pages: ImmutableList<OnboardingPage> = remember {
        onboardingProvider.getOnboardingPages(context = context).toImmutableList()
    }
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    TrackScreenView(
        screenName = ONBOARDING_SCREEN_NAME,
        screenClass = ONBOARDING_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = ONBOARDING_SCREEN_NAME,
        state = state.completion,
    )

    LifecycleResumeEffect(Unit) {
        activity?.let { viewModel.onEvent(OnboardingEvent.RequestConsent(host = ConsentHost(activity = it))) }
        onPauseOrDispose { }
    }

    LaunchedEffect(state.completion) {
        if (state.completion == OnboardingCompletion.Saved) navigator.enterShell()
    }

    OnboardingScreenContent(
        state = state,
        pages = pages,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
    )

    MessageHost(
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
        drawHost = false,
    )
}

/**
 * Renders [pages] in a pager with Skip, Back and Next, and reports the selected page and finishing
 * through [onEvent]. On a large window each page keeps a readable width, centred, instead of
 * stretching across it.
 *
 * @param onEvent Receives the events [OnboardingViewModel] handles.
 * @param snackbarHostState The host the scaffold draws, provided to the pages as
 * [LocalPageSnackbarHostState] so their messages show here too.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun OnboardingScreenContent(
    state: OnboardingUiState,
    pages: ImmutableList<OnboardingPage>,
    onEvent: (OnboardingEvent) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val pagerState: PagerState = rememberPagerState(initialPage = state.currentTabIndex) { pages.size }

    BackHandler(enabled = pages.isNotEmpty() && pagerState.currentPage > 0) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onEvent(OnboardingEvent.PageSelected(index = pagerState.currentPage))
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { DefaultSnackbarHost(snackbarState = snackbarHostState) },
        topBar = {
            if (pages.isNotEmpty()) {
                TopAppBar(
                    title = { },
                    actions = {
                        AnimatedVisibility(
                            visible = pagerState.currentPage < pages.size - 1,
                            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
                            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
                        ) {
                            GeneralButton(
                                style = GeneralButtonStyle.Outlined,
                                onClick = { onEvent(OnboardingEvent.CompleteOnboarding) },
                                icon = ToolkitIcon.Vector(imageVector = Icons.Filled.SkipNext),
                                contentDescription = stringResource(id = R.string.skip_button_content_description),
                                label = stringResource(id = R.string.skip_button_text)
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (pages.isNotEmpty()) {
                OnboardingFooter(
                    pagerState = pagerState,
                    pageCount = pages.size,
                    onNextClicked = {
                        if (pagerState.currentPage < pages.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onEvent(OnboardingEvent.CompleteOnboarding)
                        }
                    },
                    onBackClicked = {
                        if (pagerState.currentPage > 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues: PaddingValues ->
        CompositionLocalProvider(LocalPageSnackbarHostState provides snackbarHostState) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .hapticPagerSwipe(pagerState = pagerState)
                    .padding(paddingValues = paddingValues)
            ) { pageIndex: Int ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .widthIn(max = OnboardingContentMaxWidth),
                    ) {
                        when (val page = pages[pageIndex]) {
                            is OnboardingPage.DefaultPage -> DefaultOnboardingPage(page = page)
                            is OnboardingPage.CustomPage -> page.content(pageIndex == pagerState.currentPage)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenContentPreview() {
    MaterialTheme {
        OnboardingScreenContent(
            state = OnboardingUiState(),
            pages = persistentListOf(
                OnboardingPage.DefaultPage(
                    key = "welcome",
                    title = "Welcome",
                    description = "A short tour of what the app can do.",
                    imageVector = Icons.Outlined.Star,
                ),
                OnboardingPage.DefaultPage(
                    key = "theme",
                    title = "Make it yours",
                    description = "Pick a theme and a palette.",
                    imageVector = Icons.Outlined.Palette,
                ),
            ),
            onEvent = {},
        )
    }
}
