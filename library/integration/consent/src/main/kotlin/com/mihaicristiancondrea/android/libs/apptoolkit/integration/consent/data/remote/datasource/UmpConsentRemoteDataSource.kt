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
import com.google.android.ump.UserMessagingPlatform
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.canShowConsentForm
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.CONSENT_LOG_TAG
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.AdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * UMP-backed implementation of [ConsentRemoteDataSource].
 *
 * @param adMobAppIdProvider resolves the *host* app's AdMob application id. Passing a foreign id to
 * UMP produces consent requests against a publisher account that does not own the running app,
 * which is the failure mode that precedes the SDK's metrics-ping crash.
 */
class UmpConsentRemoteDataSource(
    private val adMobAppIdProvider: AdMobAppIdProvider,
) : ConsentRemoteDataSource {

    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> = callbackFlow {
        trySend(DataState.Loading())

        val activity = host.activity
        val params = buildRequestParameters()
        val consentInfo = UserMessagingPlatform.getConsentInformation(activity)

        runCatching {
            consentInfo.requestConsentInfoUpdate(
                activity,
                params,
                {
                    if (!host.canShowConsentForm) {
                        // Recheck the host after the async update: a finishing activity cannot safely show a form.
                        Log.w(CONSENT_LOG_TAG, "Consent host is no longer able to show a form.")
                        trySend(
                            DataState.Error(error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO)
                        )
                        close()
                        return@requestConsentInfoUpdate
                    }
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
                                if (!host.canShowConsentForm) {
                                    Log.w(
                                        CONSENT_LOG_TAG,
                                        "Consent form loaded after the host stopped; not showing."
                                    )
                                    trySend(
                                        DataState.Error(
                                            error = Errors.UseCase.FAILED_TO_LOAD_CONSENT_INFO
                                        )
                                    )
                                    close()
                                    return@loadConsentForm
                                }
                                runCatching {
                                    consentForm.show(activity) { formError ->
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
     * Uses the same host-manifest AdMob ID as the ads SDK, omitting the explicit ID when none
     * is valid. Supplying it also supports hosts that initialize the ads SDK lazily; no library
     * publisher ID is used.
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
