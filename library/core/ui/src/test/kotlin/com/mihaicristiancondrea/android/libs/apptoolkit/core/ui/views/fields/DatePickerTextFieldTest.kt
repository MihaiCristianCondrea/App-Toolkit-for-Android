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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields

import android.content.res.Configuration
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.text.DateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class DatePickerTextFieldTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `resolveDisplayLocale falls back to default when configuration locales are empty`() {
        val configuration = Configuration().apply {
            setLocales(LocaleList.getEmptyLocaleList())
        }

        assertEquals(Locale.getDefault(), resolveDisplayLocale(configuration = configuration))
    }

    @Test
    fun `parseIsoDateToLocalMillis parses ISO dates in the given zone and rejects invalid input`() {
        val zone = ZoneId.of("America/Los_Angeles")
        val expected = LocalDate.of(2026, 3, 15).atStartOfDay(zone).toInstant().toEpochMilli()

        assertEquals(expected, parseIsoDateToLocalMillis(dateString = "2026-03-15", zone = zone))
        assertNull(parseIsoDateToLocalMillis(dateString = "not-a-date", zone = zone))
    }

    @Test
    fun `formats the date using the configuration locale`() {
        val locale = Locale.GERMANY
        val millis = requireNotNull(parseIsoDateToLocalMillis(dateString = "2026-03-15"))
        val expectedText = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(Date(millis))
        val configuration = Configuration().apply {
            setLocales(LocaleList(locale))
        }

        compose.setContent {
            MaterialTheme {
                CompositionLocalProvider(LocalConfiguration provides configuration) {
                    DatePickerTextField(
                        dateMillis = millis,
                        onDateSelected = {},
                    )
                }
            }
        }

        compose.waitForIdle()
        compose.onNodeWithText(text = expectedText).assertExists()
    }
}
