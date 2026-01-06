package com.d4rk.android.apps.apptoolkit.app.apps.list.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class FetchDeveloperAppsUseCase(
    private val repository: DeveloperAppsRepository
) {
    /**
     * Returns developer apps.
     *
     * The repository flow is emitted untouched so upstream errors and cancellations keep their
     * original semantics. The leading [DataState.Loading] allows screens to render a progress UI
     * before the repository responds.
     */
    operator fun invoke(): Flow<DataState<List<AppInfo>, AppErrors>> = flow {
        emitAll(repository.fetchDeveloperApps())
    }
}
