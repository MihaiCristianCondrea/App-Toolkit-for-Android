package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository.DeveloperAppsRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.RootError
import kotlinx.coroutines.flow.Flow

class FetchDeveloperAppsUseCase(
    private val repository: DeveloperAppsRepository
) {
    operator fun invoke(): Flow<DataState<List<AppInfo>, RootError>> =
        repository.fetchDeveloperApps()
}
