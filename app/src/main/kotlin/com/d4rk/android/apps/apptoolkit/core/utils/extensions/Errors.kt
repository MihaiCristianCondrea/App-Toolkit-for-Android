package com.d4rk.android.apps.apptoolkit.core.utils.extensions

import android.database.sqlite.SQLiteException
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.sql.SQLException
import com.d4rk.android.libs.apptoolkit.R as LibR

fun Errors.asUiText(): UiTextHelper = when (this) {
    Errors.Network.NO_INTERNET,
    Errors.Network.REQUEST_TIMEOUT -> UiTextHelper.StringResource(R.string.error_failed_to_load_apps)

    Errors.Network.SERIALIZATION -> UiTextHelper.StringResource(LibR.string.unknown_error)
    Errors.UseCase.NO_DATA -> UiTextHelper.StringResource(R.string.error_failed_to_fetch_our_apps)
    Errors.UseCase.FAILED_TO_LOAD_APPS -> UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
    Errors.UseCase.ILLEGAL_ARGUMENT -> UiTextHelper.StringResource(LibR.string.illegal_argument_error)
    Errors.Database.DATABASE_OPERATION_FAILED -> UiTextHelper.StringResource(LibR.string.io_error)
}

fun Throwable.toError(default: Errors = Errors.UseCase.NO_DATA): Errors {
    return when (this) {
        is UnknownHostException -> Errors.Network.NO_INTERNET
        is SocketTimeoutException -> Errors.Network.REQUEST_TIMEOUT
        is ConnectException -> Errors.Network.NO_INTERNET
        is SerializationException -> Errors.Network.SERIALIZATION
        is SQLException, is SQLiteException -> Errors.Database.DATABASE_OPERATION_FAILED
        is IllegalArgumentException -> Errors.UseCase.ILLEGAL_ARGUMENT
        else -> default
    }
}
