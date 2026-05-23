package com.mihaicristiancondrea.mediastudio.app.main.ui

import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mihaicristiancondrea.libs.mediaplayer.data.mappers.PlaybackMediaItemExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.mihaicristiancondrea.mediastudio.app.navigation.host.NavigationHost
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerEvent
import com.mihaicristiancondrea.libs.mediaplayer.ui.states.PlayerSurfaceState

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    private val pipSourceRectHint = MutableStateFlow<Rect?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)
        observePiPState()

        setContent {
            MaterialTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val player by viewModel.player.collectAsStateWithLifecycle()
                val playerSurface = remember(player) { PlayerSurfaceState(player) }
                val onEvent = remember { { event: PlayerEvent -> viewModel.onEvent(event) } }

                NavigationHost(
                    uiState = uiState ,
                    playerSurface = playerSurface ,
                    onEvent = onEvent ,
                    onVideoBoundsChanged = ::onVideoBoundsChanged ,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val data = intent.data
            if (data != null && data.toString() == "https://mihaicristiancondrea.ro/player") {
                viewModel.onEvent(PlayerEvent.NotificationOpened)
            }
        } else if (intent.action == "${PlaybackMediaItemExtras.PackageName}.OPEN_PLAYER") {
            viewModel.onEvent(PlayerEvent.NotificationOpened)
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.onEvent(PlayerEvent.PiPModeChanged(isInPictureInPictureMode))
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onPictureInPictureUiStateChanged(pipState: PictureInPictureUiState) {
        super.onPictureInPictureUiStateChanged(pipState)
        if (pipState.isTransitioningToPip) {
            viewModel.onEvent(PlayerEvent.PiPTransitionStarted(viewModel.uiState.value.currentAssetId))
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()

        val state = viewModel.uiState.value
        if (!state.canEnterPiP) return

        /*
         * For Android 8-11 we manually enter PiP.
         * For Android 12+ do not call enterPictureInPictureMode here.
         * setAutoEnterEnabled(true) handles it more smoothly.
         */
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.O until Build.VERSION_CODES.S) {
            viewModel.onEvent(PlayerEvent.PiPTransitionStarted(state.currentAssetId))

            enterPictureInPictureMode(
                buildPiPParams(
                    autoEnter = false,
                    sourceRectHint = pipSourceRectHint.value,
                )
            )
        }
    }

    private fun onVideoBoundsChanged(assetId: String, rect: Rect) {
        val state = viewModel.uiState.value

        if (assetId != state.currentAssetId || !state.canEnterPiP || isInPictureInPictureMode ||rect.isEmpty) return

        val stableRect = Rect(rect)

        if (pipSourceRectHint.value != stableRect) {
            pipSourceRectHint.value = stableRect
        }
    }


    private fun observePiPState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(viewModel.uiState , pipSourceRectHint) { state, sourceRect ->
                    state to sourceRect
                }.collectLatest { (state, sourceRect) ->
                    setPictureInPictureParams(
                        buildPiPParams(
                            autoEnter = state.canEnterPiP,
                            sourceRectHint = sourceRect,
                        )
                    )
                }
            }
        }
    }

    private fun buildPiPParams(autoEnter: Boolean, sourceRectHint: Rect?): PictureInPictureParams {
        val builder = PictureInPictureParams.Builder().setAspectRatio(Rational(16, 9))
        sourceRectHint?.takeIf { !it.isEmpty }?.let(builder::setSourceRectHint)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setAutoEnterEnabled(autoEnter)
            builder.setSeamlessResizeEnabled(true)
        }

        return builder.build()
    }
}
