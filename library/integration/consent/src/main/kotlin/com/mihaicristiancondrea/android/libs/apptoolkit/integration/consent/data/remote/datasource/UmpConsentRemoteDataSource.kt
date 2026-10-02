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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.datasource

import android.util.Log
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.CONSENT_LOG_TAG
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.AdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.canShowConsentForm
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * UMP-backed implementation of [ConsentRemoteDataSource].
 *
 * Every UMP callback ends the round trip once: with success, or with a [ConsentException] whose
 * reason says which step failed. A form dismissed with a [FormError] is a failure, not a success.
 *
 * @param adMobAppIdProvider resolves the *host* app's AdMob application id. Passing a foreign id to
 * UMP produces consent requests against a publisher account that does not own the running app,
 * which is the failure mode that precedes the SDK's metrics-ping crash.
 */
class UmpConsentRemoteDataSource(
    private val adMobAppIdProvider: AdMobAppIdProvider,
) : ConsentRemoteDataSource {

    override suspend fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Unit = suspendCancellableCoroutine { continuation ->
        val finish: (ConsentException?) -> Unit = { failure ->
            if (continuation.isActive) {
                if (failure == null) continuation.resume(Unit) else continuation.resumeWithException(failure)
            }
        }

        try {
            val consentInfo = UserMessagingPlatform.getConsentInformation(host.activity)
            consentInfo.requestConsentInfoUpdate(
                host.activity,
                buildRequestParameters(),
                { showForm(host = host, showIfRequired = showIfRequired, finish = finish) },
                { requestError: FormError ->
                    finish(
                        consentFailure(
                            reason = ConsentException.Reason.REQUEST_FAILED,
                            message = "Failed to request consent info: ${requestError.message}",
                        )
                    )
                },
            )
        } catch (exception: Exception) {
            finish(
                consentFailure(
                    reason = ConsentException.Reason.REQUEST_FAILED,
                    message = "Failed to request consent info.",
                    cause = exception,
                )
            )
        }
    }

    /**
     * Shows the form once the consent information is up to date. The host is checked again first,
     * because it can go away while the update is in flight, and showing a form on a finishing
     * activity throws from the window manager.
     */
    private fun showForm(
        host: ConsentHost,
        showIfRequired: Boolean,
        finish: (ConsentException?) -> Unit,
    ) {
        if (!host.canShowConsentForm) {
            finish(
                consentFailure(
                    reason = ConsentException.Reason.HOST_UNAVAILABLE,
                    message = "Consent host is no longer able to show a form.",
                )
            )
            return
        }
        if (showIfRequired) {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(host.activity) { formError: FormError? ->
                finish(formError?.toFormFailure())
            }
        } else {
            UserMessagingPlatform.loadConsentForm(
                host.activity,
                { consentForm: ConsentForm -> showLoadedForm(host = host, consentForm = consentForm, finish = finish) },
                { formError: FormError ->
                    finish(
                        consentFailure(
                            reason = ConsentException.Reason.FORM_FAILED,
                            message = "Failed to load consent form: ${formError.message}",
                        )
                    )
                },
            )
        }
    }

    /** Shows a form loaded on request, unless the host stopped while it was loading. */
    private fun showLoadedForm(
        host: ConsentHost,
        consentForm: ConsentForm,
        finish: (ConsentException?) -> Unit,
    ) {
        if (!host.canShowConsentForm) {
            finish(
                consentFailure(
                    reason = ConsentException.Reason.HOST_UNAVAILABLE,
                    message = "Consent form loaded after the host stopped; not showing.",
                )
            )
            return
        }
        try {
            consentForm.show(host.activity) { formError: FormError? -> finish(formError?.toFormFailure()) }
        } catch (exception: Exception) {
            finish(
                consentFailure(
                    reason = ConsentException.Reason.FORM_FAILED,
                    message = "Failed to show consent form.",
                    cause = exception,
                )
            )
        }
    }

    private fun FormError.toFormFailure(): ConsentException =
        consentFailure(
            reason = ConsentException.Reason.FORM_FAILED,
            message = "Consent form error: $message",
        )

    /** Logs [message] and returns it as a [ConsentException]. */
    private fun consentFailure(
        reason: ConsentException.Reason,
        message: String,
        cause: Throwable? = null,
    ): ConsentException {
        if (reason == ConsentException.Reason.HOST_UNAVAILABLE) {
            Log.w(CONSENT_LOG_TAG, message, cause)
        } else {
            Log.e(CONSENT_LOG_TAG, message, cause)
        }
        return ConsentException(reason = reason, message = message, cause = cause)
    }

    /**
     * Builds the request parameters for UMP with the host app's AdMob id from its manifest, the
     * value the Google Mobile Ads SDK reads. Without a valid id, `setAdMobAppId` is skipped rather
     * than falling back to a library constant.
     */
    private fun buildRequestParameters(): ConsentRequestParameters {
        val builder = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)

        val appId: String? = adMobAppIdProvider.adMobAppId()
        if (appId != null) {
            builder.setAdMobAppId(appId)
        } else {
            Log.w(
                CONSENT_LOG_TAG,
                "Requesting consent without an AdMob app id: the host app declares no valid " +
                        "${AdMobAppIdProvider.MANIFEST_METADATA_KEY} meta-data."
            )
        }

        return builder.build()
    }
}
