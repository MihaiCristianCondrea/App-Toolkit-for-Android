package com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors

import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDatabaseLockedException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.ProtocolException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.sql.SQLException
import javax.net.ssl.SSLException
import io.ktor.client.network.sockets.ConnectTimeoutException as KtorConnectTimeoutException

/**
 * Converts a [Throwable] into a domain-level [Errors] value.
 *
 * The mapping centralizes common transport and storage failures so that repositories and use
 * cases do not duplicate boilerplate checks.
 *
 * @param default value returned when the throwable type is not recognized.
 * @return [Errors] describing the failure.
 */
fun Throwable.toError(default: Errors = Errors.Network.UNKNOWN): Errors {
    return when (this) {
        is CancellationException -> Errors.UseCase.CANCELLED

        is HttpRequestTimeoutException,
        is KtorConnectTimeoutException,
        is SocketTimeoutException -> Errors.Network.REQUEST_TIMEOUT

        is RedirectResponseException -> Errors.Network.HTTP_REDIRECT
        is ClientRequestException -> if (response.status.value == 429) Errors.Network.RATE_LIMITED else Errors.Network.HTTP_CLIENT_ERROR
        is ServerResponseException -> Errors.Network.HTTP_SERVER_ERROR
        is ResponseException -> Errors.Network.UNKNOWN

        is UnknownHostException -> Errors.Network.NO_INTERNET
        is ConnectException -> Errors.Network.NO_INTERNET

        is SSLException -> Errors.Network.SSL_ERROR

        is SerializationException -> Errors.Network.SERIALIZATION

        is SQLiteDatabaseLockedException -> Errors.Database.DATABASE_LOCKED
        is SQLiteConstraintException -> Errors.Database.DATABASE_CONSTRAINT
        is SQLiteCantOpenDatabaseException -> Errors.Database.DATABASE_CANT_OPEN
        is SQLiteDatabaseCorruptException -> Errors.Database.DATABASE_CORRUPT
        is SQLiteFullException -> Errors.Database.DATABASE_FULL
        is SQLiteDiskIOException,
        is SQLiteException -> Errors.Database.DATABASE_OPERATION_FAILED

        is SQLException -> Errors.Database.DATABASE_OPERATION_FAILED

        is EOFException,
        is ProtocolException,
        is SocketException -> Errors.Network.CONNECTION_CLOSED

        is IOException -> Errors.Network.CONNECTION_ERROR

        is IllegalArgumentException -> Errors.UseCase.ILLEGAL_ARGUMENT
        is UnsupportedOperationException -> Errors.UseCase.UNSUPPORTED_OPERATION
        is IllegalStateException -> Errors.UseCase.NO_DATA

        else -> default
    }
}
