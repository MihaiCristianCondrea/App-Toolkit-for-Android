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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * The bar is a different Material composable per style. Its buttons must survive a swap between
 * them, or a tab switch replays their entrance: the menu button and actions slid out and back in.
 */
@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ShellTopAppBarTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `the menu button and actions keep their state when the bar changes style`() {
        var style by mutableStateOf(TopBarStyle.Small)
        var navigationCompositions = 0
        var actionCompositions = 0
        compose.setContent {
            MaterialTheme {
                ShellTopAppBar(
                    style = style,
                    title = "Title",
                    navigationIcon = { remember { navigationCompositions++ } },
                    actions = { remember { actionCompositions++ } },
                    scrollBehavior = null,
                )
            }
        }
        compose.waitForIdle()

        style = TopBarStyle.CenterAligned
        compose.waitForIdle()
        style = TopBarStyle.Large
        compose.waitForIdle()

        assertEquals(1, navigationCompositions)
        assertEquals(1, actionCompositions)
    }
}
