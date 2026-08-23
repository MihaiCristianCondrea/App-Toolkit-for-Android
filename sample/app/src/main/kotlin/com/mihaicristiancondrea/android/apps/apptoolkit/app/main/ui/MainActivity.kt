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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainAction
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.appNavigationEntryBuilders
import com.mihaicristiancondrea.android.apps.apptoolkit.core.data.local.datastore.DatastoreInterface
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.NavigationRoutes
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.toNavKeyOrDefault
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.factory.GmsHostFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.app.startup.ui.StartupActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.extensions.activity.observeActions
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val dataStore: DatastoreInterface by inject()
    private val dispatchers: DispatcherProvider by inject()
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
        observeActions()
    }

    override fun onResume() {
        super.onResume()
        handleGmsEvents()
    }

    private fun handleStartup() {
        lifecycleScope.launch {
            val isFirstLaunch: Boolean =
                withContext(context = dispatchers.io) { dataStore.startup.first() }
            keepSplashVisible = false
            if (isFirstLaunch) {
                startStartupActivity()
            } else {
                val startRoute: StableNavKey = withContext(context = dispatchers.io) {
                    dataStore.startupDestinationFlow(
                        defaultRoute = NavigationRoutes.ROUTE_APPS_LIST,
                        mapToKey = String::toNavKeyOrDefault,
                    ).first()
                }
                setMainActivityContent(startRoute = startRoute)
            }
        }
    }

    private fun startStartupActivity() {
        openActivity(activityClass = StartupActivity::class.java)
        finish()
    }

    private fun setMainActivityContent(startRoute: StableNavKey) {
        setContent {
            AppTheme {
                MainScreen(
                    startRoute = startRoute,
                    entryBuilders = { context -> appNavigationEntryBuilders(context = context) },
                )
            }
        }
    }

    private fun observeActions() {
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
