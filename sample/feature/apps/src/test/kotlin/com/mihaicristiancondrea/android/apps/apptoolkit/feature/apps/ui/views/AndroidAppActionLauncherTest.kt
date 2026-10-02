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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views

import android.content.Context
import android.widget.Toast
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.ClipboardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AndroidAppActionLauncherTest {

    private val context = mockk<Context>()
    private val toast = mockk<Toast>(relaxed = true)

    @BeforeEach
    fun setUp() {
        every { context.getString(R.string.app_details_package_name_clip_label) } returns "Package name"
        every { context.getString(R.string.app_details_package_name_copied) } returns "Package name copied"
        mockkStatic(Toast::class)
        every { Toast.makeText(context, any<CharSequence>(), Toast.LENGTH_SHORT) } returns toast
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Toast::class)
    }

    @Test
    fun `a successful copy uses only the system confirmation when available`() {
        val clipboard = FakeClipboardRepository(confirmsCopies = true)
        val launcher = AndroidAppActionLauncher(context = context, clipboardRepository = clipboard)

        assertTrue(launcher.copyPackageName("com.example.app"))

        assertEquals(listOf("Package name" to "com.example.app"), clipboard.entries)
        verify(exactly = 0) { Toast.makeText(context, any<CharSequence>(), any()) }
        verify(exactly = 0) { toast.show() }
    }

    @Test
    fun `a successful copy shows one toast when the system does not confirm it`() {
        val clipboard = FakeClipboardRepository(confirmsCopies = false)
        val launcher = AndroidAppActionLauncher(context = context, clipboardRepository = clipboard)

        assertTrue(launcher.copyPackageName("com.example.app"))

        assertEquals(listOf("Package name" to "com.example.app"), clipboard.entries)
        verify(exactly = 1) { Toast.makeText(context, "Package name copied", Toast.LENGTH_SHORT) }
        verify(exactly = 1) { toast.show() }
    }

    @Test
    fun `an invalid package is not copied or confirmed`() {
        val clipboard = FakeClipboardRepository(confirmsCopies = false)
        val launcher = AndroidAppActionLauncher(context = context, clipboardRepository = clipboard)

        assertFalse(launcher.copyPackageName(""))

        assertTrue(clipboard.entries.isEmpty())
        verify(exactly = 0) { Toast.makeText(context, any<CharSequence>(), any()) }
    }

    @Test
    fun `a rejected copy never shows a success toast`() {
        val clipboard = FakeClipboardRepository(
            confirmsCopies = false,
            failure = IllegalStateException("Clipboard rejected the write"),
        )
        val launcher = AndroidAppActionLauncher(context = context, clipboardRepository = clipboard)

        assertFailsWith<IllegalStateException> { launcher.copyPackageName("com.example.app") }

        assertTrue(clipboard.entries.isEmpty())
        verify(exactly = 0) { Toast.makeText(context, any<CharSequence>(), any()) }
    }

    private class FakeClipboardRepository(
        override val confirmsCopies: Boolean,
        private val failure: Throwable? = null,
    ) : ClipboardRepository {
        val entries = mutableListOf<Pair<String, String>>()

        override fun copyText(label: String, text: String, isSensitive: Boolean) {
            failure?.let { throw it }
            entries += label to text
        }
    }
}
