package com.d4rk.android.libs.apptoolkit.core.ui.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith

/**
 * Default navigation animations shared across the toolkit.
 */
object NavigationAnimations { // TODO: Move it to the library
    private const val FadeScaleDurationMillis = 200
    private val fadeScaleEnterSpec = tween<Float>(durationMillis = FadeScaleDurationMillis)
    private val fadeScaleExitSpec = tween<Float>(durationMillis = FadeScaleDurationMillis)

    fun default(): ContentTransform {
        val enter: EnterTransition = fadeIn(animationSpec = fadeScaleEnterSpec) + scaleIn(
            initialScale = 0.92f,
            animationSpec = fadeScaleEnterSpec
        )
        val exit: ExitTransition = fadeOut(animationSpec = fadeScaleExitSpec) + scaleOut(
            targetScale = 0.95f,
            animationSpec = fadeScaleExitSpec
        )
        return enter togetherWith exit
    }
}
