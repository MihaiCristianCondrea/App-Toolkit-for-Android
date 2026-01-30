package com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource

import android.util.Log
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.model.ConsentRemoteResult
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.logging.CONSENT_LOG_TAG
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * UMP-backed implementation of [ConsentRemoteDataSource].
 */
class UmpConsentRemoteDataSource : ConsentRemoteDataSource {

    override suspend fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): ConsentRemoteResult {
        val activity = host.activity
        val params = buildRequestParameters(activity)
        val consentInfo = UserMessagingPlatform.getConsentInformation(activity)

        return suspendCancellableCoroutine { continuation ->
            try {
                consentInfo.requestConsentInfoUpdate(
                    activity,
                    params,
                    {
                        if (showIfRequired &&
                            consentInfo.consentStatus != ConsentInformation.ConsentStatus.REQUIRED &&
                            consentInfo.consentStatus != ConsentInformation.ConsentStatus.UNKNOWN
                        ) {
                            if (continuation.isActive) {
                                continuation.resume(ConsentRemoteResult.Success)
                            }
                            return@requestConsentInfoUpdate
                        }

                        UserMessagingPlatform.loadConsentForm(
                            activity,
                            { consentForm: ConsentForm ->
                                try {
                                    consentForm.show(activity) {
                                        if (continuation.isActive) {
                                            continuation.resume(ConsentRemoteResult.Success)
                                        }
                                    }
                                } catch (throwable: Throwable) {
                                    Log.e(
                                        CONSENT_LOG_TAG,
                                        "Failed to show consent form.",
                                        throwable
                                    )
                                    if (continuation.isActive) {
                                        continuation.resume(
                                            ConsentRemoteResult.Failure(
                                                error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                                            )
                                        )
                                    }
                                }
                            },
                            { formError ->
                                Log.e(
                                    CONSENT_LOG_TAG,
                                    "Failed to load consent form: ${formError.message}"
                                )
                                if (continuation.isActive) {
                                    continuation.resume(
                                        ConsentRemoteResult.Failure(
                                            error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                                        )
                                    )
                                }
                            }
                        )
                    },
                    { requestError ->
                        Log.e(
                            CONSENT_LOG_TAG,
                            "Failed to request consent info: ${requestError.message}"
                        )
                        if (continuation.isActive) {
                            continuation.resume(
                                ConsentRemoteResult.Failure(
                                    error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                                )
                            )
                        }
                    }
                )
            } catch (throwable: Throwable) {
                Log.e(CONSENT_LOG_TAG, "Failed to request consent info.", throwable)
                if (continuation.isActive) {
                    continuation.resume(
                        ConsentRemoteResult.Failure(
                            error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                        )
                    )
                }
            }
        }
    }

    /**
     * Builds the request parameters for UMP.
     *
     * Change rationale: previously the AdMob app id was always passed to UMP. If the host app
     * provided a blank or malformed id, the SDK could crash while parsing it. We now validate the
     * id before setting it to avoid runtime exceptions while still allowing consent requests.
     */
    private fun buildRequestParameters(activity: android.app.Activity): ConsentRequestParameters {
        val appId = activity.getString(R.string.ad_mob_app_id).trim()
        val builder = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)

        if (appId.isNotBlank() && appId.startsWith("ca-app-pub-")) {
            builder.setAdMobAppId(appId)
        } else {
            Log.w(CONSENT_LOG_TAG, "Skipping AdMob app id because it is blank or malformed.")
        }

        return builder.build()
    }
}
