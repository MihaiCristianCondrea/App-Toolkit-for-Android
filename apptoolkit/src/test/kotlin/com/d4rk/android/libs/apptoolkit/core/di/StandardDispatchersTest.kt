package com.d4rk.android.libs.apptoolkit.core.di

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
