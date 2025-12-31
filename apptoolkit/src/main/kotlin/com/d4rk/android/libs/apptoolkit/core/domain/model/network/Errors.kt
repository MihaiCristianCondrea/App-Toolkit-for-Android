package com.d4rk.android.libs.apptoolkit.core.domain.model.network

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LAUNCH_REVIEW
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LOAD_APPS
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LOAD_FAQ
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LOAD_SKU_DETAILS
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_REQUEST_REVIEW
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_UPDATE_APP
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.ILLEGAL_ARGUMENT
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.NO_DATA


/**
 * A sealed interface representing the different error categories surfaced by the toolkit.
 * It groups issues under Network, UseCase, and Database to enable consistent error handling.
 */
sealed interface Errors : Error {

    /**
     * Represents network-related failures such as connectivity, request timing, or parsing issues.
     */
    enum class Network : Errors {
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERIALIZATION,
    }

    /**
     * Represents errors specific to business logic flows.
     *
     * - [NO_DATA] - Expected data is missing or empty.
     * - [FAILED_TO_LAUNCH_REVIEW] - Launching the in-app review flow failed.
     * - [FAILED_TO_LOAD_FAQ] - FAQ content could not be retrieved.
     * - [FAILED_TO_REQUEST_REVIEW] - Requesting the in-app review dialog failed.
     * - [FAILED_TO_UPDATE_APP] - An update operation failed.
     * - [FAILED_TO_LOAD_SKU_DETAILS] - SKU or product details could not be loaded.
     * - [FAILED_TO_LOAD_CONSENT_INFO] - Consent information failed to load.
     * - [FAILED_TO_LOAD_APPS] - Application listings failed to load.
     * - [ILLEGAL_ARGUMENT] - An invalid argument was provided to a use case.
     */
    enum class UseCase : Errors {
        NO_DATA,
        FAILED_TO_LAUNCH_REVIEW,
        FAILED_TO_LOAD_FAQ,
        FAILED_TO_REQUEST_REVIEW,
        FAILED_TO_UPDATE_APP,
        FAILED_TO_LOAD_SKU_DETAILS,
        FAILED_TO_LOAD_CONSENT_INFO,
        FAILED_TO_LOAD_APPS,
        ILLEGAL_ARGUMENT,
    }

    /**
     * Represents database-level failures.
     */
    enum class Database : Errors {
        DATABASE_OPERATION_FAILED,
    }
}
