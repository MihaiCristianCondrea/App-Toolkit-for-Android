package com.d4rk.android.libs.apptoolkit.core.domain.model.network

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.Network.NO_INTERNET
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.Network.REQUEST_TIMEOUT
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.Network.SERIALIZATION
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LAUNCH_REVIEW
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LOAD_FAQS
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_LOAD_SKU_DETAILS
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_REQUEST_REVIEW
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.FAILED_TO_UPDATE_APP
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors.UseCase.NO_DATA


/**
 * A sealed interface representing various types of errors that can occur within an application.
 * It categorizes errors into different domains like Network, UseCase, and Database.
 * This allows for specific and granular error handling throughout the app.
 *
 * It inherits from the base [Error] interface.
 */
sealed interface Errors : Error {

    /**
     * Represents network-related errors.
     *
     * This enum class categorizes common issues that can occur during network operations,
     * such as communication problems with a server or data parsing failures.
     *
     * - [REQUEST_TIMEOUT]: Indicates that a network request took too long to complete.
     * - [NO_INTERNET]: Signals that there is no active internet connection available.
     * - [SERIALIZATION]: Occurs when there is an error parsing the network response (e.g., malformed JSON).
     */
    enum class Network : Errors {
        REQUEST_TIMEOUT , NO_INTERNET , SERIALIZATION
    }

    /**
     * Represents errors specific to application use cases.
     *
     * - [NO_DATA] - Indicates that an expected data set was not found or is empty.
     * - [FAILED_TO_LAUNCH_REVIEW] - An error occurred while trying to launch the in-app review flow.
     * - [FAILED_TO_LOAD_FAQS] - Failed to load Frequently Asked Questions data.
     * - [FAILED_TO_REQUEST_REVIEW] - The request to show the in-app review dialog failed.
     * - [FAILED_TO_UPDATE_APP] - An error occurred during the in-app update process.
     * - [FAILED_TO_LOAD_SKU_DETAILS] - Failed to load product/SKU details for in-app purchases.
     * - [FAILED_TO_LOAD_CONSENT_INFO] - Failed to load user consent information (e.g., for GDPR).
     */
    enum class UseCase : Errors {
        NO_DATA , FAILED_TO_LAUNCH_REVIEW , FAILED_TO_LOAD_FAQS , FAILED_TO_REQUEST_REVIEW , FAILED_TO_UPDATE_APP , FAILED_TO_LOAD_SKU_DETAILS , FAILED_TO_LOAD_CONSENT_INFO
    }

    /**
     * Represents errors that occur during database operations.
     */
    enum class Database : Errors {
        DATABASE_OPERATION_FAILED
    }
}
