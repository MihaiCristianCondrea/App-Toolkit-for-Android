package com.d4rk.android.apps.apptoolkit.core.utils.extensions

import android.database.sqlite.SQLiteException
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.sql.SQLException

// FIXME: I'll have to think abou tthis
fun Errors.asUiText() : UiTextHelper {
    return when (this) {
        // Network errors
        else -> UiTextHelper.StringResource(com.d4rk.android.libs.apptoolkit.R.string.unknown_error)

    }
}

// FIXME: This too
fun Throwable.toError(default : Errors = Errors.UseCase.NO_DATA) : Errors {
    return when (this) {
        is UnknownHostException -> Errors.Network.NO_INTERNET
        is SocketTimeoutException -> Errors.Network.REQUEST_TIMEOUT
        is ConnectException -> Errors.Network.NO_INTERNET
        is SerializationException -> Errors.Network.SERIALIZATION
        is SQLException , is SQLiteException -> Errors.Database.DATABASE_OPERATION_FAILED
        is IllegalArgumentException -> Errors.UseCase.ILLEGAL_ARGUMENT
        else -> default
    }
}