package com.d4rk.android.libs.apptoolkit.app.about.data.repository

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.copyTextToClipboard
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestAboutRepositoryImpl {

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

    private fun repository(context: Context = mockk()): AboutRepositoryImpl =
        AboutRepositoryImpl(
            deviceProvider = deviceProvider,
            buildInfoProvider = buildInfoProvider,
            context = context,
        )

    @Test
    fun `getAboutInfo returns expected info`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = repository()

        val result: AboutInfo = repo.getAboutInfo()

        assertThat(result.appVersion).isEqualTo(buildInfoProvider.appVersion)
        assertThat(result.appVersionCode).isEqualTo(buildInfoProvider.appVersionCode)
        assertThat(result.deviceInfo).isEqualTo(deviceProvider.deviceInfo)
    }

    @Test
    fun `copyDeviceInfo delegates to copyTextToClipboard`() {
        val ctx = mockk<Context>(relaxed = true)
        val repo = repository(ctx)

        val extFile =
            "com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.ContextExtensionsKt"
        mockkStatic(extFile)

        try {
            val copied = runCatching {
                every { ctx.copyTextToClipboard(any(), any(), any()) } returns true
                repo.copyDeviceInfo("label", "info")
            }.getOrThrow()

            verify {
                ctx.copyTextToClipboard(
                    "label",
                    "info",
                    any()
                )
            }
            assertThat(copied).isTrue()
        } finally {
            unmockkStatic(extFile)
        }
    }
}
