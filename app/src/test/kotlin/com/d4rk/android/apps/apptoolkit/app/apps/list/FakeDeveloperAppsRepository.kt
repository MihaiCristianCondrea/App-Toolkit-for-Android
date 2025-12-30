package com.d4rk.android.apps.apptoolkit.app.apps.list

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Fake implementation of [DeveloperAppsRepository] that returns a predefined list.
 * It can optionally emit an error when [fetchDeveloperApps] is called.
 */
class FakeDeveloperAppsRepository(
    private val apps: List<AppInfo>,
    private val fetchError: AppErrors? = null,
) : DeveloperAppsRepository {
    override fun fetchDeveloperApps(): Flow<DataState<List<AppInfo>, AppErrors>> = flow {
        fetchError?.let {
            emit(DataState.Error(error = it))
            return@flow
        }
        emit(DataState.Success(apps))
    }
}
