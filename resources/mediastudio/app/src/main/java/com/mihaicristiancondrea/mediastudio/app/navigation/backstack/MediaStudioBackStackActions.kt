package com.mihaicristiancondrea.mediastudio.app.navigation.backstack

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mihaicristiancondrea.libs.navigation.backstack.navigateSingleTop
import com.mihaicristiancondrea.mediastudio.app.navigation.models.Screen

fun SnapshotStateList<Screen>.navigateToSettings() {
    navigateSingleTop(Screen.Settings)
}
