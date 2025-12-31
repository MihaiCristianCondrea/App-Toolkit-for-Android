package com.d4rk.android.libs.apptoolkit.core.utils.extensions.context

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.d4rk.android.libs.apptoolkit.core.logging.CLIPBOARD_HELPER_LOG_TAG
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClipboardHelperTest {

    private inline fun <T> runCatchingFinally(
        block: () -> T,
        finallyBlock: () -> Unit
    ): T {
        val result = runCatching(block)
        val cleanupError = runCatching(finallyBlock).exceptionOrNull()

        if (cleanupError != null) {
            result.exceptionOrNull()?.let { primary ->
                primary.addSuppressed(cleanupError)
                throw primary
            }
            throw cleanupError
        }

        return result.getOrThrow()
    }

    @Test
    fun `copyTextToClipboard copies text and invokes callback for API 32`() {
        val context = mockk<Context>()
        val clipboardManager = mockk<ClipboardManager>()
        every { context.getSystemService(ClipboardManager::class.java) } returns clipboardManager

        val clipDataSlot = slot<ClipData>()
        justRun { clipboardManager.setPrimaryClip(capture(clipDataSlot)) }

        mockkStatic(Build.VERSION::class)

        runCatchingFinally(
            block = {
                every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.S_V2

                var callbackInvoked = false

                val result = context.copyTextToClipboard(
                    label = "label",
                    text = "text",
                    onCopyFallback = { callbackInvoked = true },
                )

                verify(exactly = 1) { clipboardManager.setPrimaryClip(any()) }
                assertEquals("label", clipDataSlot.captured.description.label.toString())
                assertEquals("text", clipDataSlot.captured.getItemAt(0).text.toString())
                assertTrue(callbackInvoked)
                assertTrue(result)
            },
            finallyBlock = {
                unmockkStatic(Build.VERSION::class)
            }
        )
    }

    @Test
    fun `copyTextToClipboard does not invoke callback on API above 32`() {
        val context = mockk<Context>()
        val clipboardManager = mockk<ClipboardManager>()
        every { context.getSystemService(ClipboardManager::class.java) } returns clipboardManager

        val clipDataSlot = slot<ClipData>()
        justRun { clipboardManager.setPrimaryClip(capture(clipDataSlot)) }

        mockkStatic(Build.VERSION::class)

        runCatchingFinally(
            block = {
                every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.TIRAMISU

                var callbackInvoked = false

                val result = context.copyTextToClipboard(
                    label = "label",
                    text = "text",
                    onCopyFallback = { callbackInvoked = true },
                )

                verify(exactly = 1) { clipboardManager.setPrimaryClip(any()) }
                assertEquals("label", clipDataSlot.captured.description.label.toString())
                assertEquals("text", clipDataSlot.captured.getItemAt(0).text.toString())
                assertFalse(callbackInvoked)
                assertTrue(result)
            },
            finallyBlock = {
                unmockkStatic(Build.VERSION::class)
            }
        )
    }

    @Test
    fun `copyTextToClipboard logs warning when clipboard service unavailable`() {
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns null

        mockkStatic(Log::class)

        runCatchingFinally(
            block = {
                every { Log.w(CLIPBOARD_HELPER_LOG_TAG, "Clipboard service unavailable") } returns 0

                var callbackInvoked = false

                val result = context.copyTextToClipboard(
                    label = "label",
                    text = "text",
                    onCopyFallback = { callbackInvoked = true },
                )

                assertFalse(callbackInvoked)
                verify(exactly = 1) {
                    Log.w(
                        CLIPBOARD_HELPER_LOG_TAG,
                        "Clipboard service unavailable"
                    )
                }
                assertFalse(result)
            },
            finallyBlock = {
                unmockkStatic(Log::class)
            }
        )
    }
}
