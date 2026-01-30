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

class FetchDeveloperAppsUseCaseTest {

    @Test
    fun `use case prepends loading state to repository emissions`() = runTest {
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
            every { fetchDeveloperApps() } returns repositoryEmissions.asFlow()
        }
        val useCase = FetchDeveloperAppsUseCase(repository)

        val result = useCase().toList()

        val expected = mutableListOf<DataState<List<AppInfo>, AppErrors>>(
            DataState.Loading(),
        )
        expected.addAll(repositoryEmissions)

        assertEquals(expected, result)
        verify(exactly = 1) { repository.fetchDeveloperApps() }
    }

    @Test
    fun `use case propagates synchronous repository exceptions`() = runTest {
        val exception = IllegalStateException("boom")
        val repository = mockk<DeveloperAppsRepository> {
            every { fetchDeveloperApps() } returns flow { throw exception }
        }
        val useCase = FetchDeveloperAppsUseCase(repository)

        val thrown = assertFailsWith<IllegalStateException> {
            useCase().toList()
        }

        assertSame(exception, thrown)
        verify(exactly = 1) { repository.fetchDeveloperApps() }
    }
}
