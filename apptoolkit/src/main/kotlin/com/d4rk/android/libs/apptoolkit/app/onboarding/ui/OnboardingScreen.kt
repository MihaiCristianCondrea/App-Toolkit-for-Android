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

package com.d4rk.android.libs.apptoolkit.app.onboarding.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingAction
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.contract.OnboardingEvent
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.model.OnboardingPage
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.state.OnboardingUiState
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.controls.OnboardingFooter
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.default.DefaultOnboardingPage
import com.d4rk.android.libs.apptoolkit.app.onboarding.utils.interfaces.providers.OnboardingProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.hapticPagerSwipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ONBOARDING_SCREEN_NAME = "Onboarding"
private const val ONBOARDING_SCREEN_CLASS = "OnboardingScreen"


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen() {
    val context = LocalContext.current
    val firebaseController: FirebaseController = koinInject()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = ONBOARDING_SCREEN_NAME,
        screenClass = ONBOARDING_SCREEN_CLASS,
    )

    val onboardingProvider: OnboardingProvider = koinInject()
    val pages: List<OnboardingPage> =
        remember { onboardingProvider.getOnboardingPages(context = context) }

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: OnboardingViewModel = koinViewModel()
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState = screenState.data ?: OnboardingUiState()

    TrackScreenState(
        firebaseController = firebaseController,
        screenName = ONBOARDING_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    val pagerState: PagerState =
        rememberPagerState(initialPage = uiState.currentTabIndex) { pages.size }

    BackHandler(enabled = pages.isNotEmpty() && pagerState.currentPage > 0) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onEvent(OnboardingEvent.UpdateCurrentTab(pagerState.currentPage))
    }

    LaunchedEffect(Unit) {
        viewModel.actionEvent.collect { action ->
            when (action) {
                OnboardingAction.OnboardingCompleted -> onboardingProvider.onOnboardingFinished(
                    context
                )
            }
        }
    }

    val onSkipRequested = {
        viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
    }

    Scaffold(
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
                            GeneralOutlinedButton(
                                onClick = { onSkipRequested() },
                                vectorIcon = Icons.Filled.SkipNext,
                                iconContentDescription = stringResource(id = R.string.skip_button_content_description),
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
                            viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .hapticPagerSwipe(pagerState = pagerState)
                .padding(paddingValues = paddingValues)
        ) { pageIndex: Int ->
            when (val page = pages[pageIndex]) {
                is OnboardingPage.DefaultPage -> DefaultOnboardingPage(page = page)
                is OnboardingPage.CustomPage -> page.content(pageIndex == pagerState.currentPage)
            }
        }
    }
}
