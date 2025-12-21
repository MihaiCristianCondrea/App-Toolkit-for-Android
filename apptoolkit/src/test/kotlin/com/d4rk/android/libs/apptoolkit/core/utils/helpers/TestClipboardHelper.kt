package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestClipboardHelper {
    @Test
    fun `copyTextToClipboard sets primary clip and executes callback when appropriate`() {
        println("🚀 [TEST] copyTextToClipboard sets primary clip and executes callback when appropriate")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        val clipSlot = slot<ClipData>()
        justRun { manager.setPrimaryClip(capture(clipSlot)) }

        var callbackExecuted = false
        ClipboardHelper.copyTextToClipboard(context, "label", "text") { callbackExecuted = true }

        verify { manager.setPrimaryClip(any()) }
        assertEquals("label", clipSlot.captured.description.label)
        assertEquals("text", clipSlot.captured.getItemAt(0).text)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            assertTrue(callbackExecuted)
        } else {
            assertFalse(callbackExecuted)
        }
        println("🏁 [TEST DONE] copyTextToClipboard sets primary clip and executes callback when appropriate")
    }

    @Test
    fun `copyTextToClipboard handles missing manager gracefully`() {
        println("🚀 [TEST] copyTextToClipboard handles missing manager gracefully")
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns null

        ClipboardHelper.copyTextToClipboard(context, "l", "t")

        verify { context.getSystemService(Context.CLIPBOARD_SERVICE) }
        println("🏁 [TEST DONE] copyTextToClipboard handles missing manager gracefully")
    }

    @Test
    fun `copyTextToClipboard handles manager exception`() {
        println("🚀 [TEST] copyTextToClipboard handles manager exception")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        every { manager.setPrimaryClip(any()) } throws RuntimeException("boom")

        assertFailsWith<RuntimeException> {
            ClipboardHelper.copyTextToClipboard(context, "l", "t")
        }
        println("🏁 [TEST DONE] copyTextToClipboard handles manager exception")
    }

    @Test
    fun `copyTextToClipboard propagates IllegalStateException`() {
        println("🚀 [TEST] copyTextToClipboard propagates IllegalStateException")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        every { manager.setPrimaryClip(any()) } throws IllegalStateException("bad state")

        assertFailsWith<IllegalStateException> {
            ClipboardHelper.copyTextToClipboard(context, "l", "t")
        }
        println("🏁 [TEST DONE] copyTextToClipboard propagates IllegalStateException")
    }

    @Test
    fun `copyTextToClipboard propagates SecurityException`() {
        println("🚀 [TEST] copyTextToClipboard propagates SecurityException")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        every { manager.setPrimaryClip(any()) } throws SecurityException("no permission")

        assertFailsWith<SecurityException> {
            ClipboardHelper.copyTextToClipboard(context, "l", "t")
        }
        println("🏁 [TEST DONE] copyTextToClipboard propagates SecurityException")
    }

    @Test
    fun `copyTextToClipboard skips callback on Android T or newer`() {
        println("🚀 [TEST] copyTextToClipboard skips callback on Android T or newer")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        justRun { manager.setPrimaryClip(any()) }

        var executed = false
        ClipboardHelper.copyTextToClipboard(context, "l", "t") { executed = true }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            assertFalse(executed)
        } else {
            assertTrue(executed)
        }
        println("🏁 [TEST DONE] copyTextToClipboard skips callback on Android T or newer")
    }

    @Test
    fun `copyTextToClipboard propagates exception from callback`() {
        println("🚀 [TEST] copyTextToClipboard propagates exception from callback")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        justRun { manager.setPrimaryClip(any()) }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            assertFailsWith<RuntimeException> {
                ClipboardHelper.copyTextToClipboard(context, "l", "t") {
                    throw RuntimeException("callback failed")
                }
            }
        } else {
            ClipboardHelper.copyTextToClipboard(context, "l", "t") {
                throw RuntimeException("callback failed")
            }
        }

        verify { manager.setPrimaryClip(any()) }
        println("🏁 [TEST DONE] copyTextToClipboard propagates exception from callback")
    }

    @Test
    fun `copyTextToClipboard handles empty label and text`() {
        println("🚀 [TEST] copyTextToClipboard handles empty label and text")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        val clipSlot = slot<ClipData>()
        justRun { manager.setPrimaryClip(capture(clipSlot)) }

        ClipboardHelper.copyTextToClipboard(context, "", "")

        verify { manager.setPrimaryClip(any()) }
        assertEquals("", clipSlot.captured.description.label)
        assertEquals("", clipSlot.captured.getItemAt(0).text)
        println("🏁 [TEST DONE] copyTextToClipboard handles empty label and text")
    }

    @Test
    fun `copyTextToClipboard handles long label and text`() {
        println("🚀 [TEST] copyTextToClipboard handles long label and text")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        val clipSlot = slot<ClipData>()
        justRun { manager.setPrimaryClip(capture(clipSlot)) }

        val longLabel = "a".repeat(10000)
        val longText = "b".repeat(10000)
        ClipboardHelper.copyTextToClipboard(context, longLabel, longText)

        verify { manager.setPrimaryClip(any()) }
        assertEquals(longLabel, clipSlot.captured.description.label)
        assertEquals(longText, clipSlot.captured.getItemAt(0).text)
        println("🏁 [TEST DONE] copyTextToClipboard handles long label and text")
    }

    private fun setSdkInt(tempValue: Int, block: () -> Unit) {
        val field = Build.VERSION::class.java.getDeclaredField("SDK_INT")
        field.isAccessible = true
        val modifiersField = java.lang.reflect.Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        val originalModifiers = field.modifiers
        modifiersField.setInt(field, field.modifiers and java.lang.reflect.Modifier.FINAL.inv())
        val originalValue = field.get(null)
        field.set(null, tempValue)
        runCatching {
            block()
        }.also {
            field.set(null, originalValue)
            modifiersField.setInt(field, originalModifiers)
        }
    }

    @Test
    fun `copyTextToClipboard skips callback exactly on API 33`() {
        println("🚀 [TEST] copyTextToClipboard skips callback exactly on API 33")
        val manager = mockk<ClipboardManager>()
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns manager
        justRun { manager.setPrimaryClip(any()) }

        var executed = false
        setSdkInt(Build.VERSION_CODES.TIRAMISU) {
            ClipboardHelper.copyTextToClipboard(context, "l", "t") { executed = true }
        }

        assertFalse(executed)
        println("🏁 [TEST DONE] copyTextToClipboard skips callback exactly on API 33")
    }

    @Test
    fun `copyTextToClipboard handles unexpected clipboard service type`() {
        println("🚀 [TEST] copyTextToClipboard handles unexpected clipboard service type")
        val context = mockk<Context>()
        every { context.getSystemService(Context.CLIPBOARD_SERVICE) } returns "not a manager"

        ClipboardHelper.copyTextToClipboard(context, "l", "t")

        verify { context.getSystemService(Context.CLIPBOARD_SERVICE) }
        println("🏁 [TEST DONE] copyTextToClipboard handles unexpected clipboard service type")
    }
}

