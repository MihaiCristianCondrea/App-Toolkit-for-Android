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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.geometry.Offset
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.HolidaySeason
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * The holiday greeting: what each answer reports, and how it looks for both seasons, in light and
 * dark mode, on a compact phone, a phone, a landscape phone and a tablet, and with a large font.
 * The screenshots are the reference images in `src/test/screenshots`.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = PHONE)
class HolidayGreetingDialogTest {

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val answers = mutableListOf<Boolean>()

    private fun showContent(season: HolidaySeason) {
        compose.setContent {
            MaterialTheme { HolidayGreetingContent(season = season, onAnswer = { answers += it }) }
        }
    }

    @Test
    fun `the filled button takes the holiday theme`() {
        showContent(HolidaySeason.CHRISTMAS)
        compose.onNodeWithText("Let it snow!").performClick()
        assertEquals(listOf(true), answers)
    }

    @Test
    fun `the outlined button keeps the current theme`() {
        showContent(HolidaySeason.HALLOWEEN)
        compose.onNodeWithText("Keep my theme").performClick()
        assertEquals(listOf(false), answers)
    }

    @Test
    fun `a tap beside the card keeps the current theme`() {
        showContent(HolidaySeason.CHRISTMAS)
        compose.onRoot().performTouchInput { click(topLeft + Offset(4f, 4f)) }
        assertEquals(listOf(false), answers)
    }

    @Test
    fun `a tap on the card's text answers nothing`() {
        showContent(HolidaySeason.HALLOWEEN)
        compose.onNodeWithText("The pumpkins are awake!").performClick()
        assertEquals(emptyList<Boolean>(), answers)
    }

    @Test
    fun `the title is a heading and the illustration is not announced`() {
        showContent(HolidaySeason.CHRISTMAS)
        compose.onNodeWithText("Brrr… Christmas is here!")
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading))
            .assertIsDisplayed()
        compose.onAllNodes(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Image)).assertCountEquals(0)
    }

    @Test
    fun `back keeps the current theme, and the dialog answers only once`() {
        compose.setContent {
            MaterialTheme {
                HolidayGreetingDialog(season = HolidaySeason.CHRISTMAS, onAnswer = { answers += it })
            }
        }
        compose.onNode(isDialog()).assertExists()
        Espresso.pressBack()
        compose.waitForIdle()
        assertEquals(listOf(false), answers)
    }

    @Test
    fun `a button then back answers once`() {
        compose.setContent {
            MaterialTheme {
                HolidayGreetingDialog(season = HolidaySeason.HALLOWEEN, onAnswer = { answers += it })
            }
        }
        compose.onNodeWithText("Get spooky!").performClick()
        Espresso.pressBack()
        compose.waitForIdle()
        assertEquals(listOf(true), answers)
    }

    @Test
    fun `christmas on a phone`() = capture(HolidaySeason.CHRISTMAS, "christmas_phone")

    @Test
    fun `halloween on a phone`() = capture(HolidaySeason.HALLOWEEN, "halloween_phone")

    @Test
    @Config(qualifiers = PHONE_NIGHT)
    fun `christmas on a phone in dark mode`() = capture(HolidaySeason.CHRISTMAS, "christmas_phone_dark")

    @Test
    @Config(qualifiers = PHONE_NIGHT)
    fun `halloween on a phone in dark mode`() = capture(HolidaySeason.HALLOWEEN, "halloween_phone_dark")

    @Test
    @Config(qualifiers = COMPACT)
    fun `christmas on a compact phone`() = capture(HolidaySeason.CHRISTMAS, "christmas_compact")

    @Test
    @Config(qualifiers = COMPACT)
    fun `halloween on a compact phone`() = capture(HolidaySeason.HALLOWEEN, "halloween_compact")

    @Test
    @Config(qualifiers = LANDSCAPE)
    fun `christmas on a landscape phone`() = capture(HolidaySeason.CHRISTMAS, "christmas_landscape")

    @Test
    @Config(qualifiers = TABLET)
    fun `halloween on a tablet`() = capture(HolidaySeason.HALLOWEEN, "halloween_tablet")

    @Test
    fun `christmas with a large font`() {
        RuntimeEnvironment.setFontScale(2f)
        capture(HolidaySeason.CHRISTMAS, "christmas_large_font")
    }

    @Test
    fun `halloween with a large font`() {
        RuntimeEnvironment.setFontScale(2f)
        capture(HolidaySeason.HALLOWEEN, "halloween_large_font")
    }

    private fun capture(season: HolidaySeason, name: String) {
        compose.setContent {
            // The app's own scheme, light or dark, around the greeting, which keeps its season's colours.
            MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
                HolidayGreetingContent(season = season, onAnswer = {})
            }
        }
        compose.onRoot().captureRoboImage("src/test/screenshots/holiday_greeting_$name.png")
    }
}

private const val PHONE = "w411dp-h891dp-xhdpi"
private const val PHONE_NIGHT = "w411dp-h891dp-night-xhdpi"
private const val COMPACT = "w320dp-h568dp-xhdpi"
private const val LANDSCAPE = "w891dp-h411dp-land-xhdpi"
private const val TABLET = "w800dp-h1280dp-xhdpi"
