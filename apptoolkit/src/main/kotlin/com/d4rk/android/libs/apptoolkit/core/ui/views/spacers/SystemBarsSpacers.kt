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