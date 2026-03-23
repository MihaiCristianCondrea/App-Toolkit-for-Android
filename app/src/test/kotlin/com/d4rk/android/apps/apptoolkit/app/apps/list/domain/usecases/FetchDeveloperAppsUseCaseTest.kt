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

package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FetchDeveloperAppsUseCaseTest {

    @Test
    fun `use case mirrors repository emissions without prepending loading state`() = runTest {
        var flowCollected = false
        val apps = listOf(
            AppInfo(
                name = "App",
                packageName = "pkg",
                iconUrl = "icon",
                description = "Description",
                screenshots = emptyList(),
            )
        )
        val repositoryEmissions = listOf(
            DataState.Success(apps),
            DataState.Error<List<AppInfo>, AppErrors>(
                error = AppErrors.Common(Errors.Network.REQUEST_TIMEOUT)
            ),
        )
        val repository = mockk<DeveloperAppsRepository> {
            every { fetchDeveloperApps() } returns flow {
                flowCollected = true
                repositoryEmissions.asFlow().collect { emit(it) }
            }
        }
        val useCase = FetchDeveloperAppsUseCase(repository)

        val result = useCase().toList()

        assertEquals(repositoryEmissions, result)
        assertTrue(flowCollected)
        verify(exactly = 1) { repository.fetchDeveloperApps() }
    }

    @Test
    fun `use case propagates synchronous repository exceptions`() = runTest {
        val exception = IllegalStateException("boom")
        var flowCollected = false
        val repository = mockk<DeveloperAppsRepository> {
            every { fetchDeveloperApps() } returns flow {
                flowCollected = true
                throw exception
            }
        }
        val useCase = FetchDeveloperAppsUseCase(repository)

        val thrown = assertFailsWith<IllegalStateException> {
            useCase().toList()
        }

        assertSame(exception, thrown)
        assertTrue(flowCollected)
        verify(exactly = 1) { repository.fetchDeveloperApps() }
    }
}
