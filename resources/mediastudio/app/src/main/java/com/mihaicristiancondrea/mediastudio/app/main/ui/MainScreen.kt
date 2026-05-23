package com.mihaicristiancondrea.mediastudio.app.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.libs.mediaplayer.domain.models.PlaybackQueueType
import com.mihaicristiancondrea.libs.mediaplayer.ui.contracts.PlayerEvent
import com.mihaicristiancondrea.libs.navigation.models.MainNavigationItem
import com.mihaicristiancondrea.libs.navigation.models.NavigationItemIcon
import com.mihaicristiancondrea.libs.navigation.ui.NavigationIcon
import com.mihaicristiancondrea.mediastudio.R
import com.mihaicristiancondrea.mediastudio.app.main.ui.views.player.NowPlayingSheet
import com.mihaicristiancondrea.mediastudio.app.navigation.models.Screen
import com.mihaicristiancondrea.mediastudio.app.player.ui.models.PlaybackCardUiList
import com.mihaicristiancondrea.mediastudio.app.player.ui.states.PlayerUiState
import com.mihaicristiancondrea.mediastudio.core.ui.views.rememberDeviceRoundedCornerShape

// TODO: This will become a domain pre-filled navigation
private val mainNavigationItems = listOf(
    MainNavigationItem(
        destination = Screen.Home,
        title = "Home",
        shortTitle = "Home",
        icon = NavigationItemIcon.Vector(Icons.Outlined.Home),
        selectedIcon = NavigationItemIcon.Vector(Icons.Rounded.Home),
    ),

    MainNavigationItem(
        destination = Screen.Apps,
        title = "Applications",
        shortTitle = "Apps",
        icon = NavigationItemIcon.Vector(Icons.Outlined.Apps),
        selectedIcon = NavigationItemIcon.Vector(Icons.Rounded.Apps),
        badgeText = "NEW",
    ),

    MainNavigationItem(
        destination = Screen.Favorites ,
        title = "Favorites" ,
        shortTitle = "Fav" ,
        icon = NavigationItemIcon.Vector(Icons.Outlined.FavoriteBorder) ,
        selectedIcon = NavigationItemIcon.Vector(Icons.Rounded.Favorite) ,
        badgeText = "12" ,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: PlayerUiState,
    onEvent: (PlayerEvent) -> Unit,
    currentKey: Any, // FIXME: Unstable parameter 'currentKey' prevents composable from being skippable
    onNavigateTopLevel: (Screen) -> Unit,
    onNavigateSettings: () -> Unit,
    content: @Composable () -> Unit,
) {
    val deviceCornerShape = rememberDeviceRoundedCornerShape()
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize().clip(deviceCornerShape) .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    val titleTextStyle = MaterialTheme.typography.titleLarge

                    val iconSize = with(LocalDensity.current) {
                        titleTextStyle.fontSize.toDp()
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically ,
                        horizontalArrangement = Arrangement.spacedBy(8.dp) ,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo) ,
                            contentDescription = null ,
                            modifier = Modifier.size(iconSize + 4.dp) ,
                        )

                        Text(
                            text = "Media Studio",
                            style = titleTextStyle,
                        )
                    }
                },
                actions = {
                    FilledTonalIconButton(
                        onClick = onNavigateSettings,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open settings",
                        )
                    }
                },
                scrollBehavior = topAppBarScrollBehavior,
            )
        },
        bottomBar = {
            Column {
                uiState.nowPlaying?.let {
                    NowPlayingSheet(
                        nowPlaying = uiState.nowPlaying,
                        queueItems = if (uiState.nowPlaying.queueType == PlaybackQueueType.Radio) {
                            uiState.radioCards
                        } else {
                            PlaybackCardUiList()
                        },
                        onEvent = onEvent,
                    )
                }

                NavigationBar {
                    mainNavigationItems.forEach { item ->
                        val selected = currentKey == item.destination.toString()

                        val iconToUse = if (selected) {
                            item.selectedIcon ?: item.icon
                        } else {
                            item.icon
                        }

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                onNavigateTopLevel(item.destination)
                            },
                            icon = {
                                if (item.badgeText != null) {
                                    BadgedBox(
                                        badge = {
                                            val badgeText = item.badgeText
                                    Badge {
                                        if (badgeText != null) {
                                            Text(badgeText)
                                        }
                                    }
                                        },
                                    ) {
                                        iconToUse.NavigationIcon(
                                            contentDescription = item.title,
                                        )
                                    }
                                } else {
                                    iconToUse.NavigationIcon(
                                        contentDescription = item.title,
                                    )
                                }
                            },
                            label = {
                                Text(item.title)
                            },
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (uiState.isPipPresentation || uiState.isTransitioningToPip) {
                            Modifier
                        } else {
                            Modifier.padding(paddingValues)
                        }
                    ),
        ) {
            content()
        }
    }
}
