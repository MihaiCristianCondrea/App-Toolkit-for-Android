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

package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

/**
 * Configures feedback behavior for button interactions.
 *
 * @param soundEffect The Android sound effect to play, or `null` to disable sound.
 * @param hapticFeedbackType The haptic feedback type to perform, or `null` to disable haptics.
 */
data class ButtonFeedback(
    val soundEffect: Int? = SoundEffectConstants.CLICK,
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
