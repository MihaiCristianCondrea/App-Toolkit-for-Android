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
