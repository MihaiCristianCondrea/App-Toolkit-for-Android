package com.d4rk.android.apps.apptoolkit.core.domain.model.network

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Error

/**
 * App module re-exports the shared [com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors]
 * to avoid duplicating domain error definitions. All call sites should rely on this alias so that
 * throwable-to-error mappings and UI representations share one canonical model.
 */
sealed interface Errors : Error {

    enum class Network : Errors {
        REQUEST_TIMEOUT, NO_INTERNET, SERIALIZATION
    }

    enum class UseCase : Errors {
        NO_DATA, FAILED_TO_LOAD_APPS, ILLEGAL_ARGUMENT, FAILED_TO_LAUNCH_REVIEW, FAILED_TO_LOAD_FAQ, FAILED_TO_REQUEST_REVIEW, FAILED_TO_UPDATE_APP, FAILED_TO_LOAD_SKU_DETAILS, FAILED_TO_LOAD_CONSENT_INFO
    }

    enum class Database : Errors {
        DATABASE_OPERATION_FAILED
    }
}
