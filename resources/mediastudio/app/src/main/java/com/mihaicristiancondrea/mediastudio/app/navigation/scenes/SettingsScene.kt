package com.mihaicristiancondrea.mediastudio.app.navigation.scenes

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import com.mihaicristiancondrea.mediastudio.app.navigation.models.Screen
import com.mihaicristiancondrea.mediastudio.app.settings.ui.SettingsScreen

class SettingsScene(
    override val key: Any,
    private val entry: NavEntry<Screen>,
    override val previousEntries: List<NavEntry<Screen>>,
    private val onBack: () -> Unit,
) : Scene<Screen> {

    override val entries: List<NavEntry<Screen>> = listOf(entry)

    override val content: @Composable () -> Unit = {
        SettingsScreen(
            onBack = onBack ,
            content = {
                entry.Content()
            } ,
        )
    }
}

