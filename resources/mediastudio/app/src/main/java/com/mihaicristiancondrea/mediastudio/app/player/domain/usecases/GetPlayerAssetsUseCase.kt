package com.mihaicristiancondrea.mediastudio.app.player.domain.usecases

import com.mihaicristiancondrea.libs.mediaplayer.domain.interfaces.PlaybackAssetProvider
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAsset
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackAssetType
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackSettings
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackStreamFormat
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackStreamSource

class GetPlayerAssetsUseCase : PlaybackAssetProvider {
    private companion object {
        val radioCapabilities = PlaybackSettings(
            supportsPrevious = true,
            supportsNext = true,
        )
    }

    override operator fun invoke(): List<PlaybackAsset> {
        return listOf(
            PlaybackAsset(
                id = "radio_digi_fm",
                title = "Digi FM",
                subtitle = "Live radio",
                artworkUri = "https://play-lh.googleusercontent.com/9RJq-f6yv0FxJOHtFfSSnUUrIP9it_JlfV2n00vZUNJPRo3fwAIRJJH6Ni4ovcjkaruNgit6l_OzuHnODBZqsQ=w416-h235-rw",
                type = PlaybackAssetType.Radio,
                isLive = true,
                capabilities = radioCapabilities,
                streamSource = PlaybackStreamSource.Direct(
                    uri = "http://edge76.rdsnet.ro:84/digifm/digifm.mp3" ,
                    format = PlaybackStreamFormat.Mp3
                ),
            ),

            PlaybackAsset(
                id = "radio_kiss_fm",
                title = "Romantic FM",
                subtitle = "Live radio",
                artworkUri = "https://yt3.googleusercontent.com/pWfQpQtxhjHtyHqMY4t0pO-be7CWDjV9wLddpEUtualL3wWId8PCd7Jp9oXlCqrc29xBbYEInc8=s900-c-k-c0x00ffffff-no-rj",
                type = PlaybackAssetType.Radio,
                isLive = true,
                capabilities = radioCapabilities,
                streamSource = PlaybackStreamSource.Direct(
                    uri = "http://live.romanticfm.ro:8002/;stream/1" ,
                ),
            ),

            PlaybackAsset(
                id = "radio_pro_fm",
                title = "Pro FM",
                subtitle = "Live radio",
                artworkUri = "https://s.iw.ro/gateway/g/ZmlsZVNvdXJjZT1odHRwJTNBJTJGJTJG/c3RvcmFnZTA3dHJhbnNjb2Rlci5yY3Mt/cmRzLnJvJTJGc3RvcmFnZSUyRjIwMjEl/MkYwOCUyRjAzJTJGMTM2NDg4MV8xMzY0/ODgxX3Byb2ZtXzc4MHg0NDAucG5nJmhh/c2g9Mzk2OTQ4ZDA2ZmU1M2EyZGFkZDM0NzEzM2M0YWYwOTE=.png",
                type = PlaybackAssetType.Radio,
                isLive = true,
                capabilities = radioCapabilities,
                streamSource = PlaybackStreamSource.Direct(
                    uri = "https://edge76.rcs-rds.ro/radio/profm/index.m3u8"
                ),
            ),

            PlaybackAsset(
                id = "radio_dance",
                title = "Dance FM",
                subtitle = "Live radio",
                artworkUri = "https://is1-ssl.mzstatic.com/image/thumb/Purple116/v4/2b/e9/60/2be960df-212d-56b3-3cdd-174291ce1f7d/AppIcon-DanceFM-0-0-1x_U007emarketing-0-7-0-85-220.png/1200x630wa.jpg",
                type = PlaybackAssetType.Radio,
                isLive = true,
                capabilities = radioCapabilities,
                streamSource = PlaybackStreamSource.Direct(
                    uri = "http://edge126.rdsnet.ro:84/profm/dancefm.mp3",
                    format = PlaybackStreamFormat.Mp3
                ),
            ),

            PlaybackAsset(
                id = "video_sample",
                title = "Video",
                subtitle = "Regular VOD video",
                artworkUri = "asset:///player/media_artwork.png",
                thumbnailUri = "asset:///player/media_artwork.png",
                type = PlaybackAssetType.Video,
                capabilities = PlaybackSettings(
                    supportsPiP = true,
                    supportsSeek = true,
                ),
                streamSource = PlaybackStreamSource.Direct(
                    uri = "https://v6.iw.ro/video/v/ZmlsZVNvdXJjZT1odHRwJTNBJTJGJTJG/c3RvcmFnZTA4dHJhbnNjb2Rlci5yY3Mt/cmRzLnJvJTJGc3RvcmFnZSUyRjIwMjYl/MkYwNSUyRjA3JTJGMjcxNjI0Nl8yNzE2/MjQ2X3ZhcmlhbnRlLWd1dmVybi0xLm1w/NCZmcj0xJmhhc2g9NmFmZmIyNzc1YjQzZTZkYzU2NGM1NTQyMjcyNzhiNjI=.mp4" ,
                    format = PlaybackStreamFormat.Mp4
                ),
            ),

            PlaybackAsset(
                id = "podcast_sample",
                title = "Podcast",
                subtitle = "Audio-only podcast",
                artworkUri = "asset:///player/media_artwork.png",
                type = PlaybackAssetType.Podcast,
                capabilities = PlaybackSettings(
                    supportsSeek = true,
                ),
                streamSource = PlaybackStreamSource.Direct(
                    uri = "https://example.com/podcast/episode.mp3"
                ),
            ),

            PlaybackAsset(
                id = "live_video_dai_cast",
                title = "Live TV DIGI 24",
                subtitle = "Live video with Cast + DAI",
                artworkUri = "asset:///player/media_artwork.png",
                thumbnailUri = "asset:///player/media_artwork.png",
                type = PlaybackAssetType.LiveVideo,
                isLive = true,
                capabilities = PlaybackSettings(
                    supportsPiP = true,
                    supportsCast = true,
                ),
                streamSource = PlaybackStreamSource.ServerSideDai(
                    assetKey = "qqA_2cTaSGSI6A_KABXqIA",
                    manifestUri = null,
                    format = PlaybackStreamFormat.Hls,
                ),
            ),
        )
    }
}
