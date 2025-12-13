package com.d4rk.android.libs.apptoolkit.app.help.ui

import android.content.Context
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    activity: ComponentActivity,
    config: HelpScreenConfig,
    scope: CoroutineScope,
    viewModel: HelpViewModel
) { // FIXME: Unstable parameter 'activity' prevents composable from being skippable && Parameter 'scope' has runtime-determined stability && Parameter 'viewModel' has runtime-determined stability
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())
    val context: Context = LocalContext.current
    val isFabExtended: MutableState<Boolean> = remember { mutableStateOf(value = true) }
    val screenState: UiStateScreen<UiHelpScreen> by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(HelpEvent.LoadFaq)
    }

    LaunchedEffect(key1 = scrollBehavior.state.contentOffset) {
        isFabExtended.value = scrollBehavior.state.contentOffset >= 0f
    }

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.help),
        onBackClicked = { activity.finish() },
        actions = {
            HelpScreenMenuActions(
                context = context,
                activity = activity,
                showDialog = remember { mutableStateOf(value = false) },
                config = config
            )
        },
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
            AnimatedExtendedFloatingActionButton(
                visible = true,
                expanded = isFabExtended.value,
                onClick = {
                    ReviewHelper.forceLaunchInAppReview(activity = activity, scope = scope)
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
                HelpScreenContent(questions = data.questions, paddingValues = paddingValues, activity = activity)
            }
        )
    }
}

@Composable
fun HelpScreenContent(
    questions: List<UiHelpQuestion>,
    paddingValues: PaddingValues,
    activity: ComponentActivity
) { // FIXME: Unstable parameter 'activity' prevents composable from being skippable && Parameter 'questions' has runtime-determined stability
    val adsConfig: AdsConfig = koinInject(qualifier = named("help_large_banner_ad"))
    LazyColumn(
        modifier = Modifier.fillMaxSize() , contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() , bottom = paddingValues.calculateBottomPadding() , start = SizeConstants.LargeSize , end = SizeConstants.LargeSize
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
                adsConfig = adsConfig,
                modifier = Modifier.animateItem()
            )
        }
        item {
            ContactUsCard(onClick = {
                IntentsHelper.sendEmailToDeveloper(context = activity , applicationNameRes = R.string.app_name)
            })
            repeat(3) {
                ExtraLargeVerticalSpacer()
            }
        }
    }
}
