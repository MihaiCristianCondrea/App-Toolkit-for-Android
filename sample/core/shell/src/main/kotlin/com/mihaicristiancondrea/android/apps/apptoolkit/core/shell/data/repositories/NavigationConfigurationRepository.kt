package com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.data.repositories

import kotlinx.coroutines.flow.Flow

interface NavigationConfigurationRepository {
    val componentsShowcaseUnlocked: Flow<Boolean>
}
