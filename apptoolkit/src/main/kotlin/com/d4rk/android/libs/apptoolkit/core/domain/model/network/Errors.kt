package com.d4rk.android.libs.apptoolkit.core.domain.model.network

/**
 * A sealed interface representing the different error categories surfaced by the toolkit.
 * It groups issues under Network, UseCase, and Database to enable consistent error handling.
 */
sealed interface Errors : Error {

    enum class Network : Errors {
        NO_INTERNET,
        CONNECTION_ERROR,
        CONNECTION_CLOSED,

        REQUEST_TIMEOUT,

        SSL_ERROR,

        HTTP_REDIRECT,
        HTTP_CLIENT_ERROR,
        HTTP_SERVER_ERROR,
        RATE_LIMITED,

        SERIALIZATION,

        UNKNOWN,
    }

    enum class UseCase : Errors {
        NO_DATA,
        FAILED_TO_LAUNCH_REVIEW,
        FAILED_TO_LOAD_FAQ,
        FAILED_TO_REQUEST_REVIEW,
        FAILED_TO_UPDATE_APP,
        FAILED_TO_LOAD_SKU_DETAILS,
        FAILED_TO_LOAD_CONSENT_INFO,

        ILLEGAL_ARGUMENT,
        INVALID_STATE,
        UNSUPPORTED_OPERATION,
        CANCELLED,
    }

    enum class Database : Errors {
        DATABASE_OPERATION_FAILED,
        DATABASE_LOCKED,
        DATABASE_CONSTRAINT,
        DATABASE_CANT_OPEN,
        DATABASE_CORRUPT,
        DATABASE_FULL,
    }
}
