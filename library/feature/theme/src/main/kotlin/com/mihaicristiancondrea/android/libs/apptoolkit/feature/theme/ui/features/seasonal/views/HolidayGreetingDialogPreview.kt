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

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.HolidaySeason

@Preview(name = "Christmas", widthDp = 411, heightDp = 891)
@Composable
private fun ChristmasGreetingPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        HolidayGreetingContent(season = HolidaySeason.CHRISTMAS, onAnswer = {})
    }
}

@Preview(name = "Christmas, dark", widthDp = 411, heightDp = 891)
@Composable
private fun ChristmasGreetingDarkPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HolidayGreetingContent(season = HolidaySeason.CHRISTMAS, onAnswer = {})
    }
}

@Preview(name = "Halloween", widthDp = 411, heightDp = 891)
@Composable
private fun HalloweenGreetingPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        HolidayGreetingContent(season = HolidaySeason.HALLOWEEN, onAnswer = {})
    }
}

@Preview(name = "Halloween, dark", widthDp = 411, heightDp = 891)
@Composable
private fun HalloweenGreetingDarkPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HolidayGreetingContent(season = HolidaySeason.HALLOWEEN, onAnswer = {})
    }
}

@Preview(name = "Christmas, compact phone", widthDp = 320, heightDp = 568)
@Composable
private fun ChristmasGreetingCompactPreview() {
    MaterialTheme {
        HolidayGreetingContent(season = HolidaySeason.CHRISTMAS, onAnswer = {})
    }
}

@Preview(name = "Halloween, landscape", widthDp = 891, heightDp = 411)
@Composable
private fun HalloweenGreetingLandscapePreview() {
    MaterialTheme {
        HolidayGreetingContent(season = HolidaySeason.HALLOWEEN, onAnswer = {})
    }
}

@Preview(name = "Christmas, tablet", widthDp = 800, heightDp = 1280)
@Composable
private fun ChristmasGreetingTabletPreview() {
    MaterialTheme {
        HolidayGreetingContent(season = HolidaySeason.CHRISTMAS, onAnswer = {})
    }
}

@Preview(name = "Halloween, large font", widthDp = 411, heightDp = 891, fontScale = 2f)
@Composable
private fun HalloweenGreetingLargeFontPreview() {
    MaterialTheme {
        HolidayGreetingContent(season = HolidaySeason.HALLOWEEN, onAnswer = {})
    }
}
