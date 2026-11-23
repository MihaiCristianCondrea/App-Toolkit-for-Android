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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import java.io.IOException
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencePersistingExtensionsTest {

    @Test
    fun `a failed save is reported and later changes are still saved`() {
        val scope = TestScope(StandardTestDispatcher())
        val handle = Snapshot.registerGlobalWriteObserver { Snapshot.sendApplyNotifications() }
        try {
            val state = mutableStateOf(0)
            val saved = mutableListOf<Int>()
            val errors = mutableListOf<Throwable>()

            state.persistChanges(
                scope = scope,
                currentValue = { 0 },
                onPersist = { value ->
                    if (value == 1) throw IOException("disk")
                    saved += value
                },
                onError = { error, _ -> errors += error },
            )
            scope.advanceUntilIdle()

            state.value = 1
            Snapshot.sendApplyNotifications()
            scope.advanceUntilIdle()
            state.value = 2
            Snapshot.sendApplyNotifications()
            scope.advanceUntilIdle()

            assertEquals(1, errors.size)
            assertEquals(listOf(2), saved)
        } finally {
            handle.dispose()
        }
    }
}
