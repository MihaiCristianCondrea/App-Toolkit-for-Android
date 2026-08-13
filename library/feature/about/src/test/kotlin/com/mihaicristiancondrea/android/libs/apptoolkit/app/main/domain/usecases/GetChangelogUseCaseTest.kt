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

/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.app.main.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.data.repositories.ChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetChangelogUseCaseTest {

    @Test
    fun `current version section is preferred`() = runTest {
        val history = "# 2.0.0\n- Current\n# 1.0.0\n- Previous"
        val repository = FakeChangelogRepository(success(history))
        val useCase = GetChangelogUseCase(repository, buildInfo(version = "2.0.0"))

        val result = useCase().first()

        assertEquals(
            "# 2.0.0\n- Current",
            assertIs<DataState.Success<String, Errors>>(result).data,
        )
        assertEquals("com.example.app", repository.requestedPackage)
    }

    @Test
    fun `full history is shown when current version heading is absent`() = runTest {
        val history = "# 1.0.0\n- Previous"
        val useCase = GetChangelogUseCase(
            repository = FakeChangelogRepository(success(history)),
            buildInfoProvider = buildInfo(version = "2.0.0"),
        )

        val result = useCase().first()

        assertEquals(
            history,
            assertIs<DataState.Success<String, Errors>>(result).data,
        )
    }

    @Test
    fun `blank response remains blank for the localized no updates state`() = runTest {
        val useCase = GetChangelogUseCase(
            repository = FakeChangelogRepository(success("  ")),
            buildInfoProvider = buildInfo(version = "2.0.0"),
        )

        val result = useCase().first()

        assertEquals("", assertIs<DataState.Success<String, Errors>>(result).data)
    }

    private fun success(markdown: String): DataState<String, Errors> =
        DataState.Success(data = markdown)

    private fun buildInfo(version: String): BuildInfoProvider = object : BuildInfoProvider {
        override val appVersion: String = version
        override val appVersionCode: Int = 20
        override val packageName: String = "com.example.app"
        override val isDebugBuild: Boolean = false
    }
}

private class FakeChangelogRepository(
    private val result: DataState<String, Errors>,
) : ChangelogRepository {
    var requestedPackage: String? = null

    override fun fetchChangelog(packageName: String): Flow<DataState<String, Errors>> {
        requestedPackage = packageName
        return flowOf(result)
    }
}
