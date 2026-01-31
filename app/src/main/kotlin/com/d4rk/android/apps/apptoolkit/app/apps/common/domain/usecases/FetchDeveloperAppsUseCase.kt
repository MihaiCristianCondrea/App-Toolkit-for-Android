package com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.repository.DeveloperAppsRepository
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class FetchDeveloperAppsUseCase(
    private val repository: DeveloperAppsRepository,
    private val firebaseController: FirebaseController,
) {
    /**
     * Returns developer apps.
     *
     * The repository flow is emitted untouched so upstream errors and cancellations keep their
     * original semantics. Loading UI should be driven by the ViewModel using `onStart` to keep
     * presentation concerns out of the domain layer.
     */
    operator fun invoke(): Flow<DataState<List<AppInfo>, AppErrors>> = flow {
        firebaseController.logBreadcrumb(
            message = "Fetch developer apps started",
            attributes = mapOf("source" to "FetchDeveloperAppsUseCase"),
        )
        emitAll(repository.fetchDeveloperApps())
    }.onStart {
        firebaseController.logBreadcrumb(
            message = "Fetch developer apps collecting",
            attributes = mapOf("stage" to "onStart"),
        )
    }
}
