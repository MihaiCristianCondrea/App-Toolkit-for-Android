package com.mihaicristiancondrea.android.apps.apptoolkit.app.main.data.repositories

import kotlinx.coroutines.flow.Flow

interface NavigationConfigurationRepository {
    val componentsShowcaseUnlocked: Flow<Boolean>
}
