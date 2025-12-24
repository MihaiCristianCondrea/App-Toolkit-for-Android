package com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository

import com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository.GeneralSettingsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
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
            GeneralSettingsRepositoryImpl()
        val result = repository.getContentKey("valid").first()
        assertThat(result).isEqualTo("valid")
    }

    @Test
    fun `getContentKey throws on null key`() = runTest(dispatcherExtension.testDispatcher) {
        val repository =
            GeneralSettingsRepositoryImpl()
        assertThrows<IllegalArgumentException> {
            repository.getContentKey(null).first()
        }
    }

    @Test
    fun `getContentKey throws on blank key`() = runTest(dispatcherExtension.testDispatcher) {
        val repository =
            GeneralSettingsRepositoryImpl()
        assertThrows<IllegalArgumentException> {
            repository.getContentKey("").first()
        }
    }

    @Test
    fun `getContentKey uses provided dispatcher`() = runTest(dispatcherExtension.testDispatcher) {
        val trackingDispatcher = TrackingDispatcher()
        val repository = GeneralSettingsRepositoryImpl()

        val result = repository.getContentKey("value").first()

        assertThat(result).isEqualTo("value")
        assertThat(trackingDispatcher.dispatchCount).isGreaterThan(0)
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
