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

package com.d4rk.android.libs.apptoolkit.app.theme.ui.style.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.d4rk.android.libs.apptoolkit.R

private val baseline = Typography()

private val appFontFamily = FontFamily(
    Font(R.font.google_sans_flex_variable)
)

val AppTypography: Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = appFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = appFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = appFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = appFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = appFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = appFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = appFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = appFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = appFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = appFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = appFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = appFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = appFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = appFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = appFontFamily),
)
