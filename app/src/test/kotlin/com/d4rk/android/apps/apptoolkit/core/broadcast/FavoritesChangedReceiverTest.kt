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

package com.d4rk.android.apps.apptoolkit.core.broadcast

import android.content.Context
import android.content.Intent
import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class FavoritesChangedReceiverTest {

    private val context = mockk<Context>(relaxed = true)
    private val receiver = FavoritesChangedReceiver()

    @Test
    fun `onReceive reads package name extra when present`() {
        val packageName = "com.example.app"
        val intent = mockk<Intent>()

        every { intent.getStringExtra(FavoritesChangedReceiver.EXTRA_PACKAGE_NAME) } returns packageName

        mockkStatic(Log::class)

        runCatching {
            every { Log.d(any(), any()) } returns 0

            receiver.onReceive(context, intent)

            verify(exactly = 1) { intent.getStringExtra(FavoritesChangedReceiver.EXTRA_PACKAGE_NAME) }
            verify { Log.d(any(), match { packageName in it }) }
        }.also {
            runCatching { unmockkStatic(Log::class) }
        }.getOrThrow()
    }

    @Test
    fun `onReceive completes without exception when package name extra missing`() {
        val intent = mockk<Intent>()

        every { intent.getStringExtra(FavoritesChangedReceiver.EXTRA_PACKAGE_NAME) } returns null

        mockkStatic(Log::class)

        runCatching {
            every { Log.d(any(), any()) } returns 0

            assertDoesNotThrow {
                receiver.onReceive(context, intent)
            }

            verify(exactly = 1) { intent.getStringExtra(FavoritesChangedReceiver.EXTRA_PACKAGE_NAME) }
            verify { Log.d(any(), match { "Favorites changed:" in it }) }
        }.also {
            runCatching { unmockkStatic(Log::class) }
        }.getOrThrow()
    }
}

