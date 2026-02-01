package com.d4rk.android.libs.apptoolkit.app.theme.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.d4rk.android.libs.apptoolkit.app.theme.domain.model.WallpaperSwatchColors
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * Renders a compact dot-based preview for a palette choice.
 */
@Composable
fun ThemePalettePreviewDots(
    colors: WallpaperSwatchColors,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ThemePaletteDot(color = colors.primary)
        ThemePaletteDot(color = colors.secondary)
        ThemePaletteDot(color = colors.tertiary)
    }
}

@Composable
private fun ThemePaletteDot(color: Color) {
    Box(
        modifier = Modifier
            .size(SizeConstants.LargeSize)
            .clip(RoundedCornerShape(SizeConstants.LargeSize))
            .background(color = color)
    )
}
