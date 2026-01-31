package com.d4rk.android.libs.apptoolkit.app.startup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.startup.ui.state.StartupUiState
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.TopAppBarScaffold
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import org.koin.compose.koinInject

private const val STARTUP_SCREEN_NAME = "Startup"
private const val STARTUP_SCREEN_CLASS = "StartupScreen"

@Composable
fun StartupScreen(
    screenState: UiStateScreen<StartupUiState>,
    onContinueClick: () -> Unit,
) {
    val firebaseController: FirebaseController = koinInject()

    TrackScreenView(
        firebaseController = firebaseController,
        screenName = STARTUP_SCREEN_NAME,
        screenClass = STARTUP_SCREEN_CLASS,
    )
    TrackScreenState(
        firebaseController = firebaseController,
        screenName = STARTUP_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = { NoDataScreen() },
        onSuccess = { data: StartupUiState ->
            TopAppBarScaffold(
                title = stringResource(R.string.welcome),
                content = { paddingValues ->
                    StartupScreenContent(paddingValues = paddingValues)
                },
                floatingActionButton = {
                    AnimatedExtendedFloatingActionButton(
                        visible = data.consentFormLoaded,
                        modifier = Modifier.bounceClick(),
                        containerColor = if (data.consentFormLoaded) {
                            FloatingActionButtonDefaults.containerColor
                        } else {
                            Gray
                        },
                        text = { Text(text = stringResource(id = R.string.agree)) },
                        onClick = onContinueClick,
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                            )
                        },
                    )
                },
            )
        },
    )
}

@Composable
fun StartupScreenContent(
    paddingValues: PaddingValues,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_startup),
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            composition = composition,
            restartOnPlay = true,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Crop,
            speed = 1.2f,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(all = SizeConstants.MediumSize * 2)
                .safeDrawingPadding(),
            verticalArrangement = Arrangement.spacedBy(space = SizeConstants.LargeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                AsyncImage(
                    model = R.drawable.il_startup,
                    contentDescription = null,
                )
                InfoMessageSection(
                    message = stringResource(R.string.summary_browse_terms_of_service_and_privacy_policy),
                    learnMoreText = stringResource(R.string.learn_more),
                    learnMoreUrl = AppLinks.PRIVACY_POLICY,
                )
            }
        }
    }
}