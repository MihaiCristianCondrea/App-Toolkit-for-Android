package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.app.Activity
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestConsentFormHelper {
    @Test
    fun `showConsentForm success returns`() = runBlocking {
        println("🚀 [TEST] showConsentForm success returns")
        val activity = mockk<Activity>()
        val consentInfo = mockk<ConsentInformation>()
        val consentForm = mockk<ConsentForm>()

        every { consentInfo.requestConsentInfoUpdate(activity, any(), any(), any()) } answers {
            val onSuccess = arg<() -> Unit>(2)
            onSuccess()
        }

        mockkStatic(UserMessagingPlatform::class)
        every { UserMessagingPlatform.loadConsentForm(any(), any(), any()) } answers {
            val onLoaded = arg<(ConsentForm) -> Unit>(1)
            onLoaded(consentForm)
        }
        every { consentForm.show(activity, any()) } answers {
            val onDismissed = arg<() -> Unit>(1)
            onDismissed()
        }

        ConsentFormHelper.showConsentForm(activity, consentInfo)
        verify { consentForm.show(activity, any()) }
        println("🏁 [TEST DONE] showConsentForm success returns")
    }

    @Test
    fun `showConsentForm handles exception from loadConsentForm`() = runBlocking {
        println("🚀 [TEST] showConsentForm handles exception from loadConsentForm")
        val activity = mockk<Activity>()
        val consentInfo = mockk<ConsentInformation>()

        every { consentInfo.requestConsentInfoUpdate(activity, any(), any(), any()) } answers {
            val onSuccess = arg<() -> Unit>(2)
            onSuccess()
        }

        mockkStatic(UserMessagingPlatform::class)
        every {
            UserMessagingPlatform.loadConsentForm(
                any(),
                any(),
                any()
            )
        } throws RuntimeException("fail")

        ConsentFormHelper.showConsentForm(activity, consentInfo)
        println("🏁 [TEST DONE] showConsentForm handles exception from loadConsentForm")
    }

    @Test
    fun `showConsentForm handles OutOfMemoryError from loadConsentForm`() = runBlocking {
        println("🚀 [TEST] showConsentForm handles OutOfMemoryError from loadConsentForm")
        val activity = mockk<Activity>()
        val consentInfo = mockk<ConsentInformation>()

        every { consentInfo.requestConsentInfoUpdate(activity, any(), any(), any()) } answers {
            val onSuccess = arg<() -> Unit>(2)
            onSuccess()
        }

        mockkStatic(UserMessagingPlatform::class)
        every {
            UserMessagingPlatform.loadConsentForm(
                any(),
                any(),
                any()
            )
        } throws OutOfMemoryError("oom")

        ConsentFormHelper.showConsentForm(activity, consentInfo)
        println("🏁 [TEST DONE] showConsentForm handles OutOfMemoryError from loadConsentForm")
    }
}
