package com.d4rk.android.libs.apptoolkit.app.main.domain.repository

import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateHost
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository that wraps Play Core in-app update interactions.
 */
interface InAppUpdateRepository {
    /**
     * Requests an in-app update and emits the result of the attempt.
     */
    fun requestUpdate(host: InAppUpdateHost): Flow<InAppUpdateResult>
}
