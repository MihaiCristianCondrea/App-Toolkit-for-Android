package com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.ui.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import kotlinx.coroutines.flow.Flow

interface NavigationItemsProvider {
    fun items(): Flow<List<NavigationDrawerItem>>
}
