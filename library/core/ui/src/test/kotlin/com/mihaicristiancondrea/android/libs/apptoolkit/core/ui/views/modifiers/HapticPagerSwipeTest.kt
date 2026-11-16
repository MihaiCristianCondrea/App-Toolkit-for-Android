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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** A pager ticks for the person's own swipes only, never for a slide the app starts. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class HapticPagerSwipeTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val ticks = mutableListOf<HapticFeedbackType>()
    private lateinit var pagerState: PagerState
    private lateinit var scope: CoroutineScope

    private fun setContent() {
        val haptics = object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) {
                ticks += hapticFeedbackType
            }
        }
        compose.setContent {
            CompositionLocalProvider(LocalHapticFeedback provides haptics) {
                pagerState = rememberPagerState { 3 }
                scope = rememberCoroutineScope()
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.size(200.dp).testTag("pager").hapticPagerSwipe(pagerState),
                ) { Box(Modifier.fillMaxSize()) }
            }
        }
        compose.waitForIdle()
    }

    @Test
    fun `a swipe to the next page ticks once`() {
        setContent()

        compose.onNodeWithTag("pager").performTouchInput { swipeLeft() }
        compose.waitForIdle()

        assertEquals(1, pagerState.currentPage)
        assertEquals(listOf(HapticFeedbackType.SegmentTick), ticks)
    }

    @Test
    fun `a slide the app starts gives no tick`() {
        setContent()

        scope.launch { pagerState.animateScrollToPage(2) }
        compose.waitForIdle()

        assertEquals(2, pagerState.currentPage)
        assertEquals(emptyList<HapticFeedbackType>(), ticks)
    }
}
