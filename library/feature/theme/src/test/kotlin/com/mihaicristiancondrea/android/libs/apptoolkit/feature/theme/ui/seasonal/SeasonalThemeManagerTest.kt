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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal

import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test

/**
 * `ComponentActivity.setContent` adopts the content view's first child when it is a `ComposeView`.
 * An overlay added to an activity that had not set its content yet became that child, and the app
 * crashed with "No NavigationEventDispatcher was provided" when navigation composed inside it.
 */
class SeasonalThemeManagerTest {

    private val manager = SeasonalThemeManager(application = mockk<Application>(relaxed = true))

    @Test
    fun `an activity without content yet gets no overlay, only a watcher`() {
        val content = contentView(childCount = 0)
        val activity = activityWith(content)

        manager.onActivityResumed(activity)

        verify(exactly = 0) { content.addView(any(), any<ViewGroup.LayoutParams>()) }
        verify(exactly = 1) { content.addOnLayoutChangeListener(any()) }
    }

    @Test
    fun `a layout before any content still adds nothing`() {
        val listener = slot<View.OnLayoutChangeListener>()
        val posted = slot<Runnable>()
        val content = contentView(childCount = 0)
        every { content.addOnLayoutChangeListener(capture(listener)) } returns Unit
        every { content.post(capture(posted)) } returns true
        val activity = activityWith(content)

        manager.onActivityResumed(activity)
        listener.captured.onLayoutChange(content, 0, 0, 0, 0, 0, 0, 0, 0)
        posted.captured.run()

        verify(exactly = 0) { content.addView(any(), any<ViewGroup.LayoutParams>()) }
    }

    @Test
    fun `content that already carries the overlay is left alone`() {
        val overlay = mockk<View>(relaxed = true) {
            every { tag } returns "com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.SeasonalOverlay"
        }
        val screen = mockk<View>(relaxed = true) { every { tag } returns null }
        val content = contentView(childCount = 2)
        every { content.getChildAt(0) } returns screen
        every { content.getChildAt(1) } returns overlay
        val activity = activityWith(content)

        manager.onActivityResumed(activity)

        verify(exactly = 0) { content.addView(any(), any<ViewGroup.LayoutParams>()) }
    }

    @Test
    fun `the content view is watched once however often the activity resumes`() {
        val content = contentView(childCount = 0)
        var watcher: Any? = null
        every { content.getTag(any<Int>()) } answers { watcher }
        every { content.setTag(any<Int>(), any()) } answers { watcher = secondArg() }
        val activity = activityWith(content)

        manager.onActivityResumed(activity)
        manager.onActivityResumed(activity)

        verify(exactly = 1) { content.addOnLayoutChangeListener(any()) }
    }

    private fun contentView(childCount: Int): ViewGroup = mockk(relaxed = true) {
        every { this@mockk.childCount } returns childCount
        every { getTag(any<Int>()) } returns null
    }

    private fun activityWith(content: ViewGroup): ComponentActivity = mockk(relaxed = true) {
        every { isFinishing } returns false
        every { isDestroyed } returns false
        every { findViewById<ViewGroup>(android.R.id.content) } returns content
    }
}
