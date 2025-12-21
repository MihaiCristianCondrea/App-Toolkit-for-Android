package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model

import androidx.compose.runtime.Immutable

/**
 * Represents a single item in the app list.
 * This can either be an actual application or an advertisement.
 */
@Immutable
sealed interface AppListItem {
    /**
     * Represents an item in a list of applications.
     * This sealed interface allows for different types of items in the list,
     * such as an actual application or an advertisement.
     */
    @Immutable
    data class App(val appInfo: AppInfo) : AppListItem

    /**
     * Represents an advertisement placeholder in the app list.
     */
    @Immutable
    data object Ad : AppListItem
}