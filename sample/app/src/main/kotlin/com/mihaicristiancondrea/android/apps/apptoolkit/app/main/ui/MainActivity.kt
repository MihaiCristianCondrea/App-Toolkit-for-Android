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

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.MainNavigationDefaults
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.NavigationRoutes
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.appNavigationEntryBuilders
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.toNavKeyOrDefault
import com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local.DatastoreInterface
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.data.managers.NavigationManager
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.MainScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.MainViewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.contracts.MainAction
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.ComponentsActivity
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.navigation.ComponentsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.extensions.activity.observeActions
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.factory.GmsHostFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.ui.HelpActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup.StartupActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.SettingsActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.SupportActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.GeneralSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LibraryExtrasRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LicensesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.NavigationDrawerRoutes
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportRoute
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R as ComponentsR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as AboutR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.R as HelpR

class MainActivity : AppCompatActivity() {

    private val dataStore: DatastoreInterface by inject()
    private val dispatchers: DispatcherProvider by inject()
    private val navigationManager: NavigationManager by inject()
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        openSettingsForShortcut(intent)
    }

    private fun handleStartup() {
        val openSettings = intent.action == ACTION_OPEN_SETTINGS
        lifecycleScope.launch {
            val isFirstLaunch: Boolean =
                withContext(context = dispatchers.io) { dataStore.startup.first() }
            keepSplashVisible = false
            if (isFirstLaunch) {
                startStartupActivity()
            } else {
                val startRoute: StableNavKey = withContext(context = dispatchers.io) {
                    dataStore.startupDestinationFlow(
                        defaultRoute = NavigationRoutes.ROUTE_TOOLKIT_TILES,
                        mapToKey = String::toNavKeyOrDefault,
                    ).first()
                }
                setMainActivityContent(startRoute = startRoute)
                if (openSettings) {
                    openSettingsActivity()
                }
            }
        }
    }

    private fun openSettingsForShortcut(intent: Intent) {
        if (intent.action == ACTION_OPEN_SETTINGS) {
            openSettingsActivity()
        }
    }

    private fun openSettingsActivity() {
        openActivity(activityClass = SettingsActivity::class.java)
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
                    bottomBarItems = MainNavigationDefaults.bottomBarItems,
                    fabSupportedRoutes = MainNavigationDefaults.fabSupportedRoutes,
                    entryBuilders = { context -> appNavigationEntryBuilders(context = context) },
                    onTitleLookup = { route ->
                        when (route) {
                            is SettingsRoute -> stringResource(AboutR.string.settings)
                            is GeneralSettingsRoute -> route.title
                            is HelpRoute -> stringResource(HelpR.string.help)
                            is AdsSettingsRoute -> stringResource(AboutR.string.ads)
                            is PermissionsRoute -> stringResource(AboutR.string.permissions)
                            is LicensesRoute -> stringResource(AboutR.string.oss_license_title)
                            is SupportRoute -> stringResource(AboutR.string.support_us)
                            is LibraryExtrasRoute -> stringResource(CommonR.string.app_name)
                            is ComponentsRoute -> stringResource(ComponentsR.string.components_title)
                            else -> {
                                MainNavigationDefaults.bottomBarItems
                                    .find { it.route == route }?.let { stringResource(it.title) }
                                    ?: stringResource(CommonR.string.app_name)
                            }
                        }
                    },
                    onIsSelected = { itemRoute, currentRoute ->
                        when (itemRoute) {
                            NavigationRoutes.ROUTE_COMPONENTS -> currentRoute is ComponentsRoute
                            NavigationDrawerRoutes.ROUTE_SETTINGS -> currentRoute is SettingsRoute || currentRoute is GeneralSettingsRoute
                            NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK -> currentRoute is HelpRoute
                            NavigationDrawerRoutes.ROUTE_SUPPORT -> currentRoute is SupportRoute
                            else -> false
                        }
                    },
                    onLaunchActivity = { route: StableNavKey ->
                        when (route) {
                            is ComponentsRoute -> {
                                openActivity(activityClass = ComponentsActivity::class.java)
                                true
                            }

                            is SettingsRoute -> {
                                openActivity(activityClass = SettingsActivity::class.java)
                                true
                            }

                            is HelpRoute -> {
                                openActivity(activityClass = HelpActivity::class.java)
                                true
                            }

                            is SupportRoute -> {
                                openActivity(activityClass = SupportActivity::class.java)
                                true
                            }

                            else -> false
                        }
                    },
                    onNavigationRequested = { route ->
                        navigationManager.navigateTo(route = route.toNavKeyOrDefault())
                    }
                )
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

    private companion object {
        const val ACTION_OPEN_SETTINGS =
            "com.d4rk.android.apps.apptoolkit.action.OPEN_SETTINGS"
    }
}
