package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.quicksettings

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class QuickSettingsMainSafetyTest {
    @Test
    fun `a caller need not dispatch provider or preference reads`() = runTest {
        val io = Executors.newSingleThreadExecutor { Thread(it, "tile-storage") }.asCoroutineDispatcher()
        mockkStatic(Settings.Secure::class)
        try {
            val preferences = mockk<SharedPreferences>()
            val context = mockk<Context>(relaxed = true)
            val dispatchers = mockk<DispatcherProvider>()
            every { dispatchers.io } returns io
            every { context.getSharedPreferences("quick_settings_tiles", Context.MODE_PRIVATE) } answers {
                assertEquals("tile-storage", Thread.currentThread().name)
                preferences
            }
            every { preferences.all } answers {
                assertEquals("tile-storage", Thread.currentThread().name)
                emptyMap<String, Any>()
            }
            every { Settings.Secure.getString(any(), any()) } answers {
                assertEquals("tile-storage", Thread.currentThread().name)
                null
            }

            val source = AndroidQuickSettingsTilesLocalDataSource(context, dispatchers)
            assertEquals(emptySet(), source.activeTileComponents())
        } finally {
            unmockkStatic(Settings.Secure::class)
            io.close()
        }
    }
}
