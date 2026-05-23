package com.mihaicristiancondrea.libs.navigation.backstack

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mihaicristiancondrea.libs.navigation.models.NavigationDestination
import com.mihaicristiancondrea.libs.navigation.models.isTopLevel

fun <T : NavigationDestination> SnapshotStateList<T>.navigateTopLevel(
    destination: T,
) {
    check(destination.isTopLevel) {
        "Only top-level destinations can be used with navigateTopLevel()."
    }

    while (lastOrNull()?.isTopLevel == false) {
        removeLastOrNull()
    }

    if (lastOrNull() != destination) {
        add(destination)
    }
}

fun <T : NavigationDestination> SnapshotStateList<T>.navigateSingleTop(
    destination: T,
) {
    if (lastOrNull() != destination) {
        add(destination)
    }
}

fun <T> SnapshotStateList<T>.navigateBack(onFinish: () -> Unit) {
    if (size > 1) {
        removeLastOrNull()
    } else {
        onFinish()
    }
}
