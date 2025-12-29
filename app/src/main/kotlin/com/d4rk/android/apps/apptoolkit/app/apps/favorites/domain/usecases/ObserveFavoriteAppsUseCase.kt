package com.d4rk.android.apps.apptoolkit.app.apps.favorites.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart

class ObserveFavoriteAppsUseCase(
    private val fetchDeveloperAppsUseCase: FetchDeveloperAppsUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
) {
    operator fun invoke(): Flow<DataState<List<AppInfo>, Errors>> =
        combine(
            fetchDeveloperAppsUseCase().onStart { emit(DataState.Loading()) },
            observeFavoritesUseCase(),
        ) { appsState, favorites ->
            when (appsState) {
                is DataState.Success -> {
                    val filtered = appsState.data.filter { it.packageName in favorites }
                    DataState.Success(filtered)
                }

                is DataState.Loading -> {
                    val filtered = appsState.data?.filter { it.packageName in favorites }
                    DataState.Loading(filtered)
                }

                is DataState.Error -> {
                    val filtered = appsState.data?.filter { it.packageName in favorites }
                    DataState.Error(data = filtered, error = appsState.error)
                }
            }
        }.distinctUntilChanged()
}
