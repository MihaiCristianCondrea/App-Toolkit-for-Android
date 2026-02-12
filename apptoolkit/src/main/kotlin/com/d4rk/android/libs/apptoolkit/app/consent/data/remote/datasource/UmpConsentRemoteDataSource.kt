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
