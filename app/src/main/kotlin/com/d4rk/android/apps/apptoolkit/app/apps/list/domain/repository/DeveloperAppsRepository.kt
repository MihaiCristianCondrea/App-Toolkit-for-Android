package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.RootError
import kotlinx.coroutines.flow.Flow

interface DeveloperAppsRepository {
    fun fetchDeveloperApps(): Flow<DataState<List<AppInfo>, RootError>>
}
