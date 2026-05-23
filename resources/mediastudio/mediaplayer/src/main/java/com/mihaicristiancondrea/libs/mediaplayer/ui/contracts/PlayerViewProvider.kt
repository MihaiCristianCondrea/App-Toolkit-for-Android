package com.mihaicristiancondrea.libs.mediaplayer.ui.contracts

import androidx.media3.common.Player
import kotlinx.coroutines.flow.StateFlow

interface PlayerViewProvider {
    val player: StateFlow<Player?>
}