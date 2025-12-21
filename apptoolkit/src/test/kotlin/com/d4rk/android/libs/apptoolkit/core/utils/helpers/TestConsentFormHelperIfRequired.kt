package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.app.Activity
import android.util.Log
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestConsentFormHelperIfRequired {
    @Test
    fun `showConsentFormIfRequired when required loads and shows form`() = runBlocking {
        println("🚀 [TEST] showConsentFormIfRequired when required loads and shows form")
        val activity = mockk<Activity>()
        val consentInfo = mockk<ConsentInformation>()
        val consentForm = mockk<ConsentForm>()

        every { consentInfo.consentStatus } returns ConsentInformation.ConsentStatus.REQUIRED
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

        ConsentFormHelper.showConsentFormIfRequired(activity, consentInfo)

        verify { UserMessagingPlatform.loadConsentForm(any(), any(), any()) }
        verify { consentForm.show(activity, any()) }
        println("🏁 [TEST DONE] showConsentFormIfRequired when required loads and shows form")
    }

    @Test
    fun `showConsentFormIfRequired when not required skips loading`() = runBlocking {
        println("🚀 [TEST] showConsentFormIfRequired when not required skips loading")
        val activity = mockk<Activity>()
        val consentInfo = mockk<ConsentInformation>()

        every { consentInfo.consentStatus } returns ConsentInformation.ConsentStatus.NOT_REQUIRED
        every { consentInfo.requestConsentInfoUpdate(activity, any(), any(), any()) } answers {
            val onSuccess = arg<() -> Unit>(2)
            onSuccess()
        }

        mockkStatic(UserMessagingPlatform::class)

        ConsentFormHelper.showConsentFormIfRequired(activity, consentInfo)

        verify(exactly = 0) { UserMessagingPlatform.loadConsentForm(any(), any(), any()) }
        println("🏁 [TEST DONE] showConsentFormIfRequired when not required skips loading")
    }

    @Test
    fun `showConsentFormIfRequired handles load error`() = runBlocking {
        println("🚀 [TEST] showConsentFormIfRequired handles load error")
        val activity = mockk<Activity>()
        val consentInfo = mockk<ConsentInformation>()

        every { consentInfo.consentStatus } returns ConsentInformation.ConsentStatus.REQUIRED
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
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        ConsentFormHelper.showConsentFormIfRequired(activity, consentInfo)

        verify { Log.e(any(), any(), any()) }
        println("🏁 [TEST DONE] showConsentFormIfRequired handles load error")
    }
}
