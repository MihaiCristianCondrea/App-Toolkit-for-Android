package com.mihaicristiancondrea.mediastudio.app.player.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PipAwareVisibility(
    visible: Boolean,
    isPipLayout: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (isPipLayout) {
        if (visible) {
            Box(modifier = modifier) {
                content()
            }
        }
    } else {
        AnimatedVisibility(
            visible = visible,
            modifier = modifier,
        ) {
            content()
        }
    }
}
