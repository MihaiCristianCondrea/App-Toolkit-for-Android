package com.d4rk.android.libs.apptoolkit.app.theme.ui.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


// TODO: Move it somehwere else ui/data/domain layers
@Immutable
data class WallpaperSwatchColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
)