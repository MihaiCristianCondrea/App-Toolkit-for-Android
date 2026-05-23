package com.mihaicristiancondrea.libs.navigation.animations

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberBottomNavTransitions(): BottomNavTransitions {
    return remember { BottomNavTransitions() }
}

class BottomNavTransitions {
    fun transition(): ContentTransform {
        return fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
    }
}
