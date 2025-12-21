package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.RootError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest

/**
 * Use case that combines the list of developer apps with the current set of
 * favorites and emits only those apps that are marked as favorite.
 */
class ObserveFavoriteAppsUseCase(
    private val fetchDeveloperAppsUseCase: FetchDeveloperAppsUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val dispatchers: DispatcherProvider,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<DataState<List<AppInfo>, RootError>> {
        return fetchDeveloperAppsUseCase()
            .transformLatest { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        var hasEmittedFavorites = false
                        val favoritesFlow = observeFavoritesUseCase()
                            .onEach { hasEmittedFavorites = true }
                            .map { favorites ->
                                val favoriteApps =
                                    dataState.data.filter { favorites.contains(it.packageName) }
                                DataState.Success(favoriteApps)
                            }
                            .onCompletion { cause ->
                                if (cause == null && !hasEmittedFavorites) {
                                    emit(DataState.Success(emptyList()))
                                }
                            }
                        emitAll(favoritesFlow)
                    }

                    is DataState.Error -> emit(
                        DataState.Error(
                            data = dataState.data,
                            error = dataState.error
                        )
                    )

                    is DataState.Loading -> emit(DataState.Loading(data = dataState.data))
                }
            }
            .flowOn(dispatchers.io)
    }
}

