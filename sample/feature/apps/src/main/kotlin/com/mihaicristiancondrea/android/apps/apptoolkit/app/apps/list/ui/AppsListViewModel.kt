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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.AppScreenTracking
import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.FavoritesRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.InstalledAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui.contracts.HomeAction
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui.contracts.HomeEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui.states.AppListUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.ui.states.AppsListFilter
import com.mihaicristiancondrea.android.apps.apptoolkit.core.domain.models.network.AppErrors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.ui.utils.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onFailure
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.onSuccess
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.dismissSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setLoading
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setNoData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.setSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

/**
 * ViewModel for the Apps List screen.
 *
 * This ViewModel is responsible for fetching and managing the list of developer applications,
 * handling user interactions such as fetching apps, opening a random app, and toggling favorites.
 * It observes changes in favorite apps and updates the UI state accordingly.
 *
 * @param developerAppsRepository Source of the developer's app catalog and per-app details.
 * @param installedAppsRepository Resolves which catalog entries are installed and their metadata.
 * @param favoritesRepository Reads and updates the set of favorite app package names.
 * @param dispatchers Provides coroutine dispatchers for different contexts (IO, Main, etc.).
 * @param firebaseController Reports ViewModel flow failures to Firebase.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppsListViewModel(
    private val developerAppsRepository: DeveloperAppsRepository,
    private val installedAppsRepository: InstalledAppsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val dispatchers: DispatcherProvider,
    firebaseController: FirebaseController,
) : LoggedScreenViewModel<AppListUiState, HomeEvent, HomeAction>(
    initialState = UiStateScreen(data = AppListUiState()),
    firebaseController = firebaseController,
    screenName = AppScreenTracking.Screens.APPS_LIST.name,
) {

    private val fetchAppsTrigger = MutableSharedFlow<Unit>(replay = 1)
    private var fetchJob: Job? = null
    private var appDetailsJob: Job? = null
    private var appInstallInfoJob: Job? = null
    private var toggleJob: Job? = null

    val favorites = favoritesRepository.observeFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val canOpenRandomApp = screenState
        .map { it.data?.apps?.isNotEmpty() == true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    init {
        observeFetch()
        observeFilterValidity()
        onEvent(HomeEvent.FetchApps)
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.FetchApps -> fetchAppsTrigger.tryEmit(Unit)

            is HomeEvent.FilterSelected -> selectFilter(event.filter)
            is HomeEvent.AppSelected -> selectApp(event.packageName)
            HomeEvent.RetryAppDetails -> screenData?.selectedApp?.packageName?.let(::loadSelectedAppDetails)
            HomeEvent.AppDetailsDismissed -> clearSelectedAppInstallInfo()

            HomeEvent.OpenRandomApp -> {
                val randomApp = screenData?.apps?.randomOrNull() ?: return
                startOperation(
                    action = Actions.OPEN_RANDOM_APP,
                    extra = mapOf(ExtraKeys.PACKAGE_NAME to randomApp.packageName),
                )
                sendAction(HomeAction.OpenRandomApp(randomApp))
            }
        }
    }

    private fun observeFetch() {
        startOperation(action = Actions.OBSERVE_FETCH)
        fetchJob = fetchJob.restart {
            fetchAppsTrigger
                .flatMapLatest {
                    developerAppsRepository.fetchDeveloperApps()
                        .flowOn(dispatchers.io)
                        .onStart {
                            breadcrumb(
                                message = "Fetch developer apps collecting",
                                attributes = mapOf("source" to "AppsListViewModel"),
                            )
                            updateStateThreadSafe {
                                screenState.dismissSnackbar()
                                screenState.setLoading()
                            }
                        }
                }
                .catchReport(action = Actions.OBSERVE_FETCH) {
                    updateStateThreadSafe {
                        showLoadAppsError()
                    }
                }
                .onEach { result ->
                    result
                        .onSuccess { apps ->
                            val list = apps.toImmutableList()
                            val installedPackages = withContext(dispatchers.io) {
                                installedAppsRepository.getInstalledPackages(
                                    packageNames = list.map { app -> app.packageName },
                                ).toImmutableSet()
                            }
                            updateStateThreadSafe {
                                val base = screenData ?: AppListUiState()
                                val updated = base.copy(
                                    apps = list,
                                    installedPackages = installedPackages,
                                )

                                if (list.isEmpty()) {
                                    screenState.setNoData(data = updated)
                                } else {
                                    screenState.setSuccess(data = updated)
                                }
                            }
                        }
                        .onFailure { error ->
                            updateStateThreadSafe {
                                showLoadAppsError(error)
                            }
                        }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun observeFilterValidity() {
        screenState.mapNotNull { it.data }
            .onEach { state ->
                val allAppsCount = state.apps.size
                val installedPackagesCount = state.installedPackages.size
                val favoritesCount = favorites.value.size

                val isFilterValid = when (state.selectedFilter) {
                    AppsListFilter.All -> true
                    AppsListFilter.Installed -> installedPackagesCount > 0
                    AppsListFilter.NotInstalled -> installedPackagesCount in 1..<allAppsCount
                    AppsListFilter.Favorites -> favoritesCount > 0
                }

                if (!isFilterValid) {
                    selectFilter(AppsListFilter.All)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun selectFilter(filter: AppsListFilter) {
        screenState.update { current ->
            current.copy(data = (current.data ?: AppListUiState()).copy(selectedFilter = filter))
        }
    }

    private fun selectApp(packageName: String) {
        val selectedApp = screenData?.apps?.firstOrNull { it.packageName == packageName } ?: return
        screenState.update { current ->
            current.copy(
                data = current.data?.copy(
                    selectedApp = selectedApp,
                    selectedAppDetails = null,
                    isAppDetailsLoading = true,
                    hasAppDetailsError = false,
                    selectedAppInstallInfo = null,
                ),
            )
        }
        loadSelectedAppDetails(packageName)
        loadSelectedAppInstallInfo(packageName)
    }

    private fun loadSelectedAppDetails(packageName: String) {
        appDetailsJob = appDetailsJob.restart {
            developerAppsRepository.fetchAppDetails(packageName)
                .flowOn(dispatchers.io)
                .onStart {
                    screenState.update { current ->
                        current.copy(
                            data = current.data?.copy(
                                selectedAppDetails = null,
                                isAppDetailsLoading = true,
                                hasAppDetailsError = false,
                            ),
                        )
                    }
                }
                .catchReport(
                    action = Actions.LOAD_APP_DETAILS,
                    extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
                ) {
                    updateAppDetailsFailure(packageName)
                }
                .onEach { result ->
                    result
                        .onSuccess { details ->
                            screenState.update { current ->
                                val data = current.data ?: return@update current
                                // A cancelled request can still finish at the transport boundary.
                                // Never apply its detail document to a newer sheet selection.
                                if (data.selectedApp?.packageName != packageName) return@update current
                                current.copy(
                                    data = data.copy(
                                        selectedAppDetails = details,
                                        isAppDetailsLoading = false,
                                        hasAppDetailsError = false,
                                    ),
                                )
                            }
                        }
                        .onFailure {
                            updateAppDetailsFailure(packageName)
                        }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun updateAppDetailsFailure(packageName: String) {
        screenState.update { current ->
            val data = current.data ?: return@update current
            if (data.selectedApp?.packageName != packageName) return@update current
            current.copy(
                data = data.copy(
                    selectedAppDetails = null,
                    isAppDetailsLoading = false,
                    hasAppDetailsError = true,
                ),
            )
        }
    }

    private fun loadSelectedAppInstallInfo(packageName: String) {
        if (packageName.isBlank()) {
            screenState.update { current ->
                current.copy(
                    data = current.data?.copy(
                        selectedAppInstallInfo = installedAppsRepository.getInstallInfo(packageName),
                    ),
                )
            }
            return
        }
        appInstallInfoJob = appInstallInfoJob.restart {
            launchReport(
                action = Actions.LOAD_APP_INSTALL_INFO,
                extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
                block = {
                    val installInfo = withContext(dispatchers.io) {
                        installedAppsRepository.getInstallInfo(packageName)
                    }
                    screenState.update { current ->
                        val data = current.data ?: return@update current
                        if (data.selectedApp?.packageName != packageName) return@update current
                        current.copy(data = data.copy(selectedAppInstallInfo = installInfo))
                    }
                },
                onError = {
                    screenState.update { current ->
                        val data = current.data ?: return@update current
                        if (data.selectedApp?.packageName != packageName) return@update current
                        current.copy(data = data.copy(selectedAppInstallInfo = null))
                    }
                },
            )
        }
    }

    private fun clearSelectedAppInstallInfo() {
        appDetailsJob?.cancel()
        appInstallInfoJob?.cancel()
        screenState.update { current ->
            current.copy(
                data = current.data?.copy(
                    selectedApp = null,
                    selectedAppDetails = null,
                    isAppDetailsLoading = false,
                    hasAppDetailsError = false,
                    selectedAppInstallInfo = null,
                ),
            )
        }
    }

    private fun showLoadAppsError(error: AppErrors? = null) {
        screenState.setError(
            message = error?.toErrorMessage()
                ?: UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
        )
    }

    fun toggleFavorite(packageName: String) {
        toggleJob = toggleJob.restart {
            launchReport(
                action = Actions.TOGGLE_FAVORITE,
                extra = mapOf(ExtraKeys.PACKAGE_NAME to packageName),
                block = {
                    withContext(dispatchers.io) { favoritesRepository.toggleFavorite(packageName) }
                },
                onError = {
                    updateStateThreadSafe {
                        screenState.setError(
                            message = UiTextHelper.StringResource(R.string.error_failed_to_update_favorite),
                        )
                    }
                },
            )
        }
    }

    private object Actions {
        const val OBSERVE_FETCH: String = "observeFetch"
        const val TOGGLE_FAVORITE: String = "toggleFavorite"
        const val OPEN_RANDOM_APP: String = "openRandomApp"
        const val LOAD_APP_INSTALL_INFO: String = "loadAppInstallInfo"
        const val LOAD_APP_DETAILS: String = "loadAppDetails"
    }

    private object ExtraKeys {
        const val PACKAGE_NAME: String = "packageName"
    }
}
