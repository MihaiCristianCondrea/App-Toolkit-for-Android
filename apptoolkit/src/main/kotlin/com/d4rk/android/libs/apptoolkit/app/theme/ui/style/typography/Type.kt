package com.d4rk.android.libs.apptoolkit.app.theme.ui.style.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.R

private val baseline = Typography()

private val appFontFamily = FontFamily(
    Font(R.font.google_sans_flex_variable)
)

private fun TextStyle.withAppTypographyDefaults(): TextStyle {
    val normalizedWeight: FontWeight? = when (fontWeight) {
        FontWeight.Bold,
        FontWeight.ExtraBold,
        FontWeight.Black -> FontWeight.SemiBold

        else -> fontWeight
    }
    return copy(
        fontFamily = appFontFamily,
        fontWeight = normalizedWeight
    )
}

val AppTypography: Typography = Typography(
    displayLarge = baseline.displayLarge.withAppTypographyDefaults(),
    displayMedium = baseline.displayMedium.withAppTypographyDefaults(),
    displaySmall = baseline.displaySmall.withAppTypographyDefaults(),
    headlineLarge = baseline.headlineLarge.withAppTypographyDefaults(),
    headlineMedium = baseline.headlineMedium.withAppTypographyDefaults(),
    headlineSmall = baseline.headlineSmall.withAppTypographyDefaults(),
    titleLarge = baseline.titleLarge.withAppTypographyDefaults(),
    titleMedium = baseline.titleMedium.withAppTypographyDefaults(),
    titleSmall = baseline.titleSmall.withAppTypographyDefaults(),
    bodyLarge = baseline.bodyLarge.withAppTypographyDefaults(),
    bodyMedium = baseline.bodyMedium.withAppTypographyDefaults(),
    bodySmall = baseline.bodySmall.withAppTypographyDefaults(),
    labelLarge = baseline.labelLarge.withAppTypographyDefaults(),
    labelMedium = baseline.labelMedium.withAppTypographyDefaults(),
    labelSmall = baseline.labelSmall.withAppTypographyDefaults(),
)
