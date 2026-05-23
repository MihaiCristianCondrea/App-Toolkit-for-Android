package com.mihaicristiancondrea.libs.mediaplayer.ui.states

import androidx.compose.runtime.Stable
import androidx.media3.common.Player

@Stable
data class PlayerSurfaceState(
    val player: Player? = null ,
)