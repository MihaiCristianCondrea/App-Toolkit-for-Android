package com.mihaicristiancondrea.libs.mediaplayer.ui.views

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.media3.cast.MediaRouteButtonViewProvider
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.media3.ui.R as Media3UiR
import com.mihaicristiancondrea.libs.mediaplayer.ui.states.PlayerSurfaceState

@OptIn(UnstableApi::class)
@Composable
fun ComposePlayerView(
    playerSurface: PlayerSurfaceState,
    modifier: Modifier = Modifier,
    isPipPresentation: Boolean = false,
    showCastButton: Boolean = false,
    hideSeekBar: Boolean = false,
    onPlayerViewBoundsChanged: ((Rect) -> Unit)? = null,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PlayerView(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                setKeepContentOnPlayerReset(true)
                setArtworkDisplayMode(PlayerView.ARTWORK_DISPLAY_MODE_OFF)
                defaultArtwork = null

                useController = !isPipPresentation
                controllerAutoShow = !isPipPresentation

                setShowRewindButton(false)
                setShowFastForwardButton(false)
                setShowPreviousButton(false)
                setShowNextButton(false)

                setShowSubtitleButton(true)
                if (showCastButton && !isPipPresentation) {
                    setMediaRouteButtonViewProvider(MediaRouteButtonViewProvider())
                } else {
                    setMediaRouteButtonViewProvider(null)
                }
                setFullscreenButtonClickListener {
                    // TODO: here goes the full screen redirect that will likely be triggered from the app host
                }

                findViewById<View>(Media3UiR.id.exo_progress)?.isVisible = !hideSeekBar

                addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
                    val rect = Rect()
                    view.getGlobalVisibleRect(rect)
                    if (!rect.isEmpty) {
                        onPlayerViewBoundsChanged?.invoke(rect)
                    }
                }

                this.player = playerSurface.player
            }
        },
        update = { playerView ->
            playerView.useController = !isPipPresentation
            playerView.controllerAutoShow = !isPipPresentation

            if (playerView.player !== playerSurface.player) {
                playerView.player = playerSurface.player
            }

            val rect = Rect()
            playerView.getGlobalVisibleRect(rect)
            if (!rect.isEmpty) {
                onPlayerViewBoundsChanged?.invoke(rect)
            }
        },
        onRelease = { playerView ->
            playerView.player = null
        },
    )
}
