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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.effects

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.io.IOException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class DataStoreStateCollectorsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `onErrorReset value is preserved across subsequent recompositions`() {
        var tick by mutableIntStateOf(value = 0)
        val failingFlow = flow {
            emit(value = "loaded")
            throw IOException("read failed")
        }

        compose.setContent {
            val state by failingFlow.collectDataStoreState(
                initial = { "initial" },
                logTag = "DataStoreStateCollectorsTest",
                onErrorReset = { mutable -> mutable.value = "recovered" },
            )
            Text(text = "${state}_$tick")
        }

        compose.waitForIdle()
        compose.onNodeWithText(text = "recovered_0").assertExists()

        tick = 1
        compose.waitForIdle()
        compose.onNodeWithText(text = "recovered_1").assertExists()
    }

    @Test
    fun `initial provider is not re-evaluated on unrelated recompositions`() {
        var tick by mutableIntStateOf(value = 0)
        var initialCalls = 0
        val stableFlow = flowOf("value")

        compose.setContent {
            val state by stableFlow.collectDataStoreState(
                initial = {
                    initialCalls++
                    "initial"
                },
                logTag = "DataStoreStateCollectorsTest",
            )
            Text(text = "${state}_$tick")
        }

        compose.waitForIdle()
        assertEquals(1, initialCalls)

        tick = 1
        compose.waitForIdle()
        tick = 2
        compose.waitForIdle()

        assertEquals(1, initialCalls)
        compose.onNodeWithText(text = "value_2").assertExists()
    }
}
