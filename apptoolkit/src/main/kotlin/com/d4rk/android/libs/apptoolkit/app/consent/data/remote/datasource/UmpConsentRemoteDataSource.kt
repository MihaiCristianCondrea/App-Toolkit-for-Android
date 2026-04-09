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

package com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource

import android.util.Log
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.utils.constants.logging.CONSENT_LOG_TAG
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * UMP-backed implementation of [ConsentRemoteDataSource].
 */
class UmpConsentRemoteDataSource : ConsentRemoteDataSource {

    private companion object {
        /**
         * Canonical AdMob app id format used by UMP.
         *
         * Example: `ca-app-pub-3940256099942544~3347511713`
         */
        val AD_MOB_APP_ID_REGEX: Regex =
            Regex(pattern = "^ca-app-pub-[0-9]{16}~[0-9]{10}$")
    }

    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> = callbackFlow {
        trySend(DataState.Loading())

        val activity = host.activity
        val params = buildRequestParameters(activity)
        val consentInfo = UserMessagingPlatform.getConsentInformation(activity)

        runCatching {
            consentInfo.requestConsentInfoUpdate(
                activity,
                params,
                {
                    if (showIfRequired) {
                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                            if (formError != null) {
                                Log.e(
                                    CONSENT_LOG_TAG,
                                    "Consent form error: ${formError.message}"
                                )
                                trySend(
                                    DataState.Error(
                                        error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                                    )
                                )
                            } else {
                                trySend(DataState.Success(Unit))
                            }
                            close()
                        }
                    } else {
                        UserMessagingPlatform.loadConsentForm(
                            activity,
                            { consentForm: ConsentForm ->
                                runCatching {
                                    consentForm.show(activity) {
                                        trySend(DataState.Success(Unit))
                                        close()
                                    }
                                }.onFailure { throwable ->
                                    Log.e(
                                        CONSENT_LOG_TAG,
                                        "Failed to show consent form.",
                                        throwable
                                    )
                                    trySend(
                                        DataState.Error(
                                            error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                                        )
                                    )
                                    close()
                                }
                            },
                            { formError ->
                                Log.e(
                                    CONSENT_LOG_TAG,
                                    "Failed to load consent form: ${formError.message}"
                                )
                                trySend(
                                    DataState.Error(
                                        error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                                    )
                                )
                                close()
                            }
                        )
                    }
                },
                { requestError ->
                    Log.e(
                        CONSENT_LOG_TAG,
                        "Failed to request consent info: ${requestError.message}"
                    )
                    trySend(
                        DataState.Error(
                            error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                        )
                    )
                    close()
                }
            )
        }.onFailure { throwable ->
            Log.e(CONSENT_LOG_TAG, "Failed to request consent info.", throwable)
            trySend(DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO))
            close()
        }

        awaitClose { }
    }

    /**
     * Builds the request parameters for UMP.
     *
     * Change rationale: we previously accepted any id prefixed with `ca-app-pub-`, which still
     * allowed malformed values to reach UMP internals. Those malformed values can crash parsing in
     * the consent SDK, so we now gate `setAdMobAppId` behind the canonical AdMob app id regex and
     * skip invalid values safely.
     */
    private fun buildRequestParameters(activity: android.app.Activity): ConsentRequestParameters {
        val appId = activity.getString(R.string.ad_mob_app_id).trim()
        val builder = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)

        if (appId.matches(AD_MOB_APP_ID_REGEX)) {
            builder.setAdMobAppId(appId)
        } else {
            Log.w(
                CONSENT_LOG_TAG,
                "Skipping AdMob app id because it does not match canonical format."
            )
        }

        return builder.build()
    }
}
