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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class RememberThemePreferencesStateTest {

    @Test
    fun `a storage failure falls back to the default`() = runTest {
        val failing = flow<Boolean> {
            throw StorageException(reason = StorageException.Reason.CORRUPT)
        }

        assertEquals(listOf(true), failing.orDefaultOnStorageFailure(default = true).toList())
    }

    @Test
    fun `values emitted before a storage failure are kept`() = runTest {
        val failing = flow {
            emit(false)
            throw StorageException(reason = StorageException.Reason.FAILED)
        }

        assertEquals(listOf(false, true), failing.orDefaultOnStorageFailure(default = true).toList())
    }

    @Test
    fun `other failures still propagate`() = runTest {
        val failing = flow<Boolean> { throw IllegalStateException("bug") }

        assertFailsWith<IllegalStateException> {
            failing.orDefaultOnStorageFailure(default = true).toList()
        }
    }
}
