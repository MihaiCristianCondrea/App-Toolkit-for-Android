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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4ContractValidator
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.HomeEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.AppsListFilter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.StandardDispatcherExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pins the events the apps list emits to the schema `:sample:core:analytics` declares.
 *
 * The GA4 helper extensions live in the reusable library and therefore spell their parameter keys
 * as literals; `AppGa4Contract` is the app-owned vocabulary for the same keys. Without this test
 * the two can drift silently, because a wrong parameter name still produces a valid GA4 call.
 */
class AppsListAnalyticsContractTest : AppsListViewModelBaseTest() {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = StandardDispatcherExtension()

        private val APPS = listOf(
            AppInfo(
                name = "App Toolkit",
                packageName = "com.mihaicristiancondrea.apptoolkit",
                iconUrl = "https://example.com/icon.png",
                category = AppCategory(label = "Tools", id = "tools"),
            ),
        )
    }

    private fun AnalyticsEvent.assertSatisfiesContract() {
        assertTrue(
            AppGa4ContractValidator.isValidEventName(name),
            "Invalid GA4 event name: $name",
        )
        assertEquals(
            emptySet(),
            AppGa4ContractValidator.missingRequiredParams(eventName = name, params = params.keys),
            "Event $name is missing required parameters",
        )
        assertEquals(
            emptySet(),
            AppGa4ContractValidator.forbiddenParams(params.keys),
            "Event $name carries forbidden parameters",
        )
    }

    @Test
    fun `every logged event satisfies the app GA4 contract`() =
        runTest(dispatcherExtension.testDispatcher) {
            setup(fetchApps = APPS)
            advanceUntilIdle()
            viewModel.onEvent(HomeEvent.FilterSelected(AppsListFilter.Installed))
            viewModel.onEvent(HomeEvent.AppSelected(APPS.first().packageName))
            advanceUntilIdle()

            assertTrue(firebaseController.loggedEvents.isNotEmpty(), "No events were logged")
            firebaseController.loggedEvents.forEach { it.assertSatisfiesContract() }
        }

    @Test
    fun `view_item and view_item_list use the declared parameter names`() =
        runTest(dispatcherExtension.testDispatcher) {
            setup(fetchApps = APPS)
            advanceUntilIdle()
            viewModel.onEvent(HomeEvent.AppSelected(APPS.first().packageName))
            advanceUntilIdle()

            val viewItem = firebaseController.loggedEvents
                .single { it.name == AppGa4Contract.EventName.VIEW_ITEM }
            assertTrue(AppGa4Contract.Param.ITEM_CATEGORY in viewItem.params)

            val viewItemList = firebaseController.loggedEvents
                .first { it.name == AppGa4Contract.EventName.VIEW_ITEM_LIST }
            assertTrue(AppGa4Contract.Param.ITEM_LIST_ID in viewItemList.params)
            assertTrue(AppGa4Contract.Param.ITEM_LIST_NAME in viewItemList.params)
        }
}
