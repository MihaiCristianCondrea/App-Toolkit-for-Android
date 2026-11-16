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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons

import android.view.View
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

/**
 * Configures feedback behavior for button interactions.
 *
 * The click sound is not part of it by default: Compose plays the system click sound for every
 * clickable, so a button that played it as well sounded twice.
 *
 * @param soundEffect An extra Android sound effect to play on top of Compose's click, or `null`
 * (the default) for Compose's click alone.
 * @param hapticFeedbackType The haptic feedback type to perform, or `null` to disable haptics.
 */
data class ButtonFeedback(
    val soundEffect: Int? = null,
    val hapticFeedbackType: HapticFeedbackType? = HapticFeedbackType.ContextClick,
) {
    /**
     * Performs the configured feedback using the provided [View] and [HapticFeedback].
     */
    fun performClick(view: View, hapticFeedback: HapticFeedback) {
        soundEffect?.let(view::playSoundEffect)
        hapticFeedbackType?.let(hapticFeedback::performHapticFeedback)
    }
}
