package com.mihaicristiancondrea.mediastudio.app.main.ui.views.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerEvent
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.NowPlayingUi
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.PlaybackCardUiList

@Composable
fun NowPlayingSheet(
    nowPlaying: NowPlayingUi,
    queueItems: PlaybackCardUiList,
    onEvent: (PlayerEvent) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val artwork = nowPlaying.thumbnailUri ?: nowPlaying.artworkUri
    val queueLabel = if (nowPlaying.itemCount > 1 && nowPlaying.currentIndex >= 0) {
        "${nowPlaying.currentIndex + 1}/${nowPlaying.itemCount}"
    } else {
        nowPlaying.subtitle
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    if (artwork != null) {
                        AsyncImage(
                            model = artwork,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Text(
                            text = nowPlaying.title.take(2).uppercase(),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = nowPlaying.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = queueLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                IconButton(
                    onClick = { onEvent(PlayerEvent.SkipToPreviousClicked) },
                    enabled = nowPlaying.hasPrevious,
                ) {
                    Icon(imageVector = Icons.Rounded.SkipPrevious, contentDescription = "Previous")
                }

                IconButton(onClick = { onEvent(PlayerEvent.TogglePlaybackClicked) }) {
                    Icon(
                        imageVector = if (nowPlaying.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (nowPlaying.isPlaying) "Pause" else "Play",
                    )
                }

                IconButton(
                    onClick = { onEvent(PlayerEvent.SkipToNextClicked) },
                    enabled = nowPlaying.hasNext,
                ) {
                    Icon(imageVector = Icons.Rounded.SkipNext, contentDescription = "Next")
                }

                if (queueItems.items.isNotEmpty()) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                            contentDescription = if (expanded) "Collapse queue" else "Expand queue",
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded && queueItems.items.isNotEmpty()) {
                Column(
                    modifier = Modifier
                            .padding(top = 8.dp)
                            .heightIn(max = 220.dp)
                            .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    queueItems.items.forEach { queueItem ->
                        NowPlayingQueueRow(
                            item = queueItem,
                            onClick = { onEvent(PlayerEvent.RadioClicked(queueItem.id)) },
                        )
                    }
                }
            }
        }
    }
}
