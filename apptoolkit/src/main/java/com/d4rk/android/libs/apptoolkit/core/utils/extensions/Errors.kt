package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import android.database.sqlite.SQLiteException
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.sql.SQLException

/**
 * Converts a [Throwable] into a domain specific [Errors] value.
 *
 * The mapping centralizes error handling by translating common
 * networking, serialization and database failures into
 * semantic categories understood by the rest of the toolkit.
 *
 * @param default value returned when the [Throwable] type is not recognized
 * @return [Errors] describing the failure
 */
fun Throwable.toError(default: Errors = Errors.UseCase.NO_DATA): Errors =
    when (this) {
        is UnknownHostException -> Errors.Network.NO_INTERNET
        is SocketTimeoutException -> Errors.Network.REQUEST_TIMEOUT
        is ConnectException -> Errors.Network.NO_INTERNET
        is SerializationException -> Errors.Network.SERIALIZATION
        is SQLException, is SQLiteException -> Errors.Database.DATABASE_OPERATION_FAILED
        else -> default
    }
