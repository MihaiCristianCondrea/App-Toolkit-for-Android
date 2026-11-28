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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.mappers

import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.models.HolidayGreetingStyle

/**
 * The greeting for each season. Christmas is a frosty night: navy sky under white text, snow fading
 * to frost white under icy-blue buttons. Halloween is a moonlit night: deep purple above and below,
 * with a glowing orange button.
 */
internal fun HolidaySeason.toHolidayGreetingStyle(): HolidayGreetingStyle = when (this) {
    HolidaySeason.CHRISTMAS -> HolidayGreetingStyle(
        illustration = R.drawable.il_christmas_season,
        title = R.string.holiday_greeting_christmas_title,
        message = R.string.holiday_greeting_christmas_message,
        accept = R.string.holiday_greeting_christmas_accept,
        topEdge = Color(0xFF0A1F3A),
        bottomEdge = Color(0xFFF2F8FF),
        topScrim = Color(0xFF0A1F3A),
        bottomScrim = Color(0xFFF2F8FF),
        titleColor = Color.White,
        messageColor = Color(0xFFDCEEFF),
        primaryContainer = Color(0xFF1658B8),
        primaryContent = Color.White,
        secondaryContent = Color(0xFF0F3F80),
        secondaryOutline = Color(0xFF5C87C2),
    )

    HolidaySeason.HALLOWEEN -> HolidayGreetingStyle(
        illustration = R.drawable.il_halloween_season,
        title = R.string.holiday_greeting_halloween_title,
        message = R.string.holiday_greeting_halloween_message,
        accept = R.string.holiday_greeting_halloween_accept,
        topEdge = Color(0xFF1C1233),
        bottomEdge = Color(0xFF1E1233),
        topScrim = Color(0xFF1F1438),
        bottomScrim = Color(0xFF1E1233),
        titleColor = Color.White,
        messageColor = Color(0xFFE6DAF7),
        primaryContainer = Color(0xFFFF8C1A),
        primaryContent = Color(0xFF2B1000),
        secondaryContent = Color(0xFFFFC58A),
        secondaryOutline = Color(0xFFFF9E40),
    )
}
