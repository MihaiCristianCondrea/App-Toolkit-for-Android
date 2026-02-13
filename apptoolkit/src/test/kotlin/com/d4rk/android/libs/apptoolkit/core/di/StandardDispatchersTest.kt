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

package com.d4rk.android.libs.apptoolkit.core.di

import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.StandardDispatchers
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.StandardDispatcherExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(StandardDispatcherExtension::class)
class StandardDispatchersTest {

    private val dispatchers = StandardDispatchers()

    @Test
    fun `main returns DispatchersMain`() {
        assertThat(dispatchers.main).isEqualTo(Dispatchers.Main)
    }

    @Test
    fun `io returns DispatchersIO`() {
        assertThat(dispatchers.io).isEqualTo(Dispatchers.IO)
    }

    @Test
    fun `default returns DispatchersDefault`() {
        assertThat(dispatchers.default).isEqualTo(Dispatchers.Default)
    }

    @Test
    fun `unconfined returns DispatchersUnconfined`() {
        assertThat(dispatchers.unconfined).isEqualTo(Dispatchers.Unconfined)
    }
}
