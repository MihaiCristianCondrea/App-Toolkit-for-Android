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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.FavoritesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.InstalledAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInstallInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.HomeEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers.toAppDetailsFailed
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers.toCatalogueFailed
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers.toFavoriteErrorMessage
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers.toStaleCatalogueMessage
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.AppListUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.AppsListFilter
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.isAvailable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics.AppInteractionType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics.logAppInteraction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logSelectContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logViewItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logViewItemList
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Job

/**
 * Owns catalogue loading, favorites and installed state, and the selected app's details.
 *
 * A failed download falls back to the catalogue saved by the last successful one, shown as stale
 * with a message, and fails the screen only when nothing was saved. Results for an app that is no
 * longer selected never replace the newer selection.
 *
 * Every repository is main-safe and the filtering runs in the content, so it needs no dispatcher.
 */
class AppsListViewModel(
    private val developerAppsRepository: DeveloperAppsRepository,
    private val installedAppsRepository: InstalledAppsRepository,
    private val favoritesRepository: FavoritesRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<AppListUiState, HomeEvent>(
    initialState = AppListUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.APPS_LIST.name,
    viewModelName = "AppsListViewModel",
) {
    private var loadJob: Job? = null
    private var savedAppsJob: Job? = null
    private var appDetailsJob: Job? = null
    private var appInstallInfoJob: Job? = null

    init {
        observeFavorites()
        onEvent(HomeEvent.Load)
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Load -> load()
            HomeEvent.OpenRandomApp -> openRandomApp()
            HomeEvent.RandomAppOpened -> setState { copy(randomAppToOpen = null) }
            is HomeEvent.FilterSelected -> {
                telemetryRepository.logViewItemList(
                    itemListId = event.filter.name.lowercase(),
                    itemListName = "developer_apps_${event.filter.name.lowercase()}",
                )
                setState { copy(selectedFilter = event.filter).withAvailableFilter() }
            }

            is HomeEvent.FavoriteToggled -> toggleFavorite(event.packageName)
            is HomeEvent.AppSelected -> selectApp(event.packageName)
            HomeEvent.RetryAppDetails -> currentState.selectedApp?.packageName?.let(::loadSelectedAppDetails)
            HomeEvent.AppDetailsDismissed -> clearSelectedApp()
        }
    }

    /**
     * Downloads the catalogue. Content already shown stays while the download runs, marked as
     * refreshing, so a retry from the stale catalogue does not blank the grid.
     */
    private fun load() {
        savedAppsJob?.cancel()
        loadJob = loadJob.restart {
            launchReport(
                action = Actions.OBSERVE_FETCH,
                onError = { error -> showSavedApps(fetchError = error) },
            ) {
                breadcrumb(
                    message = "Fetch developer apps collecting",
                    attributes = mapOf("source" to "AppsListViewModel"),
                )
                setState {
                    val shown = apps
                    copy(apps = if (shown is Loadable.Ready) shown.copy(refreshing = true) else Loadable.Loading)
                }
                showApps(apps = developerAppsRepository.fetchDeveloperApps(), stale = false)
            }
        }
    }

    /**
     * Shows the saved catalogue after [fetchError], with a message saying why it may be old. The
     * screen fails with [fetchError] only when nothing was saved or the saved copy cannot be read.
     */
    private fun showSavedApps(fetchError: Throwable) {
        savedAppsJob = savedAppsJob.restart {
            launchReport(
                action = Actions.LOAD_SAVED_APPS,
                onError = { setState { copy(apps = fetchError.toCatalogueFailed()) } },
            ) {
                val savedApps = developerAppsRepository.savedDeveloperApps().orEmpty()
                if (savedApps.isEmpty()) {
                    setState { copy(apps = fetchError.toCatalogueFailed()) }
                } else {
                    showApps(apps = savedApps, stale = true)
                    showMessage(fetchError.toStaleCatalogueMessage())
                }
            }
        }
    }

    private suspend fun showApps(apps: List<AppInfo>, stale: Boolean) {
        val list: ImmutableList<AppInfo> = apps.toImmutableList()
        val installed = installedAppsRepository.getInstalledPackages(
            packageNames = list.map { app -> app.packageName },
        ).toImmutableSet()
        setState {
            copy(
                apps = if (list.isEmpty()) Loadable.Empty() else Loadable.Ready(value = list, stale = stale),
                installedPackages = installed,
            ).withAvailableFilter()
        }
        if (list.isNotEmpty()) {
            telemetryRepository.logViewItemList(
                itemListId = "all",
                itemListName = "developer_apps_all",
            )
        }
    }

    /**
     * Keeps the favorites in the state. A change also re-checks the selected filter, so removing
     * the last favorite does not leave an empty Favorites filter selected with its chip gone.
     */
    private fun observeFavorites() {
        favoritesRepository.observeFavorites().collectReport(action = Actions.OBSERVE_FAVORITES) { favorites ->
            setState { copy(favorites = favorites.toImmutableSet()).withAvailableFilter() }
        }
    }

    /**
     * Each call completes on its own, so two quick taps on different cards both apply. A failure
     * keeps the grid and shows a message.
     */
    private fun toggleFavorite(packageName: String) {
        launchReport(
            action = Actions.TOGGLE_FAVORITE,
            extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
            onError = { error -> showMessage(error.toFavoriteErrorMessage()) },
        ) {
            favoritesRepository.toggleFavorite(packageName)
        }
    }

    /**
     * Picks the app and closes the details sheet. The screen opens the app, since that needs a
     * `Context`, and reports back with [HomeEvent.RandomAppOpened].
     */
    private fun openRandomApp() {
        val randomApp = currentState.loadedApps.randomOrNull() ?: return
        telemetryRepository.logSelectContent(
            contentType = "random_app",
            itemId = randomApp.packageName,
        )
        startOperation(
            action = Actions.OPEN_RANDOM_APP,
            extra = mapOf(ExtraKeys.PACKAGE_NAME to randomApp.packageName),
        )
        clearSelectedApp()
        setState { copy(randomAppToOpen = randomApp) }
    }

    private fun selectApp(packageName: String) {
        val selectedApp = currentState.loadedApps.firstOrNull { it.packageName == packageName } ?: return
        telemetryRepository.logViewItem(
            itemId = selectedApp.packageName,
            itemName = selectedApp.name,
            itemCategory = selectedApp.category?.label,
        )
        telemetryRepository.logAppInteraction(
            source = "apps_list",
            appInfo = selectedApp,
            interaction = AppInteractionType.OpenDetailsBottomSheet,
        )
        setState {
            copy(
                selectedApp = selectedApp,
                selectedAppDetails = Loadable.Loading,
                selectedAppInstallInfo = null,
            )
        }
        loadSelectedAppDetails(packageName)
        loadSelectedAppInstallInfo(packageName)
    }

    private fun loadSelectedAppDetails(packageName: String) {
        appDetailsJob = appDetailsJob.restart {
            launchReport(
                action = Actions.LOAD_APP_DETAILS,
                extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
                onError = { error ->
                    updateSelectedApp(packageName) { copy(selectedAppDetails = error.toAppDetailsFailed()) }
                },
            ) {
                updateSelectedApp(packageName) { copy(selectedAppDetails = Loadable.Loading) }
                val details = developerAppsRepository.fetchAppDetails(packageName)
                updateSelectedApp(packageName) { copy(selectedAppDetails = Loadable.Ready(details)) }
            }
        }
    }

    /** An app without a package name cannot be installed, so it needs no lookup. */
    private fun loadSelectedAppInstallInfo(packageName: String) {
        if (packageName.isBlank()) {
            setState { copy(selectedAppInstallInfo = AppInstallInfo(isInstalled = false, versionInfo = null)) }
            return
        }
        appInstallInfoJob = appInstallInfoJob.restart {
            launchReport(
                action = Actions.LOAD_APP_INSTALL_INFO,
                extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
                onError = { updateSelectedApp(packageName) { copy(selectedAppInstallInfo = null) } },
            ) {
                val installInfo = installedAppsRepository.getInstallInfo(packageName)
                updateSelectedApp(packageName) { copy(selectedAppInstallInfo = installInfo) }
            }
        }
    }

    private fun clearSelectedApp() {
        appDetailsJob?.cancel()
        appInstallInfoJob?.cancel()
        setState {
            copy(
                selectedApp = null,
                selectedAppDetails = Loadable.Empty(),
                selectedAppInstallInfo = null,
            )
        }
    }

    /**
     * Applies [reduce] only while [packageName] is still the selected app. A cancelled request can
     * still finish at the transport boundary, and its result must not reach a newer selection.
     */
    private fun updateSelectedApp(packageName: String, reduce: AppListUiState.() -> AppListUiState) {
        setState { if (selectedApp?.packageName == packageName) reduce() else this }
    }

    /**
     * Falls back to [AppsListFilter.All] when the selected filter matches nothing. It is not
     * reported: nobody tapped it, and the tap itself is reported where the event arrives.
     */
    private fun AppListUiState.withAvailableFilter(): AppListUiState {
        val isAvailable = selectedFilter.isAvailable(
            appCount = loadedApps.size,
            installedCount = installedPackages.size,
            favoritesCount = favorites.size,
        )
        return if (isAvailable) this else copy(selectedFilter = AppsListFilter.All)
    }

    private object Actions {
        const val OBSERVE_FETCH: String = "observeFetch"
        const val LOAD_SAVED_APPS: String = "loadSavedApps"
        const val OBSERVE_FAVORITES: String = "observeFavorites"
        const val TOGGLE_FAVORITE: String = "toggleFavorite"
        const val OPEN_RANDOM_APP: String = "openRandomApp"
        const val LOAD_APP_INSTALL_INFO: String = "loadAppInstallInfo"
        const val LOAD_APP_DETAILS: String = "loadAppDetails"
    }

    private object ExtraKeys {
        const val PACKAGE_NAME: String = "packageName"
    }
}
