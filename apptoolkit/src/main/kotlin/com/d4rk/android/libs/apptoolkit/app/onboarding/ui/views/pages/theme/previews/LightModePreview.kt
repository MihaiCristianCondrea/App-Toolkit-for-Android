package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun LightModePreview(modifier: Modifier = Modifier) {
    MiniUiPreview(
        modifier = modifier,
        background = Brush.linearGradient(listOf(Color(0xFFF4F6FA), Color(0xFFEFF2F7))),
        surface = Color.White.copy(alpha = 0.92f),
        line = Color(0xFF1B1F2A).copy(alpha = 0.20f),
        accent = MaterialTheme.colorScheme.primary,
    )
}