package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun SystemModePreview(modifier: Modifier = Modifier) {
    val lightBg = Color(0xFFF2F4F8)
    val darkBg = Color(0xFF111318)

    MiniUiPreview(
        modifier = modifier,
        background = Brush.horizontalGradient(
            colorStops = arrayOf(
                0.0f to lightBg,
                0.50f to lightBg,
                0.50f to darkBg,
                1.0f to darkBg
            )
        ),
        surface = Color.White.copy(alpha = 0.72f),
        line = Color(0xFF1B1F2A).copy(alpha = 0.18f),
        accent = MaterialTheme.colorScheme.primary,
    )
}