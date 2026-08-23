package com.mihaicristiancondrea.android.apps.apptoolkit.app.main.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.core.data.local.datastore.DatastoreInterface
import kotlinx.coroutines.flow.Flow

class DefaultNavigationConfigurationRepository(
    dataStore: DatastoreInterface,
) : NavigationConfigurationRepository {
    override val componentsShowcaseUnlocked: Flow<Boolean> = dataStore.componentsShowcaseUnlocked
}
