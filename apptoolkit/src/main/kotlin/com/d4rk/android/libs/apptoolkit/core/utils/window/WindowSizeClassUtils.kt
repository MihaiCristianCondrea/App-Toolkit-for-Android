package com.d4rk.android.libs.apptoolkit.core.utils.window

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize

/**
 * Remembers the current [WindowSizeClass] for the active configuration.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val density = LocalDensity.current
    val container = LocalWindowInfo.current.containerSize
    val dpSize = remember(container, density) {
        with(density) { DpSize(container.width.toDp(), container.height.toDp()) }
    }
    return remember(dpSize) { WindowSizeClass.calculateFromSize(dpSize) }
}


/**
 * Returns the current [WindowWidthSizeClass] calculated from the active window metrics.
 */
@Composable
fun rememberWindowWidthSizeClass(): WindowWidthSizeClass {
    val windowSizeClass = rememberWindowSizeClass()
    return remember(windowSizeClass) { windowSizeClass.widthSizeClass }
}
