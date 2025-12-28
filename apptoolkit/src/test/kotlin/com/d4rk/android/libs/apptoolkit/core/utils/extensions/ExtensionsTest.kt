package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiLanguages
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiPaths
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
    fun `developerAppsApiUrl appends environment language and path segments`() {
        val baseUrl = "https://example.com/repository"

        assertAll(
            {
                assertEquals(
                    "$baseUrl/${ApiEnvironments.ENV_DEBUG}/${ApiLanguages.DEFAULT}/${ApiPaths.DEVELOPER_APPS_API}",
                    ApiEnvironments.ENV_DEBUG.developerAppsApiUrl(
                        baseRepositoryUrl = baseUrl,
                        language = ApiLanguages.DEFAULT,
                    )
                )
            },
            {
                assertEquals(
                    "$baseUrl/${ApiEnvironments.ENV_RELEASE}/${ApiLanguages.DEFAULT}/${ApiPaths.DEVELOPER_APPS_API}",
                    ApiEnvironments.ENV_RELEASE.developerAppsApiUrl(
                        baseRepositoryUrl = baseUrl,
                        language = ApiLanguages.DEFAULT,
                    )
                )
            },
            {
                assertEquals(
                    "$baseUrl/${ApiEnvironments.ENV_RELEASE}/${ApiLanguages.DEFAULT}/${ApiPaths.DEVELOPER_APPS_API}",
                    "unknown".developerAppsApiUrl(
                        baseRepositoryUrl = baseUrl,
                        language = ApiLanguages.DEFAULT,
                    )
                )
            },
            {
                assertTrue(
                    ApiConstants.BASE_REPOSITORY_URL.developerAppsApiUrl()
                        .contains(ApiConstants.BASE_REPOSITORY_URL) &&
                            ApiConstants.BASE_REPOSITORY_URL.developerAppsApiUrl()
                                .contains(ApiLanguages.DEFAULT)
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
