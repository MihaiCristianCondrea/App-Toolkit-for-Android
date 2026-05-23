package com.mihaicristiancondrea.mediastudio.app.navigation.host

import android.graphics.Rect
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerEvent
import com.mihaicristiancondrea.libs.mediaplayer.ui.states.PlayerSurfaceState
import com.mihaicristiancondrea.libs.navigation.animations.rememberBottomNavTransitions
import com.mihaicristiancondrea.libs.navigation.animations.rememberNativeActivityTransitions
import com.mihaicristiancondrea.libs.navigation.backstack.navigateBack
import com.mihaicristiancondrea.libs.navigation.backstack.navigateTopLevel
import com.mihaicristiancondrea.libs.navigation.models.isTopLevel
import com.mihaicristiancondrea.mediastudio.app.navigation.backstack.navigateToSettings
import com.mihaicristiancondrea.mediastudio.app.navigation.models.Screen
import com.mihaicristiancondrea.mediastudio.app.navigation.scenes.MainSceneStrategy
import com.mihaicristiancondrea.mediastudio.app.player.ui.PlayerScreen
import com.mihaicristiancondrea.mediastudio.app.player.ui.states.PlayerUiState
import com.mihaicristiancondrea.mediastudio.app.settings.ui.SettingsScreenContent
import com.mihaicristiancondrea.mediastudio.core.ui.views.text.CenteredText

@Composable
fun NavigationHost(
    uiState: PlayerUiState,
    playerSurface: PlayerSurfaceState,
    onEvent: (PlayerEvent) -> Unit,
    onVideoBoundsChanged: (String, Rect) -> Unit,
) {
    val activity = LocalActivity.current
    val transitions = rememberNativeActivityTransitions()
    val bottomNavTransitions = rememberBottomNavTransitions()

    val backStack = remember {
        mutableStateListOf<Screen>(Screen.Home)
    }

    val sceneStrategy = remember {
        MainSceneStrategy(
            uiState = uiState,
            onEvent = onEvent,
            onNavigateTopLevel = backStack::navigateTopLevel ,
            onNavigateSettings = backStack::navigateToSettings ,
        )
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.navigateBack {
                activity?.finish()
            }
        },
        sceneStrategies = listOf(sceneStrategy),
        entryProvider = entryProvider {
            entry<Screen.Home> {
                PlayerScreen(
                    uiState = uiState ,
                    playerSurface = playerSurface ,
                    onEvent = onEvent ,
                    onVideoBoundsChanged = onVideoBoundsChanged ,
                )
            }

            entry<Screen.Apps> {
                AppsScreen()
            }

            entry<Screen.Favorites> {
                FavoritesScreen()
            }

            entry<Screen.Settings> {
                SettingsScreenContent()
            }
        },
        transitionSpec = {
            val entry = (targetState as? List<*>)?.lastOrNull() as? NavEntry<*>
            val screen = entry?.contentKey as? Screen
            if (screen?.isTopLevel == true) {
                bottomNavTransitions.transition()
            } else {
                transitions.forward()
            }
        },
        popTransitionSpec = {
            val entry = (targetState as? List<*>)?.lastOrNull() as? NavEntry<*>
            val screen = entry?.contentKey as? Screen
            if (screen?.isTopLevel == true) {
                bottomNavTransitions.transition()
            } else {
                transitions.pop()
            }
        },
        predictivePopTransitionSpec = {
            val entry = (targetState as? List<*>)?.lastOrNull() as? NavEntry<*>
            val screen = entry?.contentKey as? Screen
            if (screen?.isTopLevel == true) {
                bottomNavTransitions.transition()
            } else {
                transitions.predictivePop()
            }
        },
    )
}

@Composable
private fun AppsScreen() {
    CenteredText("Apps")
}

@Composable
private fun FavoritesScreen() {
    CenteredText("Favorites")
}
