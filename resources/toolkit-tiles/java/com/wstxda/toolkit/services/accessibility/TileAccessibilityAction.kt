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

package com.wstxda.toolkit.services.accessibility

import android.accessibilityservice.AccessibilityService
import android.os.Build
import androidx.annotation.RequiresApi

object TileAccessibilityAction {

    @RequiresApi(Build.VERSION_CODES.P)
    const val LOCK_SCREEN = AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN

    @RequiresApi(Build.VERSION_CODES.P)
    const val TAKE_SCREENSHOT = AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT

    @RequiresApi(Build.VERSION_CODES.P)
    const val POWER_DIALOG = AccessibilityService.GLOBAL_ACTION_POWER_DIALOG
}