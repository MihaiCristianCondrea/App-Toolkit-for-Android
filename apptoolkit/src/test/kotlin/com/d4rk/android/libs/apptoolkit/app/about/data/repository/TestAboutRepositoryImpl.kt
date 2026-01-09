package com.d4rk.android.libs.apptoolkit.app.about.data.repository

import android.content.Context
import android.os.Build
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.CopyDeviceInfoResult
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.copyTextToClipboard
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
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

    /*
    FIXME:

    Missing mocked calls inside every { ... } block: make sure the object inside the block is a mock
Note: if you tried to stub a Kotlin inline function, it cannot be mocked. Inline functions are inlined at call sites, so no call is recorded. Extract a non-inline wrapper or mock the dependencies used inside the inline function.
io.mockk.MockKException: Missing mocked calls inside every { ... } block: make sure the object inside the block is a mock
Note: if you tried to stub a Kotlin inline function, it cannot be mocked. Inline functions are inlined at call sites, so no call is recorded. Extract a non-inline wrapper or mock the dependencies used inside the inline function.
	at app//io.mockk.impl.recording.states.StubbingState.checkMissingCalls(StubbingState.kt:14)
	at app//io.mockk.impl.recording.states.StubbingState.recordingDone(StubbingState.kt:8)
	at app//io.mockk.impl.recording.CommonCallRecorder.done(CommonCallRecorder.kt:47)
	at app//io.mockk.impl.eval.RecordedBlockEvaluator.record(RecordedBlockEvaluator.kt:63)
	at app//io.mockk.impl.eval.EveryBlockEvaluator.every(EveryBlockEvaluator.kt:30)
	at app//io.mockk.MockKDsl.internalEvery(API.kt:95)
	at app//io.mockk.MockKKt.every(MockK.kt:148)
	at app//com.d4rk.android.libs.apptoolkit.app.about.data.repository.TestAboutRepositoryImpl.copyDeviceInfo delegates to copyTextToClipboard(TestAboutRepositoryImpl.kt:70)


    */
    @Test
    fun `copyDeviceInfo delegates to copyTextToClipboard`() {
        val context = mockk<Context>(relaxed = true)
        val repo = repository(context)

        val extFile =
            "com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.ContextClipboardExtensionsKt"
        mockkStatic(extFile)
        mockkStatic(Build.VERSION::class)

        try {
            every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.S_V2

            val fallbackSlot = slot<() -> Unit>()
            val copyResult = run {
                every {
                    context.copyTextToClipboard(
                        "label",
                        "info",
                        isSensitive = false,
                        onCopyFallback = capture(fallbackSlot)
                    )
                } answers {
                    fallbackSlot.captured.invoke()
                    true
                }
                repo.copyDeviceInfo("label", "info")
            }

            verify {
                context.copyTextToClipboard(
                    "label",
                    "info",
                    isSensitive = false,
                    onCopyFallback = any(),
                ) // FIXME: The result of `copyTextToClipboard` is not used
            }
            assertThat(copyResult).isEqualTo(
                CopyDeviceInfoResult(
                    copied = true,
                    shouldShowFeedback = true
                )
            )
        } finally {
            unmockkStatic(Build.VERSION::class)
            unmockkStatic(extFile)
        }
    }
}
