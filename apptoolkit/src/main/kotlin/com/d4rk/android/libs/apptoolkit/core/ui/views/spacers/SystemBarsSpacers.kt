package com.d4rk.android.libs.apptoolkit.core.ui.views.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Spacer equal to the height of the status bar.
 */
@Composable
fun StatusBarSpacer() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
    )
}

/**
 * Spacer equal to the height of the navigation bar.
 */
@Composable
fun NavigationBarSpacer() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .windowInsetsBottomHeight(WindowInsets.navigationBars)
    )
}

/**
 * Spacer for both status + navigation bars combined.
 * Useful for full-screen layouts.
 */
@Composable
fun SystemBarsSpacer() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .windowInsetsBottomHeight(WindowInsets.systemBars)
    )
}

/**
 * Spacer equal to the height of the display cutout (notch / camera hole).
 */
@Composable
fun DisplayCutoutSpacer() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.displayCutout)
    )
}

/**
 * Spacer equal to the height of system gesture areas.
 * Useful when bottom controls must avoid swipe regions.
 */
@Composable
fun SystemGesturesSpacer() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .windowInsetsBottomHeight(WindowInsets.systemGestures)
    )
}

/**
 * Spacer equal to the height of the on-screen keyboard (IME).
 */
@Composable
fun ImeSpacer() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .windowInsetsBottomHeight(WindowInsets.ime)
    )
}