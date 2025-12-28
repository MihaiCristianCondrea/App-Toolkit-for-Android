package com.d4rk.android.apps.apptoolkit.core.utils.extensions

import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import com.d4rk.android.libs.apptoolkit.R as LibR

/**
 * Maps domain [Errors] to user-facing [UiTextHelper] values for the app module.
 *
 * This keeps UI layers free from error-handling boilerplate while centralizing how each category
 * should be communicated to the user.
 */
fun Errors.asUiText(): UiTextHelper =
    when (this) { // TODO | FIXME: This is unused but it will be useful for the usecases
        Errors.Network.NO_INTERNET,
        Errors.Network.REQUEST_TIMEOUT -> UiTextHelper.StringResource(R.string.error_failed_to_load_apps)

        Errors.Network.SERIALIZATION -> UiTextHelper.StringResource(LibR.string.unknown_error)

        Errors.UseCase.NO_DATA -> UiTextHelper.StringResource(R.string.error_failed_to_fetch_our_apps)
        Errors.UseCase.FAILED_TO_LOAD_APPS -> UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
        Errors.UseCase.ILLEGAL_ARGUMENT -> UiTextHelper.StringResource(LibR.string.illegal_argument_error)
        Errors.UseCase.FAILED_TO_LAUNCH_REVIEW,
        Errors.UseCase.FAILED_TO_LOAD_FAQ,
        Errors.UseCase.FAILED_TO_REQUEST_REVIEW,
        Errors.UseCase.FAILED_TO_UPDATE_APP,
        Errors.UseCase.FAILED_TO_LOAD_SKU_DETAILS,
        Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO -> UiTextHelper.StringResource(LibR.string.unknown_error) // TODO: Make strings for the errors

        Errors.Database.DATABASE_OPERATION_FAILED -> UiTextHelper.StringResource(LibR.string.io_error)
    }
