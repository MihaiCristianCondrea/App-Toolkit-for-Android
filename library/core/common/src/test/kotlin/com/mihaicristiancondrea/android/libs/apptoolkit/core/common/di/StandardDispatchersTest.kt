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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.StandardDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.StandardDispatcherExtension
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(StandardDispatcherExtension::class)
class StandardDispatchersTest {

    private val dispatchers = StandardDispatchers()

    @Test
    fun `main returns DispatchersMain`() {
        assertEquals(Dispatchers.Main, dispatchers.main)
    }

    @Test
    fun `io returns DispatchersIO`() {
        assertEquals(Dispatchers.IO, dispatchers.io)
    }

    @Test
    fun `default returns DispatchersDefault`() {
        assertEquals(Dispatchers.Default, dispatchers.default)
    }

    @Test
    fun `unconfined returns DispatchersUnconfined`() {
        assertEquals(Dispatchers.Unconfined, dispatchers.unconfined)
    }
}
