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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.TimeZone
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class DatePickerDialogTest {

    private val utcMidnight: Long =
        LocalDate.of(2026, 3, 15).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    @Test
    fun `the picked day keeps its date west of UTC`() {
        val original = TimeZone.getDefault()
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"))

            assertEquals("2026-03-15", pickerMillisToIsoDate(utcMidnight))
        } finally {
            TimeZone.setDefault(original)
        }
    }

    @Test
    fun `the picked day keeps its date east of UTC`() {
        val original = TimeZone.getDefault()
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("Pacific/Auckland"))

            assertEquals("2026-03-15", pickerMillisToIsoDate(utcMidnight))
        } finally {
            TimeZone.setDefault(original)
        }
    }

    @Test
    fun `the initial selection is midnight UTC of the local day`() {
        val millis = todayAsPickerMillis(zone = ZoneId.of("America/Los_Angeles"))

        assertEquals(0L, millis % (24L * 60 * 60 * 1000))
        assertEquals(
            LocalDate.now(ZoneId.of("America/Los_Angeles")).toString(),
            pickerMillisToIsoDate(millis),
        )
    }
}
