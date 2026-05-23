package com.mihaicristiancondrea.mediastudio.app.navigation.models

import com.mihaicristiancondrea.libs.navigation.models.NavigationDestination
import com.mihaicristiancondrea.libs.navigation.models.NavigationDestinationType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavigationDestination {

    @Serializable
    data object Home : Screen {
        override val destinationType = NavigationDestinationType.TopLevel
    }

    @Serializable
    data object Apps : Screen {
        override val destinationType = NavigationDestinationType.TopLevel
    }

    @Serializable
    data object Favorites : Screen {
        override val destinationType = NavigationDestinationType.TopLevel
    }

    @Serializable
    data object Settings : Screen {
        override val destinationType = NavigationDestinationType.ActivityLike
    }
}
