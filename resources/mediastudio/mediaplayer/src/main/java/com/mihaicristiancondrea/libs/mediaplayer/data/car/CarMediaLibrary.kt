package com.mihaicristiancondrea.libs.mediaplayer.data.car

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.mihaicristiancondrea.libs.mediaplayer.data.mappers.toCarMediaItem
import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackAssetProvider
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAssetType

class CarMediaLibrary(private val playbackAssetProvider: PlaybackAssetProvider) {

    private val rootId = "root"
    private val radioId = "radio"
    private val podcastId = "podcast"

    fun getRootItem(): MediaItem {
        return createFolder(rootId, "Media Studio Player")
    }

    fun getChildren(parentId: String): List<MediaItem> {
        val assets = playbackAssetProvider()
        return when (parentId) {
            rootId -> listOf(createFolder(radioId, "Radio"), createFolder(podcastId, "Podcasts"))
            radioId -> assets.filter { it.type == PlaybackAssetType.Radio }.map { it.toCarMediaItem() }
            podcastId -> assets.filter { it.type == PlaybackAssetType.Podcast }.map { it.toCarMediaItem() }
            else -> emptyList()
        }
    }

    fun getItem(mediaId: String): MediaItem? {
        return when (mediaId) {
            rootId -> getRootItem()
            radioId -> createFolder(radioId, "Radio")
            podcastId -> createFolder(podcastId, "Podcasts")
            else -> playbackAssetProvider().firstOrNull { it.id == mediaId }?.toCarMediaItem()
        }
    }

    fun search(query: String): List<MediaItem> {
        val normalizedQuery = query.trim()
        val audioSafeAssets = playbackAssetProvider().filter { asset ->
            asset.type == PlaybackAssetType.Radio || asset.type == PlaybackAssetType.Podcast
        }

        val matches = if (normalizedQuery.isBlank()) {
            audioSafeAssets
        } else {
            audioSafeAssets.filter { asset ->
                asset.title.contains(normalizedQuery, ignoreCase = true) ||
                        asset.subtitle.contains(normalizedQuery, ignoreCase = true)
            }
        }

        return matches.map { it.toCarMediaItem() }
    }

    private fun createFolder(id: String, title: String): MediaItem {
        return MediaItem.Builder()
                .setMediaId(id)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                            .setIsBrowsable(true)
                            .setIsPlayable(false)
                            .setTitle(title)
                            .build()
                )
                .build()
    }
}
