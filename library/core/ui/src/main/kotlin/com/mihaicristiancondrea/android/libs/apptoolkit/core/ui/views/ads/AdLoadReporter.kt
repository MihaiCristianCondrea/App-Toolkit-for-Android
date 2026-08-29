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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import android.util.Log
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider

/** Why an ad slot has nothing to show. */
enum class AdSlotFailure {
    /** The request came back with no ad. Usually no fill, which is normal and not a defect. */
    NO_AD,

    /** The loader could not be called at all, for example because the SDK was not initialized. */
    NOT_REQUESTED,
}

/**
 * The one place an ad that failed to load is written down.
 *
 * An empty ad slot is silent by design: the SDK reports the failure to a callback and the UI simply
 * renders nothing. That is right for users and useless for anyone trying to work out why a slot is
 * blank, because no-fill, a misconfigured unit id and an SDK that was never initialized all look
 * identical from the outside. Every toolkit ad surface routes its failures through here so the three
 * can be told apart afterwards.
 *
 * Logcat carries the detail during development. Crashlytics gets a breadcrumb for every failure and
 * a non-fatal only for the failures a developer can act on, so a device with no fill does not report
 * an issue on every scroll.
 *
 * @property firebaseController where breadcrumbs and non-fatals go.
 * @property buildInfoProvider used to decide how loudly to report; debug builds get more.
 */
class AdLoadReporter(
    private val firebaseController: FirebaseController,
    private val buildInfoProvider: BuildInfoProvider,
) {

    /**
     * Records that [adUnitId] came back without an ad.
     *
     * [errorCode] and [errorMessage] come from the SDK's `LoadAdError`. No fill is ordinary and is
     * logged but never recorded as a non-fatal: on a small app, or a device Google has no inventory
     * for, it is the expected answer and would otherwise drown the console.
     */
    fun onAdFailedToLoad(
        slotName: String,
        adUnitId: String,
        errorCode: String,
        errorMessage: String,
    ) {
        val isNoFill = errorCode.contains(other = NO_FILL_CODE, ignoreCase = true)
        Log.w(
            LOG_TAG,
            "Ad slot '$slotName' got no ad for unit '$adUnitId'. code=$errorCode ($errorMessage)",
        )

        firebaseController.logBreadcrumb(
            message = "Ad slot failed to load",
            attributes = mapOf(
                "slot" to slotName,
                "ad_unit_id" to adUnitId,
                "error_code" to errorCode.toString(),
                "error_message" to errorMessage,
                "no_fill" to isNoFill.toString(),
            ),
        )

        if (!isNoFill) {
            firebaseController.recordNonFatal(
                throwable = AdSlotLoadException(
                    slotName = slotName,
                    adUnitId = adUnitId,
                    errorCode = errorCode,
                    errorMessage = errorMessage,
                ),
            )
        }
    }

    /**
     * Records that the request was never made, which is always a defect on our side.
     *
     * The usual cause is a slot asking before `MobileAds.initialize` has run. Unlike no fill there is
     * no legitimate reason for it, so it is a non-fatal in every build.
     */
    fun onAdRequestNotStarted(
        slotName: String,
        adUnitId: String,
        throwable: Throwable? = null,
    ) {
        Log.w(LOG_TAG, "Ad slot '$slotName' could not request unit '$adUnitId'.", throwable)

        firebaseController.recordNonFatal(
            throwable = throwable ?: AdSlotNotRequestedException(
                slotName = slotName,
                adUnitId = adUnitId,
            ),
        )
    }

    /** True when an empty slot should explain itself on screen. Debug builds only. */
    val showsDebugPlaceholder: Boolean get() = buildInfoProvider.isDebugBuild

    private companion object {
        const val LOG_TAG: String = "AdSlot"

        /**
         * The `LoadAdError.ErrorCode` name that means no ad was available.
         *
         * Matched by name rather than by enum constant so the toolkit does not break when the SDK
         * adds or renames codes, and matched loosely because the value arrives already stringified.
         */
        const val NO_FILL_CODE: String = "NO_FILL"
    }
}

/** Non-fatal marker for an ad request the SDK answered with an error other than no fill. */
class AdSlotLoadException(
    slotName: String,
    adUnitId: String,
    errorCode: String,
    errorMessage: String,
) : Exception("Ad slot '$slotName' failed for unit '$adUnitId': code=$errorCode ($errorMessage)")

/** Non-fatal marker for an ad request that was never made. */
class AdSlotNotRequestedException(
    slotName: String,
    adUnitId: String,
) : Exception("Ad slot '$slotName' never requested unit '$adUnitId'.")
