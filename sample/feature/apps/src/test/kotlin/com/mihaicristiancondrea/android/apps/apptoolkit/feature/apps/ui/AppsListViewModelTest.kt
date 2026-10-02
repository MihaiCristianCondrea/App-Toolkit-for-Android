/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInstallInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.HomeEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.AppsListFilter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppsListViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()

        private val FIRST_APP = AppInfo(
            name = "App",
            packageName = "pkg",
            iconUrl = "url",
            shortDescription = "Description",
        )

        private val SECOND_APP = AppInfo(
            name = "Other",
            packageName = "other.pkg",
            iconUrl = "url",
            shortDescription = "Other description",
        )

        private val NO_INTERNET = NetworkException(reason = NetworkException.Reason.NO_INTERNET)
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(
        developerAppsRepository: FakeDeveloperAppsRepository = FakeDeveloperAppsRepository(
            apps = listOf(FIRST_APP, SECOND_APP),
        ),
        favoritesRepository: FakeFavoritesRepository = FakeFavoritesRepository(),
        installedAppsRepository: FakeInstalledAppsRepository = FakeInstalledAppsRepository(),
    ): AppsListViewModel = AppsListViewModel(
        developerAppsRepository = developerAppsRepository,
        installedAppsRepository = installedAppsRepository,
        favoritesRepository = favoritesRepository,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private val UiTextHelper.resourceId: Int
        get() = (this as UiTextHelper.StringResource).resourceId

    private fun AppsListViewModel.readyApps(): Loadable.Ready<List<AppInfo>> =
        assertIs<Loadable.Ready<List<AppInfo>>>(state.value.apps)

    private fun AppsListViewModel.onlyMessage(): UiMessage = messages.value.single()

    @Test
    fun `first load shows the catalogue with its installed packages`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            installedAppsRepository = FakeInstalledAppsRepository(installedPackages = setOf("pkg")),
        )
        advance()

        val apps = viewModel.readyApps()
        assertEquals(listOf(FIRST_APP, SECOND_APP), apps.value)
        assertFalse(apps.stale)
        assertEquals(setOf("pkg"), viewModel.state.value.installedPackages)
        assertTrue(viewModel.state.value.canOpenRandomApp)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `first load keeps a large catalogue whole`() = runTest(dispatcherExtension.testDispatcher) {
        val apps = (1..10_000).map { index ->
            AppInfo(name = "App$index", packageName = "pkg$index", iconUrl = "url$index")
        }
        val viewModel = createViewModel(developerAppsRepository = FakeDeveloperAppsRepository(apps = apps))
        advance()

        assertEquals(apps.size, viewModel.readyApps().value.size)
    }

    @Test
    fun `an empty catalogue shows the empty state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(developerAppsRepository = FakeDeveloperAppsRepository(apps = emptyList()))
        advance()

        assertIs<Loadable.Empty>(viewModel.state.value.apps)
        assertFalse(viewModel.state.value.canOpenRandomApp)
    }

    @Test
    fun `offline with a saved catalogue shows it as stale with a message that can retry`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(
                developerAppsRepository = FakeDeveloperAppsRepository(
                    fetchFailure = NO_INTERNET,
                    savedApps = listOf(FIRST_APP),
                ),
            )
            advance()

            val apps = viewModel.readyApps()
            assertEquals(listOf(FIRST_APP), apps.value)
            assertTrue(apps.stale)
            val message = viewModel.onlyMessage()
            assertTrue(message.isError)
            assertEquals(CoreUiR.string.screen_error_no_internet, message.text.resourceId)
            assertEquals(CoreUiR.string.try_again, message.actionLabel?.resourceId)
        }

    @Test
    fun `offline without a saved catalogue fails with a retry`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            developerAppsRepository = FakeDeveloperAppsRepository(fetchFailure = NO_INTERNET),
        )
        advance()

        val failed = assertIs<Loadable.Failed>(viewModel.state.value.apps)
        assertEquals(CoreUiR.string.screen_error_no_internet, failed.message.resourceId)
        assertTrue(failed.retryable)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `a failure with no text of its own shows the apps fallback`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            developerAppsRepository = FakeDeveloperAppsRepository(
                fetchFailure = IllegalStateException("Catalogue request failed"),
            ),
        )
        advance()

        val failed = assertIs<Loadable.Failed>(viewModel.state.value.apps)
        assertEquals(R.string.error_failed_to_load_apps, failed.message.resourceId)
    }

    @Test
    fun `an unreadable response fails without a retry`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            developerAppsRepository = FakeDeveloperAppsRepository(
                fetchFailure = NetworkException(reason = NetworkException.Reason.SERIALIZATION),
            ),
        )
        advance()

        assertFalse(assertIs<Loadable.Failed>(viewModel.state.value.apps).retryable)
    }

    @Test
    fun `an unreadable saved catalogue fails with the download's failure`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(
                developerAppsRepository = FakeDeveloperAppsRepository(
                    fetchFailure = NO_INTERNET,
                    savedAppsFailure = StorageException(reason = StorageException.Reason.FAILED),
                ),
            )
            advance()

            val failed = assertIs<Loadable.Failed>(viewModel.state.value.apps)
            assertEquals(CoreUiR.string.screen_error_no_internet, failed.message.resourceId)
        }

    @Test
    fun `a failed download is reported with its action`() = runTest(dispatcherExtension.testDispatcher) {
        createViewModel(developerAppsRepository = FakeDeveloperAppsRepository(fetchFailure = NO_INTERNET))
        advance()

        val error = telemetryRepository.loggedEvents.single { it.name == "vm_op_error" }
        assertEquals(AnalyticsValue.Str("observeFetch"), error.params["action"])
        assertEquals(AnalyticsValue.Str("NetworkException"), error.params["error_class"])
    }

    @Test
    fun `retry after a thrown error shows the catalogue`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeDeveloperAppsRepository(
            apps = listOf(FIRST_APP),
            fetchFailure = IllegalStateException("Catalogue request failed"),
        )
        val viewModel = createViewModel(developerAppsRepository = repository)
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.apps)

        repository.fetchFailure = null
        viewModel.onEvent(HomeEvent.Load)
        advance()

        assertEquals(listOf(FIRST_APP), viewModel.readyApps().value)
        assertEquals(2, repository.fetchCount)
    }

    @Test
    fun `retry from the stale catalogue shows the fresh one`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeDeveloperAppsRepository(
            apps = listOf(FIRST_APP, SECOND_APP),
            fetchFailure = NO_INTERNET,
            savedApps = listOf(FIRST_APP),
        )
        val viewModel = createViewModel(developerAppsRepository = repository)
        advance()
        assertTrue(viewModel.readyApps().stale)

        repository.fetchFailure = null
        viewModel.onEvent(HomeEvent.Load)
        advance()

        val apps = viewModel.readyApps()
        assertEquals(listOf(FIRST_APP, SECOND_APP), apps.value)
        assertFalse(apps.stale)
        assertFalse(apps.refreshing)
    }

    @Test
    fun `selecting a filter keeps it while it matches apps`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            favoritesRepository = FakeFavoritesRepository(initialFavorites = setOf("pkg")),
        )
        advance()

        viewModel.onEvent(HomeEvent.FilterSelected(AppsListFilter.Favorites))

        assertEquals(AppsListFilter.Favorites, viewModel.state.value.selectedFilter)
    }

    @Test
    fun `selecting a filter that matches nothing falls back to All`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(HomeEvent.FilterSelected(AppsListFilter.Installed))

        assertEquals(AppsListFilter.All, viewModel.state.value.selectedFilter)
    }

    @Test
    fun `removing the last favorite resets the Favorites filter`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            favoritesRepository = FakeFavoritesRepository(initialFavorites = setOf("pkg")),
        )
        advance()
        viewModel.onEvent(HomeEvent.FilterSelected(AppsListFilter.Favorites))

        viewModel.onEvent(HomeEvent.FavoriteToggled(packageName = "pkg"))
        advance()

        assertTrue(viewModel.state.value.favorites.isEmpty())
        assertEquals(AppsListFilter.All, viewModel.state.value.selectedFilter)
    }

    @Test
    fun `toggling a favorite adds it and toggling again removes it`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(HomeEvent.FavoriteToggled(packageName = "pkg"))
        advance()
        assertEquals(setOf("pkg"), viewModel.state.value.favorites)

        viewModel.onEvent(HomeEvent.FavoriteToggled(packageName = "pkg"))
        advance()
        assertTrue(viewModel.state.value.favorites.isEmpty())
    }

    @Test
    fun `a failed favorite update keeps the grid and shows a message`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            favoritesRepository = FakeFavoritesRepository(toggleError = IllegalStateException("Write failed")),
        )
        advance()

        viewModel.onEvent(HomeEvent.FavoriteToggled(packageName = "pkg"))
        advance()

        assertIs<Loadable.Ready<List<AppInfo>>>(viewModel.state.value.apps)
        val message = viewModel.onlyMessage()
        assertTrue(message.isError)
        assertEquals(R.string.error_failed_to_update_favorite, message.text.resourceId)
    }

    @Test
    fun `a shown message leaves the queue`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            favoritesRepository = FakeFavoritesRepository(toggleError = IllegalStateException("Write failed")),
        )
        advance()
        viewModel.onEvent(HomeEvent.FavoriteToggled(packageName = "pkg"))
        advance()

        viewModel.messageShown(viewModel.onlyMessage().id)

        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `selecting an app loads its details and install state`() = runTest(dispatcherExtension.testDispatcher) {
        val installInfo = AppInstallInfo(isInstalled = true, versionInfo = null)
        val viewModel = createViewModel(
            installedAppsRepository = FakeInstalledAppsRepository(installInfoMap = mapOf("pkg" to installInfo)),
        )
        advance()

        viewModel.onEvent(HomeEvent.AppSelected(packageName = "pkg"))
        advance()

        val state = viewModel.state.value
        assertEquals(FIRST_APP, state.selectedApp)
        val details = assertIs<Loadable.Ready<AppDetails>>(state.selectedAppDetails)
        assertEquals("Description", details.value.description)
        assertEquals(installInfo, state.selectedAppInstallInfo)
    }

    @Test
    fun `failed details can be retried`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeDeveloperAppsRepository(
            apps = listOf(FIRST_APP),
            detailsFailure = NO_INTERNET,
        )
        val viewModel = createViewModel(developerAppsRepository = repository)
        advance()
        viewModel.onEvent(HomeEvent.AppSelected(packageName = "pkg"))
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.selectedAppDetails)

        repository.detailsFailure = null
        viewModel.onEvent(HomeEvent.RetryAppDetails)
        advance()

        assertIs<Loadable.Ready<*>>(viewModel.state.value.selectedAppDetails)
    }

    @Test
    fun `dismissing the details clears the selection`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()
        viewModel.onEvent(HomeEvent.AppSelected(packageName = "pkg"))
        advance()

        viewModel.onEvent(HomeEvent.AppDetailsDismissed)

        val state = viewModel.state.value
        assertNull(state.selectedApp)
        assertIs<Loadable.Empty>(state.selectedAppDetails)
        assertNull(state.selectedAppInstallInfo)
    }

    @Test
    fun `opening a random app asks the screen to open one and closes the details`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            advance()
            viewModel.onEvent(HomeEvent.AppSelected(packageName = "pkg"))
            advance()

            viewModel.onEvent(HomeEvent.OpenRandomApp)

            val state = viewModel.state.value
            val randomApp = assertNotNull(state.randomAppToOpen)
            assertTrue(randomApp in listOf(FIRST_APP, SECOND_APP))
            assertNull(state.selectedApp)
            assertTrue(telemetryRepository.loggedEvents.any { it.name == "select_content" })
        }

    @Test
    fun `the random app opens once`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()
        viewModel.onEvent(HomeEvent.OpenRandomApp)

        viewModel.onEvent(HomeEvent.RandomAppOpened)

        assertNull(viewModel.state.value.randomAppToOpen)
    }

    @Test
    fun `opening a random app does nothing without a catalogue`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(developerAppsRepository = FakeDeveloperAppsRepository(apps = emptyList()))
        advance()

        viewModel.onEvent(HomeEvent.OpenRandomApp)

        assertNull(viewModel.state.value.randomAppToOpen)
    }
}
