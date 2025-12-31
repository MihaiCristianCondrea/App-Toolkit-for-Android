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

