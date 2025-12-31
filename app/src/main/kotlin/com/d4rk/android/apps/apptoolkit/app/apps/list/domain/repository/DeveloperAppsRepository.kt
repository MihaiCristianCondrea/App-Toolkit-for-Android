package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import kotlinx.coroutines.flow.Flow

interface DeveloperAppsRepository {
    fun fetchDeveloperApps(): Flow<DataState<List<AppInfo>, AppErrors>>
}
