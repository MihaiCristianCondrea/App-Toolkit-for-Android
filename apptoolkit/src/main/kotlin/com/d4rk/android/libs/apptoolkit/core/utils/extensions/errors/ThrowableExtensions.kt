package com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors

import android.database.sqlite.SQLiteException
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.sql.SQLException

/**
 * Converts a [Throwable] into a domain-level [Errors] value.
 *
 * The mapping centralizes common transport and storage failures so that repositories and use
 * cases do not duplicate boilerplate checks.
 *
 * @param default value returned when the throwable type is not recognized.
 * @return [Errors] describing the failure.
 */
fun Throwable.toError(default: Errors = Errors.UseCase.NO_DATA): Errors =
    when (this) {
        is UnknownHostException,
        is ConnectException -> Errors.Network.NO_INTERNET

        is SocketTimeoutException -> Errors.Network.REQUEST_TIMEOUT
        is SerializationException -> Errors.Network.SERIALIZATION
        is SQLException, is SQLiteException -> Errors.Database.DATABASE_OPERATION_FAILED
        is IllegalArgumentException -> Errors.UseCase.ILLEGAL_ARGUMENT
        else -> default
    }
