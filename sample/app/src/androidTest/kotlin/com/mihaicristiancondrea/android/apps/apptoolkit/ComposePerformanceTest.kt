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

package com.mihaicristiancondrea.android.apps.apptoolkit

import android.text.Spanned
import android.text.style.URLSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.shimmerEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.text.HtmlText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs

@RunWith(AndroidJUnit4::class)
class ComposePerformanceTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun htmlKeepsSpansAcrossStylingChangesAndUpdatesChangedContent() {
        val dark = mutableStateOf(false)
        val html = mutableStateOf("<b>First</b> <a href=\"https://example.com/first\">link</a>")
        lateinit var root: View
        compose.setContent {
            val view = LocalView.current
            SideEffect { root = view.rootView }
            MaterialTheme(colorScheme = if (dark.value) darkColorScheme() else lightColorScheme()) {
                HtmlText(html.value, style = MaterialTheme.typography.bodyMedium.copy(fontSize = if (dark.value) 20.sp else 14.sp))
            }
        }
        lateinit var textView: TextView
        lateinit var original: CharSequence
        compose.runOnIdle {
            textView = checkNotNull(findHtmlView(root))
            original = textView.text
            assertEquals("First link", original.toString())
            assertEquals("https://example.com/first", (original as Spanned).getSpans(0, original.length, URLSpan::class.java).single().url)
            dark.value = true
        }
        compose.runOnIdle {
            assertSame(original, textView.text)
            html.value = "<i>Second</i> <a href=\"https://example.com/second\">link</a>"
        }
        compose.runOnIdle {
            val updated = textView.text as Spanned
            assertEquals("Second link", updated.toString())
            assertEquals("https://example.com/second", updated.getSpans(0, updated.length, URLSpan::class.java).single().url)
        }
    }

    @Test
    fun cachedShimmerMatchesOriginalGradientThroughoutCycle() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            MaterialTheme {
                val colors = listOf(
                    MaterialTheme.colorScheme.surfaceContainer,
                    MaterialTheme.colorScheme.outlineVariant,
                    MaterialTheme.colorScheme.surfaceContainerHighest,
                    MaterialTheme.colorScheme.surfaceContainer,
                )
                val progress = rememberInfiniteTransition().animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing), RepeatMode.Restart),
                )
                Column {
                    Box(Modifier.size(96.dp, 72.dp).testTag("cached").shimmerEffect())
                    Box(Modifier.size(96.dp, 72.dp).testTag("reference").drawBehind {
                        val start = -2f * size.width + 4f * size.width * progress.value
                        drawRect(Brush.linearGradient(colors, Offset(start, 0f), Offset(start + size.width, size.height)))
                    })
                }
            }
        }
        repeat(5) {
            val actual = compose.onNodeWithTag("cached").captureToImage().toPixelMap()
            val expected = compose.onNodeWithTag("reference").captureToImage().toPixelMap()
            assertEquals(expected.width, actual.width)
            assertEquals(expected.height, actual.height)
            for (y in 0 until actual.height step 4) {
                for (x in 0 until actual.width step 4) {
                    val a = actual[x, y]
                    val e = expected[x, y]
                    assertTrue("Shimmer changed at frame $it, ($x, $y)",
                        abs(a.red - e.red) < 0.02f && abs(a.green - e.green) < 0.02f && abs(a.blue - e.blue) < 0.02f)
                }
            }
            compose.mainClock.advanceTimeBy(368)
        }
    }

    private fun findHtmlView(view: View): TextView? {
        if (view is TextView && view.text.toString() == "First link") return view
        if (view is ViewGroup) {
            for (index in 0 until view.childCount) {
                findHtmlView(view.getChildAt(index))?.let { return it }
            }
        }
        return null
    }
}
