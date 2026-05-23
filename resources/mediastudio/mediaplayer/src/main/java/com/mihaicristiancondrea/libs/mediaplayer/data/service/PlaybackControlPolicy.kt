package com.mihaicristiancondrea.libs.mediaplayer.data.service

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import com.mihaicristiancondrea.libs.mediaplayer.data.mappers.PlaybackMediaItemExtras

@OptIn(UnstableApi::class)
class PlaybackControlPolicy {

    fun availablePlayerCommands(baseCommands: Player.Commands, player: Player): Player.Commands {
        val currentItem = player.currentMediaItem ?: return baseCommands
        val builder = baseCommands.buildUpon()

        if (!currentItem.supportsSeek()) {
            builder
                .remove(Player.COMMAND_SEEK_BACK)
                .remove(Player.COMMAND_SEEK_FORWARD)
                .remove(Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM)
                .remove(Player.COMMAND_SEEK_TO_DEFAULT_POSITION)
        }

        if (!player.hasPreviousMediaItem()) {
            builder
                .remove(Player.COMMAND_SEEK_TO_PREVIOUS)
                .remove(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
        }

        if (!player.hasNextMediaItem()) {
            builder
                .remove(Player.COMMAND_SEEK_TO_NEXT)
                .remove(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
        }

        return builder.build()
    }

    fun mediaButtonPreferences(player: Player): List<CommandButton> {
        if (player.currentMediaItem == null) return emptyList()

        return buildList {
            if (player.hasPreviousMediaItem()) {
                add(
                    CommandButton.Builder(CommandButton.ICON_PREVIOUS)
                        .setDisplayName("Previous")
                        .setPlayerCommand(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
                        .build()
                )
            }

            add(
                CommandButton.Builder(
                    if (player.isPlaying) CommandButton.ICON_PAUSE else CommandButton.ICON_PLAY
                )
                    .setDisplayName("Play/Pause")
                    .setPlayerCommand(Player.COMMAND_PLAY_PAUSE)
                    .build()
            )

            if (player.hasNextMediaItem()) {
                add(
                    CommandButton.Builder(CommandButton.ICON_NEXT)
                        .setDisplayName("Next")
                        .setPlayerCommand(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                        .build()
                )
            }
        }
    }

    private fun MediaItem.supportsSeek(): Boolean {
        return mediaMetadata.extras?.getBoolean(PlaybackMediaItemExtras.SupportsSeek)
            ?: false
    }
}
