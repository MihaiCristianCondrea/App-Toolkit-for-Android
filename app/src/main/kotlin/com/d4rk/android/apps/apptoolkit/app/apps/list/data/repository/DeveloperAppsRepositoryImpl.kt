package com.d4rk.android.apps.apptoolkit.app.apps.list.data.repository

import com.d4rk.android.apps.apptoolkit.app.apps.list.data.mapper.toDomain
import com.d4rk.android.apps.apptoolkit.app.apps.list.data.remote.DeveloperAppsRemoteDataSource
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository.DeveloperAppsRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.RootError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeveloperAppsRepositoryImpl(
    private val remote: DeveloperAppsRemoteDataSource,
) : DeveloperAppsRepository {

    override fun fetchDeveloperApps(): Flow<DataState<List<AppInfo>, RootError>> = flow {
        try {
            val response = remote.fetchDeveloperApps()
            val apps = response.data.apps.map { it.toDomain() }
            emit(DataState.Success(apps))
        } catch (t: Throwable) {
            // Map this to your RootError system (network/http/serialization/etc)
            // You likely already have a mapper. Example placeholder:
            // emit(DataState.Error(data = null, error = RootError(t))) // TODO:
        }
    }
}
