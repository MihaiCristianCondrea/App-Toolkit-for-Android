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

package com.d4rk.android.apps.apptoolkit.app.main.data.repository

import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.main.data.repository.MainRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainRepositoryImplTest {

    @Test
    fun `getNavigationDrawerItems emits expected items`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = MainRepositoryImpl(TestDispatchers(dispatcher))

        val items = repository.getNavigationDrawerItems().first()

        assertEquals(4, items.size)
        assertEquals(
            listOf(
                R.string.settings,
                R.string.help_and_feedback,
                R.string.updates,
                R.string.share
            ),
            items.map(NavigationDrawerItem::title)
        )
        assertEquals(
            listOf(
                NavigationDrawerRoutes.ROUTE_SETTINGS,
                NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
                NavigationDrawerRoutes.ROUTE_UPDATES,
                NavigationDrawerRoutes.ROUTE_SHARE,
            ),
            items.map(NavigationDrawerItem::route)
        )
    }
}
