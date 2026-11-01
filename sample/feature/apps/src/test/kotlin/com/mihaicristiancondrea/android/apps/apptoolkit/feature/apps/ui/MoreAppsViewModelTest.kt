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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.MoreAppsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import kotlinx.coroutines.test.runTest
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.AppInstallInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.AppVersionMetadata
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MoreAppsViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()

        private val FIRST_APP = AppSummary(name = "App", packageName = "pkg", iconUrl = "url", shortDescription = "Description")
        private val SECOND_APP = AppSummary(name = "Other", packageName = "other.pkg", iconUrl = "url", shortDescription = "Other")
        private val NO_INTERNET = NetworkException(reason = NetworkException.Reason.NO_INTERNET)
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun createViewModel(
        developerAppsRepository: FakeDeveloperAppsRepository = FakeDeveloperAppsRepository(apps = listOf(FIRST_APP, SECOND_APP)),
        installedAppsRepository: FakeInstalledAppsRepository = FakeInstalledAppsRepository(),
        favoritesRepository: FakeFavoritesRepository = FakeFavoritesRepository(),
    ) = MoreAppsViewModel(
        developerAppsRepository = developerAppsRepository,
        installedAppsRepository = installedAppsRepository,
        favoritesRepository = favoritesRepository,
        telemetryRepository = telemetryRepository,
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `loads the catalogue with the packages installed on the device`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(installedAppsRepository = FakeInstalledAppsRepository(installedPackages = setOf("pkg")))
        advance()

        val apps = assertIs<Loadable.Ready<List<AppSummary>>>(viewModel.state.value.apps)
        assertEquals(listOf(FIRST_APP, SECOND_APP), apps.value)
        assertEquals(setOf("pkg"), viewModel.state.value.installedPackages)
        assertFalse(telemetryRepository.loggedEvents.any { it.name == "view_item_list" })
    }

    @Test
    fun `an empty catalogue is the empty state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(developerAppsRepository = FakeDeveloperAppsRepository(apps = emptyList()))
        advance()

        assertIs<Loadable.Empty>(viewModel.state.value.apps)
    }

    @Test
    fun `offline it shows the saved catalogue without a message`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(
            developerAppsRepository = FakeDeveloperAppsRepository(fetchFailure = NO_INTERNET, savedApps = listOf(SECOND_APP)),
        )
        advance()

        val apps = assertIs<Loadable.Ready<List<AppSummary>>>(viewModel.state.value.apps)
        assertEquals(listOf(SECOND_APP), apps.value)
        assertTrue(apps.stale)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `offline with nothing saved fails, and a retry loads it`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = FakeDeveloperAppsRepository(apps = listOf(FIRST_APP), fetchFailure = NO_INTERNET)
        val viewModel = createViewModel(developerAppsRepository = repository)
        advance()
        assertIs<Loadable.Failed>(viewModel.state.value.apps)

        repository.fetchFailure = null
        viewModel.onEvent(MoreAppsEvent.Load)
        advance()

        assertEquals(listOf(FIRST_APP), assertIs<Loadable.Ready<List<AppSummary>>>(viewModel.state.value.apps).value)
        assertEquals(2, repository.fetchCount)
    }

    @Test
    fun `selecting an app opens its details and dismissing closes them`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(MoreAppsEvent.AppSelected("other.pkg"))
        advance()
        assertEquals(SECOND_APP, viewModel.state.value.selectedApp)
        assertIs<Loadable.Ready<*>>(viewModel.state.value.selectedAppDetails)
        assertFalse(viewModel.state.value.selectedAppInstallInfo?.isInstalled ?: true)
        assertTrue(telemetryRepository.loggedEvents.any { it.name == "view_item" })

        viewModel.onEvent(MoreAppsEvent.AppDetailsDismissed)
        assertNull(viewModel.state.value.selectedApp)
        assertIs<Loadable.Empty>(viewModel.state.value.selectedAppDetails)
    }

    @Test
    fun `an app that is not in the catalogue opens nothing`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(MoreAppsEvent.AppSelected("missing.pkg"))

        assertNull(viewModel.state.value.selectedApp)
    }

    @Test
    fun `a favorite toggled from the details sheet is kept`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        advance()

        viewModel.onEvent(MoreAppsEvent.FavoriteToggled("pkg"))
        advance()

        assertEquals(setOf("pkg"), viewModel.state.value.favorites)
    }

    @Test
    fun installUpdateAndUninstallRefreshBothTheCardAndOpenDetailsWithoutDownloading() = runTest(dispatcherExtension.testDispatcher) {
        val catalogue = FakeDeveloperAppsRepository(apps = listOf(FIRST_APP))
        val packages = FakeInstalledAppsRepository()
        val model = createViewModel(catalogue, packages)
        advance()
        model.onEvent(MoreAppsEvent.AppSelected(FIRST_APP.packageName))
        advance()
        assertFalse(model.state.value.selectedAppInstallInfo!!.isInstalled)

        for ((installed, version) in listOf(true to 1L, true to 2L, false to 0L)) {
            packages.installedPackages = if (installed) setOf(FIRST_APP.packageName) else emptySet()
            val info = AppInstallInfo(installed, if (installed) AppVersionMetadata("v$version", version) else null)
            packages.installInfoMap = mapOf(FIRST_APP.packageName to info)
            model.onEvent(MoreAppsEvent.RefreshInstalled)
            advance()
            assertEquals(installed, FIRST_APP.packageName in model.state.value.installedPackages)
            assertEquals(info, model.state.value.selectedAppInstallInfo)
            assertEquals(FIRST_APP, model.state.value.selectedApp)
            assertEquals(1, catalogue.fetchCount)
        }
        assertFalse(telemetryRepository.loggedEvents.any { it.name == "view_item_list" })
    }

    @Test
    fun catalogueRefreshAndRecompositionDoNotManufactureImpressions() = runTest(dispatcherExtension.testDispatcher) {
        val model = createViewModel()
        advance()
        assertFalse(telemetryRepository.loggedEvents.any { it.name == "view_item_list" })
        repeat(3) { model.onEvent(MoreAppsEvent.CarouselExposed) }
        model.onEvent(MoreAppsEvent.RefreshInstalled)
        model.onEvent(MoreAppsEvent.Load)
        advance()
        model.onEvent(MoreAppsEvent.CarouselExposed)
        assertEquals(1, telemetryRepository.loggedEvents.count { it.name == "view_item_list" })
        model.onEvent(MoreAppsEvent.CarouselHidden)
        model.onEvent(MoreAppsEvent.CarouselExposed)
        assertEquals(2, telemetryRepository.loggedEvents.count { it.name == "view_item_list" })
    }

    @Test
    fun anEmptyOrUnavailableCatalogueHasNoImpression() = runTest(dispatcherExtension.testDispatcher) {
        val model = createViewModel(FakeDeveloperAppsRepository(apps = emptyList()))
        advance()
        model.onEvent(MoreAppsEvent.CarouselExposed)
        model.onEvent(MoreAppsEvent.RefreshInstalled)
        assertTrue(telemetryRepository.loggedEvents.none { it.name == "view_item_list" })
    }
}
