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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.CLIPBOARD_HELPER_LOG_TAG
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
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

    @Disabled(
        "These stub Build.VERSION.SDK_INT with mockkStatic, which cannot intercept a static final field, so they have never passed, they were JUnit 4 in a JUnit-platform-only build and silently skipped. Porting them needs Robolectric's @Config(sdk = …) or an injectable SDK-level provider on the helper under test."
    )
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

    @Disabled(
        "These stub Build.VERSION.SDK_INT with mockkStatic, which cannot intercept a static final field, so they have never passed, they were JUnit 4 in a JUnit-platform-only build and silently skipped. Porting them needs Robolectric's @Config(sdk = …) or an injectable SDK-level provider on the helper under test."
    )
    @Test
    fun `copyTextToClipboard does not invoke callback on API 33`() {
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

    @Disabled(
        "These stub Build.VERSION.SDK_INT with mockkStatic, which cannot intercept a static final field, so they have never passed, they were JUnit 4 in a JUnit-platform-only build and silently skipped. Porting them needs Robolectric's @Config(sdk = …) or an injectable SDK-level provider on the helper under test."
    )
    @Test
    fun `copyTextToClipboard does not invoke callback on API above 33`() {
        val context = mockk<Context>()
        val clipboardManager = mockk<ClipboardManager>()
        every { context.getSystemService(ClipboardManager::class.java) } returns clipboardManager

        val clipDataSlot = slot<ClipData>()
        justRun { clipboardManager.setPrimaryClip(capture(clipDataSlot)) }

        mockkStatic(Build.VERSION::class)

        runCatchingFinally(
            block = {
                every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.UPSIDE_DOWN_CAKE

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

    @Disabled(
        "These stub Build.VERSION.SDK_INT with mockkStatic, which cannot intercept a static final field, so they have never passed, they were JUnit 4 in a JUnit-platform-only build and silently skipped. Porting them needs Robolectric's @Config(sdk = …) or an injectable SDK-level provider on the helper under test."
    )
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

    /**
     * Runs the helper against [clipboard]. The whole suite above is disabled, so these are the only
     * tests that actually exercise it; they assert what the helper can know, which is whether the
     * clipboard accepted the write. Confirming the write by reading the clipboard back is not
     * possible: a write needs no window focus, a read does, so a denied read is indistinguishable
     * from a dropped write.
     */
    private fun copyWith(clipboard: ClipboardManager?): Boolean {
        val context = mockk<Context>()
        every { context.getSystemService(ClipboardManager::class.java) } returns clipboard

        mockkStatic(ClipData::class)
        mockkStatic(Log::class)
        return runCatchingFinally(
            block = {
                every { ClipData.newPlainText(any(), any()) } returns mockk<ClipData>()
                every { Log.w(any<String>(), any<String>()) } returns 0
                every { Log.w(any<String>(), any<String>(), any()) } returns 0
                context.copyTextToClipboard(label = "label", text = "text")
            },
            finallyBlock = {
                unmockkStatic(Log::class)
                unmockkStatic(ClipData::class)
            },
        )
    }

    @Test
    fun `copyTextToClipboard reports success when the clipboard accepts the write`() {
        val clipboard = mockk<ClipboardManager>()
        justRun { clipboard.setPrimaryClip(any()) }

        assertTrue(copyWith(clipboard))
        verify(exactly = 1) { clipboard.setPrimaryClip(any()) }
    }

    @Test
    fun `copyTextToClipboard reports failure when the clipboard service is missing`() {
        assertFalse(copyWith(clipboard = null))
    }

    @Test
    fun `copyTextToClipboard reports failure when the clipboard throws`() {
        val clipboard = mockk<ClipboardManager>()
        every { clipboard.setPrimaryClip(any()) } throws SecurityException("denied")

        assertFalse(copyWith(clipboard))
    }
}
