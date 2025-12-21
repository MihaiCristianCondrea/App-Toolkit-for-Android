package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
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

    @Test
    fun `copyTextToClipboard copies text and invokes callback for API 32`() {
        val context = mockk<Context>()
        val clipboardManager = mockk<ClipboardManager>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns clipboardManager

        val clipDataSlot = slot<ClipData>()
        justRun { clipboardManager.setPrimaryClip(capture(clipDataSlot)) }

        mockkStatic(Build.VERSION::class)
        try {
            every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.S_V2

            var callbackInvoked = false

            ClipboardHelper.copyTextToClipboard(
                context = context,
                label = "label",
                text = "text",
                onShowSnackbar = { callbackInvoked = true },
            )

            verify(exactly = 1) { clipboardManager.setPrimaryClip(any()) }
            assertEquals("label", clipDataSlot.captured.description.label.toString())
            assertEquals("text", clipDataSlot.captured.getItemAt(0).text.toString())
            assertTrue(callbackInvoked)
        } finally {
            unmockkStatic(Build.VERSION::class)
        }
    }

    @Test
    fun `copyTextToClipboard does not invoke callback on API above 32`() {
        val context = mockk<Context>()
        val clipboardManager = mockk<ClipboardManager>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns clipboardManager

        val clipDataSlot = slot<ClipData>()
        justRun { clipboardManager.setPrimaryClip(capture(clipDataSlot)) }

        mockkStatic(Build.VERSION::class)
        try {
            every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.TIRAMISU

            var callbackInvoked = false

            ClipboardHelper.copyTextToClipboard(
                context = context,
                label = "label",
                text = "text",
                onShowSnackbar = { callbackInvoked = true },
            )

            verify(exactly = 1) { clipboardManager.setPrimaryClip(any()) }
            assertEquals("label", clipDataSlot.captured.description.label.toString())
            assertEquals("text", clipDataSlot.captured.getItemAt(0).text.toString())
            assertFalse(callbackInvoked)
        } finally {
            unmockkStatic(Build.VERSION::class)
        }
    }

    @Test
    fun `copyTextToClipboard logs warning when clipboard service unavailable`() {
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns null

        mockkStatic(Log::class)
        try {
            every { Log.w("ClipboardHelper", "Clipboard service unavailable") } returns 0

            var callbackInvoked = false

            ClipboardHelper.copyTextToClipboard(
                context = context,
                label = "label",
                text = "text",
                onShowSnackbar = { callbackInvoked = true },
            )

            assertFalse(callbackInvoked)
            verify(exactly = 1) { Log.w("ClipboardHelper", "Clipboard service unavailable") }
        } finally {
            unmockkStatic(Log::class)
        }
    }
}
