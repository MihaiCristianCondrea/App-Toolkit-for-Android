package com.d4rk.android.libs.apptoolkit.app.oboarding.ui

import android.app.Activity
import android.view.SoundEffectConstants
import android.view.View
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.OutlinedIconButtonWithText
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.oboarding.domain.data.model.ui.OnboardingPage
import com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components.OnboardingBottomNavigation
import com.d4rk.android.libs.apptoolkit.app.oboarding.ui.components.pages.OnboardingDefaultPageLayout
import com.d4rk.android.libs.apptoolkit.app.oboarding.utils.interfaces.providers.OnboardingProvider
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.hapticPagerSwipe
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(activity : Activity) {
    val onboardingProvider: OnboardingProvider = koinInject()
    val pages: List<OnboardingPage> =
        remember { onboardingProvider.getOnboardingPages(context = activity) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: OnboardingViewModel = viewModel()
    val pagerState: PagerState = rememberPagerState(
        initialPage = viewModel.currentTabIndex,
    ) { pages.size }
    LaunchedEffect(pagerState.currentPage) {
        viewModel.currentTabIndex = pagerState.currentPage
    }
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = activity)
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current
    val onSkipRequested = {
        coroutineScope.launch {
            dataStore.saveStartup(isFirstTime = false)
        }
        onboardingProvider.onOnboardingFinished(context = activity)
    }

    Scaffold(topBar = {
        if (pages.isNotEmpty()) {
            TopAppBar(title = { }, actions = {
                AnimatedVisibility(
                    visible = pagerState.currentPage < pages.size - 1,
                    enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
                ) {
                    OutlinedIconButtonWithText(
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                            onSkipRequested()
                        },
                        icon = Icons.Filled.SkipNext,
                        iconContentDescription = stringResource(id = R.string.skip_button_content_description),
                        label = stringResource(id = R.string.skip_button_text)
                    )
                }
            })
        }
    }, bottomBar = {
        if (pages.isNotEmpty()) {
            OnboardingBottomNavigation(
                pagerState = pagerState,
                pageCount = pages.size,
                onNextClicked = {
                    if (pagerState.currentPage < pages.size - 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        coroutineScope.launch {
                            dataStore.saveStartup(isFirstTime = false)
                            onboardingProvider.onOnboardingFinished(activity)
                        }
                    }
                },
                onBackClicked = {
                    if (pagerState.currentPage > 0) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                })
        }
    }) { paddingValues: PaddingValues ->
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize()
                .hapticPagerSwipe(pagerState = pagerState)
                .padding(paddingValues = paddingValues)
        ) { pageIndex: Int ->
            when (val page = pages[pageIndex]) {
                is OnboardingPage.DefaultPage -> OnboardingDefaultPageLayout(page = page)
                is OnboardingPage.CustomPage -> page.content()
            }
        }
    }
}