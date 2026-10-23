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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class MessageToastHostTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun messagesArePostedInOrderAndAcknowledgedOnce() {
        val first = UiMessage(UiText.DynamicString("First"))
        val second = UiMessage(UiText.DynamicString("Second"), isError = true)
        var queue by mutableStateOf(persistentListOf(first, second))
        val shown = mutableListOf<Long>()
        compose.setContent {
            MessageToastHost(messages = queue, onShown = { id ->
                shown += id
                queue = queue.removeAt(0)
            })
        }
        compose.runOnIdle {
            assertEquals(listOf(first.id, second.id), shown)
            assertEquals(2, ShadowToast.shownToastCount())
            assertEquals("Second", ShadowToast.getTextOfLatestToast())
            assertEquals(Toast.LENGTH_LONG, ShadowToast.getLatestToast().duration)
        }
    }

    @Test
    fun theTilesHostCanKeepItsShortErrorDuration() {
        val error = UiMessage(UiText.DynamicString("Error"), isError = true)
        compose.setContent {
            MessageToastHost(
                messages = persistentListOf(error),
                onShown = {},
                errorDuration = Toast.LENGTH_SHORT,
            )
        }
        compose.runOnIdle {
            assertEquals(Toast.LENGTH_SHORT, ShadowToast.getLatestToast().duration)
            assertEquals(1, ShadowToast.shownToastCount())
        }
    }
}
