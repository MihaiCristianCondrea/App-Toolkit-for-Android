package com.d4rk.android.libs.apptoolkit.core.utils.window

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * Remembers the current [WindowSizeClass] for the active configuration.
 */
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        WindowSizeClass.calculateFromSize(
            size = DpSize(
                width = configuration.screenWidthDp.dp,
                height = configuration.screenHeightDp.dp,
            ),
        )
    }
}

/**
 * Returns the current [WindowWidthSizeClass] calculated from the active window metrics.
 */
@Composable
fun rememberWindowWidthSizeClass(): WindowWidthSizeClass {
    val windowSizeClass = rememberWindowSizeClass()
    return remember(windowSizeClass) { windowSizeClass.widthSizeClass }
}
