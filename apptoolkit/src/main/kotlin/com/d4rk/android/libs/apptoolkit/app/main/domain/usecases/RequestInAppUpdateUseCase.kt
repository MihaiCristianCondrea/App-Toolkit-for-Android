package com.d4rk.android.libs.apptoolkit.app.main.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateHost
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateResult
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.InAppUpdateRepository
import kotlinx.coroutines.flow.Flow

/**
 * Requests an in-app update and exposes the update attempt as a stream.
 */
class RequestInAppUpdateUseCase(
    private val repository: InAppUpdateRepository,
) {
    operator fun invoke(host: InAppUpdateHost): Flow<InAppUpdateResult> {
        return repository.requestUpdate(host = host)
    }
}
