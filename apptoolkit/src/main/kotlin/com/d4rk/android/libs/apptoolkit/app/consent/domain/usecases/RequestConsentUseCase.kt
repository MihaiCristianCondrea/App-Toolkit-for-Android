package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow

/**
 * Emits consent-loading results as a [Flow] so callers can react to loading, success, or errors.
 */
class RequestConsentUseCase(
    private val repository: ConsentRepository,
) {

    operator fun invoke(
        host: ConsentHost,
        showIfRequired: Boolean = true,
    ): Flow<DataState<Unit, Errors.UseCase>> {
        return repository.requestConsent(
            host = host,
            showIfRequired = showIfRequired,
        )
    }
}
