package com.mihaicristiancondrea.libs.navigation.models

import androidx.navigation3.runtime.NavKey

enum class NavigationDestinationType {
    TopLevel,
    ActivityLike,
    Nested,
}

interface NavigationDestination : NavKey {
    val destinationType: NavigationDestinationType
}

val NavigationDestination.isTopLevel: Boolean
    get() = destinationType == NavigationDestinationType.TopLevel

val NavigationDestination.isActivityLike: Boolean
    get() = destinationType == NavigationDestinationType.ActivityLike

val NavigationDestination.isNested: Boolean
    get() = destinationType == NavigationDestinationType.Nested
