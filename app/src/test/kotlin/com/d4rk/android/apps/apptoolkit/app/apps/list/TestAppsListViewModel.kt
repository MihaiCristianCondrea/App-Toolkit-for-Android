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

package com.d4rk.android.apps.apptoolkit.app.apps.list

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.StandardDispatcherExtension
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestAppsListViewModel : TestAppsListViewModelBase() {

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
                description = "Description $it",
                screenshots = emptyList(),
            )
        }
        setup(fetchApps = apps, dispatchers = TestDispatchers(dispatcherExtension.testDispatcher))
        viewModel.uiState.testSuccess(expectedSize = apps.size)
    }

    @Test
    fun `toggle favorite updates state`() = runTest(dispatcherExtension.testDispatcher) {
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
        toggleAndAssert(packageName = "pkg", expected = true)
        toggleAndAssert(packageName = "pkg", expected = false)
    }
}