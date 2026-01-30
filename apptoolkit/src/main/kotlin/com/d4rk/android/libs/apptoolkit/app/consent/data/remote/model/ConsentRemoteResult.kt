package com.d4rk.android.libs.apptoolkit.app.consent.data.remote.model

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors

/**
 * Result wrapper for remote consent operations.
 */
sealed interface ConsentRemoteResult {
    data object Success : ConsentRemoteResult
    data class Failure(val error: Errors.UseCase) : ConsentRemoteResult
}
