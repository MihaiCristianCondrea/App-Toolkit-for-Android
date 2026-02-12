/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.toError
import kotlinx.serialization.SerializationException
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.sql.SQLException
import kotlin.test.assertEquals

class ErrorsKtTest {

    @Test
    fun `maps network exceptions to errors`() {
        assertEquals(Errors.Network.NO_INTERNET, UnknownHostException().toError())
        assertEquals(Errors.Network.NO_INTERNET, ConnectException().toError())
        assertEquals(Errors.Network.REQUEST_TIMEOUT, SocketTimeoutException().toError())
    }

    @Test
    fun `maps serialization and database exceptions`() {
        assertEquals(Errors.Network.SERIALIZATION, SerializationException("oops").toError())
        assertEquals(Errors.Database.DATABASE_OPERATION_FAILED, SQLException().toError())
    }

    @Test
    fun `maps illegal argument to use case error`() {
        assertEquals(Errors.UseCase.ILLEGAL_ARGUMENT, IllegalArgumentException().toError())
    }

    @Test
    fun `returns default error when not mapped`() {
        val default = Errors.UseCase.NO_DATA
        assertEquals(default, IllegalStateException().toError(default))
    }
}
