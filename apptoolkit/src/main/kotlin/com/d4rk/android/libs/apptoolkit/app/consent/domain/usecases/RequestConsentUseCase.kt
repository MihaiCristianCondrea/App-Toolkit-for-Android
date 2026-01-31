package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Emits consent-loading results as a [Flow] so callers can react to loading, success, or errors.
 */
class RequestConsentUseCase(
    private val repository: ConsentRepository,
    private val firebaseController: FirebaseController,
) {

    operator fun invoke(
        host: ConsentHost,
        showIfRequired: Boolean = true,
    ): Flow<DataState<Unit, Errors.UseCase>> {
        return repository.requestConsent(
            host = host,
            showIfRequired = showIfRequired,
        ).onStart {
            firebaseController.logBreadcrumb(
                message = "Consent request started",
                attributes = mapOf(
                    "host" to host.activity::class.java.name,
                    "showIfRequired" to showIfRequired.toString(),
                ),
            )
        }
    }
}
