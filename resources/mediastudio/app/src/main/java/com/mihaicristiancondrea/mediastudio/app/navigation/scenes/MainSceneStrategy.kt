package com.mihaicristiancondrea.mediastudio.app.navigation.scenes

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.mihaicristiancondrea.mediastudio.app.navigation.models.Screen
import com.mihaicristiancondrea.mediastudio.app.player.ui.states.PlayerUiState
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerEvent

class MainSceneStrategy(
    private val uiState: PlayerUiState,
    private val onEvent: (PlayerEvent) -> Unit,
    private val onNavigateTopLevel: (Screen) -> Unit,
    private val onNavigateSettings: () -> Unit,
) : SceneStrategy<Screen> {

    override fun SceneStrategyScope<Screen>.calculateScene(
        entries: List<NavEntry<Screen>> ,
    ): Scene<Screen>? {
        val currentEntry = entries.lastOrNull() ?: return null
        val previousEntries = entries.dropLast(1)

        return when (currentEntry.contentKey) {
            Screen.Settings.toString() -> SettingsScene(
                key = "settings",
                entry = currentEntry,
                previousEntries = previousEntries,
                onBack = onBack,
            )

            else -> MainScene(
                key = "main-shell",
                entry = currentEntry,
                previousEntries = previousEntries,
                uiState = uiState,
                onEvent = onEvent,
                onNavigateTopLevel = onNavigateTopLevel,
                onNavigateSettings = onNavigateSettings,
            )
        }
    }
}
