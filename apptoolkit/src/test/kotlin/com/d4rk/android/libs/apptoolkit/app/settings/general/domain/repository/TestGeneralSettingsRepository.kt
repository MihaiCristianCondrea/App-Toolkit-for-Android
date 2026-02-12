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

package com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository

import com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository.GeneralSettingsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.coroutines.CoroutineContext

class TestGeneralSettingsRepository {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `getContentKey returns provided key`() = runTest(dispatcherExtension.testDispatcher) {
        val repository =
            GeneralSettingsRepositoryImpl(firebaseController = mockk<FirebaseController>(relaxed = true))
        val result = repository.getContentKey("valid").first()
        assertThat(result).isEqualTo("valid")
    }

    @Test
    fun `getContentKey throws on null key`() = runTest(dispatcherExtension.testDispatcher) {
        val repository =
            GeneralSettingsRepositoryImpl(firebaseController = mockk<FirebaseController>(relaxed = true))
        assertThrows<IllegalArgumentException> {
            repository.getContentKey(null).first()
        }
    }

    @Test
    fun `getContentKey throws on blank key`() = runTest(dispatcherExtension.testDispatcher) {
        val repository =
            GeneralSettingsRepositoryImpl(firebaseController = mockk<FirebaseController>(relaxed = true))
        assertThrows<IllegalArgumentException> {
            repository.getContentKey("").first()
        }
    }

    @Test
    fun `getContentKey uses provided dispatcher`() = runTest(dispatcherExtension.testDispatcher) {
        val repository =
            GeneralSettingsRepositoryImpl(firebaseController = mockk<FirebaseController>(relaxed = true))
        val result = repository.getContentKey("value").first()
        assertThat(result).isEqualTo("value")
    }
}

private class TrackingDispatcher : CoroutineDispatcher() {
    var dispatchCount: Int = 0
        private set

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatchCount++
        block.run()
    }
}
