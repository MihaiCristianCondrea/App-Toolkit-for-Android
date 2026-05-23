package com.mihaicristiancondrea.mediastudio.app.navigation.scenes

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import com.mihaicristiancondrea.mediastudio.app.main.ui.MainScreen
import com.mihaicristiancondrea.mediastudio.app.navigation.models.Screen
import com.mihaicristiancondrea.mediastudio.app.player.ui.states.PlayerUiState
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerEvent

class MainScene(
    override val key: Any,
    private val entry: NavEntry<Screen>,
    override val previousEntries: List<NavEntry<Screen>>,
    private val uiState: PlayerUiState,
    private val onEvent: (PlayerEvent) -> Unit,
    private val onNavigateTopLevel: (Screen) -> Unit,
    private val onNavigateSettings: () -> Unit,
) : Scene<Screen> {

    override val entries: List<NavEntry<Screen>> = listOf(entry)

    override val content: @Composable () -> Unit = {
        MainScreen(
            uiState = uiState,
            onEvent = onEvent,
            currentKey = entry.contentKey ,
            onNavigateTopLevel = onNavigateTopLevel ,
            onNavigateSettings = onNavigateSettings ,
            content = {
                entry.Content()
            } ,
        )
    }
}
