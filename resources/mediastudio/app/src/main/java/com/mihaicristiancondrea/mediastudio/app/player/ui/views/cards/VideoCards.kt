package com.mihaicristiancondrea.mediastudio.app.player.ui.views.cards

import android.graphics.Rect
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mihaicristiancondrea.libs.mediaplayer.ui.states.PlayerSurfaceState
import com.mihaicristiancondrea.libs.mediaplayer.ui.views.ComposePlayerView
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.PlaybackCardUi
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.PlaybackCardUiList

@Composable
fun RadioRow(
    radios: PlaybackCardUiList ,
    onClick: (String) -> Unit ,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        radios.items.forEach { radio ->
            val containerColor by animateColorAsState(
                targetValue = if (radio.isPlaying) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant ,
                label = "radio_color_anim"
            )

            val cardWeight by animateFloatAsState(targetValue = if (radio.isSelected) 1.5f else 1f, label = "radio_weight_anim")
            val cardHeight by animateDpAsState(targetValue = if (radio.isSelected) 120.dp else 90.dp, label = "radio_height_anim")

            Card(
                onClick = { onClick(radio.id) },
                modifier = Modifier
                        .weight(cardWeight)
                        .height(cardHeight),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (radio.isSelected) 8.dp else 2.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (radio.artworkUri != null) {
                        AsyncImage(
                            model = radio.artworkUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        Box(
                            modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)), startY = 50f))
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = radio.title.take(2).uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (radio.artworkUri != null) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "LIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.sp
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.onError
                            )
                        }
                    }

                    if (radio.isSelected) {
                        Box(
                            modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                        ) {
                            AnimatedContent(
                                targetState = radio.isPlaying,
                                transitionSpec = {
                                    scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut()
                                },
                                label = "radio_play_anim"
                            ) { isPlaying ->
                                Icon(
                                    imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                    contentDescription = null,
                                    tint = if (radio.artworkUri != null) Color.White else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoPlayerCard(
    card: PlaybackCardUi? ,
    isBuffering: Boolean ,
    playerSurface: PlayerSurfaceState ,
    isPipPresentation: Boolean ,
    onClick: () -> Unit ,
    onVideoBoundsChanged: (String, Rect) -> Unit ,
) {
    if (card == null) return

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
                .fillMaxWidth()
                .then(if (isPipPresentation) Modifier.fillMaxHeight() else Modifier),
        enabled = !isPipPresentation,
        shape = if (isPipPresentation) RoundedCornerShape(0.dp) else RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isPipPresentation) 0.dp else 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                        .fillMaxWidth()
                        .then(if (isPipPresentation) Modifier.weight(1f) else Modifier)
                        .then(if (!isPipPresentation) Modifier.aspectRatio(16f / 9f) else Modifier)
                        .background(Color.Black)
            ) {
                if (card.shouldAttachPlayerView) {
                    ComposePlayerView(
                        playerSurface = playerSurface ,
                        modifier = Modifier.fillMaxSize() ,
                        isPipPresentation = isPipPresentation ,
                        onPlayerViewBoundsChanged = { rect ->
                            onVideoBoundsChanged(card.id , rect)
                        })
                }

                if (card.shouldShowThumbnail && !isPipPresentation) {
                    val thumbnail = card.thumbnailUri ?: card.artworkUri
                    if (thumbnail != null) {
                        AsyncImage(
                            model = thumbnail,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        Box(
                            modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f))
                        )
                    }

                    Box(
                        modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp),
                        )
                    }

                    if (isBuffering) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (!isPipPresentation) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun AudioOnlyCard(
    card: PlaybackCardUi? ,
    onClick: () -> Unit ,
) {
    if (card == null) return

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                if (card.artworkUri != null) {
                    AsyncImage(
                        model = card.artworkUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    Box(modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.2f)))
                }

                AnimatedContent(
                    targetState = card.isPlaying,
                    transitionSpec = { scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut() },
                    label = "audio_play_anim"
                ) { isPlaying ->
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = if (card.artworkUri != null) Color.White else MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
            ) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = card.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun LiveVideoCard(
    card: PlaybackCardUi? ,
    playerSurface: PlayerSurfaceState ,
    showCast: Boolean ,
    isBuffering: Boolean ,
    isPipPresentation: Boolean ,
    onClick: () -> Unit ,
    onVideoBoundsChanged: (String, Rect) -> Unit ,
) {
    if (card == null) return

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
                .fillMaxWidth()
                .then(if (isPipPresentation) Modifier.fillMaxHeight() else Modifier),
        enabled = !isPipPresentation,
        shape = if (isPipPresentation) RoundedCornerShape(0.dp) else RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isPipPresentation) 0.dp else 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!isPipPresentation) {
                Row(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = card.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = card.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                        .fillMaxWidth()
                        .then(if (isPipPresentation) Modifier.weight(1f) else Modifier)
                        .then(if (!isPipPresentation) Modifier.aspectRatio(16f / 9f) else Modifier)
                        .background(Color.Black),
            ) {
                if (card.shouldAttachPlayerView) {
                    ComposePlayerView(
                        playerSurface = playerSurface ,
                        modifier = Modifier.fillMaxSize() ,
                        hideSeekBar = true ,
                        showCastButton = showCast ,
                        isPipPresentation = isPipPresentation ,
                        onPlayerViewBoundsChanged = { rect ->
                            onVideoBoundsChanged(card.id , rect)
                        })
                }

                if (card.shouldShowThumbnail && !isPipPresentation) {
                    val thumbnail = card.thumbnailUri ?: card.artworkUri
                    if (thumbnail != null) {
                        AsyncImage(
                            model = thumbnail,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        Box(
                            modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f))
                        )
                    }

                    Box(
                        modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp),
                        )
                    }

                    if (isBuffering) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}