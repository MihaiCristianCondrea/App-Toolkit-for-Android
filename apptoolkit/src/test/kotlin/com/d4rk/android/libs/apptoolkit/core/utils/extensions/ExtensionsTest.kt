package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiLanguages
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiPaths
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean.toApiEnvironment
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.errors.toError
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.developerAppsApiUrl
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

    // FIXME:
    /*


Multiple Failures (3 failures)
	java.lang.AssertionError: Expected <NO_INTERNET>, actual <CONNECTION_ERROR>.
	java.lang.AssertionError: Expected <DATABASE_OPERATION_FAILED>, actual <UNKNOWN>.
	java.lang.AssertionError: Expected <NO_DATA>, actual <INVALID_STATE>.
org.opentest4j.MultipleFailuresError: Multiple Failures (3 failures)
	java.lang.AssertionError: Expected <NO_INTERNET>, actual <CONNECTION_ERROR>.
	java.lang.AssertionError: Expected <DATABASE_OPERATION_FAILED>, actual <UNKNOWN>.
	java.lang.AssertionError: Expected <NO_DATA>, actual <INVALID_STATE>.
	at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:80)
	at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:58)
	at org.junit.jupiter.api.Assertions.assertAll(Assertions.java:3106)
	at org.junit.jupiter.api.AssertionsKt.assertAll(Assertions.kt:69)
	at org.junit.jupiter.api.AssertionsKt.assertAll(Assertions.kt:102)
	at com.d4rk.android.libs.apptoolkit.core.utils.extensions.ExtensionsTest.toError maps throwable to domain error(ExtensionsTest.kt:100)
	Suppressed: java.lang.AssertionError: Expected <NO_INTERNET>, actual <CONNECTION_ERROR>.
		at kotlin.test.DefaultAsserter.fail(DefaultAsserter.kt:16)
		at kotlin.test.Asserter$DefaultImpls.assertTrue(Assertions.kt:694)
		at kotlin.test.DefaultAsserter.assertTrue(DefaultAsserter.kt:11)
		at kotlin.test.Asserter$DefaultImpls.assertEquals(Assertions.kt:713)
		at kotlin.test.DefaultAsserter.assertEquals(DefaultAsserter.kt:11)
		at kotlin.test.AssertionsKt__AssertionsKt.assertEquals(Assertions.kt:63)
		at kotlin.test.AssertionsKt.assertEquals(Unknown Source)
		at kotlin.test.AssertionsKt__AssertionsKt.assertEquals$default(Assertions.kt:62)
		at kotlin.test.AssertionsKt.assertEquals$default(Unknown Source)
		at com.d4rk.android.libs.apptoolkit.core.utils.extensions.ExtensionsTest.toError_maps_throwable_to_domain_error$lambda$2(ExtensionsTest.kt:108)
		at org.junit.jupiter.api.AssertionsKt.convert$lambda$0$0(Assertions.kt:64)
		at org.junit.jupiter.api.AssertAll.lambda$assertAll$0(AssertAll.java:68)
		at java.base/java.util.stream.ReferencePipeline$3$1.accept(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline$3$1.accept(Unknown Source)
		at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.copyInto(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.evaluate(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.evaluateToArrayNode(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toArray(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toArray(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toList(Unknown Source)
		at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:77)
		... 5 more
	Suppressed: java.lang.AssertionError: Expected <DATABASE_OPERATION_FAILED>, actual <UNKNOWN>.
		at kotlin.test.DefaultAsserter.fail(DefaultAsserter.kt:16)
		at kotlin.test.Asserter$DefaultImpls.assertTrue(Assertions.kt:694)
		at kotlin.test.DefaultAsserter.assertTrue(DefaultAsserter.kt:11)
		at kotlin.test.Asserter$DefaultImpls.assertEquals(Assertions.kt:713)
		at kotlin.test.DefaultAsserter.assertEquals(DefaultAsserter.kt:11)
		at kotlin.test.AssertionsKt__AssertionsKt.assertEquals(Assertions.kt:63)
		at kotlin.test.AssertionsKt.assertEquals(Unknown Source)
		at kotlin.test.AssertionsKt__AssertionsKt.assertEquals$default(Assertions.kt:62)
		at kotlin.test.AssertionsKt.assertEquals$default(Unknown Source)
		at com.d4rk.android.libs.apptoolkit.core.utils.extensions.ExtensionsTest.toError_maps_throwable_to_domain_error$lambda$4(ExtensionsTest.kt:116)
		at org.junit.jupiter.api.AssertionsKt.convert$lambda$0$0(Assertions.kt:64)
		at org.junit.jupiter.api.AssertAll.lambda$assertAll$0(AssertAll.java:68)
		at java.base/java.util.stream.ReferencePipeline$3$1.accept(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline$3$1.accept(Unknown Source)
		at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.copyInto(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.evaluate(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.evaluateToArrayNode(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toArray(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toArray(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toList(Unknown Source)
		at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:77)
		... 5 more
	Suppressed: java.lang.AssertionError: Expected <NO_DATA>, actual <INVALID_STATE>.
		at kotlin.test.DefaultAsserter.fail(DefaultAsserter.kt:16)
		at kotlin.test.Asserter$DefaultImpls.assertTrue(Assertions.kt:694)
		at kotlin.test.DefaultAsserter.assertTrue(DefaultAsserter.kt:11)
		at kotlin.test.Asserter$DefaultImpls.assertEquals(Assertions.kt:713)
		at kotlin.test.DefaultAsserter.assertEquals(DefaultAsserter.kt:11)
		at kotlin.test.AssertionsKt__AssertionsKt.assertEquals(Assertions.kt:63)
		at kotlin.test.AssertionsKt.assertEquals(Unknown Source)
		at kotlin.test.AssertionsKt__AssertionsKt.assertEquals$default(Assertions.kt:62)
		at kotlin.test.AssertionsKt.assertEquals$default(Unknown Source)
		at com.d4rk.android.libs.apptoolkit.core.utils.extensions.ExtensionsTest.toError_maps_throwable_to_domain_error$lambda$6(ExtensionsTest.kt:127)
		at org.junit.jupiter.api.AssertionsKt.convert$lambda$0$0(Assertions.kt:64)
		at org.junit.jupiter.api.AssertAll.lambda$assertAll$0(AssertAll.java:68)
		at java.base/java.util.stream.ReferencePipeline$3$1.accept(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline$3$1.accept(Unknown Source)
		at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.copyInto(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.evaluate(Unknown Source)
		at java.base/java.util.stream.AbstractPipeline.evaluateToArrayNode(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toArray(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toArray(Unknown Source)
		at java.base/java.util.stream.ReferencePipeline.toList(Unknown Source)
		at org.junit.jupiter.api.AssertAll.assertAll(AssertAll.java:77)
		... 5 more
    */
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
