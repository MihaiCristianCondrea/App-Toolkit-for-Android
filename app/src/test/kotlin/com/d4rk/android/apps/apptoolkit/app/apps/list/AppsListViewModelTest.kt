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

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.d4rk.android.apps.apptoolkit.app.apps.list

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.contract.HomeEvent
import com.d4rk.android.apps.apptoolkit.app.apps.list.ui.state.AppsListFilter
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.StandardDispatcherExtension
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class AppsListViewModelTest : AppsListViewModelBaseTest() {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = StandardDispatcherExtension()
    }

    @Test
    fun `fetch apps - large list`() = runTest(dispatcherExtension.testDispatcher) {
        val apps = (1..10_000).map {
            AppInfo(
                name = "App$it",
                packageName = "pkg$it",
                iconUrl = "url$it",
                shortDescription = "Description $it",
            )
        }
        setup(fetchApps = apps, dispatchers = TestDispatchers(dispatcherExtension.testDispatcher))
        viewModel.uiState.testSuccess(expectedSize = apps.size)
    }


    @Test
    fun `select filter updates apps list state`() = runTest {
        val apps = listOf(
            AppInfo(
                name = "App",
                packageName = "pkg",
                iconUrl = "url",
                shortDescription = "Description",
            )
        )
        // Setup with 1 app and initial favorites so Favorites filter is valid
        setup(
            fetchApps = apps,
            initialFavorites = setOf("pkg"),
            installedPackages = setOf("pkg"),
            dispatchers = TestDispatchers(UnconfinedTestDispatcher())
        )

        // Favorites is valid because we have one favorite
        viewModel.onEvent(HomeEvent.FilterSelected(AppsListFilter.Favorites))

        assertEquals(AppsListFilter.Favorites, viewModel.uiState.value.data?.selectedFilter)
    }

    @Test
    fun `filter resets to All when current filter becomes invalid`() = runTest {
        val apps = listOf(
            AppInfo(
                name = "App",
                packageName = "pkg",
                iconUrl = "url",
                shortDescription = "Description",
            )
        )
        setup(
            fetchApps = apps,
            initialFavorites = setOf("pkg"),
            installedPackages = setOf("pkg"),
            dispatchers = TestDispatchers(UnconfinedTestDispatcher())
        )

        // Set to Favorites
        viewModel.onEvent(HomeEvent.FilterSelected(AppsListFilter.Favorites))
        assertEquals(AppsListFilter.Favorites, viewModel.uiState.value.data?.selectedFilter)

        // Remove favorite
        viewModel.toggleFavorite("pkg")
        advanceUntilIdle() // Wait for toggleFavorite job

        // Should reset to All
        assertEquals(AppsListFilter.All, viewModel.uiState.value.data?.selectedFilter)
    }

    @Test
    fun `toggle favorite updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val apps = listOf(
            AppInfo(
                name = "App",
                packageName = "pkg",
                iconUrl = "url",
                shortDescription = "Description",
            )
        )
        setup(fetchApps = apps, dispatchers = TestDispatchers(dispatcherExtension.testDispatcher))
        toggleAndAssert(packageName = "pkg", expected = true)
        toggleAndAssert(packageName = "pkg", expected = false)
    }

    @Test
    fun `selecting an app loads package details`() = runTest {
        val app = AppInfo(
            name = "App",
            packageName = "pkg",
            iconUrl = "url",
            shortDescription = "Expanded description",
        )
        setup(
            fetchApps = listOf(app),
            dispatchers = TestDispatchers(UnconfinedTestDispatcher()),
        )
        advanceUntilIdle()

        viewModel.onEvent(HomeEvent.AppSelected(packageName = app.packageName))
        advanceUntilIdle()

        val state = viewModel.uiState.value.data
        assertEquals(app, state?.selectedApp)
        assertEquals("Expanded description", state?.selectedAppDetails?.description)
        assertEquals(false, state?.isAppDetailsLoading)
        assertEquals(false, state?.hasAppDetailsError)
    }
}
