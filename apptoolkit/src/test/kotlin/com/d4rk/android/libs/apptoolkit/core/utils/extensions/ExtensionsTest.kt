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
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiHost
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean.toApiEnvironment
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.toError
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.normalizeRoute
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.sanitizeUrlOrNull
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.robolectric.annotation.Config
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.sql.SQLException
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFailsWith

@Config(sdk = [34])
class ExtensionsTest {

    @Test
    fun `toApiEnvironment maps debug flag to environment`() {
        assertAll(
            { assertEquals("debug", true.toApiEnvironment()) },
            { assertEquals("release", false.toApiEnvironment()) },
        )
    }

    @Test
    fun `ApiHost builds public metadata routes and encodes packages`() {
        val baseUrl = "https://example.com/"

        assertAll(
            {
                assertEquals(
                    "https://example.com/api/v1/apps",
                    ApiHost.appsUrl(baseUrl),
                )
            },
            {
                assertEquals(
                    "https://example.com/api/v1/apps/com.example%2Fapp%20name",
                    ApiHost.appDetailsUrl(
                        packageName = "com.example/app name",
                        baseUrl = baseUrl,
                    ),
                )
            },
            {
                assertEquals(
                    "https://example.com/api/v1/apps/com.example.app/changelog.md",
                    ApiHost.appChangelogUrl(
                        packageName = "com.example.app",
                        baseUrl = baseUrl,
                    ),
                )
            },
        )
        assertFailsWith<IllegalArgumentException> {
            ApiHost.appDetailsUrl(packageName = " ", baseUrl = baseUrl)
        }
    }

    @Test
    fun `sanitizeUrlOrNull trims whitespace and handles blank input`() {
        assertAll(
            { assertEquals("https://d4rk.dev", " https://d4rk.dev ".sanitizeUrlOrNull()) },
            { assertNull("   ".sanitizeUrlOrNull()) },
            { assertNull(null.sanitizeUrlOrNull()) },
            { assertNull("https://host.com/image with spaces.png".sanitizeUrlOrNull()) },
            { assertNull("www.host.com/image.png".sanitizeUrlOrNull()) },
            { assertNull("ftp://host.com/image.png".sanitizeUrlOrNull()) },
            { assertNull("https:///image.png".sanitizeUrlOrNull()) },
        )
    }

    @Test
    fun `normalizeRoute extracts route segment`() {
        assertAll(
            { assertEquals("home", "home?param=value".normalizeRoute()) },
            { assertEquals("home", "home/details".normalizeRoute()) },
            { assertNull("".normalizeRoute()) },
            { assertNull("   ".normalizeRoute()) },
            { assertNull(null.normalizeRoute()) },
        )
    }

    @Test
    fun `toError maps throwable to domain error`() {
        assertAll(
            { assertEquals(Errors.Network.NO_INTERNET, java.net.UnknownHostException().toError()) },
            {
                assertEquals(
                    Errors.Network.REQUEST_TIMEOUT,
                    SocketTimeoutException().toError()
                )
            },
            { assertEquals(Errors.Network.NO_INTERNET, ConnectException().toError()) },
            {
                assertEquals(
                    Errors.Network.SERIALIZATION,
                    SerializationException("invalid").toError()
                )
            },
            {
                assertEquals(
                    Errors.Database.DATABASE_OPERATION_FAILED,
                    SQLException().toError()
                )
            },
            {
                assertEquals(
                    Errors.UseCase.ILLEGAL_ARGUMENT,
                    IllegalArgumentException().toError()
                )
            },
            { assertEquals(Errors.UseCase.NO_DATA, IllegalStateException().toError()) },
        )
    }
}
