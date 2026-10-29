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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.github.GithubConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.ProvideTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.StartupRoute
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.appGraph
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.startKeyFor
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.utils.extensions.startupValueFlow
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.data.repositories.ComponentsShowcaseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.AppDefaults
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.data.models.InAppUpdateHost
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.ChangelogDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.ShellHost
import kotlinx.coroutines.flow.first
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The sample's only activity: [ShellHost] with the graph from `appGraph`, which draws every tab,
 * page, bar and drawer, and the first-launch start screens. Consent, review and update run from
 * here, where the activity is.
 */
class MainActivity : AppCompatActivity() {

    private val onboardingRepository: OnboardingRepository by inject()
    private val displayPreferences: DisplayPreferencesRepository by inject()
    private val componentsShowcaseRepository: ComponentsShowcaseRepository by inject()
    private val viewModel: MainViewModel by viewModel()
    private var updateResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {}
    private var keepSplashVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashVisible }
        enableEdgeToEdge()
        setShellContent()
    }

    override fun onResume() {
        super.onResume()
        handleGmsEvents()
    }

    private fun setShellContent() {
        setContent {
            // Around everything, not only ShellHost: the changelog dialog reports too.
            ProvideTelemetry {
                AppTheme {
                    var showChangelog by rememberSaveable { mutableStateOf(false) }
                    val isShowcaseUnlocked by componentsShowcaseRepository.isUnlocked
                        .collectAsStateWithLifecycle(initialValue = false)
                    val showDebugPages = BuildConfig.DEBUG || isShowcaseUnlocked
                    val graph = remember(showDebugPages) {
                        appGraph(
                            showDebugPages = showDebugPages,
                            onShowChangelog = { showChangelog = true },
                        )
                    }

                    val shellSnackbars = remember { SnackbarHostState() }
                    MessageHost(
                        viewModel = viewModel,
                        snackbarHostState = shellSnackbars,
                        drawHost = false,
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        ShellHost(
                            graph = graph,
                            // Read before the first frame: the first-launch start screens until
                            // onboarding is done, then the start page chosen in the display settings.
                            // DataStore reads are main-safe, so no dispatcher switch is needed.
                            resolveStart = {
                                if (!onboardingRepository.observeOnboardingCompletion().first()) {
                                    StartupRoute
                                } else {
                                    displayPreferences.startupValueFlow(
                                        defaultRoute = ToolkitTilesRoute.ROUTE_ID,
                                        mapToValue = ::startKeyFor,
                                    ).first()
                                }
                            },
                            defaults = AppDefaults.shell,
                            onReady = { keepSplashVisible = false },
                            snackbarHostState = shellSnackbars,
                        )
                    }

                    if (showChangelog) {
                        ChangelogDialog(
                            onDismiss = { showChangelog = false },
                            releasesUrl = GithubConstants.APP_TOOLKIT_RELEASES,
                        )
                    }
                }
            }
        }
    }

    private fun handleGmsEvents() {
        viewModel.onEvent(
            event = MainEvent.RequestConsent(host = ConsentHost(activity = this))
        )
        viewModel.onEvent(
            event = MainEvent.RequestReview(host = ReviewHost(activity = this))
        )
        viewModel.onEvent(
            event = MainEvent.RequestInAppUpdate(
                host = InAppUpdateHost(activity = this, updateResultLauncher = updateResultLauncher)
            )
        )
    }
}
