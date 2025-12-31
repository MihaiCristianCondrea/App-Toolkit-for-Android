package com.d4rk.android.apps.apptoolkit.core.domain.model.network

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Error

/**
 * App module re-exports the shared [com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors]
 * to avoid duplicating domain error definitions. All call sites should rely on this alias so that
 * throwable-to-error mappings and UI representations share one canonical model.
 */
sealed interface AppErrors : Error {

    enum class Network : AppErrors {
        REQUEST_TIMEOUT, NO_INTERNET // TODO: Move generic errors to the library
    }

    enum class UseCase : AppErrors {
        NO_DATA, FAILED_TO_LOAD_APPS // TODO: keep FAILED_TO_LOAD_APPS but NO_DATA should not be there
    }
}
