package com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper

/**
 * Maps domain [Errors] to user-facing [UiTextHelper] values.
 *
 * This keeps UI layers free from error-handling boilerplate while centralizing how each category
 * should be communicated to the user.
 */
fun Errors.asUiText(): UiTextHelper {
    return when (this) {
        // Network
        Errors.Network.NO_INTERNET -> UiTextHelper.StringResource(R.string.no_internet_error)
        Errors.Network.CONNECTION_ERROR -> UiTextHelper.StringResource(R.string.connection_error)
        Errors.Network.CONNECTION_CLOSED -> UiTextHelper.StringResource(R.string.connection_closed_error)
        Errors.Network.REQUEST_TIMEOUT -> UiTextHelper.StringResource(R.string.request_timeout_error)
        Errors.Network.SSL_ERROR -> UiTextHelper.StringResource(R.string.ssl_error)

        Errors.Network.HTTP_REDIRECT -> UiTextHelper.StringResource(R.string.http_redirect_error)
        Errors.Network.HTTP_CLIENT_ERROR -> UiTextHelper.StringResource(R.string.http_client_error)
        Errors.Network.HTTP_SERVER_ERROR -> UiTextHelper.StringResource(R.string.http_server_error)
        Errors.Network.RATE_LIMITED -> UiTextHelper.StringResource(R.string.rate_limited_error)

        Errors.Network.SERIALIZATION -> UiTextHelper.StringResource(R.string.serialization_error)
        Errors.Network.UNKNOWN -> UiTextHelper.StringResource(R.string.unknown_error)

        // UseCase (generic)
        Errors.UseCase.NO_DATA -> UiTextHelper.StringResource(R.string.no_data_error)
        Errors.UseCase.ILLEGAL_ARGUMENT -> UiTextHelper.StringResource(R.string.illegal_argument_error)
        Errors.UseCase.INVALID_STATE -> UiTextHelper.StringResource(R.string.invalid_state_error)
        Errors.UseCase.UNSUPPORTED_OPERATION -> UiTextHelper.StringResource(R.string.unsupported_operation_error)
        Errors.UseCase.CANCELLED -> UiTextHelper.StringResource(R.string.cancelled_error)

        // UseCase (toolkit features)
        Errors.UseCase.FAILED_TO_LAUNCH_REVIEW -> UiTextHelper.StringResource(R.string.error_failed_to_launch_review)
        Errors.UseCase.FAILED_TO_LOAD_FAQ -> UiTextHelper.StringResource(R.string.error_failed_to_load_faq)
        Errors.UseCase.FAILED_TO_REQUEST_REVIEW -> UiTextHelper.StringResource(R.string.error_failed_to_request_review)
        Errors.UseCase.FAILED_TO_UPDATE_APP -> UiTextHelper.StringResource(R.string.error_failed_to_update_app)
        Errors.UseCase.FAILED_TO_LOAD_SKU_DETAILS -> UiTextHelper.StringResource(R.string.error_failed_to_load_sku_details)
        Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO -> UiTextHelper.StringResource(R.string.error_failed_to_load_consent_info)

        // Database
        Errors.Database.DATABASE_OPERATION_FAILED -> UiTextHelper.StringResource(R.string.database_error)
        Errors.Database.DATABASE_LOCKED -> UiTextHelper.StringResource(R.string.database_locked_error)
        Errors.Database.DATABASE_CONSTRAINT -> UiTextHelper.StringResource(R.string.database_constraint_error)
        Errors.Database.DATABASE_CANT_OPEN -> UiTextHelper.StringResource(R.string.database_cant_open_error)
        Errors.Database.DATABASE_CORRUPT -> UiTextHelper.StringResource(R.string.database_corrupt_error)
        Errors.Database.DATABASE_FULL -> UiTextHelper.StringResource(R.string.database_full_error)
    }
}
