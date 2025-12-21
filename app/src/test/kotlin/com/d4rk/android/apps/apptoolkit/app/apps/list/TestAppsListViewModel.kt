package com.d4rk.android.apps.apptoolkit.app.apps.list

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
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