package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Config(sdk = [34])
class ExtensionsTest {

    @Test
    fun `toApiEnvironment maps debug flag to environment`() {
        assertAll(
            { assertEquals(ApiEnvironments.ENV_DEBUG, true.toApiEnvironment()) },
            { assertEquals(ApiEnvironments.ENV_RELEASE, false.toApiEnvironment()) },
        )
    }

    @Test
    fun `developerAppsBaseUrl appends environment segment`() {
        val baseUrl = "https://example.com/repository"

        assertAll(
            {
                assertEquals(
                    "$baseUrl/${ApiEnvironments.ENV_DEBUG}",
                    ApiEnvironments.ENV_DEBUG.developerAppsBaseUrl(baseUrl)
                )
            },
            {
                assertEquals(
                    "$baseUrl/${ApiEnvironments.ENV_RELEASE}",
                    ApiEnvironments.ENV_RELEASE.developerAppsBaseUrl(baseUrl)
                )
            },
            {
                assertEquals(
                    "$baseUrl/${ApiEnvironments.ENV_RELEASE}",
                    "unknown".developerAppsBaseUrl(baseUrl)
                )
            },
            {
                assertTrue(
                    ApiConstants.BASE_REPOSITORY_URL.developerAppsBaseUrl()
                        .contains(ApiConstants.BASE_REPOSITORY_URL)
                )
            },
        )
    }

    @Test
    fun `sanitizeUrlOrNull trims whitespace and handles blank input`() {
        assertAll(
            { assertEquals("https://d4rk.dev", " https://d4rk.dev ".sanitizeUrlOrNull()) },
            { assertNull("   ".sanitizeUrlOrNull()) },
            { assertNull(null.sanitizeUrlOrNull()) },
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
                    java.net.SocketTimeoutException().toError()
                )
            },
            { assertEquals(Errors.Network.NO_INTERNET, java.net.ConnectException().toError()) },
            {
                assertEquals(
                    Errors.Network.SERIALIZATION,
                    SerializationException("invalid").toError()
                )
            },
            {
                assertEquals(
                    Errors.Database.DATABASE_OPERATION_FAILED,
                    java.sql.SQLException().toError()
                )
            },
            { assertEquals(Errors.UseCase.NO_DATA, IllegalStateException().toError()) },
        )
    }
}
