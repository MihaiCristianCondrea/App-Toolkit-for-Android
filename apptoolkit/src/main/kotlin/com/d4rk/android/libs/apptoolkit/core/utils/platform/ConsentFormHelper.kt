package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.app.Activity
import android.util.Log
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.logging.CONSENT_FORM_HELPER_LOG_TAG
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Helper functions for showing the Google UMP consent dialog.
 *
 * These methods expose a suspend API so callers can work with coroutines and
 * remain on the main thread while the underlying UMP SDK performs network and
 * I/O work on background threads.
 */
object ConsentFormHelper {

    /**
     * Request consent information and display the consent form if required.
     * The function returns once the form has been displayed or it has been
     * determined that showing the form isn't necessary.
     */
    suspend fun showConsentFormIfRequired(
        activity: Activity,
        consentInfo: ConsentInformation,
    ) {
        val params: ConsentRequestParameters =
            ConsentRequestParameters.Builder()
                .setAdMobAppId(activity.getString(R.string.ad_mob_app_id))
                .setTagForUnderAgeOfConsent(false).build()

        suspendCancellableCoroutine { continuation ->
            consentInfo.requestConsentInfoUpdate(activity, params, {
                if (consentInfo.consentStatus != ConsentInformation.ConsentStatus.REQUIRED &&
                    consentInfo.consentStatus != ConsentInformation.ConsentStatus.UNKNOWN
                ) {
                    if (continuation.isActive) continuation.resume(Unit)
                    return@requestConsentInfoUpdate
                }

                runCatching {
                    UserMessagingPlatform.loadConsentForm(activity, { consentForm: ConsentForm ->
                        runCatching {
                            consentForm.show(activity) {
                                if (continuation.isActive) continuation.resume(Unit)
                            }
                        }.onFailure {
                            Log.e(CONSENT_FORM_HELPER_LOG_TAG, "Failed to load consent form", it)
                            if (continuation.isActive) continuation.resume(Unit)
                        }
                    }, { t ->
                        Log.e(
                            CONSENT_FORM_HELPER_LOG_TAG,
                            "Failed to load consent form: ${t.message}"
                        )
                        if (continuation.isActive) continuation.resume(Unit)
                    })
                }.onFailure {
                    Log.e(CONSENT_FORM_HELPER_LOG_TAG, "Failed to load consent form", it)
                    if (continuation.isActive) continuation.resume(Unit)
                }
            }, {
                if (continuation.isActive) continuation.resume(Unit)
            })
        }
    }

    /**
     * Always show the consent form regardless of the current consent status.
     * The function returns once the form has been displayed (or if loading the
     * form failed).
     */
    suspend fun showConsentForm(
        activity: Activity,
        consentInfo: ConsentInformation,
    ) {
        val params: ConsentRequestParameters =
            ConsentRequestParameters.Builder()
                .setAdMobAppId(activity.getString(R.string.ad_mob_app_id))
                .setTagForUnderAgeOfConsent(false).build()

        suspendCancellableCoroutine { continuation ->
            consentInfo.requestConsentInfoUpdate(activity, params, {
                runCatching {
                    UserMessagingPlatform.loadConsentForm(activity, { consentForm: ConsentForm ->
                        runCatching {
                            consentForm.show(activity) {
                                if (continuation.isActive) continuation.resume(Unit)
                            }
                        }.onFailure {
                            if (continuation.isActive) continuation.resume(Unit)
                        }
                    }, {
                        if (continuation.isActive) continuation.resume(Unit)
                    })
                }.onFailure {
                    if (continuation.isActive) continuation.resume(Unit)
                }
            }, {
                if (continuation.isActive) continuation.resume(Unit)
            })
        }
    }
}
