package com.d4rk.android.libs.apptoolkit.app.help.ui

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.actions.HelpEvent
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.HelpScreenConfig
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.UiHelpQuestion
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.ui.UiHelpScreen
import com.d4rk.android.libs.apptoolkit.app.help.ui.components.ContactUsCard
import com.d4rk.android.libs.apptoolkit.app.help.ui.components.HelpQuestionsList
import com.d4rk.android.libs.apptoolkit.app.help.ui.components.dropdown.HelpScreenMenuActions
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.ads.HelpNativeAdCard
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab.AnimatedExtendedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraLargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ReviewHelper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named


// TODO: Move in helper class:
private tailrec fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    config: HelpScreenConfig,
) {
    val viewModel: HelpViewModel = koinViewModel()
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val scope = rememberCoroutineScope()

    val scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    val isFabExtended = rememberSaveable { mutableStateOf(true) }
    val showDialog = rememberSaveable { mutableStateOf(false) }

    val screenState: UiStateScreen<UiHelpScreen> by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(HelpEvent.LoadFaq)
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
        onBackClicked = { activity?.finish() },
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
                    // In previews activity can be null.
                    activity?.let {
                        ReviewHelper.forceLaunchInAppReview(activity = it, scope = scope)
                    }
                },
                text = { Text(text = stringResource(id = R.string.feedback)) },
                icon = { Icon(Icons.Outlined.RateReview, contentDescription = null) }
            )
        }
    ) { paddingValues ->
        ScreenStateHandler(
            screenState = screenState,
            onLoading = { LoadingScreen() },
            onEmpty = {
                NoDataScreen(
                    showRetry = true,
                    onRetry = { viewModel.onEvent(HelpEvent.LoadFaq) },
                    paddingValues = paddingValues
                )
            },
            onError = {
                NoDataScreen(
                    isError = true,
                    showRetry = true,
                    onRetry = { viewModel.onEvent(HelpEvent.LoadFaq) },
                    paddingValues = paddingValues
                )
            },
            onSuccess = { data: UiHelpScreen ->
                HelpScreenContent(
                    questions = data.questions,
                    paddingValues = paddingValues
                )
            }
        )
    }
}

@Composable
fun HelpScreenContent(
    questions: ImmutableList<UiHelpQuestion>,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val adsConfig: AdsConfig = koinInject(qualifier = named("help_large_banner_ad"))

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding(),
            start = SizeConstants.LargeSize,
            end = SizeConstants.LargeSize
        ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)
    ) {
        item {
            Text(text = stringResource(id = R.string.popular_help_resources))
        }

        item {
            HelpQuestionsList(questions = questions)
        }

        item {
            HelpNativeAdCard(
                adUnitId = adsConfig.bannerAdUnitId,
                modifier = Modifier.animateItem()
            )
        }

        item {
            ContactUsCard(
                onClick = {
                    IntentsHelper.sendEmailToDeveloper(
                        context = context,
                        applicationNameRes = R.string.app_name
                    )
                }
            )
            repeat(3) { ExtraLargeVerticalSpacer() }
        }
    }
}