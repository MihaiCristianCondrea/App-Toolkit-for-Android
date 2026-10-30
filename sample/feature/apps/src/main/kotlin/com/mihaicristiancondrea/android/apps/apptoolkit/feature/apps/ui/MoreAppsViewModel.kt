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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.FavoritesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.analytics.AppInteractionType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.analytics.logAppInteraction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.MoreAppsEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers.toAppDetailsFailed
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers.toCatalogueFailed
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers.toFavoriteErrorMessage
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.MoreAppsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.AppInstallInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.InstalledAppsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logViewItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logViewItemList
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Job

/**
 * Owns the "More apps" section at the end of Quick Tools: the developer's catalogue, which of its
 * apps are installed, and the details sheet of the app that was tapped.
 *
 * A failed download falls back to the catalogue saved by the last successful one, shown as is,
 * and fails the section only when nothing was saved; the section is a footer, so it never raises
 * a message of its own. Results for an app that is no longer selected never replace the newer
 * selection.
 *
 * It reports as part of Quick Tools, the screen it sits on. Every repository is main-safe, so it
 * needs no dispatcher.
 */
class MoreAppsViewModel(
    private val developerAppsRepository: DeveloperAppsRepository,
    private val installedAppsRepository: InstalledAppsRepository,
    private val favoritesRepository: FavoritesRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<MoreAppsUiState, MoreAppsEvent>(
    initialState = MoreAppsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "MoreAppsViewModel",
) {
    private var loadJob: Job? = null
    private var savedAppsJob: Job? = null
    private var appDetailsJob: Job? = null
    private var appInstallInfoJob: Job? = null

    init {
        observeFavorites()
        onEvent(MoreAppsEvent.Load)
    }

    override fun handleEvent(event: MoreAppsEvent) {
        when (event) {
            MoreAppsEvent.Load -> load()
            is MoreAppsEvent.AppSelected -> selectApp(event.packageName)
            is MoreAppsEvent.FavoriteToggled -> toggleFavorite(event.packageName)
            MoreAppsEvent.RetryAppDetails -> currentState.selectedApp?.packageName?.let(::loadSelectedAppDetails)
            MoreAppsEvent.AppDetailsDismissed -> clearSelectedApp()
        }
    }

    private fun load() {
        savedAppsJob?.cancel()
        loadJob = loadJob.restart {
            launchReport(
                action = Actions.FETCH_APPS,
                onError = { error -> showSavedApps(fetchError = error) },
            ) {
                setState {
                    val shown = apps
                    copy(apps = if (shown is Loadable.Ready) shown.copy(refreshing = true) else Loadable.Loading)
                }
                showApps(apps = developerAppsRepository.fetchDeveloperApps(), stale = false)
            }
        }
    }

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
                }
            }
        }
    }

    private suspend fun showApps(apps: List<AppSummary>, stale: Boolean) {
        val list: ImmutableList<AppSummary> = apps.toImmutableList()
        val installed = installedAppsRepository.getInstalledPackages(
            packageNames = list.map { app -> app.packageName },
        ).toImmutableSet()
        setState {
            copy(
                apps = if (list.isEmpty()) Loadable.Empty() else Loadable.Ready(value = list, stale = stale),
                installedPackages = installed,
            )
        }
        if (list.isNotEmpty()) {
            telemetryRepository.logViewItemList(itemListId = ITEM_LIST_ID, itemListName = ITEM_LIST_NAME)
        }
    }

    private fun observeFavorites() {
        favoritesRepository.observeFavorites().collectReport(action = Actions.OBSERVE_FAVORITES) { favorites ->
            setState { copy(favorites = favorites.toImmutableSet()) }
        }
    }

    private fun toggleFavorite(packageName: String) {
        launchReport(
            action = Actions.TOGGLE_FAVORITE,
            extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
            onError = { error -> showMessage(error.toFavoriteErrorMessage()) },
        ) {
            favoritesRepository.toggleFavorite(packageName)
        }
    }

    private fun selectApp(packageName: String) {
        val loaded = (currentState.apps as? Loadable.Ready)?.value ?: return
        val selectedApp = loaded.firstOrNull { it.packageName == packageName } ?: return
        telemetryRepository.logViewItem(
            itemId = selectedApp.packageName,
            itemName = selectedApp.name,
            itemCategory = selectedApp.category?.label,
        )
        telemetryRepository.logAppInteraction(
            source = INTERACTION_SOURCE,
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
    private fun updateSelectedApp(packageName: String, reduce: MoreAppsUiState.() -> MoreAppsUiState) {
        setState { if (selectedApp?.packageName == packageName) reduce() else this }
    }

    private object Actions {
        const val FETCH_APPS: String = "fetchMoreApps"
        const val LOAD_SAVED_APPS: String = "loadSavedApps"
        const val OBSERVE_FAVORITES: String = "observeFavorites"
        const val TOGGLE_FAVORITE: String = "toggleFavorite"
        const val LOAD_APP_INSTALL_INFO: String = "loadAppInstallInfo"
        const val LOAD_APP_DETAILS: String = "loadAppDetails"
    }

    private object ExtraKeys {
        const val PACKAGE_NAME: String = "packageName"
    }

    internal companion object {
        /** The `app_card_interaction` source of the section's cards. */
        const val INTERACTION_SOURCE: String = "more_apps"
        const val ITEM_LIST_ID: String = "more_apps"
        const val ITEM_LIST_NAME: String = "developer_more_apps"
    }
}
