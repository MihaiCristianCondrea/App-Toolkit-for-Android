package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun DarkModePreview(modifier: Modifier = Modifier) {
    MiniUiPreview(
        modifier = modifier,
        background = Brush.linearGradient(listOf(Color(0xFF16181D), Color(0xFF0F1115))),
        surface = Color(0xFF23262D).copy(alpha = 0.95f),
        line = Color.White.copy(alpha = 0.16f),
        accent = MaterialTheme.colorScheme.primary,
    )
}