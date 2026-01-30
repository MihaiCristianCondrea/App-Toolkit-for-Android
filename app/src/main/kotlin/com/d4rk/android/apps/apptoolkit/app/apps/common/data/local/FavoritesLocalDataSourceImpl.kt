package com.d4rk.android.apps.apptoolkit.app.apps.common.data.local

import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import kotlinx.coroutines.flow.Flow

class FavoritesLocalDataSourceImpl(
    private val dataStore: CommonDataStore,
) : FavoritesLocalDataSource {

    override fun observeFavorites(): Flow<Set<String>> = dataStore.favoriteApps

    override suspend fun toggleFavorite(packageName: String) {
        dataStore.toggleFavoriteApp(packageName)
    }
}
