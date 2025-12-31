package com.d4rk.android.apps.apptoolkit.core.domain.model.network

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Error
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors

/**
 * App-specific error surface.
 *
 * The app can emit its own errors (e.g., developer apps list) while still allowing shared
 * toolkit errors to flow through unchanged via [Common]. This keeps the app extensible without
 * duplicating the shared error taxonomy.
 */
sealed interface AppErrors : Error {

    /**
     * Wrapper that allows the app module to propagate shared toolkit errors.
     */
    data class Common(val value: Errors) : AppErrors

    enum class UseCase : AppErrors {
        FAILED_TO_LOAD_APPS
    }
}
