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

package com.d4rk.android.apps.apptoolkit.app.apps.favorites

import app.cash.turbine.test
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.StandardDispatcherExtension
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class TestFavoriteAppsViewModel : TestFavoriteAppsViewModelBase() {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = StandardDispatcherExtension()
    }

    @Test
    fun `toggle favorite throws after load`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] toggle favorite throws after load")
        val apps = listOf(
            AppInfo(
                name = "App",
                packageName = "pkg",
                iconUrl = "url",
                description = "Description",
                screenshots = emptyList(),
            )
        )
        setup(
            fetchApps = apps,
            initialFavorites = emptySet(),
            toggleError = RuntimeException("fail"),
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )

        viewModel.favorites.test {
            awaitItem()
            viewModel.toggleFavorite("pkg")
            runCurrent()
            expectNoEvents()
            assertThat(viewModel.favorites.value.contains("pkg")).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        println("\uD83C\uDFC1 [TEST DONE] toggle favorite throws after load")
    }

    @Test
    fun `load favorites emits saved apps`() = runTest(dispatcherExtension.testDispatcher) {
        val apps = listOf(
            AppInfo(
                name = "App1",
                packageName = "pkg1",
                iconUrl = "url1",
                description = "Description 1",
                screenshots = emptyList(),
            ),
            AppInfo(
                name = "App2",
                packageName = "pkg2",
                iconUrl = "url2",
                description = "Description 2",
                screenshots = emptyList(),
            )
        )
        setup(
            fetchApps = apps,
            initialFavorites = setOf("pkg1"),
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )

        viewModel.uiState.test {
            val initial = awaitItem()
            assertThat(initial.screenState).isInstanceOf(ScreenState.IsLoading::class.java)

            val success = awaitItem()
            assertThat(success.screenState).isInstanceOf(ScreenState.Success::class.java)
            assertThat(success.data?.apps?.map { it.packageName }).containsExactly("pkg1")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggle favorite updates favorites flow`() = runTest(dispatcherExtension.testDispatcher) {
        val apps = listOf(
            AppInfo(
                name = "App",
                packageName = "pkg",
                iconUrl = "url",
                description = "Description",
                screenshots = emptyList(),
            )
        )
        setup(fetchApps = apps, dispatchers = TestDispatchers(dispatcherExtension.testDispatcher))

        viewModel.favorites.test {
            assertThat(awaitItem()).isEmpty()
            viewModel.toggleFavorite("pkg")
            assertThat(awaitItem()).containsExactly("pkg")
            viewModel.toggleFavorite("pkg")
            assertThat(awaitItem()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `load favorites with no saved apps shows no data`() =
        runTest(dispatcherExtension.testDispatcher) {
            val apps = listOf(
                AppInfo(
                    name = "App",
                    packageName = "pkg",
                    iconUrl = "url",
                    description = "Description",
                    screenshots = emptyList(),
                )
            )
            setup(
                fetchApps = apps,
                initialFavorites = emptySet(),
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
            )

            viewModel.uiState.test {
                awaitItem() // Initial loading
                val state = awaitItem()
                assertThat(state.screenState).isInstanceOf(ScreenState.NoData::class.java)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `removing last favorite shows no data state`() =
        runTest(dispatcherExtension.testDispatcher) {
            val apps = listOf(
                AppInfo(
                    name = "App",
                    packageName = "pkg",
                    iconUrl = "url",
                    description = "Description",
                    screenshots = emptyList(),
                )
            )
            setup(
                fetchApps = apps,
                initialFavorites = setOf("pkg"),
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
            )

            viewModel.uiState.test {
                awaitItem() // Initial loading
                val success = awaitItem()
                assertThat(success.screenState).isInstanceOf(ScreenState.Success::class.java)
                assertThat(success.data?.apps?.map { it.packageName }).containsExactly("pkg")

                viewModel.toggleFavorite("pkg")

                val noData = awaitItem()
                assertThat(noData.screenState).isInstanceOf(ScreenState.NoData::class.java)
                assertThat(noData.data?.apps).isEmpty()

                cancelAndIgnoreRemainingEvents()
            }
        }
}
