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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote

import android.app.Activity
import android.util.Log
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.AdMobAppIdDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UmpConsentRemoteDataSourceTest {

    @BeforeEach
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.w(any(), any<String>(), any()) } returns 0
        every { Log.e(any(), any<String>(), any()) } returns 0

        mockkStatic(UserMessagingPlatform::class)
        mockkConstructor(ConsentRequestParameters.Builder::class)
        val builtParams = mockk<ConsentRequestParameters>(relaxed = true)
        every { anyConstructed<ConsentRequestParameters.Builder>().setTagForUnderAgeOfConsent(any()) } answers {
            self as ConsentRequestParameters.Builder
        }
        every { anyConstructed<ConsentRequestParameters.Builder>().setAdMobAppId(any()) } answers {
            self as ConsentRequestParameters.Builder
        }
        every { anyConstructed<ConsentRequestParameters.Builder>().build() } returns builtParams
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    private fun host(
        finishing: Boolean = false,
        destroyed: Boolean = false,
        changingConfigurations: Boolean = false,
    ): ConsentHost {
        val activity = mockk<Activity>(relaxed = true) {
            every { isFinishing } returns finishing
            every { isDestroyed } returns destroyed
            every { isChangingConfigurations } returns changingConfigurations
        }
        return ConsentHost(activity = activity)
    }

    @Test
    fun `requestConsent fails with HOST_UNAVAILABLE if host dies while resolving app id`() = runTest {
        var changing = false
        val activity = mockk<Activity>(relaxed = true) {
            every { isFinishing } returns false
            every { isDestroyed } returns false
            every { isChangingConfigurations } answers { changing }
        }
        val dataSource = UmpConsentRemoteDataSource(
            adMobAppIdDataSource = {
                changing = true
                "ca-app-pub-3940256099942544~3347511713"
            },
            dispatchers = TestDispatchers(UnconfinedTestDispatcher(testScheduler)),
        )

        val failure = assertFailsWith<ConsentException> {
            dataSource.requestConsent(host = ConsentHost(activity = activity), showIfRequired = true)
        }

        assertEquals(ConsentException.Reason.HOST_UNAVAILABLE, failure.reason)
        verify(exactly = 0) { UserMessagingPlatform.getConsentInformation(any()) }
    }

    @Test
    fun `requestConsent sets AdMob app id only when host provides one`() = runTest {
        val consentInfo = mockk<ConsentInformation>()
        every { UserMessagingPlatform.getConsentInformation(any()) } returns consentInfo
        every {
            consentInfo.requestConsentInfoUpdate(any(), any(), any(), any())
        } answers {
            val success = thirdArg<ConsentInformation.OnConsentInfoUpdateSuccessListener>()
            success.onConsentInfoUpdateSuccess()
        }
        every {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(any(), any())
        } answers {
            secondArg<ConsentForm.OnConsentFormDismissedListener>().onConsentFormDismissed(null)
        }

        val withId = UmpConsentRemoteDataSource(
            adMobAppIdDataSource = AdMobAppIdDataSource { "ca-app-pub-3940256099942544~3347511713" },
            dispatchers = TestDispatchers(UnconfinedTestDispatcher(testScheduler)),
        )
        withId.requestConsent(host = host(), showIfRequired = true)
        verify(exactly = 1) {
            anyConstructed<ConsentRequestParameters.Builder>()
                .setAdMobAppId("ca-app-pub-3940256099942544~3347511713")
        }

        val withoutId = UmpConsentRemoteDataSource(
            adMobAppIdDataSource = AdMobAppIdDataSource { null },
            dispatchers = TestDispatchers(UnconfinedTestDispatcher(testScheduler)),
        )
        withoutId.requestConsent(host = host(), showIfRequired = true)
        verify(exactly = 1) {
            anyConstructed<ConsentRequestParameters.Builder>().setAdMobAppId(any())
        }
    }

    @Test
    fun `requestConsent catches synchronous exceptions from loadAndShowConsentFormIfRequired`() = runTest {
        val consentInfo = mockk<ConsentInformation>()
        every { UserMessagingPlatform.getConsentInformation(any()) } returns consentInfo
        every {
            consentInfo.requestConsentInfoUpdate(any(), any(), any(), any())
        } answers {
            thirdArg<ConsentInformation.OnConsentInfoUpdateSuccessListener>().onConsentInfoUpdateSuccess()
        }
        every {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(any(), any())
        } throws IllegalStateException("window token invalid")

        val dataSource = UmpConsentRemoteDataSource(
            adMobAppIdDataSource = AdMobAppIdDataSource { null },
            dispatchers = TestDispatchers(UnconfinedTestDispatcher(testScheduler)),
        )

        val failure = assertFailsWith<ConsentException> {
            dataSource.requestConsent(host = host(), showIfRequired = true)
        }

        assertEquals(ConsentException.Reason.FORM_FAILED, failure.reason)
    }

    @Test
    fun `requestConsent catches synchronous exceptions from loadConsentForm`() = runTest {
        val consentInfo = mockk<ConsentInformation>()
        every { UserMessagingPlatform.getConsentInformation(any()) } returns consentInfo
        every {
            consentInfo.requestConsentInfoUpdate(any(), any(), any(), any())
        } answers {
            thirdArg<ConsentInformation.OnConsentInfoUpdateSuccessListener>().onConsentInfoUpdateSuccess()
        }
        every {
            UserMessagingPlatform.loadConsentForm(any(), any(), any())
        } throws IllegalStateException("ump init failed")

        val dataSource = UmpConsentRemoteDataSource(
            adMobAppIdDataSource = AdMobAppIdDataSource { null },
            dispatchers = TestDispatchers(UnconfinedTestDispatcher(testScheduler)),
        )

        val failure = assertFailsWith<ConsentException> {
            dataSource.requestConsent(host = host(), showIfRequired = false)
        }

        assertEquals(ConsentException.Reason.FORM_FAILED, failure.reason)
    }

    @Test
    fun `requestConsent ignores duplicate callbacks from UMP`() = runTest {
        val consentInfo = mockk<ConsentInformation>()
        val errorSlot = slot<ConsentInformation.OnConsentInfoUpdateFailureListener>()
        every { UserMessagingPlatform.getConsentInformation(any()) } returns consentInfo
        every {
            consentInfo.requestConsentInfoUpdate(any(), any(), any(), capture(errorSlot))
        } answers {
            val formError = mockk<FormError> { every { message } returns "network error" }
            errorSlot.captured.onConsentInfoUpdateFailure(formError)
            errorSlot.captured.onConsentInfoUpdateFailure(formError)
        }

        val dataSource = UmpConsentRemoteDataSource(
            adMobAppIdDataSource = AdMobAppIdDataSource { null },
            dispatchers = TestDispatchers(UnconfinedTestDispatcher(testScheduler)),
        )

        val failure = assertFailsWith<ConsentException> {
            dataSource.requestConsent(host = host(), showIfRequired = true)
        }

        assertEquals(ConsentException.Reason.REQUEST_FAILED, failure.reason)
    }
}
