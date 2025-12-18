package com.d4rk.android.libs.apptoolkit.app.about.data

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ClipboardHelper
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestDefaultAboutRepository {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val deviceProvider = object : AboutSettingsProvider {
        override val deviceInfo: String = "device-info"
    }

    private val buildInfoProvider = object : BuildInfoProvider {
        override val appVersion: String = "1.0"
        override val appVersionCode: Int = 1
        override val packageName: String = "pkg"
        override val isDebugBuild: Boolean = false
    }

    private fun repository(context: Context = mockk()): DefaultAboutRepository =
        DefaultAboutRepository(
            deviceProvider = deviceProvider,
            configProvider = buildInfoProvider,
            context = context,
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )

    @Test
    fun `getAboutInfoStream emits expected info`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = repository()
        val result: AboutUiState = repo.getAboutInfoStream().first()
        assertThat(result.appVersion).isEqualTo(buildInfoProvider.appVersion)
        assertThat(result.appVersionCode).isEqualTo(buildInfoProvider.appVersionCode)
        assertThat(result.deviceInfo).isEqualTo(deviceProvider.deviceInfo)
    }

    @Test
    fun `copyDeviceInfo delegates to ClipboardHelper`() = runTest(dispatcherExtension.testDispatcher) {
        val ctx = mockk<Context>(relaxed = true)
        val repo = repository(ctx)
        mockkObject(ClipboardHelper)
        try {
            every { ClipboardHelper.copyTextToClipboard(ctx, any(), any(), any()) } returns Unit
            repo.copyDeviceInfo("label", "info")
            verify { ClipboardHelper.copyTextToClipboard(ctx, "label", "info", any()) }
        } finally {
            unmockkObject(ClipboardHelper)
        }
    }
}
