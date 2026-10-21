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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.StateContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation.TopAppBarScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.contracts.StartupEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.providers.StartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.states.ConsentRequestStatus
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.states.StartupUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.OnboardingRoute
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val STARTUP_SCREEN_NAME = "Startup"
private const val STARTUP_SCREEN_CLASS = "StartupScreen"
private val STARTUP_SCREEN: TrackedScreen =
    TrackedScreen(name = STARTUP_SCREEN_NAME, className = STARTUP_SCREEN_CLASS)

/** How wide the screen grows on a large window, as onboarding's pages do; it is centred in the rest. */
private val StartupContentMaxWidth: Dp = 640.dp

/**
 * The startup start screen: the welcome illustration, the terms and privacy notice, and Agree,
 * which hands over to `OnboardingRoute`. There is no shell under it, so back leaves the app.
 *
 * Asks for [StartupProvider.requiredPermissions] once per screen instance. That decision is saved
 * across rotation and the resume the permission dialog causes, so a person who declined is not
 * asked again on the spot. Consent is asked for on every resume until it settles.
 */
@Composable
fun StartupScreen() {
    val viewModel: StartupViewModel = koinViewModel()
    val state: StartupUiState by viewModel.state.collectAsStateWithLifecycle()
    val provider: StartupProvider = koinInject()
    val navigator = LocalShellNavigator.current
    val activity = LocalActivity.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { }
    var hasRequestedPermissions by rememberSaveable { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        if (!hasRequestedPermissions && provider.requiredPermissions.isNotEmpty()) {
            hasRequestedPermissions = true
            permissionLauncher.launch(provider.requiredPermissions)
        }
        viewModel.onEvent(StartupEvent.RequestConsent(host = activity?.let(::ConsentHost)))
        onPauseOrDispose { }
    }

    ScreenHost(screen = STARTUP_SCREEN, status = state.consent) {
        StartupScreenContent(
            state = state,
            onContinue = { navigator.continueStart(OnboardingRoute) },
        )
    }
}

/**
 * Renders [state]: a loading screen until consent settles, then the welcome page with Agree.
 *
 * @param onContinue Agree was tapped. Navigation belongs to the caller.
 */
@Composable
internal fun StartupScreenContent(
    state: StartupUiState,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StateContent(state = state.consent, modifier = modifier, label = "StartupConsent") { consent ->
        when (consent) {
            ConsentRequestStatus.Pending -> LoadingScreen()

            ConsentRequestStatus.Settled -> TopAppBarScaffold(
                title = stringResource(R.string.welcome),
                content = { paddingValues -> StartupWelcome(paddingValues = paddingValues) },
                floatingActionButton = {
                    AnimatedExtendedFloatingActionButton(
                        modifier = Modifier.bounceClick(),
                        text = { Text(text = stringResource(id = R.string.agree)) },
                        onClick = onContinue,
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                            )
                        },
                    )
                },
            )
        }
    }
}

/** The animation behind a column of the illustration and the terms, at a readable width. */
@Composable
private fun StartupWelcome(
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
                .align(Alignment.TopCenter)
                .fillMaxHeight()
                .widthIn(max = StartupContentMaxWidth)
                .padding(paddingValues = paddingValues)
                .padding(all = SizeConstants.MediumSize * 2)
                .safeDrawingPadding(),
            verticalArrangement = Arrangement.spacedBy(space = SizeConstants.LargeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            animatedItem {
                AsyncImage(
                    model = R.drawable.il_startup,
                    contentDescription = null,
                )
            }
            animatedItem {
                InfoMessageSection(
                    message = stringResource(R.string.summary_browse_terms_of_service_and_privacy_policy),
                    learnMoreText = stringResource(CoreUiR.string.learn_more),
                    learnMoreUrl = AppLinks.PRIVACY_POLICY,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartupScreenContentPreview() {
    MaterialTheme {
        StartupScreenContent(
            state = StartupUiState(consent = ConsentRequestStatus.Settled),
            onContinue = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartupScreenContentLoadingPreview() {
    MaterialTheme {
        StartupScreenContent(
            state = StartupUiState(),
            onContinue = {},
        )
    }
}
