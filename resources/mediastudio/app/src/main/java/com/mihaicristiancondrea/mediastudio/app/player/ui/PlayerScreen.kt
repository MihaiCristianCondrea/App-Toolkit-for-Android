package com.mihaicristiancondrea.mediastudio.app.player.ui

import android.graphics.Rect
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerEvent
import com.mihaicristiancondrea.libs.mediaplayer.ui.states.PlayerSurfaceState
import com.mihaicristiancondrea.mediastudio.app.player.ui.states.PlayerUiState
import com.mihaicristiancondrea.mediastudio.app.player.ui.views.PipAwareVisibility
import com.mihaicristiancondrea.mediastudio.app.player.ui.views.cards.AudioOnlyCard
import com.mihaicristiancondrea.mediastudio.app.player.ui.views.cards.LiveVideoCard
import com.mihaicristiancondrea.mediastudio.app.player.ui.views.cards.RadioRow
import com.mihaicristiancondrea.mediastudio.app.player.ui.views.cards.VideoPlayerCard

@Composable
fun PlayerScreen(
    uiState: PlayerUiState,
    playerSurface: PlayerSurfaceState,
    onEvent: (PlayerEvent) -> Unit,
    onVideoBoundsChanged: (String, Rect) -> Unit,
) {
    val isPipLayout = uiState.isPipPresentation || uiState.isTransitioningToPip

    val animatedPadding by animateDpAsState(
        targetValue = if (isPipLayout) 0.dp else 16.dp,
        animationSpec = tween(durationMillis = if (isPipLayout) 0 else 300),
        label = "padding_anim",
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState(), enabled = !isPipLayout)
                    .padding(all = animatedPadding)
                    .then(Modifier),
            verticalArrangement = Arrangement.spacedBy(animatedPadding),
        ) {
            PipAwareVisibility(
                visible = !isPipLayout,
                isPipLayout = isPipLayout,
            ) {
                RadioRow(
                    radios = uiState.radioCards,
                    onClick = { onEvent(PlayerEvent.RadioClicked(it)) },
                )
            }

            PipAwareVisibility(
                visible = uiState.videoCard != null && (!isPipLayout || uiState.videoCard.isSelected),
                isPipLayout = isPipLayout,
            ) {
                VideoPlayerCard(
                    card = uiState.videoCard,
                    isBuffering = uiState.isBuffering && uiState.videoCard?.isSelected == true,
                    playerSurface = playerSurface,
                    isPipPresentation = isPipLayout,
                    onClick = { onEvent(PlayerEvent.VideoClicked) },
                    onVideoBoundsChanged = onVideoBoundsChanged,
                )
            }

            PipAwareVisibility(
                visible = !isPipLayout,
                isPipLayout = isPipLayout,
            ) {
                AudioOnlyCard(
                    card = uiState.podcastCard,
                    onClick = { onEvent(PlayerEvent.PodcastClicked) },
                )
            }

           PipAwareVisibility(
                visible = uiState.liveVideoCard != null && (!isPipLayout || uiState.liveVideoCard.isSelected),
                isPipLayout = isPipLayout,
            ) {
                LiveVideoCard(
                    card = uiState.liveVideoCard,
                    playerSurface = playerSurface,
                    showCast = !isPipLayout && uiState.liveVideoCard?.supportsCast == true,
                    isBuffering = uiState.isBuffering && uiState.liveVideoCard?.isSelected == true,
                    isPipPresentation = isPipLayout,
                    onClick = { onEvent(PlayerEvent.LiveVideoClicked) },
                    onVideoBoundsChanged = onVideoBoundsChanged,
                )
            }
        }
    }
}
