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
import androidx.lifecycle.lifecycleScope
import com.mihaicristiancondrea.android.apps.apptoolkit.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainAction
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.appGraph
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.startKeyFor
import com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local.DataStoreInterface
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.fab.RandomAppAction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.data.repositories.ComponentsShowcaseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.extensions.activity.observeActions
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.factory.GmsHostFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.views.dialogs.ChangelogDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup.StartupActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.ShellHost
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The sample's only screen host: [ShellHost] with the graph from `appGraph`, which draws every tab,
 * page, bar and drawer. Consent, review and update run from here, where the activity is.
 */
class MainActivity : AppCompatActivity() {

    private val dataStore: DataStoreInterface by inject()
    private val dispatchers: DispatcherProvider by inject()
    private val componentsShowcaseRepository: ComponentsShowcaseRepository by inject()
    private val viewModel: MainViewModel by viewModel()
    private val gmsHostFactory: GmsHostFactory by inject()
    private var updateResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {}
    private var keepSplashVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashVisible }
        enableEdgeToEdge()
        handleStartup()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        handleGmsEvents()
    }

    private fun handleStartup() {
        lifecycleScope.launch {
            val isFirstLaunch: Boolean =
                withContext(context = dispatchers.io) { dataStore.startup.first() }
            if (isFirstLaunch) {
                keepSplashVisible = false
                openActivity(activityClass = StartupActivity::class.java)
                finish()
            } else {
                setShellContent()
            }
        }
    }

    private fun setShellContent() {
        setContent {
            AppTheme {
                val randomApp = remember { RandomAppAction() }
                var showChangelog by rememberSaveable { mutableStateOf(false) }
                val isShowcaseUnlocked by componentsShowcaseRepository.isUnlocked
                    .collectAsStateWithLifecycle(initialValue = false)
                val showComponents = BuildConfig.DEBUG || isShowcaseUnlocked
                val graph = remember(showComponents) {
                    appGraph(
                        randomApp = randomApp,
                        showComponents = showComponents,
                        onShowChangelog = { showChangelog = true },
                    )
                }

                ShellHost(
                    graph = graph,
                    // The start page chosen in the display settings, read before the first frame.
                    resolveStart = {
                        withContext(context = dispatchers.io) {
                            dataStore.startupDestinationFlow(
                                defaultRoute = ToolkitTilesRoute.ROUTE_ID,
                                mapToKey = ::startKeyFor,
                            ).first()
                        }
                    },
                    onReady = { keepSplashVisible = false },
                )

                if (showChangelog) {
                    ChangelogDialog(onDismiss = { showChangelog = false })
                }
            }
        }
    }

    private fun initObservers() {
        observeActions(viewModel = viewModel) { action ->
            when (action) {
                is MainAction.ReviewOutcomeReported -> Unit
                is MainAction.InAppUpdateResultReported -> Unit
            }
        }
    }

    private fun handleGmsEvents() {
        viewModel.onEvent(
            event = MainEvent.RequestConsent(
                host = gmsHostFactory.createConsentHost(
                    activity = this
                )
            )
        )
        viewModel.onEvent(
            event = MainEvent.RequestReview(
                host = gmsHostFactory.createReviewHost(
                    activity = this
                )
            )
        )
        viewModel.onEvent(
            event = MainEvent.RequestInAppUpdate(
                host = gmsHostFactory.createUpdateHost(
                    activity = this,
                    launcher = updateResultLauncher
                )
            )
        )
    }
}
