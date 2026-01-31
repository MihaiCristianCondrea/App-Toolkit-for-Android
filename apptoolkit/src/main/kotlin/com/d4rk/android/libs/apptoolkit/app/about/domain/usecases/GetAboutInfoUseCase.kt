package com.d4rk.android.libs.apptoolkit.app.about.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for retrieving information about the application.
 *
 * This class encapsulates the business logic for fetching application-related details
 * from the [AboutRepository] and providing them as a stream of [DataState].
 *
 * @property repository The repository instance used to fetch the about information.
 */
class GetAboutInfoUseCase(
    private val repository: AboutRepository,
    private val firebaseController: FirebaseController,
) {

    operator fun invoke(): Flow<DataState<AboutInfo, Errors>> = flow {
        firebaseController.logBreadcrumb(
            message = "About info load started",
            attributes = mapOf("source" to "GetAboutInfoUseCase"),
        )
        val info = repository.getAboutInfo()
        emit(DataState.Success(info))
    }
}
