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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

/**
 * The light haptic the Toolkit's bars and rail give on every click; the drawer gives none. The
 * click sound is Compose's own: every clickable item plays it, so playing it here too sounded
 * twice.
 */
@Composable
internal fun rememberNavigationFeedback(haptic: Boolean): () -> Unit {
    val haptics = LocalHapticFeedback.current
    return remember(haptics, haptic) {
        { if (haptic) haptics.performHapticFeedback(HapticFeedbackType.ContextClick) }
    }
}
